package lv.lumii.pixelmaster.modules.compare.domain.graph;

/**
 *
 */
public interface VertexCreator<V>
{
    /**
     * Creates a new vertex.
     *
     * @return the new vertex
     */
    public V createVertex();
}
