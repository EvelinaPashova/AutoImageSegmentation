/**
 * Tests for class <code>YCbCr</code>
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 14, 2010 2:45:30 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.steganography.domain.YCbCr;
import org.junit.*;

import java.awt.*;


public class YCbCrTest
{
    @Test
    public void s1t1()
    {
        YCbCr ycc = new YCbCr( new Color( 0, 0, 0 ) );

        System.out.println( ycc );
        System.out.println( ycc.toColor() );

        System.out.println( "--------------------------------------------------" );

        ycc = new YCbCr( new Color( 255, 255, 255 ) );

        System.out.println( ycc );
        System.out.println( ycc.toColor() );

        System.out.println( "--------------------------------------------------" );

        ycc = new YCbCr( new Color( 255, 0, 0 ) );

        System.out.println( ycc );
        System.out.println( ycc.toColor() );

        System.out.println( "--------------------------------------------------" );

        ycc = new YCbCr( new Color( 0, 255, 0 ) );

        System.out.println( ycc );
        System.out.println( ycc.toColor() );

        System.out.println( "--------------------------------------------------" );

        ycc = new YCbCr( new Color( 0, 0, 255 ) );

        System.out.println( ycc );
        System.out.println( ycc.toColor() );

        System.out.println( "--------------------------------------------------" );

        ycc = new YCbCr( new Color( 79, 157, 191 ) );

        System.out.println( ycc );
        System.out.println( ycc.toColor() );
    }



    @Test
    public void s1t2()
    {
        double minY = Double.POSITIVE_INFINITY,
               maxY = Double.NEGATIVE_INFINITY;

        double minCb = Double.POSITIVE_INFINITY,
               maxCb = Double.NEGATIVE_INFINITY;

        double minCr = Double.POSITIVE_INFINITY,
               maxCr = Double.NEGATIVE_INFINITY;

        for ( int r = 0; r < 256; r++ )
        {
            for ( int g = 0; g < 256; g++ )
            {
                for ( int b = 0; b < 256; b++ )
                {
                    Color rgbA = new Color( r, g, b );
                    YCbCr ycc = new YCbCr( rgbA );
                    Color rgbB = ycc.toColor();

                    int dr = java.lang.Math.abs( rgbA.getRed  () - rgbB.getRed  () ); 
                    int dg = java.lang.Math.abs( rgbA.getGreen() - rgbB.getGreen() );
                    int db = java.lang.Math.abs( rgbA.getBlue () - rgbB.getBlue () );

                    if ( !ycc.equals( new YCbCr(ycc) ) )
                    {
                        throw new RuntimeException( "Malfunction of equals() or something else!" );
                    }

                    if ( dr > 0 || dg > 0 || db > 0 )
                    {
                        throw new RuntimeException( "Color is converted improperly: " + rgbA + " -> " + rgbB );
                    }

                    if ( ycc.Y  < minY  ) minY  = ycc.Y;
                    if ( ycc.Y  > maxY  ) maxY  = ycc.Y;

                    if ( ycc.Cb < minCb ) minCb = ycc.Cb;
                    if ( ycc.Cb > maxCb ) maxCb = ycc.Cb;

                    if ( ycc.Cr < minCr ) minCr = ycc.Cr;
                    if ( ycc.Cr > maxCr ) maxCr = ycc.Cr;
                }
            }
        }

        System.out.println( "Y  in [" + minY  + ";" + maxY  + "]" );
        System.out.println( "Cb in [" + minCb + ";" + maxCb + "]" );
        System.out.println( "Cr in [" + minCr + ";" + maxCr + "]" );
    }
}




