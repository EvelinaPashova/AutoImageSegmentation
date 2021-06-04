/**
 *  Place for all sorts of mathematical routines written by Andrey Zhmakin.
 * 
 *  @author Andrey Zhmakin
 * 
 */

package lv.lumii.pixelmaster.modules.grapheditor.domain;



public class Math
{
    public static double sqr( double x )
    {
        return ( x * x );
    }



    public static double round( double x )
    {
        return java.lang.Math.floor( x + 0.5 );
    }



    public static double round( double value, int accuracy )
    {
        double factor  = java.lang.Math.pow( 10.0, accuracy );
        double divisor = 1.0 / factor;

        return round( value * factor ) * divisor;
    }



    public static double fmod( double dividend, double divisor )
    {
        return dividend - java.lang.Math.floor( java.lang.Math.abs(dividend) / divisor ) * divisor;
    }


    public static double fraction( double value )
    {
        return value - java.lang.Math.floor( value );
    }
}




