package dhbw.navigator.views;

import dhbw.navigator.io.Menufx;
import dhbw.navigator.utility.BorderSlideBar;
import dhbw.navigator.utility.UtilityViews;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Controller for RootLayout.fxml
 * 
 * @author Stefan
 *
 */
public class RootLayoutController {
	private Menufx menufx;
	@FXML
	private StackPane primaryStackPane;
	@FXML
	private Button region;
	Label test = new Label("sdjak");

	@FXML
	public void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BorderSlideBar topFlapBar = new BorderSlideBar(300, RootLayoutController.this.region,
						RootLayoutController.this.menufx.viewRouteWindow());
				RootLayoutController.this.menufx.getStartNavigator().getPrimaryBorder().setLeft(topFlapBar);
			}
		});

	}

	public void addToCenter(Node node) {
		this.primaryStackPane.getChildren().add(node);
	}

	public void removeToCenter(Node node) {
		this.primaryStackPane.getChildren().remove(node);
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
	private void handleInfo() {
		UtilityViews.Information("Navigator \n" + "Gruppenprojekt Programmieren\n" + "2. Semester\n"
				+ "Manuela Leopold\n" + "Markus Menrath\n" + "Stefan Machmeier\n" + "Konrad Müller");
	}

	public Menufx getMenufx() {
		return this.menufx;
	}

	public void setMenufx(Menufx menufx) {
		this.menufx = menufx;
	}

}
