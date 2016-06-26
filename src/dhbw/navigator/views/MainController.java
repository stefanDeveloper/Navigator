package dhbw.navigator.views;

import java.io.File;
import java.util.ArrayList;
import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.io.Menufx;
import dhbw.navigator.models.Node;
import dhbw.navigator.utility.Map;
import dhbw.navigator.utility.UtilityViews;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressIndicator;

public class MainController extends Thread {
	private Menufx menufx;
	
	@FXML
	private Canvas mapView;
	
	@FXML
	ProgressIndicator progressIndicator;

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
		UtilityViews.runTask(100);
		this.gc = mapView.getGraphicsContext2D();
		IParser parser = new Parser();
		Osm test = (Osm) parser.parseFile(new File("Testdata/export.xml"));
		ArrayList<Node> nodes = parser.getNodes(test);
		Thread map = new Thread(Map.drawMap(gc, nodes));
		
		map.start();
		
	}
}
