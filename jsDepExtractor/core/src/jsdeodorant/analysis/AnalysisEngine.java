package jsdeodorant.analysis;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//import org.apache.//log.j.//log.er;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.WarningLevel;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;

import jsDepExtractor.Util;
import jsDepExtractor.dependencies.AnalyseAccessDependency;
import jsDepExtractor.dependencies.AnalyseCreateDependency;
import jsdeodorant.analysis.abstraction.Module;
import jsdeodorant.analysis.abstraction.ObjectCreation;
import jsdeodorant.analysis.abstraction.Program;
import jsdeodorant.analysis.abstraction.StatementProcessor;
import jsdeodorant.analysis.decomposition.Method;
import jsdeodorant.analysis.decomposition.TypeDeclaration;
import jsdeodorant.analysis.decomposition.TypeDeclarationKind;
import jsdeodorant.analysis.decomposition.TypeMember;
import jsdeodorant.analysis.module.LibraryType;
import jsdeodorant.analysis.util.FileUtil;
import jsdeodorant.experiment.CSVOutput;
import jsdeodorant.experiment.ClassAnalysisReport;
import jsdeodorant.experiment.FunctionInvocations;
import jsdeodorant.experiment.ObjectCreations;

public class AnalysisEngine {
	// static //log.er //log.= //log.er.get//log.er(AnalysisEngine.class.getName());
	private final ExtendedCompiler compiler;
	private final CompilerOptions compilerOptions;
	private ImmutableList<SourceFile> inputs;
	private ImmutableList<SourceFile> externs;
	// private PostgresOutput psqlOutput;

	public AnalysisEngine(ExtendedCompiler compiler, CompilerOptions compilerOptions) {
		this(compiler, compilerOptions, null, null);
	}

	public AnalysisEngine(ExtendedCompiler compiler, CompilerOptions compilerOptions, ImmutableList<SourceFile> inputs,
			ImmutableList<SourceFile> externs) {
		this.compiler = compiler;
		this.compilerOptions = compilerOptions;
		this.compilerOptions.setIdeMode(false);
		this.compilerOptions.skipAllCompilerPasses();
		// this.compilerOptions.setParseJsDocDocumentation(false);
		CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(this.compilerOptions);
		this.compilerOptions.setNewTypeInference(false);
		WarningLevel warningLevel = WarningLevel.QUIET;
		warningLevel.setOptionsForWarningLevel(this.compilerOptions);
		this.inputs = inputs;
		this.externs = externs;
	}

