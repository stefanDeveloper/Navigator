package dhbw.navigator.controles;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * SlideBar extends VBox
 *
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class SlideBarControle extends VBox {
	private double expandedSize;
	private boolean isExpanded;

	private PropertyChangeSupport changes = new PropertyChangeSupport( this );
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}


	public SlideBarControle(double expandedSize, final Button controlButton, Node... nodes) {
		setExpandedSize(expandedSize);
		setVisible(false);
		setPrefHeight(0);
		setMinHeight(0);
		setMinWidth(0);
		setPadding(new Insets(5,0,0,0));
		setSpacing(5);
		// Add nodes to the vbox
		getChildren().addAll(nodes);
		setStyle("-fx-background-color: #FFF;");

		if (controlButton.getProperties() != null) {
			// Set EventHanlder to controlButton, if it's clicked, VBox is
			// showing
			controlButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					// Create an animation to hide the panel.
					final Animation hidePanel = new Transition() {
						{
							setCycleDuration(Duration.millis(250));
						}

						@Override
						protected void interpolate(double frac) {
							// Set size after click
							final double size = getExpandedSize() * (1.0 - frac);
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
							setCycleDuration(Duration.millis(250));
						}

						@Override
						protected void interpolate(double frac) {
							// Set size after click
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
						// After animation show or hide panel
						if (SlideBarControle.this.isVisible()) {
							SlideBarControle.this.setVisible(false);

							isExpanded = false;
							hidePanel.play();
							setVisible(false);
						} else {
							SlideBarControle.this.setVisible(true);
							isExpanded = true;
							showPanel.play();
						}
						changes.firePropertyChange("isExpanded", !isExpanded, isExpanded);
					}
				}
			});
		}
	}

	/**
	 * @return the expandedSize
	 */
	public double getExpandedSize() {
		return expandedSize;
	}

	/**
	 * @param expandedSize
	 *            the expandedSize to set
	 */
	public void setExpandedSize(double expandedSize) {
		this.expandedSize = expandedSize;
	}

}