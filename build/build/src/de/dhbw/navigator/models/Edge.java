package de.dhbw.navigator.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Edge of Graph
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class Edge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6548099498965575157L;
	private float distance;
	private String label;
	private transient Node startNode;
	private long startNodeId;
	private long endNodeId;
	private transient Node endNode;
	private ArrayList<NodeCoordinate> allNodes = new ArrayList<NodeCoordinate>();

	public Edge(Node startNode, Node endNode) {
		setStartNode(startNode);
		setEndNode(startNode);

		allNodes.add(new NodeCoordinate(startNode));
		addPart(endNode);
	}

	/**
	 * Append a new node to the end of the edge.
	 * 
	 * @param endNode 
	 */
	public void addPart(Node endNode) {
		float tmpDist = distFrom(this.endNode, endNode);
		if(tmpDist>100){
			System.out.println(tmpDist + getStartNode().getName() + " n1: " + getEndNode().getPrimaryId() + " n2: " + endNode.getPrimaryId());

		}
		distance = distance + tmpDist;
		setEndNode(endNode);
		allNodes.add(new NodeCoordinate(endNode));
	}
	
	/**
	 * @return ArrayList with all nodes of the edge
	 */
	public ArrayList<NodeCoordinate> allNodes() {
		return allNodes;
	}

	/**
	 * Calculates the distance between two nodes.
	 * 
	 * @param node1
	 *            Node one.
	 * @param node2
	 *            Node two.
	 * @return
	 */
	float distFrom(Node node1, Node node2) {
		return distFrom(node1.getLat().floatValue(), node1.getLon().floatValue(),
				node2.getLat().floatValue(), node2.getLon().floatValue());
	}

	/**
	 * Calculates the distance between two coordinates.
	 * 
	 * @param lat1
	 *            Latitude coordinate one.
	 * @param lng1
	 *            Longitude coordinate one.
	 * @param lat2
	 *            Latitude coordinate two.
	 * @param lng2
	 *            Longitude coordinate two.
	 * @return float
	 */
	float distFrom(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);
		return dist / 1000;
	}

	/**
	 * Set the distance of the edge
	 * @param distance
	 */
	public void setDistance(float distance) {
		// Dont allow negative values
		if (distance > 0) {
			this.distance = distance;
		} else {
			// Error
		}
	}

	/**
	 * @return Distance between the nodes of the edge
	 */
	public float getDistance() {
		return distance;
	}

	/**
	 * @return Label of the edge
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return Start Node of the edge
	 */
	public Node getStartNode() {
		return startNode;
	}

	/**
	 * Set the start node of the edge
	 * @param startNode
	 */
	public void setStartNode(Node startNode) {
		this.startNode = startNode;
		startNodeId = startNode.getPrimaryId();
	}

	/**
	 * @return End node of the edge
	 */
	public Node getEndNode() {
		return endNode;
	}

	/**
	 * Set end node of the edge
	 * @param endNode
	 */
	public void setEndNode(Node endNode) {
		this.endNode = endNode;
		endNodeId = endNode.getPrimaryId();
		if (endNode.getName() != null) {
			label = "[" + getStartNode().getName() + "]-[" + getEndNode().getName() + "]";
		}
	}

	/**
	 * Get neighbour of the node
	 * @param n Node
	 * @return If the end node isn't the transfered node, return the end node of the edge.
	 * If the transfered node is the end node, return the start node of the edge
	 */
	public Node getNeighbour(Node n) {
		if (getEndNode() != n) {
			return getEndNode();
		} else {
			return getStartNode();
		}
	}

	public long getStartNodeId() {
		return startNodeId;
	}

	public long getEndNodeId() {
		return endNodeId;
	}
}
