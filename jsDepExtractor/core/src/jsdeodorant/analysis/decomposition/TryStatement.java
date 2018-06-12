package jsdeodorant.analysis.decomposition;

import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;

import jsdeodorant.analysis.abstraction.SourceContainer;

public class TryStatement extends CompositeStatement {

	private CompositeStatement catchClause;
	private CompositeStatement finallyBlock;

	public TryStatement(ParseTree statement, SourceContainer parent) {
		super(statement, StatementType.TRY, parent);
	}

	public void setCatchClause(CompositeStatement catchClause) {
		this.catchClause = catchClause;
	}

	public void setFinallyBlock(CompositeStatement finallyBlock) {
		this.finallyBlock = finallyBlock;
	}

	public CompositeStatement getCatchClause() {
		return catchClause;
	}

	public CompositeStatement getFinallyBlock() {
		return finallyBlock;
	}

}
