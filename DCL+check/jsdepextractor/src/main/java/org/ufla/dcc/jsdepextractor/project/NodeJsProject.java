package org.ufla.dcc.jsdepextractor.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.ufla.dcc.jsdepextractor.Utils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.javascript.jscomp.parsing.parser.util.LookaheadErrorReporter.ParseException;

/**
 * Representa um projeto NodeJs.
 */
public class NodeJsProject {

  /**
   * Define o nome da pasta em que se encontra a lista de projetos que o projeto depende.
   */
  private static final String DEPENDENCIES_FOLDER = File.separator + "node_modules";

  /**
   * Conjunto de pastas que não representam módulos de um sistema.
   */
  private static final Set<String> NOT_MODULE_FOLDERS =
      new HashSet<>(Arrays.asList("benchmark", "bin", "browser_build", "build", "dist", "example",
          "examples", "node_modules", "script", "scripts", "test", "tests", "tst"));

  /**
   * Define o nome de uma pasta que se encontra dentro das lista de pasta de node_modules, porém não
   * representa uma dependência.
   */
  private static final String NOT_DEPENDECY_PROJECT_FOLDER = ".bin";

  /**
   * Define o nome do arquivo package.json que é onde estão definidos as dependências do projeto.
   */
  private static final String PACKAGE_JSON_FILE = File.separator + "package.json";


  private String name;

  @Nullable
  private String version;

  private String path;

  @Nullable
  private String mainJsFileRelativePath;

  @Nullable
  private JsFile mainJsFile;

  private Collection<JsModule> modules;

  private BiMap<String, NodeJsProject> dependencies;

  private BiMap<String, NodeJsProject> nodeModulesProjects;

  @Nullable
  private NodeJsProject parent;

  /**
   * Construtor do projeto com base no caminho do projeto.
   * 
   * @param path caminho do projeto
   * @throws Exception
   */
  public NodeJsProject(String path) throws Exception {
    File projectFolder = validProjectPath(path);
    name = this.path.substring(this.path.lastIndexOf(File.separator) + 1);
    defineModulesRoot(projectFolder);
    defineNodeModulesProjects(projectFolder);
    Set<NodeJsProject> projectDefined = new HashSet<>();
    defineDependencies(projectFolder, projectDefined);
  }

  /**
   * Construtor do projeto com base no caminho do projeto e no projeto pai.
   * 
   * @param parent projeto pai
   * @param path caminho do projeto
   * @throws Exception
   */
  private NodeJsProject(NodeJsProject parent, String path) throws Exception {
    this.parent = parent;
    File projectFolder = validProjectPath(path);
    name = this.path.substring(this.path.lastIndexOf(File.separator) + 1);
    defineModulesRoot(projectFolder);
    defineNodeModulesProjects(projectFolder);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NodeJsProject other = (NodeJsProject) obj;
    if (path == null) {
      if (other.path != null)
        return false;
    } else if (!path.equals(other.path))
      return false;
    return true;
  }

  /**
   * Recupera a coleção de projetos que este projeto depende.
   * 
   * @return coleção de projetos que este projeto depende
   */
  public Collection<NodeJsProject> getDependencies() {
    return dependencies.values();
  }

  /**
   * Cria uma coleção contendo todos arquivos Javascript declarados neste projeto.
   * 
   * @return coleção com todos arquivos Javascript declarados neste projeto
   */
  public Collection<JsFile> createAllJsFilesCollection() {
    Collection<JsFile> allJsFiles = new ArrayList<>();
    for (JsModule module : getModules()) {
      allJsFiles.addAll(module.getJsFiles());
    }
    return allJsFiles;
  }

  /**
   * Cria uma mapeamento do caminho relativo de cada arquivo Javascript declarado no projeto para o
   * mesmo.
   * 
   * @return mapeamento do caminho relativo de cada arquivo Javascript declarado no projeto para o
   *         mesmo
   */
  public Map<String, JsFile> createRelativePathToJsFile() {
    Map<String, JsFile> relativePathToJsFile = createSimpleRelativePathToJsFile();
    if (mainJsFile != null) {
      relativePathToJsFile.put(File.separator, mainJsFile);
    }
    for (JsModule module : getModules()) {
      if (module.getIndexFile() != null) {
        relativePathToJsFile.put(module.getRelativePath(), module.getIndexFile());
      }
    }
    return relativePathToJsFile;
  }

  public Map<String, JsFile> createRelativePathModuleToJsFile() {
    Map<String, JsFile> relativePathoduleToJsFile = new HashMap<>();
    if (mainJsFile != null) {
      relativePathoduleToJsFile.put(File.separator, mainJsFile);
    }
    for (JsModule module : getModules()) {
      if (module.getIndexFile() != null) {
        relativePathoduleToJsFile.put(module.getRelativePath(), module.getIndexFile());
      }
    }
    return relativePathoduleToJsFile;
  }

