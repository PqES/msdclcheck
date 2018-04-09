package util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Util {

	public static String getExtensionOfFile(File f){
		int index = f.getName().lastIndexOf('.');
		if(index >= 0 && index < f.getName().length()){
			return f.getName().substring(index + 1);
		}else{
			return null;
		}
	}
	
	public static List<File> getAllFiles(File dir) throws IOException{
		if(!dir.getCanonicalFile().isDirectory()){
			throw new IOException("Folder " + dir.getCanonicalFile()+ " is not a folder");
		}
		List<File> anyFiles = new LinkedList<>();
		File files[] = dir.listFiles();
		for(File f : files){
			if(f.isDirectory()){
				anyFiles.addAll(getAllFiles(f));
			}else{
				String extension = getExtensionOfFile(f);
				if((extension != null) && ((extension.equalsIgnoreCase("cs")) || (extension.equalsIgnoreCase("java")))){
					anyFiles.add(f);
				}
			}
		}
		return anyFiles;
	}
	
}

