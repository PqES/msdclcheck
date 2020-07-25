package org.ufla.dcc.jsdepextractor;

import java.io.IOException;
import org.ufla.dcc.jsdepextractor.project.JsFile;
import com.google.javascript.jscomp.parsing.parser.Parser;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import com.google.javascript.jscomp.parsing.parser.util.ErrorReporter;
import com.google.javascript.jscomp.parsing.parser.util.SourcePosition;

public class ScriptParser {

  // public Compiler compiler;

  private ErrorReporter errorReporter;

  private Parser.Config config;

  private static ScriptParser instance;

  public static ScriptParser getInstance() {
    if (instance == null) {
      instance = new ScriptParser();
    }
    return instance;
  }

  private ScriptParser() {
    this.errorReporter = new CLIErrorReporter();
    this.config = new Parser.Config(Parser.Config.Mode.ES6_OR_ES7, true);
    // this.compiler = new Compiler();
    // CompilerOptions compilerOptions = new CompilerOptions();
    // compilerOptions.setCodingConvention(new ClosureCodingConvention());
    // compilerOptions.skipAllCompilerPasses();
    // compilerOptions.setLanguage(LanguageMode.ECMASCRIPT6_TYPED);
    // compilerOptions.setEnvironment(Environment.CUSTOM);
    // compilerOptions.setGenerateExports(true);
    // CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(compilerOptions);
    // WarningLevel.QUIET.setOptionsForWarningLevel(compilerOptions);
  }

  class CLIErrorReporter extends ErrorReporter {

    @Override
    protected void reportError(SourcePosition location, String message) {
      System.err.println(location.toString() + " - " + message);
    }

    @Override
    protected void reportWarning(SourcePosition location, String message) {
      System.err.println(location.toString() + " - " + message);
    }

  }

  public ProgramTree parse(JsFile jsFile) {
    try {
      Parser parser = new Parser(config, errorReporter, jsFile.createSourceFile());

      return parser.parseProgram();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("ERRO! Não foi possível ler o arquivo Javascript " + jsFile.getFullPath());
    }
    return null;
  }



}
