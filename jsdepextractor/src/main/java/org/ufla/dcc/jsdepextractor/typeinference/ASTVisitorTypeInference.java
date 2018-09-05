package org.ufla.dcc.jsdepextractor.typeinference;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import org.ufla.dcc.jsdepextractor.ScriptParser;
import org.ufla.dcc.jsdepextractor.Utils;
import org.ufla.dcc.jsdepextractor.ast.ASTVisitor;
import org.ufla.dcc.jsdepextractor.project.ExtractJsCommonTypes;
import org.ufla.dcc.jsdepextractor.project.JsDefinedType;
import org.ufla.dcc.jsdepextractor.project.JsFile;
import org.ufla.dcc.jsdepextractor.project.JsType;
import com.google.javascript.jscomp.parsing.parser.LiteralToken;
import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.BlockTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ClassDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForAwaitOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.IfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.LabelledStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.LiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;
import com.google.javascript.jscomp.parsing.parser.trees.ReturnStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.SwitchStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThrowStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TryStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.UpdateExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WithStatementTree;

public class ASTVisitorTypeInference extends ASTVisitor {

  private JsType exportType;

  private Map<String, String> varToExportMember;

  private Deque<Map<String, JsType>> identifierTypes;

  private ExtractJsCommonTypes extractJsCommonTypes;

  private JsFile file;

  private String relativePath;

  private Deque<FunctionDeclarationTree> functions;

  private Deque<ClassDeclarationTree> classDeclarations;

  private Deque<JsType> classTypes;

  private Deque<JsType> returnFunctions;

  private boolean exportAssociation;

  private String memberExport;

  public ASTVisitorTypeInference(JsFile file) {
    relativePath = file.getModule().getRelativePath();
    this.file = file;
    String typeName = file.getName();
    if (file.equals(file.getModule().getNodeJsProject().getMainJsFile())) {
      typeName = file.getModule().getNodeJsProject().getName();
    }
    if (file.getName().equals("index")) {
      typeName = file.getModule().getQualifiedName();
      int last = typeName.lastIndexOf(".");
      typeName = typeName.substring(last + 1);
    }
    typeName.replace("-", "_");
    typeName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
    exportType = new JsDefinedType(typeName, file);
    varToExportMember = new HashMap<>();
    identifierTypes = new ArrayDeque<>();
    functions = new ArrayDeque<>();
    classDeclarations = new ArrayDeque<>();
    returnFunctions = new ArrayDeque<>();
    classTypes = new ArrayDeque<>();
    identifierTypes.push(new HashMap<>());
    extractJsCommonTypes = ExtractJsCommonTypes.getInstance();
    ParseTree parseTree = ScriptParser.getInstance().parse(file);
//    if (file.getName().equals("bar") || file.getName().equals("foo")) {
//      System.out.println("OK");
//    }
    startTraversal(parseTree);
    // for (String var : varToExportMember.keySet()) {
    // JsType type = getIdentifierTypeIgnoreCommon(var);
    // if (type == null) {
    // continue;
    // }
    // if (varToExportMember.get(var).equals("")) {
    // if (type.getName() == null) {
    // type.setName(exportType.getName());
    // }
    // exportType = type;
    // }
    // }
    for (String var : varToExportMember.keySet()) {
      JsType type = getIdentifierTypeIgnoreCommon(var);
      if (type == null) {
        continue;
      }
      exportType.setMemberType(varToExportMember.get(var), type);
    }
    JsType emptyType = exportType.getMemberType("");
    if (emptyType != null) {
      Map<String, JsType> mapType = exportType.getMemberTypes();
      for (String id : emptyType.getMemberTypes().keySet()) {
        if (!mapType.containsKey(id)) {
          mapType.put(id, emptyType.getMemberType(id));
        }
      }
    }
//    if (file.getName().equals("foo")) {
//      System.out.println("OK");
//    }
    // if (file.equals(file.getModule().getNodeJsProject().getMainJsFile())) {
    // System.out.println("OK");
    // }
    // if (file.equals(file.getModule().getNodeJsProject().getMainJsFile())
    // && file.getModule().getNodeJsProject().getName().equals("express")) {
    // System.out.println("OK");
    // }
    if (file.getQualifiedName().equals("app.server")) {
      System.out.println("OK");
    }
//    if (file.getQualifiedName().equals("express.index")) {
//      System.out.println("OK");
//    }
//    if (file.getName().equals("express")
//        && file.getModule().getNodeJsProject().getName().equals("express")) {
//      System.out.println("OK");
//    }
    file.setExportType(exportType);
  }

  private String getImportValue(LiteralToken token) {
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
    return importValue;
  }

