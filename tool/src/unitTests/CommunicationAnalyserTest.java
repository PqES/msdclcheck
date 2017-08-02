package unitTests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import communicationAnalyser.CommunicationChecker;
import communicationAnalyser.drift.ArchitecturalDrift;
import communicationExtractor.CommunicateDefinition;
import entities.ConstraintDefinition;
import entities.MicroserviceDefinition;
import enums.Constraint;

public class CommunicationAnalyserTest {

	@Test
	public void canCommunicate() {
		HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint = new HashMap<>();
		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> mapCommunicate = new HashMap<>();
		MicroserviceDefinition ms1 = new MicroserviceDefinition("ms1", "link", "path", "language");
		Set<CommunicateDefinition> ms1Communications = new HashSet<>();
		Set<ConstraintDefinition> ms1Constraints = new HashSet<>();
		mapCommunicate.put(ms1, ms1Communications);
		mapConstraint.put(ms1, ms1Constraints);
		MicroserviceDefinition ms2 = new MicroserviceDefinition("ms2", "link", "path", "language");
		Set<CommunicateDefinition> ms2Communications = new HashSet<>();
		Set<ConstraintDefinition> ms2Constraints = new HashSet<>();
		mapCommunicate.put(ms2, ms2Communications);
		mapConstraint.put(ms2, ms2Constraints);
		ms1Communications.add(new CommunicateDefinition(ms1, ms2));
		ms1Constraints.add(new ConstraintDefinition(ms1.getName(), Constraint.CAN_COMMUNICATE, ms2.getName()));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(mapConstraint, mapCommunicate);
		assertEquals(0, drifts.size());
	}
	
	@Test
	public void cannotCommunicate() {
		HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint = new HashMap<>();
		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> mapCommunicate = new HashMap<>();
		MicroserviceDefinition ms1 = new MicroserviceDefinition("ms1", "link", "path", "language");
		Set<CommunicateDefinition> ms1Communications = new HashSet<>();
		Set<ConstraintDefinition> ms1Constraints = new HashSet<>();
		mapCommunicate.put(ms1, ms1Communications);
		mapConstraint.put(ms1, ms1Constraints);
		MicroserviceDefinition ms2 = new MicroserviceDefinition("ms2", "link", "path", "language");
		Set<CommunicateDefinition> ms2Communications = new HashSet<>();
		Set<ConstraintDefinition> ms2Constraints = new HashSet<>();
		mapCommunicate.put(ms2, ms2Communications);
		mapConstraint.put(ms2, ms2Constraints);
		ms1Communications.add(new CommunicateDefinition(ms1, ms2));
		ms1Constraints.add(new ConstraintDefinition(ms1.getName(), Constraint.CANNOT_COMMUNICATE, ms2.getName()));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(mapConstraint, mapCommunicate);
		assertEquals(1, drifts.size());
	}
	
	@Test
	public void canCommunicateOnly() {
		HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint = new HashMap<>();
		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> mapCommunicate = new HashMap<>();
		MicroserviceDefinition ms1 = new MicroserviceDefinition("ms1", "link", "path", "language");
		Set<CommunicateDefinition> ms1Communications = new HashSet<>();
		Set<ConstraintDefinition> ms1Constraints = new HashSet<>();
		mapCommunicate.put(ms1, ms1Communications);
		mapConstraint.put(ms1, ms1Constraints);
		MicroserviceDefinition ms2 = new MicroserviceDefinition("ms2", "link", "path", "language");
		Set<CommunicateDefinition> ms2Communications = new HashSet<>();
		Set<ConstraintDefinition> ms2Constraints = new HashSet<>();
		mapCommunicate.put(ms2, ms2Communications);
		mapConstraint.put(ms2, ms2Constraints);
		ms1Communications.add(new CommunicateDefinition(ms1, ms1));
		ms1Communications.add(new CommunicateDefinition(ms1, ms2));
		ms1Constraints.add(new ConstraintDefinition(ms1.getName(), Constraint.CAN_COMMUNICATE_ONLY, ms2.getName()));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(mapConstraint, mapCommunicate);
		assertEquals(1, drifts.size());
	}
	
	@Test
	public void canOnlyCommunicate() {
		HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint = new HashMap<>();
		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> mapCommunicate = new HashMap<>();
		MicroserviceDefinition ms1 = new MicroserviceDefinition("ms1", "link", "path", "language");
		Set<CommunicateDefinition> ms1Communications = new HashSet<>();
		Set<ConstraintDefinition> ms1Constraints = new HashSet<>();
		mapCommunicate.put(ms1, ms1Communications);
		mapConstraint.put(ms1, ms1Constraints);
		MicroserviceDefinition ms2 = new MicroserviceDefinition("ms2", "link", "path", "language");
		Set<CommunicateDefinition> ms2Communications = new HashSet<>();
		Set<ConstraintDefinition> ms2Constraints = new HashSet<>();
		mapCommunicate.put(ms2, ms2Communications);
		mapConstraint.put(ms2, ms2Constraints);
		ms1Communications.add(new CommunicateDefinition(ms1, ms2));
		ms2Communications.add(new CommunicateDefinition(ms2, ms2));
		ms1Constraints.add(new ConstraintDefinition(ms1.getName(), Constraint.ONLY_CAN_COMMUNICATE, ms2.getName()));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(mapConstraint, mapCommunicate);
		assertEquals(1, drifts.size());
	}
	
	@Test
	public void mustCommunicate() {
		HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint = new HashMap<>();
		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> mapCommunicate = new HashMap<>();
		MicroserviceDefinition ms1 = new MicroserviceDefinition("ms1", "link", "path", "language");
		Set<CommunicateDefinition> ms1Communications = new HashSet<>();
		Set<ConstraintDefinition> ms1Constraints = new HashSet<>();
		mapCommunicate.put(ms1, ms1Communications);
		mapConstraint.put(ms1, ms1Constraints);
		MicroserviceDefinition ms2 = new MicroserviceDefinition("ms2", "link", "path", "language");
		Set<CommunicateDefinition> ms2Communications = new HashSet<>();
		Set<ConstraintDefinition> ms2Constraints = new HashSet<>();
		mapCommunicate.put(ms2, ms2Communications);
		mapConstraint.put(ms2, ms2Constraints);
		ms1Constraints.add(new ConstraintDefinition(ms1.getName(), Constraint.MUST_COMMUNICATE, ms2.getName()));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(mapConstraint, mapCommunicate);
		assertEquals(1, drifts.size());
	}
	
	@Test
	public void warning() {
		HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> mapConstraint = new HashMap<>();
		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> mapCommunicate = new HashMap<>();
		MicroserviceDefinition ms1 = new MicroserviceDefinition("ms1", "link", "path", "language");
		Set<CommunicateDefinition> ms1Communications = new HashSet<>();
		Set<ConstraintDefinition> ms1Constraints = new HashSet<>();
		mapCommunicate.put(ms1, ms1Communications);
		mapConstraint.put(ms1, ms1Constraints);
		MicroserviceDefinition ms2 = new MicroserviceDefinition("ms2", "link", "path", "language");
		Set<CommunicateDefinition> ms2Communications = new HashSet<>();
		Set<ConstraintDefinition> ms2Constraints = new HashSet<>();
		mapCommunicate.put(ms2, ms2Communications);
		mapConstraint.put(ms2, ms2Constraints);
		ms1Communications.add(new CommunicateDefinition(ms1, ms2));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(mapConstraint, mapCommunicate);
		assertEquals(1, drifts.size());
	}
}
