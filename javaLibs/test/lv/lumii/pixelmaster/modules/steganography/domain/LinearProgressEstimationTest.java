/**
 * This class provides a series of tests for the class <code>LinearProgressEstimation</code>.
 *
 * @author Andrey Zhmakin
 *
 * Created on May 18, 2010 7:37:52 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.steganography.domain.LinearProgressEstimation;
import org.junit.*;



public class LinearProgressEstimationTest
{
    @Test
    public void s1t1()
    {
        LinearProgressEstimation progress = new LinearProgressEstimation( 0, 15, 0 );

        progress.setProgress( 5, 20 );

        long est = progress.estimateTimeLeft();

        if ( est != 40 )
        {
            throw new RuntimeException( "Wrong estimation: " + est );
        }

        progress.setProgress( 10, 30 );

        est = progress.estimateTimeLeft();

        if ( est != 15 )
        {
            throw new RuntimeException( "Wrong estimation: " + est );
        }
    }

    

    @Test
    public void s1t2()
    {
        LinearProgressEstimation progress = new LinearProgressEstimation( 0, 40, 10 );

        progress.setProgress( 20, 10 + 10 );

        long est = progress.estimateTimeLeft();

        if ( est != 10 )
        {
            throw new RuntimeException( "Wrong estimation: " + est );
        }

        // we are back (!) on to 20-th millisecond
        progress.setProgress( 10, 10 + 20 );

        est = progress.estimateTimeLeft();

        if ( est != 60 )
        {
            throw new RuntimeException( "Wrong estimation: " + est );
        }
    }


    @Test
    public void s1t3()
    {
        LinearProgressEstimation progress = new LinearProgressEstimation( 0, 10, 0 );

        long est = progress.estimateTimeLeft();

        if ( est != Integer.MAX_VALUE )
        {
            throw new RuntimeException( "Wrong estimation: " + est );
        }

        progress.setProgress( 10, 30 );

        est = progress.estimateTimeLeft();

        if ( est != 0 )
        {
            throw new RuntimeException( "Wrong estimation: " + est );
        }
    }

}




