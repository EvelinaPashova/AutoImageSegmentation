
package lv.lumii.pixelmaster.modules.spw.domain.graph;
import java.util.*;

/**
 * Class representing an iterator through edges of UGraph.
 * Since graph is non-oriented, each edge will be returned once.
 * For example, if the graph contains two adjacent vertices v1 and v2,
 * iterator will return 1 edge - [v1, v2] or [v2, v1], but not both of them.
 * 
 * @author Jevgenijs Jonass
 */
final class EdgeIterator implements Iterator<GraphEdge> {

	/** Set containing vertices that have been iterated through */
	private Set<Vertex> iterated;

	/** Iterator that points to the next vertex which is not iterated through */
	private Iterator<Vertex> it;

	/** Iterator that points to the next neighbour of vertex v1 */
	private Iterator <Vertex> neighbours;
	private Vertex v1, v2;

	/**
	 * Constructor that creates an iterator through all edges of the graph
	 * @param g cannot be null
	 */
	EdgeIterator(UGraph g) {
		iterated=new HashSet<Vertex>();
		it=g.vertices();
		if (!it.hasNext()) {
			v1=v2=null;
			return;
		}
		v1=it.next();
		neighbours=v1.neighbours();
		while (!neighbours.hasNext()) {
			if (!it.hasNext()) {
				v1=v2=null;
				return;
			}
			v1=it.next();
			neighbours=v1.neighbours();
		}
		v2=neighbours.next();
	}
	
	/**
	 * {@inheritDoc}
	 * @return <tt>true</tt> if the iterator has more elements
	 */
	public boolean hasNext() { return v1!=null && v2!=null; }

	/**
	 * Returns the next edge of the graph. This method may be called repeatedly
	 * to iterate through the edges.
	 * @pre this.hasNext()==true
	 * @return The next element
	 */
	public GraphEdge next() {
	//	if (v1==null || v2==null) throw new NoSuchElementException();
		assert !(v1==null || v2==null);
		GraphEdge edge=new GraphEdge (v1, v2);
		
		do {
			while (!neighbours.hasNext()) {
				iterated.add(v1);
				if (!it.hasNext()) {
					v1=v2=null;
					return edge;
				}
				v1=it.next();
				neighbours=v1.neighbours();
			}
			v2=neighbours.next();
		} while (iterated.contains(v2));
		return edge;
	}

	/** Unsupported */
	public void remove() {
		assert false;
	//	throw new UnsupportedOperationException("Not supported yet.");
	}
}
