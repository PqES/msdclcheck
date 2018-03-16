package dependencies;

import java.util.HashSet;
import java.util.Set;

public class MethodNormalAnnotationDependency extends MethodAnnotationDependency{
	private Set<MemberPair> membersValues = new HashSet<>();
	private String value;
	public MethodNormalAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset, Integer length,
			String methodName,Set<MemberPair> membersValues) {
		super(nameClass1, nameClass2, position, offset, length, methodName);
		this.membersValues = membersValues;
	}

	public MethodNormalAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset, Integer length,
			String methodName,String value) {
		super(nameClass1, nameClass2, position, offset, length, methodName);
		this.value = value;
	}
	public String toString() {
		String output =  "'" + this.getNameClass1() + "  contains the method " + this.getMethodName()
			+"  that is annotated by class " + this.getNameClass2()+"'" +
			"with value: " + getValue() + ";";
//		for (MemberPair m : this.membersValues) { 
//			 output += " with value: " + m.getValue() + ";";
//		}
		return output;
	}
	public Set<MemberPair> getMembersValues() {
		return membersValues;
	}

	public String getValue() {
		return this.value;
	}
}
