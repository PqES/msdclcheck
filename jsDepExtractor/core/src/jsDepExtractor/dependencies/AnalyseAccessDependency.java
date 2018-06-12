package jsDepExtractor.dependencies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import jsDepExtractor.Util;

public class AnalyseAccessDependency {
	private static AnalyseAccessDependency instance;

	private AnalyseAccessDependency() {
	}

	public static AnalyseAccessDependency getInstance() {
		if (instance == null) {
			instance = new AnalyseAccessDependency();
		}
		return instance;
	}

	private void getObjectAccess(File f, String path) throws IOException {
		System.out.println("Name File: " + f.getName());

		FileReader file = new FileReader(f);
		BufferedReader buffer = new BufferedReader(file);
		String line;
		line = buffer.readLine();
		while (buffer.ready()) {
			line = buffer.readLine();
			String[] contents = line.split(",");
		//	System.out.println("Contents[0]: " + contents[0]);
			//System.err.println("Linha atual: " + line);
			String name = contents[1].replace("\"", "");			
			System.out.println("Name: " + name);
			if (name.equalsIgnoreCase("require")) {
				String module1 = contents[0];
				String nameAux = contents[4];
			//	System.out.println("Module1: "+ module1);
			//	System.out.println("nameAux: "+ nameAux);
				String module2 = analyseName(nameAux);
			//	System.err.println("Module2: " + module2);
				AccessDependency dep = new AccessDependency(module1, module2);
			//	System.err.println("Dependencia: " + dep.toString());
				Util.writeFile(path, dep.toString());
			}

		}

	}

	private String analyseName(String nameAux) {
		nameAux = nameAux.replaceAll("\"","");
		String regex_nameModules = "^['].+[']$";
		String regex_localModules = "^['./].*[']$";
		System.out.println("NameAux que chegou: " + nameAux);

		if (nameAux.matches(regex_nameModules)) {
			if (!(nameAux.contains("./"))) {
				String a =  nameAux.replaceAll("\'", "");
				String b = "nodule_modules/";
				System.out.println("A string eh: " + b.concat(a));
				return b.concat(a);
			}
			else {
				return nameAux.replaceAll("\'", "").replaceAll("\\/", "#");
			}
		}
		return null;
	}

	public void analyseFiles(String pathProject) throws IOException {
		// String projectName = project.getName();
		Path path = Paths.get("log/functions/");
		File file = path.toFile();
	//	System.out.println("File do log: " + file.getAbsolutePath());
		List<File> projectFiles = Util.getAllFiles(path.toString(), pathProject);

		for (File f : projectFiles) {
		//	System.out.println("Cada file: " + f.getAbsolutePath());
			if (f.getName().contains("invocations")) {
		//		System.err.println("Eh invocations!!!!");
				getObjectAccess(f, pathProject);
			}

		}

	}
}
