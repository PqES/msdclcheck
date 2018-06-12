package jsDepExtractor.dependencies;

public class CreateFieldDependency extends CreateDependency{
	private String field;
	
	public CreateFieldDependency(String module1, String module2, String field) {
		super(module1, module2);
		this.field = field;
	}
	public String toString() {
		return "'" + 
				this.getModule1() + "' contains the field '" + this.field + 
				"' that receives an instantiation of an object of '" + this.getModule2() + "'";
	}
}
