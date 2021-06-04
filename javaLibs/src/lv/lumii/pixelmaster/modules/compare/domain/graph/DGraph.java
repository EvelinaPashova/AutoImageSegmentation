package lv.lumii.pixelmaster.modules.compare.domain.graph;

import java.util.*;

/**
 *
 */
public interface DGraph<V, E> extends Graph<V,E>
{
    /**
     * Returns the "in degree" of the specified vertex. An in degree of a vertex
     *
     * @param Vertex vertex whose degree is to be calculated.
     *
     * @return the degree of the specified vertex.
     */
    public int inDegreeOf(V Vertex);


    /**
     * Returns a set of all edges incoming into the specified vertex.
     *
     * @param Vertex the vertex for which the list of incoming edges to be
     * returned.
     *
     * @return a set of all edges incoming into the specified vertex.
     */
    public List<E> incomingEdgesOf(V Vertex);


    /**
     * Returns the "out degree" of the specified vertex. An out degree of a
     * vertex in a directed graph is the number of outward directed edges from
     * that vertex.
     *
     * @param Vertex vertex whose degree is to be calculated.
     *
     * @return the degree of the specified vertex.
     */
    public int outDegreeOf(V Vertex);


    /**
     * Returns a set of all edges outgoing from the specified vertex.
     *
     * @param Vertex the vertex for which the list of outgoing edges to be
     * returned.
     *
     * @return a set of all edges outgoing from the specified vertex.
     */
    public List<E> outgoingEdgesOf(V Vertex);
}
