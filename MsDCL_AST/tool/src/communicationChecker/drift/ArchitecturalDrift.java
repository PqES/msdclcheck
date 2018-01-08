package communicationChecker.drift;

import entities.ConstraintDefinition;

public abstract class ArchitecturalDrift {
	
	public static final String DIVERGENCE = "DIVERGENCE";
	public static final String ABSENCE = "ABSENCE";
	public static final String WARNING = "WARNING";	
	private final ConstraintDefinition violateConstraint;
	
	
	protected ArchitecturalDrift(ConstraintDefinition violate){
		this.violateConstraint = violate;
	}
 
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
