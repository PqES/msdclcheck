package msdcl.core;

public class ModuleDependencies {
	private ModuleDefinition moduleOrigin;
	private ModuleDefinition moduleDestin;
	private int dependencies;
	private String  tipo;
	
	public int getDependencies() {
		return dependencies;
	}
	public void setDependencies(int dependencies) {
		this.dependencies = dependencies;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public ModuleDependencies(ModuleDefinition moduleOrigin, ModuleDefinition moduleDestin, String tipo) {
		this.moduleOrigin = moduleOrigin;
		this.moduleDestin = moduleDestin;
		this.dependencies = 1;
		this.tipo = tipo;
	}
	public ModuleDefinition getModuleDestin() {
		return moduleDestin;
	}
	public void setModuleDestin(ModuleDefinition nomeService) {
		this.moduleDestin = nomeService;
	}
	public ModuleDefinition getModuleOrigin() {
		return moduleOrigin;
	}
	public void setModuleOrigin(ModuleDefinition moduleOrigin) {
		this.moduleOrigin = moduleOrigin;
	}
	public Integer getDependenciesNumber() {
		return dependencies;
	}
	public int setDependenciesNumber(int communicates) {
		return this.dependencies = communicates;
	}
	
}
