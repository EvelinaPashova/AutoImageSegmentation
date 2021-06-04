/**
 * <code>FinalActions</code> provides standard methods to be called on abortion, successful ending of continuous process
 * or its failure.
 *
 * @author Andrey Zhmakin
 *
 * Created on May 14, 2010 11:29:49 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;



public abstract class FinalActions
{
    /**
     * Call this method when process ended successfully.
     */

    public abstract void doOnSuccess();


    /**
     * Call this method when process ended with errors.
     */

    public abstract void doOnFailure();


    /**
     * Call this method when process is aborted somewhere in the middle.  
     */

    public abstract void doOnAbort();
}




