package communicationAnalyser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import communicationAnalyser.drift.AbsenceDependencyConstraint;
import communicationAnalyser.drift.ArchitecturalDrift;
import communicationAnalyser.drift.DivergenceDependencyConstraint;
import communicationAnalyser.drift.WarningConstraint;
import communicationExtractor.CommunicateDefinition;

import java.util.Set;

import entities.ConstraintDefinition;
import entities.MicroserviceDefinition;
import enums.ConstraintType;

public class CommunicationAnalyser {

	private final static CommunicationAnalyser instance = new CommunicationAnalyser();
	
	public CommunicationAnalyser() {

	}

	public static CommunicationAnalyser getInstance() {
		return instance;
	}
	
	private class ClassifiedCommunicate{
		public boolean canCommunicate;
		public ConstraintDefinition associatedConstraint;
		
		public ClassifiedCommunicate(boolean canCommunicate, ConstraintDefinition associatedConstraint){
			this.canCommunicate = canCommunicate;
			this.associatedConstraint = associatedConstraint;
		}
	}
	
	//checa localmente se um microservico pode se comunicar com outro
	private ClassifiedCommunicate canCommunicateLocal(CommunicateDefinition communicate, Set<ConstraintDefinition> constraints){
		for(ConstraintDefinition constraint : constraints){
			if(communicate.getCalle().getName().equalsIgnoreCase(constraint.getMicroserviceDestin())){
				if(constraint.getConstraint().getConstraintType() == ConstraintType.CANNOT_COMMUNICATE){
					//e' violacao
					return new ClassifiedCommunicate(false, constraint);
				}else{
					//pode comunicar
					return new ClassifiedCommunicate(true, constraint); 
				}
			}else if(constraint.getConstraint().getConstraintType() == ConstraintType.CAN_COMMUNICATE_ONLY){
				//e' violacao
				return new ClassifiedCommunicate(false, constraint);
			}
		}
		//possivel warning
		return null;
	}
	
	//checa globalmente se um microservico pode se comunicar com outro
	private ArchitecturalDrift canCommunicateGlobal(CommunicateDefinition communicate, MicroserviceDefinition service, HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint){
		for(MicroserviceDefinition ms : mapConstraint.keySet()){
			if(!ms.equals(service)){
				for(ConstraintDefinition constraint : mapConstraint.get(ms)){
					if(communicate.getCalle().getName().equalsIgnoreCase(constraint.getMicroserviceDestin())
							&& constraint.getConstraint().getConstraintType() == ConstraintType.ONLY_CAN_COMMUNICATE){
						return new DivergenceDependencyConstraint(constraint, communicate);
					}
				}
			}
		}
		return null;
	}
	
	
	public ArchitecturalDrift canCommunicate(CommunicateDefinition communicate, MicroserviceDefinition service, HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint){
		ArchitecturalDrift drift = canCommunicateGlobal(communicate, service, mapConstraint);
		if(drift == null){
			ClassifiedCommunicate classifiedCommunicate = canCommunicateLocal(communicate, mapConstraint.get(service));
			if(classifiedCommunicate == null){
				drift = new WarningConstraint(communicate);
			}else if(!classifiedCommunicate.canCommunicate){
				drift = new DivergenceDependencyConstraint(classifiedCommunicate.associatedConstraint, communicate);
			}
		}
		return drift;
	}
	
	//obtem ausencias
	private ArchitecturalDrift getAbsence(MicroserviceDefinition service, Set<ConstraintDefinition> constraints,
			Set<CommunicateDefinition> communications) {
		for (ConstraintDefinition constraint : constraints) {
			if (constraint.getConstraint().getConstraintType() == ConstraintType.MUST_COMMUNICATE) {
				boolean absence = true;
				for (CommunicateDefinition communicate : communications) {
					if (communicate.getCalle().getName().equalsIgnoreCase(constraint.getMicroserviceDestin())) {
						absence = false;
						break;
					}
				}
				if (absence) {
					return new AbsenceDependencyConstraint(constraint);
				}
			}
		}
		return null;
	}
	
	
	
	public Set<ArchitecturalDrift> analyseCommunications(HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint,
			HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> mapCommunications) {
		Set<ArchitecturalDrift> drifts = new HashSet<>();
		ArchitecturalDrift drift;
		for (MicroserviceDefinition m : mapConstraint.keySet()) {
			for (CommunicateDefinition communicate : mapCommunications.get(m)) {
				drift = canCommunicate(communicate, m, mapConstraint);
				if(drift != null){
					drifts.add(drift);
				}
			}
			drift = getAbsence(m, mapConstraint.get(m), mapCommunications.get(m));
			if(drift != null){
				drifts.add(drift);
			}
		}
		return drifts;
	}
	
}
