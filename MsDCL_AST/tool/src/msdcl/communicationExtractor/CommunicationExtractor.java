package msdcl.communicationExtractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entities.CommunicateDefinition;
import entities.MicroserviceDefinition;
import entities.MicroservicesSystem;
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



	private Set<CommunicateDefinition> extractCommunicationsFromZull(String declaration,
			Set<CommunicateDefinition> communications, MicroserviceDefinition caller, String classe) {
		String declaration2 = declaration + ".java";
		ArrayList dependencies = new ArrayList<>();

		dependencies = dependenciesFromService.get(declaration2);

		CommunicateDefinition communication = null;

//		System.out.println("quem foi chamado:  " + declaration);

		String msName = null;
		String using = null;
		for (Object dep : dependencies) { 
//			System.out.println("Dep: " + dep.toString());
			if(dep instanceof ClassSingleAnnotationDependency || dep instanceof ClassNormalAnnotationDependency ) {
				msName = getDependencyClass(dep);
			}
			else if (dep instanceof MethodNormalAnnotationDependency) {
				using = ((MethodNormalAnnotationDependency) dep).getValue(); 
//				System.out.println("using: " + using);
				communication = new CommunicateDefinition(caller.getName(), msName, using);
				if (communication != null) {
					
//					System.out.println(" ORIGEM: " + communication.getMicroserviceOrigin() 
//					+ "  DESTINO: " + communication.getMicroserviceDestin() + 
//					"  USING:  "+communication.getUsing() + 
//					" QUEM DECLAROU: " + declaration);
					
					communications.add(communication);
				}
			}
		
		}
		return communications;

	}


	private String getDependencyClass(Object dep) {
		String msName = null;
		if (dep instanceof ClassSingleAnnotationDependency ) {
			if (((ClassSingleAnnotationDependency) dep).getNameClass2().equals("FeignClient")) {
//				System.out.println("eh SingleClass???" );
				msName = ((ClassSingleAnnotationDependency) dep).getExpression();
//				System.out.println("MsName: " + msName);
			}
			return msName;
		
		}
		else if(dep instanceof ClassNormalAnnotationDependency ) {
			if (((ClassNormalAnnotationDependency) dep).getNameClass2().equals("FeignClient")) {
//				System.out.println("eh SingleClass???" );
				msName = ((ClassNormalAnnotationDependency) dep).getValue();
//				System.out.println("MsName: " + msName);
			}
			return msName;
		
		}
		return null;
		
		
	
}

	private boolean verifyDependency(String declaration2) {
		if (dependenciesFromService.get(declaration2) == null)
			return false;
		return true;

	}

	private boolean verifyCommunicationsByAnnotations(ArrayList dependenciesFile, Set<CommunicateDefinition> communications,
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
			Set<CommunicateDefinition> accesses = new HashSet<>();
			dependenciesFromService = DependencyExtractor.getInstance().extractDependenciesFromService(caller);
			accesses.addAll(this.extractCommunicationsFromService(caller, system));
			map.put(caller, accesses);
			this.dependenciesFromService.clear();
		}
		return map;
	}
}
