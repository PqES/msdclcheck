package jsDepExtractor.dependencies;

import jsDepExtractor.enums.DependencyType;

public class AccessDependency extends Dependency{
	public AccessDependency(String module1, String module2) {
		super(module1, module2);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.ACCESS;
	}

	@Override
	public String toString() {
		return this.getModule1() + ",access," + this.getModule2();
	}
}
