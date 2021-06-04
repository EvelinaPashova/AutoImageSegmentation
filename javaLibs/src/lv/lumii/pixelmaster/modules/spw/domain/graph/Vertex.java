
package lv.lumii.pixelmaster.modules.spw.domain.graph;

import lv.lumii.pixelmaster.modules.spw.domain.Pair;
import java.awt.Point;
import java.util.*;

/**
 * Class representing graph vertex. One vertex belongs to no more that 1 graph.
 * After the vertex is added to a graph, it can be deleted, but it cannot be
 * added to another UGraph. After the vertex is deleted from the graph, it
 * cannot be added back or added to another graph.
 *
 * @author Jevgenijs Jonass
 */
public final class Vertex {

	/**
	 * The coordinates of the vertex.
	 */
	private int x, y;

	/**
	 * Pointer to the graph that contains this Vertex. The null value means that
	 * this Vertex does not belong to a graph.
	 */
	UGraph owner;

	/**
	 * Neighbours of this Vertex. The null value means that
	 * this Vertex does not belong to a graph.
	 *
	 * It was decided to use List instead of the HashSet, because
	 * otherwise the vertex would need to be deleted from
	 * all other vetices' lists of neighbours before its coordinates can
	 * be changed (for the same reason as described in the method
	 * "setLocation").
	 *
	 * In other words, if HashSet were used, then the following code would
	 * need to be added in the method "setLocation":

		for (Vertex neighbour: neighbours) {
			neighbour.neighbours.remove(this);
		}
		this.x = x;
		this.y = y;
		for (Vertex neighbour: neighbours) {
			neighbour.neighbours.add(this);
		}

	 * Since most of the graph optimization algorithms modify the coordinates
	 * of the vertices quite often, this code would introduce an overhead.
	 *
	 */
	List<Vertex> neighbours;

	/**
	 * Constructor that creates vertex that does not belong to a graph.
	 * Initializes the vertex with specified coordinates.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public Vertex(int x, int y) {
		this.x=x;
		this.y=y;
		owner=null;
		neighbours=null;
		assert invariant();
	}

	/** {@inheritDoc} */
	public int hashCode() {
		long bits = java.lang.Double.doubleToLongBits(x);
		bits ^= java.lang.Double.doubleToLongBits(y) * 31;
		return (((int) bits) ^ ((int) (bits >> 32)));
	}
	
	/**
	 * Determines whether or not two vertices are equal. Two instances of
	 * <code>Vertex</code> are equal if the values of their
	 * <code>x</code> and <code>y</code> member fields, representing
	 * their position in the coordinate space, are the same.
	 * 
	 * @param obj {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Vertex)) return super.equals(obj);
		final Vertex other = (Vertex) obj;
		return x == other.x && y == other.y;
	}

	/**
	 * Returns a string representation of this vertex and its location
	 * in the {@code (x,y)} coordinate space.
	 * @return non-null pointer
	 */
	public String toString() { return new Pair<Integer, Integer> (x, y).toString(); }

	/**
	 * Sets location of this vertex. If the graph already contains vertex at
	 * coordinates (x, y), no changes will be made. If this vertex does not belong to a
	 * graph, no changes will be made and false will be returned.
	 *
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return <tt>true</tt> if this vertex changed as a result of the call
	 */
	public boolean setLocation(int x, int y) {
		assert invariant();
		if (owner == null) return false;
		if (owner.get(x, y) != null) return false;

		/*
		 * !! Tricky code !!
		 *
		 * Very important to remove the vertex from the set...
		 * ... and ONLY THEN change its coordinates.
		 *
		 * It would lead to UNDEFINED BEHAVIOUR if the coordinates of the
		 * vertex change while this vertex is in the container,
		 * because the method "hashCode" of the class "Vertex" uses the
		 * coordinates to calculate the value it returns.
		 *
		 * As demonstrated by the code fragment below
		 * (Main.java in the default package), if the hash code
		 * of an object in a HashSet changes, it breaks invariant of the HashSet:

			import java.util.HashSet;
			import java.awt.Point;

			public class Main {
				public static void main(String[] args) {
					Point p = new Point(35, 12);
					HashSet<Point> hset = new HashSet<Point>();
					System.out.println(p.hashCode());
					hset.add(p);
					System.out.println(hset.contains(p));		// true
					p.x = 22;
					System.out.println(p.hashCode());
					System.out.println(hset.contains(p));		// undefined
				}
			}

		 * On my PC, it prints:

			-2070315008
			true
			-2064777216
			false

		 *
		 */
		owner.vertices.remove(this);
		this.x = x;
		this.y = y;
		owner.vertices.put(this, this);

		assert invariant();
		return true;
	}

	/**
	 * Adds a neighbour to this vertex. If the graph does not contain vertex at
	 * specified coordinates or it is adjacent to this vertex, no changes will
	 * be made. If this vertex does not belong to a
	 * graph, no changes will be made and false will be returned.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return true if the neighbour list changed as a result of the call
	 */
	public boolean addNeighbour(int x, int y) {
		assert invariant();
		if (owner==null) return false;
		Vertex v=owner.get(x, y);
		if (v==null) return false;
		assert invariant();
		return addNeighbour(v);
	}

	/**
	 * Adds a neighbour to the vertex. If the specified vertex belongs to
	 * another graph or it is adjacent to this vertex, no changes will be made.
	 * If this vertex does not belong to a graph, no changes will be made and false will be returned.
	 * 
	 * @param v the new neighbour of this vertex, cannot be null
	 * @return true if the neighbour list changed as a result of the call
	 */
	public boolean addNeighbour(Vertex v) {
		assert !(v==null);
		assert invariant();
		if (owner==null) return false;
		if (v==this) return false;
		if (v.owner!=owner) return false;
		if (neighbours.contains(v)) return false;
		neighbours.add(v);
		v.neighbours.add(this);
		owner.nEdges++;
		assert invariant();
		return true;
	}

