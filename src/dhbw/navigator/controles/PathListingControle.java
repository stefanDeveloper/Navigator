package dhbw.navigator.controles;

import dhbw.navigator.models.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by Konrad Mueller on 01.07.2016.
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

    public void setPath(ArrayList<Node> nodes)
    {
        if(nodes!=null)
        {
            VBox children = new VBox();
            pane.setContent(children);
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
