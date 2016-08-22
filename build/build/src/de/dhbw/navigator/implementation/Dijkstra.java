package de.dhbw.navigator.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.dhbw.navigator.models.Edge;
import de.dhbw.navigator.models.Node;

/**
 * Dijkstra
 * Implementation of the Dijkstra Algorithm
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */

public class Dijkstra implements NavigationAlgorithm {

	private ArrayList<Node> visited;
	private ArrayList<Node> toVisit;

	@Override
	public ArrayList<Node> FindPath(Node start, Node end) {
		this.visited = new ArrayList<>();
		this.toVisit = new ArrayList<>();
		this.initialize(start); //Reset all nodes
		//Start algorithm with start node
		this.toVisit.add(start);
		start.setShortestDistance(0);

		while (this.toVisit.size() > 0) {
			this.visit(this.toVisit.get(0));
			this.visited.add(this.toVisit.get(0));
			this.toVisit.remove(0);
			Collections.sort(this.toVisit, new Comparator<Node>() {
				@Override
				public int compare(Node way2, Node way1) {

					return ((Float) way2.getShortestDistance()).compareTo(way1.getShortestDistance());
				}
			});
			System.out.println();
		}

		ArrayList<Node> result = new ArrayList<>();
		Node tmp = end;
		while (tmp != null) {
			result.add(tmp);
			tmp = tmp.getLastNode();
		}

		ArrayList<Node> invertedList = new ArrayList<>();
		while (result.size() > 0) {
			invertedList.add(result.remove(result.size() - 1));
		}

		return invertedList;
	}

	/**
	 * Initialize the graph. Method calls itself recursive until every distance is negative.
	 * @param n Node with the complete graph to initialize.
	 * Set all distances from node to node to a negative value (-1)
	 */
	private void initialize(Node n) {
		n.setShortestDistance(-1);
		n.setLastNode(null);
		for (Node ns : n.getNeighbours()) {
			if (ns.getShortestDistance() != -1)
				this.initialize(ns);
		}
	}

	/**
	 * Get all neighbours from the start node and check if the neighbour nod is already visited.
	 * If the neighbour node isn't visited the distance from the start node to the neighbour node 
	 * will be calculated. If the calculated distance is shorter than the already distance,
	 * the new distance is the shortest.
	 * @param start Start node of the graph
	 */
	private void visit(Node start) {
		if(start.getName() != null && start.getName().equals("Mönchengladbach-Nordpark")) {
			System.out.print("Test");
		}
		for (Edge e : start.getEdges()) {
			Node n = e.getNeighbour(start);
			if (!visited.contains(n)) {
				float dist = start.getShortestDistance() + e.getDistance();
				if (dist < n.getShortestDistance() || n.getShortestDistance() == -1) {

					n.setShortestDistance(dist);
					n.setLastNode(start);
				}
				if (!this.toVisit.contains(n))
					this.toVisit.add(n);
			}
		}
	}
}
