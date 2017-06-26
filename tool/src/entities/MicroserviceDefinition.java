package entities;

import java.util.Set;

public class MicroserviceDefinition {

	private String name;
	private String link;
	private String path;
	private String language;
	private String dcl;
	private Set<ConstraintDefinition> rules;
	
//	public MicroserviceDefinition(String name, String link, String path, String language, Set<RuleDefinition> rules){
//		this.name = name;
//		this.link = link;
//		this.path = path;
//		this.language = language;
//		this.rules = rules;
//	}
	public MicroserviceDefinition(String name, String link, String path, String language){
		this.name = name;
		this.link = link;
		this.path = path;
		this.language = language;
		
	
	}
	public MicroserviceDefinition(){
		
	}
	public MicroserviceDefinition(String name, String link, String path, String language, String dcl){
		this.name = name;
		this.link = link;
		this.path = path;
		this.language = language;
		this.dcl = dcl;
	
	}
	
	public String getName() {
		return name;
	}

	public String getLink() {
		return link;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public String getLanguage(){
		return this.language;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public int hashCode(){
		return this.name.length();
	}
	
	
	public Set<ConstraintDefinition> getRules() {
		return rules;
	}

	public void setRules(Set<ConstraintDefinition> rules) {
		this.rules = rules;
	}
	
	public String getDcl() {
		return dcl;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof MicroserviceDefinition){
			MicroserviceDefinition m = (MicroserviceDefinition) obj;
			return this.name.equals(m.name);
		}
		return false;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
}
