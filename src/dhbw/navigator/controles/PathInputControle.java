package dhbw.navigator.controles;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.SortedSet;

/**
 * Created by Konrad Mueller on 01.07.2016.
 */
public class PathInputControle extends VBox{
    private Node node;
    private Button okButton;
    private AutoCompleteControle origin;
    private AutoCompleteControle finish;
    private Button startButton;


    public PathInputControle()
    {
        setPadding(new Insets(5));
        startButton = new Button("Start");
        okButton = new Button("Ok");
    }

    public void setNamesOfjunction(SortedSet<String> namesOfJunctions)
    {
        origin = new AutoCompleteControle("origin", namesOfJunctions);
        finish = new AutoCompleteControle("Ziel", namesOfJunctions);
        getChildren().clear();
        getChildren().addAll(origin, finish, startButton);
    }
}
