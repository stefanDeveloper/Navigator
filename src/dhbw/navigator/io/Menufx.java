package dhbw.navigator.io;

import java.io.IOException;

import dhbw.navigator.start.StartNavigator;
import dhbw.navigator.views.InfoViewController;
import dhbw.navigator.views.MainController;
import dhbw.navigator.views.RootLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Menufx {
	private StartNavigator startNavigator;
	
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
	
	public void viewInfoLabel(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(InfoViewController.class.getResource("InfoViewWindow.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			Stage infoStage = new Stage();
			infoStage.setTitle("Info");
			infoStage.setResizable(false);
			infoStage.initModality(Modality.WINDOW_MODAL);
            infoStage.initOwner(this.startNavigator.getPrimaryStage());
            Scene scene = new Scene(pane);
            infoStage.setScene(scene);

            InfoViewController controller = loader.getController();
            controller.setStage(infoStage);

            infoStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
