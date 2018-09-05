package org.ufla.dcc.jsdepextractor.project.dependency;

import org.ufla.dcc.jsdepextractor.project.JsFile;
import org.ufla.dcc.jsdepextractor.project.JsType;

public abstract class HandleDependency extends Dependency {

  protected HandleDependency(JsFile file, JsType type, int lineNumber, long offset) {
    super(file, type, lineNumber, offset);
  }
  
  @Override
  public DependencyType getDependencyType() {
    return DependencyType.HANDLE;
  }

}
