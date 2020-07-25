package msdcl.core;

public class CommunicateDefinition {

	private String microserviceOrigin;
	private String fileModuleOrigin;
	private String microserviceDestin;
	private String using;
	private ModuleDefinition module;


	public CommunicateDefinition(String microserviceOrigin,  ModuleDefinition moduleOrigin, String fileModuleOrigin, String microserviceDestin) {
		this.microserviceOrigin = microserviceOrigin;
		this.microserviceDestin = microserviceDestin;
		this.module = moduleOrigin;
		this.fileModuleOrigin = fileModuleOrigin;
	}
	public CommunicateDefinition(String microserviceOrigin, String microserviceDestin, ModuleDefinition moduleOrigin, String fileModuleOrigin, String using ) {
		this.microserviceOrigin = microserviceOrigin;
		this.microserviceDestin = microserviceDestin;
		this.module = moduleOrigin;
		this.fileModuleOrigin = fileModuleOrigin;
		this.using = using;
	}
	

	
	public CommunicateDefinition(String microserviceOrigin, String microserviceDestin, String fileModuleOrigin ) {
		this.microserviceOrigin = microserviceOrigin;
		this.microserviceDestin = microserviceDestin;
		this.fileModuleOrigin = fileModuleOrigin;
	}
	

//	public CommunicateDefinition(String microserviceOrigin, String microserviceDestin) {
//		this(microserviceOrigin, microserviceDestin, null);
//	}

	public String getMicroserviceOrigin() {
		return this.microserviceOrigin;
	}

	public String getMicroserviceDestin() {
		return this.microserviceDestin;
	}
	public ModuleDefinition getModule() {
		return this.module;
	}

	public String getUsing() {
		return this.using;
	}


	public String getFileModuleOrigin() {
		return this.fileModuleOrigin;
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
						&& this.getFileModuleOrigin().equals(access.fileModuleOrigin)
						&& this.microserviceDestin.equals(access.microserviceDestin)
						&& this.using.equals(access.using);	
			}else {
				return this.microserviceOrigin.equals(access.microserviceOrigin)
						&& this.microserviceDestin.equals(access.microserviceDestin)
						&& this.module.equals(access.module);
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String s = this.fileModuleOrigin + " ("+module.getName()+")" + " communicate " + microserviceDestin;
		if (using != null) {
			s += " using " + this.using;
		}
		
		return s;
	}

}

