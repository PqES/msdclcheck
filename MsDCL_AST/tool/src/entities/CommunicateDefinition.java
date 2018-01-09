package entities;

public class CommunicateDefinition {

	private String microserviceOrigin;
	private String microserviceDestin;
	private String using;
	private String classe;

	public CommunicateDefinition(String microserviceOrigin, String microserviceDestin, String using) {
		this.microserviceOrigin = microserviceOrigin;
		this.microserviceDestin = microserviceDestin;
		this.using = using;
	}

	public CommunicateDefinition(String microserviceOrigin, String microserviceDestin, String using, String classe) {
		this.microserviceOrigin = microserviceOrigin;
		this.microserviceDestin = microserviceDestin;
		this.using = using;
		this.classe = classe;
	}

	public CommunicateDefinition(String microserviceOrigin, String microserviceDestin) {
		this(microserviceOrigin, microserviceDestin, null);
	}

	public String getMicroserviceOrigin() {
		return this.microserviceOrigin;
	}

	public String getMicroserviceDestin() {
		return this.microserviceDestin;
	}

	public String getUsing() {
		return this.using;
	}

	public String classe() {
		return this.classe;
	}

	@Override
	public int hashCode() {
		if (using != null) {
			return microserviceOrigin.length() + microserviceDestin.length() + using.length();
		}
		return microserviceOrigin.length() + microserviceDestin.length();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CommunicateDefinition) {
			CommunicateDefinition access = (CommunicateDefinition) obj;
			if(this.using != null) {
				return this.microserviceOrigin.equals(access.microserviceOrigin)
						&& this.microserviceDestin.equals(access.microserviceDestin)
						&& this.using.equals(access.using);	
			}else {
				return this.microserviceOrigin.equals(access.microserviceOrigin)
						&& this.microserviceDestin.equals(access.microserviceDestin);
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String s = microserviceOrigin + " communicate " + microserviceDestin;
		if (using != null) {
			s += " using " + this.using + "\n";
		}
		return s;
	}
}
