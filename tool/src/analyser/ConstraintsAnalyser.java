package analyser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.sound.midi.Soundbank;

import entities.AbsenceDependencyConstraint;
import entities.AccessDefinition;
import entities.ArchitecturalDrift;
import entities.ClassifiedAccess;
import entities.ConstraintDefinition;
import entities.DivergenceDependencyConstraint;
import entities.MicroserviceDefinition;
import enums.Constraint;
import enums.ConstraintType;

public class ConstraintsAnalyser {

	private final static ConstraintsAnalyser instance = new ConstraintsAnalyser();
	
	public ConstraintsAnalyser() {

	}

	public static ConstraintsAnalyser getInstance() {
		return instance;
	}
	
	//valida comunicação only can-communicate
	private ArchitecturalDrift validateOnlyCan(ConstraintDefinition c, HashMap<MicroserviceDefinition, Set<AccessDefinition>> mapAccess){
		for(Entry<MicroserviceDefinition, Set<AccessDefinition>> entry : mapAccess.entrySet()){
			MicroserviceDefinition service = entry.getKey();
			Set<AccessDefinition> accesses = entry.getValue();
			if(!service.getName().equals(c.getMicroserviceOrigin())){
				for(AccessDefinition a : accesses){
					if(a.getCalle().getName().equals(c.getMicroserviceDestin())){
						return new DivergenceDependencyConstraint(c, a);
					}
				}
			}
		}
		return null;
	}
	
	//valida comunicação can-communicate-only: se não tiver o acesso é considerado abscence?
	private ArchitecturalDrift validateCanOnly(ConstraintDefinition c, MicroserviceDefinition service,
			Set<AccessDefinition> accesses) {
		if(service.getName().equals(c.getMicroserviceOrigin())){
			for (AccessDefinition a : accesses) {
				if (!a.getCalle().getName().equals(c.getMicroserviceDestin())) {
					return new DivergenceDependencyConstraint(c, a);
				}
			}	
		}
		return null;
	}
	
	//valida comunicação cannot-communicate
	private ArchitecturalDrift validateCannotCommunicate(ConstraintDefinition c, MicroserviceDefinition service,
			Set<AccessDefinition> accesses) {
		if(service.getName().equals(c.getMicroserviceOrigin())){
			for (AccessDefinition a : accesses) {
				if (a.getCalle().getName().equals(c.getMicroserviceDestin())) {
					return new DivergenceDependencyConstraint(c, a);
				}
			}	
		}
		return null;
	}
	
	//valida comunicação must-communicate
	private ArchitecturalDrift validateMustCommunicate(ConstraintDefinition c, MicroserviceDefinition service,
			Set<AccessDefinition> accesses) {
		if(service.getName().equals(c.getMicroserviceOrigin())){
			for (AccessDefinition a : accesses) {
				if (a.getCalle().getName().equals(c.getMicroserviceDestin())) {
					return null;
				}
			}	
		}
		return new AbsenceDependencyConstraint(c);
	}
	
	private ArchitecturalDrift validateConstraint(ConstraintDefinition c, MicroserviceDefinition service, HashMap<MicroserviceDefinition, Set<AccessDefinition>> accessMap){
		if(c.getConstraint().getConstraintType() == ConstraintType.CAN_COMMUNICATE){
			//tem o que fazer aqui?
		}else if(c.getConstraint().getConstraintType() == ConstraintType.CAN_COMMUNICATE_ONLY){
			return validateCanOnly(c, service, accessMap.get(service));
		}else if(c.getConstraint().getConstraintType()== ConstraintType.CANNOT_COMMUNICATE){
			return validateCannotCommunicate(c, service, accessMap.get(service));
		}else if(c.getConstraint().getConstraintType() == ConstraintType.MUST_COMMUNICATE){
			return validateMustCommunicate(c, service, accessMap.get(service));
		}else if(c.getConstraint().getConstraintType() == ConstraintType.ONLY_CAN_COMMUNICATE){
			return validateOnlyCan(c, accessMap);
		}
		return null;
	}

	public Set<ArchitecturalDrift> analyseConstraints(HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint,
			HashMap<MicroserviceDefinition, Set<AccessDefinition>> mapAccess) {
		Set<ArchitecturalDrift> drifts = new HashSet<>();
		for (MicroserviceDefinition m : mapConstraint.keySet()) { 
			for (ConstraintDefinition c : mapConstraint.get(m)) {
				ArchitecturalDrift drift = validateConstraint(c, m, mapAccess);
				if(drift != null){
					drifts.add(drift);
				}
			}
		}
		return drifts;
	}
}
