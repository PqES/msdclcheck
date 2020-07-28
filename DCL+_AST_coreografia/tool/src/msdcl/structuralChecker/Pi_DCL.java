package msdcl.structuralChecker;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import msdcl.communicationExtractor.JavaDepExtractor;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;
import pidclcheck.core.DependencyConstraint.ArchitecturalDrift;
import pidclcheck.exception.ParseException;
import pidclcheck.main.Main;

public class Pi_DCL {

	private final static Pi_DCL instance = new Pi_DCL();

	public Pi_DCL() {
	}

	public static Pi_DCL getInstance() {
		return instance;
	}

	public Map<MicroserviceDefinition, Collection<pidclcheck.core.DependencyConstraint.ArchitecturalDrift>> validateLocalArchitectures(MicroservicesSystem system)
			throws IOException, ParseException {
		Collection<MicroserviceDefinition> allMicroservices = system.getMicroservices();
		Map<MicroserviceDefinition, Collection<ArchitecturalDrift>> allDrifts = new HashMap<>();
		Collection<ArchitecturalDrift> drifts = null;
		for (MicroserviceDefinition ms : allMicroservices) {
			String deps = JavaDepExtractor.getInstance().getDependenciesExtracted(ms);
			
			ByteArrayInputStream depsAllMicroservice = new ByteArrayInputStream(deps.getBytes());
			ByteArrayInputStream DCLconstraints = new ByteArrayInputStream(system.getDcl(ms).getBytes());
			
			//Process p = Runtime.getRuntime().exec(" java -jar pi-dclcheck.jar " + depsAllMicroservice + ms.getPath() +  DCLconstraints);
			//BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
						
			
			
			drifts =  Main.validateLocalArchitecture(depsAllMicroservice, DCLconstraints);
			allDrifts.put(ms, drifts);
		}
		return allDrifts;
	}

	
}
