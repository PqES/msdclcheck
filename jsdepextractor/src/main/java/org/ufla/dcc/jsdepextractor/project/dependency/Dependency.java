package org.ufla.dcc.jsdepextractor.project.dependency;

import org.ufla.dcc.jsdepextractor.project.JsFile;
import org.ufla.dcc.jsdepextractor.project.JsType;

/**
 * Representa um tipo abstrato de depêndencia entre um arquivo Javascript e um tipo Javascript.
 */
public abstract class Dependency {

  protected final JsFile file;

  protected final JsType type;

  protected final int lineNumber;

  protected final long offset;

  /**
   * Constrói uma dependência abstrata de um arquivo Javascript para um tipo Javascript.
   * 
   * @param file arquivo Javascript que possui a dependência
   * @param type tipo Javascript que o arquivo depende
   * @param lineNumber linha do arquivo em que ocorre a dependência
   * @param offset deslocamento necessário para encontrar a dependência no arquivo
   */
  protected Dependency(JsFile file, JsType type, int lineNumber, long offset) {
    super();
    this.file = file;
    this.type = type;
    this.lineNumber = lineNumber;
    this.offset = offset;
  }


  /**
   * Recupera o tipo da dependência.
   * 
   * @return tipo da dependência
   */
  public DependencyType getDependencyType() {
    return DependencyType.DEPEND;
  }

  /**
   * Recupera o arquivo Javascript que possui a dependência.
   * 
   * @return arquivo Javascript que possui a dependência
   */
  public JsFile getFile() {
    return file;
  }

  /**
   * Recupera a linha do arquivo em que ocorre a dependência.
   * 
   * @return linha do arquivo em que ocorre a dependência
   */
  public int getLineNumber() {
    return lineNumber;
  }

  /**
   * Recupera o deslocamento necessário para encontrar a dependência no arquivo.
   * 
   * @return deslocamento necessário para encontrar a dependência no arquivo
   */
  public long getOffset() {
    return offset;
  }

  /**
   * Recupera o tipo Javascript que o arquivo depende.
   * 
   * @return tipo Javascript que o arquivo depende
   */
  public JsType getType() {
    return type;
  }

  @Override
  public String toString() {
    return file.getQualifiedName() + "," + getDependencyType().toString() + ","
        + type.getQualifiedName();
  }

}
