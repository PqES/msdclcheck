package jsDepExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.HashedMap;

import com.google.common.base.Strings;
import com.google.common.io.Files;

import jsdeodorant.language.PredefinedNode_modules;

public class Util {

	public static boolean verifyFileOfProject(File f, String projectName) {
		String[] paths = projectName.split("/");
		String name = paths[paths.length - 1];
		if (f.getName().contains(name)) {
			return true;
		}
		return false;
	}

	public static List<File> getAllFiles(String directoryPath, String projectName) throws IOException {
		List<File> anyFiles = new LinkedList<>();
		// File files[] = dir.listFiles();
		// anyFiles.addAll(files);
		if (!Strings.isNullOrEmpty(directoryPath)) {
			File rootDir = new File(directoryPath);

			if (!rootDir.exists())
				throw new FileNotFoundException("The directory path is not valid");
			for (File f : Files.fileTreeTraverser().preOrderTraversal(rootDir)) {
				if (f.isFile() && Files.getFileExtension(f.toPath().toString()).toLowerCase().equals("csv"))
					anyFiles.add(f);
			}
		}

		return anyFiles;
	}

	public static void writeFileAccess(String path, String line) throws IOException {
		String newLine = line.replaceAll(Pattern.quote("\"."), "").replaceAll(Pattern.quote("\""), "")
				.replaceAll(Pattern.quote(".js"), "").replaceAll("#", "\\/");
		try {
			FileWriter fw = new FileWriter(path + "/dependenciesAcces.txt", true);
			fw.write(newLine);
			fw.write("\n");
			fw.close();
		} catch (IOException e) {
			// System.out.println("Exeption: " + e.getMessage());
		}

	}

	public static void writeFileCreate(String path, String line) throws IOException {
		String newLine = line.replaceAll(Pattern.quote("\"."), "").replaceAll(Pattern.quote("\""), "")
				.replaceAll(Pattern.quote(".js"), "").replaceAll("#", "\\/");
		try {
			FileWriter fw = new FileWriter(path + "/dependenciesCreate.txt", true);
			fw.write(newLine);
			fw.write("\n");
			fw.close();
		} catch (IOException e) {
			// System.out.println("Exeption: " + e.getMessage());
		}

	}
	
	

