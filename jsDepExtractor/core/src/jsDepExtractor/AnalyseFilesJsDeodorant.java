package jsDepExtractor;

import java.io.IOException;

import jsDepExtractor.dependencies.AnalyseAccessDependency;
import jsDepExtractor.dependencies.AnalyseCreateDependency;
import jsDepExtractor.dependencies.AnalyseDeclareDependency;
import jsdeodorant.launcher.CLIRunner;

public class AnalyseFilesJsDeodorant {
	
	public static void main(String[]args) throws IOException {
		if (args.length != 1){
			System.out.println("Usage:\n jsDepExtractor [folder-dir]");
			return;
		}
		try {
			String path = args[0];
			System.out.println("Path:  " + path);
			//String pathProject = "/home/elena/Documents/Projeto/msdclcheck/AplicacaoNodeAST/MsVenda/";
			CLIRunner.inicializa(path);
			AnalyseCreateDependency.getInstance().analyseFiles(path);
			AnalyseDeclareDependency.getInstance().analyseFiles(path);
			System.out.println();
			System.err.println("------------------Acces Dependency-------------------");
			AnalyseAccessDependency.getInstance().analyseFiles(path);
			System.out.println();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
