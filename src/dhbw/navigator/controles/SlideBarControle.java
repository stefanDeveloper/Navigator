package dhbw.navigator.controles;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SlideBarControle extends VBox {
	private double expandedSize;

	public SlideBarControle(double expandedSize, final Button controlButton, Node... nodes) {
		this.setExpandedSize(expandedSize);
		this.setVisible(false);
		this.setPrefHeight(0);
		this.setMinHeight(0);
		this.setMinWidth(0);
		setPadding(new Insets(5,0,0,0));
		setSpacing(5);
		// Add nodes in the vbox
		this.getChildren().addAll(nodes);
		setStyle("-fx-background-color: #FFF;");

		if (controlButton.getProperties() != null) {

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
							final double size = SlideBarControle.this.getExpandedSize() * (1.0 - frac);
							SlideBarControle.this.setPrefWidth(size);
						}
					};
					//((BorderPane)getParent()).getLeft().setVisible(false);
					hidePanel.onFinishedProperty().set(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent actionEvent) {
							SlideBarControle.this.setVisible(false);
						}
					});

					// Create an animation to show the panel.
					final Animation showPanel = new Transition() {
						{
							this.setCycleDuration(Duration.millis(250));
						}

						@Override
						protected void interpolate(double frac) {
							final double size = SlideBarControle.this.getExpandedSize() * frac;
							SlideBarControle.this.setPrefWidth(size);
						}
					};

					showPanel.onFinishedProperty().set(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent actionEvent) {
						}
					});

					if (showPanel.statusProperty().get() == Animation.Status.STOPPED
							&& hidePanel.statusProperty().get() == Animation.Status.STOPPED) {

						if (SlideBarControle.this.isVisible()) {
							SlideBarControle.this.setVisible(false);

							hidePanel.play();

							setVisible(false);
						} else {
							SlideBarControle.this.setVisible(true);
							showPanel.play();
						}
					}
				}
			});
		}
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