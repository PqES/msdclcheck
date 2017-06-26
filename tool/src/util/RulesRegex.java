package util;

public class RulesRegex {

	
	public static final String MICROSERVICE_REGEX = "(\\w+):(\\s+)(http://)(.+);(\\s+)(.+);(\\s+)(\\w+)";
	public static final String MICROSERVICE_TOKENS = "(:(\\s+))|;(\\s+)";
	public static final String DCL_REGEX = "^\\t(.+)";
	private static final String PARTIAL_CONSTRAINT_SERVICE_NAME = "(\\s*)(\\w+)(\\s*)(,(\\s*)(\\w+)(\\s*))*";
	private static final String PARTIAL_CONSTRAINT_TYPES = "((can|cannot|must)-communicate(-only)?)";
	public static final String CONSTRAINT_REGEX = "(only)?" + PARTIAL_CONSTRAINT_SERVICE_NAME + PARTIAL_CONSTRAINT_TYPES
			+ PARTIAL_CONSTRAINT_SERVICE_NAME;
	public static final String CONSTRAINT_TOKENS = "(((\\s*),(\\s*))|(\\s+))";

}
