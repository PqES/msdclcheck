package jsDepExtractor.dependencies;

import jsDepExtractor.enums.DependencyType;

public abstract class Dependency {
	private String module1;
	private String module2;
	
	public Dependency(String module1, String module2) {
		this.module1 = module1;
		this.module2 = module2;
	}

	public String getModule1() {
		return module1;
	}

	public void setModule1(String module1) {
		this.module1 = module1;
	}

	public String getModule2() {
		return module2;
	}

	public void setModule2(String module2) {
		this.module2 = module2;
	}
	
	public abstract DependencyType getDependencyType();
	public abstract String toString();
}
