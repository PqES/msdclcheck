package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import analyser.AccessAnalyser;
import analyser.RulesAnalyser;
import entities.AccessDefinition;
import entities.MicroserviceDefinition;

public class Main {
	public static void main(String[] args) {
		MicroserviceDefinition services[] = new MicroserviceDefinition[3];
		//MsSale
		services[0] = new MicroserviceDefinition("MsSale", "www.product.com" , "C:/Users/Elder/Documents/NetBeansProjects/MicroserviceSale", "Java", null);
		//MsProduct
		services[1] = new MicroserviceDefinition("MsProduct", "http://sample-env-4.psebiyinbj.us-east-2.elasticbeanstalk.com/getProducts" , "src/entities", "Java", null);
		//MsClient
		services[2] = new MicroserviceDefinition("MsClient", "http://sample-env-3.psebiyinbj.us-east-2.elasticbeanstalk.com/getCustomerCpf/", "src/entities", "Java", null);
		try {
			HashMap<MicroserviceDefinition, Set<AccessDefinition>> accessMap = AccessAnalyser.getInstance().analyseAll(services);
			for(Entry<MicroserviceDefinition, Set<AccessDefinition>> e : accessMap.entrySet()){
				System.out.println(e.getKey()+": "+e.getValue());
			}
			RulesAnalyser.getInstance().analyseRules(accessMap); //analisador das regras, retorna um conjunto de acessos inválidos
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
