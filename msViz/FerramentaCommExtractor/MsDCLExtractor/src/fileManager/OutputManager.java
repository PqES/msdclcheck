package fileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import core.CommunicateDefinition;
import core.MicroserviceDefinition;
import core.MicroservicesSystem;
import exception.MsDCLException;

public class OutputManager {

	private static final OutputManager instance = new OutputManager();

	public OutputManager() {
	}

	public static OutputManager getInstance() {
		return instance;
	}

	public static MicroservicesSystem createExtracts(MicroservicesSystem system, Set<String> dynamicsExtracts,
			String path) throws IOException { 

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
			String[] communications = communication.split(" ");
			String origin = communications[0];
			String destin = communications[2];
			String commInterface = communications[4];
			insertMicroserviceSystem(system, origin, destin, commInterface);

			writer.println(communication);
		}
		fileWriter.close();
		return system;

	}

	public static MicroservicesSystem insertMicroserviceSystem(MicroservicesSystem system, String origin, String destin,
			String commInterface) {

		MicroserviceDefinition msDefinitionOrigin = system.getMicroserviceDefinition(origin);
		MicroserviceDefinition msDefinitionDestin = system.getMicroserviceDefinition(destin);
		if (system.getCommunications(msDefinitionOrigin) == null) {
			system.addMicroservice(msDefinitionOrigin);
			CommunicateDefinition communication = new CommunicateDefinition(origin, destin, commInterface);
			system.addCommunication(communication);
		}
		if (system.getCommunications(msDefinitionDestin) == null) {
			MicroserviceDefinition ms = new MicroserviceDefinition(destin, " ", " ");
			CommunicateDefinition communication = new CommunicateDefinition(origin, destin, commInterface);
			system.addMicroservice(ms);
			system.addCommunication(communication);
		}
		else {
			CommunicateDefinition communication = new CommunicateDefinition(origin, destin, commInterface);
			system.addCommunication(communication);
		}

		return system;
	}

	public static void createConstraints(MicroservicesSystem constraints, String path) throws IOException {

		FileWriter fileWriter = new FileWriter(path);
		PrintWriter writer = new PrintWriter(fileWriter);
		for (MicroserviceDefinition ms : constraints.getMicroservices()) {
			writer.println(ms.getName() + ":" + " " + ms.getLink() + "; " + ms.getPath());

		}
		fileWriter.close();

	}
	public MicroservicesSystem createSystemFromFile(String path) throws IOException, MsDCLException {
		MicroservicesSystem system = new MicroservicesSystem();
		FileReader file = new FileReader(path + "/extracts.txt");
		BufferedReader buffer = new BufferedReader(file);
		String line; 
		MicroserviceDefinition currentService = null;
		while (buffer.ready()) {
			line = buffer.readLine();
			String[] communications = line.split(" " );
			String origin = communications[0];
			System.out.println("Origin: " + origin);
			String destin = communications[2];
			String interfaces = communications[4];
			MicroserviceDefinition ms = new MicroserviceDefinition(destin, " ", " ");
			CommunicateDefinition communication = new CommunicateDefinition(origin, destin, interfaces);
			system.addMicroservice(ms);
			system.addCommunication(communication);
		}
		return system;

	}
	public static void generateHTML(String html, String path) throws IOException {
		String texto = html;
		try {
		FileWriter fileWriter = new FileWriter(path+ "visualizacao.html");
		fileWriter.write(texto);
		
		fileWriter.close();
		System.out.println("Matriz das comunicações gerada com sucesso! ");
		}
		catch(Exception e) {
			
		}
	}
	
}
