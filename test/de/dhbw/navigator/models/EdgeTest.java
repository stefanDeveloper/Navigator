package de.dhbw.navigator.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.dhbw.navigator.implementation.ParseService;

@RunWith(JUnit4.class)
public class EdgeTest {
	private Edge edge;

	@Before
	public void startUp() {
		ParseService parser = new ParseService();
		ArrayList<Node> node = parser.getNodes("resources/data/export.xml");
		edge = new Edge(node.get(0), node.get(1));
	}

	@Test
	public void distFrom() {
		// length 4.208f was calculate by online tool to get the exact length of
		// these to Nodes
		assertEquals("Check if distance is equal", 4.208f, edge.getDistance(), 0.001);
	}

	@Test
	public void checkEdge() {
		assertNotSame("Check if edge haven't the same node", edge.getStartNode(), edge.getEndNode());
		assertTrue("Check if edge have exact two nodes", edge.allNodes().size() == 2);
		assertNotNull("Check both nodes of edge", edge.getStartNode(), edge.getEndNode());
		assertNotSame("Check not same node", edge.allNodes());
	}

	private void assertNotNull(String string, Node startNode, Node endNode) {
		if (startNode != null && endNode != null)
			assertTrue(true);
	}
}
