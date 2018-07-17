package jsdeodorant.analysis.abstraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.BlockTree;
import com.google.javascript.jscomp.parsing.parser.trees.BreakStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.CaseClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.CatchTree;
import com.google.javascript.jscomp.parsing.parser.trees.ContinueStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DebuggerStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DefaultClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.DoWhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.EmptyStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FinallyTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.IfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.LabelledStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParenExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ReturnStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.SwitchStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThrowStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TryStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationListTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WithStatementTree;

import jsdeodorant.analysis.decomposition.AbstractExpression;
import jsdeodorant.analysis.decomposition.CompositeStatement;
import jsdeodorant.analysis.decomposition.FunctionDeclarationExpression;
import jsdeodorant.analysis.decomposition.FunctionDeclarationExpressionNature;
import jsdeodorant.analysis.decomposition.FunctionDeclarationStatement;
import jsdeodorant.analysis.decomposition.LabelledStatement;
import jsdeodorant.analysis.decomposition.ObjectLiteralExpression;
import jsdeodorant.analysis.decomposition.Statement;
import jsdeodorant.analysis.decomposition.StatementType;
import jsdeodorant.analysis.decomposition.TryStatement;
import jsdeodorant.analysis.util.IdentifierHelper;

public class StatementProcessor {
//	private static final Logger log = Logger.getLogger(StatementProcessor.class.getName());

