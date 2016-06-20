package dhbw.navigator.dijkstra;

public class Adjazenzliste {
	
	public class Cell{
		
		private Knoten data;
		private Cell next;
		
		public Cell(Knoten k){
			setData(k);
		}

		//Getter und Setter
		public Knoten getData() {
			return data;
		}

		public void setData(Knoten data) {
			this.data = data;
		}

		public Cell getNext() {
			return next;
		}

		public void setNext(Cell next) {
			this.next = next;
		}
	}
	
	private Cell anker; //Erstes Element
	private Cell ende; //Letztes Element
	
	//Getter und Setter für anker und ende
	public Cell getAnker() {
		return anker;
	}

	public void setAnker(Cell anker) {
		this.anker = anker;
	}
	
	public Cell getEnde() {
		return ende;
	}

	public void setEnde(Cell ende) {
		this.ende = ende;
	}
	
	public Adjazenzliste addLast(Knoten k){
		Cell cKnoten = new Cell(k);
		
		if(anker == null){
			anker = cKnoten;
			ende = cKnoten;
		}
		else{
			ende.next = cKnoten;
			ende = cKnoten;
		}
		return this;
	}
	
}
