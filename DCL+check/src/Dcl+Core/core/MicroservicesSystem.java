package msdcl.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MicroservicesSystem {


	//private HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraints;
	private HashMap<ModuleDefinition, Set<ConstraintDefinition>> mapConstraintsModule;	
	private HashMap<ModuleDefinition, Set<CommunicateDefinition>> mapCommunications;
	private HashMap<MicroserviceDefinition, StringBuilder> mapDcl; 
	private HashMap<MicroserviceDefinition, Set<ModuleDefinition>> mapModulesDcl;
	private HashMap<String, MicroserviceDefinition> mapService;
	private HashMap<String, String> mapInfoPathService;
	private Set<ModuleDefinition> allModuleDefinitions;
	
	
	public MicroservicesSystem(){ 
	//	this.mapConstraints = new HashMap<>();
		this.mapCommunications = new HashMap<>();
		this.mapDcl = new HashMap<>();
		this.mapService = new HashMap<>();
		this.mapInfoPathService = new HashMap<>();
		this.allModuleDefinitions = new HashSet<>();
		this.mapConstraintsModule = new HashMap<>();
		this.mapModulesDcl = new HashMap<>();
	}
	
	public void addMicroservice(MicroserviceDefinition ms){
		this.mapService.put(ms.getName(), ms);		
		this.mapDcl.put(ms, new StringBuilder());
		this.mapModulesDcl.put(ms, new HashSet<ModuleDefinition>());
	//	this.mapConstraints.put(ms, new HashSet<ConstraintDefinition>());
//		this.mapCommunications.put(ms, new HashSet<CommunicateDefinition>());
		
	}
	
	public MicroserviceDefinition getMicroserviceDefinition(String name){
		return this.mapService.get(name);
	}
	
	public void addConstraint(ConstraintDefinition c){
	//	MicroserviceDefinition ms = this.getMicroserviceDefinition(c.getMicroserviceOrigin());
		ModuleDefinition module = this.getModule(c.getModule().getName());
		if(module != null){
		//	this.mapConstraints.get(ms).add(c);
			this.mapConstraintsModule.get(module).add(c);
		}
	}
	
	public void addConstraints(Set<ConstraintDefinition> constraints){
		for(ConstraintDefinition c : constraints){
			addConstraint(c);
		}
	}
	public void addModule(ModuleDefinition moduleDefinition ) {
		
		this.mapConstraintsModule.put(moduleDefinition, new HashSet<ConstraintDefinition>());
		this.mapCommunications.put(moduleDefinition, new HashSet<CommunicateDefinition>());
	}
	
	public void addDCLModule(MicroserviceDefinition ms, Set<ModuleDefinition>  moduleDefinionList ) {
		allModuleDefinitions.addAll(moduleDefinionList);
		Set<ModuleDefinition> DCLModules = new HashSet<>();
		if(mapModulesDcl.get(ms).isEmpty()) {
			DCLModules.addAll(moduleDefinionList);
			mapModulesDcl.put(ms, moduleDefinionList);
		}else {
			Set<ModuleDefinition> DCLModulesAux = mapModulesDcl.get(ms);
			DCLModulesAux.addAll(moduleDefinionList);
			mapModulesDcl.put(ms, DCLModulesAux);
		}
		
	}
	public Set<ModuleDefinition> getDCLModule(MicroserviceDefinition ms){
		return this.mapModulesDcl.get(ms);
	}
	
	public ModuleDefinition getModuleByPath(String pathModule, MicroserviceDefinition ms) throws IOException {
		for(ModuleDefinition module : allModuleDefinitions) {		
			List<String> files =  module.getFilesToString();
			if(files !=null) {
				for(String f : files) {
					String fileName = f.replaceAll("\\/", ".").replaceAll(" ","");
					if(fileName.contains(pathModule)|| pathModule.startsWith(fileName)|| fileName.endsWith(pathModule))
						return module;
				}
			}
		}
		return null;
	}
	
//	public Set<ConstraintDefinition> getConstraints(MicroserviceDefinition ms){
//		return this.mapConstraints.get(ms);
//	}
	public Set<ModuleDefinition> getAllModules(){
		return this.allModuleDefinitions;
	}
	
	
	public Set<ConstraintDefinition> getConstraints(ModuleDefinition commModule){
		for(ModuleDefinition module:allModuleDefinitions) {			
			for(ConstraintDefinition c : mapConstraintsModule.get(module)) {
				if(c.getModule().equals(commModule)) {
				//	this.mapInfoPathService.put(microservice.getName(), commModule.getName());
					return this.mapConstraintsModule.get(commModule);
				}
			}
		}
		return null;
	}
	public MicroserviceDefinition getMsByName(String name) {
		for(MicroserviceDefinition ms: getMicroservices()) {
			if(ms.getName().equals(name))
				return ms;
		}
		return null;
	}
	
	public String getMapInfoPathService(String msName) {
		return mapInfoPathService.get(msName);
	}

	public Set<CommunicateDefinition> getCommunications(ModuleDefinition ms){
		return this.mapCommunications.get(ms);
		
	}
	
	public void addCommunication(CommunicateDefinition c){
		ModuleDefinition module = this.getModule(c.getModule().getName());
		if(module != null){
			this.mapCommunications.get(module).add(c);
		}
	}
	
	public void setCommunications(HashMap<ModuleDefinition, Set<CommunicateDefinition>> mapCommunications){
		this.mapCommunications = mapCommunications;
	}
	
	public void addDcl(MicroserviceDefinition ms, String dcl){
		this.mapDcl.get(ms).append(dcl).append('\n');
	}
	
	public String getDcl(MicroserviceDefinition ms){
		return this.mapDcl.get(ms).toString();
	}
	
	public Collection<MicroserviceDefinition> getMicroservices(){
		return this.mapService.values();
	} 
	
	public ModuleDefinition getModule(String moduleSearch) {
		for(ModuleDefinition module: getAllModules()) {
			if(module.getName().equals(moduleSearch))
				return module;
		}
		return null;
	}
	public HashMap<ModuleDefinition, Set<ConstraintDefinition>> getMapConstraintsModule() {
		return mapConstraintsModule;
	}

	public void setMapConstraintsModule(HashMap<ModuleDefinition, Set<ConstraintDefinition>> mapConstraintsModule) {
		this.mapConstraintsModule = mapConstraintsModule;
	}

	public HashMap<ModuleDefinition, Set<CommunicateDefinition>> getMapCommunications() {
		return mapCommunications;
	}

	public void setMapCommunications(HashMap<ModuleDefinition, Set<CommunicateDefinition>> mapCommunications) {
		this.mapCommunications = mapCommunications;
	}
	
}
