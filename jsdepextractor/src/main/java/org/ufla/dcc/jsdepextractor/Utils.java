package org.ufla.dcc.jsdepextractor;

import java.io.File;
import org.ufla.dcc.jsdepextractor.project.JsExtension;

public class Utils {

  /**
   * Verifica se uma pasta representa um módulo Javascript. A pasta representa um módulo Javascript
   * quando possui pelo menos um arquivo Javascript.
   * 
   * @param folder pasta a ser verificada
   * @return true, se a pasta representa um módulo Javascript, caso contrário, false
   */
  public static boolean isAJsModuleFolder(File folder) {
    if (!folder.isDirectory()) {
      return false;
    }
    for (File file : folder.listFiles()) {
      if (isJsFile(file)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Verifica se um arquivo possui a extensão de um arquivo Javascript '.js'.
   * 
   * @param file arquivo a ser verificado
   * @return true, se o arquivo é um arquivo Javascript, caso contrário, false
   */
  public static boolean isJsFile(File file) {
    return file.exists() && file.isFile()
        && (file.getAbsolutePath().endsWith(JsExtension.JS.getExtension())
            || file.getAbsolutePath().endsWith(JsExtension.MJS.getExtension()));
  }

  public static String removeJsExtension(String relativePath) {
    if (relativePath.endsWith(JsExtension.JS.getExtension())) {
      return relativePath.substring(0,
          relativePath.length() - JsExtension.JS.getExtension().length());
    } else if (relativePath.endsWith(JsExtension.MJS.getExtension())) {
      return relativePath.substring(0,
          relativePath.length() - JsExtension.MJS.getExtension().length());
    }
    return relativePath;
  }

  public static JsExtension getJsExtension(File file) {
    if (!isJsFile(file)) {
      return null;
    }
    if (file.getAbsolutePath().endsWith(JsExtension.JS.getExtension())) {
      return JsExtension.JS;
    } else if (file.getAbsolutePath().endsWith(JsExtension.MJS.getExtension())) {
      return JsExtension.MJS;
    }
    return null;
  }

}
