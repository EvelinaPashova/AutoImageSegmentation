/**
 * A set of JUnit tests for the class <code>ZhaoAndKoch</code>.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 14, 2010 3:59:00 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.steganography.domain.Util;
import lv.lumii.pixelmaster.modules.steganography.domain.ZhaoAndKoch;
import org.junit.*;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import java.util.Date;


public class ZhaoAndKochTest
{
    @Test
    public void s1t1()
    {
        if ( ZhaoAndKoch.Q( ZhaoAndKoch.U_1, ZhaoAndKoch.V_1 ) != ZhaoAndKoch.Q( ZhaoAndKoch.U_2, ZhaoAndKoch.V_2 ) )
        {
            throw new RuntimeException( "Wrong quantization values selected!" );
        }
    }

    

    public static double[] robustness = { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 };

    @Test
    public void s1t2()
    {
        for ( double r : robustness )
        {
            RasterImage img = Util.loadImage( "testdata/steganography/200px-Bridge_miles_and_chains.jpg" );

            assert ( img != null );

            ZhaoAndKoch zhaoAnkKoch = new ZhaoAndKoch( img );

            System.out.print( "Embedding with level of robustness: " + r + "..." );

            zhaoAnkKoch.setRobustness( r );

            for ( int i = 0; i < zhaoAnkKoch.getSize(); i++ )
            {
                zhaoAnkKoch.set( false, i );
            }

            for ( int i = 0; i < zhaoAnkKoch.getSize(); i++ )
            {
                if ( zhaoAnkKoch.get(i) != false )
                {
                    throw new RuntimeException( "Data read is not equal to data encoded!" );
                }
            }

            System.out.println( "OK" );
        }
    }



    @Test
    public void s1t3()
    {
        for ( double r : robustness )
        {
            RasterImage img = Util.loadImage( "testdata/steganography/200px-Bridge_miles_and_chains.jpg" );

            assert ( img != null );

            ZhaoAndKoch zhaoAnkKoch = new ZhaoAndKoch( img );

            System.out.print( "Embedding with level of robustness: " + r + "..." );

            zhaoAnkKoch.setRobustness( r );

            for ( int i = 0; i < zhaoAnkKoch.getSize(); i++ )
            {
                zhaoAnkKoch.set( true, i );
            }

            for ( int i = 0; i < zhaoAnkKoch.getSize(); i++ )
            {
                if ( zhaoAnkKoch.get(i) != true )
                {
                    throw new RuntimeException( "Data read is not equal to data encoded!" );
                }
            }

            System.out.println( "OK" );
        }
    }


    @Test
    public void s1t4()
    {
        for ( double r : robustness )
        {
            RasterImage img = Util.loadImage( "testdata/steganography/200px-Bridge_miles_and_chains.jpg" );

            assert ( img != null );

            ZhaoAndKoch zhaoAnkKoch = new ZhaoAndKoch( img );

            System.out.print( "Embedding with level of robustness: " + r + "..." );

            zhaoAnkKoch.setRobustness( r );

            for ( int i = 0; i < zhaoAnkKoch.getSize(); i++ )
            {
                zhaoAnkKoch.set( i % 2 == 1, i );
            }

            for ( int i = 0; i < zhaoAnkKoch.getSize(); i++ )
            {
                if ( zhaoAnkKoch.get(i) != (i % 2 == 1) )
                {
                    throw new RuntimeException( "Data read is not equal to data encoded!" );
                }
            }

            System.out.println( "OK" );
        }
    }



    @Test
    public void timeTrialWritingRaw()
    {
        RasterImage img = Util.loadImage( "testdata/steganography/200px-Bridge_miles_and_chains.jpg" );

        assert ( img != null );

        ZhaoAndKoch zhaoAnkKoch = new ZhaoAndKoch( img );

        long startMoment = new Date().getTime();

        for ( int i = 0; i < zhaoAnkKoch.getSize(); i++ )
        {
            zhaoAnkKoch.set( i % 2 == 1, i );
        }

        long endMoment = new Date().getTime();

        System.out.println( "It has been taken " + ((endMoment - startMoment) / 1000.0) + " seconds to encode "
                            + zhaoAnkKoch.getSize() + "-bit long message." );

        System.out.println( "Encoding speed was "
                            + ( zhaoAnkKoch.getSize() / ((endMoment - startMoment) / 1000.0) ) + "bps" );
    }



    @Test
    public void timeTrialReadingRaw()
    {
        RasterImage img = Util.loadImage( "testdata/steganography/200px-Bridge_miles_and_chains.jpg" );

        assert ( img != null );

        ZhaoAndKoch zhaoAnkKoch = new ZhaoAndKoch( img );

        long startMoment = new Date().getTime();

        for ( int i = 0; i < zhaoAnkKoch.getSize(); i++ )
        {
            zhaoAnkKoch.get( i );
        }

        long endMoment = new Date().getTime();

        System.out.println( "It has been taken " + ((endMoment - startMoment) / 1000.0) + " seconds to extract "
                            + zhaoAnkKoch.getSize() + "-bit long message." );

        System.out.println( "Extraction speed was "
                            + ( zhaoAnkKoch.getSize() / ((endMoment - startMoment) / 1000.0) ) + "bps" );
    }
}




