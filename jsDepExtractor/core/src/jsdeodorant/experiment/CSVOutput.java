package jsdeodorant.experiment;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jsdeodorant.analysis.AnalysisResult;
import jsdeodorant.analysis.abstraction.FunctionInvocation;
import jsdeodorant.analysis.abstraction.Module;
import jsdeodorant.analysis.abstraction.ObjectCreation;
import jsdeodorant.analysis.decomposition.FunctionDeclaration;
import jsdeodorant.analysis.module.LibraryType;
import jsdeodorant.analysis.util.FileUtil;
import jsdeodorant.analysis.util.IdentifierHelper;

public class CSVOutput {
	// static Logger log = Logger.getLogger(CSVOutput.class.getName());
	private Module currentModule;
	private CSVFileWriter csvWriter;

	public CSVOutput() {
	}

	public CSVOutput(Module module) {
		this.currentModule = module;
	}

	public void functionSignatures() {
		if (currentModule.getProgram().getFunctionDeclarationList().size() == 0)
			return;
		System.out.println("MEU FILE NAME JHOW");
		System.out.println(getFileName());
		String new_file_name = getFileName();// .replace("TP12.", "TP12/");
		new_file_name = new_file_name.replace(".js.", "") + "-declarations.csv";
		csvWriter = new CSVFileWriter("log/functions/" + new_file_name);
		String fileHeader = "File path, function name, FunctionType, Library Type,Declaration Location, Number of Params,Parameter Names, Number of Return Statements";
		csvWriter.writeToFile(fileHeader.split(","));
		for (FunctionDeclaration functionDeclaration : currentModule.getProgram().getFunctionDeclarationList()) {
			StringBuilder lineToWrite = new StringBuilder();
			int parameterSize = functionDeclaration.getParameters().size();
			String parametersName = LogUtil.getParametersName(functionDeclaration.getParameters());

			lineToWrite.append(currentModule.getSourceFile().getName()).append(",")
					.append(functionDeclaration.getName().replace(",", "-")).append(",")
					.append(functionDeclaration.getKind()).append(",").append(currentModule.getLibraryType())
					.append(",")
					.append(functionDeclaration.getFunctionDeclarationTree().location.toString().replace(",", "-"))
					.append(",").append(parameterSize).append(",").append(parametersName).append(",")
					.append(functionDeclaration.getReturnStatementList().size());
			csvWriter.writeToFile(lineToWrite.toString().split(","));
		}
	}

	public void uniqueClassDeclaration() {
		String new_file_name = getFileName().replace(".", ".").replace(".js", "") + ".csv";
		String currentFilePath = "log/legacy/classes/" + new_file_name;
		csvWriter = new CSVFileWriter(currentFilePath);
		String fileHeader = "Object Creation Name, Class FQN, Is Declaration Predefined?, DeclarationType, Number of Arguments, Number of Parameters, Parameter Names, Invocation Location, Declaration Location, Is invocation in a library?, Is definition in a library?";
		csvWriter.writeToFile(fileHeader.split(","));
		Set<FunctionDeclaration> classes = new HashSet<>();
		boolean classExists = false;
		for (ObjectCreation creation : currentModule.getProgram().getObjectCreationList()) {
			if (creation.isClassDeclarationPredefined()) {
				writeClassDeclarationToFile(creation);
				// log.info(creation.getOperandOfNewName() + " " +
				// creation.getClassDeclarationLocation() + " And the invocation is at: " +
				// creation.getObjectCreationLocation());
				classExists = true;
			}
			if (creation.getClassDeclaration() != null) {
				if (!classes.contains(creation.getClassDeclaration())) {
					classes.add(creation.getClassDeclaration().getFunctionDeclaration());
					writeClassDeclarationToFile(creation);
					// log.info(creation.getOperandOfNewName() + " " +
					// creation.getClassDeclaration().getFunctionDeclarationTree().location + " And
					// the invocation is at: " + creation.getNewExpressionTree().location);
				}
				classExists = true;
			}
		}
		for (FunctionDeclaration functionDeclaration : currentModule.getProgram().getFunctionDeclarationList()) {
			if (functionDeclaration.isTypeDeclaration()) {
				// log.info("Classname is:" + functionDeclaration.getIdentifier().toString() + "
				// " + functionDeclaration.getFunctionDeclarationTree().location);
				classes.add(functionDeclaration);
				classExists = true;
			}
		}
		if (!classExists) {
			new File(currentFilePath).delete();
			return;
		} else
			AnalysisResult.increaseTotalNumberOfFiles();

		if (classes.size() > 0) {
			// log.info("Number of unique classes in this file:" + classes.size());
			AnalysisResult.increaseTotalNumberOfClasses(classes.size());
		}
	}

	public void aggregateReportForModule(Set<Module> modules) {
		String currentFilePath = "log/aggregate/modules.csv";
		csvWriter = new CSVFileWriter(currentFilePath);
		String fileHeader = "Module name, Library Type, Number of dependencies, Number of exports";
		csvWriter.writeToFile(fileHeader.split(","));
		for (Module module : modules) {
			StringBuilder lineToWrite = new StringBuilder();
			lineToWrite.append(module.getSourceFile().getName()).
			append(",").
			append(module.getLibraryType()).
			append(",").
			append(module.getDependencies().size()).
			append(",").
			append(module.getExports().size());
			csvWriter.writeToFile(lineToWrite.toString().split(","));
		}
	}

