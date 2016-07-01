package dhbw.navigator.views;

import java.util.SortedSet;

import dhbw.navigator.controles.AutoCompleteControle;
import dhbw.navigator.utility.UtilityViews;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controller for RouteWindow.fxml
 * 
 * @author Stefan
 *
 */
public class SideBarController {
	private SortedSet<String> nameOfJunctions;
	private Node node;
	@FXML
	private Button okButton;
	@FXML
	private Label cancelButton;
	private AutoCompleteControle start;
	private AutoCompleteControle finish;
	@FXML
	private Button startButton;
	@FXML
	private Button finishButton;
	@FXML
	private VBox vBox;

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public SortedSet<String> getNameOfJunctions() {
		return this.nameOfJunctions;
	}

	public void setNameOfJunctions(SortedSet<String> nameOfJunctions) {
		this.nameOfJunctions = nameOfJunctions;
	}

	@FXML
	public void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				SideBarController.this.start = new AutoCompleteControle("Start", SideBarController.this.nameOfJunctions);
				SideBarController.this.finish = new AutoCompleteControle("Ziel", SideBarController.this.nameOfJunctions);
				SideBarController.this.vBox.getChildren().add(0, SideBarController.this.start);
				SideBarController.this.vBox.getChildren().add(1, SideBarController.this.finish);
			}
		});

	}

	/**
	 * Set Start and Finish after validate check
	 */
	@FXML
	private void handleOk() {
		// Check if Name of TextFields are in the SortedSet
		if (!this.nameOfJunctions.contains(this.start.getText())) {
			UtilityViews.Error("Startpunkt ist nicht in der Liste vorhanden!\n" + "Bitte anderen Punkt w�hlen!");
		} else if (!this.nameOfJunctions.contains(this.finish.getText())) {
			UtilityViews.Error("Endpunkt ist nicht in der Liste vorhanden!\n" + "Bitte anderen Punkt w�hlen!");
		} else {
			// this.menufx.setStartTextField(this.start);
			// this.menufx.setFinishTextField(this.finish);
		}
	}

	/**
	 * Close Stage
	 */
	@FXML
	private void handleCancel() {

	}

	/**
	 * Clear Start TextField
	 */
	@FXML
	private void handleButtonStart() {
		this.start.requestFocus();
	}

	/**
	 * Clear Finish TextField
	 */
	@FXML
	private void handleButtonFinish() {
		this.finish.requestFocus();
	}

}
