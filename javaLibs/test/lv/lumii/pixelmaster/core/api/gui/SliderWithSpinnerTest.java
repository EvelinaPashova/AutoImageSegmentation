
package lv.lumii.pixelmaster.core.api.gui;

import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * A set of test suites for the class "SliderWithSpinner".
 *
 * The first test suite checks the behaviour of the SliderWithSpinner.
 * It checks whether the registered change listeners are notified.
 *
 * The second test suite checks the methods "setValue" and "getValue".
 *
 * The third test suite checks the method "removeChangeListener". First, some
 * listeners are registered to listen for the SliderWithSpinner change events.
 * Later, some of them are removed. It is checked if the removed listeners are
 * not notified of any events.
 *
 * @author Jonas
 */
public final class SliderWithSpinnerTest {

	static private final class NotificationCounter implements ChangeListener {
		private boolean wasNotified = false;
		private NotificationCounter() { }
		public void stateChanged(ChangeEvent e) { wasNotified = true; }
		private boolean wasNotified() { return wasNotified; }
		private void reset() { wasNotified = false; }
	}

	private SliderWithSpinner spider;
	private SliderWithSpinnerModel spiderModel;
	private NotificationCounter listener;

	@Before public void setUp() {
		spiderModel = new SliderWithSpinnerModel(0, 0, 100);
		spider = new SliderWithSpinner(spiderModel, SwingConstants.VERTICAL, true);
		listener = new NotificationCounter();
	}

	@Test public void s1t1() {
		spider.addChangeListener(listener);
		spider.setValue(40);
		assertTrue(listener.wasNotified());
	}
	@Test public void s1t2() {
		spider.addChangeListener(listener);
		spider.setValue(5);
		spider.setValue(7);
		assertTrue(listener.wasNotified());
	}
	@Test public void s1t3() {
		spider.addChangeListener(listener);
		spider.setValue(16);
		spider.setValue(72);
		spider.setValue(72);
		assertTrue(listener.wasNotified());
	}
	@Test public void s1t4() {
		spider.addChangeListener(listener);
		spider.setValue(14);
		spider.setValue(12);
		spider.setValue(12);
		spider.setValue(14);
		assertTrue(listener.wasNotified());
	}

	@Test public void s2t1() {
		spider.setValue(40);
		assertEquals(40, spiderModel.getValue());
		assertEquals(40, spiderModel.getValue());
	}
	@Test public void s2t2() {
		spider.setValue(5);
		spider.setValue(7);
		assertEquals(7, spiderModel.getValue());
		assertEquals(7, spiderModel.getValue());
	}
	@Test public void s2t3() {
		spider.setValue(16);
		spider.setValue(72);
		spider.setValue(72);
		assertEquals(72, spiderModel.getValue());
		assertEquals(72, spiderModel.getValue());
	}
	@Test public void s2t4() {
		spider.setValue(20);
		spider.setValue(19);
		spider.setValue(14);
		assertEquals(14, spiderModel.getValue());
		assertEquals(14, spiderModel.getValue());
	}

	@Test public void s3t1() {
		spider.addChangeListener(listener);
		spider.setValue(5);
		spider.setValue(1);
		spider.removeChangeListener(listener);
		listener.reset();
		spider.setValue(0);
		assertFalse(listener.wasNotified());
	}
	@Test public void s3t2() {
		NotificationCounter listener2 = new NotificationCounter();
		spider.addChangeListener(listener);
		spider.addChangeListener(listener2);
		spider.setValue(96);
		spider.setValue(15);
		spider.removeChangeListener(listener2);
		listener2.reset();
		spider.setValue(33);
		assertFalse(listener2.wasNotified());
		assertTrue(listener.wasNotified());
	}
}
