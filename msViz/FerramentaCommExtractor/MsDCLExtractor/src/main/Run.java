package main;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import communicationExtractor.CommunicationExtractor;
import core.CommunicateDefinition;
import core.MicroserviceDefinition;
import core.MicroservicesSystem;
import exception.MsDCLException;
import fileManager.InputManager;
import fileManager.OutputManager;
import util.Util;
import visualization.GraphVisualization;
import visualization.MatrizVisualization;

public class Run {
	public static void main(String[] args) throws MsDCLException {
		try {

//			if (args.length != 1) {
//				System.out.println("Usage:\n commextractor.jar [microservices path]");
//				return;
//			} else {
			String path = "/home/elena/Documentos/msViz/AplicacaoExemploPratico";
			//	String path = args[0];
				Util util = new Util();
				File dir = new File(path);
				util.readFileConfig(dir);
				util.createConstraints(path);				
				
				Set<String> dynamicsExtracts = util.getExtracts();
				
				MicroservicesSystem system = getConstraints(path); 
				system.setCommunications(CommunicationExtractor.getInstance().analyseAll(system));
				OutputManager.getInstance().createExtracts(system, dynamicsExtracts, path);
				
				System.out.println("Extração de Comunicações realizada com Sucesso!");
				System.out.println();
				MatrizVisualization matriz = new MatrizVisualization();
				String[][] informations = matriz.addCommunications(system);
				matriz.updateMatriz(informations);
				String html = matriz.createHtmlTable(informations);
				OutputManager.getInstance().generateHTML(html, path);
				System.out.println();
				GraphVisualization g = new GraphVisualization();
				g.generateGraph(path, matriz.getCommunications());
				System.out.println();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	// path da especificação arquitetural
	public static MicroservicesSystem getConstraints(String architecturalSpecification) throws IOException {

		InputManager inputManager = new InputManager();
		MicroservicesSystem system = inputManager.readFile(new File(architecturalSpecification + "constraints.txt"));
		return system;
	}
	

	
}