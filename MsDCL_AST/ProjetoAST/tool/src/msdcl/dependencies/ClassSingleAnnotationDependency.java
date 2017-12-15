package msdcl.dependencies;

import java.util.ArrayList;
import java.util.List;

public class ClassSingleAnnotationDependency extends ClassAnnotationDependency {

	private List<String> values;
	private String expression;
	public ClassSingleAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, List<String> values) {
		super(nameClass1, nameClass2, position, offset, length);
		this.values = new ArrayList<>();
	}

	public ClassSingleAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length,String expression) {
		super(nameClass1, nameClass2, position, offset, length);
		this.expression = expression;
	}
	
	public List<String> getValues() {
		return values;
	}
	
	public String toString() {
		return "'" + this.getNameClass1() + "   is annotated by " + this.getNameClass2()+
				
				" by expression " + this.expression + "'";
	}

}
