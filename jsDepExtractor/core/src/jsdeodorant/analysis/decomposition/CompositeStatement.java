package jsdeodorant.analysis.decomposition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;

import jsdeodorant.analysis.abstraction.SourceContainer;
import jsdeodorant.analysis.abstraction.SourceElement;
import jsdeodorant.analysis.util.ExpressionExtractor;

public class CompositeStatement extends AbstractStatement implements SourceContainer {

	private List<AbstractStatement> statementList;
	private List<AbstractExpression> expressionList;

	public CompositeStatement(ParseTree statement, StatementType type, SourceContainer parent, SourceFile sourceFile) throws IOException {
		super(statement, type, parent);
		statementList = new ArrayList<>();
		expressionList = new ArrayList<>();
		
		// Following lines are experimental, should be double checked!
		ExpressionExtractor expressionExtractor = new ExpressionExtractor();
		processFunctionInvocations(expressionExtractor.getCallExpressions(statement),sourceFile);
		processArrayLiteralExpressions(expressionExtractor.getArrayLiteralExpressions(statement));
		processAssignmentExpressions(expressionExtractor.getBinaryOperators(statement));
		processVariableDeclarations(expressionExtractor.getVariableDeclarationExpressions(statement));
		processNewExpressions(expressionExtractor.getNewExpressions(statement), sourceFile);
	}

	@Override
	public void addElement(SourceElement element) {
		if (element instanceof AbstractStatement)
			addStatement((AbstractStatement) element);
	}

	public void addStatement(AbstractStatement statement) {
		statementList.add(statement);
	}

	public void addExpression(AbstractExpression expression) {
		expressionList.add(expression);
	}

	public List<AbstractStatement> getStatements() {
		return statementList;
	}

	public List<AbstractExpression> getExpressions() {
		return expressionList;
	}

	@Override
	public List<FunctionDeclaration> getFunctionDeclarationList() {
		List<FunctionDeclaration> functionDeclarations = new ArrayList<>();
		if (this instanceof FunctionDeclarationStatement) {
			functionDeclarations.add((FunctionDeclarationStatement) this);
		}
		for (AbstractStatement statement : statementList) {
			functionDeclarations.addAll(statement.getFunctionDeclarationList());
		}
		return functionDeclarations;
	}

	@Override
	public List<ObjectLiteralExpression> getObjectLiteralList() {
		List<ObjectLiteralExpression> objectLiterals = new ArrayList<>();
		for (AbstractStatement statement : statementList) {
			objectLiterals.addAll(statement.getObjectLiteralList());
		}
		return objectLiterals;
	}

	/*
	 * public String toString() { StringBuilder sb = new StringBuilder();
	 * sb.append(getType().toString()); if (expressionList.size() > 0) {
	 * sb.append("("); for (int i = 0; i < expressionList.size() - 1; i++) {
	 * sb.append(expressionList.get(i).toString()).append("; "); }
	 * sb.append(expressionList.get(expressionList.size() - 1).toString());
	 * sb.append(")"); } sb.append("\n"); return sb.toString(); }
	 */

}
