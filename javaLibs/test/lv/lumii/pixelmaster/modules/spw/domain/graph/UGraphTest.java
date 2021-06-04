
package lv.lumii.pixelmaster.modules.spw.domain.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;
import lv.lumii.pixelmaster.modules.spw.domain.TestUtils;

/**
 * A set of test suites for the class "UGraph".
 *
 * The first test suite tests getter methods,
 * the second test suite tests modifying methods.
 * Tests for methods such as "clone", which are neither modifiers
 * nor getters, also belong to the first test suite.
 *
 * In each test case, the original graph is loaded from file.
 * In the first test suite, getter methods are called and
 * their return value is examined.
 * In the second test suite, the return values are also examined;
 * in addition, the expected modified graph is read from file
 * and then compared to the actual modified graph.
 *
 * @author Jonas
 */
public class UGraphTest {
	private UGraph graph;

	/**
	 * Test method "clone".
	 * Clone the graph and then compare it with the original.
	 */
	@Test public void s1t1() {
		graph=GraphIO.read(new File("testdata/graph/graph1.in"));
		UGraph actual = (UGraph) graph.clone();
		TestUtils.assertGraphEquals(graph, actual);
	}

	// Test method "get".
	@Test public void s1t2() {
		graph=GraphIO.read(new File("testdata/graph/graph1.in"));
		assertEquals(new Vertex(1, 1), graph.get(1, 1));
	}

	// Test method "neighbours".
	@Test public void s1t3() {
		graph=GraphIO.read(new File("testdata/graph/graph2.in"));

		Set<Vertex> actual = new IteratorConsumer<Vertex> (graph.neighbours(3, 3)).getContents();
		Set <Vertex> expected=new HashSet <Vertex> ();
		expected.add(new Vertex(5, 5));
		expected.add(new Vertex(2, 2));
		expected.add(new Vertex(6, 6));
		expected.add(new Vertex(7, 7));
		assertEquals(expected, actual);
	}

	// Test method "nEdges".
	@Test public void s1t4() {
		graph=GraphIO.read(new File("testdata/graph/graph2.in"));
        assertEquals(11, graph.nEdges());
	}

	// Test method "getEdges".
	@Test public void s1t5() {
		graph=GraphIO.read(new File("testdata/graph/graph5.in"));
		Set <GraphEdge> expected=new HashSet <GraphEdge> ();
		Vertex v1=new Vertex(1, 1);
		Vertex v2=new Vertex(2, 2);
		Vertex v3=new Vertex(3, 3);
		Vertex v4=new Vertex(4, 4);
		expected.add(new GraphEdge(v1, v4));
		expected.add(new GraphEdge(v2, v4));
		expected.add(new GraphEdge(v3, v4));
		assertEquals(expected, graph.getEdges());
	}

	// Test method "getVertices".
	@Test public void s1t6() {
		graph=GraphIO.read(new File("testdata/graph/graph5.in"));
		Set <Vertex> expected=new HashSet <Vertex> ();
		Vertex v1=new Vertex(1, 1);
		Vertex v2=new Vertex(2, 2);
		Vertex v3=new Vertex(3, 3);
		Vertex v4=new Vertex(4, 4);
		expected.add(v1);
		expected.add(v2);
		expected.add(v3);
		expected.add(v4);
		assertEquals(expected, graph.getVertices());
	}

	/**
	 * Test method "addEdge".
	 * Try to add an edge between an existent and a non-existent vertex (should fail)
	 * and then assure that the graph has not changed.
	 */
	@Test public void s2t1() {
		graph = GraphIO.read(new File("testdata/graph/graph2.in"));

		// There is no vertex at coordinates (1, 2).
		boolean res = graph.addEdge(1, 2, 1, 1);
		assertFalse(res);

		UGraph expected = GraphIO.read(new File("testdata/graph/graph2.in"));
		TestUtils.assertGraphEquals(expected, graph);
	}

	/**
	 * Test method "deleteVertex".
	 * Delete an existing vertex and assure that all edges have been deleted.
	 */
	@Test public void s2t2() {
		graph = GraphIO.read(new File("testdata/graph/graph5.in"));
		boolean res = graph.deleteVertex(4, 4);
		assertTrue(res);

		UGraph expected = GraphIO.read(new File("testdata/graph/graph4.in"));
		TestUtils.assertGraphEquals(expected, graph);
	}

	/**
	 * Test method "clear".
	 * Call the method "clear" and then assure that all vertices
	 * and edges have been deleted.
	 */
	@Test public void s2t3() {
		graph=GraphIO.read(new File("testdata/graph/graph3.in"));
		graph.clear();

		UGraph expected = GraphIO.read(new File("testdata/graph/graph6.in"));
		TestUtils.assertGraphEquals(expected, graph);
	}

	/**
	 * Test method "addVertex".
	 * Try to add a vertex at coordinates at which a vertex is already
	 * present (should fail).
	 */
	@Test public void s2t4() {
		graph=GraphIO.read(new File("testdata/graph/graph4.in"));
		assertNull(graph.addVertex(1, 1));

		UGraph expected = GraphIO.read(new File("testdata/graph/graph4.in"));
		TestUtils.assertGraphEquals(expected, graph);
	}
}
