package msdcl.dependencies;

import java.util.HashSet;
import java.util.Set;

public class FieldNormalAnnotationDependency extends FieldAnnotationDependency {
	private Set<MemberPair> membersValues = new HashSet<>();
	private String value;
	public FieldNormalAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, String nameField, String declaration, Set<MemberPair> membersValues) {
		super(nameClass1, nameClass2, position, offset, length, nameField, declaration);
		this.membersValues =membersValues ;
	}

	public FieldNormalAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, String nameField, String declaration, String value) {
		super(nameClass1, nameClass2, position, offset, length, nameField, declaration);
		this.value = value;
	}
	
	public String toString() {
		String output = this.getNameClass1() + "  contains the field " + this.nameField + " of type " + this.declaration
				+ "  that is annotated by " + this.getNameClass2()  + "'" + " with value: " + getValue() + ";";
//		for (MemberPair m : this.membersValues) { 
//			 output +=" with value: " + getValue() + ";";
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
