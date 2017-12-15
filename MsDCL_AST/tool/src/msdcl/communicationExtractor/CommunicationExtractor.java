package msdcl.communicationExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.MemberValuePair;

import entities.CommunicateDefinition;
import entities.MicroserviceDefinition;
import entities.MicroservicesSystem;
import msdcl.ast.MsDCLDependencyVisitor;
import msdcl.dependencies.AnnotationDependency;
import msdcl.dependencies.ClassAnnotationDependency;
import msdcl.dependencies.ClassNormalAnnotationDependency;
import msdcl.dependencies.FieldAnnotationDependency;
import msdcl.dependencies.FieldNormalAnnotationDependency;
import msdcl.dependencies.MemberPair;
import msdcl.dependencies.MethodAnnotationDependency;
import msdcl.exception.MsDCLException;
import util.Util;

public class CommunicationExtractor {

	private final static CommunicationExtractor instance = new CommunicationExtractor();
	// private final Pattern pattern =
	// Pattern.compile("\"(http://)(\\w|:|/|\\?|=|\\.|%)+\"");
	private final Pattern urlPattern = Pattern.compile(
			"\"(((https?|ftp|file)://)|www)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]\"",
			Pattern.CASE_INSENSITIVE);
	private final Pattern protocolPattern = Pattern.compile("(https?|ftp|file)://");
	// private final Pattern pattern = Pattern.compile("\"(http://)(.)+\"");
	private HashMap<String, Set> dependencies;

	public CommunicationExtractor() {
	}

	public static CommunicationExtractor getInstance() {

		return instance;
	}

	public CommunicateDefinition extractCommunication() {

		return null;

	}

	public CommunicateDefinition extractCommunicationFromString(String line, MicroserviceDefinition currentService,
			MicroservicesSystem system) { 
		Matcher matcher = this.urlPattern.matcher(line); 

		if (matcher.find()) {
			String stringMatched = matcher.group();
			Matcher protocolMatched = this.protocolPattern.matcher(stringMatched);
			String protocolExtracted = "";
			if (protocolMatched.find()) {
				protocolExtracted = protocolMatched.group();
			}
			String split[] = stringMatched.replaceAll("(https?|ftp|file)://|(\")", "").split("/", 2);
			String baseLink = protocolExtracted + split[0];
			String using = null;
			if (split.length > 1) {
				using = "/" + split[1];
				if (using.endsWith("/")) {
					using = using.substring(0, using.length() - 1);
				}
			}
			for (MicroserviceDefinition ms : system.getMicroservices()) {
				if (ms.getLink().equalsIgnoreCase(baseLink)) {
					return new CommunicateDefinition(currentService.getName(), ms.getName(), using);
				}
			}
			return new CommunicateDefinition(currentService.getName(), baseLink, using);
		}
		return null;
	}

	private Set<CommunicateDefinition> extractCommunicationsFromFile(File f, MicroserviceDefinition caller,
			MicroservicesSystem system) throws IOException {

		String filePath = f.getAbsolutePath();
		System.out.println("File Path: " + filePath);
		Set<CommunicateDefinition> communications = new HashSet<>();
		FileReader fr = new FileReader(f);
		BufferedReader buffer = new BufferedReader(fr);
		String line;
		while (buffer.ready()) {
			line = buffer.readLine();
			CommunicateDefinition communication = extractCommunicationFromString(line, caller, system);
			if (communication != null) {
				communications.add(communication);
			}
		}
		buffer.close();
		fr.close();
		return communications;
	}

	public Set<CommunicateDefinition> extractCommunicationFromFiles(File f, MicroserviceDefinition caller,
			MicroservicesSystem system) throws IOException, MsDCLException {

		MsDCLDependencyVisitor visitor = new MsDCLDependencyVisitor();
		String filePath = f.getAbsolutePath();
		filePath = f.getAbsolutePath();
		String s = f.getName();
		if (f.isFile()) {
			String service = Util.readFileToCharArray(filePath);
			visitor = new MsDCLDependencyVisitor(s, service);

		}

		getAnnotations(visitor);

		return null;
	}

	

	public void getAnnotations(MsDCLDependencyVisitor visitor) {

		dependencies = visitor.getDependencies2();

		for (String nameClass : dependencies.keySet()) {
			if (!dependencies.get(nameClass).isEmpty()) {

				for (Object d : dependencies.get(nameClass)) {

					if (d instanceof FieldAnnotationDependency) {
						if (((AnnotationDependency) d).getNameClass2().equals("Autowired")) {
							extractCommunicationsFromZull(((FieldAnnotationDependency) d).getDeclaration());
							System.out.println(((FieldAnnotationDependency) d).toString());
						}

					}
					if (d instanceof ClassNormalAnnotationDependency) {
						if (((ClassNormalAnnotationDependency) d).getNameClass2().equals("FeignClient")) {
							System.out.println(((ClassNormalAnnotationDependency) d).toString());
						}
					}
					System.out.println();
				}
			}

		}
	}
	
	public void extractCommunicationsFromZull(String className) {
		Set<CommunicateDefinition> communications = new HashSet<>();
		Set dependenciesOfClass = this.dependencies.get(className);
		for(Object dep : dependenciesOfClass) {
			if(dep instanceof ClassNormalAnnotationDependency) {
				for(MemberPair m : ((ClassNormalAnnotationDependency) dep).getMembersValues()) {
					if(m.getValue().equals("MsCustomer")); // Apenas teste para verificação
						//TODO: verificar os metodos e definir as comunicações
				}
			}
		}
	}
	

	public Set<CommunicateDefinition> extractCommunicationsFromService(MicroserviceDefinition caller,
			MicroservicesSystem system) throws IOException, MsDCLException {
		Set<CommunicateDefinition> accesses = new HashSet<>();
		System.out.println("File: " + caller.getPath());
		List<File> javaFiles = Util.getAllFiles(new File(caller.getPath()));

		for (File f : javaFiles) {
			System.out.println("f:  " + f.getName());
			extractCommunicationFromFiles(f, caller, system);
			
		}
		return accesses;
	}

	public HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> analyseAll(MicroservicesSystem system)
			throws IOException, MsDCLException {
		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> map = new HashMap<>();
		for (MicroserviceDefinition caller : system.getMicroservices()) {
			Set<CommunicateDefinition> accesses = new HashSet<>();
			accesses.addAll(this.extractCommunicationsFromService(caller, system));
			map.put(caller, accesses);
		}
		return map;
	}
}
