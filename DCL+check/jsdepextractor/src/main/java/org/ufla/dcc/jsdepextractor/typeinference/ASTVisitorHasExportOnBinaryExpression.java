package org.ufla.dcc.jsdepextractor.typeinference;

import java.util.ArrayList;
import java.util.Collection;
import org.ufla.dcc.jsdepextractor.ast.ASTVisitor;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;

public class ASTVisitorHasExportOnBinaryExpression extends ASTVisitor {


  private boolean exportAssociation;

  private String memberExport;

  private Collection<String> othersIdentifiers;

  public ASTVisitorHasExportOnBinaryExpression(BinaryOperatorTree node) {
    exportAssociation = false;
    memberExport = "";
    othersIdentifiers = new ArrayList<String>();
    startTraversal(node);
  }

  public boolean visit(MemberExpressionTree node) {
    if (ParseTreeType.MEMBER_EXPRESSION.equals(node.operand.type)) {
      MemberExpressionTree newNode = node.operand.asMemberExpression();
      if (!ParseTreeType.IDENTIFIER_EXPRESSION.equals(newNode.operand.type)) {
        return false;
      }
      if (newNode.memberName.value.equals("exports")
          && newNode.operand.asIdentifierExpression().identifierToken.value.equals("module")) {
        exportAssociation = true;
        memberExport = node.memberName.value;
      }
      return false;
    }
    if (!ParseTreeType.IDENTIFIER_EXPRESSION.equals(node.operand.type)) {
      return false;
    }
    if (node.memberName.value.equals("exports")
        && node.operand.asIdentifierExpression().identifierToken.value.equals("module")) {
      exportAssociation = true;
      memberExport = "";
    }
    if (node.operand.asIdentifierExpression().identifierToken.value.equals("exports")) {
      exportAssociation = true;
      memberExport = node.memberName.value;
    }
    return false;
  }

  public boolean visit(IdentifierExpressionTree node) {
    if (node.identifierToken.value.equals("exports")) {
      exportAssociation = true;
      memberExport = "";
    } else {
      othersIdentifiers.add(node.identifierToken.value);
    }
    return false;
  }

  public boolean isExportAssociation() {
    return exportAssociation;
  }

  public String getMemberExport() {
    return memberExport;
  }

  public Collection<String> getOthersIdentifiers() {
    return othersIdentifiers;
  }


}
