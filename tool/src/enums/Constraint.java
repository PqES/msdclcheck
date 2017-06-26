package enums;

public enum Constraint {
	
	CAN_COMMUNICATE("can-communicate",ConstraintType.CAN_COMMUNICATE),
	CANNOT_COMMUNICATE("cannot-communicate",ConstraintType.CANNOT_COMMUNICATE),
	CAN_COMMUNICATE_ONLY("can-communicate-only", ConstraintType.CAN_COMMUNICATE_ONLY),
	MUST_COMMUNICATE("must-communicate", ConstraintType.MUST_COMMUNICATE),
	ONLY_CAN_COMMUNICATE("only-can-communicate", ConstraintType.ONLY_CAN_COMMUNICATE);

	private String value;
	private ConstraintType constraintType;
	
	
	Constraint(String value, ConstraintType constraintType){
		this.value = value;
		this.constraintType = constraintType;
	}
	
	Constraint(){
		
	}

	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public ConstraintType getConstraintType() {
		return constraintType;
	}


	public void setConstraintType(ConstraintType constraintType) {
		this.constraintType = constraintType;
	}
	
	public  Constraint getConstraint(String value){
		return Constraint.valueOf(value.toUpperCase().replaceAll("-", "_"));
	}
	public void getAll(){
		
	}
	public String toString(){
		return value;
		
	}
	
	
	

}
