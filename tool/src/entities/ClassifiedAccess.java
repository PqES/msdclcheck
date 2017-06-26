package entities;

public class ClassifiedAccess extends AccessDefinition{

	
	private ArchitecturalDrift classificate;
	private ConstraintDefinition associateConstraint;
	
	
	public ClassifiedAccess(MicroserviceDefinition caller, MicroserviceDefinition callee, ArchitecturalDrift classificate, ConstraintDefinition associateConstraint) {
		super(caller, callee);
		this.classificate = classificate;
		this.associateConstraint = associateConstraint;
	}


	public ArchitecturalDrift getClassificate() {
		return classificate;
	}


	public void setClassificate(ArchitecturalDrift classificate) {
		this.classificate = classificate;
	}


	public ConstraintDefinition getAssociateConstraint() {
		return associateConstraint;
	}


	public void setAssociateConstraint(ConstraintDefinition associateConstraint) {
		this.associateConstraint = associateConstraint;
	}

	
}
