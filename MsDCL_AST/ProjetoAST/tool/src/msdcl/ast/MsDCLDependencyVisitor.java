package msdcl.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

//import com.sun.org.apache.xpath.internal.Expression;

import msdcl.dependencies.ClassAnnotationDependency;
import msdcl.dependencies.ClassSingleAnnotationDependency;
import msdcl.dependencies.Dependency;
import msdcl.dependencies.FieldAnnotationDependency;
import msdcl.dependencies.FieldNormalAnnotationDependency;
import msdcl.dependencies.MethodAnnotationDependency;
import msdcl.exception.MsDCLException;

public class MsDCLDependencyVisitor extends ASTVisitor {

	private ArrayList<Dependency> dependencies;
	private Set<TypeDeclaration> declarations;

	private ICompilationUnit unit;
	private CompilationUnit compUnit;
	private CompilationUnit fullClass;
	private String className;
	private HashMap<String, ArrayList> dependencies2;

	public MsDCLDependencyVisitor() {
	}

	public MsDCLDependencyVisitor(ICompilationUnit unit) throws MsDCLException {
		try {
			this.dependencies = new ArrayList<Dependency>();
			this.unit = unit;

			this.className = unit.getParent().getElementName() + "."
					+ unit.getElementName().substring(0, unit.getElementName().length() - 5);

			ASTParser parser = ASTParser.newParser(AST.JLS4);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(unit);
			parser.setResolveBindings(true);

			this.fullClass = (CompilationUnit) parser.createAST(null); // parse

			this.fullClass.accept(this);
		} catch (Exception e) {
			throw new MsDCLException(e, unit);
		}
	}

