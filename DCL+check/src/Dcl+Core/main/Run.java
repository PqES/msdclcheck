package msdcl.main;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import communicationChecker.ArchitecturalDrift;
import communicationChecker.CommunicationChecker;
import communicationExtractor.CommunicationExtractor;
import communicationExtractor.JavaDepExtractor;
import fileManager.InputManager;
import fileManager.OutputManager;
import msdcl.core.CommunicateDefinition;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;
import msdcl.core.ModuleDefinition;
import msdcl.core.ModuleDependencies;
import msdcl.exception.MsDCLException;
import pidclcheck.exception.ParseException;
import structuralChecker.Pi_DCL;
import visualization.MatrizVisualization;

public class Run {
	public static void main(String[] args)
			throws MsDCLException, ParseException, IOException, InstantiationException, IllegalAccessException, InterruptedException {
		try {
			// if (args.length != 1){
			// System.out.println("Usage:\n msdclcheck [architecturalSpecification]
			// [folder-dir]");
			// return;
			// }
			//
			// sString architecturalSpecification = args[0];

			// String folderDirName = args[1];
			// System.out.println("Folder Dir: " + architecturalSpecification);
			//
			String constraints = "constraints.txt";
		//	String communicates = "communicates.txt";
			// //String pathViolated = args[1];
			//
			MicroservicesSystem system = getConstraints(constraints);
	//		JavaDepExtractor.getInstance().getJavaDepExtractor(system);
			//
//			 extrai os acessos dos microserviços
	//		system.setCommunications(getCommunications(communicates, system));
//			//
//////
//			HashMap<ModuleDefinition, Set<CommunicateDefinition>> communications = CommunicationExtractor
//					.getInstance().analyseAllMicroservice(system,communicates);

//			for (MicroserviceDefinition m : communications.keySet()) {
//				for (CommunicateDefinition c : communications.get(m)) {
//					System.out.println(c.getMicroserviceOrigin() + "("+c.getPathMsOrigin()+")"+ " communicate  "+ c.getMicroserviceDestin()
//							+ " using " + c.getUsing());
//				}
//			}
				//system.setCommunications(communications);
			
			// // verifica violações de comunicação
//			Map<ModuleDefinition, Set<ArchitecturalDrift>> driftsCommunications = CommunicationChecker
//					.getInstance().check(system);

			 // verifica violações de projeto estrutural
			 Map<MicroserviceDefinition,
			 Collection<pidclcheck.core.DependencyConstraint.ArchitecturalDrift>>
			 driftsStructurals = Pi_DCL.getInstance().validateLocalArchitectures(system); 
			 
			// System.out.println(MatrizVisualization.getInstance().createHtmlTable(system));
		
			 
			
			 
			 
			 // gera o arquivo com as violações
			
 // 			 OutputManager.violateCommunicates( driftsCommunications, system);
  			OutputManager.violateStructural( driftsStructurals, system);
  		//	 generateHtmlMatrizStructural(system);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// path da especificação arquitetural
	public static MicroservicesSystem getConstraints(String architecturalSpecification) throws IOException {

		InputManager inputManager = new InputManager();
		MicroservicesSystem system = inputManager.readFileConstraints(new File(architecturalSpecification));
		return system;
	}
	
	public static HashMap<ModuleDefinition, Set<CommunicateDefinition>> getCommunications(String communicates, MicroservicesSystem system) throws IOException {

		InputManager inputManager = new InputManager();
		HashMap<ModuleDefinition, Set<CommunicateDefinition>> communications = inputManager.readFileCommunications(new File(communicates), system);
		return communications;
	}
	
		

	
}
