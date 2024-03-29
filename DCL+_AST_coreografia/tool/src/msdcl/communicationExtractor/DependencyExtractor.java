package msdcl.communicationExtractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import msdcl.ast.MsDCLDependencyVisitor;
import msdcl.core.MicroserviceDefinition;
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
//		imprime();
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
		} else {
			System.out.println("This is not a file!");
		}

		return dependenciesAll;
	}

	

	
	
	

}
