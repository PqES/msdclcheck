package org.ufla.dcc.jsdepextractor.project.dependency;

/**
 * Enumera os tipos de dependência.
 */
public enum DependencyType {
  ACCESS, DECLARE, CREATE, EXTEND, HANDLE, IMPLEMENTS, DEPEND;

  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }
}
