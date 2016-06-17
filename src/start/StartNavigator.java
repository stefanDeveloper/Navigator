package start;

import java.io.IOException;
import com.application.views.RootLayoutController;
import io.Menufx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartNavigator extends Application{
	private Stage primaryStage;
	private BorderPane primaryBorder;
	private Menufx menufx;
	
	
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
		this.primaryStage.setMinHeight(500);
		this.primaryStage.setMinWidth(500);
		this.primaryStage.setResizable(false);
		
		this.menufx = new Menufx();
		this.menufx.setStart(this);
		
		
		this.rootLayout();
		
	}

	public void rootLayout(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(RootLayoutController.class.getResource("RootLayoutWindow.fxml"));
			this.primaryBorder = (BorderPane) loader.load();
			
//			RootLayoutController controller = loader.getController();
//			controller.setStage(this.primaryStage);
			
			Scene scene = new Scene(this.primaryBorder);
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main (String[] args){
//		IParser parser = new Parser();
//      generated.Osm test = (generated.Osm) parser.parseFile(new File("Testdata/Ausschnitt_StuttgartLeonberg"));
		launch(args);
	}
}