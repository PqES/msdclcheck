package msdcl.communicationExtractor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import msdcl.core.CommunicateDefinition;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;
import msdcl.dependencies.AnnotationDependency;
import msdcl.dependencies.ClassNormalAnnotationDependency;
import msdcl.dependencies.ClassSingleAnnotationDependency;
import msdcl.dependencies.FieldAnnotationDependency;
import msdcl.dependencies.MethodNormalAnnotationDependency;
import msdcl.exception.MsDCLException;
import util.Util;

public class CommunicationExtractor {

	private final static CommunicationExtractor instance = new CommunicationExtractor();
	
	HashMap<String, ArrayList> dependenciesFromService = new HashMap<>();

	public CommunicationExtractor() {
	}

	public static CommunicationExtractor getInstance() {
 
		return instance;
	}



	public Set<CommunicateDefinition> extractCommunicationsFromZull(String declaration,
			Set<CommunicateDefinition> communications, MicroserviceDefinition caller, String classe) {
		String declaration2 = declaration + ".java";
		ArrayList dependencies = new ArrayList<>();

		dependencies = dependenciesFromService.get(declaration2);

		CommunicateDefinition communication = null;
		String msName = null;
		String using = null;
		for (Object dep : dependencies) { 
			if(dep instanceof ClassSingleAnnotationDependency || dep instanceof ClassNormalAnnotationDependency ) {
				msName = getDependencyClass(dep);
			}
			else if (dep instanceof MethodNormalAnnotationDependency) {
				using = ((MethodNormalAnnotationDependency) dep).getValue(); 
				communication = new CommunicateDefinition(caller.getName(), msName, using);
				if (communication != null) {
					
					
					communications.add(communication);
				}
			}
		
		}
		return communications;

	}


	public String getDependencyClass(Object dep) {
		String msName = null;
		if (dep instanceof ClassSingleAnnotationDependency ) {
			if (((ClassSingleAnnotationDependency) dep).getNameClass2().equals("FeignClient")) {
				msName = ((ClassSingleAnnotationDependency) dep).getExpression();
//				System.out.println("MsName: " + msName);
			}
			return msName;
		
		}
		else if(dep instanceof ClassNormalAnnotationDependency ) {
			if (((ClassNormalAnnotationDependency) dep).getNameClass2().equals("FeignClient")) {
				msName = ((ClassNormalAnnotationDependency) dep).getValue();
			}
			return msName;
		
		}
		return null;
}

	public boolean verifyDependency(String declaration2) {
		if (dependenciesFromService.get(declaration2) == null)
			return false;
		return true;

	}

	public boolean verifyCommunicationsByAnnotations(ArrayList dependenciesFile, Set<CommunicateDefinition> communications,
			MicroserviceDefinition caller, String classe) {
		for (Object dep : dependenciesFile) {
			if (dep instanceof FieldAnnotationDependency) {
				if (((AnnotationDependency) dep).getNameClass2().equals("Autowired")) {
					if(verifyDependency(((FieldAnnotationDependency) dep).getDeclaration() +".java")) {
						extractCommunicationsFromZull(((FieldAnnotationDependency) dep).getDeclaration(), communications,
							caller, classe);
						return true;
					}
					
				}
			}
		}

		return false;
	}

	public Set<CommunicateDefinition> extractCommunicationsFromService(MicroserviceDefinition caller,
			MicroservicesSystem system) throws IOException, MsDCLException {
		Set<CommunicateDefinition> communications = new HashSet<>();

		ArrayList dependencies = new ArrayList<>();
		// vem todas as dependências das classes do microsserviço analisado.

		List<File> javaFiles = Util.getAllFiles(new File(caller.getPath()));
		for (File f : javaFiles) {
			String fileName = f.getName();

			dependencies = DependencyExtractor.getInstance().extractDependenciesFromFiles(f);
			verifyCommunicationsByAnnotations(dependencies, communications, caller, fileName);

		}

		return communications;
	}
	
	public HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> analyseAll(MicroservicesSystem system)
			throws IOException, MsDCLException {

		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> map = new HashMap<>();
		
		for (MicroserviceDefinition caller : system.getMicroservices()) {
			JavaDepExtractor.getInstance().extractDependenciesFromEachMicroservice(caller);
			Set<CommunicateDefinition> accesses = new HashSet<>();
			dependenciesFromService = DependencyExtractor.getInstance().extractDependenciesFromService(caller);
			accesses.addAll(this.extractCommunicationsFromService(caller, system));
			map.put(caller, accesses);
			this.dependenciesFromService.clear();
		}
		return map;
	}
}
