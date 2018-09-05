package org.ufla.dcc.jsdepextractor.project;

import java.util.Map;

public class JsCommonType implements JsType {

  private String name;

  private JsType defaultMemberType;

  private Map<String, JsType> memberTypes;

  public JsCommonType(String name) {
    this.name = name;
  }

  @Override
  public JsType getMemberType(String memberName) {
    return memberTypes.getOrDefault(memberName, defaultMemberType);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getQualifiedName() {
    return "js." + name;
  }

  public void setName(String name) {}

  public void setDefaultMemberType(JsType defaultMemberType) {
    this.defaultMemberType = defaultMemberType;
  }

  public void setMemberTypes(Map<String, JsType> memberTypes) {
    this.memberTypes = memberTypes;
  }

  @Override
  public String toString() {
    return getQualifiedName();
  }

  @Override
  public void setMemberType(String memberName, JsType type) {


  }

  @Override
  public boolean isUndefined() {
    return name.equals("undefined");
  }

  @Override
  public boolean isObject() {
    return name.equals("Object");
  }

  @Override
  public boolean isNativeJs() {
    return true;
  }

  @Override
  public Map<String, JsType> getMemberTypes() {
    return memberTypes;
  }

}
