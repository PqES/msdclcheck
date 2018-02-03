package msdcl.dependencies;

import enums.DependencyType;

public abstract class Dependency {
	
	private String nameClass1;
	private String nameClass2;	
	private Integer position;
	private Integer offset;
	private Integer length;
	public Dependency(String nameClass1, String nameClass2, Integer position, Integer offset, Integer length) {
		
		this.nameClass1 = nameClass1;
		this.nameClass2 = nameClass2;
		this.length = length;
		this.position = position;
		this.offset = offset;
	}
	public Dependency( String nameClass2, Integer position, Integer offset, Integer length) {
				
		this.nameClass2 = nameClass2;
		this.length = length;
		this.position = position;
		this.offset = offset;
	}
	public String getNameClass1() {
		return nameClass1;
	}
	public void setNameClass1(String nameClass1) {
		this.nameClass1 = nameClass1;
	}
	public String getNameClass2() {
		return nameClass2;
	}
	public void setNameClass2(String nameClass2) {
		this.nameClass2 = nameClass2;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}

	public abstract DependencyType getDependencyType();
	public abstract String toString();
	
}
