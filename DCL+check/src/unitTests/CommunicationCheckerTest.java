//package unitTests;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.Map;
//import java.util.Set;
//
//import org.junit.Test;
//
//import enums.Constraint;
//import msdcl.communicationChecker.ArchitecturalDrift;
//import msdcl.communicationChecker.CommunicationChecker;
//import msdcl.core.CommunicateDefinition;
//import msdcl.core.ConstraintDefinition;
//import msdcl.core.MicroserviceDefinition;
//import msdcl.core.MicroservicesSystem;
//
//public class CommunicationCheckerTest {
//
//	private MicroservicesSystem createMicroserviceSystem(String name){
//		MicroservicesSystem system = new MicroservicesSystem();
//			system.addMicroservice(new MicroserviceDefinition(name, null, null, null));
//		return system;
//	}
//	
//	private MicroservicesSystem createMicroserviceSystem(String names[]){
//		MicroservicesSystem system = new MicroservicesSystem();
//		for(int i = 0; i < names.length; i++){
//			system.addMicroservice(new MicroserviceDefinition(names[i], null, null, null));
//		}
//		return system;
//	}
//	
//	@Test
//	public void canCommunicate() {
//		System.out.println("Veio do CAN?");
//		String servicesName[] = {"front-end", "carts", "orders", "catalogue"};
//		MicroservicesSystem system = createMicroserviceSystem(servicesName);		
//		system.addConstraint(new ConstraintDefinition("front-end","front-end/api/cart/index", Constraint.CAN_COMMUNICATE, "carts", "catalogue/getCatalogues"));
//		system.addCommunication(new CommunicateDefinition("front-end", "front-end/api/cart/index","carts"));
//		Map<MicroserviceDefinition, Set<ArchitecturalDrift>> drifts = CommunicationChecker.getInstance().check(system);
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//		assertEquals(1, drifts.size());
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//	}
//	
//	
//	@Test
//	public void cannotCommunicate() {
//		System.out.println("Veio do CANNOT?");
//	//	MicroservicesSystem system = createMicroserviceSystem("front-end");
//		String servicesName[] = {"front-end", "carts", "orders", "catalogue"};
//		MicroservicesSystem system = createMicroserviceSystem(servicesName);
//		system.addConstraint(new ConstraintDefinition("front-end","$front-end/api/cart/index", Constraint.CANNOT_COMMUNICATE, "catalogue"));
//		system.addCommunication(new CommunicateDefinition("front-end", "front-end/api/cart/index", "catalogue"));
//		Map<MicroserviceDefinition, Set<ArchitecturalDrift>> drifts = CommunicationChecker.getInstance().check(system);
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//		assertEquals(1, drifts.size());
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//	}
//	
//	@Test
//	public void canCommunicateOnly() {
//		System.out.println("Veio do CAN-ONLY?");
//		//MicroservicesSystem system = createMicroserviceSystem("front-end");
//		String servicesName[] = {"front-end", "carts", "orders", "catalogue"};
//		MicroservicesSystem system = createMicroserviceSystem(servicesName);
//		system.addConstraint(new ConstraintDefinition("front-end","$front-end/api/cart/index", Constraint.CAN_COMMUNICATE_ONLY, "carts","carts/$custId"));
//		system.addCommunication(new CommunicateDefinition("front-end", "front-end/api/cart/index", "catalogue"));
//		//system.addCommunication(new CommunicateDefinition("ms1", "ms3"));
//		Map<MicroserviceDefinition, Set<ArchitecturalDrift>> drifts = CommunicationChecker.getInstance().check(system);
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//		assertEquals(1, drifts.size());
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//	}
//	
//	@Test
//	public void canOnlyCommunicate() {
//		System.out.println("Veio do ONLY-CAN?");
//		String servicesName[] = {"front-end", "carts", "orders", "catalogue"};
//		MicroservicesSystem system = createMicroserviceSystem(servicesName);
//		system.addConstraint(new ConstraintDefinition("front-end","front-end/api/orders/index", Constraint.ONLY_CAN_COMMUNICATE, "orders", "/orders"));
//		system.addCommunication(new CommunicateDefinition("front-end", "front-end/api/orders/index", "orders"));
//		system.addCommunication(new CommunicateDefinition("front-end", "front-end/api/carts/index", "orders","/orders"));
//
//		Map<MicroserviceDefinition, Set<ArchitecturalDrift>> drifts = CommunicationChecker.getInstance().check(system);
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//		assertEquals(3, drifts.size());
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//	}
//	
//	@Test
//	public void mustCommunicate() {
//		System.out.println("Veio do MUST?");
//		String servicesName[] = {"front-end", "carts", "orders", "catalogue"};
//		MicroservicesSystem system = createMicroserviceSystem(servicesName);
////		MicroservicesSystem system2 = createMicroserviceSystem("carts");
//		system.addConstraint(new ConstraintDefinition("front-end","front-end/api/cart/index", Constraint.MUST_COMMUNICATE, "carts", "catalogue/getCatalogues"));
//		//system.addConstraint(new ConstraintDefinition("ms1", Constraint.MUST_COMMUNICATE, "ms3"));
//		system.addCommunication(new CommunicateDefinition("front-end","front-end/api/cart/index", "carts", "catalogue"));
//		Map<MicroserviceDefinition, Set<ArchitecturalDrift>> drifts = CommunicationChecker.getInstance().check(system);
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//		assertEquals(1, drifts.size());
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//	}
//	
//	@Test
//	public void warning() {
//		System.out.println("Veio do WARNING?");
//		String servicesName[] = {"front-end", "carts", "orders", "catalogue"};
//		MicroservicesSystem system = createMicroserviceSystem(servicesName);
//		system.addCommunication(new CommunicateDefinition("front-end", "front-end/api/user/index", "user"));
//		
//		Map<MicroserviceDefinition, Set<ArchitecturalDrift>> drifts = CommunicationChecker.getInstance().check(system);
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//		assertEquals(1, drifts.size());
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//	}
//	
////	@Test
////	public void usingOnly(){
////		MicroservicesSystem system = createMicroserviceSystem(2);
////		system.addConstraint(new ConstraintDefinition("ms1", Constraint.CAN_COMMUNICATE_ONLY, "ms2", "/rest1"));
////		system.addCommunication(new CommunicateDefinition("ms1", "ms2", "/rest2"));
////		Map<MicroserviceDefinition, Set<ArchitecturalDrift>> drifts = CommunicationChecker.getInstance().check(system);
////		assertEquals(1, drifts.size());
////	}
//	
//	@Test
//	public void dynamicUsing(){
//		String servicesName[] = {"front-end", "carts", "orders", "catalogue"};
//		MicroservicesSystem system = createMicroserviceSystem(servicesName);
//		system.addConstraint(new ConstraintDefinition("front-end","front-end/api/catalogue/index" ,Constraint.CANNOT_COMMUNICATE, "catalogue", "/catalogue/*"));
//		system.addConstraint(new ConstraintDefinition("front-end","front-end/api/cart/index", Constraint.MUST_COMMUNICATE, "carts", "/cart/*/login"));
//		system.addCommunication(new CommunicateDefinition("front-end","front-end/api/cart/index", "carts","/cart/login/*" ));
//		system.addCommunication(new CommunicateDefinition("front-end","front-end/api/catalogue/index", "carts","/catalogue/*/teste" ));
//		Map<MicroserviceDefinition, Set<ArchitecturalDrift>> drifts = CommunicationChecker.getInstance().check(system);
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//		assertEquals(1, drifts.size());
//		for(MicroserviceDefinition ms : drifts.keySet()) {
//			for(ArchitecturalDrift d : drifts.get(ms)) {
//				System.out.println(d.getMessage());
//			}
//		}
//	}
//	
////	@Test
////	public void toyExample(){						
////		String servicesName[] = {"front-end", "carts", "orders", "catalogue"};
////		MicroservicesSystem system = createMicroserviceSystem(servicesName);
////		
////		//constraints
////		system.addConstraint(new ConstraintDefinition("front-end","front-end/api/cart/index", Constraint.CAN_COMMUNICATE, "carts", "catalogue/getCatalogues"));
////		system.addCommunication(new CommunicateDefinition("front-end", "front-end/api/cart/index","carts"));
////		
////		system.addConstraint(new ConstraintDefinition("front-end","$front-end/api/cart/index", Constraint.CANNOT_COMMUNICATE, "catalogue"));
////		system.addCommunication(new CommunicateDefinition("front-end", "front-end/api/cart/index", "catalogue"));
////		
////		system.addConstraint(new ConstraintDefinition("front-end","$front-end/api/cart/index", Constraint.CAN_COMMUNICATE_ONLY, "carts","carts/$custId"));
////		system.addCommunication(new CommunicateDefinition("front-end", "front-end/api/cart/index", "catalogue"));
////		
////		system.addConstraint(new ConstraintDefinition("front-end","$front-end/api/orders/index", Constraint.ONLY_CAN_COMMUNICATE, "orders", "/orders"));
////		system.addCommunication(new CommunicateDefinition("front-end", "front-end/api/orders/index", "orders"));
////
////		//absence: MsCustomer must-communicate MsNewsletter
////
////		system.addConstraint(new ConstraintDefinition("front-end","front-end/api/cart/index", Constraint.MUST_COMMUNICATE, "carts", "catalogue/getCatalogues"));
////
////		
////		Map<MicroserviceDefinition, Set<ArchitecturalDrift>> drifts = CommunicationChecker.getInstance().check(system);
////		for(MicroserviceDefinition ms : drifts.keySet()) {
////			for(ArchitecturalDrift d : drifts.get(ms)) {
////				System.out.println(d.getMessage());
////			}
////		}
////		assertEquals(1, drifts.size());
////		
////		for(MicroserviceDefinition ms : drifts.keySet()) {
////			for(ArchitecturalDrift d : drifts.get(ms)) {
////				System.out.println(d.getMessage());
////			}
////		}
////		
////	}
//	
//}