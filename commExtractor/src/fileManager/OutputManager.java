package fileManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import msdcl.core.CommunicateDefinition;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;

public class OutputManager {

	private static final OutputManager instance = new OutputManager();

	public OutputManager() {
	}

	public static OutputManager getInstance() {
		return instance;
	}

	
	public static void createExtracts(MicroservicesSystem system, Set<String> dynamicsExtracts, String path)
			throws IOException {

		FileWriter fileWriter = new FileWriter(path + "/extracts.txt");
		PrintWriter writer = new PrintWriter(fileWriter);

		for (MicroserviceDefinition ms : system.getMicroservices()) {
			for (CommunicateDefinition com : system.getCommunications(ms)) {
				String communication = com.getMicroserviceOrigin() + " communicate " + com.getMicroserviceDestin()
						+ " using " + com.getUsing();
				writer.println(communication);
			}
		}

		for (String ms : dynamicsExtracts) {
			String communication = ms;
			writer.println(communication);
		}
		fileWriter.close();

	}

	public static void createConstraints(MicroservicesSystem constraints, String path) throws IOException {

		FileWriter fileWriter = new FileWriter(path + "constraints2.txt");
		PrintWriter writer = new PrintWriter(fileWriter);
		for (MicroserviceDefinition ms : constraints.getMicroservices()) {
			// writer.write();
			writer.println(ms.getName() + ":" + " " + ms.getLink() + "; " + ms.getPath());

		}
		fileWriter.close();

	}
}
