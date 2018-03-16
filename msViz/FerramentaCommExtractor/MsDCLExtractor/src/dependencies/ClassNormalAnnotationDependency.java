package dependencies;

import java.util.HashSet;
import java.util.Set;

public class ClassNormalAnnotationDependency extends ClassAnnotationDependency {
	private Set<MemberPair> membersValues = new HashSet<>();;
	private String value;
	public ClassNormalAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, Set<MemberPair> membersValues) {
		super(nameClass1, nameClass2, position, offset, length);
		this.membersValues = membersValues;
	}
	public ClassNormalAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, String value) {
		super(nameClass1, nameClass2, position, offset, length);
		this.value = value;
	}
	public Set<MemberPair> getMembersValues() {
		return membersValues;
	}

	public String toString() {
		String output = "'" + this.getNameClass1() + "   is annotated by " + this.getNameClass2() + " with expression "
				+ " with value: " + getValue() + ";";

//		for (MemberPair m : this.membersValues) {
//			output += " with value: " + getValue() + ";";
//		}
		return output;
	}
	public String getValue() {
		return this.value;
	}
}
