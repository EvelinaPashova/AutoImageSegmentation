package lv.lumii.pixelmaster.modules.compare.domain.graph;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * Directed image coordinate graph
 *
 * @author Aivars Šāblis
 * @since 15.05.2010
 */
public final class OGraph
{
    private DGraph<Point2D.Double, DefaultEdge> imageGraph;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a directed graph.
     */
    public OGraph()
    {
        imageGraph = new DefaultDirectedGraph<Point2D.Double, DefaultEdge>(DefaultEdge.class);
    } // ensure non-instantiability.


    //~ Methods ----------------------------------------------------------------

    @Override
    public Object clone()
    {
        OGraph newGraph = new OGraph();

        GraphUtils.addGraph(newGraph.imageGraph, this.imageGraph);

        return newGraph;
    }

    public void clear()
    {
        List<Point2D.Double> vertexSet = this.imageGraph.vertexSet();

        for (Point2D.Double vertex: vertexSet)
        {
           this.removeVertex(vertex);
        }
    }

    public DefaultEdge getEdge(Point2D.Double sourceVertex, Point2D.Double targetVertex)
    {
        return imageGraph.getEdge(sourceVertex, targetVertex);
    }

    public DefaultEdge addEdge(Point2D.Double sourceVertex, Point2D.Double targetVertex)
    {
        if (!(sourceVertex.equals(targetVertex) && containsVertex(sourceVertex)))
        {
            return imageGraph.addEdge(sourceVertex, targetVertex);
        }
        return null;
    }

    public boolean addEdge(Point2D.Double sourceVertex, Point2D.Double targetVertex, DefaultEdge e)
    {
        if (!(sourceVertex.equals(targetVertex) && containsVertex(sourceVertex)))
        {
            return imageGraph.addEdge(sourceVertex, targetVertex, e);
        }
        return false;
    }

    public boolean addVertex(Point2D.Double v)
    {
        return imageGraph.addVertex(v);
    }

    public Point2D.Double getEdgeSource(DefaultEdge e)
    {
        return imageGraph.getEdgeSource(e);
    }

    public Point2D.Double getEdgeTarget(DefaultEdge e)
    {
        return imageGraph.getEdgeTarget(e);
    }

    public boolean containsEdge(DefaultEdge e)
    {
        return imageGraph.containsEdge(e);
    }

    public boolean containsVertex(Point2D.Double v)
    {
        return imageGraph.containsVertex(v);
    }

    public List<DefaultEdge> edgeSet()
    {
        return imageGraph.edgeSet();
    }

    public List<DefaultEdge> edgesOf(Point2D.Double v)
    {
        return imageGraph.edgesOf(v);
    }

    public DefaultEdge removeEdge(Point2D.Double sourceVertex, Point2D.Double targetVertex)
    {
        return imageGraph.removeEdge(sourceVertex, targetVertex);
    }

    public boolean removeEdge(DefaultEdge e)
    {
        return imageGraph.removeEdge(e);
    }

    public boolean removeVertex(Point2D.Double v)
    {
        return imageGraph.removeVertex(v);
    }

    public List<Point2D.Double> vertexSet()
    {
        return imageGraph.vertexSet();
    }

    public int inDegreeOf(Point2D.Double v)
    {
        return imageGraph.inDegreeOf(v);
    }

    public List<DefaultEdge> incomingEdgesOf(Point2D.Double v)
    {
        return imageGraph.incomingEdgesOf(v);
    }

    public int outDegreeOf(Point2D.Double v)
    {
        return imageGraph.outDegreeOf(v);
    }

    public List<DefaultEdge> outgoingEdgesOf(Point2D.Double v)
    {
        return imageGraph.outgoingEdgesOf(v);
    }

    /**
     * Adds source graph to this
     * @param source OGraph
     * @return boolean
     */
    public boolean addGraph(OGraph source)
    { 
        return GraphUtils.addGraph(imageGraph, source.imageGraph);
    }

    public List<Point2D.Double> neighborListOf(Point2D.Double v)
    {
        return GraphUtils.neighborListOf(imageGraph, v);
    }

    public List<Point2D.Double> predecessorListOf(Point2D.Double v)
    {
        return GraphUtils.predecessorListOf(imageGraph, v);
    }

