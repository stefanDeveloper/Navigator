package dhbw.navigator.views;

import java.util.List;

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
	private List<String> nameOfJunctions;
	private AutoCompleteControle start;
	private AutoCompleteControle finish;
	private Node node;
	@FXML
	private Button okButton;
	@FXML
	private Label cancelButton;
	@FXML
	private Button startButton;
	@FXML
	private Button finishButton;
	@FXML
	private VBox vBox;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public List<String> getNameOfJunctions() {
		return nameOfJunctions;
	}

	public void setNameOfJunctions(List<String> nameOfJunctions) {
		this.nameOfJunctions = nameOfJunctions;

	}

	@FXML
	public void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				start = new AutoCompleteControle("Start", nameOfJunctions);
				finish = new AutoCompleteControle("Ziel", nameOfJunctions);
				vBox.getChildren().add(0, start);
				vBox.getChildren().add(1, finish);
			}
		});

	}

	/**
	 * Set Start and Finish after validate check
	 */
	@FXML
	private void handleOk() {
		// Check if Name of TextFields are in the SortedSet
		if (!nameOfJunctions.contains(start.getText())) {
			UtilityViews.Error("Startpunkt ist nicht in der Liste vorhanden!\n" + "Bitte anderen Punkt w�hlen!");
		} else if (!nameOfJunctions.contains(finish.getText())) {
			UtilityViews.Error("Endpunkt ist nicht in der Liste vorhanden!\n" + "Bitte anderen Punkt w�hlen!");
		}
	}

	/**
	 * Clear Start TextField
	 */
	@FXML
	private void handleButtonStart() {
		start.requestFocus();
	}

	/**
	 * Clear Finish TextField
	 */
	@FXML
	private void handleButtonFinish() {
		finish.requestFocus();
	}

}
