package communicationExtractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.MsDCLDependencyVisitor;
import core.MicroserviceDefinition;
import dependencies.ClassAnnotationDependency;
import dependencies.ClassNormalAnnotationDependency;
import dependencies.FieldAnnotationDependency;
import dependencies.MethodAnnotationDependency;
import dependencies.MethodNormalAnnotationDependency;
import exception.MsDCLException;
import util.Util;


public class DependencyExtractor {

	public final static DependencyExtractor instance = new DependencyExtractor();

	public DependencyExtractor() {
	}

	public static DependencyExtractor getInstance() {
		return instance; 
	}


	public HashMap<String, ArrayList> extractDependenciesFromService(MicroserviceDefinition caller)
			throws IOException, MsDCLException {
		HashMap<String, ArrayList> allMicrosserviceDependencies = new HashMap<>();
		ArrayList dependencies = new ArrayList<>();
		List<File> javaFiles = Util.getAllFiles(new File(caller.getPath()));

		for (File f : javaFiles) {
			String fileName = f.getName();
			dependencies = this.extractDependenciesFromFiles(f);
			allMicrosserviceDependencies.put(fileName, dependencies); 
		}
		return allMicrosserviceDependencies;
	}

	public ArrayList extractDependenciesFromFiles(File f) throws IOException, MsDCLException {

		ArrayList dependenciesAll = new ArrayList<>();
		Set dependencies = new HashSet<>();
		MsDCLDependencyVisitor visitor = new MsDCLDependencyVisitor();
		String filePath = f.getAbsolutePath();
		filePath = f.getAbsolutePath();
		String className = f.getName();
		if (f.isFile()) {
			String service = Util.readFileToCharArray(filePath);
			visitor = new MsDCLDependencyVisitor(className, service); 
			dependenciesAll = visitor.getAllDependenciesOfFile();
		} 

		return dependenciesAll;
	}

	public Set verifyAutowired(Set dependenciesAll) {
		Set dependencies = new HashSet<>();
		for (Object dep : dependenciesAll) { 
			if ((dep instanceof FieldAnnotationDependency)) {

				if (((FieldAnnotationDependency) dep).getNameClass2().equals("Autowired")) {
					if (!dependencies.contains(((FieldAnnotationDependency) dep).getNameClass1())) {
						dependencies.add(dep);
					}
				}
			}

		}

		return dependencies;

	}


}
