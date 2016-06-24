package dhbw.navigator.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import dhbw.navigator.generated.Osm;
import dhbw.navigator.interfaces.IDijkstra;
import dhbw.navigator.models.Edge;
import dhbw.navigator.models.Node;

public class Dijkstra implements IDijkstra {
	
	private ArrayList<Node> visited;
	private ArrayList<Node> toVisit;

	@Override
	public ArrayList<Node> FindPath(Node start, Node end) {
		this.visited = new ArrayList<>();
		this.toVisit = new ArrayList<>();
		initialize(start);
		toVisit.add(start);
		start.setShortestDistance(0);
		
		while(toVisit.size() > 0){
			visit(toVisit.get(0));
			visited.add(toVisit.get(0));
			toVisit.remove(0);
			
			Collections.sort(toVisit, new Comparator<Node>() {				@Override
				public int compare(Node way2, Node way1)
				{

					return  ((Float)way1.getShortestDistance()).compareTo(way2.getShortestDistance());
				}
			});
		}
		
		ArrayList<Node> result = new ArrayList<>();
		Node tmp = end;
		while(tmp != null){
			result.add(tmp);
			tmp = tmp.getLastNode();
		}
		
		ArrayList<Node> invertedList = new ArrayList<>();
		while(result.size() > 0){
			invertedList.add(result.remove(result.size() - 1));
		}
		
		return invertedList;
	}
	
	private void initialize(Node n)
	{
		n.setShortestDistance(-1);
		n.setLastNode(null);
		for(Node ns : n.getNeighbours())
		{
			if(ns.getShortestDistance() != -1)
				initialize(ns);
		}
	}
	
	private void visit(Node start){
		
		for(Edge e : start.getEdges()){
			Node n = e.getNeighbour(start);
			if(!visited.contains(n)){
				float dist = start.getShortestDistance() + e.getDistance();
				if(dist < n.getShortestDistance() 
						|| n.getShortestDistance() == -1){
					
					n.setShortestDistance(dist);
					n.setLastNode(start);
				}
				if(!toVisit.contains(n))
					toVisit.add(n);
			}
		}
	}
}
