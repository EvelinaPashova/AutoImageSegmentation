package lv.lumii.pixelmaster.modules.compare.domain.graph;

/**
 * TypeUtil isolates type-unsafety so that code that which uses it for
 * legitimate reasons can stay warning-free.
 *
 */
public class TypeUtil<T>
{
    //~ Methods ----------------------------------------------------------------

    /**
     * Casts an object to a type.
     *
     * @param o object to be cast
     * @param typeDecl conveys the target type information; the actual value is
     * unused and can be null since this is all just stupid compiler tricks
     *
     * @return the result of the cast
     */
    @SuppressWarnings("unchecked")
    public static <T> T uncheckedCast(Object o, TypeUtil<T> typeDecl)
    {
        return (T) o;
    }
}

