package de.dhbw.navigator.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.dhbw.navigator.implementation.ParseService;

@RunWith(JUnit4.class)
public class NodeTest {
	private Node node = new Node("Test");
	private ArrayList<Node> nodes;

	@Before
	public void startUp() {
		ParseService parser = new ParseService();
		nodes = parser.getNodes("resources/data/export.xml");
	}

	@Test
	public void distFrom() {
		assertEquals("Check if distance is equal", 4.208f, node.distFrom(nodes.get(0), nodes.get(1)), 0.001);

	}

	@Test
	public void checkNode() {
		assertSame(nodes.get(0).getEdges());
		assertNotNull("Check if node have edges", nodes.get(10).getEdges());
		assertNotSame("Check ID", nodes.get(10).getAllIds());

	}

	private void assertSame(ArrayList<Edge> edges) {

	}

}
