package entities;

import java.util.Set;

public class MicroserviceDefinition {

	private String name;
	private String link;
	private String path;
	private String language;
	private Set<RuleDefinition> rules;
	
	public MicroserviceDefinition(String name, String link, String path, String language, Set<RuleDefinition> rules){
		this.name = name;
		this.link = link;
		this.path = path;
		this.language = language;
		this.rules = rules;
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
	
	public Set<RuleDefinition> getRules(){
		return this.rules;
	}
	
	@Override
	public int hashCode(){
		return this.name.length();
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
