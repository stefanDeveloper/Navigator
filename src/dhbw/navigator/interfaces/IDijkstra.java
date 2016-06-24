package dhbw.navigator.interfaces;

import java.util.ArrayList;

import dhbw.navigator.models.Node;

/**
 * Created by Konrad Mueller on 22.06.2016.
 */
public interface IDijkstra {

    public ArrayList<Node> FindPath(Node start, Node end);
}
