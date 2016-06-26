package dhbw.navigator.views;

import dhbw.navigator.io.Menufx;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RouteController {
	
	private Menufx menufx;
	
	@FXML
	private Button okButton;
	
	@FXML
	private Label cancelButton;
	
	@FXML
	private Label startLabel; 
	
	@FXML
	private Label aimLabel;

	public Menufx getMenufx() {
		return this.menufx;
	}

	public void setMenufx(Menufx menufx) {
		this.menufx = menufx;
	}

	public Label getStartLabel() {
		return startLabel;
	}

	public void setStartLabel(Label startLabel) {
		this.startLabel = startLabel;
	}

	public Label getAimLabel() {
		return aimLabel;
	}

	public void setAimLabel(Label aimLabel) {
		this.aimLabel = aimLabel;
	}
	@FXML
	public void initialize(){
	
	}
	
	@FXML
	private void handleOk(){
		
	}
	
	@FXML
	private void handleCancel(){
		System.exit(0);
	}

}
