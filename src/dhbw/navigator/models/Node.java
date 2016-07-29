package dhbw.navigator.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import dhbw.navigator.generated.Osm;

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
	private ArrayList<Node> allIds = new ArrayList<>();
	private int junctionCount;

	//Attributes for dijkstra
	private float shortestDistance;
	private Node lastNode;

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

	public Node getLastNode() {
		return lastNode;
	}

	public void setAllIds(ArrayList<Node> list){
		allIds = list;
	}

	public void setLastNode(Node lastNode) {
		this.lastNode = lastNode;
	}

	public float getShortestDistance() {
		return shortestDistance;
	}

	public void setShortestDistance(float shortestDistance) {
		this.shortestDistance = shortestDistance;
	}

	public ArrayList<Node> getAllIds()
	{
		return allIds;
	}

	public void addDuplicatedNode(Node id)
	{
		allIds.add(id);
		junctionCount++;
	}

	public Node(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	// Add edge
	public ArrayList<Edge> addEdge(Edge edge) {
		edges.add(edge);
		return edges;
	}

	public BigDecimal getLon() {
		return lon;
	}

	// Set Lon
	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public long getPrimaryId() {
		return id;
	}

	public void setPrimaryId(long id) {
		this.id = id;
	}

	/**
	 * Compare 2 Nodes
	 * 
	 * @param compareNode
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
		return (sameName || sameRef || isBigJunctionAswell) && dist < 3;
	};

	String getSimpleString(String s){

		return s.replaceAll("[-+.^:, ]","").toLowerCase();
	}

	String stringInAlphabeticelOrder(String s){
		char [] chars = s.toLowerCase().toCharArray();
		Arrays.sort(chars);
		s = "";
		for(char c: chars){
			s+=c;
		}
		return s;
	}

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

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public boolean getIsJunction() {
		return isJunction;
	}

	public void setIsJunction(boolean junction) {
		isJunction = junction;
	}

	public int getJunctionsCount() {
		return junctionCount;
	}

	public boolean isPartOfWay(long startNodeId)
	{
		for(Node n: allIds)
		{
			if(n.getPrimaryId()==startNodeId) return true;
		}
		return false;
	}

	/**
	 * Neighbors of an Node
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
