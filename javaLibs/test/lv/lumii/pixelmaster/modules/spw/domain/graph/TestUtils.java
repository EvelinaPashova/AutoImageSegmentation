
package lv.lumii.pixelmaster.modules.spw.domain.graph;

import java.util.*;

final class IteratorConsumer<T> {

	private Set<T> hset = new HashSet<T>();

	IteratorConsumer(Iterator<T> it) {
		while (it.hasNext()) {
			T elem = it.next();
			hset.add(elem);
		}
	}

	Set<T> getContents() {
		return hset;
	}
}
