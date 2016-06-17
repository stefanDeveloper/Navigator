package io;

import java.io.IOException;
import com.application.views.RootLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import start.StartNavigator;

public class Menufx {
	private StartNavigator start;
	
	public void setStart(StartNavigator start) {
		this.start = start;
	}
	

	public void viewMainWindow(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("RootLayoutWindow.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			this.start.getPrimaryBorder().setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
