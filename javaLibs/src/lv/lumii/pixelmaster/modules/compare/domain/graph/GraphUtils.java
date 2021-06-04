package lv.lumii.pixelmaster.modules.compare.domain.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A collection of utilities to assist with graph manipulation.
 *
 */
public abstract class GraphUtils
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Adds the specified source and target vertices to the graph, if not
     * already included, and creates a new edge and adds it to the specified
     * graph similarly to the {@link Graph#addEdge(Object, Object)} method.
     *
     * @param g the graph for which the specified edge to be added.
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     *
     * @return The newly created edge if added to the graph, otherwise <code>
     * null</code>.
     */
    public static <V, E> E addEdgeWithVertices(
        Graph<V, E> g,
        V sourceVertex,
        V targetVertex)
    {
        g.addVertex(sourceVertex);
        g.addVertex(targetVertex);

        return g.addEdge(sourceVertex, targetVertex);
    }

    /**
     * Adds the specified edge to the graph, including its vertices if not
     * already included.
     *
     * @param targetGraph the graph for which the specified edge to be added.
     * @param sourceGraph the graph in which the specified edge is already
     * present
     * @param edge edge to add
     *
     * @return <tt>true</tt> if the target graph did not already contain the
     * specified edge.
     */
    public static <V, E> boolean addEdgeWithVertices(
        Graph<V, E> targetGraph,
        Graph<V, E> sourceGraph,
        E edge)
    {
        V sourceVertex = sourceGraph.getEdgeSource(edge);
        V targetVertex = sourceGraph.getEdgeTarget(edge);

        targetGraph.addVertex(sourceVertex);
        targetGraph.addVertex(targetVertex);

        return targetGraph.addEdge(sourceVertex, targetVertex, edge);
    }


        /**
     * Adds all the vertices and all the edges of the specified source graph to
     * the specified destination graph. First all vertices of the source graph
     * are added to the destination graph. Then every edge of the source graph
     * is added to the destination graph. This method returns <code>true</code>
     * if the destination graph has been modified as a result of this operation,
     * otherwise it returns <code>false</code>.
     *
     * <p>The behavior of this operation is undefined if any of the specified
     * graphs is modified while operation is in progress.</p>
     *
     * @param destination the graph to which vertices and edges are added.
     * @param source the graph used as source for vertices and edges to add.
     *
     * @return <code>true</code> if and only if the destination graph has been
     * changed as a result of this operation.
     */
    public static <V, E> boolean addGraph(
        Graph<? super V, ? super E> destination,
        Graph<V, E> source)
    {
        boolean modified = addAllVertices(destination, source.vertexSet());
        modified |= addAllEdges(destination, source, source.edgeSet());

        return modified;
    }

    /**
     * Adds a subset of the edges of the specified source graph to the specified
     * destination graph. The behavior of this operation is undefined if either
     * of the graphs is modified while the operation is in progress. {@link
     * #addEdgeWithVertices} is used for the transfer, so source vertexes will
     * be added automatically to the target graph.
     *
     * @param destination the graph to which edges are to be added
     * @param source the graph used as a source for edges to add
     * @param edges the edges to be added
     *
     * @return <tt>true</tt> if this graph changed as a result of the call
     */
    public static <V, E> boolean addAllEdges(
        Graph<? super V, ? super E> destination,
        Graph<V, E> source,
        Collection<? extends E> edges)
    {
        boolean modified = false;

        for (E e : edges) {
            V s = source.getEdgeSource(e);
            V t = source.getEdgeTarget(e);
            destination.addVertex(s);
            destination.addVertex(t);
            modified |= destination.addEdge(s, t, e);
        }

        return modified;
    }

    /**
     * Adds all of the specified vertices to the destination graph. The behavior
     * of this operation is undefined if the specified vertex collection is
     * modified while the operation is in progress. This method will invoke the
     * {@link Graph#addVertex(Object)} method.
     *
     * @param destination the graph to which edges are to be added
     * @param vertices the vertices to be added to the graph.
     *
     * @return <tt>true</tt> if graph changed as a result of the call
     *
     * @throws NullPointerException if the specified vertices contains one or
     * more null vertices, or if the specified vertex collection is <tt>
     * null</tt>.
     *
     * @see Graph#addVertex(Object)
     */
    public static <V, E> boolean addAllVertices(
        Graph<? super V, ? super E> destination,
        Collection<? extends V> vertices)
    {
        boolean modified = false;

        for (V v : vertices) {
            modified |= destination.addVertex(v);
        }

        return modified;
    }

    /**
     * Returns a list of vertices that are the neighbors of a specified vertex.
     *
     * @param g the graph to look for neighbors in.
     * @param vertex the vertex to get the neighbors of.
     *
     * @return a list of the vertices that are the neighbors of the specified
     * vertex.
     */
    public static <V, E> List<V> neighborListOf(Graph<V, E> g,
        V vertex)
    {
        List<V> neighbors = new ArrayList<V>();

        for (E e : g.edgesOf(vertex)) {
            neighbors.add(getOppositeVertex(g, e, vertex));
        }

        return neighbors;
    }

    /**
     * Returns a list of vertices that are the direct predecessors of a
     * specified vertex.
     *
     * @param g the graph to look for predecessors in.
     * @param vertex the vertex to get the predecessors of.
     *
     * @return a list of the vertices that are the direct predecessors of the
     * specified vertex.
     */
    public static <V, E> List<V> predecessorListOf(
        DGraph<V, E> g,
        V vertex)
    {
        List<V> predecessors = new ArrayList<V>();
        List<? extends E> edges = g.incomingEdgesOf(vertex);

        for (E e : edges) {
            predecessors.add(getOppositeVertex(g, e, vertex));
        }

        return predecessors;
    }

    /**
     * Returns a list of vertices that are the direct successors of a specified
     * vertex.
     *
     * @param g the graph to look for successors in.
     * @param vertex the vertex to get the successors of.
     *
     * @return a list of the vertices that are the direct successors of the
     * specified vertex.
     */
    public static <V, E> List<V> successorListOf(
        DGraph<V, E> g,
        V vertex)
    {
        List<V> successors = new ArrayList<V>();
        List<? extends E> edges = g.outgoingEdgesOf(vertex);

        for (E e : edges) {
            successors.add(getOppositeVertex(g, e, vertex));
        }

        return successors;
    }


    /**
     * Tests whether an edge is incident to a vertex.
     *
     * @param g graph containing e and v
     * @param e edge in g
     * @param v vertex in g
     *
     * @return true iff e is incident on v
     */
    public static <V, E> boolean testIncidence(Graph<V, E> g, E e, V v)
    {
        return (g.getEdgeSource(e).equals(v))
            || (g.getEdgeTarget(e).equals(v));
    }

    /**
     * Gets the vertex opposite another vertex across an edge.
     *
     * @param g graph containing e and v
     * @param e edge in g
     * @param v vertex in g
     *
     * @return vertex opposite to v across e
     */
    public static <V, E> V getOppositeVertex(Graph<V, E> g, E e, V v)
    {
        V source = g.getEdgeSource(e);
        V target = g.getEdgeTarget(e);
        if (v.equals(source)) {
            return target;
        } else if (v.equals(target)) {
            return source;
        } else {
            throw new IllegalArgumentException("no such vertex");
        }
    }

}

