package org.ufla.dcc.jsdepextractor.project;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa um tipo definido por um desenvolvedor em Javascript.
 */
public class JsDefinedType implements JsType {

  private String name;

  private Map<String, JsType> memberTypes;

  private JsFile declaredFile;

  private long declarationOffset;

  private int declarationLine;

  private int declarationColumn;

  @Override
  public Map<String, JsType> getMemberTypes() {
    return memberTypes;
  }
  public JsDefinedType(String name, JsFile declaredFile) {
    this.name = name;
    this.declaredFile = declaredFile;
    memberTypes = new HashMap<>();
  }

  /**
   * Recupera a coluna em que o tipo foi declarado.
   * 
   * @return coluna em que o tipo foi declarado
   */
  public int getDeclarationColumn() {
    return declarationColumn;
  }

  /**
   * Recupera a linha em que o tipo foi declarado.
   * 
   * @return linha em que o tipo foi declarado
   */
  public int getDeclarationLine() {
    return declarationLine;
  }

  /**
   * Recupera o deslocamento necessário para alcançar a declaração do tipo no arquivo.
   * 
   * @return deslocamento necessário para alcançar a declaração do tipo no arquivo
   */
  public long getDeclarationOffset() {
    return declarationOffset;
  }

  /**
   * Recupera o arquivo Javascript em que o tipo foi declarado.
   * 
   * @return arquivo Javascript em que o tipo foi declarado
   */
  public JsFile getDeclaredFile() {
    return declaredFile;
  }

  @Override
  public JsType getMemberType(String memberName) {
    return memberTypes.getOrDefault(memberName, ExtractJsCommonTypes.getInstance().getObjectType());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setMemberType(String memberName, JsType type) {
    memberTypes.put(memberName, type);

  }

  @Override
  public String getQualifiedName() {
    return declaredFile.getQualifiedName() + "." + name;
  }

  public void setMemberTypes(Map<String, JsType> memberTypes) {
    this.memberTypes = memberTypes;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDeclaredFile(JsFile declaredFile) {
    this.declaredFile = declaredFile;
  }

  public void setDeclarationOffset(long declarationOffset) {
    this.declarationOffset = declarationOffset;
  }

  public void setDeclarationLine(int declarationLine) {
    this.declarationLine = declarationLine;
  }

  public void setDeclarationColumn(int declarationColumn) {
    this.declarationColumn = declarationColumn;
  }

  @Override
  public String toString() {
    return getName();
  }

}
