package communicationAnalyser.drift;

import communicationExtractor.CommunicateDefinition;
import entities.ConstraintDefinition;

public class WarningConstraint extends ArchitecturalDrift{

	private CommunicateDefinition communicate;
	public WarningConstraint(CommunicateDefinition communicate) {
		super(null);
		this.communicate = communicate;
	}

	@Override
	public String getViolationType() {
		return ArchitecturalDrift.WARNING;
	}

	@Override
	public String getMessage() {
		return "Warning: no constraint for communication: "+communicate.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if(super.equals(obj) && obj instanceof WarningConstraint){
			WarningConstraint warning = (WarningConstraint) obj;
			return this.communicate.equals(warning.communicate);
		}
		return false;
	}

}
