/**
 *  Tests of method graph.getCenterOfMass().
 *
 *  @see graph.getCenterOfMass()
 *
 *  @author Andrey Zhmakin
 *
 */

package lv.lumii.pixelmaster.modules.spw.domain.graph;

import java.io.*;
import java.awt.geom.*;
import org.junit.*;



public class TestCenterOfMass
{
    public static class NonPassedTestException extends Exception { }
    public static class CantLoadGraphException extends Exception { }


    private final static Point2D[] correct = new Point2D.Double[]
            {
                    null,
                    null,
                    new Point2D.Double( 2, 1 ),
                    new Point2D.Double( 1, 1 )
            };

    
    private final static boolean[] weightless = new boolean [] { false, false, false, false };
    private final static boolean[] edgeless   = new boolean [] { true , true , false, false };


    private static void performTest( int number )
            throws NonPassedTestException, CantLoadGraphException
    {
        try
        {
            UGraph graph = GraphIO.read( new File( "testdata/graph/CenterOfMass/test_" + number + ".in" ) );

            if ( graph == null )
            {
                System.out.println( "Can\'t load graph" );
                throw new CantLoadGraphException();
            }

            Point2D centerOfMass = graph.getCenterOfMass();

            if ( !centerOfMass.equals( correct[number - 1] ) )
            {
                System.out.println( "Result (" + centerOfMass.getX() + "; " + centerOfMass.getY() + ") does not match the paragon" );
                throw new NonPassedTestException();
            }

        }
        catch ( UGraph.EdgelessGraphException e )
        {
            if ( !edgeless[number - 1] )
            {
                System.out.println( "Unexpected throw of exception UGraph.EdgelessGraphException" );
                throw new NonPassedTestException();
            }
        }
        catch ( UGraph.WeightlessGraphException e )
        {
            if ( !weightless[number - 1] )
            {
                System.out.println( "Unexpected throw of exception UGraph.WeightlessGraphException" );
                throw new NonPassedTestException();
            }
        }
    }

    @Test public void s1t1() throws NonPassedTestException, CantLoadGraphException { performTest( 1 ); }
    @Test public void s1t2() throws NonPassedTestException, CantLoadGraphException { performTest( 2 ); }
    @Test public void s1t3() throws NonPassedTestException, CantLoadGraphException { performTest( 3 ); }
    @Test public void s1t4() throws NonPassedTestException, CantLoadGraphException { performTest( 4 ); }
}




