package entities;


import communicationAnalyser.drift.DivergenceDependencyConstraint;
import communicationExtractor.CommunicateDefinition;
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
		return (this.using != null && this.using.equals(communicate.getUsing()) || this.using == null)
				&& this.microserviceDestin.equalsIgnoreCase(communicate.getMicroserviceDestin());
	}
	
	public Boolean canCommunicate(CommunicateDefinition communicate) {
		if(this.match(communicate)){
			if (this.getConstraint().getConstraintType() == ConstraintType.CANNOT_COMMUNICATE) {
				return false;
			}else{
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
