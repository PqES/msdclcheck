package jsDepExtractor.dependencies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import jsDepExtractor.Util;

public class AnalyseCreateDependency {

	private static AnalyseCreateDependency instance;

	private AnalyseCreateDependency() {
	}

	public static AnalyseCreateDependency getInstance() {
		if (instance == null) {
			instance = new AnalyseCreateDependency();
		}
		return instance;
	}

	private void getObjectCreate(File f, String path) throws IOException {
		ArrayList<CreateDependency> createsDependencies = new ArrayList<>();
	//	System.out.println("Name File: " + f.getName());

		FileReader file = new FileReader(f);
		BufferedReader buffer = new BufferedReader(file);
		String line;
		line = buffer.readLine();
		while (buffer.ready()) {
			line = buffer.readLine();
			String[] contents = line.split(",");
			//System.out.println("Contents[0]: " + contents[0]);
			//System.err.println("Linha atual: " + line);
			String module1 = contents[7];
			String[] nameModule1 = module1.split("\\(");
			String module2 = contents[8];
			String[] nameModule2 = module2.split("\\(");
			CreateDependency dep = new CreateDependency(nameModule1[0], nameModule2[0]);
			System.out.println("Module1: " + nameModule1[0]);
			System.out.println("Module2: " + nameModule2[0]);
			System.err.println("Dependencia: " + dep.toString());
			Util.writeFile(path, dep.toString());

		}

	}

	public void analyseFiles(String pathProject) throws IOException {
		Path path = Paths.get("log/legacy/classes/");
		File file = path.toFile();
	//	System.out.println("File do log: " + file.getAbsolutePath());
		List<File> projectFiles = Util.getAllFiles(path.toString(), pathProject);

		for (File f : projectFiles) {
			//System.out.println("Cada file: " + f.getAbsolutePath());
			getObjectCreate(f, pathProject);

		}

	}

}