  public Map<String, JsFile> createSimpleRelativePathToJsFile() {
    Map<String, JsFile> relativePathToJsFile = new HashMap<>();
    for (JsModule module : getModules()) {
      for (JsFile file : module.getJsFiles()) {
        relativePathToJsFile.put(file.getRelativePath(), file);
      }
    }
    return relativePathToJsFile;
  }


  /**
   * Recupera a coleção de módulos do projeto.
   * 
   * @return coleção de módulos do projeto
   */
  public Collection<JsModule> getModules() {
    return modules;
  }

  /**
   * Recupera o nome do projeto.
   * 
   * @return nome do projeto
   */
  public String getName() {
    return name;
  }

  /**
   * Recupera os projetos localizados na pasta 'node_modules'.
   * 
   * @return projetos localizados na pasta 'node_modules'
   */
  public Collection<NodeJsProject> getNodeModulesProjects() {
    return nodeModulesProjects.values();
  }

  /**
   * Recupera o pai do projeto. O pai do deste projeto é um projeto que depende deste projeto.
   * 
   * @return pai do projeto
   */
  @Nullable
  public NodeJsProject getParent() {
    return parent;
  }


  /**
   * Recupera o caminho do projeto.
   * 
   * @return caminho do projeto
   */
  public String getPath() {
    return path;
  }
  

  /**
   * Recupera o nome completo do projeto junto com sua versão.
   * 
   * @return versão do projeto
   */
  @Nullable
  public String getQualifiedName() {
    String prefix = "";
    if (parent != null) {
      prefix = "node_modules.";
    }
    if (getVersion() == null) {
      return prefix + getName();
    }
    return prefix + getName() + ".v" + getVersion();
  }

