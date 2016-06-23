package dhbw.navigator.implementation;


import java.io.File;
import java.lang.reflect.Array;
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
		ArrayList<Node> result = extractNodes(pOsm);
		result = extractWays(pOsm, result);
		result = merge(result);
		return result;
	}

	ArrayList<Node> merge(ArrayList<Node> existingNodes)
	{
		ArrayList<Node> extractedNodes = new ArrayList<>();
		for(Node n: existingNodes)
		{
			boolean exists = false;
			for(Node en: extractedNodes)
			{
				if(isSameNode(n, en))
				{
					for(Edge e: n.getEdges())
					{
						en.addEdge(e);
					}
					exists = true;
					break;
				}
			}
			if(!exists){
				extractedNodes.add(n);
			}
		}
		return extractedNodes;
	}

	boolean isSameNode(Node node1, Node node2)
	{
		if(node2.getname()==null || node1.getname() == null) return false;
		return (node1.getname().equals(node2.getname()));
	};

	ArrayList<Node> extractWays(Osm pOsm, ArrayList<Node> existingNodes)
	{
		ArrayList<Edge> edges = new ArrayList<>();

		List<Osm.Way> way;
		boolean somethingRemoved = true;
		while ((way = pOsm.getWay()).size()>0 && somethingRemoved)
		{
			somethingRemoved = false;
			for (int i = 0; i < way.size(); i++) {
				Osm.Way w = way.get(i);
				if (w.getTag("highway").equals("motorway")) {
					List<Osm.Way.Nd> nds = w.getNd();
					long startNodeId = nds.get(0).getRef();
					long endNodeId = nds.get(nds.size() - 1).getRef();

					boolean finished = false;

					//Check if way is the start of an edge
					for (Node n : existingNodes) {
						if (n.getId() == startNodeId) {
							//Way is a start of an edge, create edge
							Edge edge = new Edge();
							//Start node is the node the edge appends to
							edge.setStartNode(n);

							//Create a temp node for the end node
							edge.setEndNode(createNodeFromOsm(pOsm, endNodeId));

							//Add the new edge to the node and to the temporary edge list
							n.addEdge(edge);
							edges.add(edge);

							finished = true;
							break;
						}

					}
					if(!finished)
					{
						for (Edge e : edges) {
							if (e.getEndNode().getId() == startNodeId) {

								e.setEndNode(createNodeFromOsm(pOsm, endNodeId));

								for (Node n : existingNodes) {
									if (n.getId() == endNodeId) {
										e.setEndNode(createNodeFromOsm(pOsm, n.getId()));
										n.addEdge(e);
									}
								}
								finished = true;
								break;
							}
						}
					}
					if(finished)
					{
						pOsm.getWay().remove(w);
						somethingRemoved = true;
						break;
					}
				}
			}
		}
		return existingNodes;
	}

	Node createNodeFromOsm(Osm pOsm, long node)
	{
		Osm.Node tmpOsmNode = pOsm.getNodeById(node); //Get end node
		String name = "Temp";
		if(tmpOsmNode.doesTagExist("name")) {
			name = (String)tmpOsmNode.getTag("name");
		}
		//name = (String)tmpOsmNode.getTag("name");
		Node tmpNode = new Node(name);
		tmpNode.setLon(tmpOsmNode.getLon());
		tmpNode.setLat(tmpOsmNode.getLat());
		tmpNode.setId(tmpOsmNode.getId());
		return tmpNode;
	}

	ArrayList<Node> extractNodes(Osm pOsm)
	{
		ArrayList<Node> result = new ArrayList<Node>();
		for(Osm.Node n: pOsm.getNode())
		{
			Object tag = n.getTag("highway");
			if(tag!=null && tag.equals("motorway_junction"))
			{
				Node newNode = new Node((String)n.getTag("name"));
				newNode.setLat(n.getLat());
				newNode.setLon(n.getLon());
				newNode.setId(n.getId());
				result.add(newNode);
			}
		}
		return result;
	}
}
