package communicationExtractor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import msdcl.core.CommunicateDefinition;
import msdcl.core.ConstraintDefinition;
import msdcl.core.MicroservicesSystem;
import msdcl.exception.MsDCLException;

public class DependencyExtractor {

	public final static DependencyExtractor instance = new DependencyExtractor();

	public DependencyExtractor() {
	}

	public static DependencyExtractor getInstance() { 
		return instance;
	}

	public Set<CommunicateDefinition> extractDependenciesFromService(MicroservicesSystem system,
			String communicationFile, HashMap<String, Set<CommunicateDefinition>> communications)
			throws IOException, MsDCLException {

		for (String serviceName : communications.keySet()) {
			for (CommunicateDefinition module : communications.get(serviceName)) {
				Set<ConstraintDefinition> constraints = system.getConstraints(module.getModule());
				if (constraints != null)
					return communications.get(serviceName);
			}

		}
		return null;

	}

}
