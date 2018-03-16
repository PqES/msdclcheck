package core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MicroservicesSystem {


	private HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraints;
	private HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> mapCommunications;
	private HashMap<MicroserviceDefinition, StringBuilder> mapDcl; 
	private HashMap<String, MicroserviceDefinition> mapService;
	private Set<String> allMicrosservice;
	
	
	public MicroservicesSystem(){  
		this.mapConstraints = new HashMap<>();
		this.mapCommunications = new HashMap<>();
		this.mapDcl = new HashMap<>();
		this.mapService = new HashMap<>();
		this.allMicrosservice = new HashSet<>();
	}
	
	public void addMicroservice(MicroserviceDefinition ms){
		this.mapService.put(ms.getName(), ms);
		this.mapDcl.put(ms, new StringBuilder());
		this.mapConstraints.put(ms, new HashSet<ConstraintDefinition>());
		this.mapCommunications.put(ms, new HashSet<CommunicateDefinition>());
	}
	
	public MicroserviceDefinition getMicroserviceDefinition(String name){
		return this.mapService.get(name);
	}
	
	public void addConstraint(ConstraintDefinition c){
		MicroserviceDefinition ms = this.getMicroserviceDefinition(c.getMicroserviceOrigin());
		if(ms != null){
			this.mapConstraints.get(ms).add(c);
		}
	}
	
	public void addConstraints(Set<ConstraintDefinition> constraints){
		for(ConstraintDefinition c : constraints){
			addConstraint(c);
		}
	}
	
	public Set<ConstraintDefinition> getConstraints(MicroserviceDefinition ms){
		return this.mapConstraints.get(ms);
	}
	
	public Set<CommunicateDefinition> getCommunications(MicroserviceDefinition ms){
		return this.mapCommunications.get(ms);
		
	} 
	
	public void addCommunication(CommunicateDefinition c){
		MicroserviceDefinition ms = this.getMicroserviceDefinition(c.getMicroserviceOrigin());
		if(ms != null){
			this.mapCommunications.get(ms).add(c);
		}
	}
	
	public void setCommunications(HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> mapCommunications){
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
	public Set<String> getAllMicrosservice() { 
		return allMicrosservice;
	}

	public void setAllMicrosservice(Set<String> allMicrosservice) {
		this.allMicrosservice = allMicrosservice;
	}
	
	
	
}
