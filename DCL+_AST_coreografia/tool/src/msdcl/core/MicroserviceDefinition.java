package msdcl.core;

public class MicroserviceDefinition {

	private String name;
	private String link;
	private String path; 
	private String language;
	
	public MicroserviceDefinition(String name, String link, String path, String language){
		this.name = name;
		this.link = link;
		this.path = path;
		this.language = language;
	}
	
	public MicroserviceDefinition () {}
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
