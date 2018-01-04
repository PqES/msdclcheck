package msdcl.communicationExtractor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entities.MicroserviceDefinition;
import msdcl.ast.MsDCLDependencyVisitor;
import msdcl.dependencies.ClassAnnotationDependency;
import msdcl.dependencies.ClassNormalAnnotationDependency;
import msdcl.dependencies.FieldAnnotationDependency;
import msdcl.dependencies.MethodAnnotationDependency;
import msdcl.dependencies.MethodNormalAnnotationDependency;
import msdcl.exception.MsDCLException;
import util.Util;

public class DependencyExtractor {

	public final static DependencyExtractor instance = new DependencyExtractor();

	public DependencyExtractor() {
	}

	public static DependencyExtractor getInstance() {
		return instance;
	}

	//HashMap<String, Set> allMicrosserviceDependencies = new HashMap<>();

	public HashMap<String, Set> extractDependenciesFromService(MicroserviceDefinition caller)
			throws IOException, MsDCLException {
		HashMap<String, Set> allMicrosserviceDependencies = new HashMap<>();
		Set dependencies = new HashSet<>();
		List<File> javaFiles = Util.getAllFiles(new File(caller.getPath()));

		for (File f : javaFiles) {
			String fileName = f.getName();
			dependencies = this.extractDependenciesFromFiles(f);
			allMicrosserviceDependencies.put(fileName, dependencies);
		}
//		imprime();
		return allMicrosserviceDependencies;
	}

	public Set extractDependenciesFromFiles(File f) throws IOException, MsDCLException {

		Set dependenciesAll = new HashSet<>();
		Set dependencies = new HashSet<>();
		MsDCLDependencyVisitor visitor = new MsDCLDependencyVisitor();
		String filePath = f.getAbsolutePath();
		filePath = f.getAbsolutePath();
		String className = f.getName();
		
		if (f.isFile()) {
			String service = Util.readFileToCharArray(filePath);
			visitor = new MsDCLDependencyVisitor(className, service);
			dependenciesAll = visitor.getAllDependenciesOfFile();
			//imprime(dependenciesAll);
			// dependencies = verifyAutowired(dependenciesAll);
		} else {
			System.out.println("This is not a file!");
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

		for (Object d : dependencies) {
			System.out.println(d.toString());
		}
		return dependencies;

	}

	public void imprime(HashMap<String, Set> allMicrosserviceDependencies) {

		for (String s : allMicrosserviceDependencies.keySet()) {
			System.out.println("A classe:  " + s);
			for (Object d : allMicrosserviceDependencies.get(s)) {

				if (d instanceof FieldAnnotationDependency) {
					// if (((FieldNormalAnnotationDependency)
					// d).getNameClass2().equals("Autowired")) {
					// extractCommunicationsFromZull(((FieldAnnotationDependency)
					// d).getDeclaration(),
					// dependencies);
					System.out.print("[ " + ((FieldAnnotationDependency) d).getNameClass2() + "] ->>");
					// System.out.println();
					// }

				} else if (d instanceof ClassAnnotationDependency) {
					// if (((ClassNormalAnnotationDependency)
					// d).getNameClass2().equals("FeignClient")) {

					//System.out.print("[ " + ((ClassAnnotationDependency) d).getNameClass2() + "] ->>");
					// System.out.println();
					// }

				} else if (d instanceof MethodAnnotationDependency) {
					// System.out.println("eh metoodo? ");
					//System.out.print("[ " + ((MethodAnnotationDependency) d).getNameClass2() + "] ->>");

				}

			}
			System.out.println();
		}
	}
	public void imprime3(HashMap<String, Set> allMicrosserviceDependencies) {

		for (String s : allMicrosserviceDependencies.keySet()) {
			System.out.println("A classe:  " + s);
			for (Object d : allMicrosserviceDependencies.get(s)) {

				if (d instanceof FieldAnnotationDependency) {
					// if (((FieldNormalAnnotationDependency)
					// d).getNameClass2().equals("Autowired")) {
					// extractCommunicationsFromZull(((FieldAnnotationDependency)
					// d).getDeclaration(),
					// dependencies);
					System.out.print("[ " + ((FieldAnnotationDependency) d).getNameClass2() + "] ->>");
					// System.out.println();
					// }

				} else if (d instanceof ClassAnnotationDependency) {
					// if (((ClassNormalAnnotationDependency)
					// d).getNameClass2().equals("FeignClient")) {

					System.out.print("[ " + ((ClassAnnotationDependency) d).getNameClass2() + "] ->>");
					// System.out.println();
					// }

				} else if (d instanceof MethodAnnotationDependency) {
					// System.out.println("eh metoodo? ");
					System.out.print("[ " + ((MethodAnnotationDependency) d).getNameClass2() + "] ->>");

				}

			}
			System.out.println();
		}
	}


	public void imprime(Set allFiles) {

		for (Object d : allFiles) {

			if (d instanceof FieldAnnotationDependency) {
				// if (((AnnotationDependency) d).getNameClass2().equals("Autowired")) {
				// extractCommunicationsFromZull(((FieldAnnotationDependency)
				// d).getDeclaration(),
				// dependencies);
				// System.out.println(((FieldAnnotationDependency) d).getNameClass1() + "");
				System.out.println(((FieldAnnotationDependency) d).toString());
				System.out.println();
				// }

			} else if (d instanceof ClassNormalAnnotationDependency) {
				// if (((ClassNormalAnnotationDependency)
				// d).getNameClass2().equals("FeignClient")) {
			//	System.out.println(((ClassNormalAnnotationDependency) d).toString());
				System.out.println();
				// }

			} else if (d instanceof MethodNormalAnnotationDependency) {

			//	System.out.println(((MethodNormalAnnotationDependency) d).toString());
				System.out.println();
			}

		}

	}

}
