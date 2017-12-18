package msdcl.communicationExtractor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entities.CommunicateDefinition;
import entities.MicroserviceDefinition;
import entities.MicroservicesSystem;
import msdcl.ast.MsDCLDependencyVisitor;
import msdcl.dependencies.AnnotationDependency;
import msdcl.dependencies.ClassNormalAnnotationDependency;
import msdcl.dependencies.FieldAnnotationDependency;
import msdcl.dependencies.MemberPair;
import msdcl.exception.MsDCLException;
import util.Util;

public class DependencyExtractor {

	public final static DependencyExtractor instance = new DependencyExtractor();

	public DependencyExtractor() {
	}

	public static DependencyExtractor getInstance() {
		return instance;
	}
	
	

	public HashMap<String, Set> extractDependenciesFromService(MicroserviceDefinition caller) throws IOException, MsDCLException {

		HashMap<String, Set> allMicrosserviceDependencies = new HashMap<>();
		Set dependencies = new HashSet<>();
		List<File> javaFiles = Util.getAllFiles(new File(caller.getPath()));

		for (File f : javaFiles) {
			String fileName = f.getName();
			dependencies = this.extractDependenciesFromFiles(f, caller);
			allMicrosserviceDependencies.put(fileName, dependencies);
		}


		return allMicrosserviceDependencies;
	}
	
	public Set extractDependenciesFromFiles(File f, MicroserviceDefinition caller)
			throws IOException, MsDCLException {

		Set dependencies = new HashSet<>();
		MsDCLDependencyVisitor visitor = new MsDCLDependencyVisitor();
		String filePath = f.getAbsolutePath();
		filePath = f.getAbsolutePath();
		String fileName = f.getName();
		if (f.isFile()) {
			String service = Util.readFileToCharArray(filePath);
			visitor = new MsDCLDependencyVisitor(fileName, service);
			dependencies = visitor.getDependencies2();

		} else {
			System.out.println("This is not a file!");
		}

		return dependencies;
	}

	public void imprime(HashMap<String, Set> allFiles) {

		for (String s : allFiles.keySet()) {
			for (Object d : allFiles.get(s)) {

				if (d instanceof FieldAnnotationDependency) {
					if (((AnnotationDependency) d).getNameClass2().equals("Autowired")) {
						// extractCommunicationsFromZull(((FieldAnnotationDependency)
						// d).getDeclaration(),
						// dependencies);
						System.out.println(((FieldAnnotationDependency) d).toString());
						System.out.println();
					}

				} else if (d instanceof ClassNormalAnnotationDependency) {
					if (((ClassNormalAnnotationDependency) d).getNameClass2().equals("FeignClient")) {
						System.out.println(((ClassNormalAnnotationDependency) d).toString());
						System.out.println();
					}

				}

			}
		}
	}

}
