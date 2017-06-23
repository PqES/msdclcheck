package entities;

public class RuleDefinition {

	private String microserviceOrigin;
	private String microserviceDestin;
	private String ruleType;
	
	
	public RuleDefinition(String microserviceOrigin, String microserviceDestin, String ruleType){
		this.microserviceOrigin = microserviceOrigin;
		this.microserviceDestin = microserviceDestin;
		this.ruleType = ruleType;
	}
	
	public String getMicroserviceOrigin(){
		return this.microserviceOrigin;
	}
	
	public String getMicroserviceDestin(){
		return this.microserviceDestin;
	}
	
	public String getRuleType(){
		return this.ruleType;
	}
}
