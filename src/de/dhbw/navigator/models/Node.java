package de.dhbw.navigator.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import de.dhbw.navigator.generated.Osm;

/**
 * Node of Graph
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class Node implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1165502237304904849L;
	private String name;
	private ArrayList<Edge> edges = new ArrayList<>();
	private BigDecimal lon;
	private BigDecimal lat;
	// Interchange Number
	private String ref;
	private long id;
	private boolean isJunction;
	private transient ArrayList<Node> allIds = new ArrayList<>();
	private int junctionCount;

	//Attributes for dijkstra
	private transient  float shortestDistance;
	private transient Node lastNode;

	public Node(Osm.Node node) {
		if(node.doesTagExist("name"))setName((String) node.getTag("name"));
		setLon(node.getLon());
		setLat(node.getLat());
		setPrimaryId(node.getId());
		addDuplicatedNode(this);
		if (node.doesTagExist("ref")) {
			setRef((String) node.getTag("ref"));
			setIsJunction(true);
		}
	}
    /**
     * Returns the last node of the graph
     * @return Last node in graph
     */
	public Node getLastNode() {
		return lastNode;
	}
	/**
	 * @param list ArrayList with all IDs
	 */

	public void setAllIds(ArrayList<Node> list){
		allIds = list;
	}
	
	/**
	 * Set the last node of the graph
	 * @param lastNode Last node of the graph
	 */
	public void setLastNode(Node lastNode) {
		this.lastNode = lastNode;
	}
	
	/**
	 * Returns the shortestDistance-Attribute
	 * @return Shortest distance
	 */
	public float getShortestDistance() {
		return shortestDistance;
	}
	
	/**
	 * Set the shortest distance from node to node
	 * @param shortestDistance Shortest distance from one node to his neighbour
	 */
	public void setShortestDistance(float shortestDistance) {
		this.shortestDistance = shortestDistance;
	}
	
	/**
	 * @return ArrayList of the allIDs-Attribute
	 */
	public ArrayList<Node> getAllIds()
	{
		return allIds;
	}

	public void addDuplicatedNode(Node id)
	{
		allIds.add(id);
		junctionCount++;
	}

	/**
	 * Constructor of node
	 * @param name Name of the Node
	 */
	public Node(String name) {
		this.name = name;
	}
	
	/**
	 * Set the name of the node
	 * @param name Name of the Node
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Name of the Node
	 */
	public String getName() {
		return name;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	
	/**
	 * Return all edges of the node
	 * @return All edges of the node
	 */
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	
	/**
	 * 
	 * @param edge Edge from one node to another
	 * @return ArrayList with all edges of the node
	 */
	public ArrayList<Edge> addEdge(Edge edge) {
		edges.add(edge);
		return edges;
	}
	
	/**
	 * Get Longitude of the node
	 * @return Longitude
	 */
	public BigDecimal getLon() {
		return lon;
	}

	/**
	 * Set Longitude of the node
	 * @param lon Longitude
	 */
	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}
	
	/**
	 * Get Latitude of the node
	 * @return Latitude of the node
	 */
	public BigDecimal getLat() {
		return lat;
	}
	
	/**
	 * Set Latitude of the node
	 * @param lat Latitude of the node
	 */
	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}
	
	/**
	 * Returns the ID of the node
	 * @return Primaty ID
	 */
	public long getPrimaryId() {
		return id;
	}
	
	/**
	 * Set the ID of the node
	 * @param id Primary ID of the node
	 */
	public void setPrimaryId(long id) {
		this.id = id;
	}

	/**
	 * Compare 2 Nodes
	 * @param compareNode Node to compare
	 * @return boolean
	 */
	public boolean isSameNode(Node compareNode) {

		float dist = distFrom(compareNode, this);
		boolean sameName = compareName(getName(), compareNode.getName());
		boolean sameRef = getRef()!=null && compareNode.getRef() != null && getRef().equals(compareNode.getRef());
		boolean isBigJunctionAswell;
		isBigJunctionAswell = compareNode.getName().toLowerCase().contains("kreuz")
				&&getName().toLowerCase().contains("kreuz");
		if(!isBigJunctionAswell){
			isBigJunctionAswell = compareNode.getName().toLowerCase().contains("dreieck")
					&&getName().toLowerCase().contains("dreieck");
		}
		return (sameName || sameRef || isBigJunctionAswell)&&dist<3;
	};
	
	/**
	 * Replace special characters from String. Special characters: [-+.^:, ]
	 * @param s String with special characters
	 * @return String without any special characters
	 */
	String getSimpleString(String s){

		return s.replaceAll("[-+.^:, ]","").toLowerCase();
	}
	
	/**
	 * Sort String in alphabetical order
	 * @param s String to sort
	 * @return In alphabetical oder sorted String
	 */
	String stringInAlphabeticelOrder(String s){
		char [] chars = s.toLowerCase().toCharArray();
		Arrays.sort(chars);
		s = "";
		for(char c: chars){
			s+=c;
		}
		return s;
	}
	
	/**
	 * Compare the 2 Names (Strings)
	 * @param n1 Name 1
	 * @param n2 Name 2
	 * @return True if the input parameters are the same, else false
	 */
	Boolean compareName(String n1, String n2){
		n1 = getSimpleString(n1);
		n2 = getSimpleString(n2);
		if(n1.equals(n2)) return true;
		if(n1.contains(n2)||n2.contains(n1)) return true;
		if(stringInAlphabeticelOrder(n1).equals(stringInAlphabeticelOrder(n2))) return true;
		return false;
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
	 * Return the reference of the node
	 * @return reference of the node
	 */
	public String getRef() {
		return ref;
	}
	
	/**
	 * Set the reference of the node
	 * @param ref Ref as String
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}

	/**
	 * @return isJunction-Attribute
	 */
	public boolean getIsJunction() {
		return isJunction;
	}
	
	/**
	 * Set the isJunction-Attribute
	 * @param junction boolean
	 */
	public void setIsJunction(boolean junction) {
		isJunction = junction;
	}

	/**
	 * Return the count of junctions
	 * @return Count of junctions
	 */
	public int getJunctionsCount() {
		return junctionCount;
	}

	/**
	 * Check if the node is part of the way
	 * @param startNodeId ID of the start node
	 * @return true if part of the way, else false
	 */
	public boolean isPartOfWay(long startNodeId)
	{
		for(Node n: allIds)
		{
			if(n.getPrimaryId()==startNodeId) return true;
		}
		return false;
	}

	/**
	 * Get Neighbors of an Node
	 * 
	 * @return ArrayList of Node
	 */
	public ArrayList<Node> getNeighbours() {
		ArrayList<Node> result = new ArrayList<>();
		for (Edge e : getEdges()) {
			result.add(e.getNeighbour(this));
		}
		return result;
	}
}
