package unitTests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import communicationChecker.CommunicationChecker;
import communicationChecker.drift.ArchitecturalDrift;
import entities.CommunicateDefinition;
import entities.ConstraintDefinition;
import entities.MicroserviceDefinition;
import entities.MicroservicesSystem;
import enums.Constraint;
import enums.ConstraintType;

public class CommunicationCheckerTest {

	private MicroservicesSystem createMicroserviceSystem(int n){
		MicroservicesSystem system = new MicroservicesSystem();
		for(int i = 1; i <= n; i++){			
			system.addMicroservice(new MicroserviceDefinition("ms"+i, null, null, null));
		}
		return system;
	}
	
	private MicroservicesSystem createMicroserviceSystem(String names[]){
		MicroservicesSystem system = new MicroservicesSystem();
		for(int i = 0; i < names.length; i++){
			system.addMicroservice(new MicroserviceDefinition(names[i], null, null, null));
		}
		return system;
	}
	
	@Test
	public void canCommunicate() { 
		MicroservicesSystem system = createMicroserviceSystem(2);
		system.addConstraint(new ConstraintDefinition("ms1", Constraint.CAN_COMMUNICATE, "ms2"));
		system.addCommunication(new CommunicateDefinition("ms1", "ms2"));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(system);
		assertEquals(0, drifts.size());
	}
	
	@Test
	public void cannotCommunicate() {
		MicroservicesSystem system = createMicroserviceSystem(2);
		system.addConstraint(new ConstraintDefinition("ms1", Constraint.CANNOT_COMMUNICATE, "ms2"));
		system.addCommunication(new CommunicateDefinition("ms1", "ms2"));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(system);
		assertEquals(1, drifts.size());
	}
	
	@Test
	public void canCommunicateOnly() {
		MicroservicesSystem system = createMicroserviceSystem(3);
		system.addConstraint(new ConstraintDefinition("ms1", Constraint.CAN_COMMUNICATE_ONLY, "ms2"));
		system.addCommunication(new CommunicateDefinition("ms1", "ms2"));
		system.addCommunication(new CommunicateDefinition("ms1", "ms3"));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(system);
		assertEquals(1, drifts.size());
	}
	
	@Test
	public void canOnlyCommunicate() {
		MicroservicesSystem system = createMicroserviceSystem(3);
		system.addConstraint(new ConstraintDefinition("ms1", Constraint.ONLY_CAN_COMMUNICATE, "ms2"));
		system.addCommunication(new CommunicateDefinition("ms1", "ms2"));
		system.addCommunication(new CommunicateDefinition("ms3", "ms2"));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(system);
		assertEquals(1, drifts.size());
		for(ArchitecturalDrift d : drifts) {
			System.out.println(d.getMessage());
		}
	}
	
	@Test
	public void mustCommunicate() {
		MicroservicesSystem system = createMicroserviceSystem(3);
		system.addConstraint(new ConstraintDefinition("ms1", Constraint.CAN_COMMUNICATE, "ms2"));
		system.addConstraint(new ConstraintDefinition("ms1", Constraint.MUST_COMMUNICATE, "ms3"));
		system.addCommunication(new CommunicateDefinition("ms1", "ms2"));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(system);
		assertEquals(1, drifts.size());
	}
	
	@Test
	public void warning() {
		MicroservicesSystem system = createMicroserviceSystem(2);
		system.addCommunication(new CommunicateDefinition("ms1", "ms2"));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(system);
		assertEquals(1, drifts.size());
	}
	
	@Test
	public void usingOnly(){
		MicroservicesSystem system = createMicroserviceSystem(2);
		system.addConstraint(new ConstraintDefinition("ms1", Constraint.CAN_COMMUNICATE_ONLY, "ms2", "/rest1"));
		system.addCommunication(new CommunicateDefinition("ms1", "ms2", "/rest1/rest2"));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(system);
		assertEquals(1, drifts.size());
	}
	
	@Test
	public void dynamicUsing(){
		MicroservicesSystem system = createMicroserviceSystem(2);
		system.addConstraint(new ConstraintDefinition("ms1", Constraint.CAN_COMMUNICATE, "ms2", "/rest1/{dynamic}"));
		system.addConstraint(new ConstraintDefinition("ms1", Constraint.MUST_COMMUNICATE, "ms2", "/rest1/{dynamic}/rest3"));
		system.addCommunication(new CommunicateDefinition("ms1", "ms2", "/rest1/2"));
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(system);
		assertEquals(1, drifts.size());
	}
	
	@Test
	public void toyExample(){						
		String servicesName[] = {"MsProduct", "MsCustomer", "MsSale", "MsAuthentication", "MsNewsletter"};
		MicroservicesSystem system = createMicroserviceSystem(servicesName);
		
		//constraints
		system.addConstraint(new ConstraintDefinition("MsSale", Constraint.ONLY_CAN_COMMUNICATE, "MsAuthentication",
				"api/authenticate"));
		system.addConstraint(new ConstraintDefinition("MsCustomer", Constraint.CAN_COMMUNICATE_ONLY, "MsNewsletter"));
		system.addConstraint(new ConstraintDefinition("MsProduct", Constraint.CAN_COMMUNICATE_ONLY, "MsNewsletter"));
		system.addConstraint(new ConstraintDefinition("MsCustomer", Constraint.MUST_COMMUNICATE, "MsNewsletter"));
		system.addConstraint(new ConstraintDefinition("MsProduct", Constraint.MUST_COMMUNICATE, "MsNewsletter"));
		system.addConstraint(new ConstraintDefinition("MsSale", Constraint.MUST_COMMUNICATE, "MsCustomer"));
		system.addConstraint(new ConstraintDefinition("MsSale", Constraint.MUST_COMMUNICATE, "MsProduct"));
		system.addConstraint(new ConstraintDefinition("MsSale", Constraint.MUST_COMMUNICATE, "MsAuthentication"));		
		
		//communications
		system.addCommunication(new CommunicateDefinition("MsProduct", "MsCustomer", "/getCustomer")); //violation
		system.addCommunication(new CommunicateDefinition("MsProduct", "MsNewsletter")); //ok
		system.addCommunication(new CommunicateDefinition("MsSale", "MsCustomer", "/getCustomer")); //ok
		system.addCommunication(new CommunicateDefinition("MsSale", "MsAuthentication", "api/authenticate")); //ok
		system.addCommunication(new CommunicateDefinition("MsSale", "MsProduct")); //ok
		system.addCommunication(new CommunicateDefinition("MsProduct", "MsPostCode")); //violation
		/*
		 * absence: MsCustomer must-communicate MsNewsletter
		 */
		Set<ArchitecturalDrift> drifts = CommunicationChecker.getInstance().check(system);
		assertEquals(3, drifts.size());
	}
}
