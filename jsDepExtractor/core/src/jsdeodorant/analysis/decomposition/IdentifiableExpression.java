package jsdeodorant.analysis.decomposition;

import jsdeodorant.analysis.abstraction.AbstractIdentifier;

public interface IdentifiableExpression extends Identifiable {
	
	public void setPublicIdentifier(AbstractIdentifier alias);

	public AbstractIdentifier getPublicIdentifier();
}
