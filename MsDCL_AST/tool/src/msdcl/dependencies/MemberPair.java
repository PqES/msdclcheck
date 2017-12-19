package msdcl.dependencies;

public class MemberPair {
	private String name;
	private String value;
	public MemberPair(String name, String value) {
		this.name = name;
		this.value = value;
	}
	public MemberPair(String value) {
		this.value = value;
	}
	public String getName() { 
		return name;
	}
	public String getValue() {
		return value;
	}
	
	
	
}