	public Set<Module> run(AnalysisOptions analysisOption, String path) throws IOException {
		compiler.compile(externs, inputs, compilerOptions);
		ScriptParser scriptAnalyzer = new ScriptParser(compiler);
		JSproject jsProjectInstance = JSproject.getInstance(true);
//		if (analysisOption.isOutputToCSV())
//		//	prepareOutputToCSV();
		int functionCounts = 0;

		analysisOption.notifyAnalysisObservers(AnalysisStep.BUILDING_MODEL);
		for (SourceFile sourceFile : inputs) {
			Program program = new Program();

			ProgramTree programTree = scriptAnalyzer.parse(sourceFile);
			for (ParseTree sourceElement : programTree.sourceElements) {
			//	System.out.println("sourceFile: " + sourceFile.getName());
				StatementProcessor.processStatement(sourceElement, program, sourceFile);
				
			}

			jsProjectInstance.createModule(program, sourceFile, scriptAnalyzer.getMessages(),
					analysisOption.getPackageSystem(), analysisOption.hasModuleAnalysis());
		}

		Set<Module> modules = jsProjectInstance.getModules();
		for (Module module : modules) {
			markBuiltinLibraries(module, analysisOption);
		}

		InheritanceInferenceEngine inheritanceInferenceEngine = new InheritanceInferenceEngine();

		inheritanceInferenceEngine.configure(analysisOption.getPackageSystem());

		analysisOption.notifyAnalysisObservers(AnalysisStep.IDENTIFYING_CLASSES);
		Set<String>predefinedClassJs = new HashSet<>();
		for (Module module : modules) {
			if (module.getLibraryType() != LibraryType.BUILT_IN) {
				checkForBeingLibrary(module, analysisOption);
				addBuiltinDependencies(module, analysisOption, modules);
			}
			if (analysisOption.hasModuleAnalysis())
				jsProjectInstance.processModules(module, analysisOption.getPackageSystem(), false);

			if (analysisOption.hasClassAnalysis()) {
				if (analysisOption.analyzeLibrariesForClasses() && module.getLibraryType() == LibraryType.NONE) {
					CompositePostProcessor.processFunctionDeclarationsToFindClasses(module,
							analysisOption.getClassAnalysisMode());
				}
				inheritanceInferenceEngine.run(module);
			}

			if (analysisOption.hasFunctionAnalysis())
				if (module.getLibraryType() == LibraryType.NONE)
					CompositePostProcessor.processFunctionInvocations(module);
		}

		analysisOption.notifyAnalysisObservers(AnalysisStep.BUILDING_CLASS_HIERARCHIES);
		// the inheritance analysis needs to be finished then we find method and
		// attributes
		inheritanceInferenceEngine.buildInheritenceRelation(analysisOption.getPackageSystem());
		for (Module module : modules) {
			module.identifyConstructorInTypeDeclarationBody(); // this is when the class contains a constructor too
			if (analysisOption.getClassAnalysisMode() == ClassAnalysisMode.NON_STRICT)
				ClassInferenceEngine.analyzeMethodsAndAttributes(module);
			else {
				for (TypeDeclaration aType : module.getTypes()) {
					aType.identifyAttributes();
					aType.identifyMethodsWithinClassBody();
					aType.identifyMethodsAddedToClassPrototype();
				}
			}
		}
		// log.debug("DONE identifying methods and attributes for classes/interfaces");
		// when all methods of classes are identified we will find abstract and
		// overriden and overriding methods
		// log.debug("START identifying abstartc, overriden and overriding methods");
		for (Module module : modules) {
			if (analysisOption.getClassAnalysisMode() == ClassAnalysisMode.STRICT) {
				for (TypeDeclaration aType : module.getTypes()) {
					aType.identifyInheritanceRelatedMethods();
				}
				for (TypeDeclaration aType : module.getTypes()) {
					if (module.isInterface(aType)) {
						if (aType.getKinds() != null) {
							aType.getKinds().add(TypeDeclarationKind.INTERFACE);
						} else {
							EnumSet<TypeDeclarationKind> kinds = EnumSet.of(TypeDeclarationKind.INTERFACE);
							aType.setKinds(kinds);
						}
					} else {
						if (module.isAbstractClass(aType)) {
							if (aType.getKinds() != null) {
								aType.getKinds().add(TypeDeclarationKind.ABSTRACT_CLASS);
							} else {
								EnumSet<TypeDeclarationKind> kinds = EnumSet.of(TypeDeclarationKind.ABSTRACT_CLASS);
								aType.setKinds(kinds);
							}
						} else { // not an abstract class
							if (aType.getKinds() != null) {
								aType.getKinds().add(TypeDeclarationKind.CLASS);
							} else {
								EnumSet<TypeDeclarationKind> kinds = EnumSet.of(TypeDeclarationKind.CLASS);
								aType.setKinds(kinds);
							}
						}
					}
				}
			}
		}

		for (Module module : modules) {
			
			if (analysisOption.isOutputToCSV()) {
				CSVOutput csvOutput = new CSVOutput(module);
				FunctionInvocations functions = new FunctionInvocations(module);
				ObjectCreations objects = new ObjectCreations(module);
				
				Set<ObjectCreation> objectCreations = objects.getObjectCreations(module.getCanonicalPath());
				// Set<String> vars = Util.getVarsRequire(module.getCanonicalPath());
				System.out.println();
			//	csvOutput.functionSignatures();
			//	csvOutput.functionInvocations();
			//	csvOutput.uniqueClassDeclaration();
			//	csvOutput.getAllObjectCreateFromModule();

				// functions.getfunctionInvocations();

				Map<String, String> varsLocalDeclaration = Util.proccesVarDependencyRequire(module.getCanonicalPath());
				Set<String> invocationsFunctions = functions.getfunctionInvocations(module.getCanonicalPath());
				AnalyseAccessDependency.getInstance().analyseAccessFromFiles(path, module.getCanonicalPath(),
						invocationsFunctions, varsLocalDeclaration);
				
				AnalyseCreateDependency.getInstance().analyseCreateFromFiles(module,path,varsLocalDeclaration,objectCreations);
			}

			AnalysisResult.addPackageInstance(module);
			functionCounts += module.getProgram().getFunctionDeclarationList().size();
		}

		for (Module module : modules) {
			for (TypeDeclaration classDeclaration : module.getTypes()) {
				int methodCount = 0;
				int attrCount = 0;
				for (TypeMember member : classDeclaration.getTypeMembers()) {
					if (member instanceof Method) {
						methodCount++;
					} else {
						attrCount++;
					}
				}
			}
		}
		if (analysisOption.isOutputToCSV()) {
			ClassAnalysisReport.updateReport(modules);
	//		System.out.println("chegou aqui para analisar!!");
			ClassAnalysisReport.writeToCSV();
			CSVOutput experimentOutput = new CSVOutput();
			// experimentOutput.aggregateReportForModule(modules);
			// experimentOutput.moduleReport(modules);
		}
		return modules;
	}

//	private void prepareOutputToCSV() {
//		CSVOutput.createAndClearFolder("log.functions");
//		CSVOutput.createAndClearFolder("log.legacy/classes");
//		CSVOutput.createAndClearFolder("log.classes");
//		CSVOutput.createAndClearFolder("log.aggregate");
//	}

