package communicationAnalyser;

import communicationExtractor.CommunicateDefinition;
import entities.ConstraintDefinition;

public class ClassifiedCommunicate extends CommunicateDefinition{

	public static final String OK = "OK";
	public static final String VIOLATION = "VIOLATION";
	public static final String ABSENCE = "ABSENCE";
	
	private String classification;
	private ConstraintDefinition associatedConstraint;
	
	public ClassifiedCommunicate(CommunicateDefinition communicate, String classification,
			ConstraintDefinition associatedConstraint) {
		super(communicate.getCalle(), communicate.getCaller());
		this.classification = classification;
		this.associatedConstraint = associatedConstraint;
	}
	
	public String getClassification(){
		return this.classification;
	}
	
	public ConstraintDefinition getAssociatedConstraint(){
		return this.associatedConstraint;
	}

}
