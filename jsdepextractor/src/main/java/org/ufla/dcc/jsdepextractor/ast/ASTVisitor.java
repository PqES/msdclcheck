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
import com.google.javascript.jscomp.parsing.parser.trees.FormalParameterListTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree;
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
import com.google.javascript.jscomp.parsing.parser.trees.TypedParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypeNameTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypeQueryTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnionTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.UpdateExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationListTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WithStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.YieldExpressionTree;

public abstract class ASTVisitor {

  public void startTraversal(ParseTree node) {
    ASTVisitorTraversal.traversalVisitorFromNode(this, node);
  }

  public boolean visit(AmbientDeclarationTree node) {
    return true;
  }

  public boolean visit(ArgumentListTree node) {
    return true;
  }

  public boolean visit(ArrayLiteralExpressionTree node) {
    return true;
  }

  public boolean visit(ArrayPatternTree node) {
    return true;
  }

  public boolean visit(ArrayTypeTree node) {
    return true;
  }

  public boolean visit(AssignmentRestElementTree node) {
    return true;
  }

  public boolean visit(AwaitExpressionTree node) {
    return true;
  }

  public boolean visit(BinaryOperatorTree node) {
    return true;
  }

  public boolean visit(BlockTree node) {
    return true;
  }

  public boolean visit(BreakStatementTree node) {
    return true;
  }

  public boolean visit(CallExpressionTree node) {
    return true;
  }

  public boolean visit(CallSignatureTree node) {
    return true;
  }

  public boolean visit(CaseClauseTree node) {
    return true;
  }

  public boolean visit(CatchTree node) {
    return true;
  }

  public boolean visit(ClassDeclarationTree node) {
    return true;
  }

  public boolean visit(CommaExpressionTree node) {
    return true;
  }

  public boolean visit(ComprehensionForTree node) {
    return true;
  }

  public boolean visit(ComprehensionIfTree node) {
    return true;
  }

  public boolean visit(ComprehensionTree node) {
    return true;
  }

  public boolean visit(ComputedPropertyDefinitionTree node) {
    return true;
  }

  public boolean visit(ComputedPropertyGetterTree node) {
    return true;
  }

  public boolean visit(ComputedPropertyMemberVariableTree node) {
    return true;
  }

  public boolean visit(ComputedPropertyMethodTree node) {
    return true;
  }

  public boolean visit(ComputedPropertySetterTree node) {
    return true;
  }

  public boolean visit(ConditionalExpressionTree node) {
    return true;
  }

  public boolean visit(ContinueStatementTree node) {
    return true;
  }

  public boolean visit(DebuggerStatementTree node) {
    return true;
  }

  public boolean visit(DefaultClauseTree node) {
    return true;
  }

  public boolean visit(DefaultParameterTree node) {
    return true;
  }

  public boolean visit(DoWhileStatementTree node) {
    return true;
  }

  public boolean visit(EmptyStatementTree node) {
    return true;
  }

  public boolean visit(EnumDeclarationTree node) {
    return true;
  }

  public boolean visit(ExportDeclarationTree node) {
    return true;
  }

  public boolean visit(ExportSpecifierTree node) {
    return true;
  }

  public boolean visit(ExpressionStatementTree node) {
    return true;
  }

  public boolean visit(FinallyTree node) {
    return true;
  }

  public boolean visit(ForAwaitOfStatementTree node) {
    return true;
  }

  public boolean visit(ForInStatementTree node) {
    return true;
  }

  public boolean visit(FormalParameterListTree node) {
    return true;
  }

  public boolean visit(ForOfStatementTree node) {
    return true;
  }

  public boolean visit(ForStatementTree node) {
    return true;
  }

  public boolean visit(FunctionDeclarationTree node) {
    return true;
  }

  public boolean visit(FunctionTypeTree node) {
    return true;
  }

  public boolean visit(GenericTypeListTree node) {
    return true;
  }

