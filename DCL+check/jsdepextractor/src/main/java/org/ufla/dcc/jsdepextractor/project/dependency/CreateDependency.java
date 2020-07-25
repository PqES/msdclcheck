package org.ufla.dcc.jsdepextractor.project.dependency;

import org.ufla.dcc.jsdepextractor.project.JsFile;
import org.ufla.dcc.jsdepextractor.project.JsType;

/**
 * Representa uma depêndencia de criação entre um arquivo Javascript e um tipo Javascript.
 */
public class CreateDependency extends Dependency {

  /**
   * Constrói uma dependência de criação de um arquivo Javascript para um tipo Javascript.
   * 
   * @param file arquivo Javascript que possui a dependência
   * @param type tipo Javascript que o arquivo depende
   * @param lineNumber linha do arquivo em que ocorre a dependência
   * @param offset deslocamento necessário para encontrar a dependência no arquivo
   */
  public CreateDependency(JsFile file, JsType type, int lineNumber, long offset) {
    super(file, type, lineNumber, offset);
  }
  
  public DependencyType getDependencyType() {
    return DependencyType.CREATE;
  }

}