  public boolean visit(ImportDeclarationTree node) {
    String importValue = getImportValue(node.moduleSpecifier);
    JsType typeImport = file.getRequireType(importValue);
    if (node.defaultBindingIdentifier != null) {
      String identifier = node.defaultBindingIdentifier.value;
      identifierTypes.peek().put(identifier, typeImport);
    }
    if (node.importSpecifierList != null) {
      for (ParseTree childNode : node.importSpecifierList) {
        if (!(childNode instanceof ImportSpecifierTree)) {
          continue;
        }
        ImportSpecifierTree impoTree = childNode.asImportSpecifier();
        String exterId = impoTree.importedName.value;
        String interId = exterId;
        if (impoTree.destinationName != null) {
          interId = impoTree.destinationName.value;
        }
        identifierTypes.peek().put(interId, typeImport.getMemberType(interId));
      }
    }
    if (node.nameSpaceImportIdentifier != null) {
      identifierTypes.peek().put(node.nameSpaceImportIdentifier.value, typeImport);
    }
    return false;
  }
  

  
  public boolean visit(ExportDeclarationTree node) {
    JsType fromType = null;
    if (node.from != null) {
      fromType = file.getRequireType(getImportValue(node.from));
    }
    if (node.exportSpecifierList != null) {
      for (ParseTree childNode : node.exportSpecifierList) {
        if (!(childNode instanceof ExportSpecifierTree)) {
          continue;
        }
        ExportSpecifierTree expoTree = childNode.asExportSpecifier();
        String exterId = expoTree.importedName.value;
        String interId = exterId;
        if (expoTree.destinationName != null) {
          interId = expoTree.destinationName.value;
        }
        if (fromType != null) {
          exportType.setMemberType(interId, fromType.getMemberType(exterId));
        }
        varToExportMember.put(exterId, interId);
      }
    }
    JsType type = getType(node.declaration);
    exportType.setMemberType(type.getName(), type);
    return false;
  }
  
  public boolean visit(ClassDeclarationTree node) {
    return visitIntern(node);
  }

  public boolean visitIntern(ClassDeclarationTree node) {
    classDeclarations.push(node);
    String name = (node.name != null) ? node.name.value : "Class" + node.hashCode();
    JsType type = new JsDefinedType(name, file);
    classTypes.push(type);
    identifierTypes.peek().put(name, type);
    identifierTypes.push(new HashMap<>());
    for (ParseTree childNode : node.elements) {
      startTraversal(childNode);
    }
    identifierTypes.pop();
    classDeclarations.pop();
    if (!classDeclarations.isEmpty()) {
      JsType jsType = classTypes.pop();
      if (jsType != null && !classTypes.isEmpty()) {
        classTypes.peek().setMemberType(jsType.getName(), jsType);
      }
      classTypes.push(jsType);
    }
    return false;
  }
  
  public boolean visit(FunctionDeclarationTree node) {
    return visitIntern(node);
  }

  public boolean visitIntern(FunctionDeclarationTree node) {
    functions.push(node);
    String name = (node.name != null) ? node.name.value : null;
    JsType type = ExtractJsCommonTypes.getInstance().getUndefinedType();
    if (classDeclarations.isEmpty()) {
      type = new JsDefinedType(name, file);
    }
    identifierTypes.peek().put(name, type);
    returnFunctions.push(type);
    startTraversal(node.functionBody);
    identifierTypes.peek().put(name, returnFunctions.peek());
    functions.pop();
    if (!classDeclarations.isEmpty() && !returnFunctions.isEmpty()) {
      JsType jsType = returnFunctions.peek();
      if (jsType != null && !classTypes.isEmpty()) {
        classTypes.peek().setMemberType(jsType.getName(), jsType);
      }
      return false;
    }
    if (!functions.isEmpty()) {
      JsType jsType = returnFunctions.pop();
      if (jsType != null && !returnFunctions.isEmpty()) {
        returnFunctions.peek().setMemberType(jsType.getName(), jsType);
      }
      returnFunctions.push(jsType);
    }
    return false;
  }



  public boolean visit(BlockTree node) {
    identifierTypes.push(new HashMap<>());
    for (ParseTree childNode : node.statements) {
      startTraversal(childNode);
    }
    identifierTypes.pop();
    return false;
  }

  public boolean visit(ReturnStatementTree node) {
    if (returnFunctions.isEmpty()) {
      return false;
    }
    JsType jsType = returnFunctions.pop();
    JsType newType = getType(node.expression);
    newType.setName(jsType.getName());
    returnFunctions.push(newType);
    return false;
  }



  // public static void main(String... args) {
  // for (TokenType t : TokenType.values()) {
  // System.out.println(t.name() + " - " + t.value);
  // }
  // System.out.println(Arrays.toString(TokenType.values()));
  // }

