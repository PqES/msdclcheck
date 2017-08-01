package communicationAnalyser.drift;

import entities.ConstraintDefinition;

public class AbsenceDependencyConstraint extends ArchitecturalDrift{
	
	public AbsenceDependencyConstraint(ConstraintDefinition violate) {
		super(violate);
	}

	@Override
	public String getViolationType() {
		return ArchitecturalDrift.ABSENCE;
	}

	@Override
	public String getMessage() {
		return "Abscence: "+ getViolateConstraint().toString();
	}

}