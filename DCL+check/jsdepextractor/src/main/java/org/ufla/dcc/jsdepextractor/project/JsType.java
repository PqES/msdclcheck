package org.ufla.dcc.jsdepextractor.project;

import java.util.Map;

/**
 * Representa um tipo definido em Javascipt.
 */
public interface JsType {

  /**
   * Recupera o tipo de um determinado membro definido neste tipo.
   * 
   * @param memberName nome do membro que deseja recuperar o tipo
   * @return tipo de um determinado membro definido neste tipo
   */
  JsType getMemberType(String memberName);
  
  Map<String, JsType> getMemberTypes();

  void setMemberType(String memberName, JsType type);

  /**
   * Recupera o nome do tipo.
   * 
   * @return nome do tipo
   */
  String getName();

  void setName(String name);

  /**
   * Recupera o nome único do tipo.
   * 
   * @return nome único do tipo
   */
  String getQualifiedName();

  default boolean isUndefined() {
    return false;
  }

  default boolean isObject() {
    return false;
  }

  default boolean isNativeJs() {
    return false;
  }

}
