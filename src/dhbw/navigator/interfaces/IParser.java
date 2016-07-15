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

	public ArrayList<Node> getNodes(String xmlFilePath);
}
