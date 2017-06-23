package entities;

public class AccessDefinition {

	private MicroserviceDefinition caller;
	private MicroserviceDefinition calle;
	
	public AccessDefinition(MicroserviceDefinition caller, MicroserviceDefinition calle){
		this.caller = caller;
		this.calle = calle;
	}

	public MicroserviceDefinition getCaller() {
		return caller;
	}

	public MicroserviceDefinition getCalle() {
		return calle;
	}
	
	@Override
	public int hashCode(){
		if(caller != null && calle != null){
			return caller.getName().length() + calle.getName().length();
		}else{
			return 0;
		}
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof AccessDefinition){
			AccessDefinition access = (AccessDefinition) obj;
			if(caller != null && calle != null){
				return this.caller.equals(access.caller) && this.calle.equals(access.calle);
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		return caller.getName()+" access "+calle.getName();
	}
}
