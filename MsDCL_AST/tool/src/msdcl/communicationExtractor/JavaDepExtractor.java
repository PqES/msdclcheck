package msdcl.communicationExtractor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import entities.MicroserviceDefinition;

public class JavaDepExtractor {

	
	private  final static JavaDepExtractor instance = new JavaDepExtractor();
	
	public JavaDepExtractor() {
		
	}
	public static JavaDepExtractor getInstance() {
		return instance;
	}
	public void extractDependenciesFromEachMicroservice(MicroserviceDefinition path) {
		Main.main(path);
	}
	public String getDependenciesExtracted(MicroserviceDefinition microservice) throws IOException {
		File file = new File(microservice.getPath() + "/dependencies.txt");
		if(!file.exists()) {
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
		System.out.println("Microservice: " + microservice.getName());
		return content.toString();
	}


	
	
}
