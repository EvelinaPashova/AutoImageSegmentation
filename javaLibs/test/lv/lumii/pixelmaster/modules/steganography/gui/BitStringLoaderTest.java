
package lv.lumii.pixelmaster.modules.steganography.gui;

import lv.lumii.pixelmaster.modules.steganography.domain.BitString;
import java.io.File;
import org.junit.*;
import lv.lumii.pixelmaster.modules.steganography.domain.FinalActions;

import java.io.IOException;
import java.util.Random;
import lv.lumii.pixelmaster.modules.steganography.gui.BitStringLoader;

/**
 * This class implements some tests for <code>BitStringLoader</code>.
 */
public class BitStringLoaderTest {

    private boolean s1t4_mayContinue;

    @Test
    public void s1t4() throws IOException
    {
        final File tmpfile = File.createTempFile("test", null);
		tmpfile.deleteOnExit();

        Random generator = new Random( 0 );

        for ( int i = 0; i < 100; i += 1 )
        {
            BitString bits = new BitString( i );

            for ( int j = 0; j < i; j++ )
            {
                bits.set( generator.nextBoolean(), j );
            }

            bits.saveToFile( tmpfile );


            BitString a = BitString.loadFile( tmpfile );

            this.s1t4_mayContinue = false;

            BitString b = new BitString();
            BitStringLoader.loadFile( tmpfile, b, null,
                                new FinalActions()
                                {
                                    public void doOnSuccess() { s1t4_mayContinue = true; }
                                    public void doOnFailure() { throw new RuntimeException( "Unexpected failure!" ); }
                                    public void doOnAbort()   { throw new RuntimeException( "Unexpected abort!" ); }

                                }
                               );

            while ( !s1t4_mayContinue )
                ;

            int padding = 8 - i % 8;
            if ( padding == 8 ) padding = 0;

            if ( ( (a.size() - bits.size()) != padding ) || ( (b.size() - bits.size()) != padding) )
            {
                throw new RuntimeException( "Unexpected padding: a.size = " + a.size() + ", b.size = " + b.size() );
            }

            a.setSize( bits.size() );
            if ( !bits.equals( a ) )
            {
                System.out.println( bits );
                System.out.println( a    );

                throw new RuntimeException( "a is loaded with errors!" );
            }

            b.setSize( bits.size() );
            if ( !bits.equals( b ) )
            {
                System.out.println( bits );
                System.out.println( b    );

                throw new RuntimeException( "b is loaded with errors!" );
            }
        }
    }
}
