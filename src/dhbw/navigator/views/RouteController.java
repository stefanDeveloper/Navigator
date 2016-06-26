package dhbw.navigator.views;

import java.io.File;
import java.util.ArrayList;
import np.com.ngopal.control.AutoFillTextBox;
import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.io.Menufx;
import dhbw.navigator.models.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	private AutoFillTextBox<?> startLabel; 
	
	@FXML
	private AutoFillTextBox<?> aimLabel;
	
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

	public AutoFillTextBox<?> getStartLabel() {
		return startLabel;
	}

	public void setStartLabel(AutoFillTextBox<?> startLabel) {
		this.startLabel = startLabel;
	}

	public AutoFillTextBox<?> getAimLabel() {
		return aimLabel;
	}

	public void setAimLabel(AutoFillTextBox<?> aimLabel) {
		this.aimLabel = aimLabel;
	}
	@FXML
	public void initialize(){
		IParser parser =  new Parser();
		Osm test = (Osm) parser.parseFile(new File("Testdata/export.xml"));
		ArrayList<Node> name = parser.getNodes(test);
		ObservableList<String> t = FXCollections.observableArrayList();
		for (Node n : name){
			t.add(n.getName());
		}
		this.startLabel = new AutoFillTextBox<>(t);
		
	}
	
	@FXML
	private void handleOk(){
		
	}
	
	@FXML
	private void handleCancel(){
		this.stageRoute.close();
	}
	
	@FXML
	private void handleTypIn(){
		
	}

}
