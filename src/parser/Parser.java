package parser;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class Parser implements IParser {
		
	@Override
	public Object parseFile(File pFile) {
		Osm customer = new Osm();
		try{
			File file = new File("Testdata/Ausschnitt_StuttgartLeonberg");
			JAXBContext jaxbContext = JAXBContext.newInstance(Osm.class);
	
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			customer = (Osm) jaxbUnmarshaller.unmarshal(file);
			return customer;
		} catch (Exception e){
			e.printStackTrace();
		}
		return customer;
	}
	
	
}
