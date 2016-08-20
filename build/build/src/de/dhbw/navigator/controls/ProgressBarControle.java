package de.dhbw.navigator.controls;

import de.dhbw.navigator.start.StartNavigator;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * ProgressBar for Loading
 *
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class ProgressBarControle extends BorderPane {
	private ProgressBar pBar;
	private StartNavigator startNav;
	private SlideBarControl slideBar;
	private Label infoLabel;

	/**
	 * ProgressBar which run on a separated thread. 
	 * @param startNav
	 * @param slideBar
	 */
	@SuppressWarnings("static-access")
	public ProgressBarControle(StartNavigator startNav, SlideBarControl slideBar) {
		//Initialize ProgressBar
		pBar = new ProgressBar();
		pBar.setPrefWidth(startNav.getPrimaryStage().getWidth());
		setStyle("-fx-background-color:  rgba(255, 255, 255, 0.5)");
		this.slideBar = slideBar;
		this.startNav = startNav;

		infoLabel = new Label("Extract Nodes");
		int [] heights = new int [] {5, 5, 90};
		GridPane gridPane = new GridPane();
		pBar.setPadding(new Insets(10));
		for (int i = 0; i < 3; i++) {
			RowConstraints rc = new RowConstraints();
			rc.setPercentHeight(heights[i]);
			gridPane.getRowConstraints().add(rc);
		}

		BorderPane bp = new BorderPane();
		BorderPane shadowPane = new BorderPane();
		gridPane.add(shadowPane, 0,0);
		bp.setCenter(infoLabel);
		bp.setAlignment(infoLabel,Pos.CENTER);
		gridPane.add(bp, 0, 1);
		shadowPane.setStyle("-fx-background-color:  rgba(255, 255, 255, 0.9)");
		gridPane.setRowSpan(shadowPane, 2);
		gridPane.add(pBar, 0,0);
		gridPane.setAlignment(Pos.CENTER);



		setTop(gridPane);

		startNav.getPrimaryStage().widthProperty()
				.addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {
					double value = (double) newValue;
					pBar.setPrefWidth(value);
					infoLabel.setPrefWidth(value);
				});
	}

	boolean lastState = false;

	public void show(){
		lastState = slideBar.isExpanded();
		if (lastState)
		   startNav.getRoot().getToggleButton().fire();
		startNav.getRoot().getHelp().setDisable(true);
		startNav.getRoot().getLoadFile().setDisable(true);
		startNav.getRoot().addToCenter(this);
		System.out.println("SHOW");
	}

	public void hide(){
		System.out.println("Load Thread finished");
		startNav.getRoot().removeFromCenter(this);
		startNav.getRoot().getHelp().setDisable(false);
		startNav.getRoot().getLoadFile().setDisable(false);
		if(lastState) {
		//	startNav.getRoot().getToggleButton().fire();
		}
		System.out.println("HIDE");
	}

	public void setProgress(double progress){
		if(progress>=0 && progress <=100)
			pBar.setProgress(progress);
		else pBar.setProgress(-1);
	}

	public void setInfoLabel(String info){
		infoLabel.setText(info);
	}
}
