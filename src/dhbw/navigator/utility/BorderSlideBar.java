package dhbw.navigator.utility;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Animates a node on and off screen to the top, right, bottom or left side.
 */
public class BorderSlideBar extends VBox {
	private double expandedSize;

	/**
	 * Creates a sidebar panel in a BorderPane, containing an horizontal
	 * alignment of the given nodes.
	 *
	 * <pre>
	 * <code>
	 *  Example:
	 *
	 *  BorderSlideBar topFlapBar = new BorderSlideBar(
	 *                  100, button, Pos.TOP_LEFT, new contentController());
	 *  mainBorderPane.setTop(topFlapBar);
	 * </code>
	 * </pre>
	 *
	 * @param expandedSize
	 *            The size of the panel.
	 * @param controlButton
	 *            The button responsible to open/close slide bar.
	 * @param location
	 *            The location of the panel (TOP_LEFT, BOTTOM_LEFT,
	 *            BASELINE_RIGHT, BASELINE_LEFT).
	 * @param nodes
	 *            Nodes inside the panel.
	 */
	public BorderSlideBar(double expandedSize, final Button controlButton, Node... nodes) {
		this.setExpandedSize(expandedSize);
		this.setVisible(false);

		this.setPrefHeight(0);
		this.setMinHeight(0);
		// Add nodes in the vbox
		this.getChildren().addAll(nodes);

		controlButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				// Create an animation to hide the panel.
				final Animation hidePanel = new Transition() {
					{
						this.setCycleDuration(Duration.millis(250));
					}

					@Override
					protected void interpolate(double frac) {
						final double size = BorderSlideBar.this.getExpandedSize() * (1.0 - frac);
						BorderSlideBar.this.setPrefWidth(size);
					}
				};

				hidePanel.onFinishedProperty().set(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						BorderSlideBar.this.setVisible(false);
					}
				});

				// Create an animation to show the panel.
				final Animation showPanel = new Transition() {
					{
						this.setCycleDuration(Duration.millis(250));
					}

					@Override
					protected void interpolate(double frac) {
						final double size = BorderSlideBar.this.getExpandedSize() * frac;
						BorderSlideBar.this.setPrefWidth(size);
					}
				};

				showPanel.onFinishedProperty().set(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
					}
				});

				if (showPanel.statusProperty().get() == Animation.Status.STOPPED
						&& hidePanel.statusProperty().get() == Animation.Status.STOPPED) {

					if (BorderSlideBar.this.isVisible()) {
						hidePanel.play();

					} else {
						BorderSlideBar.this.setVisible(true);
						showPanel.play();
					}
				}
			}
		});
	}

	/**
	 * @return the expandedSize
	 */
	public double getExpandedSize() {
		return this.expandedSize;
	}

	/**
	 * @param expandedSize
	 *            the expandedSize to set
	 */
	public void setExpandedSize(double expandedSize) {
		this.expandedSize = expandedSize;
	}

}