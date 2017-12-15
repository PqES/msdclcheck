package communicationChecker;

import java.util.HashSet;
import java.util.Set;

import communicationChecker.drift.AbsenceDependencyConstraint;
import communicationChecker.drift.ArchitecturalDrift;
import communicationChecker.drift.DivergenceDependencyConstraint;
import communicationChecker.drift.WarningConstraint;
import entities.CommunicateDefinition;
import entities.ConstraintDefinition;
import entities.MicroserviceDefinition;
import entities.MicroservicesSystem;
import enums.ConstraintType;

public class CommunicationChecker {

	private final static CommunicationChecker instance = new CommunicationChecker();
	
	public CommunicationChecker() {

	}

	public static CommunicationChecker getInstance() {
		return instance;
	}
	
	
	
	//checa localmente se um microservico pode se comunicar com outro
	private ClassifiedCommunicate canCommunicateLocal(CommunicateDefinition communicate, MicroservicesSystem system){
		MicroserviceDefinition msOrigin = system.getMicroserviceDefinition(communicate.getMicroserviceOrigin());
		for(ConstraintDefinition constraint : system.getConstraints(msOrigin)){
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
	private ArchitecturalDrift canCommunicateGlobal(CommunicateDefinition communicate, MicroservicesSystem system){
		MicroserviceDefinition msOrigin = system.getMicroserviceDefinition(communicate.getMicroserviceOrigin());
		for(MicroserviceDefinition ms : system.getMicroservices()){
			if(!ms.equals(msOrigin)){
				for(ConstraintDefinition constraint : system.getConstraints(ms)){
					if(constraint.match(communicate) && constraint.getConstraint().getConstraintType() == ConstraintType.ONLY_CAN_COMMUNICATE){
						return new DivergenceDependencyConstraint(constraint, communicate);
					}
				}
			}
		}
		return null;
	}
	
	
	public ArchitecturalDrift canCommunicate(CommunicateDefinition communicate, MicroservicesSystem system){
		ArchitecturalDrift drift = canCommunicateGlobal(communicate, system);
		if(drift == null){
			ClassifiedCommunicate classifiedCommunicate = canCommunicateLocal(communicate, system);
			if(classifiedCommunicate == null){
				drift = new WarningConstraint(communicate);
			}else if(!classifiedCommunicate.canCommunicate){
				drift = new DivergenceDependencyConstraint(classifiedCommunicate.associatedConstraint, communicate);
			}
		}
		return drift;
	}
	
	//obtem ausencias
	public Set<ArchitecturalDrift> getAbsences(MicroserviceDefinition service, MicroservicesSystem system) {
		Set<ArchitecturalDrift> absences = new HashSet<>();
		for (ConstraintDefinition constraint : system.getConstraints(service)) {
			if (constraint.getConstraint().getConstraintType() == ConstraintType.MUST_COMMUNICATE) {
				boolean absence = true;
				for (CommunicateDefinition communicate : system.getCommunications(service)) {
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
	
	
	
	public Set<ArchitecturalDrift> check(MicroservicesSystem system) {
		Set<ArchitecturalDrift> drifts = new HashSet<>();
		ArchitecturalDrift drift;
		for (MicroserviceDefinition ms : system.getMicroservices()) {
			for (CommunicateDefinition communicate : system.getCommunications(ms)) {
				drift = canCommunicate(communicate, system);
				if(drift != null){
					drifts.add(drift);
				}
			}
			Set<ArchitecturalDrift> absences = getAbsences(ms, system);
			drifts.addAll(absences);
		}
		return drifts;
	}
	
}
