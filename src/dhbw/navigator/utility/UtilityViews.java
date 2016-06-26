package dhbw.navigator.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.StageStyle;

public class UtilityViews {
	

	
	
	public static void Error(String error){
		Alert alert = new Alert(AlertType.ERROR);
//		alert.getDialogPane().getScene().getStylesheets().add("com/tool/views/application.css");
		alert.setTitle("Error");
		alert.setContentText(error);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.showAndWait();
	}
	
	public static void Information(String error){
		Alert alert = new Alert(AlertType.INFORMATION);
//		alert.getDialogPane().getScene().getStylesheets().add("com/tool/views/application.css");
		alert.setTitle("Error");
		alert.setContentText(error);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.showAndWait();
	}
	
	public static void Message(String title, String message){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.showAndWait();
		
	}
	
	public static void LoadingBar(int time){
		ProgressIndicator[] pins = new ProgressIndicator[time];
		for (int i = 0; i < time; i++) {
            final ProgressIndicator pin = pins[i] = new ProgressIndicator();
            pin.setProgress(i);
        }		
	}

}
