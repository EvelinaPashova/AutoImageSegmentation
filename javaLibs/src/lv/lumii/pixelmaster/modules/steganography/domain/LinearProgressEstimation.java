/**
 * Object of class <code>LinearProgressEstimation</code> is used for estimation of time left till
 * the end of certain process.
 *
 * @author Andrey Zhmakin
 *
 * Created on May 17, 2010 12:40:19 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import java.util.Date;



public class LinearProgressEstimation
{
    /**
     * Creates an object of class <code>LinearProgressEstimation</code>.
     */
    
    public LinearProgressEstimation()
    {
        this.minimum        = 0;
        this.maximum        = 0;
        this.progress       = 0;
        this.startMoment    = 0;
        this.currentMoment  = 0;
    }



    /**
     * Creates an object of class <code>LinearProgressEstimation</code>. Progress is initialized with current time.
     *
     * @param minimum Start value of progress scale.
     * @param maximum End value of progress scale.
     */
    
    public LinearProgressEstimation( int minimum, int maximum )
    {
        assert ( minimum < maximum );

        this.minimum        = minimum;
        this.progress       = minimum;
        this.maximum        = maximum;

        Date now = new Date();

        this.startMoment    = now.getTime();
        this.currentMoment  = now.getTime();
    }


    
    /**
     * Creates an object of class <code>LinearProgressEstimation</code>; You can provide time of start moment.
     *
     * @param minimum Start value of progress scale.
     * @param maximum End value of progress scale.
     * @param startMoment Start moment in milliseconds according to time measurement used in Java.
     */
    
    public LinearProgressEstimation( int minimum, int maximum, long startMoment )
    {
        assert ( minimum < maximum );
        
        this.minimum        = minimum;
        this.progress       = minimum;
        this.maximum        = maximum;
        this.startMoment    = startMoment;
        this.currentMoment  = startMoment;
    }

    

    /**
     * Sets the progress at current moment according to system time.
     * @param progress Progress at current moment
     *                  ( <code>this.minimum</code> <= <code>progress</code> <= <code>this.maximum</code> ).
     */
    
    public void setProgress( int progress )
    {
        assert ( progress >= this.minimum && progress <= this.maximum );
        
        this.setProgress( progress, new Date().getTime() );
    }



    /**
     * Sets the <code>progress</code> at arbitrary <code>moment</code>.
     * @param progress Progress at <code>moment</code>.
     * @param moment   Most up to date time for which the progress is known. 
     */
    public void setProgress( int progress, long moment )
    {
        assert ( progress >= this.minimum && progress <= this.maximum );
        assert ( moment >= this.startMoment );

        this.progress       = progress;
        this.currentMoment  = moment;
    }


    /**
     * Estimates the time left till the end of the process.
     * @return Time left till the end of the process. If the process is over to the moment 0 is return. If there is not
     *         enough data to calculate Integer.MAX_VALUE is returned.
     */
    
    public long estimateTimeLeft()
    {
        return this.estimateTimeLeft( this.progress, this.currentMoment );
    }


    
    /**
     * Estimates the time left till the end of the process. Inner state of the object is not updated.
     *
     * @see LinearProgressEstimation#estimateTimeLeft()
     *
     * @param progress Progress and the <code>moment</code>.
     * @param moment   Arbitrary moment in milliseconds.
     * @return Estimated time left in milliseconds.
     */
    public long estimateTimeLeft( int progress, long moment )
    {
        assert ( progress >= this.minimum && progress <= this.maximum );

        return ( progress == this.minimum ) ? Integer.MAX_VALUE
                                            : (long)( (double)( this.maximum - progress )
                                                        * (double)( moment - this.startMoment )
                                                        / (double)( progress - this.minimum )
                                                     );
    }


    
    /**
     * Returns current progress in per cents [0.0-1.0].
     * @return Progress up to date in per cents.
     */
    
    public double getProgress()
    {
        return (double)(this.progress - this.minimum) / (double)(this.maximum - this.minimum);
    }


    
    /**
     * Converts a period in milliseconds to readable <code>String</code> shoving the duration in
     * days, hours, minutes and seconds.
     * 
     * @param millis Period in milliseconds.
     * @return Returns readable <code>String</code> shoving the duration in
     *         days, hours, minutes and seconds.
     * 
     */
    
    public static String toTime( long millis )
    {
        long d = millis / ( 24 * 60 * 60 * 1000 ); millis -= d * ( 24 * 60 * 60 * 1000 ); 
        long h = millis / (      60 * 60 * 1000 ); millis -= h * (      60 * 60 * 1000 );
        long m = millis / (           60 * 1000 ); millis -= m * (           60 * 1000 );
        long s = millis / (                1000 ); millis -= s * (                1000 );

        String result = "";

        if ( d > 0 )
        {
            result += d + "d ";
        }

        if ( h < 10 )
        {
            result += "0";
        }
        result += h;

        result += ":";

        if ( m < 10 )
        {
            result += "0";
        }
        result += m;

        result += ":";

        if ( s < 10 )
        {
            result += "0";
        }
        result += s;

        result += ".";

        if ( millis < 100 )
        {
            result += ( millis < 10 ) ? "00" : "0";
        }
        result += millis;

        return result;
    }


    /**
     * Beginning of the progress scale.
     */
    private int     minimum;

    /**
     * End of the progress scale.
     */
    private int     maximum;

    /**
     * Current progress on the progress scale.
     */
    private int     progress;

    /**
     * Start moment; Time in milliseconds.
     */
    private long    startMoment;

    /**
     * Moment when the progress is known to be <code>this.progress</code>.
     */
    private long    currentMoment;
    
}




