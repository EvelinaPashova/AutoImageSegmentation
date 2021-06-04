
package lv.lumii.pixelmaster.modules.spw.domain;

import org.junit.*;
import lv.lumii.pixelmaster.core.api.domain.Constants;
import static org.junit.Assert.*;

/**
 * A set of test suites for the class "CoordVector".
 * Private fields "vector" and "squares" are being checked.
 *
 * @author Jonas
 */
public class CoordVectorTest {

    private CoordVector vector;

	/**
	 * Test the constructor "public CoordVector(double... args)".
	 */
    @Test public void s1t1() {
		vector=new CoordVector(3, 4);
		double [] exp={3, 4};
		double [] exp2={9, 16};
		assertArrayEquals(exp, vector._vector(), Constants.PRECISION);
		assertArrayEquals(exp2, vector._squares(), Constants.PRECISION);
    }

	/**
	 * Test the constructor "public CoordVector(int dim)".
	 */
    @Test public void s1t2() {
		vector=new CoordVector(3);
		double [] exp={0, 0, 0};
		double [] exp2={0, 0, 0};
		assertArrayEquals(exp, vector._vector(), Constants.PRECISION);
		assertArrayEquals(exp2, vector._squares(), Constants.PRECISION);
    }

	/**
	 * Test setter and getter.
	 */
    @Test public void s1t3() {
		vector=new CoordVector(3);
		vector.setElement(2, 10.5);
		assertEquals(10.5, vector.getElement(2), Constants.PRECISION);
		assertEquals(110.25, vector._squares()[2], Constants.PRECISION);
    }

	/**
	 * Test the constructor "public CoordVector(double... args)".
	 */
    @Test public void s1t4() {
		vector=new CoordVector(4, 0.5, -8, 5, 2, 0);
		double [] exp={4, 0.5, -8, 5, 2, 0};
		double [] exp2={16, 0.25, 64, 25, 4, 0};
		assertArrayEquals(exp, vector._vector(), Constants.PRECISION);
		assertArrayEquals(exp2, vector._squares(), Constants.PRECISION);
    }

	/**
	 * Test method "scalarProduct".
	 */
    @Test public void s1t5() {
		vector=new CoordVector(4, 5);
		CoordVector w=new CoordVector(-2, 3);
		assertEquals(7, CoordVector.scalarProduct(vector, w), Constants.PRECISION);
    }
}
