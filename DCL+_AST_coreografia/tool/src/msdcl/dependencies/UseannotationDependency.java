package msdcl.dependencies;

import enums.DependencyType;

public class UseannotationDependency  extends Dependency{

	
	public UseannotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length) {
		super(nameClass1, nameClass2, position, offset, length);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.USEANNOTATION;
	}

	@Override
	public String toString() {
		return this.getNameClass1() + "useannotation " + this.getNameClass2();
	}

}
