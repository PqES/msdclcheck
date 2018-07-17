package jsdeodorant.analysis.util;

import com.google.javascript.jscomp.parsing.parser.trees.ClassDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;

public class InstanceOfClass {

	public boolean instanceOfClass (ParseTree expression) {
		return expression instanceof ClassDeclarationTree;
		
	}
}
