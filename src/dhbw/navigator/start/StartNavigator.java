package dhbw.navigator.start;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dhbw.navigator.views.RootLayoutController;
import dhbw.navigator.views.SideBarController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Start Application
 * 
 * @author Stefan
 *
 */
public class StartNavigator extends Application {
	private Stage primaryStage;
	private BorderPane primaryBorder;
	private List<String> namesOfJunctions;
	private RootLayoutController rootLayout;
	private SideBarController sideBarController;

	public List<String> getNamesOfJunctions() {
		return this.namesOfJunctions;
	}

	public void setNamesOfJunctions(List<String> namesOfJunctions) {
		this.namesOfJunctions = namesOfJunctions;
	}

	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	public BorderPane getPrimaryBorder() {
		return this.primaryBorder;
	}

	@Override
	public void start(Stage primaryStage) {
		// Initialize Stage
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Navigator");
		// Size
		this.primaryStage.setMinHeight(720);
		this.primaryStage.setMinWidth(680);
		// Load Views
		this.namesOfJunctions = new ArrayList<>();

		this.rootLayout();

	}

	/**
	 * Load BorderPane
	 */
	public void rootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("RootLayoutWindow.fxml"));
			this.primaryBorder = (BorderPane) loader.load();

			// Initialize Controller
			this.rootLayout = loader.getController();
			this.rootLayout.setStart(this);
			this.rootLayout.setNamesOfJunctions(this.namesOfJunctions);

			// Initialize Scene
			Scene scene = new Scene(this.primaryBorder);
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
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
			loader.setLocation(RootLayoutController.class.getResource("SideBarWindow.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();

			// Initialize Controller
			this.sideBarController = loader.getController();
			this.sideBarController.setNode(pane);
			this.sideBarController.setNameOfJunctions(this.namesOfJunctions);

			return pane;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
