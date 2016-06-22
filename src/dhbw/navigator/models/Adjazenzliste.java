package dhbw.navigator.models;

public class Adjazenzliste {
	
	public class Cell{
		
		private Node data;
		private Cell next;
		
		public Cell(Node k){
			setData(k);
		}

		//Getter und Setter
		public Node getData() {
			return data;
		}

		public void setData(Node data) {
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
	
	//Getter und Setter f�r anker und ende
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
	
	public Adjazenzliste addLast(Node k){
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
