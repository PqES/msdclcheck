package communicationExtractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import fileManager.InputManager;
import msdcl.core.CommunicateDefinition;
import msdcl.core.ConstraintDefinition;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;
import msdcl.core.ModuleDefinition;
import msdcl.exception.MsDCLException;

public class CommunicationExtractor {

	private final static CommunicationExtractor instance = new CommunicationExtractor();

	HashMap<String, ArrayList> dependenciesFromService = new HashMap<>();

	public CommunicationExtractor() {
	}

	public static CommunicationExtractor getInstance() {

		return instance;
	}

	public HashMap<ModuleDefinition, Set<CommunicateDefinition>> extractCommunicatesSystem(
			HashMap<ModuleDefinition, Set<CommunicateDefinition>> communications, MicroservicesSystem system) {
		for (ModuleDefinition module : system.getAllModules()) {
			if (module.getName().equals("$system")) {
				communications.put(module, new HashSet<>());
				for (ModuleDefinition atual : communications.keySet()) {
					if (atual.getName().equals(module.getName()))
						continue;
					
					Set<CommunicateDefinition> communicatesMSAtual = communications.get(atual);
					Set<CommunicateDefinition> systemModule = communications.get(module);
					systemModule.addAll(communicatesMSAtual);
					communications.put(module, systemModule);

				}
			}
		}
		return communications;

	}

	public HashMap<ModuleDefinition, Set<CommunicateDefinition>> analyseAllMicroservice(MicroservicesSystem system,
			String communicationFile) throws IOException, MsDCLException, InterruptedException {

		HashMap<ModuleDefinition, Set<CommunicateDefinition>> map = new HashMap<>();
		InputManager inputManager = new InputManager();
		HashMap<ModuleDefinition, Set<CommunicateDefinition>> communications = inputManager
				.readFileCommunications(new File(communicationFile), system);
		Set<CommunicateDefinition> dependenciesFromServiceCalle = new HashSet<>();
		for (ModuleDefinition module : communications.keySet()) {
			for (CommunicateDefinition communication : communications.get(module)) {
				if (communication != null) {
					Set<ConstraintDefinition> constraints = system.getConstraints(communication.getModule());
					if (constraints != null) {
						dependenciesFromServiceCalle = communications.get(module);
						map.put(module, dependenciesFromServiceCalle);
					}
				}

			}
		}
		map = extractCommunicatesSystem(map, system);

		return map;
	}
}
