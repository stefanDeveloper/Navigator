package dhbw.navigator.implementation;

import java.io.*;
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

public class Parser implements IParser {

	static String path = System.getProperty("user.home") + "\\desktop\\map.ser";

	@Override
	public Object parseFile(File pFile) {
		Osm customer = new Osm();
		try{
			JAXBContext jaxbContext = JAXBContext.newInstance(Osm.class);
	
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			customer = (Osm) jaxbUnmarshaller.unmarshal(pFile);
		} catch (Exception e){
			e.printStackTrace();
		}
		Collections.sort(customer.getWay(), new Comparator<Osm.Way>() {
			@Override
			public int compare(Osm.Way way2, Osm.Way way1)
			{

				return  ((Long)way1.getNd().get(0).getRef()).compareTo(way2.getNd().get(0).getRef());
			}
		});
		return customer;
	}


	public ArrayList<Node> getNodes(Osm pOsm)
	{
		long startTime = System.nanoTime();
		ArrayList<Node> result = extractNodes(pOsm);
		result = merge(result);
		result = extractWays(pOsm, result);
		printDuration(startTime, "Node parsing");
		return result;
	}

	@Override
	public ArrayList<Node> deserialize() {
		long startTime = System.nanoTime();

		ArrayList<Node> nodes;

		try{

			FileInputStream fin = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fin);
			nodes = (ArrayList<Node>) ois.readObject();
			ois.close();
			printDuration(startTime, "Deserialisation");
			return nodes;

		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}


	}

	void printDuration(long startTime, String description)
	{
		long endTime = System.nanoTime();
		long duration = (  ((endTime - startTime)/1000000));
		System.out.println("TIMER: " + description + " ,duration: " +duration + " ms.");
	}

	@Override
	public void serialize(ArrayList<Node> nodes) {
		long startTime = System.nanoTime();
		try{

			FileOutputStream fout = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(nodes);
			oos.close();
			System.out.println("Serialized map");

		}catch(Exception ex){
			ex.printStackTrace();
		}
		printDuration(startTime, "Serialisation");
	}

	ArrayList<Node> merge(ArrayList<Node> existingNodes)
	{
		ArrayList<Node> extractedNodes = new ArrayList<>();
		for(Node n: existingNodes)
		{
			boolean exists = false;
			for(Node en: extractedNodes)
			{
				if(n.isSameNode(en))
				{
					for(Edge e: n.getEdges())
					{
						en.addEdge(e);
					}
					exists = true;
					en.setJunctions((en.getJunctions()+1));
					break;
				}
			}
			if(!exists){
				extractedNodes.add(n);
			}
		}
		return extractedNodes;
	}

	/**
	 * Finds the edges for all nodes from an osm source object.
	 * @param pOsm Osm source object.
	 * @param existingNodes Nodes to append the edges to.
	 * @return The given nodes with the new edges added.
	 */
	ArrayList<Node> extractWays(Osm pOsm, ArrayList<Node> existingNodes)
	{
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

				//Check if way is the start of an edge
				for (Node n : existingNodes) {
					if (n.getId() == startNodeId) {
						//Way is a start of an edge, create edge
						Osm.Node endNode = pOsm.getNodeById(endNodeId);
						Edge edge = new Edge(n, new Node(endNode));

						//Add the new edge to the node and to the temporary edge list
						n.addEdge(edge);
						edges.add(edge);

						//Remove from list
						way.remove(i);
						length--;
						break;
					}
				}
			}
		}

		int counter = 0;
		int edgeCounter = 0;
		int loopCounter = 0;
		boolean somethingRemoved = true;

		while(somethingRemoved){
		//while ((way = pOsm.getWay()).size()>0 && somethingRemoved) {
			somethingRemoved = false;
			edgeCounter = 0;
			for (int i = 0; i < length; i++) {
				counter++;
				int blub = counter % 100000;
				if (blub == 0) System.out.println("Count: " + counter +
						"; Remaining: " + pOsm.getWay().size() +
						"; Loops: " + loopCounter);

				List<Osm.Way.Nd> nds = way.get(i).getNd();
				startNodeId = nds.get(0).getRef();

				for (Edge e : edges) {
					if (e.getEndNode().getId() == startNodeId) {
						endNodeId = nds.get(nds.size()-1).getRef();
						Osm.Node endNode = pOsm.getNodeById(endNodeId);
						e.addPart(new Node(endNode));

						for (Node n : existingNodes) {
							if (n.getId() == endNodeId) {
								e.addPart(n);
								n.addEdge(e);
							}
						}

						length--;
						way.remove(i);
						edgeCounter ++;
						somethingRemoved = true;
						break;
					}
					if(edgeCounter == edges.size()){
						loopCounter++;
						break;
					}
				}
			}
		}
		System.out.println("GENAU");
		return existingNodes;
	}

	/**
	 * Extracts all valid junctions (junctions that have a reference number)
	 * from an Osm source.
	 * The junctions get saved in a list of nodes, containing only the name,
	 * coordinates and reference number.
	 * @param pOsm An osm source object.
	 * @return A list of nodes.
	 */
	ArrayList<Node> extractNodes(Osm pOsm)
	{
		ArrayList<Node> result = new ArrayList<Node>();
		for(Osm.Node n: pOsm.getNode())
		{
			Object tag = n.getTag("highway");
			//A node has to be a junction and needs a ref to be a valid junction!
			if(tag!=null && n.doesTagExist("name")&& tag.equals("motorway_junction") && n.doesTagExist("ref"))
			{
				result.add(new Node(n));
			}
		}
		return result;
	}
}
