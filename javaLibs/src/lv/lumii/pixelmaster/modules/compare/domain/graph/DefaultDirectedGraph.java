package lv.lumii.pixelmaster.modules.compare.domain.graph;

/**
 *
 */
public class DefaultDirectedGraph<V, E>
    extends AbstractBaseGraph<V, E>
    implements DGraph<V, E>
{
    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new directed graph.
     *
     * @param edgeClass class on which to base factory for edges
     */
    public DefaultDirectedGraph(Class<? extends E> edgeClass)
    {
        this(new ClassBasedEdgeCreator<V, E>(edgeClass));
    }

    /**
     * Creates a new directed graph with the specified edge factory.
     *
     * @param ef the edge factory of the new graph.
     */
    public DefaultDirectedGraph(EdgeCreator<V, E> ef)
    {
        super(ef);
    }
}

