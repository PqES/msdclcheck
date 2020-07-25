package communicationChecker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import com.sun.xml.internal.ws.api.server.Module;

import enums.ConstraintType;
import msdcl.core.CommunicateDefinition;
import msdcl.core.ConstraintDefinition;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;
import msdcl.core.ModuleDefinition;

public class CommunicationChecker {

	private final static CommunicationChecker instance = new CommunicationChecker();

	public CommunicationChecker() {

	}

	public static CommunicationChecker getInstance() {
		return instance;
	}

	// checa localmente se um microservico pode se comunicar com outro
	private Set<ClassifiedCommunicate> canCommunicateLocal(ConstraintDefinition constraint, MicroservicesSystem system,
			ModuleDefinition msModule) {
		Set<ClassifiedCommunicate> classifiedCommunications = new HashSet<>();
		// ModuleDefinition moduleCurrent =
		// system.getModule(communicate.getModule().getName());
		Boolean fileAvailable = false;
		if (system.getCommunications(msModule) != null) {
			for (CommunicateDefinition communicate : system.getCommunications(msModule)) {
				Boolean canCommunicate = constraint.canCommunicate(communicate);
				if (canCommunicate != null && canCommunicate == true) {
					classifiedCommunications.add(new ClassifiedCommunicate(true, constraint, communicate)); // verificar
																											// must
				} else if (canCommunicate != null && canCommunicate == false) {
					if (constraint.getFilePathModule().equals(communicate.getFileModuleOrigin()))
						classifiedCommunications.add(new ClassifiedCommunicate(false, constraint, communicate));
				}
			}
		}
		return classifiedCommunications;
	}

	public Set<DivergenceDependencyConstraint> verifyOthersCommunications(MicroservicesSystem system,
			ModuleDefinition msModule, ConstraintDefinition constraint) {
		Boolean hasOtherCommunication = false;
		Set<DivergenceDependencyConstraint> allDivergences = new HashSet<>();
		for (ModuleDefinition module : system.getAllModules()) {
			if (module.equals(msModule))
				continue;
			if (system.getCommunications(module) != null) {
				for (CommunicateDefinition commActual : system.getCommunications(module)) {

					// verifica se uma comunicação atual do MS possui o mesmo MS de destino
					if (commActual.getMicroserviceDestin().equalsIgnoreCase(constraint.getMicroserviceDestin())
							&& constraint.usingMatch(commActual)) {
						hasOtherCommunication = true;
						allDivergences.add(new DivergenceDependencyConstraint(constraint, msModule, commActual));
						// return true;
					}
				}
			}
		}
		return allDivergences;

	}

	public Set<ArchitecturalDrift> canCommunicate(ConstraintDefinition constraint, MicroservicesSystem system,
			ModuleDefinition msModule) {
		Set<ArchitecturalDrift> drifts = new HashSet<>();
		Set<ClassifiedCommunicate> classifiedCommunications = (canCommunicateLocal(constraint, system, msModule));
		for (ClassifiedCommunicate classifiedCommunicate : classifiedCommunications) {
			if (!classifiedCommunicate.canCommunicate) {
				drifts.add(new DivergenceDependencyConstraint(classifiedCommunicate.associatedConstraint, msModule,
						classifiedCommunicate.communicate));

			}
		}

		return drifts;
	}

	// checa globalmente se um module pode se comunicar com um microsserviço
	private Set<ArchitecturalDrift> canCommunicateGlobal(ConstraintDefinition constraint, MicroservicesSystem system,
			ModuleDefinition msModule) {
		Set<ArchitecturalDrift> drifts = new HashSet<>();
		Set<DivergenceDependencyConstraint> divergences = verifyOthersCommunications(system, msModule, constraint);
		if (!divergences.isEmpty()) {
			drifts.addAll(divergences);
		}
		return drifts;
	}

	// obtem ausencias
	public Set<ArchitecturalDrift> getAbsences(ModuleDefinition msModule, ConstraintDefinition constraint,
			MicroservicesSystem system) {
		Set<ArchitecturalDrift> absences = new HashSet<>();
		boolean absence = true;
		for (CommunicateDefinition communicate : system.getCommunications(msModule)) {

			if (constraint.match(communicate)) {
				absence = false;
				break;
			}
		}
		if (absence) {
			absences.add(new AbsenceDependencyConstraint(constraint, msModule));
		}
		return absences;

	}

	public Map<ModuleDefinition, Set<ArchitecturalDrift>> check(MicroservicesSystem system) {
		Set<ArchitecturalDrift> drifts = new HashSet<>();
		Map<ModuleDefinition, Set<ArchitecturalDrift>> driftsCommunications = new HashMap<>();
		int countModule = 0;
		for (ModuleDefinition msModule : system.getAllModules()) {
			countModule += 1;
			if (system.getConstraints(msModule) != null) {
				for (ConstraintDefinition constraint : system.getConstraints(msModule)) {

					if (constraint.getConstraint().getConstraintType() == ConstraintType.MUST_COMMUNICATE) {
						Set<ArchitecturalDrift> absences = getAbsences(msModule, constraint, system);
						drifts.addAll(absences);
					} else if (constraint.getConstraint().getConstraintType() == ConstraintType.ONLY_CAN_COMMUNICATE) {
						drifts.addAll(canCommunicateGlobal(constraint, system, msModule));
					} else { // CAN_ONLY ou CANNOT
						drifts.addAll(canCommunicate(constraint, system, msModule));

					}

					driftsCommunications = addDriftsCommunications(msModule, drifts, driftsCommunications);
					// Set<ArchitecturalDrift> absences = getAbsences(msModule, system);
					// drifts.addAll(absences);
				}
			}

		}
		return driftsCommunications;
	}

	public Map<ModuleDefinition, Set<ArchitecturalDrift>> addDriftsCommunications(ModuleDefinition msModule,
			Set<ArchitecturalDrift> drifts, Map<ModuleDefinition, Set<ArchitecturalDrift>> driftsCommunications) {
		Set<ArchitecturalDrift> driftsModule = driftsCommunications.get(msModule);
		if (!drifts.isEmpty()) {
			if (driftsModule == null) {
				Set<ArchitecturalDrift> driftsModuleAtual = new HashSet<>();
				driftsModuleAtual.addAll(drifts);
				driftsCommunications.put(msModule, driftsModuleAtual);
				drifts.clear();
			} else {
				driftsModule.addAll(drifts);
				driftsCommunications.put(msModule, driftsModule);
				drifts.clear();
			}
		}
		return driftsCommunications;
	}

}