package msdcl.dependencies;

import java.util.HashSet;
import java.util.Set;

public class MethodNormalAnnotationDependency extends MethodAnnotationDependency{
	private Set<MemberPair> membersValues = new HashSet<>();
	public MethodNormalAnnotationDependency(String nameClass2, Integer position, Integer offset, Integer length,
			String methodName,Set<MemberPair> membersValues) {
		super(nameClass2, position, offset, length, methodName);
		this.membersValues = membersValues;
	}

	public String toString() {
		String output =  "'" + this.getNameClass1() + "  contains the method " + this.getMethodName()
			+"  that is annotated by class " + this.getNameClass2()+"'";
		for (MemberPair m : this.membersValues) { 
			 output +=" with name" + m.getName() + " and value: " + m.getValue() + ";";
		}
		return output;
	}
	public Set<MemberPair> getMembersValues() {
		return membersValues;
	}

}
