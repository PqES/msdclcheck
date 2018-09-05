package org.ufla.dcc.jsdepextractor.project;

public enum JsExtension {

  JS(".js"), MJS(".mjs");

  private String ext;

  private JsExtension(String ext) {
    this.ext = ext;
  }

  public String getExtension() {
    return ext;
  }

  @Override
  public String toString() {
    return ext;
  }

}
