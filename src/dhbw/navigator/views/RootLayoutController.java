package dhbw.navigator.views;

import dhbw.navigator.io.Menufx;
import dhbw.navigator.utility.UtilityViews;
import javafx.fxml.FXML;
/**
 * Controller for RootLayout.fxml
 * @author Stefan
 *
 */
public class RootLayoutController {
	private Menufx menufx;
	
	@FXML
	public void initialize() {

	}
	
	/**
	 * Close Stage
	 */
	@FXML
	private void handleClose() {
		System.exit(0);
	}
	
	/**
	 * Open RouteWindow 
	 */
	@FXML
	private void handleRoute() {
		this.menufx.viewRouteWindow();
	}
	
	/**
	 * Info Alert 
	 */
	@FXML
	private void handleInfo(){
		UtilityViews.Information("Navigator \n"
				+ "Gruppenprojekt Programmieren\n"
				+ "2. Semester\n"
				+ "Manuela Leopold\n"
				+ "Markus Menrath\n"
				+ "Stefan Machmeier\n"
				+ "Konrad Müller");
	}

	public Menufx getMenufx() {
		return menufx;
	}

	public void setMenufx(Menufx menufx) {
		this.menufx = menufx;
	}

}
