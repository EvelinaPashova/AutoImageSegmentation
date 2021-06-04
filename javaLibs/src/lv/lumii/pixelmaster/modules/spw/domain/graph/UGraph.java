
package lv.lumii.pixelmaster.modules.spw.domain.graph;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Class representing non-oriented graph.
 *
 * @author Jevgenijs Jonass
 */
public final class UGraph implements Cloneable {

	/** Number of edges in the graph */
	int nEdges;

	/**
	 * Vertices of the graph.
	 *
	 * It was decided to use Hashtable instead of the HashSet, because
	 * HashSet does not provide a way to retrieve the pointer to an object
	 * contained in this set:

		Set<Vertex> set = new HashSet<Vertex>();
		Vertex v = new Vertex(35, 12);
		set.add(v);
		// add 100 more objects
		v = null;
		// how to get pointer to the vertex at (35, 12)?
		// only by iterating through all elements

	 * With the Hashtable, one can get the value corresponding to any key:

		Hashtable<Vertex, Vertex> hashtable = new Hashtable<Vertex, Vertex>();
		Vertex v = new Vertex(35, 12);
		hashtable.put(v, v);
		// put 100 more objects
		v = null;
		// how to get pointer to the vertex at (35, 12)?
		Vertex d = new Vertex(35, 12);
		v = hashtable.get(d);
		assert d != v;

	 *
	 */
	Hashtable <Vertex, Vertex> vertices;
	
	/** Constructor that creates an empty graph */
	public UGraph() {
		vertices=new Hashtable <Vertex, Vertex> ();
		nEdges=0;
	}

	/**
	 * Creates a deep copy of this graph. Allocates memory for
	 * <code>vertices</code> and for all objects in it. The returned graph
	 * contains vertices at the same coordinates as vertices in this graph and
	 * has the same edges.
	 *
	 * @post class invariant of the returned <code>UGraph</code> is true
	 * @return Cloned object. Cannot be null.
	 */
	@Override
	public Object clone() {
		UGraph g=new UGraph();
		Iterator <Vertex> it=vertices();
		while (it.hasNext()) {
			Vertex v=it.next();
			g.addVertex(v.getX(), v.getY());
		}
		EdgeIterator edgeIterator=new EdgeIterator(this);
		while (edgeIterator.hasNext()) {
			GraphEdge edge=edgeIterator.next();
			g.addEdge(edge.first.getX(), edge.first.getY(),
					edge.second.getX(), edge.second.getY());
		}
		assert g.invariant();
		return g;
	}

	/** Clears the graph (deletes all vertices and edges) */
	public void clear() {
		assert invariant();
		Iterator <Vertex> it=vertices();
		while (it.hasNext()) {
			Vertex v=it.next();
			v.neighbours = null;
			v.owner = null;
		}
		vertices.clear();
		nEdges=0;
		assert invariant();
	}
	
	/**
	 * Creates and adds a vertex at specified coordinates and returns pointer
	 * to the newly allocated Vertex object. If the graph already contains
	 * vertex at given coordinates, no changes will be made and false will be
	 * returned. If the vertex was successfully added, it will have no
	 * neighbours after the method returns.
	 * 
	 * @param x The x coordinate of the vertex
	 * @param y The y coordinate of the vertex
	 * @return pointer to a Vertex object or <tt>null</tt> if the vertex was not added
	 */
	public Vertex addVertex(int x, int y) {
		assert invariant();
		Vertex v=new Vertex(x, y);
		if (vertices.containsKey(v)) return null;
		v.neighbours=new ArrayList<Vertex> ();
		v.owner=this;
		vertices.put(v, v);
		assert invariant();
		return v;
	}

	/**
	 * Deletes specified vertex from this graph. If this graph does not contain
	 * specified vertex, no changes will be made.
	 * @param v the vertex, cannot be null
	 * @return true if the graph changed as a result of the call
	 */
	public boolean deleteVertex(Vertex v) {
		assert !(v==null);
		assert invariant();
		if (v.owner!=this) return false;
		return v.delete();
	}

	/**
	 * Returns the total number of edges in the graph
	 * @return Total number of edges in the graph
	 */
	public int nEdges() { return nEdges; }

	/**
	 * Returns the number of vertices in the graph
	 * @return Total number of vertices in the graph
	 */
	public int size() { return vertices.size(); }

