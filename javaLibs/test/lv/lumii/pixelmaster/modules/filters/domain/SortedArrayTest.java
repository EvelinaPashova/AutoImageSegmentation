
package lv.lumii.pixelmaster.modules.filters.domain;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;
import lv.lumii.pixelmaster.core.api.domain.Constants;

/**
 * A set of test suites for the class "SortedArray".
 *
 * @author Jonas
 */
public class SortedArrayTest {
	private SortedArray a;

	@Test public void s1t1() {
		a=new SortedArray(7);
		int [] buf={13, 18, 8, 29, 31, 2, 14};
		int [] expectedArray={2, 8, 14, 14, 18, 29, 31};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(14);
		
		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(14);
		
		assertArrayEquals(expectedArray, a._arr());
		assertEquals(16.57142857142857142, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s1t2() {
		a=new SortedArray(7);
		int [] buf={13, 18, 8, 29, 31, 2, 14};
		int [] expectedArray={2, 8, 13, 14, 18, 29, 31};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(13);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(13);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(16.42857142857142857, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s1t3() {
		a=new SortedArray(7);
		int [] buf={13, 18, 8, 29, 31, 2, 14};
		int [] expectedArray={2, 8, 8, 14, 18, 29, 31};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(8);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(8);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(15.71428571428571428, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s1t4() {
		a=new SortedArray(7);
		int [] buf={13, 18, 8, 29, 31, 2, 14};
		int [] expectedArray={2, 8, 14, 16, 18, 29, 31};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(16);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(16);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(16.85714285714285714, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s1t5() {
		a=new SortedArray(6);
		int [] buf={15, 8, 2, 15, 2, 2};
		int [] expectedArray={2, 2, 2, 8, 8, 15};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(8);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(8);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(6.166666666666666666, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s1t6() {
		a=new SortedArray(6);
		int [] buf={15, 8, 2, 15, 2, 2};
		int [] expectedArray={2, 2, 2, 8, 15, 15};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(15);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(15);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(7.333333333333333333, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s1t7() {
		a=new SortedArray(6);
		int [] buf={15, 8, 2, 15, 2, 2};
		int [] expectedArray={2, 2, 2, 2, 8, 15};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(2);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(2);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(5.16666666666666666, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s1t8() {
		a=new SortedArray(6);
		int [] buf={15, 8, 2, 15, 2, 2};
		int [] expectedArray={2, 2, 2, 8, 9, 15};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(9);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(9);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(6.33333333333333333, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s2t1() {
		a=new SortedArray(1);
		int [] buf={5};
		int [] expectedArray={5};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(5);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(5);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(5.0, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s2t2() {
		a=new SortedArray(1);
		int [] buf={5};
		int [] expectedArray={7};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(7);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(7);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(7.0, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s2t3() {
		a=new SortedArray(1);
		int [] buf={5};
		int [] expectedArray={2};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(2);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(2);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(2.0, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s2t4() {
		a=new SortedArray(7);
		int [] buf={0, 2, 3, 2, 8, 0, 0};
		int [] expectedArray={0, 0, 2, 2, 3, 8, 16};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(16);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(16);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(4.428571428571428, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s2t5() {
		a=new SortedArray(4);
		int [] buf={20, 8, 5, 15};
		int [] expectedArray={5, 8, 15, 30};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(30);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(30);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(14.5, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s2t6() {
		a=new SortedArray(4);
		int [] buf={20, 8, 5, 15};
		int [] expectedArray={5, 8, 15, 18};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(18);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(18);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(11.5, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s2t7() {
		a=new SortedArray(4);
		int [] buf={20, 8, 5, 15};
		int [] expectedArray={3, 5, 8, 15};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(3);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(3);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(7.75, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s2t8() {
		a=new SortedArray(4);
		int [] buf={20, 8, 5, 15};
		int [] expectedArray={5, 8, 13, 15};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(13);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(13);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(10.25, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s2t9() {
		a=new SortedArray(4);
		int [] buf={20, 8, 5, 15};
		int [] expectedArray={5, 7, 8, 15};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(7);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(7);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(8.75, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}

	@Test public void s2t10() {
		a=new SortedArray(4);
		int [] buf={20, 8, 5, 15};
		int [] expectedArray={-30, 0, 15, 100};
		ArrayDeque <Integer> expectedDeque=new ArrayDeque <Integer> ();
		for (int i=0; i < buf.length; i++) expectedDeque.add(buf[i]);
		expectedDeque.remove();
		expectedDeque.add(100);
		expectedDeque.remove();
		expectedDeque.add(0);
		expectedDeque.remove();
		expectedDeque.add(-30);

		a.init(buf, 0, buf.length, buf.length, 1, 0);
		a.replace(100);
		a.replace(0);
		a.replace(-30);

		assertArrayEquals(expectedArray, a._arr());
		assertEquals(21.25, a.average(), Constants.PRECISION);
		TestUtils.assertDequeEquals(expectedDeque, a._q());
	}
}
