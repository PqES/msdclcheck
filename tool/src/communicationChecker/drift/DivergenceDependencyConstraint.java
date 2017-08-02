package communicationChecker.drift;

import com.sun.accessibility.internal.resources.accessibility;

import communicationExtractor.CommunicateDefinition;
import entities.ConstraintDefinition;

public class DivergenceDependencyConstraint extends ArchitecturalDrift{
	
	private final CommunicateDefinition communication; 
	
	public DivergenceDependencyConstraint(ConstraintDefinition violate, CommunicateDefinition access) {
		super(violate);
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
