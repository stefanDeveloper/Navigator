package dhbw.navigator.controles;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImageButtonControle extends Button {
	private final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;";
	private final String STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 6 4 4 6;";

	/**
	 * Button, which is an Image. Fire an event if mouse is clicked on Image
	 * 
	 * @param imageurl
	 *            URL of the Image
	 * @param height
	 * @param width
	 */
	public ImageButtonControle(String imageurl) {
		// Set ImageView
		ImageView image = new ImageView(new Image(imageurl));
		// Set Graphic of Button
		setGraphic(image);
		
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