	/**
	 * Finds vertex at specified coordinates
	 * @param x The x coordinate of the vertex
	 * @param y The y coordinate of the vertex
	 * @post returned Vertex is not null =&gt; [returned Vertex].owner==this
	 * @return pointer to a vertex or null if the graph does not contain vertex
	 *		at given coordinates.
	 */
	public Vertex get(int x, int y) { return vertices.get(new Vertex(x, y)); }

	/**
	 * Returns the iterator through all vertices in this graph.
	 * The iterator does not allow structural modification of the vertex set.
	 * If you add/delete vertices from the graph while iterating through
	 * the vertices, behaviour of the iterator methods is undefined
	 * (it is not guaranteed that they will raise an exception), but you can
	 * add/delete edges.
	 *
	 * <p>The returned vertex iterator will return pointers to Vertex objects
	 * that belong to this.vertices (and not to their copies).</p>
	 * 
	 * @return Vertex iterator
	 */
	public Iterator<Vertex> vertices() {
		return new VertexIterator(vertices.keySet().iterator());
	}

	/**
	 * Returns an iterator through all edges of the graph.
	 *
	 * @return Edge iterator
	 */
	public Iterator<GraphEdge> edges() {
		return new EdgeIterator(this);
	}

	/**
	 * Finds two vertices at specified coordinates and
	 * adds an edge that connects them. If this graph does not contain any
	 * of the vertices or if they are adjacent, no changes will be made.
	 * 
	 * @param x1 X coordinate of the first vertex
	 * @param y1 Y coordinate of the first vertex
	 * @param x2 X coordinate of the second vertex
	 * @param y2 Y coordinate of the second vertex
	 * @return true if the graph changed as a result of the call
	 */
	public boolean addEdge(int x1, int y1, int x2, int y2) {
		assert invariant();
		Vertex v=get(x1, y1);
		if (v==null) return false;
		return v.addNeighbour(x2, y2);
	}

	/**
	 * Finds two vertices at specified coordinates and checks if they are adjacent.
	 * @param x1 X coordinate of the first vertex
	 * @param y1 Y coordinate of the first vertex
	 * @param x2 X coordinate of the second vertex
	 * @param y2 Y coordinate of the second vertex
	 * @return true if this graph contains both vertices and they are adjacent
	 */
	public boolean adjacent(int x1, int y1, int x2, int y2) {
		Vertex v1=get(x1, y1);
		if (v1==null) return false;
		Vertex v2=get(x2, y2);
		if (v2==null) return false;
		return v1.adjacent(v2);
	}

	/**
	 * Finds two vertices at specified coordinates and
	 * deletes edge that connects them. If this graph does not contain any
	 * of the vertices or if they are not adjacent, no changes will be made.
	 * 
	 * @param x1 X coordinate of the first vertex
	 * @param y1 Y coordinate of the first vertex
	 * @param x2 X coordinate of the second vertex
	 * @param y2 Y coordinate of the second vertex
	 * @return true if the graph changed as a result of the call
	 */
	public boolean deleteEdge(int x1, int y1, int x2, int y2) {
		assert invariant();
		Vertex v=get(x1, y1);
		if (v==null) return false;
		return v.deleteNeighbour(x2, y2);
	}

	/**
	 * Finds vertex at specified coordinates and returns its degree (number of
	 * edges that connect to it)
	 * @param x X coordinate of the vertex
	 * @param y Y coordinate of the vertex
	 * @return degree of the vertex or -1 if the graph does not contain vertex
	 *		at specified coordinates
	 */
	public int degree(int x, int y) {
		Vertex v=get(x, y);
		if (v==null) return -1;
		return v.degree();
	}

	/**
	 * Finds vertex at specified coordinates and returns iterator through
	 * its neighbours.
	 * The iterator does not allow structural modification of the vertex set,
	 * but you can call <code>setLocation</code> method for any vertex.
	 * If you add/delete vertices or edges from the graph while iterating
	 * through the vertices, the behaviour of the iterator methods is undefined
	 * (it is not guaranteed that they will raise an exception).
	 *
	 * <p>The returned vertex iterator will return pointers to Vertex objects
	 * that belong to this.vertices (and not to their copies).</p>
	 * 
	 * @param x X coordinate of the vertex
	 * @param y Y coordinate of the vertex
	 * @return Iterator or null if vertex at given coordinates does not exist
	 */
	public Iterator<Vertex> neighbours(int x, int y) {
		Vertex v=get(x, y);
		if (v==null) return null;
		return v.neighbours();
	}

