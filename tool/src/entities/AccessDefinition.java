package entities;

public class AccessDefinition {

	private MicroserviceDefinition caller;
	private MicroserviceDefinition callee;
	
	public AccessDefinition(MicroserviceDefinition caller, MicroserviceDefinition callee){
		this.caller = caller;
		this.callee = callee;
	}
	public AccessDefinition(MicroserviceDefinition callee){
		
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
		if(obj instanceof AccessDefinition){
			AccessDefinition access = (AccessDefinition) obj;
			return this.caller.equals(access.caller) && this.callee.equals(access.callee);
		}
		return false;
	}
	
	@Override
	public String toString(){
		return  caller.getName() + " access " + callee.getName();
	}
}
