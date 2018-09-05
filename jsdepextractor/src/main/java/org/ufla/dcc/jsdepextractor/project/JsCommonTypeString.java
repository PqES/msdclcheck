package org.ufla.dcc.jsdepextractor.project;

import java.util.Map;

public class JsCommonTypeString {

  private String name;

  private String defaultMemberType;

  private Map<String, String> memberTypes;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDefaultMemberType() {
    return defaultMemberType;
  }

  public void setDefaultMemberType(String defaultMemberType) {
    this.defaultMemberType = defaultMemberType;
  }

  public Map<String, String> getMemberTypes() {
    return memberTypes;
  }

  public void setMemberTypes(Map<String, String> memberTypes) {
    this.memberTypes = memberTypes;
  }



}
