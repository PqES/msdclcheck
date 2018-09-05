package org.ufla.dcc.jsdepextractor;

import java.util.Collection;
import org.ufla.dcc.jsdepextractor.dependency.ExtractDependencies;
import org.ufla.dcc.jsdepextractor.dependency.ReportDependencies;
import org.ufla.dcc.jsdepextractor.dependency.ReportDependenciesMultiFiles;
import org.ufla.dcc.jsdepextractor.project.ExtractJsCommonTypes;
import org.ufla.dcc.jsdepextractor.project.NodeJsProject;
import org.ufla.dcc.jsdepextractor.project.dependency.Dependency;
import org.ufla.dcc.jsdepextractor.typeinference.TypeInference;

public class CLIRunner {

  private static void showHelpMessage() {
    System.out.println("---------------------------------------------------------------------");
    System.out.println("Execute a aplicação com o comando abaixo:");
    System.out.println("$ java -jar jsdepextractor.jar <caminho-do-projeto> !<-io> !<-injs>\n");
    System.out
        .println("onde,\n<caminho-do-projeto> - caminho do projeto NodeJs que será analizado\n");
    System.out.println("!<-io> - tag utilizada para incluir dependências de Object no relatório\n");
    System.out.println(
        "!<-injs> - tag utilizada para incluir dependências de estruturas nativas em Javascript "
            + "(e.g., Math, String, Boolean) no relatório.\\n");
    System.out.println("---------------------------------------------------------------------");
    System.out.println(
        "Exemplo, desejo gerar relatório como todas estruturas nativas em Javascript, incluíndo "
            + "Object, portanto executo o comando:");
    System.out.println("$ java -jar jsdepextractor.jar <caminho-do-projeto> -io -injs\n");
    System.out.println("---------------------------------------------------------------------");
    System.exit(0);
  }

  private static final String INCLUDE_OBJECT_TAG = "-io";

  private static final String INCLUDE_NATIVE_JAVASCRIPT_TAG = "-injs";

  public static void showErrorInvalidTag(String invalidTag) {
    System.out.printf(
        "ERRO! A tag a seguir é inválida: <%s>.\nAs seguintes tags são válidas: <-io>, <-injs>.",
        invalidTag);
    showHelpMessage();
  }

  static boolean includeObject = false;
  static boolean includeNativeJavascript = false;

  private static void checkTag(String tag) {
    if (INCLUDE_OBJECT_TAG.equals(tag)) {
      includeObject = true;
    } else if (INCLUDE_NATIVE_JAVASCRIPT_TAG.equals(tag)) {
      includeNativeJavascript = true;
    } else {
      showErrorInvalidTag(tag);
    }
  }

  private static void checkTags(String[] args) {
    if (args.length >= 2) {
      checkTag(args[1]);
    }
    if (args.length > 2) {
      checkTag(args[2]);
    }
  }

  public static void main(String[] args) throws Exception {
//    args = new String[] {
//        "/home/carlos/workspaces/microservices/JsExtractor/example/app/"};
    if (args.length == 1 && (args[0].equals("-h") || args[0].equals("-help"))) {
      showHelpMessage();
    }
    if (args.length < 1 || args.length > 3) {
      System.out.println(
          "ERRO! Quantidade inválida de argumentos.\nA aplicação deve receber de um até três "
              + "argumentos, o primeiro é a localização do projeto NodeJs que "
              + "deverá extraír as dependências, este é obrigatório.");
      showHelpMessage();
    }
    checkTags(args);
    String projectFolder = args[0];
    ExtractJsCommonTypes extractJsCommonTypes = ExtractJsCommonTypes.getInstance();
    extractJsCommonTypes.getJsCommonTypes();
    NodeJsProject nodeJsProject = new NodeJsProject(projectFolder);
    System.out.println("Realizando inferência de tipos....");
    TypeInference typeInference = new TypeInference(nodeJsProject);
    typeInference.inferTypes();
    System.out.println("Inferência de tipos realizada!");
    System.out.println("------------------------------------------");
    System.out.println("Extraíndo dependências dos arquivos javascript do projeto '"
        + nodeJsProject.getPath() + "'...");
    ExtractDependencies extractDependencies =
        new ExtractDependencies(nodeJsProject, includeObject, includeNativeJavascript);
    Collection<Dependency> dependencies = extractDependencies.extractDependencies();
    ReportDependencies reportDependencies = new ReportDependencies(dependencies,
        nodeJsProject.getName(), includeObject, includeNativeJavascript);
    reportDependencies.writeReport();
    System.out.println("Dependências recuperadas.");
    System.out.println("------------------------------------------");
    System.out.println("Relatórios das dependências do projeto '" + nodeJsProject.getPath()
        + "' estão na pasta '" + ReportDependenciesMultiFiles.rootFolder.getAbsolutePath() + "'");

  }

}
