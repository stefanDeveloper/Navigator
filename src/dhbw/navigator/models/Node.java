package dhbw.navigator.models;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Node {

	private String name;
	private ArrayList<Edge> edges = new ArrayList<>();
	private BigDecimal lon;
	private BigDecimal lat;
	private long id;

	
	public Node(String name){
		this.name = name;
	}
	
	public void setname(String name){
		this.name = name;
	}
	
	public String getname(){
		return name;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	
	//Add edge
	public ArrayList<Edge> addEdge(Edge edge){
		edges.add(edge);
		return edges;
	}

	public BigDecimal getLon() {
		return lon;
	}

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
}
