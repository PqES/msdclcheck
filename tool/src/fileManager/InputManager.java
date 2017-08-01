package fileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import entities.ConstraintDefinition;
import entities.MicroserviceDefinition;
import enums.Constraint;
import util.RulesRegex;

public class InputManager {
	
	//retornos
	private HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> serviceMap = new HashMap<>();
	private HashMap<MicroserviceDefinition, String> dclMap = new HashMap<>();
	
	public InputManager() {

	}
	
	public Constraint findConstraint(String constraint) {
		for (Constraint c : Constraint.values()) {
			if (c.getValue().equals(constraint)) {
				return c;
			}
		}
		return null;
	}
	
	private MicroserviceDefinition getMicroserviceDefinitionByName(String name){
		for(MicroserviceDefinition m : serviceMap.keySet()){
			if(m.getName().equals(name)){
				return m;
			}
		}
		return null;
	}
	
	private void addMicroservice(MicroserviceDefinition microservice){
		serviceMap.put(microservice, new HashSet<>());
		dclMap.put(microservice, "");
	}
	
	public void addConstraint(String[] leftServices, String[] rightServices, String[] usings, Constraint constraint) {
		for(String leftService : leftServices){
			MicroserviceDefinition service = getMicroserviceDefinitionByName(leftService);
			Set<ConstraintDefinition> constraints = serviceMap.get(service);
			for(String rightService : rightServices){
				if(usings == null){
					constraints.add(new ConstraintDefinition(leftService, constraint, rightService));
				}else{
					for(String using : usings){
						ConstraintDefinition c = new ConstraintDefinition(leftService, constraint, rightService, using);
						constraints.add(c);
					}
				}
			}
		}
	}
	
	private void buildConstraint(String tokens[]){
		String constraintName = "";
		int indexOfConstraintName = -1;
		int indexOfLeftServices = -1;
		int indexOfRightServices = -1;
		int indexOfUsing = tokens.length;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].contains("-")) {
				indexOfConstraintName = i;
				indexOfRightServices = i + 1;
				if (tokens[0].equals("only")) {
					indexOfLeftServices = 1;
					constraintName = "only-"+tokens[i];
				}else{
					indexOfLeftServices = 0;
					constraintName = tokens[i];
				}
			}else if(tokens[i].equalsIgnoreCase("using")){
				indexOfUsing = i;
			}
		}
		String leftServices[] = Arrays.copyOfRange(tokens, indexOfLeftServices, indexOfConstraintName);
		String rightServices[] = Arrays.copyOfRange(tokens, indexOfRightServices, indexOfUsing);
		String usings[] = null;
		if(indexOfUsing + 1 < tokens.length){
			usings = Arrays.copyOfRange(tokens, indexOfUsing + 1, tokens.length);
		}
		Constraint constraint = findConstraint(constraintName);
		addConstraint(leftServices, rightServices, usings, constraint);
	}
	
	public void readFile(File f) throws IOException {
		FileReader file = new FileReader(f);
		BufferedReader buffer = new BufferedReader(file);
		String line;
		MicroserviceDefinition currentService = null;
		while (buffer.ready()) {
			line = buffer.readLine();
			if (line.matches(RulesRegex.MICROSERVICE_REGEX)) {
				String tokens[] = line.split(RulesRegex.MICROSERVICE_TOKENS);
				String name = tokens[0];
				String link = tokens[1];
				String path = tokens[2];
				String language = tokens[3];
				currentService = new MicroserviceDefinition(name, link, path, language);
				addMicroservice(currentService);
			}else if(line.matches(RulesRegex.DCL_REGEX)){
				String dcl = line.replaceAll("\t", "");
				String dclContent = dclMap.get(currentService); 
				dclMap.put(currentService, dclContent + dcl + '\n');
			}else if(line.matches(RulesRegex.CONSTRAINT_REGEX)){
				String tokens[] = line.split(RulesRegex.CONSTRAINT_TOKENS);
				buildConstraint(tokens);
			}else{
				System.out.println("line error");
			}
		}

		buffer.close();
		file.close();
	}


	public HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> getServiceMap(){
		return this.serviceMap;
	}
	
	public HashMap<MicroserviceDefinition, String> getDclMap(){
		return this.dclMap;
	}
	
	public Set<MicroserviceDefinition> getAllServices(){
		return this.serviceMap.keySet();
	}

}
