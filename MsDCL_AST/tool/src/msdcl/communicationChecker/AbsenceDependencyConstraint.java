package msdcl.communicationChecker;

import enums.ViolationType;
import msdcl.core.ConstraintDefinition;

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
	 
	@Override
	public boolean equals(Object obj){
		if(obj instanceof AbsenceDependencyConstraint){
			AbsenceDependencyConstraint absence = (AbsenceDependencyConstraint) obj;
			return this.getViolateConstraint().equals(absence.getViolateConstraint());
		}
		return false;
	}

}
