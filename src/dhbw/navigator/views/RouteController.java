package dhbw.navigator.views;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.io.Menufx;
import dhbw.navigator.models.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
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
	private SortedSet<String> t;

	@FXML
	private ContextMenu entriesPopup;

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

	public TextField getAimLabel() {
		return aimLabel;
	}


	@FXML
	public void initialize() {
		entriesPopup = new ContextMenu();
		IParser parser = new Parser();
		Osm test = (Osm) parser.parseFile(new File("Testdata/export.xml"));
		ArrayList<Node> name = parser.getNodes(test);
		t = new TreeSet<>();
		for (Node n : name) {
			if (n.getIsJunction() == true)
				t.add(n.getName());
		}

	}

	@FXML
	private void handleOk() {
		
	}

	@FXML
	private void handleCancel() {
		this.stageRoute.close();
	}

	@FXML
	private void handleTypIn(){
		startLabel.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				  if (startLabel.getText().length() == 0)
			        {
			          entriesPopup.hide();
			        } else
			        {
			          LinkedList<String> searchResult = new LinkedList<>();
			          searchResult.addAll(t.subSet(startLabel.getText(), startLabel.getText() + Character.MAX_VALUE));
			          if (t.size() > 0)
			          {
			            populatePopup(searchResult);
			            if (!entriesPopup.isShowing())
			            {
			              entriesPopup.show(startLabel, Side.BOTTOM, 0, 0);
			            }
			          } else
			          {
			            entriesPopup.hide();
			          }
			        }
				
			}
		});
		
		startLabel.focusedProperty().addListener(new ChangeListener<Boolean>() {
		   @Override
		   public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
		      entriesPopup.hide();
		    }
		  });
		
	}
	
	private void populatePopup(List<String> searchResult) {
	    List<CustomMenuItem> menuItems = new LinkedList<>();
	    // If you'd like more entries, modify this line.
	    int maxEntries = 10;
	    int count = Math.min(searchResult.size(), maxEntries);
	    for (int i = 0; i < count; i++)
	    {
	      final String result = searchResult.get(i);
	      Label entryLabel = new Label(result);
	      CustomMenuItem item = new CustomMenuItem(entryLabel, true);
	      item.setOnAction(new EventHandler<ActionEvent>()
	      {
	        @Override
	        public void handle(ActionEvent actionEvent) {
	          startLabel.setText(result);
	          entriesPopup.hide();
	        }
	      });
	      menuItems.add(item);
	    }
	    entriesPopup.getItems().clear();
	    entriesPopup.getItems().addAll(menuItems);

	  }
	
	public SortedSet<String> getEntries() { return t; }

}
