
package lv.lumii.pixelmaster.modules.spw.domain.graph;
import java.util.*;

/**
 * Class encapsulating the standard iterator.
 * Does not allow structural modification of the collection.
 * @author Jevgenijs Jonass
 */
final class VertexIterator implements Iterator<Vertex> {

	/** The encapsulated iterator */
	private Iterator<Vertex> itr;

	/**
	 * Constructor
	 * @param itr The encapsulated iterator, cannot be null
	 */
	VertexIterator(Iterator<Vertex> itr) {
		assert !(itr==null);
		this.itr=itr;
	}

	/** {@inheritDoc} */
	public boolean hasNext() { return itr.hasNext(); }

	/**
	 * Returns the next element in the list.
	 * This method may be called repeatedly to iterate through the list.
	 * @pre this.hasNext()==true
	 * @return The next element
	 */
	public Vertex next() {
		assert hasNext();
		return itr.next();
	}

	/** Unsupported */
	public void remove() {
		assert false;
	//	throw new UnsupportedOperationException("Not supported yet.");
	}
}
