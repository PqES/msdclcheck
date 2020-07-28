package msdcl.communicationChecker;

import msdcl.core.ConstraintDefinition;
import msdcl.core.MicroserviceDefinition;

public class AbsenceDependencyConstraint extends ArchitecturalDrift{
	
	public AbsenceDependencyConstraint(ConstraintDefinition violate) {
		super(violate);
		
	}
	public AbsenceDependencyConstraint(ConstraintDefinition violate,  MicroserviceDefinition microservice) {
		super(violate, microservice);
		
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
