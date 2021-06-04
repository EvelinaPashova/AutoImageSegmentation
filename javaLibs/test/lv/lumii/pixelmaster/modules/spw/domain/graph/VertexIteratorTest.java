
package lv.lumii.pixelmaster.modules.spw.domain.graph;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

/**
 * A set of test suites for the class "VertexIterator".
 *
 * In each test case, the graph is loaded from file.
 * The expected set is then constructed and compared with the actual.
 *
 * @author Jonas
 */
public class VertexIteratorTest {
	private UGraph g;

	@Test public void s1t1() {
		g=GraphIO.read(new File("testdata/graph/graph1.in"));
		Set<Vertex> actual = new IteratorConsumer<Vertex>(g.vertices()).getContents();
		Set<Vertex> expected=new HashSet<Vertex>();
		expected.add(new Vertex(1, 1));
		expected.add(new Vertex(2, 2));
		expected.add(new Vertex(3, 3));
		expected.add(new Vertex(4, 4));
		assertEquals(expected, actual);
	}

	@Test public void s1t2() {
		g=GraphIO.read(new File("testdata/graph/graph6.in"));
		Set<Vertex> actual = new IteratorConsumer<Vertex>(g.vertices()).getContents();
		assertTrue(actual.isEmpty());
	}
}
