package msdcl.main;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import fileManager.InputManager;
import fileManager.OutputManager;
import msdcl.communicationExtractor.CommunicationExtractor;
import msdcl.core.MicroservicesSystem;
import msdcl.exception.MsDCLException;
import util.Util;

public class Run {
	public static void main(String[] args) throws MsDCLException {
		try {

			if (args.length != 1) {
				System.out.println("Usage:\n comextractor.jar [microservices path]");
				return;
			} else {

				String path = args[0];
				Util util = new Util();
				File dir = new File(path);
				util.readFileConfig(dir);
				Set<String> dynamicsExtracts = util.getExtracts();
				MicroservicesSystem system = getConstraints(path);
				system.setCommunications(CommunicationExtractor.getInstance().analyseAll(system));
				OutputManager.getInstance().createExtracts(system, dynamicsExtracts, path);
				System.out.println("Extração de Comunicações realizada com Sucesso!");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// path da especificação arquitetural
	public static MicroservicesSystem getConstraints(String architecturalSpecification) throws IOException {

		InputManager inputManager = new InputManager();
		MicroservicesSystem system = inputManager.readFile(new File(architecturalSpecification + "/constraints.txt"));
		return system;
	}

}