package org.ufla.dcc.jsdepextractor.dependency;

import java.util.ArrayList;
import java.util.Collection;
import org.ufla.dcc.jsdepextractor.project.JsFile;
import org.ufla.dcc.jsdepextractor.project.NodeJsProject;
import org.ufla.dcc.jsdepextractor.project.dependency.Dependency;

public class ExtractDependencies {

  private NodeJsProject nodeJsProject;

  private boolean includeObject;

  private boolean includeNativeJavascript;

  public ExtractDependencies(NodeJsProject nodeJsProject, boolean includeObject,
      boolean includeNativeJavascript) {
    this.includeObject = includeObject;
    this.includeNativeJavascript = includeNativeJavascript;
    this.nodeJsProject = nodeJsProject;
  }

  public Collection<Dependency> extractDependencies() {
    Collection<Dependency> dependencies = new ArrayList<>();
    for (JsFile file : nodeJsProject.createAllJsFilesCollection()) {
      ASTVisitorExtractDependencies visitor = new ASTVisitorExtractDependencies(file);
      dependencies.addAll(visitor.getDependencies());
    }
    return dependencies;
  }

  public void extractDependenciesWritingReportMultiFiles() {
    for (JsFile file : nodeJsProject.createAllJsFilesCollection()) {
      ASTVisitorExtractDependencies visitor = new ASTVisitorExtractDependencies(file);

      ReportDependenciesMultiFiles reportDependencies = new ReportDependenciesMultiFiles(
          visitor.getDependencies(), file, includeObject, includeNativeJavascript);
      reportDependencies.writeReport();
    }
  }

}
