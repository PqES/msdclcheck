package unitTests;

import static org.junit.Assert.*;
import org.junit.Test;

import communicationExtractor.CommunicateDefinition;
import communicationExtractor.CommunicationExtractor;
import entities.MicroserviceDefinition;
import entities.MicroservicesSystem;

public class CommunicationExtractorTest {

	private MicroservicesSystem createMicroserviceSystem(int n){
		MicroservicesSystem system = new MicroservicesSystem();
		for(int i = 1; i <= n; i++){			
			system.addMicroservice(new MicroserviceDefinition("ms"+i, "http://www.ms"+i+".com", null, null));
		}
		return system;
	}
	
	@Test
	public void communicationExtractor(){
		MicroservicesSystem system = createMicroserviceSystem(2);
		String communicationLine = "call(\"http://www.ms2.com/test\");";
		CommunicateDefinition communication = CommunicationExtractor.getInstance()
				.extractCommunicationFromString(communicationLine, system.getMicroserviceDefinition("ms1"), system);
		assertEquals(communication, new CommunicateDefinition("ms1", "ms2", "/test"));
	}
	
	@Test
	public void communicationExtractorUnknown(){
		MicroservicesSystem system = createMicroserviceSystem(2);
		String communicationLine = "call(\"http://www.ms3.com/test\");";
		CommunicateDefinition communication = CommunicationExtractor.getInstance()
				.extractCommunicationFromString(communicationLine, system.getMicroserviceDefinition("ms1"), system);
		assertEquals(communication, new CommunicateDefinition("ms1", "http://www.ms3.com", "/test"));
	}
}
