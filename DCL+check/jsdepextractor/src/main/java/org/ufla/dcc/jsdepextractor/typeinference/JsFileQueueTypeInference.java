package org.ufla.dcc.jsdepextractor.typeinference;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import org.ufla.dcc.jsdepextractor.ScriptParser;
import org.ufla.dcc.jsdepextractor.project.JsFile;
import org.ufla.dcc.jsdepextractor.project.NodeJsProject;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;

public class JsFileQueueTypeInference extends Observable
    implements Observer, Comparable<JsFileQueueTypeInference> {

  private JsFile file;

  private int unresolveTypeFiles;

  private PriorityQueue<JsFileQueueTypeInference> filesQueueForTypeInference;


  /**
   * Constrói uma fila de prioridades para inferir os tipos de todos os arquivos Javascript de um
   * determinado projeto NodeJs.
   * 
   * @param project projeto em qual será construída a fila para inferência de tipos
   * @throws Exception
   * @throws IOException
   */
  public JsFileQueueTypeInference(NodeJsProject project) throws Exception {
    this(project.createSimpleRelativePathToJsFile(), project.createRelativePathModuleToJsFile(),
        new PriorityQueue<>(), HashBiMap.create());
  }

  /**
   * Construtor do primeiro arquivo do projeto NodeJs para inferência de tipos a partir do projeto,
   * da fila de prioridades que irá manter os projetos que serão inferidos os tipos e do mapeamento
   * de instâncias de ProjectQueueTypeInference para suas respectivas de NodeJsProject.
   * 
   * @param project projeto que irá inferir tipos
   * @param filesQueueForTypeInference fila de prioridades que irá manter os projetos que serão
   *        inferidos os tipos
   * @param mapFiles
   * @throws Exception
   * @throws IOException
   */
  private JsFileQueueTypeInference(Map<String, JsFile> relativePathToFile,
      Map<String, JsFile> relativePathModuleToFile,
      PriorityQueue<JsFileQueueTypeInference> filesQueueForTypeInference,
      BiMap<JsFileQueueTypeInference, JsFile> mapFiles) throws Exception {
    this.filesQueueForTypeInference = filesQueueForTypeInference;
    Iterator<JsFile> iterator = relativePathToFile.values().iterator();
    if (!iterator.hasNext()) {
      throw new Exception("No FILES!");
    }
    file = iterator.next();
    mapFiles.put(this, file);
    createAndAssociateFilesDependency(relativePathToFile, relativePathModuleToFile, mapFiles);
    this.filesQueueForTypeInference.add(this);
    createAllFiles(relativePathToFile, relativePathModuleToFile, mapFiles);
  }

  private void createAllFiles(Map<String, JsFile> relativePathToFile,
      Map<String, JsFile> relativePathModuleToFile,
      BiMap<JsFileQueueTypeInference, JsFile> mapFiles) {
    for (JsFile jsFile : relativePathToFile.values()) {
      if (!mapFiles.containsValue(jsFile)) {
        new JsFileQueueTypeInference(jsFile, relativePathToFile, relativePathModuleToFile,
            filesQueueForTypeInference, mapFiles);
      }
    }

  }

  private JsFileQueueTypeInference(JsFile file, Map<String, JsFile> relativePathToFile,
      Map<String, JsFile> relativePathModuleToFile,
      PriorityQueue<JsFileQueueTypeInference> filesQueueForTypeInference,
      BiMap<JsFileQueueTypeInference, JsFile> mapFiles) {
    this.filesQueueForTypeInference = filesQueueForTypeInference;
    this.file = file;
    mapFiles.put(this, file);
    createAndAssociateFilesDependency(relativePathToFile, relativePathModuleToFile, mapFiles);
    this.filesQueueForTypeInference.add(this);
  }


  private void createAndAssociateFilesDependency(Map<String, JsFile> relativePathToFile,
      Map<String, JsFile> relativePathModuleToFile,
      BiMap<JsFileQueueTypeInference, JsFile> mapFiles) {
    ParseTree parseTree = ScriptParser.getInstance().parse(file);
    ASTVisitorRequireFiles astRequireFiles = new ASTVisitorRequireFiles(parseTree, file);
    this.unresolveTypeFiles = astRequireFiles.getRequireFiles().size();
    for (String requireFile : astRequireFiles.getRequireFiles()) {
      JsFile dependencyJsFile = relativePathToFile.get(requireFile);
      if (dependencyJsFile == null) {
        dependencyJsFile = relativePathModuleToFile.get(requireFile);
        if (dependencyJsFile == null) {
          // System.out.println(file);
          // System.out.println(requireFile);
          // System.out.println("OK");
          this.unresolveTypeFiles--;
          continue;
        }
      }
      JsFileQueueTypeInference jsFileQueueTypeInference = null;
      if (mapFiles.containsValue(dependencyJsFile)) {
        jsFileQueueTypeInference = mapFiles.inverse().get(dependencyJsFile);
      } else {
        jsFileQueueTypeInference = new JsFileQueueTypeInference(dependencyJsFile,
            relativePathToFile, relativePathModuleToFile, filesQueueForTypeInference, mapFiles);
      }
      jsFileQueueTypeInference.addObserver(this);
    }
  }

  @Override
  public int compareTo(JsFileQueueTypeInference other) {
    return unresolveTypeFiles - other.unresolveTypeFiles;
  }

  @Override
  public void notifyObservers() {
    super.setChanged();
    super.notifyObservers();
    // if (filesQueueForTypeInference.peek().file.getModule().getNodeJsProject().getName()
    // .equals("express")) {
    // System.out.println("Notificando observadores");
    // }
    deleteObservers();
  }

  @Override
  public void update(Observable observable, Object arg1) {
    // if (filesQueueForTypeInference.peek().file.getModule().getNodeJsProject().getName()
    // .equals("express")) {
    // System.out.println("Diminuindo dependencia " + file);
    // }
    unresolveTypeFiles--;
    filesQueueForTypeInference.remove(this);
    filesQueueForTypeInference.add(this);
  }

  /**
   * Remove o próximo arquivo Javascript em que a inferência de tipos deverá ser realizada.
   * 
   * @return próximo arquivo Javascript em que a inferência de tipos deverá ser realizada
   */
  public JsFile poll() {
    if (filesQueueForTypeInference.peek() == null) {
      return null;
    }
    // if (filesQueueForTypeInference.peek().file.getModule().getNodeJsProject().getName()
    // .equals("express")) {
    // System.out.println(filesQueueForTypeInference.peek().file);
    // System.out.println(filesQueueForTypeInference.peek().unresolveTypeFiles);
    // }
    filesQueueForTypeInference.peek().notifyObservers();
    return filesQueueForTypeInference.poll().file;
  }

  /**
   * Verifica se a fila está vazia.
   * 
   * @return true, se a fila está vazia, caso contrário, false
   */
  public boolean isEmpty() {
    return filesQueueForTypeInference.isEmpty();
  }


}
