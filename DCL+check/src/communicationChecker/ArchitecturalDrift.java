package communicationChecker;

import msdcl.core.ConstraintDefinition;
import msdcl.core.MicroserviceDefinition;
import msdcl.core.ModuleDefinition;

public abstract class ArchitecturalDrift {
	
	public static final String DIVERGENCE = "DIVERGENCE";
	public static final String ABSENCE = "ABSENCE";
	public static final String WARNING = "WARNING";	
	private final ConstraintDefinition violateConstraint;
	private MicroserviceDefinition microservice; 
	private ModuleDefinition module;
	
	protected ArchitecturalDrift(ConstraintDefinition violate){ 
		this.violateConstraint = violate;
	}
 
	protected ArchitecturalDrift(ConstraintDefinition violate, MicroserviceDefinition microservice){ 
		this.violateConstraint = violate;
		this.microservice = microservice;
	}
	protected ArchitecturalDrift(ConstraintDefinition violate, MicroserviceDefinition microservice, ModuleDefinition module){ 
		this.violateConstraint = violate;
		this.microservice = microservice;
		this.module = module;
	}
	protected ArchitecturalDrift(ConstraintDefinition violate, ModuleDefinition module){ 
		this.violateConstraint = violate;
		this.module = module;
	}
	public ConstraintDefinition getViolateConstraint() {
		return violateConstraint;
	} 
	public MicroserviceDefinition getMicroservice(MicroserviceDefinition ms) {
		return this.microservice;
	}
	public ModuleDefinition getModule() {
		return module;
	}

	public void setModule(ModuleDefinition module) {
		this.module = module;
	}

	public String getFileConstraintViolate() {
		return this.getViolateConstraint().getModule().getName();
	}
	public abstract String getMessage();

	public abstract String getViolationType();
	
	@Override
	public int hashCode(){
		return getViolationType().length();
	}
}
