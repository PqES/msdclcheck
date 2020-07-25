package msdcl.core;

import enums.Constraint;
import enums.ConstraintType;

public class ConstraintDefinition {

	private String microserviceOrigin;
	private ModuleDefinition commModule;
	private String filePathModule;
	private String microserviceDestin;
	private Constraint constraint;
	private String using;

	public ConstraintDefinition(String microserviceOrigin, ModuleDefinition commModule, String filePathModule,
			Constraint constraint, String microserviceDestin, String using) {
		this.microserviceOrigin = microserviceOrigin;
		this.commModule = commModule;
		this.filePathModule = filePathModule;
		this.microserviceDestin = microserviceDestin;
		this.constraint = constraint;
		this.using = using;
	}

	public ConstraintDefinition(String microserviceOrigin, ModuleDefinition commModule, String filePathModule,
			Constraint constraint, String microserviceDestin) {
		this.microserviceOrigin = microserviceOrigin;
		this.commModule = commModule;
		this.filePathModule = filePathModule;
		this.microserviceDestin = microserviceDestin;
		this.constraint = constraint;
		this.using = null;
	}
	// public ConstraintDefinition(String microserviceOrigin, String filePathModule,
	// Constraint constraint,
	// String microserviceDestin) {
	// this.microserviceOrigin = microserviceOrigin;
	// this.filePathModule = filePathModule;
	// this.microserviceDestin = microserviceDestin;
	// this.constraint = constraint;
	// this.using = null;
	// }

	public String getMicroserviceOrigin() {
		return this.microserviceOrigin;
	}

	public String getMicroserviceDestin() {
		return this.microserviceDestin;
	}

	public String getFilePathModule() {
		return this.filePathModule;
	}

	public Constraint getConstraint() {
		return this.constraint;
	}

	public String getUsing() {
		return this.using;
	}

	public boolean usingMatch(CommunicateDefinition communicate) {
		if (this.using != null && communicate.getUsing() != null) {
			String constraintRoute[] = this.using.split("/");
			String communicateRoute[] = communicate.getUsing().split("/");
			if (communicateRoute.length == constraintRoute.length) {
				if (constraintRoute.equals(communicateRoute))
					return true;
				else {
					for (int i = 0; i < constraintRoute.length; i++) {
						if (!constraintRoute[i].equals(communicateRoute[i]) && !constraintRoute[i].equals("*"))
							return false;
					}
					return true;
				}
			} else {
				if (communicateRoute.length > constraintRoute.length) {
					for (int i = 0; i < constraintRoute.length; i++) {
						if (!constraintRoute[i].equals(communicateRoute[i]) && !constraintRoute[i].equals("*"))
							return false;
					}
					return true;
				}
				return false;
			}
		}
		return this.using == null;
	}

	public boolean servicesMatch(CommunicateDefinition communicate) {
		if (this.commModule.getName().equalsIgnoreCase(communicate.getModule().getName())
				&& this.microserviceDestin.equalsIgnoreCase(communicate.getMicroserviceDestin()))
			return true;
		return false;
	}

	public boolean match(CommunicateDefinition communicate) {
		return servicesMatch(communicate) && usingMatch(communicate);
	}

	public Boolean canCommunicate(CommunicateDefinition communicate) {
		if (this.match(communicate)) {
			if (this.getConstraint().getConstraintType() == ConstraintType.CANNOT_COMMUNICATE) {
				return false;
			} else {
				return true;
			}
		} else if (this.commModule.getName().equalsIgnoreCase(communicate.getModule().getName())
				&& !this.microserviceDestin.equalsIgnoreCase(communicate.getMicroserviceDestin())
				&& this.getConstraint().getConstraintType() == ConstraintType.CAN_COMMUNICATE_ONLY) {
			return false;
		}
		return null;
	}

	public ModuleDefinition getModule() {
		return commModule;
	}

	@Override
	public String toString() {
		if (using != null) {

			return "[" + this.filePathModule + "(" + commModule.getName() + ") " + getConstraint().toString() + " "
					+ getMicroserviceDestin() + " using " + using + "]";

		}

		return "[" + filePathModule + "(" + commModule.getName() + ") " + getConstraint().toString() + " "
				+ getMicroserviceDestin();

	}
}