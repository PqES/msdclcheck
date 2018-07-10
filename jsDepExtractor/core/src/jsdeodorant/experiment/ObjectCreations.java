package jsdeodorant.experiment;

import java.util.HashSet;
import java.util.Set;

import jsdeodorant.analysis.AnalysisResult;
import jsdeodorant.analysis.abstraction.Module;
import jsdeodorant.analysis.abstraction.ObjectCreation;
import jsdeodorant.analysis.decomposition.FunctionDeclaration;

public class ObjectCreations {
	private Module currentModule;
	private static ObjectCreations instance;
	
	public ObjectCreations() {}
	public static ObjectCreations getInstance() {
		if (instance == null) {
			instance = new ObjectCreations();
		}
		return instance;
	}

	public ObjectCreations(Module module) {
		this.currentModule = module;
	}
	public Set<ObjectCreation> getObjectCreations(String path) {
		boolean classExists = false;
		// System.out.println("List Size: " +
		// currentModule.getProgram().getObjectCreationList().size());
		Set<ObjectCreation> objectCreations = new HashSet<>();

		for (ObjectCreation creation : currentModule.getProgram().getObjectCreationList()) {
			
			// System.out.println(creation);
			if(!creation.getOperandOfNewName().equals("require")) {
				objectCreations.add(creation);
				System.out.println("Create pre-definido:  " + creation.getOperandOfNewName() + "  "
					+ creation.getModuleDeclarationLocation() + "  " + creation.isClassDeclarationPredefined() + "  "
					+ creation.getClassDeclarationKind() + "  " + creation.getArguments().size() + "  "
					+ currentModule.getCanonicalPath());
			}
		}
		return objectCreations;
	}
}
