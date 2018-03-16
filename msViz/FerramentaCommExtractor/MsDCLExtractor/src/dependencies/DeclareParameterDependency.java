package dependencies;

import enums.DependencyType;

public class DeclareParameterDependency extends Dependency{
	private String nameField;
	private String nameMethod;

	public DeclareParameterDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, String nameField, String nameMethod) {
		super(nameClass1, nameClass2, position, offset, length);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.DECLARE;
	}

	@Override
	public String toString() {
		return "'" + this.getNameClass1() +"contains a formal parameter " + this.getNameField()+
				"in " + this.getNameMethod() + "that type is" + this.getNameClass2();
	}
	public String getNameField() {
		return this.nameField;
	}
	
	public String getNameMethod() {
		return this.nameMethod;
	}
}
