package msdcl.core;

import java.io.File;
import java.util.List;
import java.util.Set;

public class ModuleDefinition {
	private String name;
	private List<File> files;
	private List<String> filesToString;
	private String pathModule;
	private Boolean isExternalModule;
	public ModuleDefinition(String name, List<File> files) {
		this.name = name;
		this.files = files;
	}
	public ModuleDefinition(String name, String pathModule, Boolean isExternalModule, List<File> files) {
		this.name = name;
		this.pathModule = pathModule;
		this.isExternalModule = isExternalModule;
		this.files = files;
	}
	public ModuleDefinition(List<String> filesToString, String name, String pathModule, Boolean isExternalModule) {
		this.name = name;
		this.pathModule = pathModule;
		this.isExternalModule = isExternalModule;
		this.filesToString = filesToString;
	}
	
	public List<String> getFilesToString() {
		return filesToString;
	}
	public void setFilesToString(List<String> filesToString) {
		this.filesToString = filesToString;
	}
	public ModuleDefinition() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<File>  getFiles() {
		return this.files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
	public String getPathModule() {
		return pathModule;
	}
	public void setPathModule(String pathModule) {
		this.pathModule = pathModule;
	}
	public Boolean getIsExternalModule() {
		return isExternalModule;
	}
	public void setIsExternalModule(Boolean isExternalModule) {
		this.isExternalModule = isExternalModule;
	}
	
	

}
