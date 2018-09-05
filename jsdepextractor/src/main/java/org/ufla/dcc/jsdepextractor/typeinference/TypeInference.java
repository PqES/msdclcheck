package org.ufla.dcc.jsdepextractor.typeinference;

import org.ufla.dcc.jsdepextractor.project.JsFile;
import org.ufla.dcc.jsdepextractor.project.NodeJsProject;

/**
 * Responsável por inferir os tipos de um determinado projeto NodeJs.
 */
public class TypeInference {

  private NodeJsProject project;

  public TypeInference(NodeJsProject project) {
    this.project = project;
  }

  /**
   * Realiza a inferência de tipos no projeto.
   */
  public void inferTypes() {
    ProjectQueueTypeInference projectQueueTypeInference = new ProjectQueueTypeInference(project);
    while (!projectQueueTypeInference.isEmpty()) {
      inferTypes(projectQueueTypeInference.poll());
    }
  }

  /**
   * Realiza a inferência de tipos em um determinado projeto.
   * 
   * @param inferTypeProject projeto a ser inferido os tipos
   */
  private void inferTypes(NodeJsProject inferTypeProject) {
    try {
      JsFileQueueTypeInference fileQueueTypeInference =
          new JsFileQueueTypeInference(inferTypeProject);
      while (!fileQueueTypeInference.isEmpty()) {
        inferTypes(fileQueueTypeInference.poll());
      }
    } catch (Exception e) {
      System.out.println(inferTypeProject + " não possui arquivos JavaScript.");
    }
  }

  private void inferTypes(JsFile jsFile) {
    new ASTVisitorTypeInference(jsFile);
  }


}