	/**
	 * Checks if this vertex is adjacent to the given vertex. If the specified
	 * vertex belongs to another graph or if this vertex does not belong to any
	 * graph, returns false.
	 * 
	 * @param v the vertex, cannot be null
	 * @return true if the vertex is adjacent
	 */
	public boolean adjacent(Vertex v) {
		assert !(v==null);
		if (owner==null) return false;
		return v.owner==owner && neighbours.contains(v);
	}

	/**
	 * Returns degree of the vertex (number of edges that connect to it).
	 * If this vertex does not belong to a graph, -1 will be returned.
	 * @pre class invariant of <code>this</code> is true
	 * @post <code>this</code> remains unchanged
	 * @return Degree of the vertex
	 */
	public int degree() {
		if (owner==null) return -1;
		return neighbours.size();
	}

	/**
	 * Deletes a vertex from the neighbour list of this vertex. The neighbour
	 * is not deleted from the graph. If the graph does not contain specified
	 * vertex or it is not adjacent to this vertex, no changes will be made.
	 * If this vertex does not belong to a graph, no changes will be made and false will be returned.
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return true if the neighbour list changed as a result of the call
	 */
	public boolean deleteNeighbour(int x, int y) {
		assert invariant();
		if (owner==null) return false;
		Vertex v=owner.get(x, y);
		if (v==null) return false;
		return deleteNeighbour(v);
	}

	/**
	 * Deletes a vertex from the neighbour list of this vertex. The neighbour
	 * is not deleted from the graph. If the specified vertex belongs to another
	 * graph or it is not adjacent to this vertex, no changes will be made.
	 * If this vertex does not belong to a graph, no changes will be made and false will be returned.
	 * 
	 * @param v neighbour of this vertex, cannot be null
	 * @return true if the neighbour list changed as a result of the call.
	 */
	public boolean deleteNeighbour(Vertex v) {
		assert !(v==null);
		assert invariant();
		if (owner==null) return false;
		if (v.owner!=owner || !neighbours.remove(v)) return false;
		v.neighbours.remove(this);
		owner.nEdges--;
		assert invariant();
		return true;
	}

	/**
	 * Returns new Point object containing coordinates of this vertex.
	 * New memory is allocated for the returned object.
	 * @return Pointer to a new Point object, cannot be null
	 */
	public Point getLocation() { return new Point(x, y); }

	/**
	 * Returns iterator through all neighbours of this vertex.
	 * The iterator does not allow structural modification of the vertex set,
	 * but you can call <code>setLocation</code> method for any vertex.
	 * If you add/delete vertices or edges from the graph while iterating
	 * through the vertices, the behaviour of the iterator methods is undefined
	 * (it is not guaranteed that they will raise an exception).
	 * 
	 * <p>The returned Iterator will return pointers to Vertex objects that
	 * belong to this.neighbours (and not to their copies).</p>
	 * 
	 * @return neighbour iterator or null if this vertex does not belong to a graph
	 */
	public Iterator<Vertex> neighbours() {
		if (owner==null) return null;
		return new VertexIterator(neighbours.iterator());
	}

	/**
	 * Returns the x coordinate of the vertex
	 * @return The x coordinate
	 */
	public int getX() { return x; }

	/**
	 * Returns the y coordinate of the vertex
	 * @return The y coordinate
	 */
	public int getY() { return y; }

	/**
	 * Deletes this vertex from the graph. If this vertex does not belong to a
	 * graph, no changes will be made and false will be returned.
	 * @return true if graph containing this vertex changed as a result of the call
	 */
	public boolean delete() {
		assert invariant();
		if (owner==null) return false;
		Iterator <Vertex> itr=neighbours.iterator();
		while (itr.hasNext()) {
			Vertex v=itr.next();
			v.neighbours.remove(this);
			owner.nEdges--;
		}
		owner.vertices.remove(this);
		owner=null;
		neighbours = null;
		assert invariant();
		return true;
	}

	/**
	 * Merges this vertex with vertices in <code>v</code>.
	 * 
	 * <p>First, constructs a set S containing neighbours of all vertices in
	 * <code>v</code>. After that, for each vertex p in S, such that p!=this,
	 * adds an edge connecting p and this vertex. Finally, all vertices p
	 * in v such that p!=this are deleted from the graph.
	 * Note that <code>v</code> is allowed to contain pointer to <code>this</code></p>
	 * 
	 * <p>If this vertex does not belong to any graph, no changes will be made and false will be returned.</p>
	 * 
	 * @param v array of non-null pointers, cannot be null
	 * @return true if vertices were successfully merged
	 */
	public boolean merge(Vertex... v) {
		assert !(v==null);
		assert invariant();
		if (owner==null) return false;
		Set <Vertex> S=new HashSet <Vertex> (); // neighbours
		for (Vertex p: v) {
			if (p.owner != owner) return false;
			S.addAll(p.neighbours);
		}
		for (Vertex p: S) {
			addNeighbour(p);
		}
		for (Vertex p: v) {
			if (p!=this) p.delete();
		}
		assert invariant();
		return true;
	}

	private boolean invariant() {
		assert (owner == null) == (neighbours == null);
		if (neighbours != null) {
			assert !neighbours.contains(this);
			assert owner.vertices.contains(this);

			// uniqueness of elements in the list
			for (int i = 0; i < neighbours.size(); ++i) {
				assert !neighbours.subList(0, i).contains(neighbours.get(i));
			}

			for (Vertex v: neighbours) {
				assert v.neighbours.contains(this);
				assert v.owner == owner;
			}
		}
		return true;
	}
}
