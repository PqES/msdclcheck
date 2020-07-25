package communicationChecker;

import msdcl.core.CommunicateDefinition;
import msdcl.core.ConstraintDefinition;

public class ClassifiedCommunicate {
	public boolean canCommunicate;
	public ConstraintDefinition associatedConstraint;
	public CommunicateDefinition communicate;
	
	public ClassifiedCommunicate(boolean canCommunicate, ConstraintDefinition associatedConstraint, CommunicateDefinition communicate){
		this.canCommunicate = canCommunicate;
		this.associatedConstraint = associatedConstraint;  
		this.communicate = communicate;
	}  
}
