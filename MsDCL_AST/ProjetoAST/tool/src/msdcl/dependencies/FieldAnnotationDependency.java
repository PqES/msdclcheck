package msdcl.dependencies;

public class FieldAnnotationDependency  extends AnnotationDependency{
	
	protected String nameField ; 
	protected String declaration;
	public FieldAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length, String nameField, String declaration) {
		super(nameClass1, nameClass2, position, offset, length);
		this.nameField = nameField;
		this.declaration = declaration;
	}
//	public FieldAnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
//			Integer length, String nameField) {
//		super(nameClass1, nameClass2, position, offset, length);
//		this.nameField = nameField;
//		
//	}
	
	
	public String getNameField() {
		return nameField;
	}
	
	public String getDeclaration() {
		return declaration;
	}
	
	
	public String toString() {
		return "'"+this.getNameClass1() + "  contains the field " + this.nameField 
				+ " of type" + this.declaration + "  that is annotated by " 
				+ this.getNameClass2() + "'";
	}
	
}
