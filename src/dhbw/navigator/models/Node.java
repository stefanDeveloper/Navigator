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
	private float shortestDistance;
	private Node lastNode;

	public Node getLastNode() {
		return lastNode;
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

	// Interchange Number
	private String ref;
	private long id;
	private boolean isJunction;
	private int junctions = 1;

	public Node(String name) {
		this.name = name;
	}

	public Node(Osm.Node node) {
		// String name = null;
		// if (node.doesTagExist("name")) {
		// name = (String) node.getTag("name");
		// }
		setName((String) node.getTag("name"));
		setLon(node.getLon());
		setLat(node.getLat());
		setId(node.getId());
		if (node.doesTagExist("ref")) {
			setRef((String) node.getTag("ref"));
			setIsJunction(true);
		}
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Compare 2 Nodes
	 * 
	 * @param Node
	 * @return boolean
	 */
	public boolean isSameNode(Node compareNode) {
		return compareNode.getName().equals(getName());
	};

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

	public int getJunctions() {
		return junctions;
	}

	public void setJunctions(int junctions) {
		this.junctions = junctions;
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
