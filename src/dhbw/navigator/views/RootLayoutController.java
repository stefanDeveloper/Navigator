package dhbw.navigator.views;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import dhbw.navigator.controles.*;
import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.models.Node;
import dhbw.navigator.start.StartNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

/**
 * Controller for RootLayout.fxml
 * 
 * @author Stefan
 *
 */
public class RootLayoutController {
	private SlideBarControle flapBar;
	private PathInputControle pathInput = new PathInputControle();
	private NodeInformationControle originInformation = new NodeInformationControle();
	private NodeInformationControle destinationInformation = new NodeInformationControle();
	private PathListingControle pathListing = new PathListingControle();

	private StartNavigator start;

	private ArrayList<Node> nodes;

	@FXML
	private StackPane primaryStackPane;
	@FXML
	private Button region;

	public StartNavigator getStart() {
		return this.start;
	}

	public void setStart(StartNavigator start) {
		this.start = start;
	}

	private void loadData(boolean parseData) {
		IParser parser = new Parser();
		if (parseData) {
			Osm data = (Osm) parser.parseFile(new File("Testdata/germany.xml"));
			nodes = parser.getNodes(data);
			parser.serialize(nodes);
		} else {
			nodes = parser.deserialize();
		}
		SortedSet<String> namesOfJunctions = new TreeSet<>();
		for (Node n : nodes) {
			if (n.getIsJunction() == true)
				namesOfJunctions.add(n.getName());
		}
		if(pathInput != null) pathInput.setNamesOfjunction(namesOfJunctions);

		//Populate the view
		this.primaryStackPane.getChildren().clear();
		MapControle map = new MapControle(nodes, false);
		this.addToCenter(map);
	}

	@FXML
	public void initialize() {
		primaryStackPane.setStyle("-fx-background-color: #666;");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//Muss in anderne Thread ausgelagert werden
				flapBar = new SlideBarControle(300, region,
						pathInput,
						originInformation,
						destinationInformation,
						pathListing);
				start.getPrimaryBorder().setLeft(flapBar);
				loadData(false);
				region.fire();

				//Some test stuff
				ArrayList<Node> test = new ArrayList<Node>();
				test.add(nodes.get(0));
				test.add(nodes.get(1));
				test.add(nodes.get(2));
				test.add(nodes.get(3));
				pathListing.setPath(test);
				originInformation.setNode(test.get(0));
			}
		});
	}

	public void addToCenter(javafx.scene.Node node) {
		this.primaryStackPane.getChildren().add(node);
	}

	public void removeToCenter(javafx.scene.Node node) {
		this.primaryStackPane.getChildren().remove(node);
	}

	/**
	 * Close Stage
	 */
	@FXML
	private void handleClose() {
		System.exit(0);
	}

	/**
	 * Info Alert
	 */
	@FXML
	private void handleInfo() {
		UtilityViews.Information("Navigator \n" + "Gruppenprojekt Programmieren\n" + "2. Semester\n"
				+ "Manuela Leopold\n" + "Markus Menrath\n" + "Stefan Machmeier\n" + "Konrad M�ller");
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
}
