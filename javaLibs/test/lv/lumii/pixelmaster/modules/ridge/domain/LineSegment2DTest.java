/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lv.lumii.pixelmaster.modules.ridge.domain;

import lv.lumii.pixelmaster.modules.ridge.domain.LineSegment2D;
import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
/**
 *
 * @author Kumpa
 */
public class LineSegment2DTest {

    public LineSegment2DTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

        static final Point POINT_1 = new Point(10, 0);
        static final Point POINT_2 = new Point(0, 10);
        static final Point POINT_3 = new Point(10, 10);
        static final Point POINT_4 = new Point(10, 20);
        static final Point POINT_5 = new Point(20, 10);
        static final Point POINT_6 = new Point(20, 20);
        static final LineSegment2D line0 = new LineSegment2D(POINT_1, POINT_1);
        static final LineSegment2D line1 = new LineSegment2D(POINT_1, POINT_2);
        static final LineSegment2D line2 = new LineSegment2D(POINT_1, POINT_5);
        static final LineSegment2D line3 = new LineSegment2D(POINT_1, POINT_4);
        static final LineSegment2D line4 = new LineSegment2D(POINT_1, POINT_6);
        static final LineSegment2D line5 = new LineSegment2D(POINT_2, POINT_5);
        static final LineSegment2D line6 = new LineSegment2D(POINT_4, POINT_2);
        static final LineSegment2D line7 = new LineSegment2D(POINT_4, POINT_1);
        static final LineSegment2D line8 = new LineSegment2D(POINT_4, POINT_5);
        static final LineSegment2D line9 = new LineSegment2D(POINT_6, POINT_2);

     /**
     * Test of getSlope method, of class LineSegment2D.
     */
    @Test
    public void testGetSlope() {
        System.out.println("* METHOD TEST: getSlope() Variables");
        assertEquals( -1.0, line1.getSlope(), 0);
        assertEquals( 1.0, line2.getSlope(), 0);
        assertEquals( 2.0, line4.getSlope(), 0);
        assertEquals( 0.0, line5.getSlope(), 0);
        assertEquals( 1.0, line6.getSlope(), 0);
        assertEquals( -1.0, line8.getSlope(), 0);
        assertEquals( 0.5, line9.getSlope(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSlopeEx() {
        System.out.println("* METHOD TEST: getSlope() Exception");
        assertEquals( 0, line0.getSlope(), 0);
        assertEquals( 0, line3.getSlope(), 0);
        assertEquals( 0, line7.getSlope(), 0);
    }

    /**
     * Test of getYintercept method, of class LineSegment2D.
     */
    @Ignore
    @Test
    public void testGetYintercept() {
        System.out.println("getYintercept");
        LineSegment2D instance = new LineSegment2D();
        double expResult = 0.0;
        double result = instance.getYintercept();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of haveSlope method, of class LineSegment2D.
     */
    @Test
    public void testHaveSlope() {
        System.out.println("* METHOD TEST: haveSlope() Variables");
        assertFalse(line0.haveSlope());
        assertTrue(line1.haveSlope());
        assertTrue(line2.haveSlope());
        assertFalse(line3.haveSlope());
        assertTrue(line4.haveSlope());
        assertTrue(line5.haveSlope());
        assertTrue(line6.haveSlope());
        assertFalse(line7.haveSlope());
        assertTrue(line8.haveSlope());
        assertTrue(line9.haveSlope());
    }

    /**
     * Test of getStart method, of class LineSegment2D.
     */
    @Ignore
    @Test
    public void testGetStart() {
        System.out.println("getStart");
        LineSegment2D instance = new LineSegment2D();
        Point expResult = null;
        Point result = instance.getStart();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEnd method, of class LineSegment2D.
     */
    @Ignore
    @Test
    public void testGetEnd() {
        System.out.println("getEnd");
        LineSegment2D instance = new LineSegment2D();
        Point expResult = null;
        Point result = instance.getEnd();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of move method, of class LineSegment2D.
     */
    @Ignore
    @Test
    public void testMove() {
        System.out.println("move");
        Point start = null;
        Point end = null;
        LineSegment2D instance = new LineSegment2D();
        instance.move(start, end);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of distance method, of class LineSegment2D.
     */
    @Test
    public void testdistance() {
        System.out.println("* METHOD TEST: distance() Variables");
        double expR0 = 0.0;
        double expR1 = 10.0;
        double expR2 = 7.0711;
        double expR3 = 14.142;
        double expR4 = 0.0;
        double expR5 = 0.0;
        double expR6 = 0.0;
        double expR7 = 0.0;
        double expR8 = 0.0;
        double expR9 = 0.0;
        double result0 = line0.distance(POINT_1);
        double result1 = line0.distance(POINT_3);
        double result2 = line1.distance(POINT_3);
        double result3 = line1.distance(POINT_1);
        double result4 = line1.distance(POINT_5);
        double result5 = line2.distance(POINT_3);
        double result6 = line2.distance(POINT_4);
        double result7 = line3.distance(POINT_2);
        double result8 = line3.distance(POINT_3);
        double result9 = line3.distance(POINT_5);
        double result10 = line5.distance(POINT_1);
        double result11 = line5.distance(POINT_3);
        double result12 = line5.distance(POINT_4);
        assertEquals(expR0, result0, 0);
        assertEquals(expR1, result1, 0);
        assertEquals(expR2, result2, 0.001);
        assertEquals(expR0, result3, 0);
        assertEquals(expR3, result4, 0.001);
        assertEquals(expR2, result5, 0.001);
        assertEquals(expR3, result6, 0.001);
        assertEquals(expR1, result7, 0);
        assertEquals(0, result8, 0);
        assertEquals(expR1, result9, 0);
        assertEquals(expR1, result10, 0);
        assertEquals(0, result11, 0);
        assertEquals(expR1, result12, 0);
    }

    /**
     * Test of setValues method, of class LineSegment2D.
     */
    @Test
    public void testSetValues() {
        System.out.println("* METHOD TEST: setValues() Variables");
        assertEquals(POINT_1, line1.getStart());
        assertEquals(POINT_2, line1.getEnd());
        assertEquals(-1.0, line1.getSlope() , 0);
        assertEquals(10.0, line1.getYintercept(), 0);
        assertTrue(line1.haveSlope());
    }

}


























