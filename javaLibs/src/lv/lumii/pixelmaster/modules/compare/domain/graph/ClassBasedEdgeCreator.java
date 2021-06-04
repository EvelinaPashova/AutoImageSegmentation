package lv.lumii.pixelmaster.modules.compare.domain.graph;

/**
 *
 */
public class ClassBasedEdgeCreator<V, E>
    implements EdgeCreator<V, E>
{
    //~ Instance fields --------------------------------------------------------

    private final Class<? extends E> edgeClass;

    //~ Constructors -----------------------------------------------------------

    public ClassBasedEdgeCreator(Class<? extends E> edgeClass)
    {
        this.edgeClass = edgeClass;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * @see EdgeCreator#createEdge(Object, Object)
     */
    public E createEdge(V source, V target)
    {
        try {
            return edgeClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Edge factory failed", ex);
        }
    }
}