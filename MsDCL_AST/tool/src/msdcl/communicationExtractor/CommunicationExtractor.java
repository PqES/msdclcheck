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
import msdcl.dependencies.MethodNormalAnnotationDependency;
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
	HashMap<String, Set> dependenciesFromService = new HashMap<>();

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

	
	private Set<CommunicateDefinition> extractCommunicationsFromZull(String declaration,Set<CommunicateDefinition> communications,
			MicroserviceDefinition caller) {

		Set dependencies = dependenciesFromService.get(declaration);
		//Set<CommunicateDefinition> communications = new HashSet<>();
		CommunicateDefinition communication = new CommunicateDefinition();
		String msName = "";
		for (Object dep : dependencies) {
			if (dep instanceof ClassNormalAnnotationDependency) {
				if (((ClassNormalAnnotationDependency) dep).getNameClass2().equals("FeignClient")) {
					msName = ((ClassNormalAnnotationDependency) dep).getValue();
				}

			}
			else if (dep instanceof MethodNormalAnnotationDependency) {
				communication = new CommunicateDefinition(caller.getName(), msName, ((MethodNormalAnnotationDependency) dep).getValue());
			}

			if (communication != null) {
				communications.add(communication);
			}
		}
		return communications;

	}

	private Set<CommunicateDefinition> verifyCommunicationsByAnnotations(Set dependenciesFile,
			Set<CommunicateDefinition> communications, MicroserviceDefinition caller) {
		//Set<CommunicateDefinition> communications = new HashSet<>();
		for (Object dep : dependenciesFile) {
			if (dep instanceof FieldAnnotationDependency) {
				if (((AnnotationDependency) dep).getNameClass2().equals("Autowired")) {
					extractCommunicationsFromZull(((FieldAnnotationDependency) dep).getDeclaration(),
							communications, caller);

				}
				else
					return null;
			}
		}
		DependencyExtractor.getInstance().imprime(dependenciesFile);
		return communications;
	}

	public Set<CommunicateDefinition> extractCommunicationsFromService(MicroserviceDefinition caller,
			MicroservicesSystem system) throws IOException, MsDCLException {
		Set<CommunicateDefinition> communications = new HashSet<>();

		// vem todas as dependências das classes do microsserviço analisado.
		dependenciesFromService = DependencyExtractor.getInstance().extractDependenciesFromService(caller);
		List<File> javaFiles = Util.getAllFiles(new File(caller.getPath()));
		for (File f : javaFiles) {
			String fileName = f.getName();
			Set dependenciesFile = dependenciesFromService.get(fileName);
			DependencyExtractor.getInstance().imprime(dependenciesFile);
			verifyCommunicationsByAnnotations(dependenciesFile, communications, caller);
				communications = verifyCommunicationsByAnnotations(dependenciesFile, communications, caller);
		}
		
		return communications;
	}

	public HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> analyseAll(MicroservicesSystem system)
			throws IOException, MsDCLException {

		HashMap<MicroserviceDefinition, Set<CommunicateDefinition>> map = new HashMap<>();
		for (MicroserviceDefinition caller : system.getMicroservices()) {
			Set<CommunicateDefinition> accesses = new HashSet<>();
//			extractCommunicationsFromService(caller, system);
			accesses.addAll(this.extractCommunicationsFromService(caller, system));
			map.put(caller, accesses);
		}
		return map;
	}
}
