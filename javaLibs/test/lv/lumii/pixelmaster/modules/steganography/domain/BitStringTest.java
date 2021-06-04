/**
 * This class implements some test for <code>BitString</code>.
 *
 * @author Andrey Zhmakin
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.steganography.domain.BitString;
import org.junit.*;



public class BitStringTest
{
    @Test
    public void s1t1()
    {
        BitString a = new BitString( 10 );
        BitString b = new BitString();

        // Pay attention on parity of a.length
        for ( int i = 0; i < 10; i++ )
        {
            a.set( i % 2 == 1, i );

            if ( i % 2 == 1 )
            {
                b.set( i );
            }
        }

        if ( a.size() != b.size() )
        {
            throw new RuntimeException( "a and b should be of equal size" );
        }
    }



    @Test
    public void s1t2()
    {
        BitString a = new BitString();
        BitString b = new BitString();

        for ( int i = 0; i < 10; i++ )
        {
            if ( i % 2 == 1 )
            {
                a.set( i );
                b.set( i );
            }
        }

        a.attach( b );
        a.attach( a );

        if ( a.size() != 4 * b.size() || a.size() != 40 )
        {
            throw new RuntimeException( "Unexpected size of a: " + a.size() );
        }

        //System.out.println( "a: " + a );

        for ( int i = 0; i < a.size(); i++ )
        {
            if ( a.get(i) != ( i % 2 == 1 ) )
            {
                throw new RuntimeException( "Unexpected state of bit in a at " + i );
            }
        }
    }



    @Test
    public void s1t3()
    {
        for ( int i = 0; i <= 100; i++ )
        {
            char[] temp = new char[i];

            for ( int j = 0; j < i; j++ )
            {
                switch ( j % 3 )
                {
                    case 0: temp[j] = 'A'; break;
                    case 1: temp[j] = 'B'; break;
                    case 2: temp[j] = 'C'; break;
                }
            }

            String str = new String( temp );

            BitString bits = BitString.encodeString( str );
            BitString copy = new BitString( bits );

            if ( !str.equals( BitString.decodeString( copy ) ) )
            {
                throw new RuntimeException( "Error encoding or decoding the following string of characters: " + str );
            }
        }
    }
}




