package msdcl.communicationChecker;

import msdcl.core.ConstraintDefinition;

public class ClassifiedCommunicate {
	public boolean canCommunicate;
	public ConstraintDefinition associatedConstraint;
	
	public ClassifiedCommunicate(boolean canCommunicate, ConstraintDefinition associatedConstraint){
		this.canCommunicate = canCommunicate;
		this.associatedConstraint = associatedConstraint;  
	}  
}
