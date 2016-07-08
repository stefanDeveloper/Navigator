package dhbw.navigator.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

import dhbw.navigator.controles.AutoCompleteControle;
import dhbw.navigator.controles.MapControle;
import dhbw.navigator.controles.NodeInformationControle;
import dhbw.navigator.controles.PathListingControle;
import dhbw.navigator.controles.SlideBarControle;
import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Dijkstra;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IDijkstra;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.models.Node;
import dhbw.navigator.start.StartNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

/**
 * Controller for RootLayoutWindow.fxml
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class RootLayoutController implements PropertyChangeListener {
	private SlideBarControle flapBar;
	private AutoCompleteControle startPositionInput = new AutoCompleteControle("Start");
	private AutoCompleteControle destinationPositionInput = new AutoCompleteControle("Ziel");
	private Node startNode;
	private Node endNode;
	private NodeInformationControle originInformation = new NodeInformationControle(90);
	private NodeInformationControle destinationInformation = new NodeInformationControle(90);
	private PathListingControle pathListing = new PathListingControle();
	private Button startButton = new Button("Start");
	private MapControle map = new MapControle();


	private StartNavigator start;

	private ArrayList<Node> nodes;

	@FXML
	private StackPane primaryStackPane;

	public StartNavigator getStart() {
		return start;
	}

	public void setStart(StartNavigator start) {
		this.start = start;
	}

	/**
	 * load Xml Data
	 *
	 *            true for parse
	 */
	private void loadData(Boolean parseData) {
		IParser parser = new Parser();
		// Check boolean
		if (parseData) {
			// Parse file and serialize it
			Osm data = (Osm) parser.parseFile(new File("Testdata/germany.xml"));
			nodes = parser.getNodes(data);
			parser.serialize(nodes);
		} else {
			nodes = parser.deserialize();
		}
		SortedSet<String> namesOfJunctions = new TreeSet<>();
		for (Node n : nodes) {
			if (n.getIsJunction() == true)
				// Add names of Junction for Context Menu
				namesOfJunctions.add(n.getName());
		}
		startPositionInput.setNamesOfJunctions(namesOfJunctions);
		destinationPositionInput.setNamesOfJunctions(namesOfJunctions);

		//Set map content
		map.setOriginMap(nodes);
	}

	@FXML
	public void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//Muss in anderne Thread ausgelagert werden
				Button btn = new Button("<");
				//btn.setMinHeight(80);
				//btn.setMaxHeight(80);
				btn.setPrefHeight(80);
				btn.setBorder(Border.EMPTY);

				primaryStackPane.setAlignment(btn, Pos.CENTER_LEFT);


				addToCenter(map);
				addToCenter(btn);

				Label headerLabel = new Label("Routenwahl");
				headerLabel.setStyle("h1");

				flapBar = new SlideBarControle(325, btn,
						new Label("Routeneingabe"),
						startPositionInput,
						destinationPositionInput,
						originInformation,
						destinationInformation,
						pathListing);
				start.getPrimaryBorder().setLeft(flapBar);
				loadData(false);
				btn.fire();

				flapBar.addPropertyChangeListener(isExpanded -> {
					System.out.println(isExpanded.getNewValue());
					if((boolean)isExpanded.getNewValue()){
						 btn.setText("<");
					}else{
						btn.setText(">");
					}
				});
			}
		});
		// Add PropertyChangeListener to get and set Value
		destinationPositionInput.addPropertyChangeListener(e -> {
			if (e.getNewValue().equals("")) { // end node selected
				destinationInformation.setNode(null); // return null?
				setEndNode(null);
			} else {
				System.out.println("Ziel: " + e.getNewValue());
				Node n = getNodeByName((String) e.getNewValue(), nodes);
				if (n != null) {
					setEndNode(n);
				}
			}
		});
		// Add PropertyChangeListener to get and set Value
		startPositionInput.addPropertyChangeListener(e -> {
			if (e.getNewValue().equals("")) {
				originInformation.setNode(null);
				setStartNode(null); // reset/clear startNode
			} else {
				System.out.println("Start: " + e.getNewValue());
				Node n = getNodeByName((String) e.getNewValue(), nodes);
				if (n != null) {
					setStartNode(n);

				}
			}
		});
	}

	private Node getNodeByName(String name, ArrayList<Node> nodes) {
		for (Node n : nodes) {
			if (n.getName().equals(name))
				return n;
		}
		return null;
	}

	public void addToCenter(javafx.scene.Node node) {
		primaryStackPane.getChildren().add(node);
	}

	public void removeFromCenter(javafx.scene.Node node) {
		primaryStackPane.getChildren().remove(node);
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
		originInformation.setNode(startNode);
		nodeChanged();
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
		destinationInformation.setNode(endNode);
		nodeChanged();
	}

	void nodeChanged()
	{
		if(startNode!=null && endNode !=null)
		{
			//Start dijkstra
			IDijkstra dijkstra = new Dijkstra();
			ArrayList<Node> path = dijkstra.FindPath(startNode, endNode);
			pathListing.setPath(path);
			for (int i = 0; i < path.size(); i++) {
				System.out.println(path.get(i).getName());
			}
		}
	}
}
