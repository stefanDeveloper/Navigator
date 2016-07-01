package dhbw.navigator.controles;

import dhbw.navigator.models.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by Konrad Mueller on 01.07.2016.
 */
public class PathListingControle extends VBox{

    public void setPath(ArrayList<Node> nodes)
    {
        if(nodes!=null)
        {
            getChildren().clear();
            for(Node n: nodes)
            {
                getChildren().add(new Label(n.getName()));
            }
            setVisible(true);
        }
        else
        {
            setVisible(false);
        }
    }
}
