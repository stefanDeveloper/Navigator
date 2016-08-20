package de.dhbw.navigator.views;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Utility Views
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class UtilityViews {
	/**
	 * Error View
	 * 
	 * @param error
	 */
	public static void Error(String error) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.getDialogPane().getScene().getStylesheets().add("de/dhbw/navigator/views/application.css");
		alert.setTitle("Error");
		alert.setContentText(error);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.showAndWait();
	}

	/**
	 * Information View
	 * 
	 * @param error
	 */
	public static void Information(String error) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().getScene().getStylesheets().add("de/dhbw/navigator/views/application.css");
		alert.setTitle("Error");
		alert.setContentText(error);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.showAndWait();
	}
	/**
	 * Set Message
	 * @param title
	 * String
	 * @param message
	 * String 
	 */
	public static void Message(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.setGraphic(new ImageView(new Image("file:resources/")));
		alert.showAndWait();

	}

	/**
	 * Set Message
	 * @param title
	 * String
	 */
	public static void CatError(String title) {
		Dialog alert = new Dialog();
		alert.setTitle("");
		alert.setContentText("");
		//alert.setHeaderText(title);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.getDialogPane().setStyle("-fx-background-color: white");
		ImageView iv = new ImageView(new Image("file:resources/images/weatherCat.jpg"));
		iv.setFitHeight(250);
		iv.setFitWidth(450);
		VBox content = new VBox();
		Label info = new Label(title);
		info.setStyle("-fx-font-size: 16pt;\n" +
				"    -fx-font-family: \"Segoe UI Semibold\";\n" +
				"    -fx-text-fill: black;\n" +
				"    -fx-opacity: 1;");
		content.getChildren().addAll(info, iv);
		alert.getDialogPane().setContent(content);
		alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
		alert.showAndWait();

	}
}
