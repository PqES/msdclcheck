package dependencies;

import enums.DependencyType;

public class AnnotationDependency extends Dependency{

	public AnnotationDependency(String nameClass1, String nameClass2, Integer position, Integer offset,
			Integer length) {
		super(nameClass1, nameClass2, position, offset, length);
	}

	public AnnotationDependency( String nameClass2, Integer position, Integer offset,
			Integer length) {
		super(nameClass2, position, offset, length);
	} 
	@Override
	public DependencyType getDependencyType() {
		return DependencyType.USEANNOTATION;
	}

	@Override
	public String toString() {
		
		return "'"+ this.getNameClass1() + "   is annotaded by " + this.getNameClass2() +"'";
	}

}
