package jsdeodorant.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.google.common.base.Strings;

import jsdeodorant.analysis.AnalysisOptions;
import jsdeodorant.analysis.util.FileUtil;

public class CLIRunner extends Runner {
	private static CLIRunner runner;
	private static Flags flags;
	private static CmdLineParser parser;
	
	
	public CLIRunner() throws IOException {
		this(new String[0]);
	}

	protected CLIRunner(String[] args) throws IOException {
		super(args);
	}
	
	public CLIRunner getInstance() throws IOException {
		if(runner==null) {
			runner = new CLIRunner();
		}
		return runner;
	}
	

	public static void inicializa(String path) {
//		if (args.length != 1){
//			System.out.println("Usage:\n jsDepExtractor [folder-dir]");
//			return;
//		}
		try {
			//String path = args[0];
			// java -jar jsdepExtractor path
			
			String args2[] = new String[13];
			args2[0] = "-output-csv";
			args2[1] = "-class-analysis";
			args2[2] = "-module-analysis";
			args2[3] = "-package-system";
			//args2[4] = "CommonJS";
			args2[4] = "ClosureLibrary";
			args2[5] = "-analyze-lbClasses";
			args2[6] ="-builtin-libraries";
			args2[7] = "-externs";
			args2[8] = "-libraries";
			args2[9] = "-version";
			args2[10] = "-function-analysis";
			args2[11] = "-directory-path";
			args2[12] = path;
//			args2[13] = "-name";
//			args2[14] = "TP12";
			System.out.println("Path: " + path);
		//	Scanner reader =  new Scanner(System.in);
		//	reader.nextLine();
			CLIRunner.initializeCommandLine(args2);
			System.out.println("FLAGS?");
		//	reader.nextLine();
			if (flags.getHelp()) {
				parser.printUsage(System.err);
				return;
			}
			runner = new CLIRunner();
			System.out.println("CONFIGURE OPTIONS?");
		//	reader.nextLine();
			runner.configureOptions();
			System.out.println("PERFORM ACTIONS?");
			runner.performActions();
			//runToolForInnerModules(runner, flags.directoryPath() + "/node_modules");
		} catch (CmdLineException | IOException e) {
			parser.printUsage(System.err);
			//log.error(e.getMessage(), e);
		}
	}

	private static void runToolForInnerModules(CLIRunner runner, String nodeModuleFolder) throws IOException {
		//runner.inputs
		List<String> modules = FileUtil.getDirectoriesInDirectory(nodeModuleFolder);
		if (modules == null)
			return;
		for (final String innerModulePath : modules) {
			if (innerModulePath.contains(".bin"))
				continue;
			getAnalysisOptions().setDirectoryPath(innerModulePath);
			flags.clearJS();
			flags.setDirectoryPath(innerModulePath);
			flags.setLibraries(new ArrayList<String>() {
				{
					add(innerModulePath + File.separator + "node_modules");
					add(innerModulePath + File.separator + "lib");
				}
			});
			runner.configureOptions();
			runner.performActions();
			runToolForInnerModules(runner, innerModulePath + File.separator + "node_modules");
		}

	}

	private void configureOptions() throws IOException {
		compilerOptions = createOptions();
		setAnalysisOptions(createAnalysisOptions());
	}

	public static void initializeCommandLine(String[] args) throws CmdLineException, IOException {
		flags = new Flags();
		parser = flags.getParser();		
		flags.parse(args);
	}

