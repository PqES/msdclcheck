package msdcl.communicationExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import msdcl.core.MicroserviceDefinition;

public class JavaDepExtractor {

	private final static JavaDepExtractor instance = new JavaDepExtractor();

	public JavaDepExtractor() {  

	}

	public static JavaDepExtractor getInstance() {
		return instance;
	}

	public void extractDependenciesFromEachMicroservice(MicroserviceDefinition ms) throws IOException {
		Process p = Runtime.getRuntime().exec(" java -jar javadepextractor.jar " + ms.getPath());
		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String s = null;
	}

	public String getDependenciesExtracted(MicroserviceDefinition microservice) throws IOException {
		this.extractDependenciesFromEachMicroservice(microservice);
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

}
