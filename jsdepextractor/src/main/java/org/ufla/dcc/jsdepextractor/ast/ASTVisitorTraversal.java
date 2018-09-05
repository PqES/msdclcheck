package org.ufla.dcc.jsdepextractor.ast;

import com.google.javascript.jscomp.parsing.parser.trees.AmbientDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ArgumentListTree;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayPatternTree;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.AssignmentRestElementTree;
import com.google.javascript.jscomp.parsing.parser.trees.AwaitExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.BlockTree;
import com.google.javascript.jscomp.parsing.parser.trees.BreakStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallSignatureTree;
import com.google.javascript.jscomp.parsing.parser.trees.CaseClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.CatchTree;
import com.google.javascript.jscomp.parsing.parser.trees.ClassDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.CommaExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComprehensionForTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComprehensionIfTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComprehensionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyDefinitionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyGetterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyMemberVariableTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyMethodTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertySetterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ConditionalExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ContinueStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DebuggerStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DefaultClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.DefaultParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.DoWhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.EmptyStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.EnumDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FinallyTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForAwaitOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FormalParameterListTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.GenericTypeListTree;
import com.google.javascript.jscomp.parsing.parser.trees.GetAccessorTree;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.IfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.IndexSignatureTree;
import com.google.javascript.jscomp.parsing.parser.trees.InterfaceDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.LabelledStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.LiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberLookupExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberVariableTree;
import com.google.javascript.jscomp.parsing.parser.trees.MissingPrimaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NamespaceDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.NamespaceNameTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewTargetExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NullTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectPatternTree;
import com.google.javascript.jscomp.parsing.parser.trees.OptionalParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParameterizedTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParenExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import com.google.javascript.jscomp.parsing.parser.trees.PropertyNameAssignmentTree;
import com.google.javascript.jscomp.parsing.parser.trees.RecordTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.RestParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ReturnStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.SetAccessorTree;
import com.google.javascript.jscomp.parsing.parser.trees.SpreadExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.SuperExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.SwitchStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TemplateLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.TemplateLiteralPortionTree;
import com.google.javascript.jscomp.parsing.parser.trees.TemplateSubstitutionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThisExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThrowStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TryStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypeAliasTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypeNameTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypeQueryTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypedParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnionTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.UpdateExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationListTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WithStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.YieldExpressionTree;

class ASTVisitorTraversal {

  static void traversalVisitorFromNode(ASTVisitor astVisitor, ParseTree node) {
    ASTVisitorTraversal astVisitorTraversal = new ASTVisitorTraversal(astVisitor);
    astVisitorTraversal.traversal(node);
  }


  private ASTVisitor astVisitor;


  private ASTVisitorTraversal(ASTVisitor astVisitor) {
    this.astVisitor = astVisitor;
  }


