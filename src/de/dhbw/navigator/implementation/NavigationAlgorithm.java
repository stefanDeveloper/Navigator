package de.dhbw.navigator.implementation;

import java.util.ArrayList;

import de.dhbw.navigator.models.Node;

/**
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public interface NavigationAlgorithm {

	public ArrayList<Node> FindPath(Node start, Node end);
}