	public MsDCLDependencyVisitor(String s, String str) throws MsDCLException {
		try {
			// System.out.println("O que chegou aqui senhor: "+ str);
			this.dependencies = new ArrayList<Dependency>();
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			parser.setSource(str.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			this.fullClass = (CompilationUnit) parser.createAST(null);
			this.unit = (ICompilationUnit) fullClass.getJavaElement();

			List types = fullClass.types();
			TypeDeclaration typeDec = (TypeDeclaration) types.get(0);
			this.className = typeDec.getName().getFullyQualifiedName();

			this.declarations = new LinkedHashSet<TypeDeclaration>();

			this.dependencies2 = new HashMap<>();

			this.fullClass.accept(this);

		} catch (Exception e) {
			e.printStackTrace();
			throw new MsDCLException(e, unit);
		}
	}

	ArrayList<String> names = new ArrayList<>();

	public ArrayList<String> getNames() {
		return this.names;
	}
	// public boolean visit(MethodDeclaration method) {
	// if(method.getName().toString().equals("filter")){
	// System.out.println("Method: "+ method.getName().getFullyQualifiedName());
	// if(method.getParent().getNodeType() == ASTNode.TYPE_DECLARATION){
	// TypeDeclaration parentClass = TypeDeclaration.class.cast(method.getParent());
	// parentClass.getName().toString();
	// }
	// }
	// return false;
	// }

	public ArrayList<Dependency> getDependencies() {
		return dependencies;
	}

	public Set<TypeDeclaration> getDeclarations() {
		return declarations;
	}

	public ICompilationUnit getUnit() {
		return unit;
	}

	public CompilationUnit getFullClass() {
		return this.fullClass;
	}

	public boolean visit(MarkerAnnotation node) { // Visita à uma anotação definida em uma classe. No contexto do
													// MsDCL será considerada as anotações @Autowired (Spring)
		
			if (node.getParent().getNodeType() == ASTNode.FIELD_DECLARATION) {
				FieldDeclaration field = (FieldDeclaration) node.getParent();
				Type type = field.getType();
				String typeDependency = addNameOfTypes(type);
				this.dependencies
						.add(new FieldAnnotationDependency(this.className, 
								node.getTypeName().getFullyQualifiedName(),
								fullClass.getLineNumber(node.getStartPosition()),
								node.getStartPosition(), 
								node.getLength(),
								((VariableDeclarationFragment) field.fragments().get(0)).getName().getIdentifier(),
								typeDependency));
	
			} else if (node.getParent().getNodeType() == ASTNode.METHOD_DECLARATION) {
				MethodDeclaration method = (MethodDeclaration) node.getParent();
				this.dependencies.add(new MethodAnnotationDependency(this.className,
						node.getTypeName().getFullyQualifiedName(), fullClass.getLineNumber(node.getStartPosition()),
						node.getStartPosition(), node.getLength(), method.getName().getIdentifier()));
	
			} else if (node.getParent().getNodeType() == ASTNode.TYPE_DECLARATION) {
				
				this.dependencies.add(new ClassAnnotationDependency(this.className,
						node.getTypeName().getFullyQualifiedName(), fullClass.getLineNumber(node.getStartPosition()),
						node.getStartPosition(), node.getLength()));
	
			}
		
	
		this.dependencies2.put(className, this.dependencies);
		return true;
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		
		String  expression = node.getValue().toString();
		
		if(node.getParent().getNodeType() == ASTNode.TYPE_DECLARATION) {
			this.dependencies.add(new ClassSingleAnnotationDependency(this.className,
					node.getTypeName().getFullyQualifiedName(), 
					fullClass.getLineNumber(node.getStartPosition()),
					node.getStartPosition(),
					node.getLength(), expression));
		}
		else if(node.getParent().getNodeType() == ASTNode.FIELD_DECLARATION) {
			FieldDeclaration field = (FieldDeclaration) node.getParent();
			Type type = field.getType();
			String typeDependency = addNameOfTypes(type);
			this.dependencies.add(new FieldNormalAnnotationDependency(this.className, 
							node.getTypeName().getFullyQualifiedName(),
							fullClass.getLineNumber(node.getStartPosition()),
							node.getStartPosition(), 
							node.getLength(),
							((VariableDeclarationFragment) field.fragments().get(0)).getName().getIdentifier(),
							typeDependency, 
							expression));
		}
		return false;
		
	}
	public String addNameOfTypes(Type type) {

		if (type instanceof SimpleType) {
			return ((SimpleType) type).getName().getFullyQualifiedName();
		} else if (type instanceof QualifiedType) {
			return ((QualifiedType) type).getName().getFullyQualifiedName();
		}
		else if (type instanceof PrimitiveType) {
			return ((PrimitiveType) type).getPrimitiveTypeCode().toString();
		}
		return "";
	}

	// public boolean visit(TypeDeclaration node) {
	//
	// List types = fullClass.types();
	// TypeDeclaration typeDec = (TypeDeclaration) types.get(0);
	// this.className = typeDec.getName().getFullyQualifiedName();
	//
	//
	// return false;
	// }

	// @Override
	// public boolean visit(TypeDeclaration node) {
	// if (!node.isLocalTypeDeclaration() && !node.isMemberTypeDeclaration()) {
	// try {
	// List types = fullClass.types();
	// TypeDeclaration typeDec = (TypeDeclaration) types.get(0);
	// //IType type = ((IType) fullClass).getTypes()[0];
	// ITypeHierarchy typeHierarchy = ((IType) typeDec).newSupertypeHierarchy(null);
	//
	// //IType[] typeSuperclasses = typeHierarchy.getAllSuperclasses((IType)
	// typeDec);
	//
	//// for (IType t : typeSuperclasses) {
	//// if (node.getSuperclassType() != null
	//// &&
	// t.getFullyQualifiedName().equals(node.getSuperclassType().resolveBinding().getQualifiedName()))
	// {
	//// this.dependencies.add(new ExtendDirectDependency(this.className,
	// t.getFullyQualifiedName(), fullClass
	//// .getLineNumber(node.getSuperclassType().getStartPosition()),
	// node.getSuperclassType().getStartPosition(),
	//// node.getSuperclassType().getLength()));
	//// } else {
	//// this.dependencies.add(new ExtendIndirectDependency(this.className,
	// t.getFullyQualifiedName(), null, null, null));
	//// }
	//// }
	//
	// IType[] typeSuperinter = typeHierarchy.getAllInterfaces();
	//
	// externo: for (IType t : typeSuperinter) {
	// for (Object it : node.superInterfaceTypes()) {
	// switch (((Type) it).getNodeType()) {
	// case ASTNode.SIMPLE_TYPE:
	// SimpleType st = (SimpleType) it;
	// if
	// (t.getFullyQualifiedName().equals(st.getName().resolveTypeBinding().getQualifiedName()))
	// {
	// if (!((IType) typeDec).isInterface()) {
	// this.dependencies.add(new ImplementsDependency(this.className,
	// t.getFullyQualifiedName(),
	// fullClass.getLineNumber(st.getStartPosition()), st.getStartPosition(),
	// st.getLength()));
	// } else {
	// this.dependencies.add(new ExtendsDirectDependency(this.className,
	// t.getFullyQualifiedName(), fullClass
	// .getLineNumber(st.getStartPosition()), st.getStartPosition(),
	// st.getLength()));
	// }
	// continue externo;
	// }
	// break;
	// case ASTNode.PARAMETERIZED_TYPE:
	// ParameterizedType pt = (ParameterizedType) it;
	// if (t!= null && t.getFullyQualifiedName() != null && pt != null &&
	// pt.getType() != null && pt.getType().resolveBinding() != null &&
	// t.getFullyQualifiedName().equals(pt.getType().resolveBinding().getBinaryName()))
	// {
	// if (!((IType) typeDec).isInterface()) {
	// this.dependencies.add(new ImplementsDependency(this.className,
	// t.getFullyQualifiedName(),
	// fullClass.getLineNumber(pt.getStartPosition()), pt.getStartPosition(),
	// pt.getLength()));
	// } else {
	// this.dependencies.add(new ExtendsDirectDependency(this.className,
	// t.getFullyQualifiedName(), fullClass
	// .getLineNumber(pt.getStartPosition()), pt.getStartPosition(),
	// pt.getLength()));
	// }
	// continue externo;
	// }
	// break;
	// }
	// }
	// // this.dependencies.add(new ImplementIndirectDependency(this.className,
	// t.getFullyQualifiedName(), null, null, null));
	// }
	// } catch (JavaModelException e) {
	// throw new RuntimeException("AST Parser error.", e);
	// }
	// }
	// return true;
	// }
	//
	// @Override
	// public boolean visit(MethodDeclaration node) {
	// for (Object o : node.parameters()) {
	// if (o instanceof SingleVariableDeclaration) {
	// SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
	// this.dependencies.add(new DeclareParameterDependency(this.className,
	// this.getTargetClassName(svd.getType().resolveBinding()),
	// fullClass.getLineNumber(svd.getStartPosition()), svd.getStartPosition(),
	// svd.getLength(),
	// node.getName().getIdentifier(), svd.getName().getIdentifier()));
	// if (svd.getType().getNodeType() == Type.PARAMETERIZED_TYPE) {
	// for (Object t : ((ParameterizedType) svd.getType()).typeArguments()) {
	// if (t instanceof SimpleType) {
	// SimpleType st = (SimpleType) t;
	// this.dependencies.add(new DeclareParameterDependency(this.className,
	// this.getTargetClassName(st.resolveBinding()),
	// fullClass.getLineNumber(st.getStartPosition()), st.getStartPosition(),
	// st.getLength(), node.getName().getIdentifier(),
	// svd.getName().getIdentifier()));
	// } else if (t instanceof ParameterizedType) {
	// ParameterizedType pt = (ParameterizedType) t;
	// this.dependencies.add(new DeclareParameterDependency(this.className,
	// this.getTargetClassName(pt.getType().resolveBinding()),
	// fullClass.getLineNumber(pt.getStartPosition()), pt.getStartPosition(),
	// pt.getLength(), node.getName().getIdentifier(),
	// svd.getName().getIdentifier()));
	// }
	// }
	// }
	//
	// }
	// }
	//
	// return true;
	// }
	//
	// @Override
	// public boolean visit(VariableDeclarationStatement node) {
	// ASTNode relevantParent = getRelevantParent(node);
	//
	// switch (relevantParent.getNodeType()) {
	// case ASTNode.METHOD_DECLARATION:
	// MethodDeclaration md = (MethodDeclaration) relevantParent;
	//
	// this.dependencies.add(new DeclareVariableDependency(this.className,
	// this.getTargetClassName(node.getType().resolveBinding()),
	// fullClass.getLineNumber(node.getStartPosition()),
	// node.getType().getStartPosition(),
	// node.getType().getLength(), md.getName().getIdentifier(),
	// ((VariableDeclarationFragment)
	// node.fragments().get(0)).getName().getIdentifier()));
	//
	// break;
	// case ASTNode.INITIALIZER:
	// this.dependencies.add(new DeclareVariableDependency(this.className,
	// this.getTargetClassName(node.getType().resolveBinding()),
	// fullClass.getLineNumber(node.getStartPosition()),
	// node.getType().getStartPosition(),
	// node.getType().getLength(), "initializer static block",
	// ((VariableDeclarationFragment)
	// node.fragments().get(0)).getName().getIdentifier()));
	// break;
	// }
	//
	// return true;
	// }
	//
	// private String getTargetClassName(ITypeBinding type) {
	// String result = "";
	// if (type != null) {
	// if (!type.isAnonymous() && type.getQualifiedName() != null &&
	// !type.getQualifiedName().isEmpty()) {
	// result = type.getQualifiedName();
	// } else if (type.isLocal() && type.getName() != null &&
	// !type.getName().isEmpty()) {
	// result = type.getName();
	// } else if
	// (!type.getSuperclass().getQualifiedName().equals("java.lang.Object")
	// || type.getInterfaces() == null || type.getInterfaces().length == 0) {
	// result = type.getSuperclass().getQualifiedName();
	// } else if (type.getInterfaces() != null && type.getInterfaces().length == 1)
	// {
	// result = type.getInterfaces()[0].getQualifiedName();
	// }
	//
	// if (result.equals("")) {
	// throw new RuntimeException("AST Parser error.");
	// } else if (result.endsWith("[]")) {
	// result = result.substring(0, result.length() - 2);
	// } else if (result.matches(".*<.*>")) {
	// result = result.replaceAll("<.*>", "");
	// }
	// }
	// return result;
	// }
	//
	//
	//
	// private ASTNode getRelevantParent(final ASTNode node) {
	// for (ASTNode aux = node; aux != null; aux = aux.getParent()) {
	// switch (aux.getNodeType()) {
	// case ASTNode.FIELD_DECLARATION:
	// case ASTNode.METHOD_DECLARATION:
	// // case ASTNode.INITIALIZER:
	// return aux;
	// }
	// }
	// return node;
	// }

	public HashMap<String, ArrayList> getDependencies2() {
		return dependencies2;
	}

}
