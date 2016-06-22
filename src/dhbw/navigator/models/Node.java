package dhbw.navigator.models;

import java.util.ArrayList;

public class Node {
	
	private double laenge;
	private double breite;
	private String label;
	private ArrayList<Edge> kante;
	private Node vorganeger;
	private double distanz;
	
	public Node(String label){
		this.label = label;
	}
	
	//Setter und Getter
	public void setLaenge(double laenge){
		this.laenge = laenge;
	}
	
	public double getLaenge(){
		return laenge;
	}
	
	public void setBreite(double breite){
		this.breite = breite;
	}
	
	public double getBreite(){
		return breite;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}

	public void setKante(ArrayList<Edge> kante) {
		this.kante = kante;
	}
	
	public ArrayList<Edge> getKante() {
		return kante;
	}
	
	public Node getVorganeger() {
		return vorganeger;
	}

	public void setVorganeger(Node vorganeger) {
		this.vorganeger = vorganeger;
	}

	public double getDistanz() {
		return distanz;
	}

	public void setDistanz(double distanz) {
		this.distanz = distanz;
	}
	
	//Kante einf�gen
	public ArrayList<Edge> addKante(Edge kante){
		ArrayList<Edge> kanten = getKante();
		if(kanten==null){
			kanten = new ArrayList<Edge>();
		}
		kanten.add(kante);
		return kanten;
	}
}
