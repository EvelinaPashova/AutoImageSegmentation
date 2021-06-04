package lv.lumii.pixelmaster.modules.compare.domain;

import lv.lumii.pixelmaster.modules.compare.domain.graph.DefaultEdge;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraph;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraphReader;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;

import static java.lang.Math.*;

/**
 * Find in graph informative connected components.
 * Author: Madara Augstkalne
 * Date: 2010.29.5
 */
public class ConnectedComponents {

    // shortest distance edge
    private static DefaultEdge closestEdge;

    private static double segmentValue;
    private static double distSegmentValue;

    private ArrayList<OGraph> componentList;

    private static OGraph imgGraph;

    private static double ShortDist;

    //Constructor
    public ConnectedComponents(OGraph imageGraph) {
        imgGraph = imageGraph;
        componentList = new ArrayList<OGraph>();
    }

    private static class FindDistanceBetweenComponentLines {

        /********** Functions to find distance from vertex to edge ***********/

        /**
         *  Compute the cross product AB x AC
         *  If if BA x AC is greater than 0, the nearest point is A
         *  @param A Vertex first vertex of edge
         *  @param B Vertex second vertex of edge
         *  @param C Point2D representing point from where distance is found
         *
         *  @return cross product
         */
        private static double getCross(Point2D A, Point2D B, Point2D C)
        {
            double vectorABx = B.getX() - A.getX();
            double vectorABy = B.getY() - A.getY();

            double vectorACx = C.getX() - A.getX();
            double vectorACy = C.getY() - A.getY();

            double cross;
            cross = (vectorABx * vectorACy) - (vectorABy * vectorACx);

            return cross;
        }
        /**
         * Returns distance between two points
         * @param Ax double vertex A from graph x coordinates
         * @param Ay double vertex A from graph y coordinates
         * @param Bx double vertex B from graph x coordinates
         * @param By double vertex B from graph y coordinates
         * @return distance between to points
         *
         */
        private static double getDistance(double Ax, double Ay, double Bx, double By)
        {
            double d1 = Ax - Bx;
            double d2 = Ay - By;

            return sqrt(d1 * d1 + d2 * d2);
        }


        /*
         *
         * @param  A Vertex first vertex of edge
         * @param  B Vertex second vertex of edge
         * @param  C Point2D representing point from where distance is found
         * @return distance between vertex and edge
         */
        private static double getDistanceBetweenP(Point2D A, Point2D B, Point2D C)
        {
            double distance;

            //System.out.println("Edge: " + A.getX() + "," + A.getY() + ":" + B.getX() + "," + B.getY() + "\n");
            //System.out.println("Point: " + C.getX() + "," + C.getY() + "\n");
            double BAx = B.getX() - A.getX();
            double BAy = B.getY() - A.getY();

            double inSegment = (((C.getX() - A.getX()) * (BAx)) + ((C.getY() - A.getY()) * (BAy))) /
                    (BAx * BAx + BAy * BAy);

            segmentValue = inSegment;
            //System.out.println("segmentValue in getDistanceBetweenP: " + segmentValue);

            if (inSegment > 1)
            {
                return getDistance(B.getX(), B.getY(), C.getX(), C.getY());
            }
            else if (inSegment < 0)
            {
                return getDistance(A.getX(), A.getY(), C.getX(), C.getY());
            }
            else
            {
                distance = getCross(A,B,C) / getDistance(A.getX(), A.getY(), B.getX(), B.getY());
                //distance = getDistance((A.getX() + BAx*inSegment), C.getX(),(A.getY() + BAy*inSegment), C.getY());
            }

            return abs(distance);
        }