	public static void processStatement(ParseTree statement, SourceContainer parent, SourceFile sourceFile) throws IOException {
		if (statement instanceof BlockTree) {
			BlockTree block = statement.asBlock();
			CompositeStatement child = new CompositeStatement(block, StatementType.BLOCK, parent,sourceFile);
			parent.addElement(child);
			ImmutableList<ParseTree> statements = block.statements;
			for (ParseTree blockStatement : statements) {
				processStatement(blockStatement, child, sourceFile);
			}
		}

		else if (statement instanceof FunctionDeclarationTree) {
			FunctionDeclarationTree functionDeclarationTree = statement.asFunctionDeclaration();
			FunctionDeclarationStatement child = new FunctionDeclarationStatement(functionDeclarationTree, parent, sourceFile);
			parent.addElement(child);
		}

		else if (statement instanceof IfStatementTree) {
			IfStatementTree ifStatement = statement.asIfStatement();
			CompositeStatement child = new CompositeStatement(ifStatement, StatementType.IF, parent,sourceFile);
			AbstractExpression conditionExpression = new AbstractExpression(ifStatement.condition, child,sourceFile);
			child.addExpression(conditionExpression);
			parent.addElement(child);
			processStatement(ifStatement.ifClause, child, sourceFile);
			if (ifStatement.elseClause != null)
				processStatement(ifStatement.elseClause, child, sourceFile);
		}

		else if (statement instanceof WhileStatementTree) {
			WhileStatementTree whileStatement = statement.asWhileStatement();
			CompositeStatement child = new CompositeStatement(whileStatement, StatementType.WHILE, parent,sourceFile);
			AbstractExpression conditionExpression = new AbstractExpression(whileStatement.condition, child, sourceFile);
			child.addExpression(conditionExpression);
			parent.addElement(child);
			processStatement(whileStatement.body, child, sourceFile);
		}

		else if (statement instanceof DoWhileStatementTree) {
			DoWhileStatementTree doWhileStatement = statement.asDoWhileStatement();
			CompositeStatement child = new CompositeStatement(doWhileStatement, StatementType.DO_WHILE, parent,sourceFile);
			AbstractExpression conditionExpression = new AbstractExpression(doWhileStatement.condition, child, sourceFile);
			child.addExpression(conditionExpression);
			parent.addElement(child);
			processStatement(doWhileStatement.body, child, sourceFile);
		}

		else if (statement instanceof ForInStatementTree) {
			ForInStatementTree forInStatement = statement.asForInStatement();
			CompositeStatement child = new CompositeStatement(forInStatement, StatementType.FOR_IN, parent,sourceFile);

			AbstractExpression initializerExpression = new AbstractExpression(forInStatement.initializer, child, sourceFile);
			child.addExpression(initializerExpression);

			AbstractExpression collectionExpression = new AbstractExpression(forInStatement.collection, child, sourceFile);
			child.addExpression(collectionExpression);

			parent.addElement(child);
			processStatement(forInStatement.body, child, sourceFile);
		}

		else if (statement instanceof ForStatementTree) {
			ForStatementTree forStatement = statement.asForStatement();
			CompositeStatement child = new CompositeStatement(forStatement, StatementType.FOR, parent,sourceFile);

			if (forStatement.initializer != null) {
				AbstractExpression initializerExpression = new AbstractExpression(forStatement.initializer, child, sourceFile);
				child.addExpression(initializerExpression);
			}
			if (forStatement.condition != null) {
				AbstractExpression conditionExpression = new AbstractExpression(forStatement.condition, child, sourceFile);
				child.addExpression(conditionExpression);
			}
			if (forStatement.increment != null) {
				AbstractExpression incrementExpression = new AbstractExpression(forStatement.increment, child, sourceFile);
				child.addExpression(incrementExpression);
			}
			parent.addElement(child);
			processStatement(forStatement.body, child, sourceFile);
		}

		else if (statement instanceof ForOfStatementTree) {
			ForOfStatementTree forOfStatement = statement.asForOfStatement();
			CompositeStatement child = new CompositeStatement(forOfStatement, StatementType.FOR_OF, parent,sourceFile);

			AbstractExpression initializerExpression = new AbstractExpression(forOfStatement.initializer, child, sourceFile);
			child.addExpression(initializerExpression);

			AbstractExpression collectionExpression = new AbstractExpression(forOfStatement.collection, child, sourceFile);
			child.addExpression(collectionExpression);

			parent.addElement(child);
			processStatement(forOfStatement.body, child, sourceFile);
		}

		else if (statement instanceof WithStatementTree) {
			WithStatementTree withStatement = statement.asWithStatement();
			CompositeStatement child = new CompositeStatement(withStatement, StatementType.WITH, parent,sourceFile);

			AbstractExpression expression = new AbstractExpression(withStatement.expression, child, sourceFile);
			child.addExpression(expression);

			parent.addElement(child);
			processStatement(withStatement.body, child, sourceFile);
		}

		else if (statement instanceof SwitchStatementTree) {
			SwitchStatementTree switchStatement = statement.asSwitchStatement();
			CompositeStatement child = new CompositeStatement(switchStatement, StatementType.SWITCH, parent,sourceFile);

			AbstractExpression expression = new AbstractExpression(switchStatement.expression, child, sourceFile);
			child.addExpression(expression);

			parent.addElement(child);

			for (ParseTree caseClause : switchStatement.caseClauses) {
				processStatement(caseClause, child, sourceFile);
			}
		}

		else if (statement instanceof CaseClauseTree) {
			CaseClauseTree caseClause = statement.asCaseClause();
			CompositeStatement child = new CompositeStatement(caseClause, StatementType.CASE_CLAUSE, parent,sourceFile);

			AbstractExpression expression = new AbstractExpression(caseClause.expression, child, sourceFile);
			child.addExpression(expression);

			parent.addElement(child);

			for (ParseTree caseClauseStatement : caseClause.statements) {
				processStatement(caseClauseStatement, child, sourceFile);
			}
		}

		else if (statement instanceof DefaultClauseTree) {
			DefaultClauseTree defaultClause = statement.asDefaultClause();
			CompositeStatement child = new CompositeStatement(defaultClause, StatementType.DEFAULT_CLAUSE, parent,sourceFile);

			parent.addElement(child);

			for (ParseTree defaultClauseStatement : defaultClause.statements) {
				processStatement(defaultClauseStatement, child, sourceFile);
			}
		}

		else if (statement instanceof TryStatementTree) {
			TryStatementTree tryStatement = statement.asTryStatement();
			TryStatement child = new TryStatement(tryStatement, parent,sourceFile);
			parent.addElement(child);

			processStatement(tryStatement.body, child, sourceFile);

			if (tryStatement.catchBlock != null) {
				CatchTree catchBlock = tryStatement.catchBlock.asCatch();

				CompositeStatement catchClause = new CompositeStatement(catchBlock, StatementType.CATCH, null,sourceFile);

				AbstractExpression expression = new AbstractExpression(catchBlock.exception, catchClause, sourceFile);
				catchClause.addExpression(expression);

				processStatement(catchBlock.catchBody, catchClause,sourceFile);

				child.setCatchClause(catchClause);
			}

			if (tryStatement.finallyBlock != null) {
				FinallyTree finallyBlock = tryStatement.finallyBlock.asFinally();

				CompositeStatement finallyClause = new CompositeStatement(finallyBlock, StatementType.BLOCK, null,sourceFile);

				processStatement(finallyBlock.block, finallyClause,sourceFile);

				child.setFinallyBlock(finallyClause);
			}
		}

		else if (statement instanceof LabelledStatementTree) {
			LabelledStatementTree labelledStatement = statement.asLabelledStatement();
			LabelledStatement child = new LabelledStatement(labelledStatement, parent,sourceFile);
			child.setLabel(labelledStatement.name.value);
			parent.addElement(child);
			processStatement(labelledStatement.statement, child, sourceFile);
		}

		else if (statement instanceof VariableStatementTree) {
			VariableStatementTree variableStatement = statement.asVariableStatement();
			VariableDeclarationListTree listTree = variableStatement.declarations;
			List<AbstractExpression> declarationExpressions = new ArrayList<>();
			//List<ObjectLiteralExpression> objectLiteralExpressions = new ArrayList<>();
			for (VariableDeclarationTree variableDeclarationTree : listTree.declarations) {
				AbstractExpression expression = getFunctionDeclarationExpression(variableDeclarationTree, parent, sourceFile );
				if (expression != null)
					declarationExpressions.add(expression);
			}
			Statement child = new Statement(variableStatement, StatementType.VARIABLE, parent, sourceFile);
			for (AbstractExpression expression : declarationExpressions) {
				if (expression instanceof FunctionDeclarationExpression) {
					child.addFunctionDeclarationExpression((FunctionDeclarationExpression) expression);
					
				}
				if (expression instanceof ObjectLiteralExpression)
					child.addObjectLiteralExpression((ObjectLiteralExpression) expression);
			}
			parent.addElement(child);
		}

		else if (statement instanceof EmptyStatementTree) {
			EmptyStatementTree emptyStatement = statement.asEmptyStatement();
			Statement child = new Statement(emptyStatement, StatementType.EMPTY, parent, sourceFile);
			parent.addElement(child);
		}

		else if (statement instanceof ExpressionStatementTree) {
			ExpressionStatementTree expressionStatement = statement.asExpressionStatement();
			FunctionDeclarationExpression functionDeclarationExpression = null;
			ParseTree parentOperand = expressionStatement.expression;
			ObjectLiteralExpression objectLiteralExpression = null;
			if (parentOperand instanceof BinaryOperatorTree) {
				BinaryOperatorTree binaryOperatorTree = parentOperand.asBinaryOperator();
				ParseTree operand = binaryOperatorTree.right;
				while (operand instanceof BinaryOperatorTree && operand.asBinaryOperator().right instanceof BinaryOperatorTree) {
					operand = operand.asBinaryOperator().right.asBinaryOperator();
				}
				if (operand instanceof BinaryOperatorTree)
					operand = operand.asBinaryOperator().right;
				parentOperand = operand;
				if (operand instanceof FunctionDeclarationTree) {
					FunctionDeclarationTree functionDeclarationTree = operand.asFunctionDeclaration();
					functionDeclarationExpression = new FunctionDeclarationExpression(functionDeclarationTree, FunctionDeclarationExpressionNature.BINARY_OPERATION, binaryOperatorTree.left, parent, sourceFile);
				//	System.err.println("Nome da expressao: "+((FunctionDeclarationExpression) functionDeclarationExpression).getQualifiedName());

				} else if (operand instanceof ObjectLiteralExpressionTree) {
					ObjectLiteralExpressionTree objectLiteralExpressionTree = operand.asObjectLiteralExpression();
					objectLiteralExpression = new ObjectLiteralExpression(objectLiteralExpressionTree, parent,sourceFile);
				} else if (operand instanceof CallExpressionTree) {
					// IIFE is in a function and left identifier followed by
					// equal operator followed by function declaration
					// expression
					CallExpressionTree callExpressionTree = operand.asCallExpression();
					if (callExpressionTree.operand instanceof ParenExpressionTree) {
						ParenExpressionTree parenExpressionTree = callExpressionTree.operand.asParenExpression();
						if (parenExpressionTree.expression instanceof FunctionDeclarationTree) {
							String firstParamName = extractFirstParam(callExpressionTree);
							FunctionDeclarationTree functionDeclarationTree = parenExpressionTree.expression.asFunctionDeclaration();
							functionDeclarationExpression = new FunctionDeclarationExpression(functionDeclarationTree, FunctionDeclarationExpressionNature.IIFE, parent, sourceFile);
							if(firstParamName!=null){
								functionDeclarationExpression.setIIFEParam(firstParamName);
							}
						}
					}
				} else if (operand instanceof ArrayLiteralExpressionTree) {
					ArrayLiteralExpressionTree arrayLiteralExpression = operand.asArrayLiteralExpression();
					for (ParseTree node : arrayLiteralExpression.elements) {
						if (node instanceof FunctionDeclarationTree) {
							FunctionDeclarationTree functionDeclarationTree = node.asFunctionDeclaration();
							functionDeclarationExpression = new FunctionDeclarationExpression(functionDeclarationTree, FunctionDeclarationExpressionNature.IIFE, parent, sourceFile);
						}
					}
				}
			}
			if (parentOperand instanceof CallExpressionTree) {
				// if IIFE is in the root without any birnary operation.
				// (function(){
				// console.log('root IIFE');
				// })();
				CallExpressionTree callExpressionTree = parentOperand.asCallExpression();
				if (callExpressionTree.operand instanceof ParenExpressionTree) {
					ParenExpressionTree parenExpressionTree = callExpressionTree.operand.asParenExpression();
					if (parenExpressionTree.expression instanceof FunctionDeclarationTree) {
						FunctionDeclarationTree functionDeclarationTree = parenExpressionTree.expression.asFunctionDeclaration();
						String firstParamName = extractFirstParam(callExpressionTree);
						functionDeclarationExpression = new FunctionDeclarationExpression(functionDeclarationTree, FunctionDeclarationExpressionNature.IIFE, parent, sourceFile);
						if(firstParamName!=null){
							functionDeclarationExpression.setIIFEParam(firstParamName);
						}
					}
				} else if (callExpressionTree.operand instanceof MemberExpressionTree) {
					MemberExpressionTree memberExpressionTree = callExpressionTree.operand.asMemberExpression();
					if (memberExpressionTree.operand instanceof ParenExpressionTree) {
						ParenExpressionTree parenExpressionTree = memberExpressionTree.operand.asParenExpression();
						if (parenExpressionTree.expression instanceof FunctionDeclarationTree) {
							FunctionDeclarationTree functionDeclarationTree = parenExpressionTree.expression.asFunctionDeclaration();
							String firstParamName = extractFirstParam(callExpressionTree);
							functionDeclarationExpression = new FunctionDeclarationExpression(functionDeclarationTree, FunctionDeclarationExpressionNature.IIFE, parent, sourceFile);
							if(firstParamName!=null){
								functionDeclarationExpression.setIIFEParam(firstParamName);
							}
						}
					}
				}
				for (ParseTree node : callExpressionTree.arguments.arguments) {
					if (node instanceof FunctionDeclarationTree) {
						FunctionDeclarationTree functionDeclarationTree = node.asFunctionDeclaration();
						functionDeclarationExpression = new FunctionDeclarationExpression(functionDeclarationTree, FunctionDeclarationExpressionNature.PARAMETER, parent,sourceFile);
					}
				}
			} else if (parentOperand instanceof ParenExpressionTree) {
				ParenExpressionTree parenExpression = parentOperand.asParenExpression();
				if (parenExpression.expression instanceof CallExpressionTree) {
					CallExpressionTree callExpression = parenExpression.expression.asCallExpression();
					if (callExpression.operand instanceof FunctionDeclarationTree) {
						FunctionDeclarationTree functionDeclarationTree = callExpression.operand.asFunctionDeclaration();
						String firstParamName = extractFirstParam(callExpression);
						functionDeclarationExpression = new FunctionDeclarationExpression(functionDeclarationTree, FunctionDeclarationExpressionNature.IIFE, parent, sourceFile);
						if(firstParamName!=null){
							functionDeclarationExpression.setIIFEParam(firstParamName);
						}
					}
					for (ParseTree argument : callExpression.arguments.arguments) {
						if (argument instanceof FunctionDeclarationTree) {
							FunctionDeclarationTree functionDeclarationTree = argument.asFunctionDeclaration();
							// not sure String firstParamName = extractFirstParam(callExpression);
							functionDeclarationExpression = new FunctionDeclarationExpression(functionDeclarationTree, FunctionDeclarationExpressionNature.IIFE, parent, sourceFile);
						}
					}
				} else if (parenExpression.expression instanceof FunctionDeclarationTree) {
					FunctionDeclarationTree functionDeclarationTree = parenExpression.expression.asFunctionDeclaration();
					functionDeclarationExpression = new FunctionDeclarationExpression(functionDeclarationTree, FunctionDeclarationExpressionNature.IIFE, parent, sourceFile);
				}
			}
			Statement child = new Statement(expressionStatement, StatementType.EXPRESSION, parent,sourceFile);
			if (functionDeclarationExpression != null)
				child.addFunctionDeclarationExpression(functionDeclarationExpression);
			if (objectLiteralExpression != null)
				child.addObjectLiteralExpression(objectLiteralExpression);
			parent.addElement(child);
		}

		else if (statement instanceof BreakStatementTree) {
			BreakStatementTree breakStatement = statement.asBreakStatement();

			Statement child = new Statement(breakStatement, StatementType.BREAK, parent,sourceFile);

			parent.addElement(child);
		}

		else if (statement instanceof ContinueStatementTree) {
			ContinueStatementTree continueStatement = statement.asContinueStatement();

			Statement child = new Statement(continueStatement, StatementType.CONTINUE, parent,sourceFile);

			parent.addElement(child);
		}

		else if (statement instanceof ReturnStatementTree) {
			ReturnStatementTree returnStatement = statement.asReturnStatement();
			Statement child = new Statement(returnStatement, StatementType.RETURN, parent,sourceFile);
			if (returnStatement.expression instanceof ObjectLiteralExpressionTree) {
				AbstractExpression expression = new ObjectLiteralExpression(returnStatement.expression.asObjectLiteralExpression(), parent,sourceFile);
				child.addObjectLiteralExpression((ObjectLiteralExpression) expression);
			}
			if (returnStatement.expression instanceof FunctionDeclarationTree) {
				AbstractExpression expression = new FunctionDeclarationExpression(returnStatement.expression.asFunctionDeclaration(), FunctionDeclarationExpressionNature.RETURN, parent,sourceFile);
				child.addFunctionDeclarationExpression((FunctionDeclarationExpression) expression);
			}
			parent.addElement(child);
		}

		else if (statement instanceof ThrowStatementTree) {
			ThrowStatementTree thowStatement = statement.asThrowStatement();

			Statement child = new Statement(thowStatement, StatementType.THROW, parent,sourceFile);

			parent.addElement(child);
		}

		else if (statement instanceof DebuggerStatementTree) {
			DebuggerStatementTree debuggerStatement = statement.asDebuggerStatement();

			Statement child = new Statement(debuggerStatement, StatementType.DEBUGGER, parent,sourceFile);

			parent.addElement(child);
		}

	}

