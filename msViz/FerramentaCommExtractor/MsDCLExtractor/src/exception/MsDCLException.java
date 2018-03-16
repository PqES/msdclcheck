package exception;

import org.eclipse.jdt.core.ICompilationUnit;

public class MsDCLException extends Exception {
	private static final long serialVersionUID = 1L;
	private Exception e;
	private ICompilationUnit unit;
	private ICompilationUnit unitString;
	
	public MsDCLException(Exception e, ICompilationUnit unit) {
		this.e = e;
		this.unit = unit;
	}
	public MsDCLException(Exception e, String unitString) {
		this.e = e; 
		this.unit = unit; 
	}
	@Override
	public String toString() {
		return "MsDCLException: " + this.getLocalizedMessage() + "\n" + e.getLocalizedMessage(); 
	}
	
	@Override
	public String getLocalizedMessage() {
		return "Problem detected when visiting " + this.unit.toString();
	}
	
	
	@Override
	public StackTraceElement[] getStackTrace() {
		return this.e.getStackTrace();
	}
	
}
