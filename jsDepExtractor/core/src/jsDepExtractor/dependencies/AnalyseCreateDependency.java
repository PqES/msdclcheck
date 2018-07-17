package jsDepExtractor.dependencies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jsDepExtractor.Util;
import jsdeodorant.analysis.abstraction.Module;
import jsdeodorant.analysis.abstraction.ObjectCreation;
import jsdeodorant.language.PredefinedNode_modules;

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

	public void analyseCreateFromFiles(Module currentModule, String fileProject, Map<String, String> varsLocalDeclaration, 
			Set<ObjectCreation>objectCreations) throws IOException {
		for (ObjectCreation creation : objectCreations) {
			String line = "";
			String nameFileOrigin = Util.getNameFilewhioutRoot(fileProject, currentModule.getCanonicalPath());
//			
			
			if (creation.isClassDeclarationPredefined()) {
				System.out.println("Is predefined: " + creation.getOperandOfNewName() );
				line = nameFileOrigin + ",create," + "js."+creation.getOperandOfNewName();
				System.out.println("dependency: " + line);
				Util.writeFileCreate(fileProject, line);
			} else {
				String declaration = creation.getModuleDeclarationLocation();
				if(creation.isExportedModuleFunctionModule()) {
					String fileDependency = Util.processFileNameCreate(fileProject, declaration, creation.getOperandOfNewName());
					line = nameFileOrigin + ",create," + fileDependency;
					System.out.println("Linha predefined:  " + line);
					Util.writeFileCreate(fileProject, line);
				}
				System.out.println("quem bugooou?? "+creation.getOperandOfNewName());
				if(PredefinedNode_modules.isItPredefinedNode_module(declaration)) {
							
					line = nameFileOrigin + ",create," + "js."+creation.getOperandOfNewName();
					System.out.println("Declaration node_modules: " + line);			
					Util.writeFileCreate(fileProject, line);
					
				}
				else {
					//TODO para classes definidas no mesmo arquivo!
//					line = nameFileOrigin + ",create," + creation.getModuleDeclarationLocation() + "/."+creation.getOperandOfNewName();
//					System.out.println("Declaration node_modules: " + line);			
//					Util.writeFile(fileProject, line);

				}
				
			}

		}
	}
	// private void getObjectCreate(File f, String path) throws IOException {
	// ArrayList<CreateDependency> createsDependencies = new ArrayList<>();
	// // System.out.println("Name File: " + f.getName());
	//
	// FileReader file = new FileReader(f);
	// BufferedReader buffer = new BufferedReader(file);
	// String line;
	// line = buffer.readLine();
	// while (buffer.ready()) {
	// line = buffer.readLine();
	// String[] contents = line.split(",");
	// //System.out.println("Contents[0]: " + contents[0]);
	// //System.err.println("Linha atual: " + line);
	// String module1 = contents[7];
	// String[] nameModule1 = module1.split("\\(");
	// String module2 = contents[8];
	// String[] nameModule2 = module2.split("\\(");
	// CreateDependency dep = new CreateDependency(nameModule1[0], nameModule2[0]);
	// System.out.println("Module1: " + nameModule1[0]);
	// System.out.println("Module2: " + nameModule2[0]);
	// System.err.println("Dependencia: " + dep.toString());
	// Util.writeFile(path, dep.toString());
	//
	// }
	//
	// }

	// public void analyseFiles(String pathProject) throws IOException {
	// Path path = Paths.get("log/legacy/classes/");
	// File file = path.toFile();
	// // System.out.println("File do log: " + file.getAbsolutePath());
	// List<File> projectFiles = Util.getAllFiles(path.toString(), pathProject);
	//
	// for (File f : projectFiles) {
	// getObjectCreate(f, pathProject);
	//
	// }
	//
	// }

}
