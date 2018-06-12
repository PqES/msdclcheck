package jsdeodorant.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.javascript.jscomp.SourceFile;

import jsdeodorant.analysis.abstraction.Module;
import jsdeodorant.analysis.abstraction.Program;
import jsdeodorant.analysis.abstraction.SourceElement;
import jsdeodorant.analysis.decomposition.Statement;
import jsdeodorant.analysis.module.PackageExporter;
import jsdeodorant.analysis.module.PackageImporter;
import jsdeodorant.analysis.module.PackageSystem;
import jsdeodorant.analysis.module.closurelibrary.ClosureLibraryExportHelper;
import jsdeodorant.analysis.module.closurelibrary.ClosureLibraryImportHelper;
import jsdeodorant.analysis.module.commonjs.CommonJSExportHelper;
import jsdeodorant.analysis.module.commonjs.CommonJSRequireHelper;
import jsdeodorant.analysis.module.helma.HelmaRequireHelper;

public class JSproject {
	
	private Set<Module> modules;
	private static JSproject instance=null;
	
	public static JSproject getInstance(){
		return getInstance(false);
	}
	
	public static JSproject getInstance(boolean forceNew){
		if(forceNew || instance==null){
			instance= new JSproject();
		}
		return instance;
	}
	
	private JSproject(){
		this.modules= new LinkedHashSet<Module>();
	}

	public Set<Module> getModules() {
		return modules;
	}
	
	public void createModule(Program program,SourceFile sourceFile, List<String> messages, PackageSystem packageSystem , boolean hasModuleAnalysis){
		Module module = new Module(program, sourceFile, messages);
		this.modules.add(module);
		if(hasModuleAnalysis)
			this.processModules(module, packageSystem,true);
		
	}
	
	public void processModules(Module module, PackageSystem packageSystem, boolean onlyExports) {
		PackageImporter packageImporter = null;
		PackageExporter packageExporter = null;
		switch (packageSystem) {
		case CommonJS:
			if (!onlyExports)
				packageImporter = new CommonJSRequireHelper(module, modules);
			packageExporter = new CommonJSExportHelper(module, modules);
			break;
		case ClosureLibrary:
			if (!onlyExports)
				packageImporter = new ClosureLibraryImportHelper(module, modules);
			packageExporter = new ClosureLibraryExportHelper(module, modules);
			break;
		case Helma:
			if (!onlyExports)
				packageImporter = new HelmaRequireHelper(module, modules);
			break;
		default:
			break;
		}

		Program program = module.getProgram();
		for (SourceElement element : program.getSourceElements()) {
			if (element instanceof Statement) {
				Statement statement = (Statement) element;
				if (!onlyExports)
					packageImporter.extract(statement.getStatement());
				if(packageExporter !=null)
					packageExporter.extract(statement.getStatement());
			}
		}
	}


}
