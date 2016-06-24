package dhbw.navigator.views;

import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.io.Menufx;
import dhbw.navigator.models.Node;
import dhbw.navigator.utility.Map;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.util.ArrayList;

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
		IParser parser = new Parser();
		Osm test = (Osm) parser.parseFile(new File("Testdata/export.xml"));
		ArrayList<Node> nodes = parser.getNodes(test);
		Map.drawMap(gc, nodes);
		
	}

}