  /**
   * Recupera a versão do projeto.
   * 
   * @return versão do projeto
   */
  @Nullable
  public String getVersion() {
    return version;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((path == null) ? 0 : path.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "NodeJsProject " + getQualifiedName() + " - " + getPath();
  }

  /**
   * Define os projetos que este projeto depende.
   * 
   * @param projectFolder pasta em que este projeto está definido
   * @param projectDefined 
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void defineDependencies(File projectFolder, Set<NodeJsProject> projectDefined) throws Exception {
    if (projectDefined.contains(this)) {
      return;
    }
    projectDefined.add(this);
    dependencies = HashBiMap.create();
    JSONParser parser = new JSONParser();
    try {
      Object obj =
          parser.parse(new FileReader(projectFolder.getAbsolutePath() + PACKAGE_JSON_FILE));

      JSONObject jsonObject = (JSONObject) obj;
      version = (String) jsonObject.get("version");
      mainJsFileRelativePath = (String) jsonObject.get("main");
      if (mainJsFileRelativePath == null) {
        mainJsFileRelativePath = "index.js";
      } else if (mainJsFileRelativePath.startsWith(".")) {
        mainJsFileRelativePath = mainJsFileRelativePath.substring(1);
      }
      JSONObject dependenciesObject = (JSONObject) jsonObject.get("dependencies");
      if (dependenciesObject != null) {
        for (String dependency : (Collection<String>) dependenciesObject.keySet()) {
          NodeJsProject dependencyProject = getDependency(dependency);
          if (dependencyProject == null) {
            continue;
          }
          dependencies.put(dependency, dependencyProject);
          dependencyProject.defineDependencies(new File(dependencyProject.getPath()), projectDefined);
        }
      }
    } catch (FileNotFoundException e) {
//      e.printStackTrace();
      System.out.println(
          "Arquivo '" + projectFolder.getAbsolutePath() + PACKAGE_JSON_FILE + "' não existe.");
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exception("Problema na leitura do arquivo '" + projectFolder.getAbsolutePath()
          + PACKAGE_JSON_FILE + "'.");
    } catch (ParseException e) {
      e.printStackTrace();
      throw new Exception("Proble ao realizar o parse do arquivo '"
          + projectFolder.getAbsolutePath() + PACKAGE_JSON_FILE + "'.");
    }
    defineMainJsFile();
  }

  /**
   * Define o principal arquivo Javascript do projeto, se o mesmo existir.
   */
  private void defineMainJsFile() {
    if (mainJsFileRelativePath == null) {
      return;
    }
    int indexLastFileSeparator = mainJsFileRelativePath.lastIndexOf(File.separator);
    String moduleRelativePath = (indexLastFileSeparator == -1) ? ""
        : mainJsFileRelativePath.substring(0, indexLastFileSeparator);
    String mainJsFileName = mainJsFileRelativePath.substring(indexLastFileSeparator + 1);
    mainJsFileName = Utils.removeJsExtension(mainJsFileName);
    for (JsModule module : getModules()) {
      if (module.getRelativePath().equals(moduleRelativePath)) {
        for (JsFile file : module.getJsFiles()) {
          if (file.getName().equals(mainJsFileName)) {
            mainJsFile = file;
            return;
          }
        }
      }
    }
  }

  /**
   * Define os módulos deste projeto de forma recursiva.
   * 
   * @param folder pasta em que realiza a busca por módulos
   */
  private void defineModulesRec(File folder) {
    for (File file : folder.listFiles()) {
      if (file.isDirectory()) {
        defineModulesRec(file);
      }
      if (Utils.isAJsModuleFolder(file)) {
        modules.add(new JsModule(this, file));
      }
    }
  }

  /**
   * Define os módulos deste projeto pela raiz do projeto e continua a busca com a função recursiva
   * defineProjectModulesRec.
   * 
   * @param projectFolder pasta raiz do projeto
   */
  private void defineModulesRoot(File projectFolder) {
    modules = new ArrayList<>();
    if (Utils.isAJsModuleFolder(projectFolder)) {
      modules.add(new JsModule(this, projectFolder));
    }
    for (File file : projectFolder.listFiles()) {
      if (file.isDirectory() && !NOT_MODULE_FOLDERS.contains(file.getName())) {
        defineModulesRec(file);
        if (Utils.isAJsModuleFolder(file)) {
          modules.add(new JsModule(this, file));
        }
      }
    }
  }

  /**
   * Define a coleção de projetos que o projeto atual possui na pasta 'node_modules'.
   * 
   * @param projectFolder pasta raiz do projeto
   * @throws Exception
   */
  private void defineNodeModulesProjects(File projectFolder) throws Exception {
    nodeModulesProjects = HashBiMap.create();
    File dependenciesFolder = new File(projectFolder.getAbsolutePath() + DEPENDENCIES_FOLDER);
    if (!dependenciesFolder.exists()) {
      return;
    }
    for (File file : dependenciesFolder.listFiles()) {
      if (file.isDirectory() && !file.getName().equals(NOT_DEPENDECY_PROJECT_FOLDER)) {
        nodeModulesProjects.put(file.getName(), new NodeJsProject(this, file.getAbsolutePath()));
      }
    }
  }

  /**
   * Recupera um projeto que este projeto depende pelo nome do projeto.
   * 
   * @param dependencyName nome do projeto que este possui a dependência
   * 
   * @return projeto referente ao nome informado
   */
  private NodeJsProject getDependency(String dependencyName) {
    if (nodeModulesProjects.containsKey(dependencyName)) {
      return nodeModulesProjects.get(dependencyName);
    }
    if (parent == null) {
      return null;
    }
    return parent.getDependency(dependencyName);
  }


  /**
   * Realiza a validação do caminho do projeto.
   * 
   * @param projectPath caminho do projeto
   * @return arquivo que representa a pasta do projeto
   * @throws Exception
   */
  private File validProjectPath(String projectPath) throws Exception {
    File projectFolder = new File(projectPath);
    if (!projectFolder.exists()) {
      throw new Exception("Project path '" + projectPath + "' don't exist!");
    }
    if (!projectFolder.isDirectory()) {
      throw new Exception("Project path '" + projectPath + "' don't is a directory!");
    }
    this.path = projectFolder.getAbsolutePath();
    return projectFolder;
  }


  private JsFile getFile(String requireFile) {
    String possibleModule = requireFile.substring(0, requireFile.length());
    String moduleFile = requireFile.substring(0, requireFile.lastIndexOf(File.separator));
    String fileName = requireFile.substring(moduleFile.length() + 1, requireFile.length());
    if (possibleModule.equals(File.separator)) {
      return mainJsFile;
    }
    for (JsModule module : modules) {
      if (module.getRelativePath().equals(possibleModule)) {
        return module.getIndexFile();
      } else if (module.getRelativePath().equals(moduleFile)) {
        return module.getFile(fileName);
      }
    }
    return null;
  }

  public JsType getRequireType(String require) {
    
    if (require.startsWith(File.separator)) {
      JsFile jsFile = getFile(require);
      if (jsFile == null) {
        return ExtractJsCommonTypes.getInstance().getObjectType();
      }
      return (jsFile.getExportType() != null) ? jsFile.getExportType() : ExtractJsCommonTypes.getInstance().getObjectType();
    }
    NodeJsProject nodeJsProject = dependencies.get(require);
    if (nodeJsProject == null || nodeJsProject.mainJsFile == null || nodeJsProject.mainJsFile.getExportType() == null) {
      return ExtractJsCommonTypes.getInstance().getObjectType();
    }
    return nodeJsProject.mainJsFile.getExportType();
  }

  public JsFile getMainJsFile() {
    return mainJsFile;
  }

  public void setMainJsFile(JsFile mainJsFile) {
    this.mainJsFile = mainJsFile;
  }

}
