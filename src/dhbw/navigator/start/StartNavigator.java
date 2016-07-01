package dhbw.navigator.start;

import java.io.IOException;
import java.util.SortedSet;

import dhbw.navigator.views.RootLayoutController;
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
	private SortedSet<String> nameOfJunctions;
	private RootLayoutController root;

	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	public BorderPane getPrimaryBorder() {
		return this.primaryBorder;
	}

	public RootLayoutController getRoot() {
		return this.root;
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
			this.root = loader.getController();
			this.root.setStart(this);

			Scene scene = new Scene(this.primaryBorder);
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
