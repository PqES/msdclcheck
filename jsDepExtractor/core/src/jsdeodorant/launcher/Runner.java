package jsdeodorant.launcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//import org.apache.log4j.Level;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CommandLineRunner;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;

import jsdeodorant.analysis.AnalysisEngine;
import jsdeodorant.analysis.AnalysisOptions;
import jsdeodorant.analysis.ExtendedCompiler;
import jsdeodorant.analysis.abstraction.Module;
import jsdeodorant.analysis.util.FileUtil;

public abstract class Runner extends CommandLineRunner {
//	protected static Logger log = Logger.getLogger(AnalysisEngine.class.getName());
	protected CompilerOptions compilerOptions;
	private static AnalysisOptions analysisOptions;
	public ImmutableList.Builder<SourceFile> inputs = ImmutableList.builder();
	public ImmutableList.Builder<SourceFile> externs = ImmutableList.builder();

	public static final Function<String, SourceFile> TO_SOURCE_FILE_FN = new Function<String, SourceFile>() {
		@Override
		public SourceFile apply(String file) {
			return new SourceFile.Builder().buildFromFile(file);
		}
	};

	protected Runner(String[] args) {
		super(args);
	}

	@Override
	public CompilerOptions createOptions() {
		compilerOptions = super.createOptions();
		return compilerOptions;
	}

	@Override
	protected Compiler createCompiler() {
		Compiler compiler = super.createCompiler();
		return compiler;
	}

	public void addExternsFromFile(List<String> externs) {
		this.externs.addAll(Lists.transform(externs, TO_SOURCE_FILE_FN));
	}

	public void addInputsFromFile(List<String> inputs) {
		this.inputs.addAll(Lists.transform(inputs, TO_SOURCE_FILE_FN));
	}

	public ExtendedCompiler createExtendedCompiler() {
		return new ExtendedCompiler(getErrorPrintStream());
	}

	public Set<Module> performActions() throws IOException {
		System.out.println("entrou performance!!");
		inputs = ImmutableList.builder();
		externs = ImmutableList.builder();
		//if (analysisOptions.isLogDisabled())
			//LogManager.getLoggerRepository().setThreshold(Level.OFF);
		List<String> jsFiles = analysisOptions.getJsFiles();
		System.out.println("Tamanho do JsFiles: " + jsFiles.size());
		if (jsFiles != null ) {
			for(String s: jsFiles) {
				if(!s.contains("node_modules"))
					addInputsFromFile(jsFiles);
			}
		}
		addInputsFromFile(extractFilesFromLibraries(analysisOptions.getBuiltInLibraries()));
		List<String> optionsExterns = analysisOptions.getExterns();
		if (optionsExterns != null) {
			System.out.println("Arquivos externos!");
			addExternsFromFile(optionsExterns);
			
		}
		AnalysisEngine analysisEngine = new AnalysisEngine(createExtendedCompiler(), createOptions(), inputs.build(), externs.build());
//		log.debug("analysis starts");
		Set<Module> results = analysisEngine.run(analysisOptions);
		System.out.println("Results: ");

		for (Module m : results) {
			System.out.println(m.getDependencies());
		}
//		log.debug("analysis ends");
		return results;
	}

	private List<String> extractFilesFromLibraries(List<String> librariesWithPath) {
		List<String> allFiles = new ArrayList<>();
		if (librariesWithPath == null)
			return allFiles;
		for (String directoryPath : librariesWithPath) {
			try {
				allFiles.addAll(FileUtil.getFilesInDirectory(directoryPath, "js"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return allFiles;
	}

	public abstract AnalysisOptions createAnalysisOptions();

	public static AnalysisOptions getAnalysisOptions() {
		return analysisOptions;
	}

	public static void setAnalysisOptions(AnalysisOptions analysisOptions) {
		Runner.analysisOptions = analysisOptions;
	}

}
