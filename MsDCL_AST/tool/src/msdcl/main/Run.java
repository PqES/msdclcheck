package msdcl.main;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import fileManager.InputManager;
import fileManager.OutputManager;
import msdcl.communicationChecker.ArchitecturalDrift;
import msdcl.communicationChecker.CommunicationChecker;
import msdcl.communicationExtractor.CommunicationExtractor;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;
import msdcl.exception.MsDCLException;
import msdcl.structuralChecker.Pi_DCL;
import pidclcheck.exception.ParseException;

public class Run {
	public static void main(String[] args) throws MsDCLException, ParseException {
		try {
			if (args.length != 1){
				System.out.println("Usage:\n msdclcheck [architecturalSpecification] [folder-dir]");
				return;
			}
			
			String architecturalSpecification = args[0];
			
//			String folderDirName = args[1];		
		//	System.out.println("Folder Dir: " + architecturalSpecification);
//			
			//String path = "../toyexample/constraints.txt";
			//String pathViolated = args[1];
			
			MicroservicesSystem system  = getConstraints(architecturalSpecification);			
			
			// extrai os acessos dos microserviços
			system.setCommunications(CommunicationExtractor.getInstance().analyseAll(system)); 
			
			// verifica violações de comunicação 
			Set<ArchitecturalDrift> driftsCommunications = CommunicationChecker.getInstance().check(system);
			
			// verifica violações de projeto estrutural
			Map<MicroserviceDefinition, Collection<pidclcheck.core.DependencyConstraint.ArchitecturalDrift>> driftsStructurals  = Pi_DCL.getInstance().validateLocalArchitectures(system);
			
			// gera o arquivo com as violações
			OutputManager output = new OutputManager();
			
			output.violate("", driftsCommunications, driftsStructurals);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	// path da especificação arquitetural
	public static MicroservicesSystem getConstraints(String architecturalSpecification) throws IOException {
		
		InputManager inputManager = new InputManager(); 
		MicroservicesSystem system = inputManager.readFile(new File(architecturalSpecification));
		return system;
	}

}
