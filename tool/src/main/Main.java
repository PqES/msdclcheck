package main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import analyser.AccessAnalyser;
import analyser.ConstraintsAnalyser;
import analyser.Pi_DCL;
import entities.CommunicateDefinition;
import entities.ArchitecturalDrift;
import entities.MicroserviceDefinition;
import entities.ConstraintDefinition;
import inputManager.InputManager;
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
			HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> accessMap = AccessAnalyser.getInstance()
					.analyseAll(allServices);
			System.out.println("==== ACCESSES ====");
			for (Entry<MicroserviceDefinition, Set<CommunicateDefinition>> entry : accessMap.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}

			// verifica violações
			Set<ArchitecturalDrift> drifts = ConstraintsAnalyser.getInstance().analyseConstraints(constraintMap,
					accessMap);
			System.out.println("==== DRIFTS =====");
			for (ArchitecturalDrift a : drifts) {
				System.out.println(a.getMessage());
			}

			Pi_DCL.validateLocalArchitecture(mapDcl);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
