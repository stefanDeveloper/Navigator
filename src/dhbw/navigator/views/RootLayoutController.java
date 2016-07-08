package dhbw.navigator.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import dhbw.navigator.controles.*;
import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Dijkstra;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IDijkstra;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.models.Edge;
import dhbw.navigator.models.Node;
import dhbw.navigator.start.StartNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.StackPane;
import net.aksingh.owmjapis.CurrentWeather;

/**
 * Controller for RootLayout.fxml
 * 
 * @author Stefan
 *
 */
public class RootLayoutController implements PropertyChangeListener {
	private SlideBarControle flapBar;
	private AutoCompleteControle startPositionInput = new AutoCompleteControle("Start");
	private AutoCompleteControle destinationPositionInput = new AutoCompleteControle("Ziel");
	private Node startNode;
	private Node endNode;
	private NodeInformationControle originInformation = new NodeInformationControle();
	private NodeInformationControle destinationInformation = new NodeInformationControle();
	private PathListingControle pathListing = new PathListingControle();
	private Button startButton = new Button("Start");


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
		startPositionInput.setNamesOfJunctions(namesOfJunctions);
		destinationPositionInput.setNamesOfJunctions(namesOfJunctions);

		//Populate the view
		this.primaryStackPane.getChildren().clear();
		MapControle map = new MapControle(nodes, false);
		this.addToCenter(map);
	}

	@FXML
	public void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//Muss in anderne Thread ausgelagert werden
				flapBar = new SlideBarControle(325, region,
						startPositionInput,
						destinationPositionInput,
						new Separator(),
						originInformation,
						destinationInformation,
						pathListing);
				start.getPrimaryBorder().setLeft(flapBar);
				loadData(false);
				region.fire();
			}
		});


		destinationPositionInput.addPropertyChangeListener("text", e -> {
			if(e.getNewValue().equals(""))
			{
				destinationInformation.setNode(null);
				setEndNode(null);
			}
			else
			{
				System.out.println("Ziel: " +e.getNewValue());
				Node n = getNodeByName((String)e.getNewValue(), nodes);
				if(n!=null) {
					setEndNode(n);
				}
			}
        });
		startPositionInput.addPropertyChangeListener("text", e -> {
			if(e.getNewValue().equals(""))
			{
				originInformation.setNode(null);
				setStartNode(null);
			}
			else
			{
				System.out.println("Start: " +e.getNewValue());
				Node n = getNodeByName((String)e.getNewValue(), nodes);
				if(n!=null)
				{
					setStartNode(n);

				}
			}
		});
	}

	private Node getNodeByName(String name, ArrayList<Node> nodes)
	{
		for(Node n: nodes)
		{
			if(n.getName().equals(name)) return n;
		}
		return null;
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
