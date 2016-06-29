package dhbw.navigator.views;

import java.util.SortedSet;

import dhbw.navigator.io.Menufx;
import dhbw.navigator.utility.Utility;
import dhbw.navigator.utility.UtilityViews;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for RouteWindow.fxml
 * 
 * @author Stefan
 *
 */
public class RouteController {
	private Menufx menufx;
	private SortedSet<String> nameOfJunctions;
	private Node node;
	@FXML
	private Button okButton;
	@FXML
	private Label cancelButton;
	@FXML
	private TextField startTextField;
	@FXML
	private TextField finishTextField;
	@FXML
	private ContextMenu entriesPopupStart;
	@FXML
	private ContextMenu entriesPopupAim;
	@FXML
	private Button startButton;
	@FXML
	private Button finishButton;

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

	public Menufx getMenufx() {
		return this.menufx;
	}

	public void setMenufx(Menufx menufx) {
		this.menufx = menufx;
	}

	public TextField getStartLabel() {
		return this.startTextField;
	}

	public TextField getAimLabel() {
		return this.finishTextField;
	}

	@FXML
	public void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Add AutoComplete to TextField with ContextMenu
				Utility.AutoComplete(RouteController.this.startTextField, RouteController.this.nameOfJunctions,
						RouteController.this.entriesPopupStart);
				Utility.AutoComplete(RouteController.this.finishTextField, RouteController.this.nameOfJunctions,
						RouteController.this.entriesPopupAim);
				// Add deleteButton for Input
				Utility.checkInput(RouteController.this.startTextField, RouteController.this.startButton);
				Utility.checkInput(RouteController.this.finishTextField, RouteController.this.finishButton);
			}
		});
	}

	/**
	 * Set Start and Finish after validate check
	 */
	@FXML
	private void handleOk() {
		// Check if Name of TextFields are in the SortedSet
		if (!this.nameOfJunctions.contains(this.startTextField.getText())) {
			UtilityViews.Error("Startpunkt ist nicht in der Liste vorhanden!\n" + "Bitte anderen Punkt wählen!");
		} else if (!this.nameOfJunctions.contains(this.finishTextField.getText())) {
			UtilityViews.Error("Endpunkt ist nicht in der Liste vorhanden!\n" + "Bitte anderen Punkt wählen!");
		} else {
			this.menufx.setStartTextField(this.startTextField);
			this.menufx.setFinishTextField(this.finishTextField);
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
		this.startTextField.clear();
		this.startTextField.requestFocus();
	}

	/**
	 * Clear Finish TextField
	 */
	@FXML
	private void handleButtonFinish() {
		this.finishTextField.clear();
		this.finishTextField.requestFocus();
	}

}
