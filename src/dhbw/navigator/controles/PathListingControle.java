package dhbw.navigator.controles;

import dhbw.navigator.models.Node;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * PathListingControle
 * Control that shows each step of the path
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad M�ller, Markus Menrath
 *
 */
public class PathListingControle extends VBox{

    private ScrollPane pane = new ScrollPane();


    public PathListingControle()
    {
        setVisible(false);
        pane.setBorder(Border.EMPTY);
        pane.setBackground(Background.EMPTY);
        setSpacing(0);
        setStyle("-fx-background-color: #FFF;");
        getChildren().addAll(new Separator(), pane);
    }
    
    
    //Set the path and generate a view
    public void setPath(ArrayList<Node> nodes)
    {
        if(nodes!=null)
        {
            VBox children = new VBox();
            children.setSpacing(5);
            pane.setContent(children);
            children.getChildren().clear();
            Node n;
            String textInfoBox;
            for(int i = 0; i<nodes.size();i++){
            	
            	HBox box = new HBox();
            	box.setPadding(new Insets(0,5,0,5));
            	box.setSpacing(5);
            	Label number = new Label("" + (i+1));
            	Label info = new Label();
            	n = nodes.get(i);
            	
            	if(i>0){
                	float partDist = n.getShortestDistance() -  nodes.get(i-1).getShortestDistance();
                	textInfoBox = n.getName() + "\nTeilabschnitt: " + partDist + "\nGesamt: " + n.getShortestDistance();
            		if(i == nodes.size() - 1){
                		textInfoBox = "Ende: " + textInfoBox;
            		}
            		else {
            			textInfoBox = "�ber: " + textInfoBox;
            		}
            	}
            	else{
            		textInfoBox = "Start: " + n.getName();
            	}
            	
            	info.setText(textInfoBox);
            	box.getChildren().addAll(number, info);
            	
            	children.getChildren().add(box);
            }
            setVisible(true);
        }
        else
        {
            setVisible(false);
        }
    }
}
