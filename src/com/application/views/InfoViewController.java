package com.application.views;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class InfoViewController {
	private Stage stage;
	
	public void setStage(Stage stage){
		this.stage = stage;
	}
	
	
	@FXML
	public void initialize(){
		
	}
	
	@FXML
	public void handleOk(){
		this.stage.close();
	}

}
