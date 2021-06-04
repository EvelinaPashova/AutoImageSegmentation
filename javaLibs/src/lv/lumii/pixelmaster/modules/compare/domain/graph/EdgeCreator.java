package lv.lumii.pixelmaster.modules.compare.domain.graph;

/**
 *
 */
public interface EdgeCreator<V,E>
{
    /**
     * Creates a new edge whose endpoints are the specified source and target
     * vertices.
     *
     * @param sourceVertex the source vertex.
     * @param targetVertex the target vertex.
     *
     * @return a new edge whose endpoints are the specified source and target
     * vertices.
     */
    public E createEdge(V sourceVertex, V targetVertex);
}
