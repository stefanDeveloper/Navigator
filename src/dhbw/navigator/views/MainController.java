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
import javafx.stage.Stage;

public class MainController extends Thread {
	private Menufx 	menufx;
	private IParser	parser;
	private Map 	map;
	
	@FXML
	private Canvas mapView;
	
	@FXML
	ProgressIndicator progressIndicator;

	private GraphicsContext gc;
	private Stage primaryStage;

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
	
	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
	}
	
	
	
	@FXML
	public void initialize(){ //Thread
		this.parser =  new Parser();
		ArrayList<Node> nodes;

		String importXmlPath = "Testdata/south_mid_germany.xml";

		//Set as true to import data (Needed at first start)
		boolean reimport = false;
		if(reimport)
		{
			Osm test = (Osm) this.parser.parseFile(new File(importXmlPath));
			nodes = this.parser.getNodes(test);
			parser.serialize(nodes);
		}


		nodes = parser.deserialize();

		this.gc = mapView.getGraphicsContext2D();
		this.map = new Map(gc, nodes);
		UtilityViews.runTask(1000, this.primaryStage);
		this.map.drawMap();
	}

	
}
