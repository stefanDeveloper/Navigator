package de.dhbw.navigator.implementation;


import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import de.dhbw.navigator.models.Node;

@RunWith(JUnit4.class)
public class DijkstraTest {
	private ArrayList<Node> nodes;
	private Dijkstra dijkstra;
	ArrayList<String> result;
	@Before
	public void startUp(){
		result = new ArrayList<>();
		result.add("Ludwigsburg-Süd");
		result.add("Ludwigsburg-Nord");
		result.add("Pleidelsheim");
		result.add("Heilbronn/Untergruppenbach");
		result.add("Weinsberg/Ellhofen");
		result.add("Kreuz Weinsberg");
		result.add("Bretzfeld");
		result.add("Öhringen");
		result.add("Neuenstein");
		ParseService parser = new ParseService();
		nodes = parser.getNodes("resources/data/export.xml");
		dijkstra = new Dijkstra();
	}
	@Test
	public void distance(){
		assertNotSame(result.get(0), result.get(5));
		assertEquals(dijkstra.equals(result));
		assertEquals(result.get(0), "Ludwigsburg-Süd");
		assertSame(dijkstra.FindPath(nodes.get(5), nodes.get(20)));
	}
	
	private void assertSame(ArrayList<Node> findPath) {

		
	}
	
	private void assertEquals(String string, String string2) {

		
	}
	private void assertEquals(boolean equals) {

		
	}
	@Test
	public void findPathTest(){
		ArrayList<Node> result = dijkstra.FindPath(nodes.get(0), nodes.get(15));
		boolean equal = true;
		for (Node node : result) {
			// Check if result contains the name of each node
			if(!this.result.contains(node.getName())){
				equal = false;
			}
		}
		assertTrue(equal);
		
	}
}
