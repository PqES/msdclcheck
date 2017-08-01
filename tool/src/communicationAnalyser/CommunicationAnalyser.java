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
			Boolean canCommunicate = constraint.canCommunicate(communicate);
			if(canCommunicate != null && canCommunicate == true){
				return new ClassifiedCommunicate(true, constraint);
			}else if(canCommunicate != null && canCommunicate == false){
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
					if(constraint.match(communicate) && constraint.getConstraint().getConstraintType() == ConstraintType.ONLY_CAN_COMMUNICATE){
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
	private Set<ArchitecturalDrift> getAbsences(MicroserviceDefinition service, Set<ConstraintDefinition> constraints,
			Set<CommunicateDefinition> communications) {
		Set<ArchitecturalDrift> absences = new HashSet<>();
		for (ConstraintDefinition constraint : constraints) {
			if (constraint.getConstraint().getConstraintType() == ConstraintType.MUST_COMMUNICATE) {
				boolean absence = true;
				for (CommunicateDefinition communicate : communications) {
					if(constraint.match(communicate)){
						absence = false;
						break;
					}
				}
				if (absence) {
					absences.add(new AbsenceDependencyConstraint(constraint));
				}
			}
		}
		return absences;
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
			drifts.addAll(getAbsences(m, mapConstraint.get(m), mapCommunications.get(m)));
		}
		return drifts;
	}
	
}
