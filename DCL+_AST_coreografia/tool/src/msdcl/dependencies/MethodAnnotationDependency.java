package msdcl.dependencies;

public class MethodAnnotationDependency extends AnnotationDependency {
	private String methodName;
	public MethodAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, String methodName) {
		super(nameClass1, nameClass2, position, offset, length);
		this.methodName = methodName; 
	}
	public MethodAnnotationDependency( String nameClass2, Integer position, Integer offset,
			Integer length, String methodName) {
		super( nameClass2, position, offset, length);
		this.methodName = methodName;
	}
	public String toString() {
		return "'" + this.getNameClass1() + "  contains the method " + this.getMethodName()
			+"  that is annotated by class " + this.getNameClass2()+"'";
	}
	
	public String getMethodName() {
		return this.methodName;
	}
	
}
