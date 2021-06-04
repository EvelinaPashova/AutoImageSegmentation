/**
 * This class implements a series of JUnit tests for <code>BitsOfImageBySignificance</code>.
 *
 * @author Andrey Zhmakin
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.steganography.domain.Util;
import lv.lumii.pixelmaster.modules.steganography.domain.BitsOfImageBySignificance;
import java.io.File;
import java.io.IOException;
import org.junit.*;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import java.util.Date;


public class BitsOfImageBySignificanceTest
{
    @Test
    public void s1t1()
    {
        RasterImage img = Util.loadImage( "testdata/steganography/200px-Bridge_miles_and_chains.jpg" );

        assert ( img != null );

        BitsOfImageBySignificance bitsOfImage = new BitsOfImageBySignificance( img, 0 );

        for ( int i = 0; i < bitsOfImage.getSize(); i++ )
        {
            try
            {
                bitsOfImage.set( i, false );

                if ( bitsOfImage.get(i) != false )
                {
                    throw new RuntimeException( "Data read doesn't match written data!" );
                }

                bitsOfImage.set( i, true );

                if ( bitsOfImage.get(i) != true )
                {
                    throw new RuntimeException( "Data read doesn't match written data!" );
                }
            }
            catch ( BitsOfImageBySignificance.IndexOutOfBoundsException e )
            {
                assert false : "This should not happen!";
            }
        }
    }



    @Test
    public void s1t2() throws IOException
    {
        RasterImage img = Util.loadImage( "testdata/steganography/200px-Bridge_miles_and_chains.jpg" );
		File tmpfile = File.createTempFile("test", null);
		tmpfile.deleteOnExit();

        assert ( img != null );

        BitsOfImageBySignificance bitsOfImage = new BitsOfImageBySignificance( img, 0 );

        for ( int i = 0; i < bitsOfImage.getSize(); i++ )
        {
            try
            {
                bitsOfImage.set( i, i % 2 == 1 );
            }
            catch ( BitsOfImageBySignificance.IndexOutOfBoundsException e )
            {
                assert false : "This should not happen!";
            }
        }

        Util.saveInPNG( img, tmpfile );

        img = Util.loadImage( tmpfile );
        
        bitsOfImage = new BitsOfImageBySignificance( img, 0 );

        for ( int i = 0; i < bitsOfImage.getSize(); i++ )
        {
            try
            {
                if ( bitsOfImage.get( i ) !=  (i % 2 == 1 ) )
                {
                    throw new RuntimeException( "Data read and written do not match each other!" );
                }
            }
            catch ( BitsOfImageBySignificance.IndexOutOfBoundsException e )
            {
                assert false : "This should not happen!";
            }
        }
    }

    

    @Test
    public void timeTrialEmbedmentBare()
    {
        RasterImage img = Util.loadImage( "testdata/steganography/200px-Bridge_miles_and_chains.jpg" );

        assert ( img != null );

        BitsOfImageBySignificance bitsOfImage = new BitsOfImageBySignificance( img, 0 );


        long startMoment = new Date().getTime();

        for ( int i = 0; i < bitsOfImage.getSize(); i++ )
        {
            try
            {
                bitsOfImage.set( i, false );
            }
            catch ( BitsOfImageBySignificance.IndexOutOfBoundsException e )
            {
                assert false : "This should not happen!";
            }
        }

        long endMoment = new Date().getTime();

        System.out.println( "It has been taken " + ((endMoment - startMoment) / 1000.0) + " seconds to encode "
                            + bitsOfImage.getSize() + "-bit long message." );

        System.out.println( "Encoding speed was "
                            + ( bitsOfImage.getSize() / ((endMoment - startMoment) / 1000.0) ) + "bps" );
    }

    

    @Test
    public void timeTrialExtractionBare()
    {
        RasterImage img = Util.loadImage( "testdata/steganography/200px-Bridge_miles_and_chains.jpg" );

        assert ( img != null );

        BitsOfImageBySignificance bitsOfImage = new BitsOfImageBySignificance( img, 0 );


        long startMoment = new Date().getTime();

        for ( int i = 0; i < bitsOfImage.getSize(); i++ )
        {
            try
            {
                bitsOfImage.get( i );
            }
            catch ( BitsOfImageBySignificance.IndexOutOfBoundsException e )
            {
                assert false : "This should not happen!";
            }
        }

        long endMoment = new Date().getTime();

        System.out.println( "It has been taken " + ((endMoment - startMoment) / 1000.0) + " seconds to extract "
                            + bitsOfImage.getSize() + "-bit long message." );

        System.out.println( "Extraction speed was "
                            + ( bitsOfImage.getSize() / ((endMoment - startMoment) / 1000.0) ) + "bps" );
    }

}




