package dhbw.navigator.views;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.io.Menufx;
import dhbw.navigator.models.Node;
import dhbw.navigator.utility.Utility;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
		IParser parser = new Parser();
		Osm test = (Osm) parser.parseFile(new File("Testdata/export.xml"));
		ArrayList<Node> name = parser.getNodes(test);
		
		//Add Name of Junction to SortedSet
		this.nameOfJunctions = new TreeSet<>();
		for (Node n : name) {
			if (n.getIsJunction() == true)
				this.nameOfJunctions.add(n.getName());
		}
		
		//Add AutoComplete to TextField with ContextMenu
		Utility.AutoComplete(this.startTextField, this.nameOfJunctions, this.entriesPopupStart);
		Utility.AutoComplete(this.finishTextField, this.nameOfJunctions, this.entriesPopupAim);
		//Add deleteButton for Input
		Utility.checkInput(this.startTextField, this.startButton);
		Utility.checkInput(this.finishTextField, this.finishButton);
		
		

	}
	/**
	 * Set Start and Finish after validate check
	 */
	@FXML
	private void handleOk() {
		//Check if Name of TextFields are in the SortedSet
		if (this.nameOfJunctions.contains(this.startTextField.getText()) &&
				this.nameOfJunctions.contains(this.finishTextField.getText())){
			
		} else {
			
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
	 * Clear TextField
	 */
	@FXML
	private void handleButtonStart(){
		this.startTextField.clear();
		
	}
	
}
