package fileManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import communicationChecker.ArchitecturalDrift;
import communicationChecker.WarningConstraint;
import msdcl.core.ConstraintDefinition;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;
import msdcl.core.ModuleDefinition;
import util.RulesRegex;

public class OutputManager {

	public static void violates(Set<ArchitecturalDrift> drifts) throws IOException {
		FileWriter fileWriter = new FileWriter("violates.txt");
		PrintWriter writer = new PrintWriter(fileWriter);

		for (ArchitecturalDrift a : drifts) {
			System.out.println(a.getMessage());
			writer.println(a.getMessage());
		}

		fileWriter.close();

	}

	public static void violateCommunicates(Map<ModuleDefinition, Set<ArchitecturalDrift>> driftsCommunications,
			MicroservicesSystem system) throws IOException {
		FileWriter fileWriter = new FileWriter("violates.txt");
		PrintWriter writer = new PrintWriter(fileWriter);
		System.out.println("\tCommunication Violates: ");
		writer.println("Communication Violates: ");

		for (ModuleDefinition ms : driftsCommunications.keySet()) {
			System.out.println();
			System.out.println("\t\t" + ms.getName());
			writer.println();
			writer.println("\t" + ms.getName());
			for (ArchitecturalDrift drift : driftsCommunications.get(ms)) {
				if (drift instanceof WarningConstraint) {
					// if (((WarningConstraint)
					// drift).getMicroserviceDestin().equalsIgnoreCase(ms.getName())) {
					System.out.println("\t\t" + drift.getMessage());
					writer.println("\t" + drift.getMessage());
					// }
				} else {
					ConstraintDefinition c = drift.getViolateConstraint();
					if (c != null) {

						// if (c.getMicroserviceOrigin().equals(ms.getName())) {
						System.out.println("\t\t" + drift.getMessage());
						writer.println("\t" + drift.getMessage());
						// }
					}
				}
			}

			
		}
		System.out.println();
		writer.println();

		fileWriter.close();
	}

	public static void violateStructural(
			Map<MicroserviceDefinition, Collection<pidclcheck.core.DependencyConstraint.ArchitecturalDrift>> driftsStructurals,
			MicroservicesSystem system) throws IOException {
		
		

		for (MicroserviceDefinition ms : driftsStructurals.keySet()) {
			FileWriter fileWriter = new FileWriter(ms.getPath()+"/violates.txt");
			PrintWriter writer = new PrintWriter(fileWriter);
			System.out.println("\tStructural Violates: ");
		//	writer.println("	Structural Violates: ");
			System.out.println("Microservice " + ms.getName() + " Structural Drifts:");
			for (pidclcheck.core.DependencyConstraint.ArchitecturalDrift drift : driftsStructurals.get(ms)) {
				//System.out.println(getNewMessageFormat(drift.getInfoMessage(),drift));
			//	System.out.println(getNewMessageFormat(drift.getInfoMessage().toString(),drift.getViolatedConstraint().toString()));
				System.out.println("" + drift.getInfoMessage() + ",[" + drift.getViolatedConstraint() + "]");
				System.out.println();
				writer.println( drift.getInfoMessage() + ",[" + drift.getViolatedConstraint() + "]");
			//	writer.println();
			}
			writer.close();
		}
		
	}
	public static String getNewMessageFormat(String infoMessage, String violationConstraint) {
		String message = infoMessage.replaceAll("\\[", "").replaceAll("\\]", "");
		String []constraints = message.split(",");
		String type = constraints[0];
		String classOrigin = constraints [1];
		String classDestin = constraints [3];
		String typeRestriction = constraints[2];
		if(type.equalsIgnoreCase("divergence")) {
			return "Divergenge: The class "+classOrigin + " "+typeRestriction + " object of type "+ classDestin + " ("+
					violationConstraint+")";
		}
		else if(type.equalsIgnoreCase("absence")) {
			return "Ausence: The class " + classOrigin +" NOT " + typeRestriction + " " + classDestin + " ("
					+violationConstraint + ")";
		}
		return null;
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
