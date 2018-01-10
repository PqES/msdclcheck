package main;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import communicationChecker.CommunicationChecker;
import communicationChecker.drift.ArchitecturalDrift;
import entities.MicroserviceDefinition;
import entities.MicroservicesSystem;
import fileManager.InputManager;
import fileManager.OutputManager;
import msdcl.communicationExtractor.CommunicationExtractor;
import msdcl.communicationVerify.Pi_DCL;
import msdcl.exception.MsDCLException;
import pidclcheck.exception.ParseException;

public class Main {
	public static void main(String[] args) throws MsDCLException, ParseException {
		try {
			InputManager inputManager = new InputManager(); 
			MicroservicesSystem system = inputManager.readFile(new File("constraints.txt"));

			// checa acesso dos microserviços
			system.setCommunications(CommunicationExtractor.getInstance().analyseAll(system)); 
			//HashMap<MicroserviceDefinition, ByteArrayInputStream> dcl = CommunicationExtractor.getInstance().getDependenciesDCL(system);
			System.out.println("==== ACCESSES ====");
			for (MicroserviceDefinition ms : system.getMicroservices()) { 
				System.out.println(ms + ": " + system.getCommunications(ms));
			} 

			// verifica violações
			Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(system);
			System.out.println("==== DRIFTS =====");
			for (ArchitecturalDrift a : drifts) {
				System.out.println(a.getMessage());
			}
			System.out.println("=================");
			OutputManager output = new OutputManager();
			output.violates(drifts);
			
			System.out.println("==== DCLs ====");
			Pi_DCL.getInstance().validateLocalArchitectures(system);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void getConstraints(String path) throws IOException {
		
		InputManager inputManager = new InputManager(); 
		MicroservicesSystem system = inputManager.readFile(new File(path));
	}

}
