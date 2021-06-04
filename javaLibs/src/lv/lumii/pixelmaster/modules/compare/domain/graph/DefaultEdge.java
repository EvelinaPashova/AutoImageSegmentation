package lv.lumii.pixelmaster.modules.compare.domain.graph;


public class DefaultEdge
       extends IntrusiveEdge
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Retrieves the source of this edge. This is protected, for use by
     * subclasses only (e.g. for implementing toString).
     *
     * @return source of this edge
     */
    protected Object getSource()
    {
        return source;
    }

    /**
     * Retrieves the target of this edge. This is protected, for use by
     * subclasses only (e.g. for implementing toString).
     *
     * @return target of this edge
     */
    protected Object getTarget()
    {
        return target;
    }

    @Override
    public String toString()
    {
        return "(" + source + " : " + target + ")";
    }
}