        /**
         *
         *  Gets shortest distance between vertex of graph A and closest edge of graph B
         *
         * @param C Point2D Vertex C coordinates
         * @param graph UGraph graph
         * @return Shortest distance between vertex and closest edge
         *
         */
        private static double getShortestDistance(Point2D C, OGraph graph)
        {

            double shortestDistance = Double.POSITIVE_INFINITY;
            double tmpDistance;
           // Point2D.Double xPoint = new Point2D.Double();
           // DefaultEdge shortestEdge = new DefaultEdge();

            java.util.List<DefaultEdge> edgeSet = graph.edgeSet();


            int i = 0;
            while ((i < edgeSet.size()) && (shortestDistance != 0))
            {
               DefaultEdge edge = edgeSet.get(i);


               Point2D v1 = graph.getEdgeSource(edge);
               Point2D v2 = graph.getEdgeTarget(edge);

               //System.out.println("v1" + v1.toString() + "; v2:" + v2.toString() + "; C:" + C.toString());

               tmpDistance = getDistanceBetweenP(v1, v2, C);
               if (tmpDistance < shortestDistance)
               {
                   shortestDistance = tmpDistance;
                   closestEdge = edge;
                   distSegmentValue  = segmentValue;

                  // System.out.println("segmentValue in getShort ciklā: " + segmentValue + " " + shortestDistance);

               }

              //  System.out.println("segmentValue in getShort: " + segmentValue + " " + shortestDistance);

               i++;
            }

            //System.out.println("shortestDistance: " + shortestDistance);
            //System.out.println();

            return shortestDistance;
        }

    }

    /**
     * Remove useless components from component list
     */

    public void RemoveUselessComponents()
    {
        boolean removed;

       //System.out.println("Before remove: " + this.componentList.size());
        //Append all components graphs
        for (int i = 0; i < this.componentList.size(); i++)
        {
           // System.out.println("Lengt " + (int)this.componentList.get(i).getGraphLength());
            int size =  (int)this.componentList.get(i).getGraphLength();

            //previous 6
            //System.out.println("Segment length " + size);
            if (size < 8)
            {
                //System.out.println("Removed component: " + this.componentList.get(i).getGraphLength());
                this.componentList.remove(this.componentList.get(i));
                removed = true;
            }
            else
            {
                removed = false;
            }

            if (removed)
            {
                i--;
            }
        }
        //System.out.println("After remove: " + this.componentList.size());
    }

    /**
     * Divide source image graph in components
     * @param imgGraph
     */

    public void FindGraphComponents(OGraph imgGraph) {

        java.util.List<Point2D.Double> vertexList = imgGraph.vertexSet();

       while(vertexList.size() > 1)
       {

        java.util.List<Point2D.Double> vertexCurr = new ArrayList<Point2D.Double>();
        vertexCurr.add(vertexList.get(vertexList.size()-1));

        java.util.List<DefaultEdge> edgeList ;

        boolean found = true;


            while (found)
            {
                found = false;
                java.util.List<Point2D.Double> neighbourList = new ArrayList<Point2D.Double>();
                for (Point2D.Double vertex: vertexCurr)
                {
                    java.util.List<Point2D.Double> tmpList = imgGraph.neighborListOf(vertex);
                    if (tmpList.size() > 0)
                    {
                        neighbourList.addAll(tmpList);
                    }
                }

                for (Point2D.Double v: neighbourList)
                {
                     if (!vertexCurr.contains(v))
                     {
                         vertexCurr.add(v);
                         found = true;
                    }
                }
            }

             OGraph compGraph = new OGraph();

            for (Point2D.Double vertex: vertexCurr)
            {
                    edgeList  = imgGraph.edgesOf(vertex);

                    for(DefaultEdge edge : edgeList)
                    {
                        if (!compGraph.containsEdge(edge))
                        {

                            Point2D.Double vertex1 = imgGraph.getEdgeSource(edge);
                            compGraph.addVertex(vertex1);

                            Point2D.Double vertex2 = imgGraph.getEdgeTarget(edge);
                            compGraph.addVertex(vertex2);

                            compGraph.addEdge(vertex1, vertex2);

                        }
                    }
            }
            vertexList.removeAll(vertexCurr);

           this.componentList.add(compGraph);
       }
    }

    /**
     * Connect two graphs.
     * Create connective edge.
     * @param Agraph
     * @param Bgraph
     * @param edge
     * @param p
     * @param segmentValue
     * @return
     */

