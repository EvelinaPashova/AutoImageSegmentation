
package lv.lumii.pixelmaster.modules.spw.domain.graph;

import java.util.*;
import org.junit.*;
import java.io.*;
import static org.junit.Assert.*;

/**
 * A set of test suites for the class "EdgeIterator".
 *
 * In each test case, the graph is loaded from file.
 * The expected set is then constructed and compared with the actual.
 *
 * @author Jonas
 */
public class EdgeIteratorTest {
    private UGraph g;

    @Test public void s1t1() {
		g=GraphIO.read(new File("testdata/graph/graph1.in"));
		Set<GraphEdge> exp=new HashSet<GraphEdge>();

		Set<GraphEdge> actual = new IteratorConsumer<GraphEdge>(g.edges()).getContents();
		exp.add(new GraphEdge(new Vertex(1, 1), new Vertex(2, 2)));
		exp.add(new GraphEdge(new Vertex(2, 2), new Vertex(3, 3)));
		exp.add(new GraphEdge(new Vertex(2, 2), new Vertex(4, 4)));
		exp.add(new GraphEdge(new Vertex(3, 3), new Vertex(4, 4)));
		assertEquals(exp, actual);
    }

    @Test public void s1t2() {
		g=GraphIO.read(new File("testdata/graph/graph4.in"));
		Set<GraphEdge> actual = new IteratorConsumer<GraphEdge>(g.edges()).getContents();
		assertTrue(actual.isEmpty());
    }

    @Test public void s1t3() {
		g=GraphIO.read(new File("testdata/graph/graph6.in"));
		Set<GraphEdge> actual = new IteratorConsumer<GraphEdge>(g.edges()).getContents();
		assertTrue(actual.isEmpty());
    }
}
