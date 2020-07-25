package fileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.internal.core.ModuleSourcePathManager;

import enums.Constraint;
import msdcl.core.CommunicateDefinition;
import msdcl.core.ConstraintDefinition;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;
import msdcl.core.ModuleDefinition;
import msdcl.core.ModuleDependencies;
import util.RulesRegex;
import util.Util;

public class InputManager {

	public InputManager() {

	}

	public Constraint findConstraint(String constraint) {
		for (Constraint c : Constraint.values()) {
			if (c.getValue().equals(constraint)) {
				return c;
			}
		}
		return null;
	}

	public Set<ConstraintDefinition> generateConstraints(String[] leftServices, MicroservicesSystem system,
			String[] rightServices, String[] usings, Constraint constraint, String nameService) {
		Set<ConstraintDefinition> constraints = new HashSet<>();

		for (String moduleName : leftServices) {
			for (ModuleDefinition commModule : system.getAllModules()) {
				if (commModule.getName().equals(moduleName)) {
					List<File> filesModuleCurrent = commModule.getFiles();
					for (String rightService : rightServices) {
						if (usings == null) {
							for (File nameFile : filesModuleCurrent) {
								ConstraintDefinition c = new ConstraintDefinition(nameService, commModule,
										nameFile.getAbsolutePath(), constraint, rightService);
								constraints.add(c);

							}
						} else {
							for (String using : usings) {
								for (File nameFile : commModule.getFiles()) {
									String folderPath = Util.getFileGeneralName(nameFile.getAbsolutePath(),
											nameService);
									ConstraintDefinition c = new ConstraintDefinition(nameService, commModule,
											nameFile.getAbsolutePath(), constraint, rightService, using);
									constraints.add(c);
								}
							}
						}
					}
				}
			}
		}

		return constraints;
	}

