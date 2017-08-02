package communicationAnalyser.drift;

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
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof ArchitecturalDrift){
			ArchitecturalDrift drift = (ArchitecturalDrift) obj;
			return this.violateConstraint.equals(drift.violateConstraint) && getViolationType().equals(drift.getViolationType());
		}
		return false;
	}
}
