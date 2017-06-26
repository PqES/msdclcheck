package main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import analyser.AccessAnalyser;
import analyser.ConstraintsAnalyser;
import entities.AccessDefinition;
import entities.ArchitecturalDrift;
import entities.MicroserviceDefinition;
import entities.ConstraintDefinition;
import inputManager.InputManager;

public class Main {
	public static void main(String[] args) {
		try {
			InputManager inputManager = new InputManager();
			inputManager.readFile(new File("constraints.txt"));
			
			//obtem Microserviços, DCL's e regras do arquivo
			Set<MicroserviceDefinition> allServices = inputManager.getAllServices();
			HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> constraintMap = inputManager.getServiceMap();
			HashMap<MicroserviceDefinition, String> dclMap = inputManager.getDclMap();
			
			//checa acesso dos microserviços
			HashMap<MicroserviceDefinition, Set<AccessDefinition>> accessMap = AccessAnalyser.getInstance().analyseAll(allServices);
			System.out.println("==== ACCESSES ====");
			for(Entry<MicroserviceDefinition, Set<AccessDefinition>> entry : accessMap.entrySet()){
				System.out.println(entry.getKey()+": "+entry.getValue());
			}
			
			//verifica violações
			Set<ArchitecturalDrift> drifts = ConstraintsAnalyser.getInstance().analyseConstraints(constraintMap, accessMap);
			System.out.println("==== DRIFTS =====");
			for(ArchitecturalDrift a : drifts){
				System.out.println(a.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	
		
		
			
	
	}

	
}
