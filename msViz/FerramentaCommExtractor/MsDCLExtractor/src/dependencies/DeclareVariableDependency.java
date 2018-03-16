package dependencies;

import enums.DependencyType;

public class DeclareVariableDependency extends Dependency{
	public String nameField;
	public String nameMethod;

	public DeclareVariableDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, String nameField, String nameMethod) {
		super(nameClass1, nameClass2, position, offset, length);
		this.nameField = nameField;
		this.nameMethod = nameMethod;
	}

	public DeclareVariableDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length) {
		super(nameClass1, nameClass2, position, offset, length);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.DECLARE;
	}

	@Override
	public String toString() {
		return "'"+this.getNameClass1() + "contains a local variable "+this.nameField 
				+" is declarated in " + this.nameMethod + "in the " + this.getNameClass2();
	}
	public String getNameField() {
		return nameField;
	}

	public void setNameField(String nameField) {
		this.nameField = nameField;
	}
	public String getNameMethod() {
		return nameMethod;
	}

	public void setNameMethod(String nameMethod) {
		this.nameMethod = nameMethod;
	}
}
