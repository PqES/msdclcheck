package main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import communicationChecker.CommunicationChecker;
import communicationChecker.drift.ArchitecturalDrift;
import entities.MicroserviceDefinition;
import entities.MicroservicesSystem;
import enums.Constraint;
import fileManager.InputManager;
import fileManager.OutputManager;
import msdcl.communicationExtractor.CommunicationExtractor;
import msdcl.communicationVerify.Pi_DCL;
import msdcl.exception.MsDCLException;
import entities.CommunicateDefinition;
import entities.ConstraintDefinition;
import pidclcheck.exception.ParseException;

public class Main {
	public static void main(String[] args) throws MsDCLException {
		try {
			InputManager inputManager = new InputManager(); 
			MicroservicesSystem system = inputManager.readFile(new File("constraints.txt"));

			// checa acesso dos microserviços
			system.setCommunications(CommunicationExtractor.getInstance().analyseAll(system)); 
			 
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
		//	Pi_DCL.validateLocalArchitecture(mapDcl);

//		} catch (ParseException e) {
//			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
