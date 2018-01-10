package fileManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import communicationChecker.CommunicationChecker;
import communicationChecker.drift.ArchitecturalDrift;
import entities.MicroserviceDefinition;

public class OutputManager {

	public static void violates(MicroserviceDefinition ms,Set<ArchitecturalDrift> driftsC, Set<ArchitecturalDrift> drifts) throws IOException{
		FileWriter fileWriter = new FileWriter("violates.txt");
		PrintWriter writer = new PrintWriter(fileWriter);		
		
		for (ArchitecturalDrift a : drifts) {
			System.out.println(a.getMessage()); 
			writer.println(a.getMessage());
		}
		
		fileWriter.close();
		
	}
}