  private JsType getTypeFromLiteral(LiteralExpressionTree node) {
    switch (node.literalToken.type) {
      case REGULAR_EXPRESSION:
        return extractJsCommonTypes.getType("RegExp");
      case NUMBER:
        return extractJsCommonTypes.getType("Number");
      case STRING:
        return extractJsCommonTypes.getType("String");
      case TRUE:
      case FALSE:
        return extractJsCommonTypes.getType("Boolean");
      default:
        return extractJsCommonTypes.getUndefinedType();
    }
  }

  private JsType getTypeFromUnaryExpression(UnaryExpressionTree node) {
    switch (node.operator.type) {
      case MINUS:
      case PLUS:
      case TILDE: // ~ (not bitwise)
        return extractJsCommonTypes.getType("Number");
      case BANG: // ! (NOT)
        return extractJsCommonTypes.getType("Boolean");
      case TYPEOF:
        return extractJsCommonTypes.getType("String");
      default:
        return extractJsCommonTypes.getUndefinedType();
    }
  }

  private JsType getTypeFromUpdate(UpdateExpressionTree node) {
    switch (node.operator.type) {
      case PLUS_PLUS:
      case MINUS_MINUS:
        return extractJsCommonTypes.getType("Number");
      default:
        return extractJsCommonTypes.getUndefinedType();
    }
  }

  private void setTypeLeftBinary(JsType type, BinaryOperatorTree node) {
    switch (node.left.type) {
      case IDENTIFIER_EXPRESSION:
        identifierTypes.peek().put(node.left.asIdentifierExpression().identifierToken.value, type);
        break;
      case MEMBER_EXPRESSION:
        MemberExpressionTree meTree2 = node.left.asMemberExpression();
        getType(meTree2.operand).setMemberType(meTree2.memberName.value, type);
        break;
      default:
        break;
    }
  }
  


  private JsType getType(ParseTree node) {
    if (node == null) {
      return extractJsCommonTypes.getUndefinedType();
    }
    JsType type = null;
    switch (node.type) {
      case EXPRESSION_STATEMENT:
        return getType(node.asExpressionStatement().expression);
      case UNARY_EXPRESSION:
        return getTypeFromUnaryExpression(node.asUnaryExpression());
      case UPDATE_EXPRESSION:
        return getTypeFromUpdate(node.asUpdateExpression());
      case ARRAY_LITERAL_EXPRESSION:
        return extractJsCommonTypes.getType("Array");
      case LITERAL_EXPRESSION:
        return getTypeFromLiteral(node.asLiteralExpression());
      case CALL_EXPRESSION:
        String require = getRequestExpression(node.asCallExpression());
        if (require == null) {
          return getType(node.asCallExpression().operand);
        } else {
          return file.getRequireType(require);
        }
      case IDENTIFIER_EXPRESSION:
        return getIdentifierType(node.asIdentifierExpression().identifierToken.value);
      case MEMBER_EXPRESSION:
        MemberExpressionTree meTree = node.asMemberExpression();
        return getType(meTree.operand).getMemberType(meTree.memberName.value);
      case CLASS_DECLARATION:
        visitIntern(node.asClassDeclaration());
        return classTypes.pop();
      case FUNCTION_DECLARATION:
        visitIntern(node.asFunctionDeclaration());
        return returnFunctions.pop();
      case NEW_EXPRESSION:
        return getType(node.asNewExpression().operand);
      case OBJECT_LITERAL_EXPRESSION:
        return extractJsCommonTypes.getObjectType();
      case BINARY_OPERATOR:
        BinaryOperatorTree binaryOperatorTree = node.asBinaryOperator();
        switch (binaryOperatorTree.operator.type) {
          case OPEN_ANGLE:
          case CLOSE_ANGLE:
          case LESS_EQUAL:
          case GREATER_EQUAL:
          case EQUAL_EQUAL:
          case NOT_EQUAL:
          case EQUAL_EQUAL_EQUAL:
          case NOT_EQUAL_EQUAL:
          case AND:
          case OR:
            return extractJsCommonTypes.getType("Boolean");
          case MINUS:
          case STAR:
          case STAR_STAR:
          case PERCENT:
          case LEFT_SHIFT:
          case RIGHT_SHIFT:
          case UNSIGNED_RIGHT_SHIFT:
          case AMPERSAND:
          case BAR:
          case CARET:
          case MINUS_EQUAL:
          case STAR_EQUAL:
          case STAR_STAR_EQUAL:
          case PERCENT_EQUAL:
          case LEFT_SHIFT_EQUAL:
          case RIGHT_SHIFT_EQUAL:
          case UNSIGNED_RIGHT_SHIFT_EQUAL:
          case AMPERSAND_EQUAL:
          case BAR_EQUAL:
          case CARET_EQUAL:
          case SLASH:
          case SLASH_EQUAL:
            return extractJsCommonTypes.getType("Number");
          case PLUS:
          case PLUS_EQUAL:
            String leftType = getType(binaryOperatorTree.left).getName();
            String rightType = getType(binaryOperatorTree.right).getName();
            if ((leftType.equals("Boolean") || leftType.equals("Number"))
                && (rightType.equals("Boolean") || rightType.equals("Number"))) {
              type = extractJsCommonTypes.getType("Number");
            } else {
              type = extractJsCommonTypes.getType("String");
            }
            if (TokenType.PLUS_EQUAL.equals(binaryOperatorTree.operator.type)) {
              setTypeLeftBinary(type, binaryOperatorTree);
            }
            return type;
          case EQUAL:
            ASTVisitorHasExportOnBinaryExpression visitor =
                new ASTVisitorHasExportOnBinaryExpression(binaryOperatorTree);
            type = getType(binaryOperatorTree.right);
            if (type.getName() == null) {
              type.setName(getName(binaryOperatorTree.left));
            }
            if (visitor.isExportAssociation()) {
              exportAssociation = true;
              memberExport = visitor.getMemberExport();
              exportType.setMemberType(memberExport, type);
              for (String identifier : visitor.getOthersIdentifiers()) {
                varToExportMember.put(identifier, memberExport);
              }
            }
            setTypeLeftBinary(type, binaryOperatorTree);
            return type;
          default:
            break;
        }
        return extractJsCommonTypes.getUndefinedType();
      default:
        return extractJsCommonTypes.getUndefinedType();
    }
  }

