package jsdeodorant.analysis.abstraction;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.google.javascript.jscomp.parsing.parser.trees.ClassDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewExpressionTree;

import jsdeodorant.analysis.decomposition.AbstractExpression;
import jsdeodorant.analysis.decomposition.AbstractFunctionFragment;
import jsdeodorant.analysis.decomposition.TypeDeclaration;
import jsdeodorant.analysis.util.DebugHelper;
import jsdeodorant.analysis.util.ExternalAliasHelper;
import jsdeodorant.analysis.util.IdentifierHelper;

public class ObjectCreation extends Creation {
//	static Logger log = Logger.getLogger(ObjectCreation.class.getName());
	private AbstractFunctionFragment statement;
	private NewExpressionTree newExpressionTree;
	// In one scope we can have two functions with exact same names. but the
	// last one is executed in run-time because the last one is redeclared
	private Path pathClass;
	private TypeDeclaration classDeclaration;
	private AbstractExpression operandOfNew;
	private List<AbstractExpression> arguments;
	private boolean isClassDeclarationPredefined = false;
	private boolean isFunctionObject = false;
	private boolean isExportedModuleFunctionModule = false;
	private AbstractIdentifier identifier;
	private AbstractIdentifier aliasedIdentifier;
	private Module classDeclarationModule;
	private String moduleDeclarationLocation;

	public ObjectCreation(NewExpressionTree newExpressionTree, AbstractFunctionFragment statement) {
		this.newExpressionTree = newExpressionTree;
		this.statement = statement;
		String regex = "\\(\\v*[\\'\\\"].+\\.js[\\'\\\"]\\)function";
		Pattern pattern = Pattern.compile(regex);
		//Matcher matcher = pattern.matcher(line);
		//System.out.println("Name: " + IdentifierHelper.getIdentifier(newExpressionTree.operand).identifierName);
		if (IdentifierHelper.getIdentifier(newExpressionTree.operand).identifierName.equals("function"))
			setFunctionObject(true);
		else if(pattern.matcher(IdentifierHelper.getIdentifier(newExpressionTree.operand).identifierName).find()) {
			String regexName = "[\\'\\\"].+\\.js[\\'\\\"]";
			Pattern patternName = Pattern.compile(regexName);
			Matcher matcherName = patternName.matcher(IdentifierHelper.getIdentifier(newExpressionTree.operand).identifierName);
			this.moduleDeclarationLocation = matcherName.group(0);
			setExportedModuleFunctionModule(true);
		}
	}

	/**
	 * 
	 * @param newExpressionTree
	 * @param operandOfNew
	 *            the name of class that would instantiate
	 * @param arguments
	 *            passed to constructor
	 */
	public ObjectCreation(NewExpressionTree newExpressionTree, AbstractExpression operandOfNew, List<AbstractExpression> arguments) {
		this.newExpressionTree = newExpressionTree;
		this.operandOfNew = operandOfNew;
		this.arguments = arguments;
		String regex = "\\@\\(\\v*[\\'\\\"].+\\.js[\\'\\\"]\\)function";
		Pattern pattern = Pattern.compile(regex);
		
		if (this.operandOfNew.asIdentifiableExpression().getIdentifier().equals("function"))
			setFunctionObject(true);
		else if(pattern.matcher(IdentifierHelper.getIdentifier(newExpressionTree.operand).identifierName).find()) {
			String regexName = "[\\'\\\"].+\\.js[\\'\\\"]";
			Pattern patternName = Pattern.compile(regexName);
			Matcher matcherName = patternName.matcher(IdentifierHelper.getIdentifier(newExpressionTree.operand).identifierName);
			this.moduleDeclarationLocation = matcherName.group(0);
			setExportedModuleFunctionModule(true);
		}
	}

	public TypeDeclaration getClassDeclaration() {
		return classDeclaration;
	}

	public void setClassDeclaration(TypeDeclaration classDeclaration, Module module) {
		this.classDeclaration = classDeclaration;
		this.classDeclarationModule = module;
	}

	public AbstractExpression getOperandOfNew() {
		return operandOfNew;
	}

	public String getOperandOfNewName() {
		AbstractIdentifier identifier = IdentifierHelper.getIdentifier(operandOfNew.getExpression());
		return Strings.isNullOrEmpty(identifier.toString()) ? "<Anonymous>" : identifier.toString();
	}

	public AbstractIdentifier getIdentifier() {
		if (identifier == null)
			identifier = IdentifierHelper.getIdentifier(operandOfNew.getExpression());
		return identifier;
	}

