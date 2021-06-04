
package lv.lumii.pixelmaster.modules.filters.domain;

import java.util.ArrayDeque;
import static org.junit.Assert.*;

final class TestUtils {

	/**
	 * Determines whether or not two deques are equal. Two instances of
	 * <code>ArrayDeque<Integer></code> are equal if they have the same
	 * size and the same elements.
	 */
	static void assertDequeEquals(ArrayDeque<Integer> expected, ArrayDeque<Integer> actual) {
		assertArrayEquals(expected.toArray(), actual.toArray());
	}
}
