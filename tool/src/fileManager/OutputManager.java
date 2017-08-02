package fileManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import communicationAnalyser.CommunicationChecker;
import communicationAnalyser.drift.ArchitecturalDrift;

public class OutputManager {

	public static void violates(Set<ArchitecturalDrift> drifts) throws IOException{
		FileWriter fileWriter = new FileWriter("violates.txt");
		PrintWriter writer = new PrintWriter(fileWriter);		
		
		for (ArchitecturalDrift a : drifts) {
			System.out.println(a.getMessage());
			writer.println(a.getMessage());
		}
		
		fileWriter.close();
		
	}
}
