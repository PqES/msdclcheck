package jsDepExtractor.enums;

public enum DependencyType {
	CREATE("creates", TypeDependency.CREATE),
	ACCESS("access", TypeDependency.ACCESS),
	DECLARE("declare", TypeDependency.DECLARE);
	
	private String value;
	private TypeDependency typeDependency;
	
	private DependencyType(String value, TypeDependency typeDependency) {
		this.value = value;
		this.typeDependency = typeDependency;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public TypeDependency getTypeDependency() {
		return typeDependency;
	}
	public void setTypeDependency(TypeDependency typeDependency) {
		this.typeDependency = typeDependency;
	}

}
