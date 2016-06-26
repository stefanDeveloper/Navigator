package dhbw.navigator.views;

import dhbw.navigator.io.Menufx;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RouteController {
	
	private Menufx menufx;
	
	@FXML
	private Button okButton;
	
	@FXML
	private Label cancelButton;
	
	@FXML
	private TextField startLabel; 
	
	@FXML
	private TextField aimLabel;
	
	private Stage stageRoute;

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
		return startLabel;
	}

	public void setStartLabel(TextField startLabel) {
		this.startLabel = startLabel;
	}

	public TextField getAimLabel() {
		return aimLabel;
	}

	public void setAimLabel(TextField aimLabel) {
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
		this.stageRoute.close();
	}

}
