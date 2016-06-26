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
	private StartNavigator startNavigator;
	private TextField startLabel; 
	private TextField aimLabel;
	
	public void setStartNavigator(StartNavigator startNavigator) {
		this.startNavigator = startNavigator;
	}
	

	public void viewMainWindow(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("MainWindow.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			MainController menuController = loader.getController();
			menuController.setMenufx(this);
			
			
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
			
			RouteController routeController = loader.getController();
			routeController.setMenufx(this);
			routeController.setAimLabel(this.aimLabel);
			routeController.setStartLabel(this.startLabel);
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
