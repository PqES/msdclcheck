package org.ufla.dcc.jsdepextractor.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nullable;
import org.ufla.dcc.jsdepextractor.Utils;

/**
 * Representa um módulo de um projeto NodeJs.
 */
public class JsModule {

  private String qualifiedName;

  private String relativePath;

  private NodeJsProject nodeJsProject;

  @Nullable
  private JsFile indexFile;

  private Collection<JsFile> jsFiles;


  /**
   * Construtor de um módulo nodejs a partir do projeto e a pasta em que está contido.
   * 
   * @param nodeJsProject projeto em que o módulo está contido
   * @param moduleFolder pasta em que o módulo está contido
   */
  public JsModule(NodeJsProject nodeJsProject, File moduleFolder) {
    relativePath = moduleFolder.getAbsolutePath().substring(nodeJsProject.getPath().length());
    qualifiedName =
        nodeJsProject.getQualifiedName().concat(relativePath).replace(File.separator, ".");
    this.nodeJsProject = nodeJsProject;
    defineJsFiles(moduleFolder);
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JsModule other = (JsModule) obj;
    if (qualifiedName == null) {
      if (other.qualifiedName != null)
        return false;
    } else if (!qualifiedName.equals(other.qualifiedName))
      return false;
    return true;
  }

  /**
   * Recupera o caminho inteiro de onde o módulo está localizado.
   * 
   * @return caminho inteiro de onde o módulo está localizado
   */
  public String getFullPath() {
    return nodeJsProject.getPath() + relativePath;
  }

  /**
   * Recupera o arquivo index.js do módulo, pode retornar null, se o módulo não tiver este arquivo.
   * 
   * @return arquivo index.js do módulo, pode retornar null, se o módulo não tiver este arquivo.
   */
  @Nullable
  public JsFile getIndexFile() {
    return indexFile;
  }

  /**
   * Recupera a coleção de arquivos definidos neste módulo.
   * 
   * @return coleção de arquivos definidos neste módulo.
   */
  public Collection<JsFile> getJsFiles() {
    return jsFiles;
  }

  /**
   * Recupera o projeto nodejs em que este módulo está definido.
   * 
   * @return projeto nodejs em que este módulo está definido.
   */
  public NodeJsProject getNodeJsProject() {
    return nodeJsProject;
  }

  /**
   * Recupera o nome único do módulo.
   * 
   * @return nome único do módulo
   */
  public String getQualifiedName() {
    return qualifiedName;
  }

  /**
   * Recupera o caminho relativo do módulo.
   * 
   * @return caminho relativo do módulo
   */
  public String getRelativePath() {
    return relativePath;
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
    return "JsModule " + getFullPath();
  }

  /**
   * Define os arquivos javascript deste módulo.
   * 
   * @param moduleFolder pasta em que o módulo está contido
   */
  private void defineJsFiles(File moduleFolder) {
    jsFiles = new ArrayList<>();
    for (File file : moduleFolder.listFiles()) {
      if (Utils.isJsFile(file)) {
        JsFile jsFile = new JsFile(this, file, Utils.getJsExtension(file));
        jsFiles.add(jsFile);
        if (jsFile.getName().equals("index")) {
          indexFile = jsFile;
        }
      }
    }
  }


  public JsType getRequireType(String require) {
    return nodeJsProject.getRequireType(require);
  }


  public JsFile getFile(String fileName) {
    for (JsFile file : jsFiles) {
      if (file.getName().equals(fileName)) {
        return file;
      }
    }
    return null;
  }

}