  private void traversal(AmbientDeclarationTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.declaration);
  }

  private void traversal(ArgumentListTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    if (node.arguments == null) {
      return;
    }
    for (ParseTree childNode : node.arguments) {
      traversal(childNode);
    }
  }

  private void traversal(ArrayLiteralExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    if (node.elements == null) {
      return;
    }
    for (ParseTree childNode : node.elements) {
      traversal(childNode);
    }
  }

  private void traversal(ArrayPatternTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    if (node.elements == null) {
      return;
    }
    for (ParseTree childNode : node.elements) {
      traversal(childNode);
    }
  }

  private void traversal(ArrayTypeTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.elementType);
  }

  private void traversal(AssignmentRestElementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.assignmentTarget);
  }

  private void traversal(AwaitExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
  }

  private void traversal(BinaryOperatorTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.left);
    traversal(node.right);
  }

  private void traversal(BlockTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    if (node.statements == null) {
      return;
    }
    for (ParseTree childNode : node.statements) {
      traversal(childNode);
    }
  }

  private void traversal(BreakStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(CallExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.operand);
    traversal((ParseTree) node.arguments);
  }

  private void traversal(CallSignatureTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal((ParseTree) node.generics);
    traversal((ParseTree) node.formalParameterList);
    traversal(node.returnType);
  }

  private void traversal(CaseClauseTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
    if (node.statements == null) {
      return;
    }
    for (ParseTree childNode : node.statements) {
      traversal(childNode);
    }
  }

  private void traversal(CatchTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.exception);
    traversal(node.catchBody);
  }

  private void traversal(ClassDeclarationTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal((ParseTree) node.generics);
    traversal(node.superClass);
    if (node.interfaces != null) {
      for (ParseTree childNode : node.interfaces) {
        traversal(childNode);
      }
    }
    if (node.elements == null) {
      return;
    }
    for (ParseTree childNode : node.elements) {
      traversal(childNode);
    }
  }

  private void traversal(CommaExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    if (node.expressions == null) {
      return;
    }
    for (ParseTree childNode : node.expressions) {
      traversal(childNode);
    }
  }

  private void traversal(ComprehensionForTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.initializer);
    traversal(node.collection);
  }

  private void traversal(ComprehensionIfTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
  }

  private void traversal(ComprehensionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    if (node.children != null) {
      for (ParseTree childNode : node.children) {
        traversal(childNode);
      }
    }
    traversal(node.tailExpression);
  }

  private void traversal(ComputedPropertyDefinitionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.property);
    traversal(node.value);
  }

  private void traversal(ComputedPropertyGetterTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.property);
    traversal(node.returnType);
    traversal((ParseTree) node.body);
  }

  private void traversal(ComputedPropertyMemberVariableTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.property);
    traversal(node.declaredType);
  }

  private void traversal(ComputedPropertyMethodTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.property);
    traversal(node.method);
  }

  private void traversal(ComputedPropertySetterTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.property);
    traversal((ParseTree) node.parameter);
    traversal((ParseTree) node.body);
  }

  private void traversal(ConditionalExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.condition);
    traversal(node.left);
    traversal(node.right);
  }

  private void traversal(ContinueStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(DebuggerStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(DefaultClauseTree node) {
    if (!astVisitor.visit(node) || node.statements == null) {
      return;
    }
    for (ParseTree childNode : node.statements) {
      traversal(childNode);
    }
  }

  private void traversal(DefaultParameterTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.lhs);
    traversal(node.defaultValue);
  }

  private void traversal(DoWhileStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.body);
    traversal(node.condition);
  }

  private void traversal(EmptyStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(EnumDeclarationTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    for (ParseTree childNode : node.members) {
      traversal(childNode);
    }
  }

  private void traversal(ExportDeclarationTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.declaration);
    if (node.exportSpecifierList == null) {
      return;
    }
    for (ParseTree childNode : node.exportSpecifierList) {
      traversal(childNode);
    }
  }

  private void traversal(ExportSpecifierTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(ExpressionStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
  }

  private void traversal(FinallyTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.block);
  }

  private void traversal(ForAwaitOfStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.initializer);
    traversal(node.collection);
    traversal(node.body);
  }

  private void traversal(ForInStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.initializer);
    traversal(node.collection);
    traversal(node.body);
  }

  private void traversal(FormalParameterListTree node) {
    if (!astVisitor.visit(node) || node.parameters == null) {
      return;
    }
    for (ParseTree childNode : node.parameters) {
      traversal(childNode);
    }
  }

  private void traversal(ForOfStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.initializer);
    traversal(node.collection);
    traversal(node.body);
  }

  private void traversal(ForStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.initializer);
    traversal(node.condition);
    traversal(node.increment);
    traversal(node.body);
  }

  private void traversal(FunctionDeclarationTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal((ParseTree) node.generics);
    traversal((ParseTree) node.formalParameterList);
    traversal(node.returnType);
    traversal(node.functionBody);

  }

  private void traversal(FunctionTypeTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal((ParseTree) node.formalParameterList);
    traversal(node.returnType);
  }

  private void traversal(GenericTypeListTree node) {
    if (!astVisitor.visit(node) || node.generics == null || node.generics.values() == null) {
      return;
    }
    for (ParseTree childNode : node.generics.values()) {
      traversal(childNode);
    }
  }

  private void traversal(GetAccessorTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal((ParseTree) node.body);
    traversal(node.returnType);
  }

  private void traversal(IdentifierExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(IfStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.condition);
    traversal(node.ifClause);
    traversal(node.elseClause);
  }

  private void traversal(ImportDeclarationTree node) {
    if (!astVisitor.visit(node) || node.importSpecifierList == null) {
      return;
    }
    for (ParseTree childNode : node.importSpecifierList) {
      traversal(childNode);
    }
  }

  private void traversal(ImportSpecifierTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(IndexSignatureTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.name);
    traversal(node.declaredType);
  }

  private void traversal(InterfaceDeclarationTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal((ParseTree) node.generics);
    if (node.superInterfaces != null) {
      for (ParseTree childNode : node.superInterfaces) {
        traversal(childNode);
      }
    }
    if (node.elements != null) {
      for (ParseTree childNode : node.elements) {
        traversal(childNode);
      }
    }
  }

  private void traversal(LabelledStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.statement);
  }

  private void traversal(LiteralExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(MemberExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.operand);
  }

  private void traversal(MemberLookupExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.operand);
    traversal(node.memberExpression);
  }

  private void traversal(MemberVariableTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.declaredType);
  }

  private void traversal(MissingPrimaryExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(NamespaceDeclarationTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal((ParseTree) node.name);
    if (node.elements != null) {
      for (ParseTree childNode : node.elements) {
        traversal(childNode);
      }
    }
  }

  private void traversal(NamespaceNameTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(NewExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.operand);
    traversal((ParseTree) node.arguments);
  }

  private void traversal(NewTargetExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(NullTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(ObjectLiteralExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    for (ParseTree childNode : node.propertyNameAndValues) {
      traversal(childNode);
    }
  }

  private void traversal(ObjectPatternTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    for (ParseTree childNode : node.fields) {
      traversal(childNode);
    }
  }

  private void traversal(OptionalParameterTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.param);
  }

  private void traversal(ParameterizedTypeTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal((ParseTree) node.typeName);
    if (node.typeArguments != null) {
      for (ParseTree childNode : node.typeArguments) {
        traversal(childNode);
      }
    }
  }

  private void traversal(ParenExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
  }

  private void traversal(ParseTree node) {
    if (node == null || !astVisitor.visit(node)) {
      return;
    }
    switch (node.type) {
      case AMBIENT_DECLARATION:
        traversal(node.asAmbientDeclaration());
        break;
      case ARGUMENT_LIST:
        traversal((ArgumentListTree) node);
        break;
      case ARRAY_LITERAL_EXPRESSION:
        traversal(node.asArrayLiteralExpression());
        break;
      case ARRAY_PATTERN:
        traversal(node.asArrayPattern());
        break;
      case ARRAY_TYPE:
        traversal(node.asArrayType());
        break;
      case ASSIGNMENT_REST_ELEMENT:
        traversal(node.asAssignmentRestElement());
        break;
      case AWAIT_EXPRESSION:
        traversal(node.asAwaitExpression());
        break;
      case BINARY_OPERATOR:
        traversal(node.asBinaryOperator());
        break;
      case BLOCK:
        traversal(node.asBlock());
        break;
      case BREAK_STATEMENT:
        traversal(node.asBreakStatement());
        break;
      case CALL_EXPRESSION:
        traversal(node.asCallExpression());
        break;
      case CALL_SIGNATURE:
        traversal(node.asCallSignature());
        break;
      case CASE_CLAUSE:
        traversal(node.asCaseClause());
        break;
      case CATCH:
        traversal(node.asCatch());
        break;
      case CLASS_DECLARATION:
        traversal(node.asClassDeclaration());
        break;
      case COMMA_EXPRESSION:
        traversal(node.asCommaExpression());
        break;
      case COMPREHENSION_FOR:
        traversal(node.asComprehensionFor());
        break;
      case COMPREHENSION_IF:
        traversal(node.asComprehensionIf());
        break;
      case COMPREHENSION:
        traversal(node.asComprehension());
        break;
      case COMPUTED_PROPERTY_DEFINITION:
        traversal(node.asComputedPropertyDefinition());
        break;
      case COMPUTED_PROPERTY_GETTER:
        traversal(node.asComputedPropertyGetter());
        break;
      case COMPUTED_PROPERTY_MEMBER_VARIABLE:
        traversal(node.asComputedPropertyMemberVariable());
        break;
      case COMPUTED_PROPERTY_METHOD:
        traversal(node.asComputedPropertyMethod());
        break;
      case COMPUTED_PROPERTY_SETTER:
        traversal(node.asComputedPropertySetter());
        break;
      case CONDITIONAL_EXPRESSION:
        traversal(node.asConditionalExpression());
        break;
      case CONTINUE_STATEMENT:
        traversal(node.asContinueStatement());
        break;
      case DEBUGGER_STATEMENT:
        traversal(node.asDebuggerStatement());
        break;
      case DEFAULT_CLAUSE:
        traversal(node.asDefaultClause());
        break;
      case DEFAULT_PARAMETER:
        traversal(node.asDefaultParameter());
        break;
      case DO_WHILE_STATEMENT:
        traversal(node.asDoWhileStatement());
        break;
      case EMPTY_STATEMENT:
        traversal(node.asEmptyStatement());
        break;
      case ENUM_DECLARATION:
        traversal(node.asEnumDeclaration());
        break;
      case EXPORT_DECLARATION:
        traversal(node.asExportDeclaration());
        break;
      case EXPORT_SPECIFIER:
        traversal(node.asExportSpecifier());
        break;
      case EXPRESSION_STATEMENT:
        traversal(node.asExpressionStatement());
        break;
      case FINALLY:
        traversal(node.asFinally());
        break;
      case FOR_AWAIT_OF_STATEMENT:
        traversal(node.asForAwaitOfStatement());
        break;
      case FOR_IN_STATEMENT:
        traversal(node.asForInStatement());
        break;
      case FORMAL_PARAMETER_LIST:
        traversal(node.asFormalParameterList());
        break;
      case FOR_OF_STATEMENT:
        traversal(node.asForOfStatement());
        break;
      case FOR_STATEMENT:
        traversal(node.asForStatement());
        break;
      case FUNCTION_DECLARATION:
        traversal(node.asFunctionDeclaration());
        break;
      case FUNCTION_TYPE:
        traversal(node.asFunctionType());
        break;
      case GENERIC_TYPE_LIST:
        traversal(node.asGenericTypeList());
        break;
      case GET_ACCESSOR:
        traversal(node.asGetAccessor());
        break;
      case IDENTIFIER_EXPRESSION:
        traversal(node.asIdentifierExpression());
        break;
      case IF_STATEMENT:
        traversal(node.asIfStatement());
        break;
      case IMPORT_DECLARATION:
        traversal(node.asImportDeclaration());
        break;
      case IMPORT_SPECIFIER:
        traversal(node.asImportSpecifier());
        break;
      case INDEX_SIGNATURE:
        traversal(node.asIndexSignature());
        break;
      case INTERFACE_DECLARATION:
        traversal(node.asInterfaceDeclaration());
        break;
      case LABELLED_STATEMENT:
        traversal(node.asLabelledStatement());
        break;
      case LITERAL_EXPRESSION:
        traversal(node.asLiteralExpression());
        break;
      case MEMBER_EXPRESSION:
        traversal(node.asMemberExpression());
        break;
      case MEMBER_LOOKUP_EXPRESSION:
        traversal(node.asMemberLookupExpression());
        break;
      case MEMBER_VARIABLE:
        traversal(node.asMemberVariable());
        break;
      case MISSING_PRIMARY_EXPRESSION:
        traversal(node.asMissingPrimaryExpression());
        break;
      case NAMESPACE_DECLARATION:
        traversal(node.asNamespaceDeclaration());
        break;
      case NAMESPACE_NAME:
        traversal((NamespaceNameTree) node);
        break;
      case NEW_EXPRESSION:
        traversal(node.asNewExpression());
        break;
      case NEW_TARGET_EXPRESSION:
        traversal(node.asNewTargetExpression());
        break;
      case NULL:
        traversal(node.asNull());
        break;
      case OBJECT_LITERAL_EXPRESSION:
        traversal(node.asObjectLiteralExpression());
        break;
      case OBJECT_PATTERN:
        traversal(node.asObjectPattern());
        break;
      case OPTIONAL_PARAMETER:
        traversal(node.asOptionalParameter());
        break;
      case PARAMETERIZED_TYPE_TREE:
        traversal(node.asParameterizedType());
        break;
      case PAREN_EXPRESSION:
        traversal(node.asParenExpression());
        break;
      case PROGRAM:
        traversal(node.asProgram());
        break;
      case PROPERTY_NAME_ASSIGNMENT:
        traversal(node.asPropertyNameAssignment());
        break;
      case RECORD_TYPE:
        traversal(node.asRecordType());
        break;
      case REST_PARAMETER:
        traversal(node.asRestParameter());
        break;
      case RETURN_STATEMENT:
        traversal(node.asReturnStatement());
        break;
      case SET_ACCESSOR:
        traversal(node.asSetAccessor());
        break;
      case SPREAD_EXPRESSION:
        traversal(node.asSpreadExpression());
        break;
      case SUPER_EXPRESSION:
        traversal(node.asSuperExpression());
        break;
      case SWITCH_STATEMENT:
        traversal(node.asSwitchStatement());
        break;
      case TEMPLATE_LITERAL_EXPRESSION:
        traversal(node.asTemplateLiteralExpression());
        break;
      case TEMPLATE_LITERAL_PORTION:
        traversal(node.asTemplateLiteralPortion());
        break;
      case TEMPLATE_SUBSTITUTION:
        traversal(node.asTemplateSubstitution());
        break;
      case THIS_EXPRESSION:
        traversal(node.asThisExpression());
        break;
      case THROW_STATEMENT:
        traversal(node.asThrowStatement());
        break;
      case TRY_STATEMENT:
        traversal(node.asTryStatement());
        break;
      case TYPE_ALIAS:
        traversal(node.asTypeAlias());
        break;
      case TYPED_PARAMETER:
        traversal(node.asTypedParameter());
        break;
      case TYPE_NAME:
        traversal(node.asTypeName());
        break;
      case TYPE_QUERY:
        traversal(node.asTypeQuery());
        break;
      case UNARY_EXPRESSION:
        traversal(node.asUnaryExpression());
        break;
      case UNION_TYPE:
        traversal(node.asUnionType());
        break;
      case UPDATE_EXPRESSION:
        traversal(node.asUpdateExpression());
        break;
      case VARIABLE_DECLARATION_LIST:
        traversal(node.asVariableDeclarationList());
        break;
      case VARIABLE_DECLARATION:
        traversal(node.asVariableDeclaration());
        break;
      case VARIABLE_STATEMENT:
        traversal(node.asVariableStatement());
        break;
      case WHILE_STATEMENT:
        traversal(node.asWhileStatement());
        break;
      case WITH_STATEMENT:
        traversal(node.asWithStatement());
        break;
      case YIELD_EXPRESSION:
        traversal((YieldExpressionTree) node);
        break;
      case MODULE_IMPORT:
        System.out.print("ModuleImport");
        break;
    }
  }

  private void traversal(ProgramTree node) {
    if (!astVisitor.visit(node) || node.sourceElements == null) {
      return;
    }
    for (ParseTree childNode : node.sourceElements) {
      traversal(childNode);
    }
  }

  private void traversal(PropertyNameAssignmentTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.value);
  }

  private void traversal(RecordTypeTree node) {
    if (!astVisitor.visit(node) || node.members == null) {
      return;
    }
    for (ParseTree childNode : node.members) {
      traversal(childNode);
    }
  }

  private void traversal(RestParameterTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.assignmentTarget);
  }

  private void traversal(ReturnStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
  }

  private void traversal(SetAccessorTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal((ParseTree) node.parameter);
    traversal((ParseTree) node.body);
  }

  private void traversal(SpreadExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
  }

  private void traversal(SuperExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(SwitchStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
    if (node.caseClauses != null) {
      for (ParseTree childNode : node.caseClauses) {
        traversal(childNode);
      }
    }
  }

  private void traversal(TemplateLiteralExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.operand);
    if (node.elements != null) {
      for (ParseTree childNode : node.elements) {
        traversal(childNode);
      }
    }
  }

  private void traversal(TemplateLiteralPortionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(TemplateSubstitutionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
  }

  private void traversal(ThisExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(ThrowStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.value);
  }

  private void traversal(TryStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.body);
    traversal(node.catchBlock);
    traversal(node.finallyBlock);
  }

  private void traversal(TypeAliasTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.original);
  }

  private void traversal(TypedParameterTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.param);
    traversal(node.typeAnnotation);
  }

  private void traversal(TypeNameTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(TypeQueryTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
  }

  private void traversal(UnaryExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.operand);
  }

  private void traversal(UnionTypeTree node) {
    if (!astVisitor.visit(node) || node.types == null) {
      return;
    }
    for (ParseTree childNode : node.types) {
      traversal(childNode);
    }
  }

  private void traversal(UpdateExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.operand);
  }

  private void traversal(VariableDeclarationListTree node) {
    if (!astVisitor.visit(node) || node.declarations == null) {
      return;
    }
    for (ParseTree childNode : node.declarations) {
      traversal((ParseTree) childNode);
    }
  }

  private void traversal(VariableDeclarationTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.lvalue);
    traversal(node.declaredType);
    traversal(node.initializer);
  }

  private void traversal(VariableStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal((ParseTree) node.declarations);
  }

  private void traversal(WhileStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.condition);
    traversal(node.body);
  }

  private void traversal(WithStatementTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
    traversal(node.body);
  }

  private void traversal(YieldExpressionTree node) {
    if (!astVisitor.visit(node)) {
      return;
    }
    traversal(node.expression);
  }

}