    public boolean ConnectComponentGraphs(OGraph Agraph, OGraph Bgraph, DefaultEdge edge, Point2D.Double p, double segmentValue) {

        Point2D.Double xPoint = new Point2D.Double();

        Point2D.Double v1 = Agraph.getEdgeSource(edge);
        Point2D.Double v2 = Agraph.getEdgeTarget(edge);


        //connect graphs
        Agraph.addGraph(Bgraph);

        //calculate cross point

        if (segmentValue > 1)
        {
            Agraph.addEdge(p, v2);

        }
        else if (segmentValue < 0)
        {
            Agraph.addEdge(p, v1);
        }
        else
        {
            boolean ad;

            xPoint.setLocation(v1.getX() + (v2.getX()-v1.getX())*segmentValue,v1.getY() + (v2.getY()-v1.getY())*segmentValue);
            ad = Agraph.addVertex(xPoint);
            Agraph.addEdge(v1, xPoint);
            Agraph.addEdge(xPoint, v2);
            Agraph.addEdge(xPoint, p);
            Agraph.removeEdge(v1,v2);

        }

        return true;
    }

    /**
     * find distance between graphs, and compare closest
     * @param igraph
     * @param jgraph
     * @return if graphs are compared 
     */

    public boolean FindDistanceBetweenGraphs (OGraph igraph, OGraph jgraph) {

        double ShortestDistance;

        Point2D.Double ClosestVertex = null;

        DefaultEdge ClosestEdge = null;

        double tmpSegmentValue = 0;

        double tmpShortestDistance = Double.POSITIVE_INFINITY;
        
        boolean isCompare = false;

        java.util.List<Point2D.Double> ivertexSet = igraph.vertexSet();
        for (Point2D.Double ivertex: ivertexSet)
        {
            ShortestDistance= FindDistanceBetweenComponentLines.getShortestDistance(ivertex, jgraph);

            if (tmpShortestDistance > ShortestDistance)
            {
                tmpShortestDistance = ShortestDistance;
                ClosestVertex = ivertex;

                ClosestEdge = closestEdge;

                tmpSegmentValue = distSegmentValue;

            }
        }

        ShortDist = tmpShortestDistance;
        //System.out.println("Closest distance " + tmpShortestDistance);

        if (tmpShortestDistance < 11) // previous "10"
        {
            isCompare = ConnectComponentGraphs(igraph, jgraph, ClosestEdge, ClosestVertex, tmpSegmentValue);
            this.componentList.remove(jgraph);
        }

        return isCompare;
    }

    public void ConnectClosestComponents ()  throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {

        Point2D iCenterOfMass;
        Point2D jCenterOfMass;

        boolean isConnect;

        for (int i = 0; i < this.componentList.size()-1; i++)
        {

            iCenterOfMass =  this.componentList.get(i).getCenterOfMass();
           
            for (int j = 1 ; j < this.componentList.size(); j++)
            {
                jCenterOfMass =  this.componentList.get(j).getCenterOfMass();


                //don`t check component by itself
                if(i != j)
                {
                    //check graphs center of mass

                    double massCenterDistance = 0;
                    if (iCenterOfMass != null)
                    {
                         massCenterDistance = sqrt(pow((iCenterOfMass.getX()-jCenterOfMass.getX()),2) +
                                 pow((iCenterOfMass.getY()-jCenterOfMass.getY()),2));

                    }

                    //if center of mass is less than 40, find closest distance between graphs
                    //previous 60   -  20
                   // System.out.println("mass center distance " + massCenterDistance);
                    if ((massCenterDistance < 66) && (massCenterDistance != 0))
                    {
                        //connect closest graphs
                        isConnect = FindDistanceBetweenGraphs(this.componentList.get(i), this.componentList.get(j));

                        //round from beginning                                                                        
                        if (isConnect)
                        {
                            i = 0;
                            j = 0;
                        }

                    }
                }
            }
        }
    }

    public  ArrayList<OGraph> ReturnComponentList ()
    {
        return this.componentList;
    }

    public double getShortestDistance ()
    {
        return ShortDist;
    }

    public ArrayList<OGraph> FindGraphComponents ()
    {

        this.FindGraphComponents(imgGraph);

        this.RemoveUselessComponents();

        try {
            this.ConnectClosestComponents();

        } catch (OGraph.EdgelessGraphException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (OGraph.WeightlessGraphException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

//        for (int m = 0; m < this.componentList.size(); m++) {
//            OGraphReader.write(this.componentList.get(m), new File("ChackFile.txt"));
//        }

        return this.componentList;
    }
}
