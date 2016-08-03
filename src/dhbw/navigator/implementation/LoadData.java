package dhbw.navigator.implementation;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import dhbw.navigator.controles.MapControle;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.interfaces.ISerialiser;
import dhbw.navigator.models.Node;

public class LoadData extends Thread {
	private ArrayList<Node> nodes;
	private String serialiseFilePath;
	private MapControle map;
	private String xmlFilePath;
	/**
	 *Load the map data. Data will be parsed form source if serialised data is
	 * not avaliable.
	 * @param nodes
	 * @param serialiseFilePath
	 * @param xmlFilePath
	 */
	public LoadData(ArrayList<Node> nodes, String serialiseFilePath, String xmlFilePath){
		this.nodes = nodes;
		this.serialiseFilePath = serialiseFilePath;
		this.map = new MapControle();
		this.xmlFilePath = xmlFilePath;
	}
	public void load(Boolean parseData, String xmlFilePath) { 
		IParser parser = new Parser2();
		ISerialiser serialiser = new Serialiser();
		// Set default location

		// Check boolean
		if (parseData) {
			// Parse file and serialize it
			nodes = parser.getNodes(xmlFilePath);
			serialiser.serialize(nodes, serialiseFilePath);
		} else {
			nodes = serialiser.deserialize(serialiseFilePath);
		}
		SortedSet<String> namesOfJunctions = new TreeSet<>();
		for (Node n : nodes) {
			if (n.getIsJunction())
				// Add names of Junction for Context Menu
				namesOfJunctions.add(n.getName());
		}
		//startPositionInput.setNamesOfJunctions(namesOfJunctions);
		//destinationPositionInput.setNamesOfJunctions(namesOfJunctions);
		//Node n = getNodeByName("Mönchengladbach-Nordpark", nodes);
		// Set map content
		map.setOriginMap(nodes);
	}
	
	public void run(){
		load(true, xmlFilePath);
	}

}
