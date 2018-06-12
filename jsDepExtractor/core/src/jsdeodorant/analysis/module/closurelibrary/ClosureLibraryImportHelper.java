package jsdeodorant.analysis.module.closurelibrary;

import java.util.List;
import java.util.Set;

import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;

import jsdeodorant.analysis.abstraction.AbstractIdentifier;
import jsdeodorant.analysis.abstraction.Export;
import jsdeodorant.analysis.abstraction.Module;
import jsdeodorant.analysis.decomposition.AbstractExpression;
import jsdeodorant.analysis.module.PackageImporter;
import jsdeodorant.analysis.util.IdentifierHelper;

public class ClosureLibraryImportHelper implements PackageImporter {
	private Module currentModule;
	private Set<Module> modules;

	public ClosureLibraryImportHelper(Module module, Set<Module> modules) {
		this.currentModule = module;
		this.modules = modules;
	}

	@Override
	public void extract(ParseTree expression) {
		if (expression instanceof ExpressionStatementTree) {
			System.out.println("eh expression!!");
			ExpressionStatementTree expressionStatement = expression.asExpressionStatement();
			if (expressionStatement.expression instanceof CallExpressionTree) {
				CallExpressionTree callExpression = expressionStatement.expression.asCallExpression();
				AbstractIdentifier operandIdentifier = IdentifierHelper.getIdentifier(callExpression.operand);
				if (operandIdentifier.toString().equals("goog.require")) {
					AbstractIdentifier importName = IdentifierHelper.getIdentifier(callExpression.arguments.arguments.get(0));
					for (Module module : modules) {
						if (module.equals(currentModule))
							continue;
						for (Export export : module.getExports()) {
							if (importName.toString().replace("'", "").equals(export.getName()))
								this.currentModule.addDependency(importName.toString().replace("'", ""), module, new AbstractExpression(expressionStatement.expression));
						}
					}
				}
			}
		}

	}
}
