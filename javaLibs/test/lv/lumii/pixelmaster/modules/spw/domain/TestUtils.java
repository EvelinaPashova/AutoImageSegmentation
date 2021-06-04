
package lv.lumii.pixelmaster.modules.spw.domain;

import lv.lumii.pixelmaster.modules.spw.domain.graph.UGraph;
import static org.junit.Assert.*;

final public class TestUtils {

	/**
	 * Determines whether or not two graphs are equal. Two instances of
	 * <code>UGraph</code> are equal if they contain the same vertices
	 * (at the same coordinates) and the same edges (edges connecting
	 * the pairs of vertices with the same coordinates).
	 */
	public static void assertGraphEquals(UGraph expected, UGraph actual) {

		/*
		 * Sets of vertices and sets of edges are compared using the method
		 * "assertEquals", which internally calls the method
		 * "Set<T>.equals". This method works because the methods "equals"
		 * and "hashCode" of classes "Vertex" and "GraphEdge"
		 * have been overridden.
		 */
		assertEquals(expected.getVertices(), actual.getVertices());
		assertEquals(expected.getEdges(), actual.getEdges());
	}
}
