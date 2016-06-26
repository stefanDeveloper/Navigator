package dhbw.navigator.utility;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
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
	
	 public static void runTask(int time) {
	        final double wndwWidth = 300.0d;
	        Label updateLabel = new Label("Running tasks...");
	        updateLabel.setPrefWidth(wndwWidth);
	        ProgressBar progress = new ProgressBar();
	        progress.setPrefWidth(wndwWidth);

	        VBox updatePane = new VBox();
	        updatePane.setPadding(new Insets(10));
	        updatePane.setSpacing(5.0d);
	        updatePane.getChildren().addAll(updateLabel, progress);

	        Stage taskUpdateStage = new Stage(StageStyle.UNIFIED);
	        taskUpdateStage.setScene(new Scene(updatePane));
	        taskUpdateStage.show();

	        Task<Void> longTask = new Task<Void>() {
	            @Override
	            protected Void call() throws Exception {
	                for (int i = 1; i <= time; i++) {
	                    if (isCancelled()) {
	                        break;
	                    }
	                    updateProgress(i, time);
	                    updateMessage("Task part " + String.valueOf(i) + " complete");

	                    Thread.sleep(100);
	                }
	                return null;
	            }
	        };

	        longTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	            @Override
	            public void handle(WorkerStateEvent t) {
	                taskUpdateStage.hide();
	            }
	        });
	        progress.progressProperty().bind(longTask.progressProperty());
	        updateLabel.textProperty().bind(longTask.messageProperty());

	        taskUpdateStage.show();
	        new Thread(longTask).start();
	    }

}
