package org.ufla.dcc.jsdepextractor.project.dependency;

import org.ufla.dcc.jsdepextractor.project.JsFile;
import org.ufla.dcc.jsdepextractor.project.JsType;

/**
 * Responsável por construir dependências.
 */
public class DependencyFactory {

  /**
   * Constrói uma dependência, de um determinado tipo, de um arquivo Javascript para um tipo
   * Javascript.
   * 
   * @param dependencyType tipo da dependência
   * @param file arquivo Javascript que possui a dependência
   * @param type tipo Javascript que o arquivo depende
   * @param lineNumber linha do arquivo em que ocorre a dependência
   * @param offset deslocamento necessário para encontrar a dependência no arquivo
   * @return instância da dependência especificada.
   */
  public static Dependency createDependency(DependencyType dependencyType, JsFile file, JsType type,
      int lineNumber, long offset) {
    switch (dependencyType) {
      case ACCESS:
        return new AccessDependency(file, type, lineNumber, offset);
      case CREATE:
        return new CreateDependency(file, type, lineNumber, offset);
      default:
        throw new RuntimeException("Invalid DependencyType - '" + dependencyType + "'!");
    }
  }
}