	private static String extractFirstParam(CallExpressionTree callExpressionTree) {
		String superTypeNme=null;
		if(callExpressionTree.arguments.arguments.size()>0){
			ParseTree arg0=callExpressionTree.arguments.arguments.get(0);
			if(arg0 instanceof IdentifierExpressionTree){
				 superTypeNme=arg0.asIdentifierExpression().identifierToken.value;
			}else if(arg0 instanceof MemberExpressionTree){
				AbstractIdentifier abstractIdentifier=IdentifierHelper.getIdentifier(arg0);
				if(abstractIdentifier instanceof CompositeIdentifier){
					superTypeNme=((CompositeIdentifier)abstractIdentifier).toString();
				}else{
					superTypeNme=((PlainIdentifier) abstractIdentifier).getIdentifierName();
				}
			}
		}
		return superTypeNme;
	}

	public static AbstractExpression getFunctionDeclarationExpression(VariableDeclarationTree variableDeclarationTree, SourceContainer parent, SourceFile sourceFile) throws IOException {
		if (variableDeclarationTree.initializer instanceof FunctionDeclarationTree) {
			FunctionDeclarationTree functionDeclarationTree = variableDeclarationTree.initializer.asFunctionDeclaration();
			return new FunctionDeclarationExpression(functionDeclarationTree, FunctionDeclarationExpressionNature.VARIABLE_DECLARATION, variableDeclarationTree.lvalue, parent,sourceFile);
		} else if (variableDeclarationTree.initializer instanceof ObjectLiteralExpressionTree) {
			ObjectLiteralExpressionTree objectLiteralExpressionTree = variableDeclarationTree.initializer.asObjectLiteralExpression();
			return new ObjectLiteralExpression(objectLiteralExpressionTree, parent,sourceFile);
		}
		// var foo=new function() {...}
		else if (variableDeclarationTree.initializer instanceof NewExpressionTree) {
			NewExpressionTree newExpressionTree = variableDeclarationTree.initializer.asNewExpression();
			if (newExpressionTree.operand instanceof FunctionDeclarationTree) {
				FunctionDeclarationExpression functionDeclarationExpression = new FunctionDeclarationExpression(newExpressionTree.operand.asFunctionDeclaration(), FunctionDeclarationExpressionNature.NEW_FUNCTION, variableDeclarationTree.lvalue, parent,sourceFile);
				return functionDeclarationExpression;
			}
		}
		// Handling IIFEs: var someObj = (function () {...})();
		else if (variableDeclarationTree.initializer instanceof CallExpressionTree) {
			CallExpressionTree callExpressionTree = variableDeclarationTree.initializer.asCallExpression();
			if (callExpressionTree.operand instanceof ParenExpressionTree)
				if (callExpressionTree.operand.asParenExpression().expression instanceof FunctionDeclarationTree) {
					String firstParamName = extractFirstParam(callExpressionTree);
					FunctionDeclarationExpression functionDeclarationExpression = new FunctionDeclarationExpression(callExpressionTree.operand.asParenExpression().expression.asFunctionDeclaration(), FunctionDeclarationExpressionNature.IIFE, variableDeclarationTree.lvalue, parent,sourceFile);
					if(firstParamName!=null)
						functionDeclarationExpression.setIIFEParam(firstParamName);
					return functionDeclarationExpression;
				}
		} else if (variableDeclarationTree.initializer instanceof ParenExpressionTree) {
			ParenExpressionTree parenExpression = variableDeclarationTree.initializer.asParenExpression();
			if (parenExpression.expression instanceof CallExpressionTree) {
				CallExpressionTree callExpressionTree = parenExpression.expression.asCallExpression();
				if (callExpressionTree.operand instanceof FunctionDeclarationTree) {
					String firstParamName = extractFirstParam(callExpressionTree);
					FunctionDeclarationExpression functionDeclarationExpression = new FunctionDeclarationExpression(callExpressionTree.operand.asFunctionDeclaration(), FunctionDeclarationExpressionNature.IIFE, variableDeclarationTree.lvalue, parent,sourceFile);
					if(firstParamName!=null)
						functionDeclarationExpression.setIIFEParam(firstParamName);
					return functionDeclarationExpression;
				}
			}
		}
		return null;
	}
}