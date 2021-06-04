
package lv.lumii.pixelmaster.modules.spw.domain.graph;

/**
 * Class representing edge of non-oriented graph
 * @author Jevgenijs Jonass
 */
public final class GraphEdge {

	/** The first element */
	public Vertex first;

	/** The second element */
	public Vertex second;

	/**
	 * Constructor
	 * @param f The first vertex, cannot be null
	 * @param s The second vertex, cannot be null
	 */
	public GraphEdge(Vertex f, Vertex s) {
		assert f != null && s != null;
		first = f;
		second = s;
	}

	/**
	 * Determines whether or not two edges are equal.
	 * Two instances of <code>GraphEdge</code> e1 and e2 are equal if
	 * <code>(e1.first.equals(e2.first) && e1.second.equals(e2.second)) ||
	 * (e1.first.equals(e2.second) && e1.second.equals(e2.first))</code>
	 *
	 * <p>In other words, two (non-oriented) edges are equal if the
	 * vertices they connect have the same locations.</p>
	 *
	 * @param obj {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof GraphEdge)) return super.equals(obj);
		final GraphEdge other = (GraphEdge) obj;
		return (first.equals(other.first) && second.equals(other.second)) ||
			(first.equals(other.second) && second.equals(other.first));
	}

	/** {@inheritDoc} */
	public int hashCode() {
		int hash = 5;
		hash = 37 * hash + (this.first.hashCode())
				+ (this.second.hashCode());
		return hash;
	}

	/**
	 * {@inheritDoc}
	 * @pre class invariant of <code>this</code> is true
	 * @post <code>this</code> remains unchanged
	 * @return a string representation of the object.
	 * @author Jevgenijs Jonass.
	 * @since 04.05.2009
	 */
	@Override
	public String toString() {
		return "{" + first.toString() + ", " + second.toString() + "}";
	}

    public Vertex vertexFirst() 
    {
        return this.first;
    }

    public Vertex vertexSecond()
    {
        return this.second;
    }

    /**
     *  Gets the point which is in the middle of the rout between vertices of the edge.
     *
     *  @return Point2D object with Euclidean coordinates of the middle point of the edge calculated with double precision.
     *
     *  Added by Andrey Zhmakin, March-06-2010
     */

    public java.awt.geom.Point2D getMiddlePoint()
    {
        return new java.awt.geom.Point2D.Double( 0.5 * (double)( this.first.getX() + this.second.getX() ),
                                                 0.5 * (double)( this.first.getY() + this.second.getY() ) );
    }
    
    /**
     *  Gets the length of the edge.
     *
     *  @return Length of the edge of the graph.
     *
     *  Added by Andrey Zhmakin, March-06-2010
     */

    public double getLength()
    {
        return java.awt.geom.Point2D.Double.distance( this.first.getX(), this.first.getY(), this.second.getX(), this.second.getY() );
        /*
        return Math.sqrt(   zhmakin.Math.sqr( (double)( this.first.getX() - this.second.getX() ) )
                          + zhmakin.Math.sqr( (double)( this.first.getY() - this.second.getY() ) )
                         );
        */
    }

	private boolean invariant() {
		assert first != null && second != null;
		return true;
	}
}
