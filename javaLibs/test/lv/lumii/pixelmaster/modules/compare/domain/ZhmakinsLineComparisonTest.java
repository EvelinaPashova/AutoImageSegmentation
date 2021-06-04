/**
 * Test for line comparison by Andrey Zhmakin.
 *
 * Compares three graphs which shapes are similar to letters N, Z and И written inside the square.
 *  
 * @author Andrey Zhmakin
 * 
 */

package lv.lumii.pixelmaster.modules.compare.domain;

import lv.lumii.pixelmaster.modules.compare.domain.CompareLines;
import java.io.*;
import org.junit.*;

import lv.lumii.pixelmaster.modules.spw.domain.graph.*;



public class ZhmakinsLineComparisonTest
{
    @Test
    public void s1t1()
            throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException
    {
        UGraph N  = GraphIO.read( new File( "testdata/compare/CompareLines/latinN.txt"     ) ),
               Z  = GraphIO.read( new File( "testdata/compare/CompareLines/latinZ.txt"     ) ),
               И  = GraphIO.read( new File( "testdata/compare/CompareLines/cyrillicI.txt"  ) ),
               Иs = GraphIO.read( new File( "testdata/compare/CompareLines/cyrillicIs.txt" ) );

        CompareLines N_to_Z  = new CompareLines( N, Z  ),
                     Z_to_И  = new CompareLines( Z, И  ),
                     И_to_N  = new CompareLines( И, N  ),
                     И_to_Ns = new CompareLines( И, Иs );

        //System.out.println( zhmakin.Math.round( 1234.5678,  2 ) );
        //System.out.println( zhmakin.Math.round( 1234.5678, -2 ) );
        /*
        System.out.println( zhmakin.Math.round( N_to_Z .DoCompare() , 8 ) );
        System.out.println( zhmakin.Math.round( Z_to_И .DoCompare() , 8 ) );
        System.out.println( zhmakin.Math.round( И_to_N .DoCompare() , 8 ) );
        System.out.println( zhmakin.Math.round( И_to_Ns.DoCompare() , 8 ) );
        */
    }
}




