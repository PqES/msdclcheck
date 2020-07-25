package org.ufla.dcc.jsdepextractor.dependency;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import org.ufla.dcc.jsdepextractor.project.dependency.Dependency;

public class ReportDependencies {

  public static File rootFolder = new File("report");

  private Collection<Dependency> dependencies;

  private File file;

  private boolean includeObject;

  private boolean includeNativeJavascript;

  public ReportDependencies(Collection<Dependency> dependencies, String projectName,
      boolean includeObject, boolean includeNativeJavascript) {
    this.includeObject = includeObject;
    this.includeNativeJavascript = includeNativeJavascript;
    this.dependencies = dependencies;
    file = new File(rootFolder.getAbsolutePath() + File.separator + projectName + ".deps");
  }


  public void writeReport() {
    rootFolder.mkdirs();
    file.delete();
    BufferedWriter bw = null;
    try {
      file.createNewFile();
      FileWriter fw = new FileWriter(file);
      bw = new BufferedWriter(fw);
      for (Dependency dependency : dependencies) {
        if (dependency.getType().isUndefined()
            || dependency.getType().getName() == null
            || (!includeObject && dependency.getType().isObject())
            || (!includeNativeJavascript && dependency.getType().isNativeJs())) {
          continue;
        }
        bw.write(dependency + "\n");
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      try {
        if (bw != null)
          bw.close();
      } catch (Exception ex) {
        System.out.println("Error in closing the BufferedWriter " + ex);
      }
    }
  }


}
