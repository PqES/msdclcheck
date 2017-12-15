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
import entities.MicroservicesSystem;
import enums.Constraint;
import util.RulesRegex;

public class InputManager {
	
	
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
	
	public Set<ConstraintDefinition> generateConstraints(String[] leftServices, String[] rightServices, String[] usings, Constraint constraint) {
		Set<ConstraintDefinition> constraints = new HashSet<>();
		for(String leftService : leftServices){
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
		return constraints;
	}
	
	private Set<ConstraintDefinition> extractConstraints(String tokens[]){
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
		return generateConstraints(leftServices, rightServices, usings, constraint);
	}
	
	public MicroservicesSystem readFile(File f) throws IOException {
		MicroservicesSystem system = new MicroservicesSystem();
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
				system.addMicroservice(currentService);
			}else if(line.matches(RulesRegex.DCL_REGEX)){
				String dcl = line.replaceAll("\t", "");
				system.addDcl(currentService, dcl);
			}else if(line.matches(RulesRegex.CONSTRAINT_REGEX)){
				String tokens[] = line.split(RulesRegex.CONSTRAINT_TOKENS);
				Set<ConstraintDefinition> constraints = extractConstraints(tokens);
				system.addConstraints(constraints);
			}else{
				System.out.println("line error");
			}
		}

		buffer.close();
		file.close();
		return system;
	}
}
