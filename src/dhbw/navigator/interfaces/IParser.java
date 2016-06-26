package dhbw.navigator.interfaces;

import java.io.File;
import java.util.ArrayList;
import dhbw.navigator.generated.Osm;
import dhbw.navigator.models.Node;

public interface IParser {
	
	/**
	 * 
	 * @param pFile
	 * @return 
	 */
	public Object parseFile(File pFile);

	public ArrayList<Node> getNodes(Osm pOsm);
}
