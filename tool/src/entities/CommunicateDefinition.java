package entities;

public class CommunicateDefinition {

	private MicroserviceDefinition caller;
	private MicroserviceDefinition callee;
	
	public CommunicateDefinition(MicroserviceDefinition caller, MicroserviceDefinition callee){
		this.caller = caller;
		this.callee = callee;
	}
	public CommunicateDefinition(MicroserviceDefinition callee){
		
		this.callee = callee;
	}
	
	public MicroserviceDefinition getCaller() {
		return caller;
	}

	public MicroserviceDefinition getCalle() {
		return callee;
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
		return  caller.getName() + " communicate " + callee.getName();
	}
}
