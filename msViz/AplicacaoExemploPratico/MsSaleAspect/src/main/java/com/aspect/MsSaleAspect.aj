package com.aspect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public aspect MsSaleAspect {

	public pointcut callGetForObject():
		call(* postForObject(..)) ;

	after()  returning: callGetForObject() {

		Object[] args = thisJoinPoint.getArgs();
		String s0 = (String) args[0];
		System.err.println("S0: " + s0);
		String[] string = s0.split("//");
		String[] name = string[1].split("/");
		String retorno = name[0];
		System.out.println();
		System.err.println("Name: " + name[0]);
		String c = "";
		// System.out.println(name.length);
		for (int i = 1; i < name.length; i++) {
			// System.out.println("name: " + name[i]);
			if (!name[i].contains("?") && (!name[i].contains("&"))) {
				// System.out.println("nao contains!!");
				c += "/" + name[i] + "/";
			} else if (name[i].contains("?")) {
				String aux[] = name[i].split("\\?");
				c += aux[0];
			} else if (name[i].contains("&")) {
				String aux[] = name[i].split("&");
				c += "/" + aux[0];
			}

		}
		retorno += " using " + c;
		getInformation(retorno);

		System.err.println(retorno);
		try {
			writeExtractDynamicFile(retorno);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getInformation(String info) {

		return info;
	}

	public void writeExtractDynamicFile(String communication) throws IOException {
		try {
			File f = new File("DynamicExtract.txt");
			FileWriter fileWriter = new FileWriter(f, true);
			if(!verifyFile(f,communication)) {
				System.out.println("retornou true");
				fileWriter.write(communication);
				fileWriter.write("\n");
			}
			fileWriter.close();
			System.out.println("Arquivo de extrações dinamicas criado com sucesso! ");
			System.out.println("file: " + f.getAbsolutePath());
		} catch (Exception e) {

		}

	}

	public boolean verifyFile(File f, String communication) {
		try {
			System.out.println("FileIU: " + f);
			FileReader reader = new FileReader(f);
			BufferedReader buffer = new BufferedReader(reader);
			String line;
			try {
				while(buffer.ready()) {
					line = 	buffer.readLine();
					System.err.println("line: "+ line);
					if(line.equals(communication)) {
						return true;
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
		
	}
	
}