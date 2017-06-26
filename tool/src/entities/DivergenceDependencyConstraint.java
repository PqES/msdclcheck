package entities;

import com.sun.accessibility.internal.resources.accessibility;

public class DivergenceDependencyConstraint extends ArchitecturalDrift{
	
	private final CommunicateDefinition access; 
	
	public DivergenceDependencyConstraint(ConstraintDefinition violate, CommunicateDefinition access) {
		super(violate);
		this.access = access;
	}
	
	public String getViolationType(){
		return ArchitecturalDrift.DIVERGENCE;
	}

	@Override
	public String getMessage() {
		return "Divergence: " + getViolateConstraint().toString() + " (" + access.toString() + ")";
	}
}