	@Override
	public AnalysisOptions createAnalysisOptions() {
		try {
			setAnalysisOptions(new AnalysisOptions());
			getAnalysisOptions().setClassAnalysis(flags.classAnalysis());		
			Scanner reader = new Scanner(System.in); 		
	//		System.out.println("SET FUNCTIONALANYSIS?");
	//		reader.nextLine();
			getAnalysisOptions().setFunctionAnalysis(flags.functionAnalysis());
	//		System.out.println("SET MODULE ANALYSIS?");
			//reader.nextLine();
			getAnalysisOptions().setModuleAnlysis(flags.moduleAnalysis());
	//		System.out.println("SET OUTPUT TO CSV?");
		//	reader.nextLine();
			getAnalysisOptions().setOutputToCSV(flags.outputToCSV());
	//		System.out.println("OUTPUT TO CSV?");
		//	reader.nextLine();
////			System.out.println("output: "+flags.outputToCSV());
//			System.out.println("SET OUTPUT DB?");
		//	reader.nextLine();
			getAnalysisOptions().setOutputToDB(flags.outputToDB());
//			System.out.println("SET CALCULATE CYCLOMATIC?");
	//		reader.nextLine();
			getAnalysisOptions().setCalculateCyclomatic(flags.calculateCyclomatic());
//			System.out.println("SET LOG DISABLED?");
	//		reader.nextLine();
			getAnalysisOptions().setLogDisabled(flags.disableLog());
//			System.out.println("SET DIRECTORY FLAGS?");
		//.nextLine();
//			System.out.println(flags.directoryPath());
			if (!Strings.isNullOrEmpty(flags.directoryPath())) {
				getAnalysisOptions().setDirectoryPath(flags.directoryPath());
//				System.out.println("Flags do diretorio: "+flags.directoryPath());
			}
			System.out.println("SET JS FILES?");
	//		reader.nextLine();
		
			getAnalysisOptions().setJsFiles(flags.getJS());
//			System.out.println(flags.getJS());
			System.out.println("SET EXTERNS?");
	//		reader.nextLine();
			getAnalysisOptions().setExterns(flags.getExterns());
			System.out.println("SET LIBS?");
	//		reader.nextLine();
			getAnalysisOptions().setLibraries(flags.getLibraries());
			System.out.println("SET ANALYZED LIBRARIES FOR CLASSES?");
	//		reader.nextLine();
			getAnalysisOptions().setAnalyzeLibrariesForClasses(flags.analyzeLibraryClasses());
			System.out.println("SET BUILT IN LIBRARIES?");
	//		reader.nextLine();
			getAnalysisOptions().setBuiltinLibraries(flags.getBuiltinLibraries());
	//		System.out.println("SET PSQL SERVER NAME?");
	//		reader.nextLine();
			getAnalysisOptions().setPsqlServerName(flags.getPsqlServerName());
//			System.out.println("SET PSQL PORT NUMBER?");
	//		reader.nextLine();
			getAnalysisOptions().setPsqlPortNumber(flags.getPsqlPort());
	//		System.out.println("SET PSQL DATABASE NAME?");
	//		reader.nextLine();
			getAnalysisOptions().setPsqlDatabaseName(flags.getPsqlDbName());
	//		System.out.println("SET PSQL USER?");
	//		reader.nextLine();
			getAnalysisOptions().setPsqlUser(flags.getPsqlUser());
	//		System.out.println("SET PSQL PASSWORD?");
	//		reader.nextLine();
			getAnalysisOptions().setPsqlPassword(flags.getPsqlPassword());
		//	System.out.println("SET NAME?");
		//	reader.nextLine();
			getAnalysisOptions().setName(flags.getName());
//			reader.nextLine();
//			System.out.println("Next?");
//			System.out.println("name: " + flags.getName());
//			System.out.println("SET VERSION?");
//			reader.nextLine();
			getAnalysisOptions().setVersion(flags.getVersion());
//			System.out.println("SET PACKAGE SYSTEM?");
//			reader.nextLine();
			getAnalysisOptions().setPackageSystem(flags.getPackageSystem());
//			System.out.println("SET CLASS ANALYSIS MODE?");
		//	reader.nextLine();
			getAnalysisOptions().setClassAnalysisMode(flags.getClassAnalysisMode());
//			reader.nextLine();
//			System.out.println("Next?");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getAnalysisOptions();
	}
}
