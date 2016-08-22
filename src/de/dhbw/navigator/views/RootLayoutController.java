package de.dhbw.navigator.views;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import de.dhbw.navigator.controls.AutoCompleteControl;
import de.dhbw.navigator.controls.ImageButtonControl;
import de.dhbw.navigator.controls.MapControl;
import de.dhbw.navigator.controls.NavigationControl;
import de.dhbw.navigator.controls.NodeInformationControl;
import de.dhbw.navigator.controls.PathListingControl;
import de.dhbw.navigator.controls.ProgressBarControle;
import de.dhbw.navigator.controls.RotationImageViewControl;
import de.dhbw.navigator.controls.SlideBarControl;
import de.dhbw.navigator.controls.SwitchButtonControl;
import de.dhbw.navigator.controls.ToggleButtonControl;
import de.dhbw.navigator.implementation.Dijkstra;
import de.dhbw.navigator.implementation.LoadData;
import de.dhbw.navigator.implementation.NavigationAlgorithm;
import de.dhbw.navigator.models.Node;
import de.dhbw.navigator.start.StartNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

/**
 * Controller for RootLayoutWindow.fxml
 *
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class RootLayoutController {
	private SlideBarControl slideBar;
	private AutoCompleteControl startPositionInput = new AutoCompleteControl("Start");
	private AutoCompleteControl destinationPositionInput = new AutoCompleteControl("Ziel");
	private Node startNode;
	private Node destinationNode;
	private NodeInformationControl originInformation = new NodeInformationControl(90);
	private NodeInformationControl destinationInformation = new NodeInformationControl(90);
	private PathListingControl pathListing = new PathListingControl();
	private MapControl map = new MapControl();
	private SwitchButtonControl switchButton;
	private String serialiseFilePath = "resources/serializeData/map.ser";
	private String xmlFilePath = "resources/data/export.xml";
	private StartNavigator start;
	private ArrayList<Node> nodes;
	private ToggleButtonControl toggleButton;
	private NavigationControl navigationControl;
	private BorderPane layoutPane;
	private RotationImageViewControl rotateView;
	private Thread loadThread;
	private Thread rotateThread  = new Thread();
	private static final class Lock {}
	public static final Object lock = new Lock();
	private ProgressBarControle pBar;
	@FXML
	private StackPane primaryStackPane;
	@FXML
	private Menu loadFile;
	@FXML
	private Menu help;
	@FXML
	private MenuItem info;
	@FXML
	private MenuItem sync;
	@FXML
	private MenuItem close;

	public Thread getRotateThread() {
		return rotateThread;
	}

	public StartNavigator getStart() {
		return start;
	}

	public void setStart(StartNavigator start) {
		this.start = start;
	}

	public AutoCompleteControl getStartPositionInput() {
		return startPositionInput;
	}

	public void setStartPositionInput(AutoCompleteControl startPositionInput) {
		this.startPositionInput = startPositionInput;
	}

	public AutoCompleteControl getDestinationPositionInput() {
		return destinationPositionInput;
	}

	public void setDestinationPositionInput(AutoCompleteControl destinationPositionInput) {
		this.destinationPositionInput = destinationPositionInput;
	}

	public MapControl getMap() {
		return map;
	}

	public void setMap(MapControl map) {
		this.map = map;
	}

	public String getSerialiseFilePath() {
		return serialiseFilePath;
	}

	public void setSerialiseFilePath(String serialiseFilePath) {
		this.serialiseFilePath = serialiseFilePath;
	}

	public String getXmlFilePath() {
		return xmlFilePath;
	}

	public void setXmlFilePath(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}

	public NavigationControl getNavigationControl() {
		return navigationControl;
	}

	public ToggleButtonControl getToggleButton() {
		return toggleButton;
	}

	public Menu getLoadFile() {
		return loadFile;
	}

	public Menu getHelp() {
		return help;
	}

	@FXML
	public void initialize() {
		// Set ImageButton
		ImageButtonControl imageButton = new ImageButtonControl("File:resources/images/sync.png");
		help.setGraphic(new ImageView(new Image("File:resources/images/info.png")));
		// Set Graphic to Menu
		loadFile.setGraphic(imageButton);
		// Handle Click and show Tooltip
		Object a = loadFile.getStyle();
		rotateView = new RotationImageViewControl(imageButton);
		imageButton.setOnMouseClicked(event -> {
			handleLoadData();
		});
		// Set Style if mouse entered graphic of the menu
		imageButton.setOnMouseEntered(e -> loadFile.setStyle((String) a));
		// Reset focus
		imageButton.setOnMouseExited(e -> loadFile.setStyle("-fx-background-color: transparent;"));
		// Set Tooltip
		imageButton.setTooltip(new Tooltip("Lade Datei"));
		// Set ID

		Platform.runLater(new Runnable() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				switchButton = new SwitchButtonControl(startPositionInput, destinationPositionInput);
				toggleButton = new ToggleButtonControl();
				// Set Id's for UI Test
				switchButton.setId("switch");
				toggleButton.setId("toggle");
				destinationPositionInput.getTextField().setId("destination");
				startPositionInput.getTextField().setId("start");
				primaryStackPane.setAlignment(toggleButton, Pos.CENTER_LEFT);
				navigationControl = new NavigationControl(map);
				primaryStackPane.setAlignment(navigationControl, Pos.BOTTOM_RIGHT);
				addToCenter(map);
				addToCenter(navigationControl);
				addToCenter(toggleButton);
				layoutPane = new BorderPane();
				layoutPane.setCenter(switchButton);
				layoutPane.setAlignment(switchButton, Pos.CENTER);
				slideBar = new SlideBarControl(325, toggleButton, new Label(" Routeneingabe"), startPositionInput,
						layoutPane, destinationPositionInput, originInformation, destinationInformation, pathListing);
				// Set ChangeListener, check if slideBar is expanded or not and
				// change text, tooltip

				toggleButton.setChangeListener(slideBar);
				toggleButton.fire();
				start.getPrimaryBorder().setLeft(slideBar);
				pBar = new ProgressBarControle(getStart(), slideBar);
				File file = new File(serialiseFilePath);
				// Load File if serialize file not exists
				load(!file.exists());

				// addToCenter(pBar);

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


	void load(boolean ParseData){
		ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
		ListenableFuture<ArrayList<Node>> explosion = service.submit(new Callable<ArrayList<Node>>() {
			public ArrayList<Node> call() {
				rotateThread = new Thread();
				rotateThread.start();
				LoadData ld = new LoadData();
				ArrayList<Node> nodes = ld.load(ParseData,xmlFilePath, serialiseFilePath, pBar);
				//rotateThread.stop();
				return nodes;
			}
		});

		Futures.addCallback(explosion, new FutureCallback<ArrayList<Node>>() {
			// we want this handler to run immediately after we push the big red button!
			public void onSuccess(ArrayList<Node> explosion) {
				nodes = explosion;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						SortedSet<String> namesOfJunctions = new TreeSet<>();
						for (Node n : nodes) {
							if (n.getIsJunction() && n.getName() != null)
								// Add names of Junction for Context Menu
								namesOfJunctions.add(n.getName());
						}
						getStartPositionInput().setNamesOfJunctions(namesOfJunctions);
						getDestinationPositionInput().setNamesOfJunctions(namesOfJunctions);
						getMap().setOriginMap(nodes);
					}
				});
				rotateView.stopRotation();
			}
			public void onFailure(Throwable thrown) {
				System.out.println("ERROR");
				rotateView.stopRotation();
			}
		});
	}


	public void load3(Boolean parseData) {
		startNode = null;
		destinationNode = null;
		//LoadData ld = new LoadData(parseData, this);
		//loadThread = new Thread(ld);
		//loadThread.start();
	}

	/**
	 * Call when ever the origin or destination node changes.
	 */
	void nodeChanged() {
		if (startNode == null && destinationNode == null) {
		} else if (startNode == null) {
		} else if (destinationNode == null) {
		} else if (destinationNode == startNode) {
		} else {
			System.out.println(
					"Calculate path from \"" + startNode.getName() + "\" to \"" + destinationNode.getName() + "\".");
			// Start dijkstra
			NavigationAlgorithm dijkstra = new Dijkstra();
			ArrayList<Node> path = dijkstra.FindPath(startNode, destinationNode);
			pathListing.setPath(path);
			System.out.print("Path: ");
			for (int i = 0; i < path.size(); i++) {
				System.out.println(path.get(i).getName());
			}
			System.out.print("\n");
			map.setPath(path);
		}
	}

	/**
	 * Check if an property changed event, listening to a string attribute
	 * brings a new empty string.
	 * 
	 * 
	 * @param e
	 *            PropertyChangedEvent, needs to listen to a string attribute!
	 * @return Boolean if the new string is empty.
	 */
	boolean isNewStringEmpty(PropertyChangeEvent e) {
		return e.getNewValue().equals("");
	}

	/**
	 * Finds a node in a list of node based on the node name.
	 * 
	 * @param name
	 *            Name of the node to find.
	 * @param nodes
	 *            ArrayList<Node> where the node should be found in.
	 * @return The first node in the list with the given name.
	 */
	private Node getNodeByName(String name, ArrayList<Node> nodes) {
		for (Node n : nodes) {
			if (n.getName() != null && n.getName().equals(name))
				return n;
		}
		// Return null if the list does not contain the node.
		return null;
	}

	// Add a node to the center of the view
	public void addToCenter(javafx.scene.Node node) {
		primaryStackPane.getChildren().add(node);
	}

	// Add a node to the center of the view
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
				+ "Manuela Leopold\n" + "Markus Menrath\n" + "Stefan Machmeier\n" + "Konrad Müller");
	}

	/**
	 * Load Data again
	 */
	@FXML
	private void handleSync() {
		load(true);
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	/**
	 * Set attribute nodes.
	 * 
	 * @param nodes
	 *            ArrayList<Node> to set nodes to.
	 */
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Set the start node of the serialiseFilePath.
	 * 
	 * @param pStartNode
	 *            Start node.
	 */
	public void setStartNode(Node pStartNode) {
		startNode = pStartNode;
		if (pStartNode == null) {
			originInformation.clearNode();
		} else {
			originInformation.setNode(pStartNode);
			map.setStart(startNode);
			System.out.println("Start node set: " + pStartNode.getName());
			nodeChanged();
		}

	}

	/**
	 * Set the destinati node of the serialiseFilePath.
	 * 
	 * @param pEndNode
	 *            Destination node
	 */
	public void setDestinationNode(Node pEndNode) {
		destinationNode = pEndNode;
		if (pEndNode == null) {
			destinationInformation.clearNode();
		} else {
			destinationInformation.setNode(pEndNode);
			map.setDestinationNode(destinationNode);
			System.out.println("Destination node set: " + pEndNode.getName());
			nodeChanged();
		}
	}

	/**
	 * Open a FileChooser to select the Data, which will be parsed and draw
	 * 
	 * @throws InterruptedException
	 */
	private void handleLoadData() {
		// Add FileChooser with xml filter an directory
		rotateThread = new Thread();
		rotateView.startRotation();
		System.out.println("Load Data");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(xmlFilePath).getParentFile());
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Xml files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(start.getPrimaryStage());
		if (file != null) {
			xmlFilePath = file.getPath();
			System.out.println("Data is " + file.getPath());
			// Initialize Thread and start
			load(true);
			
		} else {
			rotateView.stopRotation();
		}
	}

}
