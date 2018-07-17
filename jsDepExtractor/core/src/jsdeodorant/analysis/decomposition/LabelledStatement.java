package jsdeodorant.analysis.decomposition;

import java.io.IOException;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;

import jsdeodorant.analysis.abstraction.SourceContainer;

public class LabelledStatement extends CompositeStatement {

	private String label;

	public LabelledStatement(ParseTree statement, SourceContainer parent, SourceFile sourceFile) throws IOException {
		super(statement, StatementType.LABELLED, parent,sourceFile);

	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
