
package lv.lumii.pixelmaster.modules.spw.domain;

import lv.lumii.pixelmaster.modules.spw.domain.graph.Vertex;
import org.junit.*;
import lv.lumii.pixelmaster.core.api.domain.Constants;
import static org.junit.Assert.*;

/**
 * A set of test suites for the class "Line".
 *
 * @author Jonas
 */
public class LineTest {

	/**
	 * Test method "angleBetweenLinesCos".
	 * The angle between two parallel lines.
	 */
    @Test public void s1t1() {
		double actualCos=Line.angleBetweenLinesCos(1, 1, 2, 2, 3, 3);
		assertEquals(1, actualCos, Constants.PRECISION);
    }

	/**
	 * Test method "angleBetweenVectorsCos".
	 * The angle between two equal vectors.
	 */
    @Test public void s1t2() {
		double actualCos=Line.angleBetweenVectorsCos(1, 1, 2, 2, 1, 1);
		assertEquals(1, actualCos, Constants.PRECISION);
    }

	/**
	 * Test method "distanceFromLineToPoint".
	 * Distance from the point (1, 3) to the line that passes through
	 * points (0, 0) and (4, 4).
	 */
    @Test public void s1t3() {
		double actualDistance=Line.distanceFromLineToPoint(new Vertex(1, 3),
				new Vertex(0, 0), new Vertex(4, 4));
		assertEquals(Math.sqrt(2), actualDistance, Constants.PRECISION);
    }

	/**
	 * Test method "distanceFromLineToPoint".
	 * Distance from the point (1, -1) to the horizontal line that
	 * passes through points (-1, -2) and (4, -2).
	 */
    @Test public void s1t4() {
		double actualDistance=Line.distanceFromLineToPoint(new Vertex(1, -1),
				new Vertex(-1, -2), new Vertex(4, -2));
		assertEquals(1, actualDistance, Constants.PRECISION);
    }

	/**
	 * Test method "distanceFromLineToPoint".
	 * Distance from the point (0, 1) to the vertical line
	 * that passes through it.
	 */
    @Test public void s1t5() {
		double actualDistance=Line.distanceFromLineToPoint(new Vertex(0, 1),
				new Vertex(0, 2), new Vertex(0, -2));
		assertEquals(0, actualDistance, Constants.PRECISION);
    }

	/**
	 * Test method "distanceFromLineToPoint".
	 * Distance from the point (-2, 3) to the line that passes through it.
	 */
    @Test public void s1t6() {
		double actualDistance=Line.distanceFromLineToPoint(new Vertex(-2, 3),
				new Vertex(-2, 3), new Vertex(-1, -2));
		assertEquals(0, actualDistance, Constants.PRECISION);
    }
}
