package entities;

public class DivergenceDependencyConstraint extends ArchitecturalDrift{
	private  final String forbiddenDependency;
	
	public DivergenceDependencyConstraint(ConstraintDefinition violate, String forbiddenDependency) {
		super(violate);
		this.forbiddenDependency = forbiddenDependency;
	}

	public String getForbiddenDependency() {
		return forbiddenDependency;
	}
	
	public String getViolationType(){
		return "Divergence";
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return this.getViolateConstraint().getMicroserviceOrigin()+ " not " + ""+ 
				this.getViolateConstraint().getConstraint() + " " + this.getViolateConstraint().getMicroserviceDestin();
	}
}
