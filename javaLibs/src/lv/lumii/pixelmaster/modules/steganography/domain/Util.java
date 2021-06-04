/**
 * Class contains miscellaneous static methods utilized in the process of information concealment.
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.steganography.domain.YCbCr;
import java.awt.*;
import java.util.Random;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;



public class Util
{
    public static int random( Random generator, int floor, int ceiling )
    {
        return floor + generator.nextInt( ceiling - floor );
    }



    public static double random( Random generator, double floor, double ceiling )
    {
        assert ( floor <= ceiling );

        return floor + generator.nextDouble() * ( ceiling - floor );
    }



    public static double getLuminosity( Color color )
    {
        return new YCbCr(color).Y;
    }



    public static Color adjustLuminosity( Color color, double luminosity )
    {
        YCbCr ycc = new YCbCr( color );

        ycc.Y = 262.65 * luminosity;

        return ycc.toColor();
    }



    public static BitString convert( String string )
    {
        return BitString.encodeString( string );
    }



    public static String toString( BitString bits )
    {
        return BitString.decodeString( bits );
    }



    public static BitString convert( int value )
    {
        BitString result = new BitString( 32 );

        for ( int i = 0; i < 32; i++ )
        {
            if ( ((value >> i) & 1) == 1 ) { result.set  ( i ); }
            else                           { result.clear( i ); }
        }

        return result;
    }



    public static int toInt( BitString bits )
    {
        assert (bits.size() == 32) : "This is definitely not an int!";

        int result = 0;

        for ( int i = 0; i < 32; i++ )
        {
            if ( bits.get(i) )
            {
                result |= 1 << i;
            }
        }

        return result;
    }



    public static BitString concatenate( BitString start, BitString end )
    {
        BitString result = new BitString( start.size() + end.size() );

        int i = 0;

        for ( ; i < start.size(); i++ )
        {
            if ( start.get( i ) )
            {
                result.set( i );
            }
        }

        for ( int j = 0; j < end.size(); i++, j++ )
        {
            if ( end.get(j) )
            {
                result.set( i );
            }
        }

        return result;
    }



    public static BitString load( File file )
        throws IOException
    {
        return BitString.loadFile( file );
    }



    public static void save( File file, BitString data )
        throws IOException
    {
        data.saveToFile( file ); 
    }


    
    /**
     * Create encryption key on the basis of password; Generation procedure in cryptographically weak.
     *
     * @param password Password intended for conversion to key.
     * 
     * @return 64-bit width encryption key.
     *
     */
    
    public static long getKeyFromPassword( char[] password )
    {
        long key = 0;

        int offset = 0;

        for ( char c : password )
        {
            key ^= (long)c << offset;

            offset += 8;

            if ( offset > 56 )
            {
                offset = 0;
            }
        }

        return key;
    }



    public static RasterImage loadImage( String filename )
    {
		return loadImage(new File(filename));
	}

    public static RasterImage loadImage( File file )
    {
        try
        {
            BufferedImage buffered = ImageIO.read( file );

            assert ( buffered != null ) : "Can't load image";
            assert ( buffered.getWidth () >= 1 );
            assert ( buffered.getHeight() >= 1 );

            RasterImage image = new RasterImage( buffered.getWidth(), buffered.getHeight() );

            ImageConverter.convertBufImage( buffered, image );

            return image;
        }
        catch ( IOException e )
        {
            assert false : "Error occurred while reading image from file!";
            return null;
        }
    }

    

    public static boolean saveInPNG( RasterImage image, File file )
    {
        try
        {
            if ( !ImageIO.write( ImageConverter.toBuffered(image), "png", file ) )
            {
                assert false : "Data format error!";
                return false;
            }
            else
            {
                return true;
            }
        }
        catch ( IOException e )
        {
            assert false : "File IO error!";
            return false;
        }


    }
}




