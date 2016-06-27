package dhbw.navigator.start;

import dhbw.navigator.generated.Osm;
import dhbw.navigator.implementation.Dijkstra;
import dhbw.navigator.implementation.Parser;
import dhbw.navigator.interfaces.IDijkstra;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.models.Node;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Konrad Mueller on 22.06.2016.
        */
public class TestStart extends Application {


    public static void main (String[] args){
 		IParser parser = new Parser();
        String name = "south_mid_germany.xml";
        //name = "export.xml";
        Osm test = (Osm) parser.parseFile(new File("Testdata/" + name));
        ArrayList<Node> nodes = parser.getNodes(test);
        parser.serialize(nodes);
        ArrayList<Node> deserializedData = parser.deserialize();
        IDijkstra d = new Dijkstra();
        ArrayList<Node> nodeList = d.FindPath(nodes.get(0), nodes.get(2));
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
