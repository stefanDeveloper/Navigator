package dhbw.navigator.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import dhbw.navigator.generated.Osm;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.models.Edge;
import dhbw.navigator.models.Node;

/**
 * Parser
 * Can parse, Serialize and deserialize nodes
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class Parser implements IParser {
	/**
	 * Parse XML-File into OSM-Object
	 */
	@Override
	public Object parseFile(File pFile) {
		Osm customer = new Osm();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Osm.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			customer = (Osm) jaxbUnmarshaller.unmarshal(pFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Sort Collection to enhance speed in the future
		Collections.sort(customer.getWay(), new Comparator<Osm.Way>() {
			@Override
			public int compare(Osm.Way way2, Osm.Way way1) {
				return ((Long) way1.getNd().get(0).getRef()).compareTo(way2.getNd().get(0).getRef());
			}
		});
		return customer;
	}

	@Override
	public ArrayList<Node> getNodes(Osm pOsm) {
		Timer timer = new Timer("Get nodes");
		ArrayList<Node> result = extractNodes(pOsm);
		result = mergeNodes(result);
		result = extractWays(pOsm, result);
		result = mergeEdges(result);
		for(Node n: result)
		{
			n.setAllIds(null);
		}
		timer.printDuration();
		return result;
	}

	//TODO Remove debug function
	private void testTest(Node start, ArrayList<Node> test) {
		if (!test.contains(start)) {
			test.add(start);
			for (Node n : start.getNeighbours()) {

				testTest(n, test);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Node> deserialize(String path) {
		Timer timer = new Timer("Deserilisation");

		ArrayList<Node> nodes;

		try {

			FileInputStream fin = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fin);
			Object object = ois.readObject();
			nodes = (ArrayList<Node>)object;
			ois.close();
			timer.printDuration();

			ArrayList<Node> test = new ArrayList<>();
			testTest(nodes.get(200), test);

			return nodes;

		} catch (Exception ex) {
			//ex.printStackTrace();
			return null;
		}

	}

	@Override
	public void serialize(ArrayList<Node> nodes, String path) {
		Timer timer = new Timer("Serilisation");

		try {
			//Delete old files
			File file = new File(path);
			boolean result = Files.deleteIfExists(file.toPath());
			System.out.println("Old file deleted: " + result);

			FileOutputStream fout = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(nodes);
			oos.close();
			System.out.println("Serialized map");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		timer.printDuration();
	}


	/**
	 * Merges duplicated nodes.
	 * @param givenNodes
	 * @return
	 */
	ArrayList<Node> mergeNodes(ArrayList<Node> givenNodes) {
		Timer timer = new Timer("Merging");
		ArrayList<Node> extractedNodes = new ArrayList<>();

		while(!givenNodes.isEmpty()){
			boolean exists = false;
			for(Node en: extractedNodes){
				if(en.isSameNode(givenNodes.get(0)))
				{
					//Node already exists, add the reference id to the id list of the existing node.
					en.addId(givenNodes.get(0).getPrimaryId());
					givenNodes.remove(0);
					exists = true;
					break;
				}
			}
			if(!exists)
			{
				//Add node to extracted nodes
				extractedNodes.add(givenNodes.get(0));
				//Remove node from the given node list
				givenNodes.remove(0);
			}
		}
		timer.printDuration();
		return extractedNodes;
	}

	ArrayList<Node> mergeEdges(ArrayList<Node> nodes){
		for(Node n: nodes){
			int edgeCount = n.getEdges().size();
			for (int i = 0; i < edgeCount; i++) {
				for (int j = 0; j < edgeCount; j++) {
					if(j!=i && n.getEdges().get(i).getStartNode() == n.getEdges().get(j).getEndNode())
					{
						n.getEdges().remove(j);
						edgeCount--;
						break;
					}
				}
			}
		}
		return nodes;
	}

	/**
	 * Finds the edges for all nodes from an osm source object.
	 * 
	 * @param pOsm
	 *            Osm source object.
	 * @param existingNodes
	 *            Nodes to append the edges to.
	 * @return The given nodes with the new edges added.
	 */
	ArrayList<Node> extractWays(Osm pOsm, ArrayList<Node> existingNodes) {
		System.out.println("Extracting edges started.");

		ArrayList<Edge> edges = new ArrayList<>();
		List<Osm.Way> way = pOsm.getWay();
		long startNodeId;
		long endNodeId;
		int length = way.size();

		for (int i = 0; i < length; i++) {

			Osm.Way w = way.get(i);
			if (w.getTag("highway").equals("motorway")) {
				List<Osm.Way.Nd> nds = w.getNd();
				startNodeId = nds.get(0).getRef();
				endNodeId = nds.get(nds.size() - 1).getRef();

				// Check if way is the start of an edge
				for (Node n : existingNodes) {
					if (n.isPartOfWay(startNodeId)) {
						// Way is a start of an edge, create edge
						Osm.Node endNode = pOsm.getNodeById(endNodeId);
						Edge edge = new Edge(n, new Node(endNode));

						// Add the new edge to the node and to the temporary
						// edge list
						n.addEdge(edge);
						edges.add(edge);

						// Remove from list
						way.remove(i);
						length--;
						break;
					}
				}
			}
		}

		int counter = 0;
		int edgeCounter = 0;
		int finishedEdgeCounter = 0;
		int loopCounter = 0;
		boolean somethingRemoved = true;

		while (somethingRemoved) {
			// while ((way = pOsm.getWay()).size()>0 && somethingRemoved) {
			somethingRemoved = false;
			edgeCounter = 0;
			for (int i = 0; i < length; i++) {

				//Debug log
				counter++;
				int blub = counter % 50000;
				if (blub == 0)
					System.out.println(
							"Count: " + counter + "; Remaining: " + pOsm.getWay().size() + "; Loops: " + loopCounter);

				List<Osm.Way.Nd> nds = way.get(i).getNd();
				startNodeId = nds.get(0).getRef();

				for (Edge e : edges) {
					//Check if the way has to append to an existing node
					if (e.getEndNode().getPrimaryId() == startNodeId) {
						endNodeId = nds.get(nds.size() - 1).getRef();
						Osm.Node endNode = pOsm.getNodeById(endNodeId);
						e.addPart(new Node(endNode));

						for (Node n : existingNodes) {
							//Check if the endNode is a junction, if that is the case,
							//the edge is finished.
							if (n.isPartOfWay(endNodeId)) {
								e.addPart(n);
								n.addEdge(e);
								finishedEdgeCounter++;
							}
						}

						length--;
						way.remove(i);
						edgeCounter++;
						somethingRemoved = true;
						break;
					}
					if (edgeCounter == edges.size()) {
						loopCounter++;
						break;
					}
				}
			}
		}
		System.out.println("Extracting edges finished.");
		return existingNodes;
	}

	/**
	 * Extracts all valid junctions (junctions that have a reference number)
	 * from an Osm source. The junctions get saved in a list of nodes,
	 * containing only the name, coordinates and reference number.
	 * 
	 * @param pOsm
	 *            An osm source object.
	 * @return A list of nodes.
	 */
	ArrayList<Node> extractNodes(Osm pOsm) {
		ArrayList<Node> result = new ArrayList<Node>();
		for (Osm.Node n : pOsm.getNode()) {
			Object tag = n.getTag("highway");
			// A node has to be a junction and needs a ref to be a valid
			// junction!
			if (tag != null && n.doesTagExist("name") && tag.equals("motorway_junction") && n.doesTagExist("ref")) {
				result.add(new Node(n));
			}
		}
		return result;
	}
}
