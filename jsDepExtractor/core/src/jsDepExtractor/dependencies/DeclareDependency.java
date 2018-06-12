package jsDepExtractor.dependencies;

import jsDepExtractor.enums.DependencyType;

public class DeclareDependency extends Dependency{
	public DeclareDependency(String module1, String module2) {
		super(module1, module2);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.DECLARE;
	}

	@Override
	public String toString() {
		return this.getModule1() + ",declare," + this.getModule2();
	}

}
