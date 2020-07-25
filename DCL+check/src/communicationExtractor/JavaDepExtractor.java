package communicationExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import msdcl.core.MicroserviceDefinition;
import msdcl.core.MicroservicesSystem;

public class JavaDepExtractor {

	private final static JavaDepExtractor instance = new JavaDepExtractor();

	public JavaDepExtractor() {

	}

	public static JavaDepExtractor getInstance() {
		return instance;
	}

	public void extractJavaDependenciesFromEachMicroservice(MicroserviceDefinition ms) throws IOException {
		Runtime.getRuntime().exec("java -jar javadepextractor.jar " + ms.getPath());

	}

	public String getDependenciesExtracted(MicroserviceDefinition microservice) throws IOException {
		if (microservice.getLanguage().equalsIgnoreCase("Java")) {
			File file = new File(microservice.getPath() + "/dependencies.txt");
			if (!file.exists()) {
				throw new IOException("Folder not exists!");
			}
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			StringBuilder content = new StringBuilder(); 
			while (br.ready()) { 
				content.append(br.readLine()).append('\n');
			}
			br.close();
			fr.close();
			return content.toString();
		} else if (microservice.getLanguage().equalsIgnoreCase("Js")) {
			File file = new File(microservice.getPath() + "/dependencies.txt");
			if (!file.exists()) {
				throw new IOException("Folder not exists!");
			}
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			StringBuilder content = new StringBuilder();
			while (br.ready()) {
				content.append(br.readLine()).append('\n');
			}
			br.close();
			fr.close();
			return content.toString();
		}
		return null;

	}

	public void getJavaDepExtractor(MicroservicesSystem system) throws IOException, InterruptedException {
		for (MicroserviceDefinition ms : system.getMicroservices()) {
			if (ms.getLanguage().equalsIgnoreCase("java")) {
				Process p = Runtime.getRuntime().exec("java -jar javadepextractor.jar " + ms.getPath());
				p.waitFor();
				int finalValueProccess = p.exitValue();
				System.out.println("Value final: " + finalValueProccess);
			}
		}

	}
}
