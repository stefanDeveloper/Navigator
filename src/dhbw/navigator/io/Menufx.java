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
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Input/Output Class
 * @author Stefan
 *
 */
public class Menufx {
	private Stage 						primaryStage;
	private TextField					startTextField; 
	private TextField					finishTextField;
	private StartNavigator 				startNavigator;
	private SortedSet<String> 			nameOfJunctions;
	private ArrayList<Node> 			nodes;
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
	public SortedSet<String> getNameOfJunctions() {
		return nameOfJunctions;
	}
	public void setNameOfJunctions(SortedSet<String> nameOfJunctions) {
		this.nameOfJunctions = nameOfJunctions;
	}
	public TextField getStartTextField() {
		return startTextField;
	}
	public void setStartTextField(TextField startTextField) {
		this.startTextField = startTextField;
	}
	public TextField getFinishTextField() {
		return finishTextField;
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

	
	/**
	 * Load MainWindow.fxml
	 */
	public void viewMainWindow(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("MainWindow.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			//Initialize Controller
			MainController mainController = loader.getController();
			mainController.setStage(this.primaryStage);
			mainController.setNodes(nodes);
			mainController.setMenufx(this);	
			
			
			this.startNavigator.getPrimaryBorder().setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load RouteWindow.fxml
	 */
	public void viewRouteWindow(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("RouteWindow.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			//Set Stage
			Stage stage = new Stage();
			stage.setResizable(false);
			//Initialize Controller
			RouteController routeController = loader.getController();
			routeController.setMenufx(this);
			this.finishTextField = routeController.getAimLabel();
			this.startTextField = routeController.getStartLabel();
			routeController.setStageRoute(stage);
			routeController.setNameOfJunctions(nameOfJunctions);
			
			stage.centerOnScreen();
			stage.initModality(Modality.WINDOW_MODAL);
			
			//Set Scene
			Scene scene = new Scene(pane);
            stage.setScene(scene);
			stage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