	/**
	 * Finds vertex at specified coordinates and deletes it from this graph.
	 * If this graph does not contain specified vertex, no changes will be made.
	 *
	 * @param x X coordinate of the vertex
	 * @param y Y coordinate of the vertex
	 * @pre class invariant of <code>this</code> is true
	 * @post class invariant of <code>this</code> is true
	 * @return true if the graph changed as a result of the call
	 */
	public boolean deleteVertex(int x, int y) {
		assert invariant();
		Vertex v=get(x, y);
		if (v==null) return false;
		return v.delete();
	}

	/**
	 * Returns a set containing edges of this graph.
	 * <p>The returned Set will contain pointers to newly allocated
	 * GraphEdge objects, which contain pointers to Vertex objects that belong
	 * to this.vertices (and not to their copies).</p>
	 * 
	 * @return set containing all edges, cannot be null
	 */
	public Set<GraphEdge> getEdges() {
		EdgeIterator it=new EdgeIterator(this);
		GraphEdge edge;
		Set<GraphEdge> s=new HashSet<GraphEdge>();
		while (it.hasNext()) {
			edge=it.next();
			s.add(edge);
		}
		return s;
	}

	/**
	 * Returns a set containing vertices of this graph.
	 * <p>The returned Set will contain pointers to Vertex objects that
	 * belong to this.vertices (and not to their copies).</p>
	 *
	 * @return set containing all vertices, cannot be null
	 */
	public Set<Vertex> getVertices() {
		Iterator<Vertex> it = vertices();
		Vertex v;
		Set<Vertex> s=new HashSet<Vertex>();
		while (it.hasNext()) {
			v=it.next();
			s.add(v);
		}
		return s;
	}


    public class EdgelessGraphException     extends Exception {}
    public class WeightlessGraphException   extends Exception {} 

        /**
         *  Gets center of mass of the graph.
         *
         *  @throws EdgelessGraphException      Graph contains no edges.
         *  @throws WeightlessGraphException    Total length of graph edges is equal to zero. 
         *
         *  @return Returns new object of class java.awt.geom.Point2D.Double with coordinates of center of mass in Euclidean space.
         * 
         *  Added by Andrey Zhmakin, March-06-2010
         */

        public Point2D getCenterOfMass()
            throws EdgelessGraphException, WeightlessGraphException 
        {
            Iterator<GraphEdge> i = new EdgeIterator( this );

            if ( !i.hasNext() )
            {
                throw new EdgelessGraphException();
            }

            double  totalMass      = 0;
            Point2D totalRadius    = new Point2D.Double( 0, 0 );

            while ( i.hasNext() )
            {
                GraphEdge   edge    = i.next();
                
                double      mass    = edge.getLength();
                Point2D     radius  = edge.getMiddlePoint();

                totalMass += mass;

                totalRadius.setLocation( totalRadius.getX() + mass * radius.getX(),
                                         totalRadius.getY() + mass * radius.getY()
                                        );
            }

            if ( totalMass == 0 )
            {
                throw new WeightlessGraphException();
            }
            else
            {
                totalMass = 1.0 / totalMass;
            }

            return new Point2D.Double( totalRadius.getX() * totalMass,
                                       totalRadius.getY() * totalMass
                                      );
        }

    /**
     * Gets length of graph
     *
     * @throws EdgelessGraphException      Graph contains no edges.
     * @throws WeightlessGraphException    Total length of graph edges is equal to zero.
     *
     * @return total length of graph
     *
     * Added by Aivars Šāblis, 09.03.2010
     */

    public double getGraphLength() throws EdgelessGraphException, WeightlessGraphException
    {

        Iterator<GraphEdge> i = new EdgeIterator(this);

        if (!i.hasNext())
        {
            throw new EdgelessGraphException();
        }

        double totalLength = 0;

        while (i.hasNext())
        {
           GraphEdge edge = i.next();

           double edgeLength = edge.getLength();

           totalLength += edgeLength;
        }

        if (totalLength == 0)
        {
            throw new WeightlessGraphException();
        }

        return totalLength;
    }

	private boolean invariant() {
		assert vertices != null;
		assert nEdges == getEdges().size();
		for (Vertex v: getVertices()) {
			assert v.owner == this;
			for (Vertex w: getVertices()) {
				if (v != w) {
					assert v.neighbours != w.neighbours;
				}
			}

			// make sure that keys of the Hashtable are equal to the values
			for (Map.Entry<Vertex, Vertex> entry: vertices.entrySet()) {
				assert entry.getKey() == entry.getValue();
			}
		}
		return true;
	}
}
