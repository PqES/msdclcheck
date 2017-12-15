package msdcl.dependencies;

import java.util.ArrayList;
import java.util.List;

public class FieldNormalAnnotationDependency extends FieldAnnotationDependency {

	private List<String> values;
	private String expression;

	public FieldNormalAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, String nameField, String declaration, List<String> values) {
		super(nameClass1, nameClass2, position, offset, length, nameField, declaration);
		this.values = new ArrayList<String>();
	}

	public FieldNormalAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, String nameField, String declaration, String expression) {
		super(nameClass1, nameClass2, position, offset, length, nameField, declaration);
		this.expression = expression;
	}

	public List<String> getValues() {
		return values;
	}

//	public String toString() {
//		return "'" + this.getNameClass1() + "  contains the field " + this.nameField + " of type" + this.declaration
//				+ "  that is annotated by " + this.getNameClass2() + "'" + " with by values " + this.values + "'";
//	}
	public String toString() {
		return "'" + this.getNameClass1() + "  contains the field " + this.nameField + " of type" + this.declaration
				+ "  that is annotated by " + this.getNameClass2() + "'" + " with by expression  " + this.expression + "'";
	}
	
}
