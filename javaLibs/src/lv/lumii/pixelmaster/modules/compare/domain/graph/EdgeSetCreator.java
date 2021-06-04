package lv.lumii.pixelmaster.modules.compare.domain.graph;

import java.util.*;

/**
 *
 */
public interface EdgeSetCreator<V, E>
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Create a new edge set for a particular vertex.
     *
     * @param vertex the vertex for which the edge set is being created;
     *
     * @return new set
     */
    public List<E> createEdgeSet(V vertex);
}
