package jsDepExtractor;

import java.io.FileReader;
import java.io.IOException;

import jsDepExtractor.dependencies.AnalyseAccessDependency;
import jsdeodorant.launcher.CLIRunner;

public class AnalyseFilesJsDeodorant {
	
	public static void main(String[]args) throws IOException {
//		if (args.length != 1){
//			System.out.println("Usage:\n jsDepExtractor [folder-dir]");
//			return;
//		}
		try {
			//String path = args[0];
			//System.out.println("Path:  " + path);
			//String path = "/home/elena/Documents/Projeto/msdclcheck/AplicacaoNodeAST/MsVenda/";
			String path = "/home/elena/Documents/Projeto/TestesJs/acmeair-nodejs-master/";
			
			//ArrayList<String> codigo = Util.processFiles(path);
			//Util.writeFileCode(path, codigo);
	//		Util.getVarsRequire(path);
			//Util.processRequireDependencie(path);
			CLIRunner.inicializa(path);
			//System.err.println("------------------Create Dependency-------------------");
		//	AnalyseCreateDependency.getInstance().analyseFiles(path);
//		//	AnalyseDeclareDependency.getInstance().analyseFiles(path);
//			System.out.println();
			System.err.println("------------------Acces Dependency-------------------");
//			System.out.println();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
