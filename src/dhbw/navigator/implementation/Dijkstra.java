package dhbw.navigator.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import dhbw.navigator.interfaces.IDijkstra;
import dhbw.navigator.models.Edge;
import dhbw.navigator.models.Node;

/**
 * Dijkstra
 * Implementation of the Dijkstra Algorithm
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */

public class Dijkstra implements IDijkstra {

	private ArrayList<Node> visited;
	private ArrayList<Node> toVisit;

	@Override
	public ArrayList<Node> FindPath(Node start, Node end) {
		this.visited = new ArrayList<>();
		this.toVisit = new ArrayList<>();
		this.initialize(start);
		this.toVisit.add(start);
		start.setShortestDistance(0);

		while (this.toVisit.size() > 0) {
			this.visit(this.toVisit.get(0));
			this.visited.add(this.toVisit.get(0));
			this.toVisit.remove(0);

			Collections.sort(this.toVisit, new Comparator<Node>() {
				@Override
				public int compare(Node way2, Node way1) {

					return ((Float) way1.getShortestDistance()).compareTo(way2.getShortestDistance());
				}
			});
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

	private void initialize(Node n) {
		n.setShortestDistance(-1);
		n.setLastNode(null);
		for (Node ns : n.getNeighbours()) {
			if (ns.getShortestDistance() != -1)
				this.initialize(ns);
		}
	}

	private void visit(Node start) {

		for (Edge e : start.getEdges()) {
			Node n = e.getNeighbour(start);
			if (!this.visited.contains(n)) {
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
