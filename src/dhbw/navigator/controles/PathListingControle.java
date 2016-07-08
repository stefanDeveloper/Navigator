package dhbw.navigator.controles;

import dhbw.navigator.models.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by Konrad Mueller on 01.07.2016.
 */
public class PathListingControle extends ScrollPane{

    public PathListingControle()
    {
        setVisible(false);
    }

    public void setPath(ArrayList<Node> nodes)
    {
        if(nodes!=null)
        {
            VBox children = new VBox();
            setContent(children);
            children.getChildren().clear();
            for(Node n: nodes)
            {
                children.getChildren().add(new Label(n.getName()));
            }
            setVisible(true);
        }
        else
        {
            setVisible(false);
        }
    }
}
