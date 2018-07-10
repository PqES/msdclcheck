package jsdeodorant.language;

import java.util.ArrayList;
import java.util.List;

public class PredefinedNodeModule {
	private String objectName;
	private List<String> modules;

	public PredefinedNodeModule(String name) {
		this.objectName = name;
		this.modules = new ArrayList<>();
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String name) {
		this.objectName = name;
	}

	public List<String> getModules() {
		return modules;
	}

	public void addNodeModule(String moduleName) {
		this.modules.add(moduleName);
	}

	public boolean contains(String moduleName) {
		for (String modules : modules) {
			if ((objectName + "." + modules).equalsIgnoreCase(moduleName))
				return true;
		}
		return false;
	}
}
