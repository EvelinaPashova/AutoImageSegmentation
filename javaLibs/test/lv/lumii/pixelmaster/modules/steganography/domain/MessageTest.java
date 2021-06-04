/**
 * This class provides a series of test for <code>Message</code>.
 *
 * @author Andrey Zhmakin
 *
 * Created on May 27, 2010 10:28:32 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.steganography.domain.Util;
import lv.lumii.pixelmaster.modules.steganography.domain.Message;
import lv.lumii.pixelmaster.modules.steganography.domain.BitString;
import org.junit.*;

import java.util.Random;


public class MessageTest
{
    @Test
    public void s1t1()
    {
        BitString message = Util.convert( "Copyright © by Andrey Zhmakin" );

        BitString coded = Message.produceLowLevelMessage( message, 20 );

        BitString body = coded.get( Message.SERVICE_DATA_SIZE, coded.size() );

        System.out.println( "body.size() = " + body.size() );

        assert ( body.size() == coded.size() - Message.SERVICE_DATA_SIZE );

        BitString decoded = new BitString();

        Message.decodeBody( body, 2, message.size(), decoded );

        System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
        print( message );
        System.out.println();
        System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
        print( coded );
        System.out.println();
        System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
        print( decoded );
        System.out.println();
        System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );

        System.out.println( Util.toString( message ) );
        System.out.println( Util.toString( decoded ) );

        if ( !message.equals( decoded ) )
        {
            throw new RuntimeException( "Original and decoded messages do not match!" );
        }
    }



    @Test
    public void s1t2()
    {
        Random generator = new Random(0);
        
        for ( int len = 0; len < 4000; len++ )
        {
            BitString message = new BitString( len );

            for ( int i = 0; i < len; i++ )
            {
                message.set( generator.nextBoolean(), i );
            }

            for ( int s = 0; s <= 100; s += 5 )
            {
                testMessage( message, s );
            }
        }
    }



    private static void testMessage( BitString rawMessage, int strengthOfEcc )
    {
        BitString coded = Message.produceLowLevelMessage( rawMessage, strengthOfEcc );

        BitString body = coded.get( Message.SERVICE_DATA_SIZE, coded.size() );

        BitString decoded = new BitString();

        int st = Message.getEccStrength( coded.get( 0, Message.SIZE_OF_ECC_STRENGTH ) );
        int sz = Message.decodeMessageSize( coded.get( Message.SIZE_OF_ECC_STRENGTH, Message.SERVICE_DATA_SIZE ) );

        // adjusting strengthOfEcc to new constraints
        strengthOfEcc /= 10;
        if  ( strengthOfEcc % 2 == 1 ) strengthOfEcc++;
        
        if ( st != strengthOfEcc )
        {
            throw new RuntimeException( "Strength of ECC is wrongfully decoded!" );
        }

        if ( sz != rawMessage.size() )
        {
            throw new RuntimeException( "Message size is wrongfully decoded!" );
        }

        Message.decodeBody( body, st, sz, decoded );

        if ( !rawMessage.equals( decoded ) )
        {
            throw new RuntimeException( "Original and decoded messages do not match!" );
        }
    }



    private static void print( BitString bits )
    {
        for ( int i = 0; i < bits.size(); i++ )
        {
            if ( i != 0 && i % 4 == 0 )
            {
                System.out.print( "'" );
            }

            System.out.print( bits.get(i) ? '1' : '0' );
        }
    }
}




