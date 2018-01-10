package msdcl.communicationVerify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;

import entities.MicroserviceDefinition;
import entities.MicroservicesSystem;
import msdcl.communicationExtractor.JavaDepExtractor;
import pidclcheck.core.DependencyConstraint.ArchitecturalDrift;
import pidclcheck.exception.ParseException;
import pidclcheck.main.Main;

public class Pi_DCL {

	private  final static Pi_DCL instance = new Pi_DCL();
	
	public Pi_DCL() {}
	
	public static Pi_DCL getInstance() {
		return instance;
	}
	

	public  void validateLocalArchitectures(MicroservicesSystem system)
			throws IOException, ParseException {
		Collection<MicroserviceDefinition> allMicroservices = system.getMicroservices();
		for (MicroserviceDefinition ms : allMicroservices) {
			String deps = JavaDepExtractor.getInstance().getDependenciesExtracted(ms);
			ByteArrayInputStream depsAllMicroservice = new ByteArrayInputStream (deps.getBytes());
			String constraints = system.getDcl(ms);
			ByteArrayInputStream DCLconstraints = new ByteArrayInputStream(system.getDcl(ms).getBytes());
			Collection<ArchitecturalDrift> drifts = Main.validateLocalArchitecture( depsAllMicroservice, DCLconstraints);
			for(ArchitecturalDrift drift : drifts){
				
				System.out.println(drift.getInfoMessage() + ",[" + drift.getViolatedConstraint() + "]");
			}
			System.out.println("fim");
			
			
		}
	}
	
}
