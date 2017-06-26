package analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import entities.CommunicateDefinition;
import entities.MicroserviceDefinition;
import util.Util;

public class AccessAnalyser {

	private final static AccessAnalyser instance = new AccessAnalyser();

	public AccessAnalyser() {

	}

	public static AccessAnalyser getInstance() {
		return instance;
	}

	private Set<CommunicateDefinition> checkAccess(File f, MicroserviceDefinition caller,
			Set<MicroserviceDefinition> allMicroservices) throws IOException {
		Set<CommunicateDefinition> accesses = new HashSet<>();
		FileReader fr = new FileReader(f);
		BufferedReader buffer = new BufferedReader(fr);
		String line;
		while (buffer.ready()) {
			line = buffer.readLine();
			for (MicroserviceDefinition m : allMicroservices) {

				if (line.contains(m.getLink())) {

					accesses.add(new CommunicateDefinition(caller, m));
				}
			}
		}
		buffer.close();
		fr.close();
		return accesses;
	}

	public Set<CommunicateDefinition> analyse(MicroserviceDefinition caller, Set<MicroserviceDefinition> allMicroservices)
			throws IOException {

		Set<CommunicateDefinition> accesses = new HashSet<>();

		List<File> javaFiles = Util.getAllFiles(new File(caller.getPath()));
		for (File f : javaFiles) {
			accesses.addAll(checkAccess(f, caller, allMicroservices));
		}
		return accesses;
	}

	public HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> analyseAll(
			Set<MicroserviceDefinition> allMicroservices) throws IOException {

		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> map = new HashMap<>();
		for (MicroserviceDefinition caller : allMicroservices) {

			Set<CommunicateDefinition> accesses = new HashSet<>();
			accesses.addAll(this.analyse(caller, allMicroservices));
			map.put(caller, accesses);
		}
		return map;
	}
}
