package dhbw.navigator.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedSet;

import dhbw.navigator.models.Node;
import dhbw.navigator.start.StartNavigator;
import dhbw.navigator.views.MainController;
import dhbw.navigator.views.RootLayoutController;
import dhbw.navigator.views.RouteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Input/Output Class
 * 
 * @author Stefan
 *
 */
public class Menufx {
	private Stage primaryStage;
	private TextField startTextField;
	private TextField finishTextField;
	private StartNavigator startNavigator;
	private SortedSet<String> nameOfJunctions;
	private ArrayList<Node> nodes;
	private MainController mainController;
	private RouteController routeController;

	public ArrayList<Node> getNodes() {
		return this.nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public SortedSet<String> getNameOfJunctions() {
		return this.nameOfJunctions;
	}

	public void setNameOfJunctions(SortedSet<String> nameOfJunctions) {
		this.nameOfJunctions = nameOfJunctions;
	}

	public TextField getStartTextField() {
		return this.startTextField;
	}

	public void setStartTextField(TextField startTextField) {
		this.startTextField = startTextField;
	}

	public TextField getFinishTextField() {
		return this.finishTextField;
	}

	public void setFinishTextField(TextField finishTextField) {
		this.finishTextField = finishTextField;
	}

	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setStartNavigator(StartNavigator startNavigator) {
		this.startNavigator = startNavigator;
	}

	public StartNavigator getStartNavigator() {
		return this.startNavigator;
	}

	/**
	 * Load MainWindow.fxml
	 */
	public void viewMainWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("MainWindow.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();

			// Initialize Controller
			this.mainController = loader.getController();
			this.mainController.setStage(this.primaryStage);
			this.mainController.setNodes(this.nodes);
			this.mainController.setMenufx(this);

			this.startNavigator.getRoot().addToCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load RouteWindow.fxml
	 */
	public AnchorPane viewRouteWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("RouteWindow.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();

			// Initialize Controller
			this.routeController = loader.getController();
			this.routeController.setMenufx(this);
			this.routeController.setNode(pane);
			this.routeController.setNameOfJunctions(this.nameOfJunctions);
			this.finishTextField = this.routeController.getAimLabel();
			this.startTextField = this.routeController.getStartLabel();

			// this.startNavigator.getRoot().addToCenter(pane);
			return pane;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
