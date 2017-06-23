package analyser;

import java.util.HashMap;
import java.util.Set;

import entities.AccessDefinition;
import entities.MicroserviceDefinition;

public class RulesAnalyser {

	private final static RulesAnalyser instance = new RulesAnalyser();
	
	public RulesAnalyser(){
		
	}
	
	public static RulesAnalyser getInstance(){
		return instance;
	}

	public void analyseRules(HashMap<MicroserviceDefinition, Set<AccessDefinition>> accessMap) {
		//analisa as regras
	}

	
	
}
