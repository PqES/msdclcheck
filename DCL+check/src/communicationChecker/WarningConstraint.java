package communicationChecker;

import enums.ViolationType;
import msdcl.core.CommunicateDefinition;
import msdcl.core.ConstraintDefinition;
import msdcl.core.ModuleDefinition;

public class WarningConstraint extends ArchitecturalDrift{

	private ModuleDefinition module;
	public WarningConstraint(ModuleDefinition module) {
		super(null);
		this.module = module;
	}

	@Override
	public String getViolationType() {
		return ArchitecturalDrift.WARNING;
	}

	public String getModuleDestin() {
		return module.getName();
	}

	@Override
	public String getMessage() {
		return "Warning: The "+module.getName() + " was not specified in the Architecture";
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof WarningConstraint){
			WarningConstraint warning = (WarningConstraint) obj;
			return this.module.equals(warning.module); 
		}
		return false;
	}

}
