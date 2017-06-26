package entities;

public class AbsenceDependencyConstraint extends ArchitecturalDrift{
	
	private String msLeftSide;
	private String msRightSide;
	
	public AbsenceDependencyConstraint(ConstraintDefinition violate, String msLeftSide, String msRightSide) {
		super(violate);
		this.msLeftSide = msLeftSide;
		this.msRightSide = msRightSide;// TODO Auto-generated constructor stub
	}

	@Override
	public String getViolationType() {
		// TODO Auto-generated method stub
		return "ABSENCE";
	}

	@Override
	public String getMessage() {
		
		return this.msLeftSide + "does not" + this.getViolateConstraint().getConstraint().getValue() + this.msRightSide;
	}

}
