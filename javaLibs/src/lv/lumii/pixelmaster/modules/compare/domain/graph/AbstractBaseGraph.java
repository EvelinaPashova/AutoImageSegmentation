package lv.lumii.pixelmaster.modules.compare.domain.graph;

import java.util.*;

/**
 *
 */
public abstract class AbstractBaseGraph<V, E>
                extends AbstractGraph<V, E>
                implements Graph<V, E>,
                           Cloneable
{
    //~ Static fields/initializers ---------------------------------------------

    private static final String LOOPS_NOT_ALLOWED = "loops not allowed";

    //~ Instance fields --------------------------------------------------------

    private EdgeCreator<V, E> edgeCreator;
    private EdgeSetCreator<V, E> edgeSetCreator;
    private LinkedHashMap<E, IntrusiveEdge> edgeMap;

    private transient TypeUtil<V> vertexTypeDecl = null;

    private LinkedHashMap<V, DirectedEdgeContainer<V, E>> vertexMapDirected =
        new LinkedHashMap<V, DirectedEdgeContainer<V, E>>();
    
    
    //~ Constructors -----------------------------------------------------------

    /**
     * Construct a new pseudograph.
     *
     * @param ef the edge factory of the new graph.
     *
     * @throws NullPointerException if the specified edge creator is <code>
     * null</code>.
     */
    public AbstractBaseGraph(EdgeCreator<V, E> ef)
    {
        if (ef == null) {
            throw new NullPointerException();
        }

        edgeMap = new LinkedHashMap<E, IntrusiveEdge>();
        edgeCreator = ef;

        this.edgeSetCreator = new ArrayListFactory<V, E>();
    }


    //~ Methods ----------------------------------------------------------------

    /**
     * @see Graph#getAllEdges(Object, Object)
     */
    public List<E> getAllEdges(V sourceVertex, V targetVertex)
    {
        List<E> edges = null;

        if (containsVertex(sourceVertex) && containsVertex(targetVertex))
        {
            edges = new ArrayList<E>();

            DirectedEdgeContainer<V, E> ec = getEdgeContainer(sourceVertex);

            for (E anOutgoing : ec.outgoing) {

                if (getEdgeTarget(anOutgoing).equals(targetVertex)) {
                    edges.add(anOutgoing);
                }
            }
         }

        return edges;
    }

    /**
     * @see Graph#getEdge(Object, Object)
     */
    public E getEdge(V sourceVertex, V targetVertex)
    {
        if (containsVertex(sourceVertex)
            && containsVertex(targetVertex))
        {
            DirectedEdgeContainer<V, E> ec = getEdgeContainer(sourceVertex);

            for (E anOutgoing : ec.outgoing) {

                if (getEdgeTarget(anOutgoing).equals(targetVertex)) {
                    return anOutgoing;
                }
            }
         }

         return null;
    }

    /**
     * @see Graph#getEdgeCreator()
     * @return edgeCreator edge creator
     */
    public EdgeCreator<V, E> getEdgeCreator()
    {
        return edgeCreator;
    }

    /**
     * Set the {@link EdgeSetCreator} to use for this graph. Initially, a graph
     * is created with a default implementation which always supplies an {@link
     * java.util.ArrayList} with capacity 1.
     *
     * @param edgeSetCreator creator to use for subsequently created edge sets
     * (this call has no effect on existing edge sets)
     */
    public void setEdgeSetFactory(EdgeSetCreator<V, E> edgeSetCreator)
    {
        this.edgeSetCreator = edgeSetCreator;
    }

    /**
     * @see Graph#addEdge(Object, Object)
     */
    public E addEdge(V sourceVertex, V targetVertex)
    {
        assertVertexExist(sourceVertex);
        assertVertexExist(targetVertex);

        if (containsEdge(sourceVertex, targetVertex))
        {
            return null;
        }

        /*if (sourceVertex.equals(targetVertex)) {
            throw new IllegalArgumentException(LOOPS_NOT_ALLOWED);
        }  */

        E e = edgeCreator.createEdge(sourceVertex, targetVertex);

        if (containsEdge(e)) { // this restriction should stay!

            return null;
        } else {
            IntrusiveEdge intrusiveEdge =
                createIntrusiveEdge(e, sourceVertex, targetVertex);

            edgeMap.put(e, intrusiveEdge);
            V source = getEdgeSource(e);
            V target = getEdgeTarget(e);

            getEdgeContainer(source).addOutgoingEdge(e);
            getEdgeContainer(target).addIncomingEdge(e);

            return e;
        }
    }

    /**
     * @see Graph#addEdge(Object, Object, Object)
     */
    public boolean addEdge(V sourceVertex, V targetVertex, E e)
    {
        if (e == null) {
            throw new NullPointerException();
        } else if (containsEdge(e)) {
            return false;
        }

        assertVertexExist(sourceVertex);
        assertVertexExist(targetVertex);

        if (containsEdge(sourceVertex, targetVertex))
        {
            return false;
        }

        /*if (sourceVertex.equals(targetVertex)) {
            throw new IllegalArgumentException(LOOPS_NOT_ALLOWED);
        } */

        IntrusiveEdge intrusiveEdge =
            createIntrusiveEdge(e, sourceVertex, targetVertex);

        edgeMap.put(e, intrusiveEdge);
        V source = getEdgeSource(e);
        V target = getEdgeTarget(e);

        getEdgeContainer(source).addOutgoingEdge(e);
        getEdgeContainer(target).addIncomingEdge(e);

        return true;
    }

    private IntrusiveEdge createIntrusiveEdge(
        E e,
        V sourceVertex,
        V targetVertex)
    {
        IntrusiveEdge intrusiveEdge;
        if (e instanceof IntrusiveEdge) {
            intrusiveEdge = (IntrusiveEdge) e;
        } else {
            intrusiveEdge = new IntrusiveEdge();
        }
        intrusiveEdge.source = sourceVertex;
        intrusiveEdge.target = targetVertex;
        return intrusiveEdge;
    }

    /**
     * @see Graph#addVertex(Object)
     */
    public boolean addVertex(V v)
    {
        if (v == null) {
            throw new NullPointerException();
        } else if (containsVertex(v)) {
            return false;
        } else {
            // add with a lazy edge container entry
            vertexMapDirected.put(v, null);

            return true;
        }
    }

    /**
     * @see Graph#getEdgeSource(Object)
     */
    public V getEdgeSource(E e)
    {
        return TypeUtil.uncheckedCast(
            getIntrusiveEdge(e).source,
            vertexTypeDecl);
    }

    /**
     * @see Graph#getEdgeTarget(Object)
     */
    public V getEdgeTarget(E e)
    {
        return TypeUtil.uncheckedCast(
            getIntrusiveEdge(e).target,
            vertexTypeDecl);
    }

    private IntrusiveEdge getIntrusiveEdge(E e)
    {
        if (e instanceof IntrusiveEdge) {
            return (IntrusiveEdge) e;
        }

        return edgeMap.get(e);
    }

    /**
     * Returns a shallow copy of this graph instance. Neither edges nor vertices
     * are cloned.
     *
     * @return a shallow copy of this set.
     *
     * @throws RuntimeException
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        try {
            TypeUtil<AbstractBaseGraph<V, E>> typeDecl = null;

            AbstractBaseGraph<V, E> newGraph =
                TypeUtil.uncheckedCast(super.clone(), typeDecl);

            newGraph.edgeMap = new LinkedHashMap<E, IntrusiveEdge>();

            newGraph.edgeCreator = this.edgeCreator;

            GraphUtils.addGraph(newGraph, this);

            return newGraph;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } 
    }

    /**
     * @see Graph#containsEdge(Object)
     */
    public boolean containsEdge(E e)
    {
        return edgeMap.containsKey(e);
    }

    /**
     * @see Graph#containsVertex(Object)
     */
    public boolean containsVertex(V v)
    {
        return vertexMapDirected.containsKey(v);
    }

    /**
     * @see Graph#edgeSet()
     */
    public List<E> edgeSet()
    {
        return Collections.unmodifiableList(new ArrayList<E>(edgeMap.keySet()));
    }

    /**
     * @see Graph#edgesOf(Object)
     */
    public List<E> edgesOf(V vertex)
    {
        ArrayList<E> inAndOut =
                new ArrayList<E>(getEdgeContainer(vertex).incoming);
        inAndOut.addAll(getEdgeContainer(vertex).outgoing);

        return Collections.unmodifiableList(inAndOut);
    }

    /**
     * @see DGraph#inDegreeOf(Object)
     */
    public int inDegreeOf(V vertex)
    {
        return getEdgeContainer(vertex).incoming.size();
    }

    /**
     * @see DGraph#incomingEdgesOf(Object)
     */
    public List<E> incomingEdgesOf(V vertex)
    {
        return getEdgeContainer(vertex).getIncomingEdges();
    }

    /**
     * @see DGraph#outDegreeOf(Object)
     */
    public int outDegreeOf(V vertex)
    {
        return getEdgeContainer(vertex).outgoing.size();
    }

    /**
     * @see DGraph#outgoingEdgesOf(Object)
     */
    public List<E> outgoingEdgesOf(V vertex)
    {
        return getEdgeContainer(vertex).getOutgoingEdges();
    }

    /**
     * @see Graph#removeEdge(Object, Object)
     */
    public E removeEdge(V sourceVertex, V targetVertex)
    {
        E e = getEdge(sourceVertex, targetVertex);

        if (e != null) {
            V source = getEdgeSource(e);
            V target = getEdgeTarget(e);

            getEdgeContainer(source).removeOutgoingEdge(e);
            getEdgeContainer(target).removeIncomingEdge(e);
            edgeMap.remove(e);
        }

        return e;
    }

    /**
     * @see Graph#removeEdge(Object)
     */
    public boolean removeEdge(E e)
    {
        if (containsEdge(e)) {
            V source = getEdgeSource(e);
            V target = getEdgeTarget(e);

            getEdgeContainer(source).removeOutgoingEdge(e);
            getEdgeContainer(target).removeIncomingEdge(e);
            edgeMap.remove(e);

            return true;
        } else {
            return false;
        }
    }

    /**
     * @see Graph#removeVertex(Object)
     */
    public boolean removeVertex(V v)
    {
        if (containsVertex(v)) {
            List<E> touchingEdgesList = edgesOf(v);

            // cannot iterate over list - will cause
            // ConcurrentModificationException
            removeAllEdges(new ArrayList<E>(touchingEdgesList));

            vertexMapDirected.remove(v); // remove the vertex itself

            return true;
        } else {
            return false;
        }
    }

    /**
     * @see Graph#vertexSet()
     */
    public List<V> vertexSet()
    {
        return Collections.synchronizedList(new ArrayList<V>(vertexMapDirected.keySet()));
    }


    /**
     * Edge container for specified vertex.
     *
     * @param vertex a vertex in this graph.
     *
     * @return EdgeContainer
     */
    private DirectedEdgeContainer<V, E> getEdgeContainer(V vertex)
    {
        assertVertexExist(vertex);

        DirectedEdgeContainer<V, E> ec = vertexMapDirected.get(vertex);

        if (ec == null) {
            ec = new DirectedEdgeContainer<V, E>(edgeSetCreator, vertex);
                vertexMapDirected.put(vertex, ec);
        }

        return ec;
    }



    private static class ArrayListFactory<VV, EE>
        implements EdgeSetCreator<VV, EE>
    {
        /**
         * @see //EdgeSetFactory.createEdgeSet
         */
        public List<EE> createEdgeSet(VV vertex)
        {
            // NOTE:  use size 1 to keep memory usage under control
            // for the common case of vertices with low degree
            return new ArrayList<EE>(1);
        }
    }


       private static class DirectedEdgeContainer<VV, EE>
       {

           List<EE> incoming;
           List<EE> outgoing;

           DirectedEdgeContainer(EdgeSetCreator<VV, EE> edgeSetFactory,
               VV vertex)
           {
               incoming = edgeSetFactory.createEdgeSet(vertex);
               outgoing = edgeSetFactory.createEdgeSet(vertex);
           }

           /**
            * A build of unmodifiable incoming edge set.
            *
            * @return List
            */
           public List<EE> getIncomingEdges()
           {
               return Collections.unmodifiableList(incoming);
           }

           /**
            * A build of unmodifiable outgoing edge set.
            *
            * @return List
            */
           public List<EE> getOutgoingEdges()
           {
               return Collections.unmodifiableList(outgoing);
           }

           /**
            * .
            *
            * @param e EE
            */
           public void addIncomingEdge(EE e)
           {
               incoming.add(e);
           }

           /**
            * .
            *
            * @param e EE
            */
           public void addOutgoingEdge(EE e)
           {
               outgoing.add(e);
           }

           /**
            * .
            *
            * @param e EE
            */
           public void removeIncomingEdge(EE e)
           {
               incoming.remove(e);
           }

           /**
            * .
            *
            * @param e EE
            */
           public void removeOutgoingEdge(EE e)
           {
               outgoing.remove(e);
           }
       }

}
