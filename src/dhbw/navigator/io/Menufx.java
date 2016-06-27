package dhbw.navigator.io;

import java.io.IOException;

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

public class Menufx {
	private Stage 						primaryStage;
	private TextField					startTextField; 
	private TextField					finishTextField;
	private StartNavigator 				startNavigator;
		
	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	

	public void setStartNavigator(StartNavigator startNavigator) {
		this.startNavigator = startNavigator;
	}


	public void viewMainWindow(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("MainWindow.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			MainController mainController = loader.getController();
			mainController.setMenufx(this);
			mainController.setStage(this.primaryStage);		
			
			
			this.startNavigator.getPrimaryBorder().setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void viewRouteWindow(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("RouteWindow.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			Stage stage = new Stage();
			stage.setResizable(false);
			RouteController routeController = loader.getController();
			routeController.setMenufx(this);
			this.finishTextField = routeController.getAimLabel();
			this.startTextField = routeController.getStartLabel();
			routeController.setStageRoute(stage);
			
			stage.centerOnScreen();
			stage.initModality(Modality.WINDOW_MODAL);
			
			Scene scene = new Scene(pane);
            stage.setScene(scene);
			stage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
