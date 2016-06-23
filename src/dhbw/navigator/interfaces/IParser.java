package dhbw.navigator.interfaces;

import dhbw.navigator.generated.Osm;
import dhbw.navigator.models.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface IParser {
	
	/**
	 * 
	 * @param pFile
	 * @return 
	 */
	public Object parseFile(File pFile);

	public ArrayList<Node> getNodes(Osm pOsm);
}
