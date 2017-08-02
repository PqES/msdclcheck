package communicationExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entities.MicroserviceDefinition;
import entities.MicroservicesSystem;
import util.Util;

public class CommunicationExtractor {

	private final static CommunicationExtractor instance = new CommunicationExtractor();

	public CommunicationExtractor() {

	}

	public static CommunicationExtractor getInstance() {
		return instance;
	}

	private String extractUsing(String line, String link) {
		Pattern p = Pattern.compile("\".*" + link + "(/\\w*)*\"");
		Matcher m = p.matcher(line);
		String using = "";
		while (m.find()) {
			String completeLink = m.group().replaceAll("\"", "");
			if (completeLink.endsWith("/")) {
				completeLink = completeLink.substring(0, completeLink.length() - 1);
			}
			String route[] = completeLink.split(link);
			using = route[route.length - 1];
		}
		if (!using.isEmpty()) {
			return using;
		}
		return null;
	}

	private Set<CommunicateDefinition> checkAccess(File f, MicroserviceDefinition caller,
			Collection<MicroserviceDefinition> allMicroservices) throws IOException {
		Set<CommunicateDefinition> accesses = new HashSet<>();
		FileReader fr = new FileReader(f);
		BufferedReader buffer = new BufferedReader(fr);
		String line;
		while (buffer.ready()) {
			line = buffer.readLine();
			for (MicroserviceDefinition m : allMicroservices) {
				if (line.contains(m.getLink())) {
					String using = extractUsing(line, m.getLink());
					accesses.add(new CommunicateDefinition(caller, m, using));
				}
			}
		}
		buffer.close();
		fr.close();
		return accesses;
	}

	public Set<CommunicateDefinition> analyse(MicroserviceDefinition caller, Collection<MicroserviceDefinition> allMicroservices) throws IOException {
		Set<CommunicateDefinition> accesses = new HashSet<>();
		List<File> javaFiles = Util.getAllFiles(new File(caller.getPath()));
		for (File f : javaFiles) {
			accesses.addAll(checkAccess(f, caller, allMicroservices));
		}
		return accesses;
	}

	public HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> analyseAll(MicroservicesSystem system) throws IOException {
		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> map = new HashMap<>();
		for (MicroserviceDefinition caller : system.getMicroservices()) {
			Set<CommunicateDefinition> accesses = new HashSet<>();
			accesses.addAll(this.analyse(caller, system.getMicroservices()));
			map.put(caller, accesses);
		}
		return map;
	}
}

