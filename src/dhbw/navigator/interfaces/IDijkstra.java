package dhbw.navigator.interfaces;

import dhbw.navigator.models.Node;

/**
 * Created by Konrad Mueller on 22.06.2016.
 */
public interface IDijkstra {

    public void FindPath(Node start, Node end);
}
