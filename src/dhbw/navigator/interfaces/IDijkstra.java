package dhbw.navigator.interfaces;

import java.util.ArrayList;

import dhbw.navigator.models.Node;

/**
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public interface IDijkstra {

	public ArrayList<Node> FindPath(Node start, Node end);
}
