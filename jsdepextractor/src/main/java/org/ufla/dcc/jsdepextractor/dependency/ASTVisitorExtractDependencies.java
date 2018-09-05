package org.ufla.dcc.jsdepextractor.dependency;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
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
import org.ufla.dcc.jsdepextractor.project.dependency.Dependency;
import org.ufla.dcc.jsdepextractor.project.dependency.DependencyFactory;
import org.ufla.dcc.jsdepextractor.project.dependency.DependencyType;
import com.google.javascript.jscomp.parsing.parser.LiteralToken;
import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.BlockTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ClassDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.DoWhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForAwaitOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.IfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.LabelledStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.LiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectLiteralExpressionTree;
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

public class ASTVisitorExtractDependencies extends ASTVisitor {

  private Collection<Dependency> dependencies;

  private Deque<Map<String, JsType>> identifierTypes;

  private ExtractJsCommonTypes extractJsCommonTypes;

  private JsFile file;

  private String relativePath;

  private Deque<FunctionDeclarationTree> functions;

  private Deque<JsType> returnFunctions;

  private Deque<ClassDeclarationTree> classDeclarations;

  private Deque<JsType> classTypes;

  public ASTVisitorExtractDependencies(JsFile file) {
    this.file = file;
    relativePath = file.getModule().getRelativePath();
    dependencies = new ArrayList<Dependency>();
    identifierTypes = new ArrayDeque<Map<String, JsType>>();
    identifierTypes.push(new HashMap<>());
    extractJsCommonTypes = ExtractJsCommonTypes.getInstance();
    functions = new ArrayDeque<>();
    classDeclarations = new ArrayDeque<>();
    returnFunctions = new ArrayDeque<>();
    classTypes = new ArrayDeque<>();
    ParseTree parseTree = ScriptParser.getInstance().parse(file);
//    if (file.getQualifiedName().equals("app.server")) {
//      System.out.println("OK");
//    }
    startTraversal(parseTree);
  }

  // public boolean visit(Statement node) {
  // switch (node.type) {
  // case IMPORT_DECLARATION:
  // case FUNCTION_DECLARATION:
  // case CLASS_DECLARATION:
  // case BINARY_OPERATOR:
  // case VARIABLE_DECLARATION:
  // case RETURN_STATEMENT:
  // case BLOCK:
  // return true;
  // default:
  // getType(node);
  //// return true;
  //// }
  //
  // }

  public boolean visit(NewExpressionTree node) {
    getType(node);
    return false;
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
//    if (file.getQualifiedName().equals("app.server")) {
//      System.out.println("OK");
//    }
    JsType typeImport = file.getRequireType(importValue);
    if (node.defaultBindingIdentifier != null) {
      String identifier = node.defaultBindingIdentifier.value;
      identifierTypes.peek().put(identifier, typeImport);
      dependencies.add(DependencyFactory.createDependency(DependencyType.ACCESS, file, typeImport,
          node.location.start.line, node.location.start.offset));
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
        dependencies.add(DependencyFactory.createDependency(DependencyType.ACCESS, file, typeImport,
            node.location.start.line, node.location.start.offset));
      }
    }
    if (node.nameSpaceImportIdentifier != null) {
      identifierTypes.peek().put(node.nameSpaceImportIdentifier.value, typeImport);
      dependencies.add(DependencyFactory.createDependency(DependencyType.ACCESS, file, typeImport,
          node.location.start.line, node.location.start.offset));
    }
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
    dependencies.add(DependencyFactory.createDependency(DependencyType.ACCESS, file, newType,
        node.location.start.line, node.location.start.offset));
    newType.setName(jsType.getName());
    returnFunctions.push(newType);
    return false;


  }

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
  
  public boolean visit(UnaryExpressionTree node) {
    getType(node);
    return false;
  }
  
  public boolean visit(UpdateExpressionTree node) {
    getType(node);
    return false;
  }
  
  public boolean visit(ArrayLiteralExpressionTree node) {
    getType(node);
    return false;
  }
  
  public boolean visit(LiteralExpressionTree node) {
    getType(node);
    return false;
  }
  
  public boolean visit(CallExpressionTree node) {
    getType(node);
    return false;
  }
  
  public boolean visit(IdentifierExpressionTree node) {
    getType(node);
    return false;
  }
  
  public boolean visit(MemberExpressionTree node) {
    getType(node);
    return false;
  }
  
  public boolean visit(ObjectLiteralExpressionTree node) {
    getType(node);
    return false;
  }

  private JsType getType(ParseTree node) {
    if (node == null) {
      return extractJsCommonTypes.getUndefinedType();
    }
    JsType type = null;
    DependencyType depType = DependencyType.ACCESS;
    switch (node.type) {
      case UNARY_EXPRESSION:
        type = getTypeFromUnaryExpression(node.asUnaryExpression());
        break;
      case UPDATE_EXPRESSION:
        type = getTypeFromUpdate(node.asUpdateExpression());
        break;
      case ARRAY_LITERAL_EXPRESSION:
        type = extractJsCommonTypes.getType("Array");
        break;
      case LITERAL_EXPRESSION:
        type = getTypeFromLiteral(node.asLiteralExpression());
        break;
      case CALL_EXPRESSION:
        CallExpressionTree nodeCall = node.asCallExpression();
        String require = getRequestExpression(nodeCall);
        if (require == null) {
          type = getType(nodeCall.operand);
          if (nodeCall.arguments != null) {
            for (ParseTree nodeChild : nodeCall.arguments.arguments) {
              getType(nodeChild);
            }
          }
        } else {
          type = file.getRequireType(require);
        }
        break;
      case IDENTIFIER_EXPRESSION:
        type = getIdentifierType(node.asIdentifierExpression().identifierToken.value);
        break;
      case MEMBER_EXPRESSION:
        MemberExpressionTree meTree = node.asMemberExpression();
        type = getType(meTree.operand).getMemberType(meTree.memberName.value);
        break;
      case CLASS_DECLARATION:
        visitIntern(node.asClassDeclaration());
        type = classTypes.pop();
        break;
      case FUNCTION_DECLARATION:
        visitIntern(node.asFunctionDeclaration());
        type = returnFunctions.pop();
        break;
      case NEW_EXPRESSION:
        type = getType(node.asNewExpression().operand);
        depType = DependencyType.CREATE;
        break;
      case OBJECT_LITERAL_EXPRESSION:
        type = extractJsCommonTypes.getObjectType();
        break;
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
            type = extractJsCommonTypes.getType("Boolean");
            break;
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
            type = extractJsCommonTypes.getType("Number");
            break;
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
            break;
          case EQUAL:
            type = getType(binaryOperatorTree.right);
            if (type.getName() == null) {
              type.setName(getName(binaryOperatorTree.left));
            }
            setTypeLeftBinary(type, binaryOperatorTree);
            break;
          default:
            break;
        }
        type = extractJsCommonTypes.getUndefinedType();
        break;
      default:
        type = extractJsCommonTypes.getUndefinedType();
    }
    dependencies.add(DependencyFactory.createDependency(depType, file, type,
        node.location.start.line, node.location.start.offset));
    return type;
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
    String varName = node.lvalue.asIdentifierExpression().identifierToken.value;
    JsType type = getType(node.initializer);
    if (type.getName() == null) {
      type.setName(varName);
    }
    identifierTypes.peek().put(varName, type);
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

  public Collection<Dependency> getDependencies() {
    return dependencies;
  }



}
