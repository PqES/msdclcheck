package msdcl.dependencies;



public class Dependency {
	

	private String nameClassA; 
	private String nameClassB;
	private Integer length;
	private Integer lineA;
	private String offset;
	
	
	public Dependency(String nameClassA, String nameClassB, Integer length, Integer lineA, String offset) {
		super();
		this.nameClassA = nameClassA;
		this.nameClassB = nameClassB;
		this.length = length;
		this.lineA = lineA;
		this.offset = offset;
	}
	
	public String getNameClassA() {
		return nameClassA;
	}


	public void setNameClassA(String nameClassA) {
		this.nameClassA = nameClassA;
	}


	public String getNameClassB() {
		return nameClassB;
	}


	public void setNameClassB(String nameClassB) {
		this.nameClassB = nameClassB;
	}


	public Integer getLength() {
		return length;
	}


	public void setLength(Integer length) {
		this.length = length;
	}


	public Integer getLineA() {
		return lineA;
	}


	public void setLineA(Integer lineA) {
		this.lineA = lineA;
	}


	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public final boolean sameType(Dependency other) {
		return (this.getDependencyType().equals(other.getDependencyType()) && this.classNameB.equals(other.classNameB));
	}

}
