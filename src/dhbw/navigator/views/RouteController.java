package dhbw.navigator.views;

import java.util.SortedSet;
import dhbw.navigator.io.Menufx;
import dhbw.navigator.utility.Utility;
import dhbw.navigator.utility.UtilityViews;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * Controller for RouteWindow.fxml
 * @author Stefan
 *
 */
public class RouteController {
	private Menufx menufx;
	private Stage stageRoute;
	private SortedSet<String> nameOfJunctions;
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

	
	public SortedSet<String> getNameOfJunctions() {
		return nameOfJunctions;
	}

	public void setNameOfJunctions(SortedSet<String> nameOfJunctions) {
		this.nameOfJunctions = nameOfJunctions;
	}

	public Stage getStageRoute() {
		return stageRoute;
	}

	public void setStageRoute(Stage stageRoute) {
		this.stageRoute = stageRoute;
	}

	public Menufx getMenufx() {
		return this.menufx;
	}

	public void setMenufx(Menufx menufx) {
		this.menufx = menufx;
	}

	public TextField getStartLabel() {
		return startTextField;
	}

	public TextField getAimLabel() {
		return finishTextField;
	}


	@FXML
	public void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//Add AutoComplete to TextField with ContextMenu
				Utility.AutoComplete(startTextField, nameOfJunctions, entriesPopupStart);
				Utility.AutoComplete(finishTextField, nameOfJunctions, entriesPopupAim);
				//Add deleteButton for Input
				Utility.checkInput(startTextField, startButton);
				Utility.checkInput(finishTextField, finishButton);
			}
		});
	}
	
	/**
	 * Set Start and Finish after validate check
	 */
	@FXML
	private void handleOk() {
		//Check if Name of TextFields are in the SortedSet
		if (!this.nameOfJunctions.contains(this.startTextField.getText())){
			UtilityViews.Error("Startpunkt ist nicht in der Liste vorhanden!\n"
					+ "Bitte anderen Punkt wählen!");
		} else if (!this.nameOfJunctions.contains(this.finishTextField.getText())){
			UtilityViews.Error("Endpunkt ist nicht in der Liste vorhanden!\n"
					+ "Bitte anderen Punkt wählen!");
		} else {
			this.menufx.setStartTextField(this.startTextField);
			this.menufx.setFinishTextField(this.finishTextField);
			this.stageRoute.close();
		}
	}
	
	/**
	 * Close Stage
	 */
	@FXML
	private void handleCancel() {
		this.stageRoute.close();
	}
	
	/**
	 * Clear Start TextField
	 */
	@FXML
	private void handleButtonStart(){
		this.startTextField.clear();
		this.startTextField.requestFocus();
	}
	
	/**
	 * Clear Finish TextField
	 */
	@FXML
	private void handleButtonFinish(){
		this.finishTextField.clear();
		this.finishTextField.requestFocus();
	}
	
	
}
