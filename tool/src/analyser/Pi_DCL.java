package analyser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import entities.MicroserviceDefinition;
import pidclcheck.core.DependencyConstraint.ArchitecturalDrift;
import pidclcheck.exception.ParseException;
import pidclcheck.main.Main;

public class Pi_DCL {

	private static ByteArrayInputStream getDependencies(MicroserviceDefinition microservice) throws IOException {
		File file = new File(microservice.getPath() + "/dependencies.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		StringBuilder content = new StringBuilder();
		while (br.ready()) {
			content.append(br.readLine()).append('\n');
		}
		br.close();
		fr.close();
		return new ByteArrayInputStream(content.toString().getBytes());
	}

	public static void validateLocalArchitecture(HashMap<MicroserviceDefinition, String> mapDcl)
			throws IOException, ParseException {
		for (Entry<MicroserviceDefinition, String> entry : mapDcl.entrySet()) {
			
			ByteArrayInputStream deps = getDependencies(entry.getKey());
			ByteArrayInputStream dcls = new ByteArrayInputStream(entry.getValue().getBytes());
			System.out.println(entry.getKey().getName());
			Collection<ArchitecturalDrift> drifts = Main.validateLocalArchitecture(deps, dcls);
			for(ArchitecturalDrift drift : drifts){
				System.out.println(drift.getInfoMessage() + ",[" + drift.getViolatedConstraint() + "]");
			}
			System.out.println("fim");
		}
	}
}
