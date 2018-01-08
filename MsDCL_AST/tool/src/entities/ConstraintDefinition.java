package entities;


import communicationChecker.drift.DivergenceDependencyConstraint;
import enums.Constraint;
import enums.ConstraintType;

public class ConstraintDefinition {

	private String microserviceOrigin;
	private String microserviceDestin;
	private Constraint constraint;
	private String using;

	public ConstraintDefinition(String microserviceOrigin, Constraint constraint, String microserviceDestin,
			String using) {
		this.microserviceOrigin = microserviceOrigin;
		this.microserviceDestin = microserviceDestin;
		this.constraint = constraint;
		this.using = using; 
	}

	public ConstraintDefinition(String microserviceOrigin, Constraint constraint, String microserviceDestin) {
		this.microserviceOrigin = microserviceOrigin;
		this.microserviceDestin = microserviceDestin;
		this.constraint = constraint;
		this.using = null;
	}
 
	public String getMicroserviceOrigin() {
		return this.microserviceOrigin;
	}

	public String getMicroserviceDestin() {
		return this.microserviceDestin;
	}

	public Constraint getConstraint() {
		return this.constraint;
	}

	public String getUsing() {
		return this.using;
	}
	
	public boolean match(CommunicateDefinition communicate){
	//	System.out.println(this.microserviceOrigin + " vs. " + communicate.getMicroserviceOrigin() );
	//	System.out.println(this.microserviceDestin + " vs. " + communicate.getMicroserviceDestin() );
		
		if(this.microserviceOrigin.equalsIgnoreCase(communicate.getMicroserviceOrigin())
				&& this.microserviceDestin.equalsIgnoreCase(communicate.getMicroserviceDestin())){
			if(this.using != null && communicate.getUsing() != null){
				String constraintRoute[] = this.using.split("/");
				String communicateRoute[] = communicate.getUsing().split("/");
				if(constraintRoute.length == communicateRoute.length){
					for(int i = 0; i < constraintRoute.length; i++){
						
				//		System.out.println("c1: " + constraintRoute[i]);
				//		System.out.println("c2: " + communicateRoute[i]);
						if(!constraintRoute[i].equals("{dynamic}") && !constraintRoute[i].equals(communicateRoute[i])){
							return false;
						}
					}
					return true;
				}
			}else if(this.using == null){
				return true;
			}
		}
		return false;
	}
	
	public Boolean canCommunicate(CommunicateDefinition communicate) {
		
		if(this.match(communicate)){
		//	System.out.println("Entrou? ");
		//	System.out.println("Constraint " + this.getConstraint());
			if (this.getConstraint().getConstraintType() == ConstraintType.CANNOT_COMMUNICATE) {
			//	System.out.println("CANNOT  " + this.getConstraint());
				return false;
			}else{
				//System.out.println("TRUE!! "); 
				return true;
			}
		}else if(!this.microserviceDestin.equalsIgnoreCase(communicate.getMicroserviceDestin()) &&
				this.getConstraint().getConstraintType() == ConstraintType.CAN_COMMUNICATE_ONLY){
			return false;
		}
		return null;
	}

	@Override
	public String toString() {
		if (using != null) {
			return getMicroserviceOrigin() + " " + getConstraint().toString() + " " + getMicroserviceDestin()
					+ " using " + using;
		}
		return getMicroserviceOrigin() + " " + getConstraint().toString() + " " + getMicroserviceDestin();
	}
}
