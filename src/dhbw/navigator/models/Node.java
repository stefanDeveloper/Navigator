package dhbw.navigator.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

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
	private ArrayList<Long> allIds = new ArrayList<>();

	//Attributes for dijkstra
	private float shortestDistance;
	private Node lastNode;

	public Node(Osm.Node node) {
		setName((String) node.getTag("name"));
		setLon(node.getLon());
		setLat(node.getLat());
		setPrimaryId(node.getId());
		addId(node.getId());
		if (node.doesTagExist("ref")) {
			setRef((String) node.getTag("ref"));
			setIsJunction(true);
		}
	}

	public Node getLastNode() {
		return lastNode;
	}

	public void setAllIds(ArrayList<Long> list){
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

	public ArrayList<Long> getAllIds()
	{
		return allIds;
	}

	public void addId(long id)
	{
		allIds.add(id);
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
		boolean sameName = compareNode.getName().equals(getName());
		if(sameName && dist > 100){
			System.out.println(dist + " name: " + this.getName());
		}
		return sameName && dist < 10;
	};

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
		return allIds.size();
	}

	public boolean isPartOfWay(long startNodeId)
	{
		for(Long l: allIds)
		{
			if(l==startNodeId) return true;
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
