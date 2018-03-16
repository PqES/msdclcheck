package dependencies;

import enums.DependencyType;

public class ImplementsDependency extends Dependency{

	

	public ImplementsDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length) {
		super(nameClass1, nameClass2, position, offset, length);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.IMPLEMENTS;
	}

	@Override
	public String toString() {
		return this.getNameClass1() + "implements " + this.getNameClass2();
	}

}
