package entities;

public class RulesRegex {

	String patternConstraint = "(only)?(\\w+)((-)(\\w+)*)*";
	String patternMicroservice = "(\\w+)(\\s*)(,\\s*\\w+\\s*)*:?";
	String optionalSpacesRegex = "(\\s*)";	
	final String link = ".*;?";
	final String source = "(/).*;?";
	final String language = "(\\w+)";

	String microserviceDefinition = patternMicroservice + optionalSpacesRegex + link + optionalSpacesRegex + source
			+ optionalSpacesRegex + language;

	String constraints = patternConstraint + optionalSpacesRegex + patternMicroservice + patternConstraint
			+ optionalSpacesRegex + patternMicroservice;

	public RulesRegex() {

	}

	public String getConstraints() {
		return constraints;
	}

	public String getMicroserviceDefinition() {
		return microserviceDefinition;
	}

	public String getPatternConstraint() {
		return patternConstraint;
	}

	public String getpatternMicroservice() {
		return patternMicroservice;
	}

	public String getOptionalSpacesRegex() {
		return optionalSpacesRegex;
	}

	

}