	public AbstractIdentifier getAliasedIdentifier() {
		if (aliasedIdentifier != null)
			return aliasedIdentifier;
		if (getIdentifier() instanceof PlainIdentifier)
			return aliasedIdentifier = identifier;
		return aliasedIdentifier = ExternalAliasHelper.getAliasedIdentifier(statement, getIdentifier());
	}

	public List<AbstractExpression> getArguments() {
		return arguments;
	}

	public NewExpressionTree getNewExpressionTree() {
		return newExpressionTree;
	}

	public void setNewExpressionTree(NewExpressionTree newExpressionTree) {
		this.newExpressionTree = newExpressionTree;
	}

	public void setOperandOfNew(AbstractExpression operandOfNew) {
		this.operandOfNew = operandOfNew;
	}

	public void setArguments(List<AbstractExpression> arguments) {
		this.arguments = arguments;
	}

	public String toString() {
		return DebugHelper.extract(newExpressionTree);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ObjectCreation) {
			ObjectCreation toCompare = (ObjectCreation) other;
			return Objects.equals(this.newExpressionTree, toCompare.newExpressionTree);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(newExpressionTree);
	}

	public AbstractFunctionFragment getStatement() {
		return statement;
	}

	public void setStatement(AbstractFunctionFragment statement) {
		this.statement = statement;
	}

	public boolean isClassDeclarationPredefined() {
		return isClassDeclarationPredefined;
	}

	public void setClassDeclarationPredefined(boolean isClassDeclarationPredefined) {
		this.isClassDeclarationPredefined = isClassDeclarationPredefined;
	}

	public boolean isFunctionObject() {
		return isFunctionObject;
	}

	public void setFunctionObject(boolean isFunctionObject) {
		this.isFunctionObject = isFunctionObject;
	}
	
	public void setExportedModuleFunctionModule(boolean isExportedModuleFunctionModule) {
		this.isExportedModuleFunctionModule = isExportedModuleFunctionModule;
	}

	public String getClassDeclarationQualifiedName() {
		if (this.isClassDeclarationPredefined)
			return getIdentifier().toString();
		else			
			return  this.getClassDeclaration().getFunctionDeclaration().getQualifiedName();
	}

	public String getClassDeclarationKind() {
		if (this.getClassDeclaration() != null)
			return this.getClassDeclaration().getFunctionDeclaration().getKind().toString();
		else
			return "";
	}

	public String getObjectCreationLocation() {
//		if(this.getClassDeclaration().getFunctionDeclaration().getExportedModuleFunction()) {
//			return this.getClassDeclaration().getFunctionDeclaration().getModuleDeclarationLocation();
//		}
		System.err.println("Local da criação do objeto: " + this.newExpressionTree.location.toString());
		return this.newExpressionTree.location.toString();
				//this.newExpressionTree.location.toString();
	//	return SourceLocationHelper.getLocation(this.newExpressionTree.location);
	}

	public String getClassDeclarationLocation() {
		if (this.getClassDeclaration() != null) {
			if (this.getClassDeclaration().getFunctionDeclaration().getExportedModuleFunction()){
				return this.getClassDeclaration().getFunctionDeclaration().getModuleDeclarationLocation();
			}
			if(this.isExportedModuleFunctionModule) {
				return this.moduleDeclarationLocation;
			}
			//return classDeclarationModule.getCanonicalPath() + "." + this.getClassDeclaration().getFunctionDeclaration().getName();
			return classDeclarationModule.getCanonicalPath() + "."+ this.getClassDeclaration().getFunctionDeclaration().getFunctionDeclarationTree().location.toString();
			//return SourceLocationHelper.getLocation();
		}

		else {
			if (this.isClassDeclarationPredefined) {
	//			System.out.println("Local declaration predefined:  " +"js."+this.getOperandOfNewName() );
				return "js."+this.getOperandOfNewName();
			}
			return "";
		}
			
	}

	public Module getClassDeclarationModule() {
		return classDeclarationModule;
	}

	public void setClassDeclarationModule(Module classDeclarationModule) {
		this.classDeclarationModule = classDeclarationModule;
	}

	public String getModuleDeclarationLocation() {
		return moduleDeclarationLocation;
	}

	public void setModuleDeclarationLocation(String moduleDeclarationLocation) {
		this.moduleDeclarationLocation = moduleDeclarationLocation;
	}

	public boolean isExportedModuleFunctionModule() {
		return isExportedModuleFunctionModule;
	}
	public boolean setIsExportedModuleFunctionModule(Boolean isExportedModuleFunctionModule) {
		return isExportedModuleFunctionModule = isExportedModuleFunctionModule;
	}
}
