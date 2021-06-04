/**
 * This class implements a series of tests for </code>ConformityTable</code>.
 *
 * @author Andrey Zhmakin
 *
 * Created on Mar 26, 2010 4:18:46 AM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.steganography.domain.ConformityTable;
import org.junit.*;



public class ConformityTableTest
{
    @Test
    public void s1t1()
    {
        for ( int i = 1; i <= 100; i++ )
        {
            testTable( 0, i );
        }
    }


    
    @Test
    public void s1t2()
    {
        for ( int i = 1; i <= 100; i++ )
        {
            testTable( i, i * 10 );
        }
    }



    @Test
    public void s1t3()
    {
        for ( int i = 1; i <= 100; i++ )
        {
            testTable( -i, i * 2 );
        }
    }



    private static void testTable( int from, int to )
    {
        ConformityTable table = new ConformityTable( from, to, 0 );

        for ( int i = from; i < to; i++ )
        {
            int appearances = 0;

            for ( int j = from; j < to; j++ )
            {
                if ( table.get(j) == i )
                {
                    appearances++;
                }
            }

            if ( appearances < 1 )
            {
                throw new RuntimeException( "This element is not present: " + i );
            }
            else if ( appearances > 1 )
            {
                throw new RuntimeException( "This " + i + " element is encountered " + appearances + " time(s)"  );
            }
        }
    }
}




