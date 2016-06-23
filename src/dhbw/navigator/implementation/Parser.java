package dhbw.navigator.implementation;


import java.io.File;
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
			public int compare(Osm.Way fruit2, Osm.Way fruit1)
			{

				return  ((Long)fruit1.getNd().get(0).getRef()).compareTo(fruit2.getNd().get(0).getRef());
			}
		});
		return customer;
	}


	public ArrayList<Node> getNodes(Osm pOsm)
	{
		ArrayList<Node> result = new ArrayList<Node>();
		ArrayList<Edge> edges = new ArrayList<>();
		for(Osm.Node n: pOsm.getNode())
		{
			Object tag = n.getTag("highway");
			if(tag!=null && tag.equals("motorway_junction"))
			{
				Node newNode = new Node((String)n.getTag("name"));
				newNode.setLat(n.getLat().doubleValue());
				newNode.setLon(n.getLon().doubleValue());
				newNode.setId(n.getId());
				result.add(newNode);
			}
		}
		List<Osm.Way> way;
		boolean somethingRemoved = true;
		while ((way = pOsm.getWay()).size()>0 && somethingRemoved)
		{
			somethingRemoved = false;
			for (int i = 0; i < way.size(); i++) {
				Osm.Way w = way.get(i);
				if (!w.getTag("highway").equals("motorway_link")) {
					List<Osm.Way.Nd> nds = w.getNd();
					long startNodeId = nds.get(0).getRef();
					long endNodeId = nds.get(nds.size() - 1).getRef();

					boolean finished = false;

					for (Node n : result) {
						if (n.getId() == startNodeId) {
							Edge edge = new Edge();
							edge.getNode().add(n);
							edge.setFirstNodeId(startNodeId);
							edge.setLastNodeId(endNodeId);
							n.addEdge(edge);
							edges.add(edge);


							finished = true;
							break;
						}

					}
					if(!finished)
					{
						for (Edge e : edges) {
							if (e.getLastNodeId() == startNodeId) {
								e.setLastNodeId(endNodeId);
								for (Node n : result) {
									if (n.getId() == endNodeId) {
										n.addEdge(e);
										e.getNode().add(n);

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
						i = way.size()+10;
						somethingRemoved = true;
					}
				}
			}
		}
		return result;
	}
}
