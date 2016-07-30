package dhbw.navigator.start;

import java.io.IOException;

import dhbw.navigator.views.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Start Application
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class StartNavigator extends Application {
	private Stage primaryStage;
	private BorderPane primaryBorder;
	private RootLayoutController root;

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public BorderPane getPrimaryBorder() {
		return primaryBorder;
	}

	public RootLayoutController getRoot() {
		return root;
	}

	@Override
	public void start(Stage primaryStage) {
		// Initialize Stage
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Navigator");

		// Set Size
		this.primaryStage.setMinHeight(720);
		this.primaryStage.setMinWidth(680);

		// Set Icon
		this.primaryStage.getIcons().add(new Image("file:resources/images/icon.jpg"));

		// Load Views
		rootLayout();

	}

	/**
	 * Load BorderPane
	 */
	public void rootLayout() {
		try {
			// Load FXML data
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("RootLayoutWindow.fxml"));
			// Set primary border
			primaryBorder = (BorderPane) loader.load();
			// Set controller
			root = loader.getController();
			root.setStart(this);

			// Set scene to primary stage (window)
			Scene scene = new Scene(primaryBorder);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
