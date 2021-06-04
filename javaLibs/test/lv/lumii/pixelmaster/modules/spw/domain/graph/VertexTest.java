
package lv.lumii.pixelmaster.modules.spw.domain.graph;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import lv.lumii.pixelmaster.modules.spw.domain.TestUtils;

/**
 * A set of test suites for the class "Vertex".
 *
 * The first test suite tests getter methods,
 * the second test suite tests modifying methods.
 * Tests for methods such as "equals", which are neither modifiers
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
public class VertexTest {
	private UGraph g;

	/**
	 * Test method "equals".
	 * Create two vertices with different coordinates
	 * that do not belong to any graph and then
	 * compare them. The method should indicate that they are not equal.
	 */
	@Test public void s1t1() {
		Vertex v1=new Vertex(9, 16), v2=new Vertex(20, 10);
		boolean b=v1.equals(v2);
		assertFalse(b);
	}

	/**
	 * Test method "equals".
	 * Assure that vertex does not equal to an object of class "Object".
	 */
	@Test public void s1t2() {
		Vertex v1=new Vertex(9, 16);
		boolean b=v1.equals(new Object());
		assertFalse(b);
	}

	/**
	 * Test method "adjacent".
	 * Check adjacency of two existing vertices.
	 */
	@Test public void s1t3() {
		g=GraphIO.read(new File("testdata/graph/graph2.in"));
		assertTrue(g.get(3, 3).adjacent(g.get(5, 5)));
	}

	/**
	 * Test method "degree".
	 * Check the degree of an existing vertex.
	 */
	@Test public void s1t4() {
		g=GraphIO.read(new File("testdata/graph/graph1.in"));
		assertEquals(1, g.get(1, 1).degree());
	}

	/**
	 * Test method "setLocation".
	 * Change coordinates of an existing vertex and assure that
	 * all edges from/to that vertex have been preserved.
	 */
	@Test public void s2t1() {
		g=GraphIO.read(new File("testdata/graph/graph1.in"));
		boolean b=g.get(2, 2).setLocation(9, 9);
		assertTrue(b);

		UGraph expected = GraphIO.read(new File("testdata/graph/graph17.in"));
		TestUtils.assertGraphEquals(expected, g);
	}

	/**
	 * Test method "delete".
	 * Delete an existing vertex.
	 */
	@Test public void s2t2() {
		g=GraphIO.read(new File("testdata/graph/graph3.in"));
		assertTrue(g.get(4, 4).delete());

		UGraph expected = GraphIO.read(new File("testdata/graph/graph18.in"));
		TestUtils.assertGraphEquals(expected, g);
	}

	/**
	 * Test method "merge".
	 * Merge vertices (3, 3), (5, 5) and (7, 7).
	 * All vertices belong to the same graph.
	 *
	 * The resulting graph should look like in the picture 1 of
	 * the test case specification document for the package "graph".
	 */
	@Test public void s2t3() {
		g=GraphIO.read(new File("testdata/graph/graph2.in"));
		assertTrue(g.get(3, 3).merge(g.get(5, 5), g.get(7, 7)));

		UGraph expected = GraphIO.read(new File("testdata/graph/graph16.in"));
		TestUtils.assertGraphEquals(expected, g);
	}

	/**
	 * Test method "merge".
	 * Merge vertices (0, 0), (1, 1), (3, 3) and (4, 4).
	 * All vertices belong to the same graph.
	 */
	@Test public void s2t4() {
		g=GraphIO.read(new File("testdata/graph/graph3.in"));
		assertTrue(g.get(0, 0).merge(g.get(4, 4), g.get(1, 1), g.get(3, 3)));

		UGraph expected = GraphIO.read(new File("testdata/graph/graph11.in"));
		TestUtils.assertGraphEquals(expected, g);
	}

	/**
	 * Test method "merge".
	 * Try to merge a vertex with a vertex that does not
	 * belong to this graph (should fail).
	 * Assure that the graph has not changed.
	 */
	@Test public void s2t5() {
		g=GraphIO.read(new File("testdata/graph/graph3.in"));
		assertFalse(g.get(0, 0).merge(g.get(4, 4), g.get(1, 1), new Vertex(3, 3)));

		UGraph expected = GraphIO.read(new File("testdata/graph/graph3.in"));
		TestUtils.assertGraphEquals(expected, g);
	}

	/**
	 * Test method "merge".
	 * Merge the vertex with itself.
	 */
	@Test public void s2t6() {
		g=GraphIO.read(new File("testdata/graph/graph1.in"));
		assertTrue(g.get(4, 4).merge(g.get(4, 4), g.get(1, 1)));

		UGraph expected = GraphIO.read(new File("testdata/graph/graph12.in"));
		TestUtils.assertGraphEquals(expected, g);
	}
}