	private void addBuiltinDependencies(Module module, AnalysisOptions analysisOption, Set<Module> modules) {
		for (Module lbModule : modules) {
			if (lbModule.getLibraryType() == LibraryType.BUILT_IN) {
				String[] path = lbModule.getCanonicalPath().split("/");
				module.addDependency(FileUtil.getElementsOf(path, path.length - 1, path.length - 1).replace(".js", ""),
						lbModule, null);
			}
		}
	}

	private void checkForBeingLibrary(Module module, AnalysisOptions analysisOption) {
		try {
			if (analysisOption.getLibraries() != null && analysisOption.getLibraries().size() > 0)
				for (String library : analysisOption.getLibraries())
					if (new File(module.getSourceFile().getOriginalPath()).getCanonicalPath()
							.contains(new File(library).getCanonicalPath())) {
						module.setAsLibrary(LibraryType.EXTERNAL_LIBRARY);
						return;
					}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void markBuiltinLibraries(Module module, AnalysisOptions analysisOption) {
		if (analysisOption.getBuiltInLibraries() != null && analysisOption.getBuiltInLibraries().size() > 0)
			for (String library : analysisOption.getBuiltInLibraries())
				try {
					if (new File(module.getSourceFile().getOriginalPath()).getCanonicalPath()
							.contains(new File(library).getCanonicalPath())) {
						module.setAsLibrary(LibraryType.BUILT_IN);
						return;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	private boolean containsError(SourceFile sourceFile, Result result) {
		for (JSError error : result.errors) {
			if (error.sourceName.equals(sourceFile.getOriginalPath()))
				return true;
		}
		return false;
	}

	public ImmutableList<SourceFile> getInputs() {
		return this.inputs;
	}

	public void setInputs(ImmutableList<SourceFile> inputs) {
		this.inputs = inputs;
	}

}
