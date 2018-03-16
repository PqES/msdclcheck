package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import core.MicroserviceDefinition;
import core.MicroservicesSystem;
import fileManager.OutputManager;

public class Util {

	private MicroservicesSystem system;
	Set<MicroserviceDefinition> microservice;
	Set<File> files = new HashSet<>();
	String nameMs;
	Set<String> extracts = new HashSet<>();
	boolean path = true;

	public Util() {
		this.system = new MicroservicesSystem();
		microservice = new HashSet<>(); 
	}

	public static String getExtensionOfFile(File f) {
		int index = f.getName().lastIndexOf('.');
		if (index >= 0 && index < f.getName().length()) {
			return f.getName().substring(index + 1);
		} else {
			return null;
		}
	}

	public static List<File> getAllFiles(File dir) throws IOException {
		if (!dir.getCanonicalFile().isDirectory()) {
			throw new IOException("Folder " + dir.getCanonicalFile() + " is not a folder");
		}
		List<File> anyFiles = new LinkedList<>();
		File files[] = dir.listFiles();

		for (File f : files) {
			if (f.isDirectory()) {
				anyFiles.addAll(getAllFiles(f));
			} else {
				String extension = getExtensionOfFile(f);
				if ((extension != null)
						&& ((extension.equalsIgnoreCase("cs")) || (extension.equalsIgnoreCase("java")))) {
					anyFiles.add(f);
				}
			}
		}
		return anyFiles;
	}

	public static String readFileToCharArray(String filePath) throws IOException {

		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();

		return fileData.toString();
	}

	// Lê todos os arquivos dos microsserviços e busca pelo arquivo de configuração
	//bootstrap
	public Set<File> readFileConfig(File dir) throws IOException {
		Set<File> files = new HashSet<>();
		if (dir.isDirectory()) {

			for (File f : dir.listFiles()) {
				if(f.isDirectory()) { 
					files.add(f);
					findInformationsOfMicroservices(f, f.getPath());
					readFileMs(f);
					
				}

			}
		}
		return files;

	}

	private void readFileMs(File dir) throws IOException {

		for (File f : dir.listFiles()) {
			if (!f.isDirectory()) {
				if (f.getName().equals("DynamicExtract.txt")) {
					//System.out.println("Source: " + dir.getPath() );
					readFile(f);

				}
			} else
				continue;
		}

	}

	private List<File> findInformationsOfMicroservices(File dir, String path) throws IOException {
		boolean verifyName = false;
		List<File> anyFiles = new LinkedList<>();
		List<File> resourcesFiles = new LinkedList<>();
		String language = "";
		String name = "";
		if (dir.isDirectory()) {

			for (File f : dir.listFiles()) {

				if (f.isDirectory()) {

					name = f.getName();
					if (name.equals("resources")) {
						getInformationFile(f, path);
						verifyName = true;
					} else {

						anyFiles.addAll(findInformationsOfMicroservices(f, path));
					}

				}
			}
		}

		return resourcesFiles;
	}

	private void readFile(File f) throws IOException {

		FileReader file;
		file = new FileReader(f);
		BufferedReader buffer = new BufferedReader(file);
		String line = "";

		while (buffer.ready()) {
			line = buffer.readLine(); 
			extracts.add(nameMs + " communicate " + line);

		}
		buffer.close();
		file.close();
		

	}

	public void getInformationFile(File f, String path) throws IOException {
		MicroserviceDefinition currentService = null;

		File[] files = f.listFiles();

		for (File fl : files) {
			if (fl.getName().contains("bootstrap")) {
				FileReader file = new FileReader(fl);
				BufferedReader buffer = new BufferedReader(file);
				String line;
				String name = "";
				String server = "";
				String port = "";
				String link;

				while (buffer.ready()) {

					line = buffer.readLine();
					String[] conteudo = line.split(": ");
					if (line.contains("name:")) {
						name = conteudo[1];
						nameMs = name;
					} else if (line.contains("port")) {
						port = conteudo[1];

					} else if (line.contains("server")) {
						server = "http://" + conteudo[1] + ":";
					}
				}
				link = server + port;
				currentService = new MicroserviceDefinition(name, link, path, " ");
				microservice.add(currentService);
				system.addMicroservice(currentService);
				buffer.close();
				file.close();
			} 
		}

	}


	public void createConstraints(String path) throws IOException {
		OutputManager.createConstraints(system, path+"constraints.txt");
	}

	public Set<String> getExtracts() { 
		return extracts;
	}

}
