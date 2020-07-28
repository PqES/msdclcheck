package msdcl.communicationChecker;

import enums.ViolationType;
import msdcl.core.ConstraintDefinition;
import msdcl.core.MicroserviceDefinition;

public abstract class ArchitecturalDrift {
	
	public static final String DIVERGENCE = "DIVERGENCE";
	public static final String ABSENCE = "ABSENCE";
	public static final String WARNING = "WARNING";	
	private final ConstraintDefinition violateConstraint;
	private MicroserviceDefinition microservice; 
	
	protected ArchitecturalDrift(ConstraintDefinition violate){ 
		this.violateConstraint = violate;
	}
 
	protected ArchitecturalDrift(ConstraintDefinition violate, MicroserviceDefinition microservice){ 
		this.violateConstraint = violate;
		this.microservice = microservice;
	}
	public ConstraintDefinition getViolateConstraint() {
		return violateConstraint;
	} 
	public MicroserviceDefinition getMicroservice(MicroserviceDefinition ms) {
		return this.microservice;
	}
	public abstract String getMessage();

	public abstract String getViolationType();
	
	@Override
	public int hashCode(){
		return getViolationType().length();
	}
}
