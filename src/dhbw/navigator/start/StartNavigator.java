package dhbw.navigator.start;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.io.Menufx;
import dhbw.navigator.models.Node;
import dhbw.navigator.views.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Start Application
 * @author Stefan
 *
 */
public class StartNavigator extends Application{
	private Stage 			primaryStage;
	private BorderPane 		primaryBorder;
	private Menufx 			menufx;
	private IParser			parser;
	private SortedSet<String> nameOfJunctions;
	
	
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	public BorderPane getPrimaryBorder() {
		return this.primaryBorder;
	}
	
	@Override
	public void start (Stage primaryStage){
		//Initialize Stage
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Navigator");
		//Size
		this.primaryStage.setMinHeight(720);
		this.primaryStage.setMinWidth(680);
		this.primaryStage.setResizable(false);
		
		this.menufx = new Menufx();
		this.menufx.setStartNavigator(this);
		this.menufx.setStage(this.primaryStage);
		
		this.parser = new Parser();

		ArrayList<Node> node;
		boolean firstStart = true;
		if(firstStart)
		{
			Osm test = (Osm) parser.parseFile(new File("Testdata/germany.xml"));
			node = parser.getNodes(test);
			parser.serialize(node);
		}
		node = parser.deserialize();
		this.nameOfJunctions = new TreeSet<>();
		
		for (Node n : node) {
			if (n.getIsJunction() == true)
				this.nameOfJunctions.add(n.getName());
		}
		
		this.menufx.setNodes(node);		
		this.menufx.setNameOfJunctions(this.nameOfJunctions);
		
		//Load Views
		this.rootLayout();
		this.menufx.viewMainWindow();
		this.menufx.viewRouteWindow();
		
	}
	
	/**
	 * Load BorderPane
	 */
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
