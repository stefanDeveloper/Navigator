package dhbw.navigator.models;

public class Edge {
	
	private double distanz;
	private String label;
	Node[] node = new Node[2];
	
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
	
	public void setNode(Node[] node){
		if(node.length == 2){
			this.node = node;
		}
		else
		{
			//Error
		}
	}
	
	public Node[] getNode(){
		return node;
	}

}
