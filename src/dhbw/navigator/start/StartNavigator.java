package dhbw.navigator.start;

import java.io.IOException;
import dhbw.navigator.io.Menufx;
import dhbw.navigator.views.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartNavigator extends Application{
	private Stage 			primaryStage;
	private BorderPane 		primaryBorder;
	private Menufx 			menufx;

	
	
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	public BorderPane getPrimaryBorder() {
		return this.primaryBorder;
	}
	
	@Override
	public void start (Stage primaryStage){
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Navigator");
		this.primaryStage.setMinHeight(720);
		this.primaryStage.setMinWidth(680);
		this.primaryStage.setResizable(false);
		
		this.menufx = new Menufx();
		this.menufx.setStartNavigator(this);
		
		
		this.rootLayout();
		this.menufx.viewMainWindow();
		this.menufx.viewRouteWindow();
		
	}

	public void rootLayout(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("RootLayoutWindow.fxml"));
			this.primaryBorder = (BorderPane) loader.load();
			
			RootLayoutController controller = loader.getController();
			controller.setMenufx(this.menufx);
			
			Scene scene = new Scene(this.primaryBorder);
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main (String[] args){
		launch(args);
	}
}
