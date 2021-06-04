package lv.lumii.pixelmaster.modules.compare.domain.graph;

import java.awt.*;


/**
 *
 */
class IntrusiveEdge
    implements Cloneable
{
    //~ Instance fields --------------------------------------------------------

    Object source;

    Object target;

    //~ Methods ----------------------------------------------------------------

    /**
     * @see Object#clone()
     */
    public Object clone()
    {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // shouldn't happen as we are Cloneable
            throw new InternalError();
        }
    }
}

