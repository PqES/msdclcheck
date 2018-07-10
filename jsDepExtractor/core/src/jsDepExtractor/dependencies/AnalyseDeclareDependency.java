//package jsDepExtractor.dependencies;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//import jsDepExtractor.Util;
//
//public class AnalyseDeclareDependency {
//	private static AnalyseDeclareDependency instance;
//
//	private AnalyseDeclareDependency() {
//	}
//
//	public static AnalyseDeclareDependency getInstance() {
//		if (instance == null) {
//			instance = new AnalyseDeclareDependency();
//		}
//		return instance;
//	}
//
////	private void getObjectDeclare(File f, String path) throws IOException {
////		System.out.println("Name File: " + f.getName());
////
////		FileReader file = new FileReader(f);
////		BufferedReader buffer = new BufferedReader(file);
////		String line;
////		line = buffer.readLine();
////		while (buffer.ready()) {
////			line = buffer.readLine();
////			String[] contents = line.split(",");
////			String name = contents[2].replace("\"", "");
////			System.out.println("Name: " + name);
////			if (name.equalsIgnoreCase("DECLARATION")) {
////				String module1 = contents[0];
////				String module2 = contents[1];
////				DeclareDependency dep = new DeclareDependency(module1, module2);
////				System.err.println("Dependencia: " + dep.toString());
////				Util.writeFile(path, dep.toString());
////			}
////
////		}
//
//	}
//
//	public void analyseFiles(String pathProject) throws IOException {
//		Path path = Paths.get("log/functions/");
//		File file = path.toFile();
//	//	System.out.println("File do log: " + file.getAbsolutePath());
//		List<File> projectFiles = Util.getAllFiles(path.toString(), pathProject);
//
//		for (File f : projectFiles) {
//	//		System.out.println("Cada file: " + f.getAbsolutePath());
//			if (f.getName().contains("declarations"))
//				getObjectDeclare(f, pathProject);
//
//		}
//
//	}
//}
