package dhbw.navigator.views;

import java.util.ArrayList;

import dhbw.navigator.io.Menufx;
import dhbw.navigator.models.Node;
import dhbw.navigator.utility.Map;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class MainController extends Thread {
	private Menufx 	menufx;
	private Map 	map;
	private GraphicsContext gc;
	private Stage primaryStage;
	private ArrayList<Node> nodes;
	@FXML
	private Canvas mapView;
	@FXML
	private ProgressIndicator progressIndicator;
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
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
	public Stage getStage() {
		return this.primaryStage;
	}
	
	@FXML
	public void initialize(){ //Thread
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//Draw Map
				gc = mapView.getGraphicsContext2D();
				map = new Map(gc, nodes);
				map.drawMap();
			}
		});
		
	
	}

	
}
