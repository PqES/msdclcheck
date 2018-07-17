package jsdeodorant.experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jsDepExtractor.dependencies.AnalyseAccessDependency;
import jsdeodorant.analysis.abstraction.FunctionInvocation;
import jsdeodorant.analysis.abstraction.Module;
import jsdeodorant.analysis.util.FileUtil;
import jsdeodorant.analysis.util.IdentifierHelper;

public class FunctionInvocations {
	private Module currentModule;
	private Map<String,Set<String>> functionsInvocationsList;
	private static FunctionInvocations instance;
	
	public FunctionInvocations() {}
	public static FunctionInvocations getInstance() {
		if (instance == null) {
			instance = new FunctionInvocations();
		}
		return instance;
	}
	public FunctionInvocations(Module module) {
		this.currentModule = module;
	}
	public Set<String>  getfunctionInvocations(String path) {
		Set<String> functionsInvocationsList = new HashSet<>();
		if (currentModule.getProgram().getFunctionInvocationList().size() == 0)
			return functionsInvocationsList ;
		
		
		Set<String>filefunctionsInvocations = new HashSet<>();
		for (FunctionInvocation functionInvocation : currentModule.getProgram().getFunctionInvocationList()) {
			
			
			if(functionInvocation.isPredefined()) {
				filefunctionsInvocations.add("js"+IdentifierHelper.getIdentifier(functionInvocation.
						getCallExpressionTree()).toString());
			}
			else {
				filefunctionsInvocations.add(IdentifierHelper.getIdentifier(functionInvocation.
						getCallExpressionTree()).toString());
			}
		}
		//functionsInvocationsList.put(path, filefunctionsInvocations);
		return filefunctionsInvocations;
	}
	private String getFileName() {
		String[] filePart = currentModule.getSourceFile().getOriginalPath().split("/");
		String fileName = FileUtil.getElementsOf(filePart, filePart.length - 2, filePart.length - 1).replace("/", ".");
		if (fileName.lastIndexOf('|') == fileName.length() - 1)
			fileName = fileName.substring(0, fileName.length() - 1);
		return fileName;
	}
	public Map<String, Set<String>> getFunctionsInvocationsList() {
		return functionsInvocationsList;
	}
	public void setFunctionsInvocationsList(Map<String, Set<String>> functionsInvocationsList) {
		this.functionsInvocationsList = functionsInvocationsList;
	}

}
