package dhbw.navigator.models;

import java.util.ArrayList;

public class Edge {
	
	private double distanz;
	private String label;
	ArrayList<Node> node = new ArrayList<Node>();
	private long firstNodeId;
	private long lastNodeId;

	
	//Setter and Getter
	public void setDistanz(double distanz){
		//Dont allow negative values
		if(distanz > 0){
			this.distanz = distanz;
		}
		else{
			//Error
		}
	}
	
	public double getDistanz(){
		return distanz;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	public void setNode(ArrayList<Node> node){
		//if(node.size() == 2){
			this.node = node;
		//}
		//else
		//{
		//	//Error
		//}
	}
	
	public ArrayList<Node> getNode(){
		return node;
	}

	public long getFirstNodeId() {
		return firstNodeId;
	}

	public void setFirstNodeId(long firstNodeId) {
		this.firstNodeId = firstNodeId;
	}

	public long getLastNodeId() {
		return lastNodeId;
	}

	public void setLastNodeId(long lastNodeId) {
		this.lastNodeId = lastNodeId;
	}
}
