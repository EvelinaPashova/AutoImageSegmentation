
package lv.lumii.pixelmaster.modules.spw.domain;

/**
 * Class representing an ordered pair of objects
 * @param <T1> Type of the first element
 * @param <T2> Type of the second element
 * @author Jevgenijs Jonass
 */
public final class Pair <T1, T2> {
	/** The first element */
	public T1 first;

	/** The second element */
	public T2 second;

	/** Constructor that initializes all elements with null values */
	public Pair() { }

	/**
	 * Constructor
	 * @param f The first element, can be null
	 * @param s The second element, can be null
	 */
	public Pair(T1 f, T2 s) {
		first = f;
		second = s;
	}
	
	/**
	 * Returns a string representation of the object
	 * @return non-null pointer
	 */
	public String toString() {
		return "{" + first == null ? "null" : first.toString() + "; " +
			second == null ? "null" : second.toString() + "}";
	}

	/**
	 * Determines whether or not two pairs are equal.
	 * Two instances of <code>Pair</code> p1 and p2 are equal
	 * if their elements (pointers) are equal.
	 * 
	 * @param obj {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Pair)) return super.equals(obj);
		final Pair other = (Pair) obj;
		return first == other.first && second == other.second;
	}

	/** {@inheritDoc} */
	public int hashCode() {
		int hash = 5;
		hash = 37 * hash + (this.first != null ? this.first.hashCode() : 0)
				+ (this.second != null ? this.second.hashCode() : 0);
		return hash;
	}
}
