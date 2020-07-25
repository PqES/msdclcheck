package org.ufla.dcc.jsdepextractor.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.javascript.jscomp.parsing.parser.SourceFile;

/**
 * Representação de um arquivo Javascript.
 */
public class JsFile {

  private String name;

  private String qualifiedName;

  private JsModule module;

  private JsType exportType;
  
  private JsExtension extension;


  /**
   * Construtor de um arquivo Javascript a partir do módulo em que está definido e do arquivo que o
   * representa.
   * 
   * @param module módulo em que este arquivo está definido
   * @param file arquivo que o representa
   */
  public JsFile(JsModule module, File file, JsExtension extension) {
    this.module = module;
    name = file.getAbsolutePath();
    name = name.substring(module.getFullPath().length() + 1, name.length() - extension.getExtension().length());
    qualifiedName = module.getQualifiedName();
    if (!name.equals("index")) {
      qualifiedName += "." + name;
    }
    this.extension = extension;
  }


  /**
   * Cria uma instância de SourceFile do arquivo Javascript.
   * 
   * @return instância de SourceFile do arquivo Javascript
   * @throws IOException
   */
  public SourceFile createSourceFile() throws IOException {
    return new SourceFile(qualifiedName, new String(Files.readAllBytes(Paths.get(getFullPath()))));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JsFile other = (JsFile) obj;
    if (qualifiedName == null) {
      if (other.qualifiedName != null)
        return false;
    } else if (!qualifiedName.equals(other.qualifiedName))
      return false;
    return true;
  }


  /**
   * Recupera o mapeamento dos membros exportados para seus tipos.
   * 
   * @return mapeamento dos membros exportados para seus tipos
   */
  public JsType getExportType() {
    return exportType;
  }

  /**
   * Recupera o arquivo Javascript.
   * 
   * @return arquivo Javascript
   */
  public File getFile() {
    return new File(getFullPath());
  }

  /**
   * Recupera o caminho inteiro de onde o arquivo está localizado.
   * 
   * @return caminho inteiro de onde o arquivo está localizado
   */
  public String getFullPath() {
    return module.getFullPath() + File.separator + name + extension.getExtension();
  }

  /**
   * Recupera o módulo em que o arquivo está definido.
   * 
   * @return módulo em que o arquivo está definido
   */
  public JsModule getModule() {
    return module;
  }

  /**
   * Recupera o nome do arquivo.
   * 
   * @return nome do arquivo
   */
  public String getName() {
    return name;
  }

  /**
   * Recupera o nome único do arquivo Javascript.
   * 
   * @return nome único do arquivo Javascript
   */
  public String getQualifiedName() {
    return qualifiedName;
  }

  /**
   * Recupera o caminho relativo do arquivo Javascript.
   * 
   * @return caminho relativo do arquivo Javascript
   */
  public String getRelativePath() {
    return module.getRelativePath() + File.separator + name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return getFullPath();
  }


  public JsType getRequireType(String require) {
    return module.getRequireType(require);
  }


  public void setExportType(JsType exportType) {
    this.exportType = exportType;
  }

}
