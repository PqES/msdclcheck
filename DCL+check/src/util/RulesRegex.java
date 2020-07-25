package util;

public class RulesRegex {

	
	public static final String MICROSERVICE_REGEX = "(\\w+)(.+):(\\s+)(http://)(.+);(\\s+)(.+);(\\s+)(\\w+)(.+)";
	public static final String MICROSERVICE_TOKENS = "(:(\\s+))|;(\\s+)";
	public static final String DCL_REGEX = "^\\t(.+)";
	public static final String COMMUNICATIONS_REGEX = "\\t(only)?\\s*(\\w+)(,(\\s(\\w+))*)*\\s((can|cannot|must)-communicate(-only)?)(\\s*)((\\w+)-?(\\w+))(,(\\s*)(\\w+)(\\s*))*(\\s*)(using(\\s*)(\\/)?(\\$?(.)+))?";
	public static final String URL_REGEX= "(http:\\/\\/)(\\w+)(\\/)";
	public static final String CONSTRAINT_TOKENS = "(((\\s*),(\\s*))|(\\s+))";
	public static final String STRUCTURAL_VIOLATION = "\\[(\\w+)\\]";
	public static final String COMM_MODULE = "(\t)*(commModule).*";

}
