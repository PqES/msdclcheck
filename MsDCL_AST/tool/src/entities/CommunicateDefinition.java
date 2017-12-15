package entities;

public class CommunicateDefinition {

	private String microserviceOrigin;
	private String microserviceDestin;
	private String using;
	
	public CommunicateDefinition(String microserviceOrigin, String microserviceDestin, String using){
		this.microserviceOrigin = microserviceOrigin;
		this.microserviceDestin = microserviceDestin; 
		this.using = using;
	}
	
	public CommunicateDefinition(String microserviceOrigin, String microserviceDestin){
		this(microserviceOrigin, microserviceDestin, null);
	}
	
	public String getMicroserviceOrigin() {
		return this.microserviceOrigin;
	}

	public String getMicroserviceDestin() {
		return this.microserviceDestin;
	}
	
	public String getUsing(){
		return this.using;
	}
	@Override
	public int hashCode(){
		return microserviceOrigin.length() + microserviceDestin.length();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof CommunicateDefinition){
			CommunicateDefinition access = (CommunicateDefinition) obj;
			return this.microserviceOrigin.equals(access.microserviceOrigin)
					&& this.microserviceDestin.equals(access.microserviceDestin);
		}
		return false;
	}
	
	@Override
	public String toString(){
		String s = microserviceOrigin + " communicate " + microserviceDestin; 
		if(using != null){
			s += " using " + this.using;
		}
		return s;
	}
}
