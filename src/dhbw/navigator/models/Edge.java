package dhbw.navigator.models;

import java.util.ArrayList;

public class Edge {
	
	private double distanz;
	private String label;
	private Node startNode;
	private Node endNode;

	
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

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}
}
