package jsDepExtractor.dependencies;

import jsDepExtractor.enums.DependencyType;

public class CreateDependency extends Dependency{
	
	public CreateDependency(String module1, String module2) {
		super(module1, module2);
	}

	@Override
	public DependencyType getDependencyType() {
		return DependencyType.CREATE;
	}

	@Override
	public String toString() {
		return this.getModule1() + ",creates," + this.getModule2();
	}

}
