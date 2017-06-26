package entities;

public abstract class ArchitecturalDrift {
	
	public static final String DIVERGENCE = "DIVERGENCE";
	public static final String ABSENCE = "ABSENCE";	
	private final ConstraintDefinition violateConstraint;
	
	
	protected ArchitecturalDrift(ConstraintDefinition violate){
		this.violateConstraint = violate;
	}

	public ConstraintDefinition getViolateConstraint() {
		return violateConstraint;
	}
	
	public abstract String getMessage();

	public abstract String getViolationType();
}
