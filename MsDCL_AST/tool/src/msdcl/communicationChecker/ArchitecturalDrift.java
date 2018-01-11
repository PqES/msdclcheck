package msdcl.communicationChecker;

import enums.ViolationType;
import msdcl.core.ConstraintDefinition;
import msdcl.core.MicroserviceDefinition;

public abstract class ArchitecturalDrift {
	
	public static final String DIVERGENCE = "DIVERGENCE";
	public static final String ABSENCE = "ABSENCE";
	public static final String WARNING = "WARNING";	
	private final ConstraintDefinition violateConstraint;
	//private final ViolationType violationType;
	
	protected ArchitecturalDrift(ConstraintDefinition violate){ 
		this.violateConstraint = violate;
	}
 
//	protected ArchitecturalDrift(ConstraintDefinition violate, ViolationType violationType){ 
//		this.violateConstraint = violate;
//		this.violationType = violationType;
//	}
	public ConstraintDefinition getViolateConstraint() {
		return violateConstraint;
	} 
	
	public abstract String getMessage();

	public abstract String getViolationType();
	
	@Override
	public int hashCode(){
		return getViolationType().length();
	}
}
