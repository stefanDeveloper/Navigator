package dhbw.navigator.io;

import java.io.IOException;

import dhbw.navigator.start.StartNavigator;
import dhbw.navigator.views.MainController;
import dhbw.navigator.views.RootLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

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
	
	public void drawMap(){
		
		
	}
}