	public void functionInvocations() {
		if (currentModule.getProgram().getFunctionInvocationList().size() == 0)
			return;
		String new_file_name = getFileName().replace(".", ".").replace(".js", "") + "-invocations.csv";
		csvWriter = new CSVFileWriter("log/functions/" + new_file_name);
		System.out.println("Escrita função ");
		String fileHeader = "File path, function name, Library Type, Number of Params,Parameter Names, Invocation Location, Declaration Location, isItPredefined";
		csvWriter.writeToFile(fileHeader.split(","));
		for (FunctionInvocation functionInvocation : currentModule.getProgram().getFunctionInvocationList()) {
			StringBuilder lineToWrite = new StringBuilder();
			int parameterSize = functionInvocation.getArguments().size();
			String parametersName = LogUtil.getParametersName(functionInvocation.getArguments());
			lineToWrite.append(currentModule.getSourceFile().getName()).append(",")
					.append(IdentifierHelper.getIdentifier(functionInvocation.getCallExpressionTree()).toString()
							.replace(",", "-"))
					.append(",").append(currentModule.getLibraryType()).append(",").append(parameterSize).append(",")
					.append(parametersName).append(",")
					.append(functionInvocation.getCallExpressionTree().location.toString().replace(",", "-"))
					.append(",");
			if (functionInvocation.isPredefined())
				lineToWrite.append(functionInvocation.getPredefinedName()).append(",").append("True");
			else if (functionInvocation.getFunctionDeclaration() != null)
				lineToWrite.append(functionInvocation.getFunctionDeclaration().getFunctionDeclarationTree().location
						.toString().replace(",", "-")).append(",").append("False");
			else
				lineToWrite.append(",").append("False");
			csvWriter.writeToFile(lineToWrite.toString().split(","));
		}

	}

	private void writeClassDeclarationToFile(ObjectCreation objectCreation) {
		StringBuilder lineToWrite = new StringBuilder();
		int parameterSize;
		String parametersName;
		boolean isDefinitionInLibrary = false;
		if (objectCreation.isClassDeclarationPredefined()) {
			parameterSize = objectCreation.getArguments().size();
			parametersName = "";
			isDefinitionInLibrary = false;
		} else {
			parameterSize = objectCreation.getClassDeclaration().getFunctionDeclaration().getParameters().size();
			parametersName = LogUtil
					.getParametersName(objectCreation.getClassDeclaration().getFunctionDeclaration().getParameters());
			isDefinitionInLibrary = objectCreation.getClassDeclarationModule().getLibraryType() != LibraryType.NONE;
		}
		lineToWrite.append(objectCreation.getOperandOfNewName().replace(",", "-")).append(",")
				.append(objectCreation.getClassDeclarationQualifiedName()).append(",")
				.append(objectCreation.isClassDeclarationPredefined() ? "TRUE" : "FALSE").append(",")
				.append(objectCreation.getClassDeclarationKind()).append(",")
				.append(objectCreation.getArguments().size()).append(",").append(parameterSize).append(",")
				.append(parametersName).append(",").append(currentModule.getCanonicalPath()).
				// append(currentModule.getCanonicalPath() + "/" +
				// objectCreation.getObjectCreationLocation().replace(",", "-")).
				append(",").append(objectCreation.getClassDeclarationLocation().replace(",", "-")).append(",")
				.append(currentModule.getLibraryType()).append(",").append(isDefinitionInLibrary);
		csvWriter.writeToFile(lineToWrite.toString().split(","));
	}

	public static boolean createAndClearFolder(String folderName) {
		File directory = new File(folderName);
		if (!directory.exists()) {
			directory.mkdirs();
			return true;
		}
		// If folder already exists, remove all files
		for (File file : directory.listFiles())
			file.delete();
		return true;
	}

	private String getFileName() {
		String[] filePart = currentModule.getSourceFile().getOriginalPath().split("/");
		String fileName = FileUtil.getElementsOf(filePart, filePart.length - 2, filePart.length - 1).replace("/", ".");
		if (fileName.lastIndexOf('|') == fileName.length() - 1)
			fileName = fileName.substring(0, fileName.length() - 1);
		return fileName;
	}

	public void moduleReport(Set<Module> modules) {
//		String currentFilePath = "log/classes/" + getFileName() + ".csv";
//		csvWriter = new CSVFileWriter(currentFilePath);
//		String fileHeader = "Class name, File, Is Declaration Predefined?, Location, Has Infered? , Constructor Lines of Code, Class Lines of Codes, Number of methods, Number of attributes, Number of Parameters, Is definition in a library?, Number of instantiation, Is aliased?, Has Namespace?";
//		csvWriter.writeToFile(fileHeader.split(","));
//		Set<FunctionDeclaration> classes = new HashSet<>();
//		boolean classExists = false;
//		for (ObjectCreation creation : currentModule.getProgram().getObjectCreationList()) {
//			if (creation.isClassDeclarationPredefined()) {
//				writeClassDeclarationToFile(creation);
//				// log.info(creation.getOperandOfNewName() + " " +
//				// creation.getClassDeclarationLocation() + " And the invocation is at: " +
//				// creation.getObjectCreationLocation());
//				classExists = true;
//			}
		}
//	}
}
