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
import entities.MicroserviceDefinition;
import entities.ConstraintDefinition;
import inputManager.InputManager;

public class Main {
	public static void main(String[] args) throws IOException {
	
		
		File arquivo = new File("/home/elena/Documents/Mestrado/Artigo/msdclcheck/constraints.txt");
		
		InputManager i = new InputManager();
		i.readFile(arquivo);
		
		System.out.println("\n");
		
		try {
			HashMap<MicroserviceDefinition, Set<AccessDefinition>> accessMap = AccessAnalyser.getInstance().analyseAll(i.getMicroservice());
			HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> constraintMap = i.getMap();
			i.printHash();
			System.out.println("\n");
			for(Entry<MicroserviceDefinition, Set<AccessDefinition>> e : accessMap.entrySet()){
				System.out.println(e.getKey()+" "+e.getValue());
			}
			System.out.println("\n");
						
			ConstraintsAnalyser.getInstance().analyseConstraints(constraintMap, accessMap); //analisador das regras, retorna um conjunto de acessos inválidos
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	
		
		
			
	
	}

	
}
