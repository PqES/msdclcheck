package msdcl.dependencies;

import enums.DependencyType;

public class ExtendsDirectDependency extends Dependency{

	public ExtendsDirectDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length) {
		super(nameClass1, nameClass2, position, offset, length);
	}

	@Override
	public DependencyType getDependencyType() {
		// TODO Auto-generated method stub
		return DependencyType.EXTENDS;
	}

	@Override
	public String toString() {
		
		return this.getNameClass1() + "extends direct " + this.getNameClass2();
	}

}
