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
	Set<DivergenceDependencyConstraint> divergence = new HashSet<>();
	Set<AbsenceDependencyConstraint> absences = new HashSet<>();

	public ConstraintsAnalyser() {

	}

	public static ConstraintsAnalyser getInstance() {
		return instance;
	}

	public boolean validateOnlyCan(String msOrigin, String msDestin,
			HashMap<MicroserviceDefinition, Set<AccessDefinition>> mapAccess) {

		int violate = 0;
		Set<ArchitecturalDrift> violates = new HashSet<>();
		for (MicroserviceDefinition m : mapAccess.keySet()) {

			if (!m.getName().equals(msOrigin)) {
				for (AccessDefinition a : mapAccess.get(m)) {

					if (a.getCalle().getName().equals(msDestin)) {
						violate++;
						return true;
					}
				}
			}
		}
		System.out.println("number violate: " + violate);
		return false;
	}

	public String validateCanOnly(String msOrigin, String msDestin,
			HashMap<MicroserviceDefinition, Set<AccessDefinition>> mapAccess) {

		int violate = 0;

		for (MicroserviceDefinition m : mapAccess.keySet()) {

			if (m.getName().equals(msOrigin)) {
				for (AccessDefinition a : mapAccess.get(m)) {

					if (!a.getCalle().getName().equals(msDestin)) {

						return "divergence";
					}
				}
				if (mapAccess.get(m).size() < 1) {
					return "absence";
				}

			}

		}

		System.out.println("number violate: " + violate);
		return "";

	}

	public String validateMust(String msOrigin, String msDestin,
			HashMap<MicroserviceDefinition, Set<AccessDefinition>> mapAccess) {

		int violate = 0;

		for (MicroserviceDefinition m : mapAccess.keySet()) {

			if (m.getName().equals(msOrigin)) {
				for (AccessDefinition a : mapAccess.get(m)) {

					if (!a.getCalle().getName().equals(msDestin)) {
						return "divergence";

					}

				}

			}

		}

		System.out.println("number violate: " + violate);
		return "";

	}

	public boolean validateAusence(MicroserviceDefinition msOrigin, MicroserviceDefinition msDestin,
			Set<AccessDefinition> mapAccess) {

		return false;
	}

	public void analyseConstraints(HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint,
			HashMap<MicroserviceDefinition, Set<AccessDefinition>> mapAccess) {

		Set<DivergenceDependencyConstraint> divergences = new HashSet<>();// Salvar
																			// os
																			// resultados

		Set<ArchitecturalDrift> violates = new HashSet<>();
		for (MicroserviceDefinition m : mapConstraint.keySet()) { // Percorre as
																	// definicoes

			for (ConstraintDefinition c : mapConstraint.get(m)) { //

				if (c.getConstraint().getConstraintType() == ConstraintType.CAN_COMMUNICATE_ONLY) {

					if (validateCanOnly(c.getMicroserviceOrigin(), c.getMicroserviceDestin(), mapAccess)
							.equals("divergence")) {
						this.divergence.add(
								new DivergenceDependencyConstraint(c, c.getConstraint().getConstraintType().name()));

					}
					if (validateCanOnly(c.getMicroserviceOrigin(), c.getMicroserviceDestin(), mapAccess)
							.equals("ausence")) {

						this.absences.add(new AbsenceDependencyConstraint(c, c.getMicroserviceOrigin(),
								c.getMicroserviceDestin()));

					}

				}

				if (c.getConstraint().getConstraintType() == ConstraintType.ONLY_CAN_COMMUNICATE) {

					if (validateOnlyCan(c.getMicroserviceOrigin(), c.getMicroserviceDestin(), mapAccess)) {
						this.divergence.add(
								new DivergenceDependencyConstraint(c, c.getConstraint().getConstraintType().name()));
					}
				}

				if (c.getConstraint().getConstraintType() == ConstraintType.MUST_COMMUNICATE) {
					if (validateMust(c.getMicroserviceOrigin(), c.getMicroserviceDestin(), mapAccess)
							.equals("divergence")) {
						this.divergence.add(
								new DivergenceDependencyConstraint(c, c.getConstraint().getConstraintType().name()));
					}
				}
			}

		}

		print();
		printAbsence();
	}

	public void print() {
		for (DivergenceDependencyConstraint a : divergence) {
			System.out.println("Divergences:   " + a.getMessage());

		}

	}

	public void printAbsence() {
		for (AbsenceDependencyConstraint a : absences) {
			System.out.println("Absences:   " + a.getMessage());

		}

	}

	public Set<DivergenceDependencyConstraint> getDivergence() {
		return divergence;
	}

	public Set<AbsenceDependencyConstraint> getAbsences() {
		return absences;
	}

}
