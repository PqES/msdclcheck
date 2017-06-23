package inputManager;

import java.io.File;
import java.util.Set;

import entities.MicroserviceDefinition;

public class InputManager {

	private static final InputManager instance = new InputManager();
	
	private InputManager(){
		
	}
	
	public static InputManager getInstance(){
		return instance;
	}
	
	public Set<MicroserviceDefinition> getMicroservicesDefinitionFromFile(File f){
		return null;
	}
}
