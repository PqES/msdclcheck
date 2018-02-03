package msdcl.dependencies;

public class ClassAnnotationDependency extends AnnotationDependency{


	
	public ClassAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length) {
		super(nameClass1, nameClass2, position, offset, length);
	}
//	public ClassAnnotationDependency( String nameClass2, Integer position, Integer offset,
//			Integer length) {
//		super( nameClass2, position, offset, length);
//	}
	public String toStrint() {
		return "'" + this.getNameClass1() + "   is annotated by " + this.getNameClass2()+"'";
	}
}
