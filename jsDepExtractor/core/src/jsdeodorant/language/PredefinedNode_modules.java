package jsdeodorant.language;

import java.util.ArrayList;
import java.util.List;

public class PredefinedNode_modules {
	private static List<PredefinedNodeModule> predefinedNodeModules = new ArrayList<>();

	static {
		
		PredefinedNodeModule npmModules = new PredefinedNodeModule("npm");
		npmModules.addNodeModule("fs");
		npmModules.addNodeModule("log4js");
		npmModules.addNodeModule("ttl-lru-cache");
		npmModules.addNodeModule("node-uuid");
		
		npmModules.addNodeModule("path");
		npmModules.addNodeModule("mongodb");
		npmModules.addNodeModule("stats");
		npmModules.addNodeModule("http");
		
		npmModules.addNodeModule("cookie-parser");
		npmModules.addNodeModule("express");
		npmModules.addNodeModule("method-override");
		npmModules.addNodeModule("bluebird");
		
		npmModules.addNodeModule("path");
		npmModules.addNodeModule("nodeUtils");
		npmModules.addNodeModule("server-favicon");
		npmModules.addNodeModule("lru-cache");
		
		npmModules.addNodeModule("node-static");
		npmModules.addNodeModule("request");
		npmModules.addNodeModule("util");
		npmModules.addNodeModule("event");
		
		npmModules.addNodeModule("lodash");
		npmModules.addNodeModule("config");
		npmModules.addNodeModule("StreamHandler");
		
		npmModules.addNodeModule("cassandra-driver");
		predefinedNodeModules.add(npmModules);
	}
	
	public static boolean isItPredefinedNode_module(String name) {
		System.out.println("Lista: " + getPredefinedNodeModules().size());
		for (PredefinedNodeModule predefinedModule : predefinedNodeModules) {
			System.out.println("predefinedModule: " +predefinedModule.getObjectName());
			for(String module:predefinedModule.getModules()) {
				if (module.contains(name))
					return true;
			}
			
		}
		return false;
	}

	public static List<PredefinedNodeModule> getPredefinedNodeModules() {
		return predefinedNodeModules;
	}
}
