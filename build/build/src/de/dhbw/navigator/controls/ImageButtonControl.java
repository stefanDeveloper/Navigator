package de.dhbw.navigator.controls;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Button which contains an image
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class ImageButtonControl extends Button {
	private final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;";
	private final String STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 6 4 4 6;";

	/**
	 * Button, which is an Image. Fire an event if mouse is clicked on Image
	 * 
	 * @param imageurl
	 *            URL of the Image
	 */
	public ImageButtonControl(String imageurl) {
		// Set Graphic of Button
		setGraphic(new ImageView(new Image(imageurl)));
		setStyle(STYLE_NORMAL);
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setStyle(STYLE_PRESSED);
			}
		});

		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setStyle(STYLE_NORMAL);
			}

		});
	}
}
