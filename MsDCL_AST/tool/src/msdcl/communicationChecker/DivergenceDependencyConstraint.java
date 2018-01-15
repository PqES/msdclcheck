package msdcl.communicationChecker;

import com.sun.accessibility.internal.resources.accessibility;

import msdcl.core.CommunicateDefinition;
import msdcl.core.ConstraintDefinition;
import msdcl.core.MicroserviceDefinition;

public class DivergenceDependencyConstraint extends ArchitecturalDrift{
	
	private final CommunicateDefinition communication; 
	
	public DivergenceDependencyConstraint(ConstraintDefinition violate, CommunicateDefinition access) {
		super(violate);
		this.communication = access;
	}
	public DivergenceDependencyConstraint(ConstraintDefinition violate, MicroserviceDefinition microservice,CommunicateDefinition access) {
		super(violate, microservice);
		this.communication = access;
	}
	public String getViolationType(){
		return ArchitecturalDrift.DIVERGENCE;
	}

	@Override
	public String getMessage() {
		return "Divergence: " + getViolateConstraint().toString() + " (" + communication.toString() + ")";
	}
	
	@Override
	public boolean equals(Object obj){ 
		if(obj instanceof DivergenceDependencyConstraint){ 
			DivergenceDependencyConstraint divergence = (DivergenceDependencyConstraint) obj;
			return this.communication.equals(divergence.communication) && this.getViolateConstraint().equals(divergence.getViolateConstraint());
		}
		return false;
	}
}
