package entities;

import enums.Constraint;

public class ConstraintDefinition {

	private String microserviceOrigin;
	private String microserviceDestin;
	private Constraint constraint;
	
	
	public ConstraintDefinition(String microserviceOrigin,  Constraint constraint, String microserviceDestin){
		this.microserviceOrigin = microserviceOrigin;
		this.microserviceDestin = microserviceDestin;
		this.constraint = constraint;
	}

	public String getMicroserviceOrigin(){
		return this.microserviceOrigin;
	}
	
	public String getMicroserviceDestin(){
		return this.microserviceDestin;
	}
	
	public Constraint getConstraint(){
		return this.constraint;
	}
	
	@Override
	public String toString(){
		return getMicroserviceOrigin() + " "+getConstraint().toString() + " "+ getMicroserviceDestin();
	}
}

