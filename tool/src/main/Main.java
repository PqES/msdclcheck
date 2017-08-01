package main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import communicationAnalyser.CommunicationAnalyser;
import communicationAnalyser.drift.ArchitecturalDrift;
import communicationExtractor.CommunicateDefinition;
import communicationExtractor.CommunicationExtractor;
import communicationExtractor.Pi_DCL;
import entities.MicroserviceDefinition;
import enums.Constraint;
import fileManager.InputManager;
import fileManager.OutputManager;
import entities.ConstraintDefinition;
import pidclcheck.exception.ParseException;

public class Main {
	public static void main(String[] args) {
		try {
			InputManager inputManager = new InputManager();
			inputManager.readFile(new File("constraints.txt"));

			// obtem Microserviços, DCL's e regras do arquivo
			Set<MicroserviceDefinition> allServices = inputManager.getAllServices();
			HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> constraintMap = inputManager.getServiceMap();
			HashMap<MicroserviceDefinition, String> mapDcl = inputManager.getDclMap();

			// checa acesso dos microserviços
			HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> accessMap = CommunicationExtractor.getInstance()
					.analyseAll(allServices);
			System.out.println("==== ACCESSES ====");
			for (Entry<MicroserviceDefinition, Set<CommunicateDefinition>> entry : accessMap.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
			// verifica violações
			Set<ArchitecturalDrift> drifts = CommunicationAnalyser.getInstance().analyseCommunications(constraintMap,
					accessMap);
			System.out.println("==== DRIFTS =====");
			for (ArchitecturalDrift a : drifts) {
				System.out.println(a.getMessage());
			}
			OutputManager output = new OutputManager();
			output.violates(drifts);

			//Pi_DCL.validateLocalArchitecture(mapDcl);

		//} catch (ParseException e) {
			//e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
