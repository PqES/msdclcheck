package jsDepExtractor.dependencies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import jsDepExtractor.Util;
import jsdeodorant.experiment.FunctionInvocations;
import jsdeodorant.language.PredefinedNode_modules;

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

	public void analyseAccessFromFiles(String fileProject, String fileOrigin, Set<String> functionsInvocationsModule,
			Map<String, String> varsLocalDeclarationFileDestin) throws IOException {
		// Não há dependência de require para o arquivo analisado!
		if (varsLocalDeclarationFileDestin.size() == 0)
			return;

		for (String var : varsLocalDeclarationFileDestin.keySet()) {
			for (String function : functionsInvocationsModule) {
				String varNameFunction = function.split("\\.")[0];

				if (varNameFunction.equals(var)) {
					// System.out.println("Função : " + function);
					// System.out.println("Variavel: " + var);
					// System.out.println("declaration: " +
					// varsLocalDeclarationFileDestin.get(var));
					String declaration = varsLocalDeclarationFileDestin.get(var);
					String nameFileOrigin = Util.getNameFilewhioutRoot(fileProject, fileOrigin);
					// se for um módulo do node_modules
					if (PredefinedNode_modules.isItPredefinedNode_module(declaration)) {
						String node = Util.getNodeModulesDependency(function, varsLocalDeclarationFileDestin.get(var));
						System.out.println(nameFileOrigin + ",access," + node);
						String line = nameFileOrigin + ",access," + node;
						Util.writeFileAccess(fileProject, line);
					} else {
						String fileDestinName = Util.processFileNameAccess(fileProject,
								varsLocalDeclarationFileDestin.get(var), function, var);

						System.out.println(nameFileOrigin + ",access," + fileDestinName);
						String line = nameFileOrigin + ",access," + fileDestinName;
						Util.writeFileAccess(fileProject, line);
					}

				}
			}

		}

	}

	private String analyseName(String nameAux) {
		nameAux = nameAux.replaceAll("\"", "");
		String regex_nameModules = "^['].+[']$";
		String regex_localModules = "^['./].*[']$";
		// System.out.println("NameAux que chegou: " + nameAux);

		if (nameAux.matches(regex_nameModules)) {
			if (!(nameAux.contains("./"))) {
				String a = nameAux.replaceAll("\'", "");
				String b = "nodule_modules/";
				// System.out.println("A string eh: " + b.concat(a));
				return b.concat(a);
			} else {
				return nameAux.replaceAll("\'", "").replaceAll("\\/", "#");
			}
		}
		return null;
	}

}