	public static ArrayList<String> processFiles(String file) throws IOException {
		ArrayList<String> codigo = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				String varName = "", nameRequire = "";
				String raw_line = line;
				line = line.replaceAll("\n", "");// Remove o \n do final da linha
				String regex = "[a-zA-Z0-9]+ = new require\\([\\'\\\"].+[\\'\\\"]\\)\\(.+\\)\\;";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(line);
				ArrayList<String> requires = new ArrayList<>();

				if (matcher.find()) {
					requires.add(line);
				}
				if (requires.size() > 0) {
					// Obtem o nome da variavel criada
					String regexName = "[a-zA-Z0-9]+ = new require";
					Pattern patternName = Pattern.compile(regexName);
					Matcher matcherName = patternName.matcher(line);
					if (matcherName.find()) {
						// Pega a primeira ocorrencia encontrada
						// nome = nome.group(0);
						// Separa pelo simbolo '=', pegando somente o nome da variavel
						varName = matcherName.group(0);

						String[] names = varName.split("=");
						// Remove quaisquer espacos existentes
						varName = names[0];
						varName = varName.replace(" ", "");
						String inp = requires.get(0);
						String regexRequire = "require\\([\\'\\\"].+[\\'\\\"]\\)";
						Pattern patternRequire = Pattern.compile(regexRequire);
						Matcher matcherRequire = patternRequire.matcher(inp);
						if (matcherRequire.find()) {
							nameRequire = matcherRequire.group(0);
							String saida = String.format("var %s = %s;\n", varName, nameRequire);
							codigo.add(saida);
							codigo.add("\n");
							String regexSeparator = "\\)\\(.+\\)\\;";
							Pattern patternSeparator = Pattern.compile(regexSeparator);
							Matcher matcherSeparator = patternSeparator.matcher(requires.get(0));
							String parametros = "";
							if (matcherSeparator.find()) {
								// Pega a primeira ocorrencia encontrada
								parametros = matcherSeparator.group(0);
								parametros = parametros.substring(1, parametros.length());
								String saida2 = String.format("var %s = new %s%s", varName, varName, parametros);
								codigo.add(saida2);
								codigo.add("\n");

							}
						}
					}

				} else {
					codigo.add(raw_line);
					codigo.add("\n");
				}
				line = br.readLine();
			}
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return codigo;
	}

	public static void writeFileCode(String file, ArrayList<String> codigo) throws IOException {
		FileWriter fw = new FileWriter(file);

		for (String linha : codigo) {
			fw.write(linha);
		}
		fw.close();
	}

	public static String processNameDependencyFunction(String nameDependency) {
		/// System.out.println("Nome que chegou: " + nameDependency);
		String[] newName = nameDependency.split("\\./");
		String name = "";
		for (int i = 0; i < newName.length; i++) {
			name += newName[i];
			// System.out.println("[i]: " + name);
		}
		// System.out.println("NameTeste: " + name);
		return name;

	}

	public static Map<String, String> proccesVarDependencyRequire(String file) throws IOException {
		Map<String, String> varInformations = new HashedMap();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			// System.out.println("FileUTil: " + file);
			while (line != null) {
				String requireExpression = "", varName = "";
				String namefilePath = "";
				String lineRequire = "[a-zA-Z0-9]+ = require\\([\\'\\\"].+[\\'\\\"]\\)";
				Pattern pattern = Pattern.compile(lineRequire);
				Matcher matcherLineRequire = pattern.matcher(line);
				// Matcher requireName = file.(regexRequire,inp);
				// caso a linha analisada representa um require;
				if (matcherLineRequire.find()) {
					// Pega a primeira ocorrencia encontrada
					// verifica se a dependência é node_modules;
					requireExpression = matcherLineRequire.group(0);
					if (dependencyRequireNodeModules(requireExpression) != null) {
						namefilePath = dependencyRequireNodeModules(requireExpression);
						varName = getVarName(requireExpression);
						// System.out.println("Nome do arquivo Node: " + varName);
						namefilePath = namefilePath.replaceAll("\'", "").replaceAll("\"", "");
						varInformations.put(varName.replaceAll(" ", ""), namefilePath);
					} else {
						if (dependencyRequireProjectModules(requireExpression) != null) {
							// processa o arquivo atual que está sendo avaliado de modo que
							// seja possível sua leitura
							namefilePath = dependencyRequireProjectModules(requireExpression);
							varName = getVarName(requireExpression);
							// System.out.println("Nome do arquivo do projeto: " + varName);
							namefilePath = namefilePath.replaceAll("\'", "").replaceAll("\"", "");
							varInformations.put(varName.replaceAll(" ", ""), namefilePath);
						}
					}
					// String dep = getFileDependency(file);
					// varInformations.put(varName.replaceAll(" ", ""), namefilePath);

				}

				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return varInformations;
	}

	public static String getVarName(String line) {
		String name = line.split("=")[0];
		name.replaceAll(" ", "");
		return name;
	}

	// Função para pegar a dependência de funções definidas no node_modules
	public static String dependencyRequireNodeModules(String requireExpression) {
		// Regex que representa as definições de node_modules - que não conhenha o ./
		// ('http') por exemplo
		String regex = "[\\'\\\"][a-zA-Z0-9].+[\\'\\\"]";
		Pattern patternRequire = Pattern.compile(regex);
		Matcher matcherRequire = patternRequire.matcher(requireExpression);
		String nameFilePath = "", varName = "";
		if (matcherRequire.find()) {
			nameFilePath = matcherRequire.group(0);
			return nameFilePath;
		}
		return null;

	}
	// public static boolean verifyFunctionIsNodeModules(String functionName) {
	// if() {
	// return true;
	// }
	// if (functionName.contains("./")) {
	// return false;
	// }
	// return true;
	//
	// }

	public static String dependencyRequireProjectModules(String requireExpression) {
		// Regex que representa as definições de dependências do projeto - que conhenha
		// o ./ ('./routes/index') por exemplo
		String regex = "[\\'\\\"][\\.][\\/](.+)[\\'\\\"]";
		Pattern patternRequire = Pattern.compile(regex);
		Matcher matcherRequire = patternRequire.matcher(requireExpression);
		String nameFilePath = "", varName = "";
		if (matcherRequire.find()) {
			nameFilePath = matcherRequire.group(0);
			return nameFilePath.replace("\'", "");
		}
		return null;

	}

	public static String getFileDependency(String fileCurrent) throws IOException {

		String[] auxName = fileCurrent.split("/");
		String path = "";
		for (int i = 0; i < auxName.length - 1; i++) {
			path += auxName[i] + "/";
		}
		// System.out.println("File: " + path);
		return path;
	}

	public static String processFileNameAccess(String pathProject, String fileDependency, String functionName,
			String var) {
		// System.out.println("Nome que chegou: " + fileDependency);
		String nameDestinFile = fileDependency.split("\\./")[1];
		String dep = "", pathFileDestin = "";
		String fileName = pathProject + nameDestinFile;
		dep = getNameFilewhioutRoot(pathProject, fileName);
		String nameDepFunction = processFunctionName(functionName, var);
		return dep + nameDepFunction;

	}

	public static String processFileNameCreate(String pathProject, String fileDependency, String var) {
		// System.out.println("Nome que chegou: " + fileDependency);
		String nameDestinFile = fileDependency.split("\\./")[1];
		String dep = "", pathFileDestin = "";
		String fileName = pathProject + nameDestinFile;
		dep = getNameFilewhioutRoot(pathProject, fileName);
		return dep + "." + var;

	}

	public static String getNameVarNew(String nameNew) {
		String regex = "new ([a-zA-Z0-9]+)\\.";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(nameNew);
		if (matcher.find()) {
			String nameAux = matcher.group(0);
			String nameVarnew = nameAux.split("=")[1];
			return nameVarnew.replaceAll(" ", "");
		}
		return null;
	}

	public static String getNameFilewhioutRoot(String pathProject, String pathFile) {
		String[] modules = pathProject.split("\\/");
		String module = modules[modules.length - 1];
		String regex = "(" + module + ")" + "\\/.+";
		String dep = "";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(pathFile);
		if (matcher.find()) {
			dep = matcher.group(0);
		}
		return dep.replaceAll("\\/", "\\.").replaceAll("\\.js", "").replaceAll("'", "");
	}

	public static String processFunctionName(String functioname, String var) {
		String[] nameDepFunctions = functioname.split(var);
		String nameDepFunction = "";
		for (int i = 1; i < nameDepFunctions.length; i++) {
			nameDepFunction += nameDepFunctions[i];
		}
		return nameDepFunction;
	}

	public static String getNodeModulesDependency(String function, String declaration) {
		String[] deps = function.split("\\.");
		String node_modulesDep = "node_modules." + declaration.replace("'", "");
		for (int i = 1; i < deps.length; i++) {
			node_modulesDep = node_modulesDep + "." + deps[i];
		}
		return node_modulesDep;

	}
	
	public static Boolean verifyDeclarationLocationObjectExternModule(String operandOfNew, String sourceFile)
			throws IOException {
		Map<String, String> varsLocalDeclaration = Util.proccesVarDependencyRequire(sourceFile);

		for (String var : varsLocalDeclaration.keySet()) {
			if (operandOfNew.contains(var)) {
				return true;
			}
		}
		return false;
	}
}
