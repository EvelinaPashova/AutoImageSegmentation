/**
 * 
 * @author Andrey Zhmakin
 *
 */

package lv.lumii.pixelmaster.modules.compare.domain;

import org.junit.Test;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import lv.lumii.pixelmaster.modules.spw.domain.graph.*;



public class PointSegmentDistanceTest
{
    /**
     *
     *  Gets distance between vertex of graph A and edge of graph B
     *
     * @param x double vertex x coordinates
     * @param y double vertex y coordinates
     * @param edge EdgeIterator Object representing edge of graph
     * @return distance between vertex and edge
     * @author Madara Augstkalne
     */
    
    private static double getDistanceBetween(double x, double y, GraphEdge edge)
    {
        //System.out.println("dbx:" + x + " dby:" + y);
        double distance;
        Vertex v1 = edge.vertexFirst();
        Vertex v2 = edge.vertexSecond();
        //System.out.println("edx1:" + v1.getX() + " edx2:" + v2.getX());
        //System.out.println("edy1:" + v1.getY() + " edy2:" + v2.getY());

        distance = abs( ((v2.getX() - v1.getX()) * (v1.getY() - y)) - ((v1.getX() - x) * (v2.getY() - v1.getY()))  )
                   / (sqrt( pow(v2.getX() - v1.getX(),2) + pow(v2.getY() - v1.getY(),2)));

        //System.out.println(distance);
        return distance;
    }

    @Test
    public void t1s1()
    {
        System.out.println( getDistanceBetween( 1.0, 2.0, new GraphEdge( new Vertex( 2, 1 ), new Vertex( 4, 1 ) ) ) );
        System.out.println( getDistanceBetween( 1.0, 2.0, new GraphEdge( new Vertex( 1, 2 ), new Vertex( 4, 1 ) ) ) );
    }
}




