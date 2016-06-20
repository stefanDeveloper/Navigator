package dhbw.navigator.views;

import dhbw.navigator.io.Menufx;
import javafx.fxml.FXML;

public class RootLayoutController {
	private Menufx menufx;
	
	@FXML
	public void initialize() {

	}

	@FXML
	private void handleClose() {
		System.exit(0);
	}
	
	@FXML
	private void handleInfo(){
		this.menufx.viewInfoLabel();
	}

	public Menufx getMenufx() {
		return menufx;
	}

	public void setMenufx(Menufx menufx) {
		this.menufx = menufx;
	}

}