	private Set<ConstraintDefinition> extractConstraints(String tokens[], String serviceName,
			MicroservicesSystem system) {
		String constraintName = "";
		int indexOfConstraintName = -1;
		int indexOfLeftServices = -1;
		int indexOfRightServices = -1;
		int indexOfUsing = tokens.length;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].contains("-")) {
				indexOfConstraintName = i; // index do nome da restrição
				indexOfRightServices = i + 1;
				if (tokens[0].equals("only")) {
					indexOfLeftServices = 1;
					constraintName = "only-" + tokens[i];
				} else {
					indexOfLeftServices = 0;
					constraintName = tokens[i];
				}
			} else if (tokens[i].equalsIgnoreCase("using")) {
				indexOfUsing = i;
			}
		}
		String leftServices[] = Arrays.copyOfRange(tokens, indexOfLeftServices, indexOfConstraintName);
		String rightServices[] = Arrays.copyOfRange(tokens, indexOfRightServices, indexOfUsing);
		String usings[] = null;
		if (indexOfUsing + 1 < tokens.length) {
			usings = Arrays.copyOfRange(tokens, indexOfUsing + 1, tokens.length);
		}
		Constraint constraint = findConstraint(constraintName);
		return generateConstraints(leftServices, system, rightServices, usings, constraint, serviceName);
	}

	public MicroservicesSystem readFileConstraints(File f) throws IOException {
		MicroservicesSystem system = new MicroservicesSystem();
		FileReader file = new FileReader(f);
		BufferedReader buffer = new BufferedReader(file);
		String line;
		MicroserviceDefinition currentService = null;
		String name = "";
		String path = "";
		ModuleDefinition commModule = null;
		Set<ModuleDefinition> allModuleDefinitions = new HashSet<>();
		while (buffer.ready()) {

			line = buffer.readLine();

			if (line.matches(RulesRegex.COMM_MODULE)) {
				String rootPath[] = line.replaceAll("\t", "").split(": ");
				String moduleName = rootPath[0].split(" ")[1];
				List<File> files = null;
				if (rootPath[1].contains(".*")) {
					String dirFiles = rootPath[1].replaceAll("\\.\\*", "\\/");
					File dir = new File(dirFiles);
					files = Util.getAllJSFiles(dir);

				}
				commModule = new ModuleDefinition(moduleName, files);
				system.addModule(commModule);
				System.out.println(rootPath);
			} else if (line.matches(RulesRegex.MICROSERVICE_REGEX)) {
				String tokens[] = line.split(RulesRegex.MICROSERVICE_TOKENS);
				name = tokens[0];
				// System.out.println("name: " + name);
				String link = tokens[1];
				path = tokens[2];
				String language = tokens[3];
				currentService = new MicroserviceDefinition(name, link, path, language);
				system.addMicroservice(currentService);
				// system.getAllMicrosservice().add(currentService.getName());
			} else if (line.matches(RulesRegex.COMMUNICATIONS_REGEX)) {
				String lineAux = line.replace("\t", "");
				String tokens[] = lineAux.split(RulesRegex.CONSTRAINT_TOKENS);
				// System.out.println("Communications!!");
				Set<ConstraintDefinition> constraints = extractConstraints(tokens, name, system);
				system.addConstraints(constraints);
			} else if (line.matches(RulesRegex.DCL_REGEX)) {
				if (line.matches("(\t)*(module).*")) {
					String rootPath[] = line.replaceAll("\t", "").split(": ");
					String moduleName = rootPath[0].split(" ")[1];
					if(rootPath.length>1) {
						String[] modules = rootPath[1].split(",");
						Set<ModuleDefinition> DCLModules = extractModulesPath(modules, currentService, moduleName);
						system.addDCLModule(currentService, DCLModules);
					}else {
						String[] modules = new String[1];
						modules[0]= rootPath[1];
						Set<ModuleDefinition> DCLModules = extractModulesPath(modules, currentService, moduleName);
						system.addDCLModule(currentService, DCLModules);
					}
					
				}
				String dcl = line.replaceAll("\t", "");
				system.addDcl(currentService, dcl);
			}
		}

		buffer.close();
		file.close();
		return system;
	}

	public Set<ModuleDefinition> extractModulesPath(String[] modules, MicroserviceDefinition currentService,
			String moduleName) throws IOException {
		List<String> filesToString = new ArrayList<>();
		Boolean isExternal = false;
		List<File> files = new ArrayList<>();
		Set<ModuleDefinition> DCLModules = new HashSet<>();
		for (String module : modules) {
			if (module.contains(".*")) {
//				String dirFiles = currentService.getPath() + "src/main/java/"                               //para java
//						+ module.replaceAll("\\.\\*", "\\/").replaceAll("\\.", "/");
				String dirFiles =  currentService.getPath() + module.replaceAll("\\.(\\*)+", "\\/").replaceAll("\\.", "/");       // para js
				File dir = new File(dirFiles);
				// se o path não eh modulo externo
				if (Util.verifyPath(dir) == false) {
					files = Util.getAllJSFiles(dir);
					filesToString = Util.convertFileToString(files);
				} else
					filesToString.add(module.replaceAll("\\.(\\*)*", "\\/").replaceAll("\\.", "/"));
			} else {

				filesToString.add(module);
			}
			ModuleDefinition moduleDcl = new ModuleDefinition(filesToString, moduleName, module, isExternal);
			DCLModules.add(moduleDcl);
		}

		return DCLModules;

	}

	public HashMap<ModuleDefinition, Set<CommunicateDefinition>> readFileCommunications(File f,
			MicroservicesSystem system) throws IOException {
		HashMap<String, Set<CommunicateDefinition>> communicationsInfo = new HashMap<>();
		FileReader file = new FileReader(f);
		BufferedReader buffer = new BufferedReader(file);
		String line;
		HashMap<ModuleDefinition, Set<CommunicateDefinition>> mapCommunications = new HashMap<>();
		Set<CommunicateDefinition> communicationsDefined = new HashSet<>();
		while (buffer.ready()) {
			line = buffer.readLine();
			if (line.equals(""))
				continue;

			String[] communications = line.split(RulesRegex.CONSTRAINT_TOKENS);
			String pathModuleOrigin = communications[0];
			String nameServiceOrigin = getMsOriginName(pathModuleOrigin, system);
			ModuleDefinition module = getModule(pathModuleOrigin, system);
			if (module == null)
				continue;
			// System.out.println("Nome do serviço de origin: "+ nameServiceOrigin);
			String nameServiceDestin = communications[2];
			if (communications.length > 3) {
				String using = communications[4];
				// String interfaceName = communicateInterface(using);
				CommunicateDefinition communication = new CommunicateDefinition(nameServiceOrigin, nameServiceDestin,
						module, pathModuleOrigin, using);
				if (mapCommunications.containsKey(module)) {
					Set<CommunicateDefinition> setCommunicates = mapCommunications.get(module);
					setCommunicates.add(communication);
					mapCommunications.put(module, setCommunicates);
				} else {
					Set<CommunicateDefinition> setCommunicates = new HashSet<CommunicateDefinition>();
					setCommunicates.add(communication);
					mapCommunications.put(module, setCommunicates);

				}

			} else {
				CommunicateDefinition communication = new CommunicateDefinition(nameServiceOrigin, module,
						pathModuleOrigin, nameServiceDestin);
				if (mapCommunications.containsKey(module)) {
					Set<CommunicateDefinition> setCommunicates = mapCommunications.get(module);
					setCommunicates.add(communication);
					mapCommunications.put(module, setCommunicates);
				} else {
					Set<CommunicateDefinition> setCommunicates = new HashSet<CommunicateDefinition>();
					setCommunicates.add(communication);
					mapCommunications.put(module, setCommunicates);

				}

			}

		}
		buffer.close();
		file.close();
		system.setCommunications(mapCommunications);
		return system.getMapCommunications();
	}

	public String communicateInterface(String urlFull) {
		Pattern pattern = Pattern.compile(RulesRegex.URL_REGEX);
		Matcher matcher = pattern.matcher(urlFull);
		if (matcher.find()) {
			String name = urlFull.substring(matcher.end(), urlFull.length());
			return name;
		}
		return null;
	}

	public ModuleDefinition getModule(String pathFile, MicroservicesSystem system) {
		for (ModuleDefinition commModule : system.getAllModules()) {
			for (File file : commModule.getFiles()) {
				if (file.getAbsolutePath().contains(pathFile) && !commModule.getName().equals("$system"))
					return commModule;
			}

		}
		return null;
	}

	public String getMsOriginName(String pathMicroservice, MicroservicesSystem system) {
		for (MicroserviceDefinition ms : system.getMicroservices()) {
			if (pathMicroservice.contains(ms.getName()))
				return ms.getName();
		}
		return null;
	}

	public static HashMap<ModuleDefinition, Set<ModuleDependencies>> getDependenciesModulesViolates(String fileName,
			MicroserviceDefinition ms, MicroservicesSystem system) throws IOException {
		File fileDep = new File(fileName + "violates.txt");
		FileReader file = new FileReader(fileDep);
		BufferedReader buffer = new BufferedReader(file);
		String line;
		HashMap<ModuleDefinition, Set<ModuleDependencies>> mapModuleDependenciesViolates = new HashMap<>();
		Set<ModuleDependencies> dependencies = new HashSet<>();

		while (buffer.ready()) {
			line = buffer.readLine().replaceAll("\\[", "").replaceAll("\\]", "");
			String[] infos = line.split(",");
			String typeViolate = infos[0];
			String[] modules = infos[1].split(",");
			String origin = infos[1];
			String destin = infos[3];

			ModuleDefinition moduleOrigin = system.getModuleByPath(origin, ms);
			ModuleDefinition moduleDestin = system.getModuleByPath(destin, ms);

			dependencies = mapModuleDependenciesViolates.get(moduleOrigin);
			if (dependencies != null) {
				if (containsDep(dependencies, moduleOrigin, moduleDestin, origin, destin)) {
					dependencies = setValueDep(dependencies, moduleOrigin, moduleDestin);
					mapModuleDependenciesViolates.put(moduleOrigin, dependencies);
				} else {
					ModuleDependencies dep = new ModuleDependencies(moduleOrigin, moduleDestin, typeViolate);
					dependencies.add(dep);
					mapModuleDependenciesViolates.put(moduleOrigin, dependencies);
				}
			} else {
				Set<ModuleDependencies> dependenciesAux = new HashSet<>();
				ModuleDependencies dep = new ModuleDependencies(moduleOrigin, moduleDestin, typeViolate);

				dependenciesAux.add(dep);
				mapModuleDependenciesViolates.put(moduleOrigin, dependenciesAux);
			}

		}
		return mapModuleDependenciesViolates;
	}


	public static Boolean containsDep(Set<ModuleDependencies> dependencies, ModuleDefinition moduleOrigin,
			ModuleDefinition moduleDestin, String origin, String destin) {
		for (ModuleDependencies module : dependencies) {
			if (module.getModuleOrigin().getName().equals(moduleOrigin.getName())
					&& module.getModuleDestin().getName().equals(moduleDestin.getName())) {
				return true;
			}

		}
		return false;
	}

	public static Set<ModuleDependencies> setValueDep(Set<ModuleDependencies> dependencies,
			ModuleDefinition moduleOrigin, ModuleDefinition moduleDestin) {
		for (ModuleDependencies module : dependencies) {
			if (module.getModuleOrigin().getName().equals(moduleOrigin.getName())
					&& module.getModuleDestin().getName().equals(moduleDestin.getName())) {
				module.setDependencies(module.getDependencies() + 1);
			}

		}
		return dependencies;
	}
}
