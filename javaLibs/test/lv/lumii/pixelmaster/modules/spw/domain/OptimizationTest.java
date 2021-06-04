
package lv.lumii.pixelmaster.modules.spw.domain;

import lv.lumii.pixelmaster.modules.spw.domain.graph.UGraph;
import lv.lumii.pixelmaster.modules.spw.domain.graph.GraphIO;

import org.junit.*;
import java.io.*;

/**
 * A set of test suites for the class "Optimization".
 *
 * @author Jonas
 */
public class OptimizationTest {
    private UGraph g;

	/**
	 * Test method "connectEnds".
	 * Connect all ends at the distance 10.
	 */
	@Test public void s1t1() {
		g=GraphIO.read(new File("testdata/graph/graph3.in"));
		Optimization.connectEnds(g, 10);

		UGraph expected = GraphIO.read(new File("testdata/graph/graph13.in"));
		TestUtils.assertGraphEquals(expected, g);
	}

	/**
	 * Test method "cutTails".
	 * Cut tails of length 2.9.
	 */
	@Test public void s1t2() {
		g=GraphIO.read(new File("testdata/graph/graph5.in"));
		Optimization.cutTails(g, 2.9);

		UGraph expected = GraphIO.read(new File("testdata/graph/graph14.in"));
		TestUtils.assertGraphEquals(expected, g);
	}

	/**
	 * Test method "primaryOptimization".
	 * Perform primary optimization using the maximum possible threshold,
	 * which means that ALL vertices with degree of 3 will be optimized.
	 */
	@Test public void s1t3() {
		g=GraphIO.read(new File("testdata/graph/graph7.in"));
		Optimization.primaryOptimization(g, 1.0);

		UGraph expected = GraphIO.read(new File("testdata/graph/graph15.in"));
		TestUtils.assertGraphEquals(expected, g);
	}
}
