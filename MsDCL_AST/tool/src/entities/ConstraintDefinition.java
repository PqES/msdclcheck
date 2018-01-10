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
	
	public boolean usingMatch(CommunicateDefinition communicate) {
		if(this.using != null && communicate.getUsing() != null) {
			String constraintRoute[] = this.using.split("/");
			String communicateRoute[] = communicate.getUsing().split("/");
			if(communicateRoute.length >= constraintRoute.length){
				for(int i = 0; i < constraintRoute.length; i++){
					if(!constraintRoute[i].equals("*") && !constraintRoute[i].equals(communicateRoute[i])){
						return false;
					}
				}
				return true;
			}
		}
		return this.using == null;
	}
	
	public boolean servicesMatch(CommunicateDefinition communicate) {
		return this.microserviceOrigin.equalsIgnoreCase(communicate.getMicroserviceOrigin())
				&& this.microserviceDestin.equalsIgnoreCase(communicate.getMicroserviceDestin());
	}
	
	public boolean match(CommunicateDefinition communicate){
		return servicesMatch(communicate) && usingMatch(communicate);
	}
	
	public Boolean canCommunicate(CommunicateDefinition communicate) {	
		if(this.match(communicate)){
			if (this.getConstraint().getConstraintType() == ConstraintType.CANNOT_COMMUNICATE) {
				return false;
			}else{
				return true;
			}
		}else if(this.microserviceOrigin.equalsIgnoreCase(communicate.getMicroserviceOrigin()) &&
				!this.microserviceDestin.equalsIgnoreCase(communicate.getMicroserviceDestin()) &&
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
