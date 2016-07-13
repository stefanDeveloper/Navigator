package dhbw.navigator.views;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.*;

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
import javafx.scene.layout.*;

/**
 * Controller for RootLayoutWindow.fxml
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class RootLayoutController {
	private SlideBarControle flapBar;
	private AutoCompleteControle startPositionInput = new AutoCompleteControle("Start");
	private AutoCompleteControle destinationPositionInput = new AutoCompleteControle("Ziel");
	private Node startNode;
	private Node destinationNode;
	private NodeInformationControle originInformation = new NodeInformationControle(90);
	private NodeInformationControle destinationInformation = new NodeInformationControle(90);
	private PathListingControle pathListing = new PathListingControle();
	private MapControle map = new MapControle();

	static private String path = System.getProperty("user.home") + "\\desktop\\map.ser";

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
	 * Load the map data. Data will be parsed form source if serialised data is not avaliable.
	 * @param parseData Boolean, set true to parse the data from the source again, false to deserialise already parsed data.
	 */
	private void loadData(Boolean parseData) {
		IParser parser = new Parser();
		// Set default location

		// Check boolean
		if (parseData) {
			// Parse file and serialize it
			Osm data = (Osm) parser.parseFile(new File("Testdata/germany.xml"));
			nodes = parser.getNodes(data);
			parser.serialize(nodes, path);
		} else {
			nodes = parser.deserialize(path);
			if(nodes==null){
				System.out.println("Deserialization failed.");
				Osm data = (Osm) parser.parseFile(new File("Testdata/germany.xml"));
				nodes = parser.getNodes(data);
				parser.serialize(nodes, path);
			}
		}
		SortedSet<String> namesOfJunctions = new TreeSet<>();
		for (Node n : nodes) {
			if (n.getIsJunction())
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
			@SuppressWarnings("static-access")
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
				btn.fire();
				loadData(false);
				flapBar.addPropertyChangeListener(isExpanded -> {
					if((boolean)isExpanded.getNewValue()){
						 btn.setText("<");
					}else{
						btn.setText(">");
					}
				});
			}

		});
		// Add PropertyChangeListener to destinationPositionInput controle.
		destinationPositionInput.addPropertyChangeListener(e -> {
			if (isNewStringEmpty(e)) { // end node selected
				destinationInformation.clearNode();
				setDestinationNode(null);
			} else {
				Node n = getNodeByName((String) e.getNewValue(), nodes);
				if (n != null) {
					setDestinationNode(n);
				}
			}
		});
		// Add PropertyChangeListener startPositionInput controle.
		startPositionInput.addPropertyChangeListener(e -> {
			if (isNewStringEmpty(e)) {
				originInformation.clearNode();
				setStartNode(null); // reset/clear pStartNode
			} else {
				Node n = getNodeByName((String) e.getNewValue(), nodes);
				if (n != null) {
					setStartNode(n);
				}
			}
		});
	}

	/**
	 * Call when ever the origin or destination node changes.
	 */
	void nodeChanged()
	{
		if(startNode!=null && destinationNode !=null)
		{
			System.out.println("Calculate path from \"" + startNode.getName() + "\" to \"" + destinationNode.getName() + "\".");
			//Start dijkstra
			IDijkstra dijkstra = new Dijkstra();
			ArrayList<Node> path = dijkstra.FindPath(startNode, destinationNode);
			pathListing.setPath(path);
			System.out.print("Path: ");
			for (int i = 0; i < path.size(); i++) {
				System.out.print(path.get(i).getName());
			}
			System.out.print("\n");
			map.setPath(path);
		}
	}

	/**
	 * Check if an property changed event, listening to a string attribute brings a new empty string.
	 * @param e PropertyChangedEvent, needs to listen to a string attribute!
	 * @return Boolean if the new string is empty.
	 */
	boolean isNewStringEmpty(PropertyChangeEvent e)
	{
		return e.getNewValue().equals("");
	}

	/**
	 * Finds a node in a list of node based on the node name.
	 * @param name Name of the node to find.
	 * @param nodes ArrayList<Node> where the node should be found in.
	 * @return The first node in the list with the given name.
	 */
	private Node getNodeByName(String name, ArrayList<Node> nodes) {
		for (Node n : nodes) {
			if (n.getName().equals(name))
				return n;
		}
		//Return null if the list does not contain the node.
		return null;
	}

	//Add a node to the center of the view
	public void addToCenter(javafx.scene.Node node) {
		primaryStackPane.getChildren().add(node);
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


	/**
	 * Set attribute nodes.
	 * @param nodes ArrayList<Node> to set nodes to.
	 */
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}


	/**
	 * Set the start node of the path.
	 * @param pStartNode Start node.
	 */
	public void setStartNode(Node pStartNode) {
		this.startNode = pStartNode;
		originInformation.setNode(pStartNode);
		System.out.println("Start node set: " + pStartNode.getName());
		nodeChanged();
	}

	/**
	 * Set the destinati node of the path.
	 * @param pEndNode Destination node
	 */
	public void setDestinationNode(Node pEndNode) {
		this.destinationNode = pEndNode;
		destinationInformation.setNode(pEndNode);
		System.out.println("Destination node set: " + pEndNode.getName());
		nodeChanged();
	}



}
