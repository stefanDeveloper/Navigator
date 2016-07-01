package dhbw.navigator.views;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import dhbw.navigator.controles.MapControle;
import dhbw.navigator.controles.SlideBarControle;
import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.models.Node;
import dhbw.navigator.start.StartNavigator;
import dhbw.navigator.utility.UtilityViews;
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
	private SlideBarControle FlapBar;
	private StartNavigator start;
	private SideBarController sideBar;
	@FXML
	private StackPane primaryStackPane;
	@FXML
	private Button region;

	public SideBarController getSideBar() {
		return this.sideBar;
	}

	public void setSideBar(SideBarController sideBar) {
		this.sideBar = sideBar;
	}

	public StartNavigator getStart() {
		return this.start;
	}

	public void setStart(StartNavigator start) {
		this.start = start;
	}

	private void loadData(boolean parseData) {
		IParser parser = new Parser();
		SortedSet<String> namesOfJunctions = new TreeSet<>();
		ArrayList<Node> node;
		if (parseData) {
			Osm data = (Osm) parser.parseFile(new File("Testdata/germany.xml"));
			node = parser.getNodes(data);
			parser.serialize(node);
		} else {
			node = parser.deserialize();
		}

		for (Node n : node) {
			if (n.getIsJunction() == true)
				namesOfJunctions.add(n.getName());
		}
		this.primaryStackPane.getChildren().clear();
		MapControle map = new MapControle(node, false);
		this.sideBar.setNameOfJunctions(namesOfJunctions);
		this.addToCenter(map);
	}

	@FXML
	public void initialize() {
		this.loadData(false);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				RootLayoutController.this.FlapBar = new SlideBarControle(300, RootLayoutController.this.region,
						RootLayoutController.this.start.viewRouteWindow());
				RootLayoutController.this.start.getPrimaryBorder().setLeft(RootLayoutController.this.FlapBar);
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
}
