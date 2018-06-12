package jsDepExtractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.google.common.io.Files;

public class Util {

	public static boolean verifyFileOfProject(File f, String projectName) {
		System.out.println("F: " + f.getName() + "name project: " + projectName);
		String[] paths = projectName.split("/");
		String name = paths[paths.length - 1];
	//	System.out.println("Nome do projeto  " + name);
		if (f.getName().contains(name)) {
			return true;
		}
		return false;
	}

	public static List<File> getAllFiles(String directoryPath, String projectName) throws IOException {
		List<File> anyFiles = new LinkedList<>();
		// File files[] = dir.listFiles();
		// anyFiles.addAll(files);
		if (!Strings.isNullOrEmpty(directoryPath)) {
			File rootDir = new File(directoryPath);

			if (!rootDir.exists())
				throw new FileNotFoundException("The directory path is not valid");
			for (File f : Files.fileTreeTraverser().preOrderTraversal(rootDir)) {
				if (f.isFile() && Files.getFileExtension(f.toPath().toString()).toLowerCase().equals("csv"))
					anyFiles.add(f);
			}
		}

	
		return anyFiles;
	}
	public static void writeFile(String path, String line) throws IOException {
		String newLine = line.replaceAll(Pattern.quote("\"."), "")
				.replaceAll(Pattern.quote("\""), "").replaceAll(Pattern.quote(".js"), "").replaceAll("#", "\\/");
		try {
			FileWriter fw = new FileWriter(path + "/dependencies.txt", true);
			fw.write(newLine);
			fw.write("\n");
			fw.close();
		} catch (IOException e) {
			System.out.println("Exeption: " + e.getMessage());
		}

		// System.out.println("line: " + line);

	}
}
