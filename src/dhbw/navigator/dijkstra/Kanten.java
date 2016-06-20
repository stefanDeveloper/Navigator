package dhbw.navigator.dijkstra;

public class Kanten {
	
	private double distanz;
	private String label;
	Knoten[] knoten = new Knoten[2];
	
	//Setter und Getter
	public void setDistanz(double distanz){
		//Keine negativen Werte erlauben
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
	
	public void setKnoten(Knoten[] knoten){
		if(knoten.length == 2){
			this.knoten = knoten;
		}
		else
		{
			//Error
		}
	}
	
	public Knoten[] getKnoten(){
		return knoten;
	}

}
