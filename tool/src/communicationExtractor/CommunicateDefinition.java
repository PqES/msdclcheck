package communicationExtractor;

import entities.MicroserviceDefinition;

public class CommunicateDefinition {

	private MicroserviceDefinition caller;
	private MicroserviceDefinition callee;
	private String using;
	
	public CommunicateDefinition(MicroserviceDefinition caller, MicroserviceDefinition callee, String using){
		this.caller = caller;
		this.callee = callee;
		this.using = using;
	}
	
	public CommunicateDefinition(MicroserviceDefinition caller, MicroserviceDefinition callee){
		this.caller = caller;
		this.callee = callee;
		this.using = null;
	}
	
	public MicroserviceDefinition getCaller() {
		return caller;
	}

	public MicroserviceDefinition getCalle() {
		return callee;
	}
	
	public String getUsing(){
		return this.using;
	}
	@Override
	public int hashCode(){
		return caller.getName().length() + callee.getName().length();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof CommunicateDefinition){
			CommunicateDefinition access = (CommunicateDefinition) obj;
			return this.caller.equals(access.caller) && this.callee.equals(access.callee);
		}
		return false;
	}
	
	@Override
	public String toString(){
		String s = caller.getName() + " communicate " + callee.getName(); 
		if(using != null){
			s += " using " + this.using;
		}
		return s;
	}
}
