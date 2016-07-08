package dhbw.navigator.interfaces;

import java.io.File;
import java.util.ArrayList;

import dhbw.navigator.generated.Osm;
import dhbw.navigator.models.Node;

/**
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public interface IParser {

	/**
	 * 
	 * @param pFile
	 * @return Object
	 */
	public Object parseFile(File pFile);

	public ArrayList<Node> getNodes(Osm pOsm);

	public ArrayList<Node> deserialize();

	public void serialize(ArrayList<Node> nodes);
}
