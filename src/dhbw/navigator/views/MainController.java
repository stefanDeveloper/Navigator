package dhbw.navigator.views;

import dhbw.navigator.io.Menufx;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class MainController {
	private Menufx menufx;
	
	@FXML
	private Canvas mapView;

	private GraphicsContext gc;

	public Canvas getMap() {
		return this.mapView;
	}

	public void setMap(Canvas mapView) {
		this.mapView = mapView;
	}
	
	public Menufx getMenufx() {
		return this.menufx;
	}

	public void setMenufx(Menufx menufx) {
		this.menufx = menufx;
	}
	
	
	
	@FXML
	public void initialize(){
		this.gc = mapView.getGraphicsContext2D();
		
		
	}

}
