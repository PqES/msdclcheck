package org.ufla.dcc.jsdepextractor.typeinference;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import org.ufla.dcc.jsdepextractor.Utils;
import org.ufla.dcc.jsdepextractor.ast.ASTVisitor;
import org.ufla.dcc.jsdepextractor.project.JsFile;
import com.google.javascript.jscomp.parsing.parser.LiteralToken;
import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;

public class ASTVisitorRequireFiles extends ASTVisitor {

  private Collection<String> requireFiles;
  private String relativePath;

  public ASTVisitorRequireFiles(ParseTree node, JsFile file) {
    relativePath = file.getModule().getRelativePath();
    requireFiles = new HashSet<>();
    if (file.getModule().getNodeJsProject().getName().equals("express")) {
      // System.out.println("OK");
    }
    // System.out.println("Dependências (Bibliotecas) '" + file.getQualifiedName() + "'");
    startTraversal(node);
    // System.out.println("Dependências (arquivos) '" + file.getQualifiedName() + "'");
    // for (String reqFile : requireFiles) {
    // System.out.println(reqFile);
    // }
    // System.out.println("-------------------------------------------------------------");
  }

  @Override
  public boolean visit(ImportDeclarationTree node) {
    includeImportValue(node.moduleSpecifier);
    return false;

  }

  @Override
  public boolean visit(CallExpressionTree node) {
    if (node.operand == null) {
      return true;
    }
    if (!ParseTreeType.IDENTIFIER_EXPRESSION.equals(node.operand.type)) {
      return true;
    }
    if (!"require".equals(node.operand.asIdentifierExpression().identifierToken.value)
        && !"import".equals(node.operand.asIdentifierExpression().identifierToken.value)) {
      return true;
    }
    if (node.arguments.arguments.size() != 1) {
      return true;
    }
    ParseTree argNode = node.arguments.arguments.get(0);
    if (!ParseTreeType.LITERAL_EXPRESSION.equals(argNode.type)) {
      return true;
    }
    includeImportValue(argNode.asLiteralExpression().literalToken.asLiteral());
    return true;

  }

  private void includeImportValue(LiteralToken token) {
    String importValue = getFileName(token);
    if (importValue != null) {
      requireFiles.add(importValue);
    }
  }

  private String getFileName(LiteralToken token) {
    String importValue = token.value;
    importValue = importValue.substring(1, importValue.length() - 1);
    if (importValue.endsWith(".json")) {
      return null;
    }
    if (importValue.startsWith(".")) {
      String path = relativePath;
      while (importValue.startsWith(".." + File.separator)) {
        int lastSeparator = path.lastIndexOf(File.separator);
        path = path.substring(0, lastSeparator);
        importValue = importValue.substring(3);
      }
      if (importValue.startsWith(".")) {
        importValue = importValue.substring(2);
      }
      path += File.separator + importValue;
      return Utils.removeJsExtension(path);
    }
    return null;
  }

  public Collection<String> getRequireFiles() {
    return requireFiles;
  }

}
