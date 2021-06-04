/**
 * Set of unit-tests for zhmakin.steganography.ECC.ReedSolomon.
 *
 * @author Andrey Zhmakin
 *
 * Created on May 21, 2010 5:43:50 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain.ecc;

import lv.lumii.pixelmaster.modules.steganography.domain.ecc.ReedSolomon;
import org.junit.*;

import java.util.Arrays;



public class ReedSolomonTest
{
    @Test
    public void s1t1()
    {
        ReedSolomon rs = new ReedSolomon();

        if ( rs.N != 15 ) throw new RuntimeException( "Reed-Solomon code model is not properly initialized!" );
        if ( rs.K !=  9 ) throw new RuntimeException( "Reed-Solomon code model is not properly initialized!" );

        int[] data = new int[rs.K];

        // original test data
        data[0] =  8;
        data[1] =  6;
        data[2] =  8;
        data[3] =  1;
        data[4] =  2;
        data[5] =  4;
        data[6] = 15;
        data[7] =  9;
        data[8] =  9;

        int[] parity = rs.encode( data );

        // result from the original program
        int[] paragon = { 15, 13, 10, 0, 6, 13 };

        if ( !Arrays.equals( parity, paragon ) )
        {
            throw new RuntimeException( "Parity doesn't match the paragon!" );
        }
    }



    @Test
    public void s1t2()
    {
        ReedSolomon rs = new ReedSolomon( 15, 9 );

        //              1   2   3   4   5   6   7   8   9
        int[] data = {  8,  6,  8,  1,  2,  4, 15,  9,  9 };

        int[] parity = rs.encode( data );

        int[] recd = new int[rs.N];

        System.arraycopy( parity, 0, recd, 0            , parity.length );
        System.arraycopy( data  , 0, recd, parity.length, data.length );

        System.out.println( "====================================================" );
        print( recd );
        recd[rs.N - rs.K + 0] = 9;
        print( recd );
        int diagnosis = rs.decode( recd );
        print( recd );
        System.out.println( "====================================================" );
    }



    @Test
    public void s1t3()
    {
        ReedSolomon rs = new ReedSolomon( 15, 1 );

        //              1   2   3   4   5   6   7   8   9
        int[] data = { 10 }; 

        int[] parity = rs.encode( data );

        int[] recd = new int[rs.N];

        System.arraycopy( parity, 0, recd, 0            , parity.length );
        System.arraycopy( data  , 0, recd, parity.length, data.length );

        System.out.println( "====================================================" );
        print( recd );
        recd[rs.N - rs.K - 3] = 8;
        recd[rs.N - rs.K + 0] = 6;
        print( recd );
        rs.decode( recd );
        print( recd );
        System.out.println( "====================================================" );
    }



    @Test
    public void s1t4()
    {
        ReedSolomon rs = new ReedSolomon( 15, 7 );

        //              1   2   3   4   5   6   7   8   9
        int[] data = {  6,  8,  5, 11,  7,  2, 10 };

        int[] parity = rs.encode( data );

        int[] recd = new int[rs.N];

        System.arraycopy( parity, 0, recd, 0            , parity.length );
        System.arraycopy( data  , 0, recd, parity.length, data.length );

        System.out.println( "====================================================" );
        print( recd );
        recd[rs.N - rs.K + 0] = 7;
        recd[rs.N - rs.K + 1] = 7;
        print( recd );
        rs.decode( recd );
        print( recd );
        System.out.println( "====================================================" );
    }



    @Test
    public void s1t5()
    {
        ReedSolomon rs = new ReedSolomon( 15, 13 );

        //              1   2   3   4   5   6   7   8   9   10  11
        int[] data = { 5, 10, 5, 2, 5, 8, 4, 12, 11, 2, 10,  7, 10 };

        int[] parity = rs.encode( data );

        int[] recd = new int[rs.N];

        System.arraycopy( parity, 0, recd, 0            , parity.length );
        System.arraycopy( data  , 0, recd, parity.length, data.length );

        System.out.println( "====================================================" );
        print( recd );
        recd[0] = 0;
        print( recd );
        rs.decode( recd );
        print( recd );
        System.out.println( "====================================================" );
    }



    @Test
    public void s1t6()
    {
        ReedSolomon rs = new ReedSolomon( 15, 7 );

        //              1   2   3   4   5   6   7   8   9   10  11
        int[] data = {  9,  4,  9,  9,  5,  5, 10 };

        int[] parity = rs.encode( data );

        int[] recd = new int[rs.N];

        System.arraycopy( parity, 0, recd, 0            , parity.length );
        System.arraycopy( data  , 0, recd, parity.length, data.length );

        System.out.println( "====================================================" );
        print( recd );
        recd[rs.N - rs.K + 0] = 10;
        recd[rs.N - rs.K + 1] =  5;
        print( recd );
        rs.decode( recd );
        print( recd );
        System.out.println( "====================================================" );
    }
    
    

    private static void print( int[] a )
    {
        System.out.print( "(" + a.length + "): " );

        if ( a.length > 0 )
        {
            System.out.print( ((a[0] < 10) ? " " : "") + a[0] );
            
            for ( int i = 1; i < a.length; i++ )
            {
                System.out.print( "|" + ((a[i] < 10) ? " " : "") + a[i] );
            }
        }

        System.out.println();
    }
}




