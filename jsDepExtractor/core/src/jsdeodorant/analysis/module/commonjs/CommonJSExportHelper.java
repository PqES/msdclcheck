package jsdeodorant.analysis.module.commonjs;

import java.util.Set;

//import org.apache.log4j.Logger;

import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;

import jsdeodorant.analysis.abstraction.AbstractIdentifier;
import jsdeodorant.analysis.abstraction.Export;
import jsdeodorant.analysis.abstraction.Module;
import jsdeodorant.analysis.abstraction.PlainIdentifier;
import jsdeodorant.analysis.decomposition.AbstractExpression;
import jsdeodorant.analysis.module.PackageExporter;
import jsdeodorant.analysis.util.IdentifierHelper;

public class CommonJSExportHelper implements PackageExporter {
//	static Logger log = Logger.getLogger(CommonJSExportHelper.class.getName());
	private Module currentModule;
	private Set<Module> modules;

	public CommonJSExportHelper(Module module, Set<Module> modules) {
		this.currentModule = module;
		this.modules = modules;
	}

	public void extract(ParseTree expression) {
		if (expression instanceof ExpressionStatementTree) {
			ExpressionStatementTree expressionStatement = expression.asExpressionStatement();
			if (expressionStatement.expression instanceof BinaryOperatorTree)
				if (checkIfLValueIsRequireStatement(expressionStatement.expression.asBinaryOperator())) {
					//matchModules();
					return;
				}
		}
	}

	private boolean checkIfLValueIsRequireStatement(BinaryOperatorTree binaryOperator) {
		AbstractIdentifier lValueIdentifier = IdentifierHelper.getIdentifier(binaryOperator.left);
		if (lValueIdentifier instanceof PlainIdentifier)
			return false;
		if (lValueIdentifier.toString().contains("module.exports") || lValueIdentifier.asCompositeIdentifier().getLeftPart().toString().contains("exports")) {
			PlainIdentifier exportIdentifier = lValueIdentifier.asCompositeIdentifier().getMostRightPart();
			Export export = new Export(exportIdentifier.getIdentifierName().replace("'", ""), new AbstractExpression(binaryOperator.right));
			currentModule.addExport(export);
			return true;
		}
		return false;
	}
}