    public List<Point2D.Double> successorListOf(Point2D.Double v)
    {
        return GraphUtils.successorListOf(imageGraph, v);
    }

    public boolean testIncidence(DefaultEdge edge, Point2D.Double v)
    {
        return GraphUtils.testIncidence(imageGraph, edge, v);
    }

    /**
     * Returns total graph length
     *
     * @return double total graph length
     */
    public double getGraphLength()
    {
        List<DefaultEdge> edgeSet = imageGraph.edgeSet();
        double totalLength = 0;

        for (DefaultEdge edge: edgeSet)
        {
            totalLength += this.getEdgeLength(edge);
        }

        return totalLength;
    }

    /**
     * Calculates edge middle point
     *
     * @param edge DefaultEdge
     * @return edge middle point
     */
    public Point2D getEdgeMiddlePoint(DefaultEdge edge)
    {
        Point2D source = this.getEdgeSource(edge);
        Point2D target = this.getEdgeTarget(edge);

        return new Point2D.Double(0.5 * (source.getX() + target.getX()),
                                  0.5 * (source.getY() + target.getY()));
    }

    /**
     * Returns graphs edge length
     *
     * @param edge DefaultEdge
     * @return double length of edge
     */
    public double getEdgeLength(DefaultEdge edge)
    {
        Point2D source = this.getEdgeSource(edge);
        Point2D target = this.getEdgeTarget(edge);

        return Point2D.distance(source.getX(), source.getY(),
                                target.getX(), target.getY());
    }


    public class EdgelessGraphException     extends Exception {}
    public class WeightlessGraphException   extends Exception {}

    /**
     * Returns graph center of mass
     *
     * @return double center of mass
     * @throws EdgelessGraphException if edgeless
     * @throws WeightlessGraphException if zero weight
     */
    public Point2D getCenterOfMass() throws EdgelessGraphException, WeightlessGraphException
    {
        double  totalMass = 0;
        Point2D totalRadius = new Point2D.Double( 0, 0 );

        List<DefaultEdge> edgeSet = imageGraph.edgeSet();

        for (DefaultEdge edge: edgeSet)
        {
            double mass = this.getEdgeLength(edge);
            Point2D radius = this.getEdgeMiddlePoint(edge);


            totalMass += mass;

            totalRadius.setLocation(totalRadius.getX() + mass * radius.getX(),
                                    totalRadius.getY() + mass * radius.getY());
        }


        if ( totalMass == 0 )
        {
            throw new WeightlessGraphException();
        }
        else
        {
            totalMass = 1.0 / totalMass;
        }

        return new Point2D.Double(totalRadius.getX() * totalMass,
                                  totalRadius.getY() * totalMass); 
    }


    /**
     * Finds path (if exists) between start vertex and end vertex
     *
     * @param startVertex start vertex
     * @param endVertex end vertex
     * @return double steps for finding end vertex
     */
    public double findPath(Point2D.Double startVertex, Point2D.Double endVertex)
    {

        if (!imageGraph.containsVertex(endVertex)) {
            System.out.println(endVertex);
            throw new IllegalArgumentException(
                "graph must contain the end vertex");
            //return -1;
        }
        else {
        //TODO: Dijkstra maybe
        java.util.List<Point2D.Double> tmpVertexSet = new ArrayList<Point2D.Double>();

        tmpVertexSet.add(startVertex);

        boolean added = true;
        int i = 0;
        while (added && !tmpVertexSet.contains(endVertex))
        {
            added = false;
            java.util.List<Point2D.Double> newList = new ArrayList<Point2D.Double>();
            for (Point2D.Double vertex: tmpVertexSet)
            {
                //System.out.println(vertex);
                java.util.List<Point2D.Double> tmpList = neighborListOf(vertex);
                if (tmpList.size() > 0)
                {
                    newList.addAll(tmpList);
                }
            }

            for (Point2D.Double v: newList)
            {
               if (!tmpVertexSet.contains(v))
               {
                  tmpVertexSet.add(v);
                  added = true; 
               }
            }
            i++;
            if (i > 10)
            {
                added = false;
            }

        }
        if (added)
        {
            return i;
        }
        else
        {
            return -1;
        }
        }
    }
}