  private String getName(ParseTree node) {
    switch (node.type) {
      case IDENTIFIER_EXPRESSION:
        return node.asIdentifierExpression().identifierToken.value;
      case MEMBER_EXPRESSION:
        return node.asMemberExpression().memberName.value;
      default:
        return null;
    }
  }

  public boolean visit(BinaryOperatorTree node) {
    getType(node);
    return false;
  }


  public boolean visit(VariableDeclarationTree node) {
    if (!ParseTreeType.IDENTIFIER_EXPRESSION.equals(node.lvalue.type)) {
      return false;
    }
    exportAssociation = false;
    String varName = node.lvalue.asIdentifierExpression().identifierToken.value;
    JsType type = getType(node.initializer);
    if (type.getName() == null) {
      type.setName(varName);
    }
    identifierTypes.peek().put(varName, type);
    if (exportAssociation) {
      varToExportMember.put(varName, memberExport);
    }
    return false;
  }


  private String getRequestExpression(CallExpressionTree node) {
    if (node.operand == null) {
      return null;
    }
    if (!ParseTreeType.IDENTIFIER_EXPRESSION.equals(node.operand.type)) {
      return null;
    }
    if (!"require".equals(node.operand.asIdentifierExpression().identifierToken.value)
        && !"import".equals(node.operand.asIdentifierExpression().identifierToken.value)) {
      return null;
    }
    if (node.arguments.arguments.size() != 1) {
      return null;
    }
    ParseTree argNode = node.arguments.arguments.get(0);
    if (!ParseTreeType.LITERAL_EXPRESSION.equals(argNode.type)) {
      return null;
    }
    String importValue = argNode.asLiteralExpression().literalToken.asLiteral().value;
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
    return importValue;
  }

  private JsType getIdentifierTypeIgnoreCommon(String identifier) {
    Deque<Map<String, JsType>> identifierTypesBk = new ArrayDeque<>(identifierTypes.size());
    JsType type = null;
    while (!identifierTypes.isEmpty() && !identifierTypes.peek().containsKey(identifier)) {
      identifierTypesBk.push(identifierTypes.pop());
    }
    if (!identifierTypes.isEmpty()) {
      type = identifierTypes.peek().get(identifier);
    }
    while (!identifierTypesBk.isEmpty()) {
      identifierTypes.push(identifierTypesBk.pop());
    }
    return type;
  }



  private JsType getIdentifierType(String identifier) {
    Deque<Map<String, JsType>> identifierTypesBk = new ArrayDeque<>(identifierTypes.size());
    JsType type = null;
    while (!identifierTypes.isEmpty() && !identifierTypes.peek().containsKey(identifier)) {
      identifierTypesBk.push(identifierTypes.pop());
    }
    if (!identifierTypes.isEmpty()) {
      type = identifierTypes.peek().get(identifier);
    } else {
      type = extractJsCommonTypes.getType(identifier);
    }
    while (!identifierTypesBk.isEmpty()) {
      identifierTypes.push(identifierTypesBk.pop());
    }
    return type;
  }



}
