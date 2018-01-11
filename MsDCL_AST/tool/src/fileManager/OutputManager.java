package fileManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import msdcl.communicationChecker.ArchitecturalDrift;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;

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

	public static void violate(String path, Set<ArchitecturalDrift> driftsCommunications,
			Map<MicroserviceDefinition, Collection<pidclcheck.core.DependencyConstraint.ArchitecturalDrift>> driftsStructurals)
			throws IOException {
		FileWriter fileWriter = new FileWriter(path + "violates.txt");
		PrintWriter writer = new PrintWriter(fileWriter);

		for (MicroserviceDefinition m : driftsStructurals.keySet()) {
			System.out.println(m.getName());
			writer.println(m.getName());
			System.out.println("\tCommunication Violates: ");
			writer.println("\tCommunication Violates: ");
			for (ArchitecturalDrift a : driftsCommunications) {
				System.out.println("\t\t" + a.getMessage());
				writer.println("\t\t" + a.getMessage());
			}
			if (driftsStructurals.get(m).isEmpty()) {
				System.out.println("\tNot exists Structurals Design Violations! ");
				writer.println("\tNot exists Structurals Design Violations! ");
			} else {
				System.out.println("\t Structural Violates: ");
				writer.println("\tStructural Violates: ");

				for (pidclcheck.core.DependencyConstraint.ArchitecturalDrift a : driftsStructurals.get(m)) {
					System.out.println("\t\t" + a.getInfoMessage() + ",[" + a.getViolatedConstraint() + "]");
					writer.println("\t\t" + a.getInfoMessage() + ",[" + a.getViolatedConstraint() + "]");
				}
			}
			System.out.println();
			writer.println();

		}
		fileWriter.close();
	}
}