  public boolean visit(GetAccessorTree node) {
    return true;
  }

  public boolean visit(IdentifierExpressionTree node) {
    return true;
  }

  public boolean visit(IfStatementTree node) {
    return true;
  }

  public boolean visit(ImportDeclarationTree node) {
    return true;
  }

  public boolean visit(ImportSpecifierTree node) {
    return true;
  }

  public boolean visit(IndexSignatureTree node) {
    return true;
  }

  public boolean visit(InterfaceDeclarationTree node) {
    return true;
  }

  public boolean visit(LabelledStatementTree node) {
    return true;
  }

  public boolean visit(LiteralExpressionTree node) {
    return true;
  }

  public boolean visit(MemberExpressionTree node) {
    return true;
  }

  public boolean visit(MemberLookupExpressionTree node) {
    return true;
  }

  public boolean visit(MemberVariableTree node) {
    return true;
  }

  public boolean visit(MissingPrimaryExpressionTree node) {
    return true;
  }

  public boolean visit(NamespaceDeclarationTree node) {
    return true;
  }

  public boolean visit(NamespaceNameTree node) {
    return true;
  }

  public boolean visit(NewExpressionTree node) {
    return true;
  }

  public boolean visit(NewTargetExpressionTree node) {
    return true;
  }

  public boolean visit(NullTree node) {
    return true;
  }

  public boolean visit(ObjectLiteralExpressionTree node) {
    return true;
  }

  public boolean visit(ObjectPatternTree node) {
    return true;
  }

  public boolean visit(OptionalParameterTree node) {
    return true;
  }

  public boolean visit(ParameterizedTypeTree node) {
    return true;
  }

  public boolean visit(ParenExpressionTree node) {
    return true;
  }

  public boolean visit(ParseTree node) {
    return true;
  }

  public boolean visit(ProgramTree node) {
    return true;
  }

  public boolean visit(PropertyNameAssignmentTree node) {
    return true;
  }

  public boolean visit(RecordTypeTree node) {
    return true;
  }

  public boolean visit(RestParameterTree node) {
    return true;
  }

  public boolean visit(ReturnStatementTree node) {
    return true;
  }

  public boolean visit(SetAccessorTree node) {
    return true;
  }

  public boolean visit(SpreadExpressionTree node) {
    return true;
  }

  public boolean visit(SuperExpressionTree node) {
    return true;
  }

  public boolean visit(SwitchStatementTree node) {
    return true;
  }

  public boolean visit(TemplateLiteralExpressionTree node) {
    return true;
  }

  public boolean visit(TemplateLiteralPortionTree node) {
    return true;
  }

  public boolean visit(TemplateSubstitutionTree node) {
    return true;
  }

  public boolean visit(ThisExpressionTree node) {
    return true;
  }

  public boolean visit(ThrowStatementTree node) {
    return true;
  }

  public boolean visit(TryStatementTree node) {
    return true;
  }

  public boolean visit(TypeAliasTree node) {
    return true;
  }

  public boolean visit(TypedParameterTree node) {
    return true;
  }

  public boolean visit(TypeNameTree node) {
    return true;
  }

  public boolean visit(TypeQueryTree node) {
    return true;
  }

  public boolean visit(UnaryExpressionTree node) {
    return true;
  }

  public boolean visit(UnionTypeTree node) {
    return true;
  }

  public boolean visit(UpdateExpressionTree node) {
    return true;
  }

  public boolean visit(VariableDeclarationListTree node) {
    return true;
  }

  public boolean visit(VariableDeclarationTree node) {
    return true;
  }

  public boolean visit(VariableStatementTree node) {
    return true;
  }

  public boolean visit(WhileStatementTree node) {
    return true;
  }

  public boolean visit(WithStatementTree node) {
    return true;
  }

  public boolean visit(YieldExpressionTree node) {
    return true;
  }

}
