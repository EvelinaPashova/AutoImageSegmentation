package lv.lumii.pixelmaster.modules.compare.domain;

import lv.lumii.pixelmaster.core.api.domain.RGB;
import lv.lumii.pixelmaster.modules.compare.domain.graph.DefaultEdge;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraph;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGrid;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

import static java.lang.Math.*;
import static lv.lumii.pixelmaster.modules.ridge.domain.Util.drawLine;

/**
 *
 * Compares two directed graphs
 *
 * @author Aivars Šāblis
 *
 * double compare coefficient
 */
public class OCompareLines
{
    //~ Instance fields --------------------------------------------------------

    private OGraph graphA, graphB, copyGraphA, copyGraphB;
    private double xOffset, yOffset;
    private int totalSteps;
    private double onePieceSize;
    private RasterImage resultImg;
    private double segmentValue;
    HashMap<Point2D.Double, Point> vertexMapA;
    HashMap<Point, Point2D.Double> vertexMapB;
    ArrayList<EdgeMap> coefMap = new ArrayList<EdgeMap>();
    OGrid gridA;
    OGrid gridB;


    private class EdgeMap {
        Point2D.Double v1;
        Point2D.Double v2;
        double coef;

        EdgeMap (Point2D.Double v1, Point2D.Double v2, double coef)
        {
            this.v1 = v1;
            this.v2 = v2;
            this.coef = coef;
        }
    }


    //~ Constructors -----------------------------------------------------------

    /**
     *  //Does not allow to create objects with default constructor to avoid errors.
     *
     *  Created by Aivars Šāblis 10.03.2010
     */
    public OCompareLines()
    {
        //assert false;
        this.totalSteps = 0;
        this.vertexMapA = new HashMap<Point2D.Double, Point>();
        this.vertexMapB = new HashMap<Point, Point2D.Double>();

        this.setOnePieceSize(1);
    }


    /**
     * Constructor
     * @param graphA UGraph first graph
     * @param graphB UGraph second graph
     *
     * @throws newgraph.OGraph.EdgelessGraphException something
     * @throws newgraph.OGraph.WeightlessGraphException something
     *
     * Created by Aivars Šāblis 09.03.2010
     */
    public OCompareLines(OGraph graphA, OGraph graphB) throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
        this.graphA = graphA;
        this.graphB = graphB;
        this.totalSteps = 0;
        vertexMapA = new HashMap<Point2D.Double, Point>();
        vertexMapB = new HashMap<Point, Point2D.Double>();

        this.calculateOffset();

        this.setOnePieceSize(1);
    }


    //~ Methods ----------------------------------------------------------------

    /**
     * Loads first graph
     *
     * @param graphA UGraph
     * @throws newgraph.OGraph.EdgelessGraphException something
     * @throws newgraph.OGraph.WeightlessGraphException something
     */
    public void LoadGraphA(OGraph graphA) throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException
    {
        this.graphA = graphA;
        this.copyGraphA = graphA;

        if (this.graphB != null)
        {
             try
            {
                this.calculateOffset();
            }
            catch (OGraph.EdgelessGraphException e)
            {
                e.printStackTrace();
            }
            catch (OGraph.WeightlessGraphException e)
            {
                e.printStackTrace();
            }
        }
    }


    /**
     * Loads second graph
     *
     * @param graphB  UGraph
     * @throws newgraph.OGraph.EdgelessGraphException something
     * @throws newgraph.OGraph.WeightlessGraphException something
     */
    public void LoadGraphB(OGraph graphB) throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException
    {
        this.graphB = graphB;
        this.copyGraphB = graphB;

        try
        {
            this.calculateOffset();
        }
        catch (OGraph.EdgelessGraphException e)
        {
            e.printStackTrace();
        }
        catch (OGraph.WeightlessGraphException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Calculate offset between graphA and graphB
     *
     * @throws newgraph.OGraph.EdgelessGraphException something
     * @throws newgraph.OGraph.WeightlessGraphException something
     *
     * Added by Aivars Šāblis.
     */
    private void calculateOffset() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException
    {
        //and Graph A Center of Mass
        Point2D aMassCentre = this.graphA.getCenterOfMass();
        //and Graph B Center of Mass
        Point2D bMassCentre = this.graphB.getCenterOfMass();

        //System.out.println("a" + aMassCentre + " b" + bMassCentre);

        //calculate offset between two graphs
        this.xOffset = bMassCentre.getX() - aMassCentre.getX();
        this.yOffset = bMassCentre.getY() - aMassCentre.getY();
    }


    /** Sets on step size of line segment, between two points on edge
     *
     * @param size double - one step size
     * Added by Aivars Šāblis 15.04.2010
     */
    public void setOnePieceSize(double size)
    {
        this.onePieceSize = size;
    }


    /**
     * Initializes image (for graph drawing)
     *
     * @param width int image width
     * @param height int image height
     */
    public void setRasterImage(int width, int height)
    {
        this.resultImg = new RasterImage(width, height);
    }


    /**
     * Returns image
     *
     * @return RasterImage image ith graph drawing
     */
    public RasterImage returnRasterImage()
    {
        return this.resultImg;
    }


    /**
     * Allows to change offset between graphs
     *
     *
     * @param xOffset double sets required x offset, -1 for no changes
     * @param yOffset double sets required y offset, -1 for no changes
     *
     */
    public void setOffset (double xOffset, double yOffset)
    {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        }


    /**
     * Draws segment line in image in color according to segment coefficient
     *
     * @param C1 Point segment start point
     * @param C2 Point segment end point
     * @param segmentCoefficient double segment coefficient
     */
    public void drawGraphLine(Point C1, Point C2, double segmentCoefficient)
    {
        int colorWeight = (int)round(15*segmentCoefficient);

        if (colorWeight > 255)
        {
            colorWeight = 255;
        }

        int color = RGB.getRGB(colorWeight,0,255-colorWeight);


       // System.out.println(C1 + " ; " + C2);
        drawLine(this.resultImg, C1, C2, color);
    }


    /**
     * Draws segment line in image in color according to segment coefficient
     *
     * @param C1 Point2D.Double segment start point
     * @param C2 Point2D.Dobule segment end point
     * @param segmentCoefficient double segment coefficient
     */
    public void drawGraphLine(Point2D C1, Point2D C2, double segmentCoefficient)
    {
        int colorWeight = (int)round(15*segmentCoefficient);

        if (colorWeight > 255)
        {
            colorWeight = 255;
        }

        int color = RGB.getRGB(colorWeight,0,255-colorWeight);

        Point P1 = new Point((int)round(C1.getX() + this.xOffset/2), (int)round(C1.getY() + this.yOffset/2));
        Point P2 = new Point((int)round(C2.getX() + this.xOffset/2), (int)round(C2.getY() + this.yOffset/2));

       // System.out.println(C1 + " ; " + C2);
        drawLine(this.resultImg, P1, P2, color);
    }

    private void drawImage()
    {
       for (int i = 0, coefMapSize = this.coefMap.size(); i < coefMapSize; i++) {
            EdgeMap tmp = this.coefMap.get(i);

            //System.out.println("v1:" + tmp.v1 + " v2:" + tmp.v2 + " coef:" + tmp.coef);
            this.drawGraphLine(tmp.v1, tmp.v2, tmp.coef);

        }

    }


    /**
     *  Compute the cross product AB x AC
     *  If if BA x AC is greater than 0, the nearest point is A
     *  @param A Vertex first vertex of edge
     *  @param B Vertex second vertex of edge
     *  @param C Point2D representing point from where distance is found
     *
     *  @return cross product
     *
     * Added by Madara Augstkalne. Modified by Aivars Šāblis
     */
    private double getCross(Point2D A, Point2D B, Point2D C)
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
     * Added by Madara Augstkalne. Modified by Aivars Šāblis
     */
    private double getDistance(double Ax, double Ay, double Bx, double By)
    {
        double d1 = Ax - Bx;
        double d2 = Ay - By;

        return sqrt(d1 * d1 + d2 * d2);
    }


    /**
     *  Gets shortest distance between vertex of graph A and closest edge of graph B
     *
     * @param  A Vertex first vertex of edge
     * @param  B Vertex second vertex of edge
     * @param  C Point2D representing point from where distance is found
     * @return distance between vertex and edge
     * Added by Aivars Šāblis 13.04.2010.
     */
    private double getDistanceBetweenP(Point2D A, Point2D B, Point2D C)
    {
        double distance;

        //System.out.println("Edge: " + A.getX() + "," + A.getY() + ":" + B.getX() + "," + B.getY() + "\n");
        //System.out.println("Point: " + C.getX() + "," + C.getY() + "\n");
        double BAx = B.getX() - A.getX();
        double BAy = B.getY() - A.getY();

        double inSegment = (((C.getX() - A.getX()) * (BAx)) + ((C.getY() - A.getY()) * (BAy))) /
                (BAx * BAx + BAy * BAy);

        this.segmentValue = inSegment;

        if (inSegment >= 1)
        {
            return getDistance(B.getX(), B.getY(), C.getX(), C.getY());
        }
        else if (inSegment <= 0)
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
     * Added by Aivars Šāblis.
     */
    private double getShortestDistance(Point2D C, OGraph graph)
    {

        double shortestDistance = Double.POSITIVE_INFINITY;
        double tmpDistance;

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
           }

           i++;
        }

        //System.out.println("shortestDistance: " + shortestDistance);
        //System.out.println();

        return shortestDistance;
    }


    /**
     *
     *  Gets shortest distance between vertex of graph A and closest edge of graph B
     *  Used by sequence algorithm
     *
     * @param C Point2D Vertex C coordinates
     * @param graph UGraph graph
     * @return Shortest distance between vertex and closest edge
     *
     * Added by Aivars Šāblis.
     */
    private double getShortestDistanceSequence(Point2D C, OGraph graph)
    {

        double shortestDistance = Double.POSITIVE_INFINITY;
        double tmpDistance;
        Point2D.Double xPoint = new Point2D.Double();
        DefaultEdge shortestEdge = new DefaultEdge();

        java.util.List<DefaultEdge> edgeSet = graph.edgeSet();


        int i = 0;
        while ((i < edgeSet.size()) && (shortestDistance != 0))
        {
           DefaultEdge edge = edgeSet.get(i);


           Point2D.Double v1 = graph.getEdgeSource(edge);
           Point2D.Double v2 = graph.getEdgeTarget(edge);

           //System.out.println("v1" + v1.toString() + "; v2:" + v2.toString() + "; C:" + C.toString());

           tmpDistance = getDistanceBetweenP(v1, v2, C);
           if (tmpDistance < shortestDistance)
           {
               //System.out.println("segmentValue: " + segmentValue);
               shortestDistance = tmpDistance;
               //get point on B Graph
               if (segmentValue >= 1)
               {
                   xPoint.setLocation(v2.getX(), v2.getY());
               }
               else if (segmentValue <= 0)
               {
                   xPoint.setLocation(v1.getX(), v1.getY());
               }
               else
               {
                    xPoint.setLocation(v1.getX() + (v2.getX()-v1.getX())*segmentValue,v1.getY() + (v2.getY()-v1.getY())*segmentValue);
               }
               //System.out.println("xPoint: " + xPoint);
               shortestEdge = edge;
           }

           i++;
        }


        //add shortest distance vector crosspoint on B graph
        vertexMapB.put(new Point(this.totalSteps,1), xPoint);


        if (graph.addVertex(xPoint))
        {
            graph.addEdge(graph.getEdgeSource(shortestEdge), xPoint);
            graph.addEdge(xPoint, graph.getEdgeTarget(shortestEdge));
            graph.removeEdge(shortestEdge);
        }


        //System.out.println("shortestDistance: " + shortestDistance);
        //System.out.println();

        return shortestDistance;
    }


    /**
     * Iterating through graph to find distance to second graph via sequence
     *
     * @param graphA OGraph source graph copy
     * @param graphB OGraph target graph copy
     * @return totalCoefficient double distance coefficient
     */
    private double IterThroughGraphEdgesSequence(OGraph graphA, OGraph graphB)
    {
        double totalCoefficient = 0;
        Point2D C = new Point2D.Double();


        java.util.List<DefaultEdge> edgeSet = graphA.edgeSet();

        for (DefaultEdge edge: edgeSet)
        {
            Point2D.Double av1 = graphA.getEdgeSource(edge);
            Point2D.Double av2 = graphA.getEdgeTarget(edge);
            double edgeCoefficient = 0;
            double tmpCoefficient;

            //make parametric line equation from edge vertex coordinates
            double aX = av2.getX() - av1.getX();
            double aY = av2.getY() - av1.getY();

            int step = 0;
            double pointX0 = av1.getX();
            double pointY0 = av1.getY();
            double pointX = pointX0;
            double pointY = pointY0;


            C.setLocation(pointX0 + this.xOffset, pointY0 + this.yOffset);

            //getting edge step length
            double stepLengthX = aX * this.onePieceSize / graphA.getEdgeLength(edge);
            double stepLengthY = aY * this.onePieceSize / graphA.getEdgeLength(edge);

            if (av1.getX() <= av2.getX() && av1.getY() <= av2.getY())
            {
                while ((pointX <= av2.getX()) && (pointY <= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB);
                    edgeCoefficient += tmpCoefficient;

                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                    if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {

                        graphA.addVertex(tmp);

                        graphA.addEdge(av1, tmp);
                        graphA.addEdge(tmp, av2);
                        graphA.removeEdge(av1, av2);
                        av1 = tmp;
                    }

                    step = step + 1;
                    this.totalSteps++;

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() <= av2.getX() && av1.getY() >= av2.getY())
            {
                while ((pointX <= av2.getX()) && (pointY >= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB);
                    edgeCoefficient += tmpCoefficient;

                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                   if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {

                        graphA.addVertex(tmp);

                        graphA.addEdge(av1, tmp);
                        graphA.addEdge(tmp, av2);
                        graphA.removeEdge(av1, av2);
                        av1 = tmp;
                    }

                    step = step + 1;
                    this.totalSteps++;

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() >= av2.getX() && av1.getY() <= av2.getY())
            {
                while ((pointX >= av2.getX()) && (pointY <= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB);
                    edgeCoefficient += tmpCoefficient;


                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                   if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {

                        graphA.addVertex(tmp);

                        graphA.addEdge(av1, tmp);
                        graphA.addEdge(tmp, av2);
                        graphA.removeEdge(av1, av2);
                        av1 = tmp;
                    }

                    step = step + 1;
                    this.totalSteps++;

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() >= av2.getX() && av1.getY() >= av2.getY())
            {
                while ((pointX >= av2.getX()) && (pointY >= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB);


                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                    if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {
                        graphA.addVertex(tmp);

                        graphA.addEdge(av1, tmp);
                        graphA.addEdge(tmp, av2);
                        graphA.removeEdge(av1, av2);
                        av1 = tmp;
                    }

                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;
                    this.totalSteps++;

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }


            totalCoefficient += edgeCoefficient;
        }


        return totalCoefficient;
     }


    /**
     * Iterating through graph to find distance to second graph via sequence and draw
     *
     * @param graphA OGraph source graph copy
     * @param graphB OGraph target graph copy
     * @return totalCoefficient double distance coefficient
     */
    private double IterThroughGraphEdgesSequenceDraw(OGraph graphA, OGraph graphB)
    {
        double totalCoefficient = 0;
        Point2D C = new Point2D.Double();


        java.util.List<DefaultEdge> edgeSet = graphA.edgeSet();

        for (DefaultEdge edge: edgeSet)
        {
            Point2D.Double av1 = graphA.getEdgeSource(edge);
            Point2D.Double av2 = graphA.getEdgeTarget(edge);
            double edgeCoefficient = 0;
            double tmpCoefficient;

            //make parametric line equation from edge vertex coordinates
            double aX = av2.getX() - av1.getX();
            double aY = av2.getY() - av1.getY();

            int step = 0;
            double pointX0 = av1.getX();
            double pointY0 = av1.getY();
            double pointX = pointX0;
            double pointY = pointY0;



            C.setLocation(pointX0 + this.xOffset, pointY0 + this.yOffset);
            //vertexMapA.put(av1, new Point(this.totalSteps + step,1));



            //getting edge step length
            double stepLengthX = aX * this.onePieceSize / graphA.getEdgeLength(edge);
            double stepLengthY = aY * this.onePieceSize / graphA.getEdgeLength(edge);

            if (av1.getX() <= av2.getX() && av1.getY() <= av2.getY())
            {
                while ((pointX <= av2.getX()) && (pointY <= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB);
                    edgeCoefficient += tmpCoefficient;

                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                    if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {

                        graphA.addVertex(tmp);

                        graphA.addEdge(av1, tmp);
                        graphA.addEdge(tmp, av2);
                        graphA.removeEdge(av1, av2);
                        av1 = tmp;
                    }

                    step = step + 1;
                    this.totalSteps++;



                   /* Point v1 = new Point();

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    Point v2 = new Point((int)round(pointX+this.xOffset/2), (int)round(pointY+this.yOffset/2));

                    this.drawGraphLine(v1, v2, tmpCoefficient);   */

                    Point2D.Double prevPoint = new Point2D.Double(pointX, pointY);

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    this.coefMap.add(new EdgeMap(prevPoint, new Point2D.Double(pointX, pointY), tmpCoefficient));

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() <= av2.getX() && av1.getY() >= av2.getY())
            {
                while ((pointX <= av2.getX()) && (pointY >= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB);
                    edgeCoefficient += tmpCoefficient;


                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                   if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {

                        graphA.addVertex(tmp);

                        graphA.addEdge(av1, tmp);
                        graphA.addEdge(tmp, av2);
                        graphA.removeEdge(av1, av2);
                        av1 = tmp;
                    }

                    step = step + 1;
                    this.totalSteps++;

                    Point2D.Double prevPoint = new Point2D.Double(pointX, pointY);

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    this.coefMap.add(new EdgeMap(prevPoint, new Point2D.Double(pointX, pointY), tmpCoefficient));

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() >= av2.getX() && av1.getY() <= av2.getY())
            {
                while ((pointX >= av2.getX()) && (pointY <= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB);
                    edgeCoefficient += tmpCoefficient;


                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                   if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {

                        graphA.addVertex(tmp);

                        graphA.addEdge(av1, tmp);
                        graphA.addEdge(tmp, av2);
                        graphA.removeEdge(av1, av2);
                        av1 = tmp;
                    }

                    step = step + 1;
                    this.totalSteps++;

                    Point2D.Double prevPoint = new Point2D.Double(pointX, pointY);

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    this.coefMap.add(new EdgeMap(prevPoint, new Point2D.Double(pointX, pointY), tmpCoefficient));

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() >= av2.getX() && av1.getY() >= av2.getY())
            {
                while ((pointX >= av2.getX()) && (pointY >= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB);


                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                    if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {
                        graphA.addVertex(tmp);

                        graphA.addEdge(av1, tmp);
                        graphA.addEdge(tmp, av2);
                        graphA.removeEdge(av1, av2);
                        av1 = tmp;
                    }

                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;
                    this.totalSteps++;

                    Point2D.Double prevPoint = new Point2D.Double(pointX, pointY);

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    this.coefMap.add(new EdgeMap(prevPoint, new Point2D.Double(pointX, pointY), tmpCoefficient));

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }

            //this.totalSteps = this.totalSteps + step;
            totalCoefficient += edgeCoefficient;
        }


        return totalCoefficient;
     }



    /**
     * Iterating through graph to find distance to second graph
     *
     * @param graphA UGraph Graph from who is finding distance
     * @param graphB UGraph Graph
     * @return coefficient double
     */
    private double IterThroughGraphEdges(OGraph graphA, OGraph graphB)
    {
        double totalCoefficient = 0;
        Point2D C = new Point2D.Double();

        java.util.List<DefaultEdge> edgeSet = graphA.edgeSet();

        for (DefaultEdge edge: edgeSet)
        {
            Point2D av1 = graphA.getEdgeSource(edge);
            Point2D av2 = graphA.getEdgeTarget(edge);
            double edgeCoefficient = 0;
            double tmpCoefficient;

            //double edgeOverhead = edge.getLength() - floor(edge.getLength() / onePieceSize) * onePieceSize;

            //make parametric line equation from edge vertex coordinates
            double aX = av2.getX() - av1.getX();
            double aY = av2.getY() - av1.getY();

            //System.out.println("edge.getLength(): " + graphA.getEdgeLength(edge));
            //System.out.println("vector ax" + aX + " ay" + aY);
            //System.out.println("offset ax" + this.xOffset + " ay" + this.yOffset);
            //System.out.println();
            //System.out.println("BEdge: " + av1.getX() + "," + av1.getY() + ":" + av2.getX() + "," + av2.getY());

            int step = 0;
            double pointX0 = av1.getX();
            double pointY0 = av1.getY();
            double pointX = pointX0;
            double pointY = pointY0;


            C.setLocation(pointX0 + this.xOffset, pointY0 + this.yOffset);

            //getting edge step length
            double stepLengthX = aX * this.onePieceSize / graphA.getEdgeLength(edge);
            double stepLengthY = aY * this.onePieceSize / graphA.getEdgeLength(edge);

            //System.out.println(this.onePieceSize);
            //System.out.println("step length ax" + stepLengthX + " ay" + stepLengthY);

            if (av1.getX() <= av2.getX() && av1.getY() <= av2.getY())
            {
                while ((pointX <= av2.getX()) && (pointY <= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistance(C, graphB);
                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() <= av2.getX() && av1.getY() >= av2.getY())
            {
                while ((pointX <= av2.getX()) && (pointY >= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistance(C, graphB);
                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() >= av2.getX() && av1.getY() <= av2.getY())
            {
                while ((pointX >= av2.getX()) && (pointY <= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistance(C, graphB);
                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() >= av2.getX() && av1.getY() >= av2.getY())
            {
                while ((pointX >= av2.getX()) && (pointY >= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistance(C, graphB);
                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }

            this.totalSteps = this.totalSteps + step;
            totalCoefficient += edgeCoefficient;
        }


        return totalCoefficient;
    }


    /**
     * Iterating through graph to find distance to second graph and draw
     *
     * @param graphA UGraph Graph from who is finding distance
     * @param graphB UGraph Graph
     * @return coefficient double
     */
    private double IterThroughGraphEdgesDraw(OGraph graphA, OGraph graphB)
    {
        double totalCoefficient = 0;
        Point2D C = new Point2D.Double();

        java.util.List<DefaultEdge> edgeSet = graphA.edgeSet();

        for (DefaultEdge edge: edgeSet)
        {
            Point2D av1 = graphA.getEdgeSource(edge);
            Point2D av2 = graphA.getEdgeTarget(edge);
            double edgeCoefficient = 0;
            double tmpCoefficient;

            //double edgeOverhead = edge.getLength() - floor(edge.getLength() / onePieceSize) * onePieceSize;

            //make parametric line equation from edge vertex coordinates
            double aX = av2.getX() - av1.getX();
            double aY = av2.getY() - av1.getY();

            //System.out.println("edge.getLength(): " + edge.getLength());
            //System.out.println("vector ax" + aX + " ay" + aY);
            //System.out.println("offset ax" + this.xOffset + " ay" + this.yOffset);
            //System.out.println();
            //System.out.println("BEdge: " + av1.getX() + "," + av1.getY() + ":" + av2.getX() + "," + av2.getY());

            int step = 0;
            double pointX0 = av1.getX();
            double pointY0 = av1.getY();
            double pointX = pointX0;
            double pointY = pointY0;


            C.setLocation(pointX0 + this.xOffset, pointY0 + this.yOffset);

            //getting edge step length
            double stepLengthX = aX * this.onePieceSize / graphA.getEdgeLength(edge);
            double stepLengthY = aY * this.onePieceSize / graphA.getEdgeLength(edge);

            if (av1.getX() <= av2.getX() && av1.getY() <= av2.getY())
            {
                while ((pointX <= av2.getX()) && (pointY <= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistance(C, graphB);
                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;


                    Point v1 = new Point((int)round(pointX+this.xOffset/2), (int)round(pointY+this.yOffset/2));

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    Point v2 = new Point((int)round(pointX+this.xOffset/2), (int)round(pointY+this.yOffset/2));

                    this.drawGraphLine(v1, v2, tmpCoefficient);
                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() <= av2.getX() && av1.getY() >= av2.getY())
            {
                while ((pointX <= av2.getX()) && (pointY >= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistance(C, graphB);
                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;


                    Point v1 = new Point((int)round(pointX+this.xOffset/2), (int)round(pointY+this.yOffset/2));

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    Point v2 = new Point((int)round(pointX+this.xOffset/2), (int)round(pointY+this.yOffset/2));

                    this.drawGraphLine(v1, v2, tmpCoefficient);
                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() >= av2.getX() && av1.getY() <= av2.getY())
            {
                while ((pointX >= av2.getX()) && (pointY <= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistance(C, graphB);
                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;


                    Point v1 = new Point((int)round(pointX+this.xOffset/2), (int)round(pointY+this.yOffset/2));

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    Point v2 = new Point((int)round(pointX+this.xOffset/2), (int)round(pointY+this.yOffset/2));

                    this.drawGraphLine(v1, v2, tmpCoefficient);
                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() >= av2.getX() && av1.getY() >= av2.getY())
            {
                while ((pointX >= av2.getX()) && (pointY >= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistance(C, graphB);
                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;


                    Point v1 = new Point((int)round(pointX+this.xOffset/2), (int)round(pointY+this.yOffset/2));

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    Point v2 = new Point((int)round(pointX+this.xOffset/2), (int)round(pointY+this.yOffset/2));

                    this.drawGraphLine(v1, v2, tmpCoefficient);
                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }

            this.totalSteps = this.totalSteps + step;
            totalCoefficient += edgeCoefficient;
        }


        return totalCoefficient;
    }


    /**
     * Check whenever point sequence on B graph is same as A graph and draws image
     *
     * @param graphA OGraph source graph
     * @param graphB OGraph target graph
     *
     * @return totalCoefficient double sequence coefficient
     */
    private double checkSequenceDraw(OGraph graphA, OGraph graphB)
    {
        double totalCoefficient = 0;


        java.util.List<Point2D.Double> vertexSetA = graphA.vertexSet();
        for (Point2D.Double vertex: vertexSetA)
        {
            java.util.List<Point2D.Double> neighborsList = graphA.neighborListOf(vertex);

            Point iPoint = this.vertexMapA.get(vertex);

            if (iPoint != null) {
                //System.out.println("vertex: " + vertex);

                Point2D.Double bVertex = this.vertexMapB.get(iPoint);

                for (Point2D.Double nVertex: neighborsList)
                {
                    Point nPoint = this.vertexMapA.get(nVertex);

                    if (nPoint != null) {
                        Point2D.Double nbPoint = this.vertexMapB.get(nPoint);

                        //System.out.println("s v1: " + bVertex + "; " + iPoint + " v2: " + nbPoint + ";");
                        double distance = graphB.findPath(bVertex, nbPoint);

                        //System.out.println(distance);
                        if (distance > 5)
                        {
                            totalCoefficient += distance / this.totalSteps;
                            //System.out.println(distance);
                            //System.out.println("s v1: " + vertex + "; " + iPoint + " v2: " + nVertex + ";");
                            for (int i = 0, coefMapSize = this.coefMap.size(); i < coefMapSize; i++) {
                                EdgeMap tmp = this.coefMap.get(i);


                                if (tmp.v1.equals(vertex) && tmp.v2.equals(nVertex))
                                {
                                    //System.out.println("x v1:" + tmp.v1 + " v2:" + tmp.v2 + " coef:" + tmp.coef);
                                    tmp.coef += distance * 20;
                                    i = this.coefMap.size();
                                }
                            }
                        }
                        if (distance < 0)
                        {
                            totalCoefficient += 1;
                        }
                    }
                }
            }
        }
        return totalCoefficient;
    }


    /**
     * Check whenever point sequence on B graph is same as A graph
     *
     * @param graphA OGraph source graph
     * @param graphB OGraph target graph
     *
     * @return totalCoefficient double sequence coefficient
     */
    private double checkSequence(OGraph graphA, OGraph graphB)
    {
        double totalCoefficient = 0;


        java.util.List<Point2D.Double> vertexSetA = graphA.vertexSet();
        for (Point2D.Double vertex: vertexSetA)
        {
            java.util.List<Point2D.Double> neighborsList = graphA.neighborListOf(vertex);

            Point iPoint = this.vertexMapA.get(vertex);

            if (iPoint != null) {
                //System.out.println("vertex: " + vertex);

                Point2D.Double bVertex = this.vertexMapB.get(iPoint);

                for (Point2D.Double nVertex: neighborsList)
                {
                    Point nPoint = this.vertexMapA.get(nVertex);

                    if (nPoint != null) {
                        Point2D.Double nbPoint = this.vertexMapB.get(nPoint);

                        //System.out.println("s v1: " + bVertex + "; " + iPoint + " v2: " + nbPoint + ";");
                        double distance = graphB.findPath(bVertex, nbPoint);

                        //System.out.println(distance);
                        if (distance > 1)
                        {
                            totalCoefficient += distance / this.totalSteps;
                        }
                        if (distance < 0)
                        {
                            totalCoefficient += 1;
                        }
                    }
                }
            }
        }


       /*

        System.out.println("-----------------------------------");
        java.util.List<DefaultEdge> edgeSetB = this.copyGraphB.edgeSet();

        for (DefaultEdge edge: edgeSetB)
        {
            System.out.println(edge.toString());
        }

        //System.out.println("-----------------------------------");

        /*i = new Point(0,1);
        while (this.vertexMapA.containsKey(i))
        {
            Point2D.Double vertex = this.vertexMapB.get(i);

            java.util.List<Point2D.Double> vertexSet = this.copyGraphB.neighborListOf(vertex);

        } */

        /*java.util.List<Point2D.Double> vertexSet = copyGraphB.vertexSet();

        for (Point2D.Double vertex: vertexSet)
        {
            System.out.println("vertex: " + vertex.toString());
            System.out.println("-n");
            Point value = vertexMapA.get(vertex);
            if (value != null)
            {
                java.util.List<Point2D.Double> neighbors = copyGraphB.neighborListOf(vertex);

                for (Point2D.Double tmp: neighbors)
                {
                    System.out.println(tmp.toString());
                    copyGraphB.findPath(vertex, tmp);
                    System.out.println("----");
                }
            }
        }   */

        return totalCoefficient;
    }


    /**
     * Calculates similarity coefficient of two graphs A->B
     * Using dividing graph into smaller pieces method
     *
     * @param option int option switch
     * @return totalCoefficient
     * @throws OGraph.EdgelessGraphException  something
     * @throws OGraph.WeightlessGraphException   something
     *
     * Added by Aivars Šāblis.
     */
    public double DoCompareA(int option) throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException
    {
        //long dividedPartNum = 1;
        double totalCoefficient = 0;
        double sequenceCoefficient = 0;


        java.util.List<DefaultEdge> edgeSetA = this.graphA.edgeSet();
        java.util.List<DefaultEdge> edgeSetB = this.graphB.edgeSet();

        if ((edgeSetA.size() == 0) || (edgeSetB.size() == 0))
        {
            assert false;
            //throw new OGraph.EdgelessGraphException();
        }

        //Gets total length of Graph A
        double aLength = graphA.getGraphLength();
        //Gets total length of Graph B
        double bLength = graphB.getGraphLength();


        //Total length of Graph A and Graph B divided by two
        double totalLength = (aLength + bLength);
        //double onePieceSize = totalLength / dividedPartNum;
        //onePieceSize = 1;

        this.copyGraphA = (OGraph) graphA.clone();
        this.copyGraphB = (OGraph) graphB.clone();


        //Calculate coefficient from GraphA to GraphB
        switch (option)
        {
            case 1:  totalCoefficient += IterThroughGraphEdgesSequence(this.copyGraphA, this.copyGraphB);
                     //check point sequence
                     sequenceCoefficient = checkSequence(this.copyGraphA, this.copyGraphB);
                     break;
            case 2:  totalCoefficient += IterThroughGraphEdgesDraw(this.copyGraphA, this.copyGraphB);
                     break;
            case 3:  totalCoefficient += IterThroughGraphEdges(this.copyGraphA, this.copyGraphB);
                     break;
            case 4:  totalCoefficient += IterThroughGraphEdgesSequenceDraw(this.copyGraphA, this.copyGraphB);
                     //check point sequence
                     sequenceCoefficient = checkSequenceDraw(this.copyGraphA, this.copyGraphB);
                     break;
            default: totalCoefficient += IterThroughGraphEdgesSequence(this.copyGraphA, this.copyGraphB);
                     break;
        }

        /*java.util.List<Point2D.Double> vertexSetB = this.copyGraphB.vertexSet();
        for (Point2D.Double vertex: vertexSetB)
        {
            System.out.println(vertex.toString());
        } */


        if (this.resultImg != null)
        {
            this.drawImage();
        }

        if (totalCoefficient != 0)
        {
            totalCoefficient = totalCoefficient / this.totalSteps / totalLength * 100;
        }
        System.out.println("totalCoefficient" + totalCoefficient + "sequenceCoefficient" + sequenceCoefficient);
        CleanUp();
        return totalCoefficient + (sequenceCoefficient / totalLength);
    }


    /**
     * Calculates similarity coefficient of two graphs B->A
     * Using dividing graph into smaller pieces method
     *
     * @param option int option switch
     * @return totalCoefficient
     * @throws OGraph.EdgelessGraphException  something
     * @throws OGraph.WeightlessGraphException   something
     */
    public double DoCompareB(int option) throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException
    {
        //long dividedPartNum = 1;
        double totalCoefficient = 0;
        double sequenceCoefficient = 0;

        java.util.List<DefaultEdge> edgeSetA = this.graphA.edgeSet();
        java.util.List<DefaultEdge> edgeSetB = this.graphB.edgeSet();

        if ((edgeSetA.size() == 0) || (edgeSetB.size() == 0))
        {
            assert false;
            //throw new OGraph.EdgelessGraphException();
        }


        //Gets total length of Graph A
        double aLength = graphA.getGraphLength();
        //Gets total length of Graph B
        double bLength = graphB.getGraphLength();


        //Total length of Graph A and Graph B divided by two
        double totalLength = (aLength + bLength);
        //dividedPartNum = round(log10(totalLength));
        //double onePieceSize = totalLength / dividedPartNum;
        //onePieceSize = 1;

        this.copyGraphA = (OGraph) graphA.clone();
        this.copyGraphB = (OGraph) graphB.clone();
        //Calculate coefficient from GraphA to GraphB
        this.xOffset = -this.xOffset;
        this.yOffset = -this.yOffset;


        //Calculate coefficient from GraphA to GraphB
        switch (option)
        {
            case 1:  totalCoefficient += IterThroughGraphEdgesSequence(this.copyGraphB, this.copyGraphA);
                     //check point sequence
                     sequenceCoefficient = checkSequence(this.copyGraphB, this.copyGraphA);
                     break;
            case 2:  totalCoefficient += IterThroughGraphEdgesDraw(this.copyGraphB, this.copyGraphA);
                     break;
            case 3:  totalCoefficient += IterThroughGraphEdges(this.copyGraphB, this.copyGraphA);
                     break;
            case 4:  totalCoefficient += IterThroughGraphEdgesSequenceDraw(this.copyGraphB, this.copyGraphA);
                     //check point sequence
                     sequenceCoefficient = checkSequenceDraw(this.copyGraphB, this.copyGraphA);
                     break;
            default: totalCoefficient += IterThroughGraphEdgesSequence(this.copyGraphB, this.copyGraphA);
                     break;
        }

        if (this.resultImg != null)
        {
            this.drawImage();
        }


        this.xOffset = -this.xOffset;
        this.yOffset = -this.yOffset;

        if (totalCoefficient != 0)
        {
            totalCoefficient = totalCoefficient / this.totalSteps / totalLength * 100;
        }
        //System.out.println("totalCoefficient" + totalCoefficient + "sequenceCoefficient" + sequenceCoefficient);

        CleanUp();
        return totalCoefficient + (sequenceCoefficient / totalLength);
    }


    /**
     * Cleans up some stuff after DoCompare methods
     *
     */
    private void CleanUp()
    {
        this.totalSteps = 0;
        if (vertexMapA != null)
        {
            this.vertexMapA.clear();
        }
        if (vertexMapB != null)
        {
            this.vertexMapB.clear();
        }
        if (this.coefMap != null)
        {
            this.coefMap.clear();
        }
        if (this.gridA != null)
        {
            this.gridA = null;
        }
        if (this.gridB != null)
        {
            this.gridB = null;
        }
    }

   /**
     * Calculates similarity coefficient of two graphs B->A
     * Using dividing graph into smaller pieces method
     * Consider only B graph length
     *
     * Added by Madara Augstkalne.
     *
     * @return totalCoefficient
     * @throws OGraph.EdgelessGraphException  something
     * @throws OGraph.WeightlessGraphException   something

     */
    public double DoCompareBvsA() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException
    {

        this.setOnePieceSize(1);

        double totalCoefficient = 0;
        this.totalSteps = 0;

        java.util.List<DefaultEdge> edgeSetA = this.graphA.edgeSet();
        java.util.List<DefaultEdge> edgeSetB = this.graphB.edgeSet();

        if ((edgeSetA.size() == 0) || (edgeSetB.size() == 0))
        {
            assert false;
        }
        //Gets total length of Graph B
        double bLength = graphB.getGraphLength();

        //Calculate coefficient from GraphA to GraphB
        this.xOffset = -this.xOffset;
        this.yOffset = -this.yOffset;

       // System.out.println(this.xOffset + "" + this.yOffset);

        totalCoefficient += IterThroughGraphEdges(this.graphB, this.graphA);


        this.xOffset = -this.xOffset;
        this.yOffset = -this.yOffset;


        if (totalCoefficient != 0)
        {
            totalCoefficient = totalCoefficient / this.totalSteps / (bLength*2) * 100;
        }

        CleanUp();
        return totalCoefficient;

    }



    public double DoCompareA(int option, int sourceWidth, int sourceHeight, int targetWidth, int targetHeight)
            throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException
    {
        //long dividedPartNum = 1;
        double totalCoefficient = 0;
        double sequenceCoefficient = 0;


        java.util.List<DefaultEdge> edgeSetA = this.graphA.edgeSet();
        java.util.List<DefaultEdge> edgeSetB = this.graphB.edgeSet();

        if ((edgeSetA.size() == 0) || (edgeSetB.size() == 0))
        {
            assert false;
            //throw new OGraph.EdgelessGraphException();
        }

        //Gets total length of Graph A
        double aLength = graphA.getGraphLength();
        //Gets total length of Graph B
        double bLength = graphB.getGraphLength();


        //Total length of Graph A and Graph B divided by two
        double totalLength = (aLength + bLength);
        //double onePieceSize = totalLength / dividedPartNum;
        //onePieceSize = 1;

        this.copyGraphA = (OGraph) graphA.clone();
        this.copyGraphB = (OGraph) graphB.clone();


        this.gridA = new OGrid(this.copyGraphA, sourceWidth, sourceHeight, 20);
        this.gridA.makeGrid();
        this.gridB = new OGrid(this.copyGraphB, targetWidth, targetHeight, 20);
        this.gridB.makeGrid();
        //double test = IterGrid(this.grid);


        //Calculate coefficient from GraphA to GraphB
        switch (option)
        {
            case 5:  totalCoefficient += IterGridA(this.gridA, this.gridB);
                     //check point sequence
                     sequenceCoefficient = checkSequence(this.copyGraphA, this.copyGraphB);
                     break;
            case 6:  totalCoefficient += IterGridA(this.gridA, this.gridB);
                     //check point sequence
                     sequenceCoefficient = checkSequenceDraw(this.copyGraphA, this.copyGraphB);
                     break;
        }


        if (this.resultImg != null)
        {
            this.drawImage();
        }

        if (totalCoefficient != 0)
        {
            totalCoefficient = totalCoefficient / this.totalSteps / totalLength * 100;
        }
        System.out.println("totalCoefficient" + totalCoefficient + "sequenceCoefficient" + sequenceCoefficient);
        CleanUp();
        return totalCoefficient + (sequenceCoefficient / totalLength);
    }


    public double DoCompareB(int option, int sourceWidth, int sourceHeight, int targetWidth, int targetHeight)
        throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException
    {
        //long dividedPartNum = 1;
        double totalCoefficient = 0;
        double sequenceCoefficient = 0;

        java.util.List<DefaultEdge> edgeSetA = this.graphA.edgeSet();
        java.util.List<DefaultEdge> edgeSetB = this.graphB.edgeSet();

        if ((edgeSetA.size() == 0) || (edgeSetB.size() == 0))
        {
            assert false;
            //throw new OGraph.EdgelessGraphException();
        }


        //Gets total length of Graph A
        double aLength = graphA.getGraphLength();
        //Gets total length of Graph B
        double bLength = graphB.getGraphLength();


        //Total length of Graph A and Graph B divided by two
        double totalLength = (aLength + bLength);
        //dividedPartNum = round(log10(totalLength));
        //double onePieceSize = totalLength / dividedPartNum;
        //onePieceSize = 1;

        this.copyGraphA = (OGraph) graphA.clone();
        this.copyGraphB = (OGraph) graphB.clone();
        //Calculate coefficient from GraphA to GraphB
        this.xOffset = -this.xOffset;
        this.yOffset = -this.yOffset;
        /*for (DefaultEdge edge: edgeSetB)
        {
            System.out.println(edge.toString());
        }*/

        this.gridA = new OGrid(this.copyGraphB, targetWidth, targetHeight, 50);
        this.gridA.makeGrid();
        this.gridB = new OGrid(this.copyGraphA, sourceWidth, sourceHeight, 50);
        this.gridB.makeGrid();
        //double test = IterGrid(this.grid);

        //Calculate coefficient from GraphA to GraphB
        switch (option)
        {
            case 5:  totalCoefficient += IterGridB(this.gridA, this.gridB);
                     //check point sequence
                     sequenceCoefficient = checkSequence(this.copyGraphB, this.copyGraphA);
                     break;
            case 6:  totalCoefficient += IterGridB(this.gridA, this.gridB);
                     //check point sequence
                     sequenceCoefficient = checkSequenceDraw(this.copyGraphB, this.copyGraphA);
                     break;
        }

        if (this.resultImg != null)
        {
            this.drawImage();
        }


        this.xOffset = -this.xOffset;
        this.yOffset = -this.yOffset;

        if (totalCoefficient != 0)
        {
            totalCoefficient = totalCoefficient / this.totalSteps / totalLength * 100;
        }
        //System.out.println("totalCoefficient" + totalCoefficient + "sequenceCoefficient" + sequenceCoefficient);

        CleanUp();
        return totalCoefficient + (sequenceCoefficient / totalLength);
    }


    public double IterGridA(OGrid gridA, OGrid gridB)
    {
        int gridWidth = gridA.gridWidth();
        int gridHeight = gridA.gridHeight();
        double totalCoefficient = 0;


        for (int i = 0; i < gridHeight; i++)
        {
            for (int j = 0; j < gridWidth; j++)
            {
               //System.out.println(i + ":" + j);
               totalCoefficient += IterThroughGraphEdgesSequence(this.copyGraphA, this.copyGraphB, j, i);
            }
        }


        return totalCoefficient;
    }

    public double IterGridB(OGrid gridA, OGrid gridB)
    {
        int gridWidth = gridA.gridWidth();
        int gridHeight = gridA.gridHeight();
        double totalCoefficient = 0;

        for (int i = 0; i < gridHeight; i++)
        {
            for (int j = 0; j < gridWidth; j++)
            {
               totalCoefficient += IterThroughGraphEdgesSequence(this.copyGraphB, this.copyGraphA, j, i);
            }
        }


        return totalCoefficient;
    }


    public double IterThroughGraphEdgesSequence(OGraph graphA, OGraph graphB, int i, int j)
    {
        double totalCoefficient = 0;
        Point2D C = new Point2D.Double();

        //System.out.println(i + ":::" + j);
        List<DefaultEdge> edgeSet = this.gridA.getPane(i,j);

        for (DefaultEdge edge: edgeSet)
        {
            Point2D.Double av1 = graphA.getEdgeSource(edge);
            Point2D.Double av2 = graphA.getEdgeTarget(edge);
            double edgeCoefficient = 0;
            double tmpCoefficient;

            //make parametric line equation from edge vertex coordinates
            double aX = av2.getX() - av1.getX();
            double aY = av2.getY() - av1.getY();

            int step = 0;
            double pointX0 = av1.getX();
            double pointY0 = av1.getY();
            double pointX = pointX0;
            double pointY = pointY0;



            C.setLocation(pointX0 + this.xOffset, pointY0 + this.yOffset);
            //vertexMapA.put(av1, new Point(this.totalSteps + step,1));



            //getting edge step length
            double stepLengthX = aX * this.onePieceSize / graphA.getEdgeLength(edge);
            double stepLengthY = aY * this.onePieceSize / graphA.getEdgeLength(edge);

            if (av1.getX() <= av2.getX() && av1.getY() <= av2.getY())
            {
                while ((pointX <= av2.getX()) && (pointY <= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB, i, j);
                    edgeCoefficient += tmpCoefficient;

                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                    if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {

                        graphA.addVertex(tmp);

                        DefaultEdge tmp1 = graphA.addEdge(av1, tmp);
                        if (tmp1 != null)
                        {
                            this.gridA.addLine(tmp1);
                        }
                        DefaultEdge tmp2 = graphA.addEdge(tmp, av2);
                        if (tmp2 != null)
                        {
                            this.gridA.addLine(tmp2);
                        }

                        this.gridA.removeLine(graphA.removeEdge(av1, av2));
                        av1 = tmp;
                    }

                    step = step + 1;
                    this.totalSteps++;



                   /* Point v1 = new Point();

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    Point v2 = new Point((int)round(pointX+this.xOffset/2), (int)round(pointY+this.yOffset/2));

                    this.drawGraphLine(v1, v2, tmpCoefficient);   */

                    Point2D.Double prevPoint = new Point2D.Double(pointX, pointY);

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    this.coefMap.add(new EdgeMap(prevPoint, new Point2D.Double(pointX, pointY), tmpCoefficient));

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() <= av2.getX() && av1.getY() >= av2.getY())
            {
                while ((pointX <= av2.getX()) && (pointY >= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB, i, j);
                    edgeCoefficient += tmpCoefficient;


                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                   if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {

                        graphA.addVertex(tmp);

                        DefaultEdge tmp1 = graphA.addEdge(av1, tmp);
                        if (tmp1 != null)
                        {
                            this.gridA.addLine(tmp1);
                        }
                        DefaultEdge tmp2 = graphA.addEdge(tmp, av2);
                        if (tmp2 != null)
                        {
                            this.gridA.addLine(tmp2);
                        }
                        this.gridA.removeLine(graphA.removeEdge(av1, av2));
                        av1 = tmp;
                    }

                    step = step + 1;
                    this.totalSteps++;

                    Point2D.Double prevPoint = new Point2D.Double(pointX, pointY);

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    this.coefMap.add(new EdgeMap(prevPoint, new Point2D.Double(pointX, pointY), tmpCoefficient));

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() >= av2.getX() && av1.getY() <= av2.getY())
            {
                while ((pointX >= av2.getX()) && (pointY <= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB, i, j);
                    edgeCoefficient += tmpCoefficient;


                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                   if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {

                        graphA.addVertex(tmp);

                        DefaultEdge tmp1 = graphA.addEdge(av1, tmp);
                        if (tmp1 != null)
                        {
                            this.gridA.addLine(tmp1);
                        }
                        DefaultEdge tmp2 = graphA.addEdge(tmp, av2);
                        if (tmp2 != null)
                        {
                            this.gridA.addLine(tmp2);
                        }
                        this.gridA.removeLine(graphA.removeEdge(av1, av2));
                        av1 = tmp;
                    }

                    step = step + 1;
                    this.totalSteps++;

                    Point2D.Double prevPoint = new Point2D.Double(pointX, pointY);

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    this.coefMap.add(new EdgeMap(prevPoint, new Point2D.Double(pointX, pointY), tmpCoefficient));

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }
            else if (av1.getX() >= av2.getX() && av1.getY() >= av2.getY())
            {
                while ((pointX >= av2.getX()) && (pointY >= av2.getY()))
                {
                    tmpCoefficient = this.getShortestDistanceSequence(C, graphB, i, j);


                    Point2D.Double tmp = new Point2D.Double(pointX, pointY);
                    vertexMapA.put(tmp, new Point(this.totalSteps,1));

                    if (!(tmp.equals(av1) ||  tmp.equals(av2)))
                    {
                        graphA.addVertex(tmp);

                        DefaultEdge tmp1 = graphA.addEdge(av1, tmp);
                        if (tmp1 != null)
                        {
                            this.gridA.addLine(tmp1);
                        }
                        DefaultEdge tmp2 = graphA.addEdge(tmp, av2);
                        if (tmp2 != null)
                        {
                            this.gridA.addLine(tmp2);
                        }
                        this.gridA.removeLine(graphA.removeEdge(av1, av2));
                        av1 = tmp;
                    }

                    edgeCoefficient += tmpCoefficient;
                    step = step + 1;
                    this.totalSteps++;

                    Point2D.Double prevPoint = new Point2D.Double(pointX, pointY);

                    pointX = pointX0 + step * stepLengthX;
                    pointY = pointY0 + step * stepLengthY;

                    this.coefMap.add(new EdgeMap(prevPoint, new Point2D.Double(pointX, pointY), tmpCoefficient));

                    C.setLocation(pointX + this.xOffset, pointY + this.yOffset);
                }
            }

            //this.totalSteps = this.totalSteps + step;
            totalCoefficient += edgeCoefficient;
        }


        return totalCoefficient;
    }

    private double getShortestDistanceSequence(Point2D C, OGraph graph, int x, int y)
    {

        double shortestDistance = Double.POSITIVE_INFINITY;
        double tmpDistance;
        Point2D.Double xPoint = new Point2D.Double();
        DefaultEdge shortestEdge = new DefaultEdge();
        int rad = 0;




        while (shortestDistance == Double.POSITIVE_INFINITY)
        {
            //System.out.println("test");
            java.util.List<DefaultEdge> edgeSet = makeFindArea(x, y, rad);
            rad++;
            int i = 0;
            while ((i < edgeSet.size()) && (shortestDistance != 0))
            {
               DefaultEdge edge = edgeSet.get(i);


               Point2D.Double v1 = graph.getEdgeSource(edge);
               Point2D.Double v2 = graph.getEdgeTarget(edge);

               //System.out.println("v1" + v1.toString() + "; v2:" + v2.toString() + "; C:" + C.toString());

               tmpDistance = getDistanceBetweenP(v1, v2, C);
               if (tmpDistance < shortestDistance)
               {
                   //System.out.println("segmentValue: " + segmentValue);
                   shortestDistance = tmpDistance;
                   //get point on B Graph
                   if (segmentValue >= 1)
                   {
                       xPoint.setLocation(v2.getX(), v2.getY());
                   }
                   else if (segmentValue <= 0)
                   {
                       xPoint.setLocation(v1.getX(), v1.getY());
                   }
                   else
                   {
                        xPoint.setLocation(v1.getX() + (v2.getX()-v1.getX())*segmentValue,v1.getY() + (v2.getY()-v1.getY())*segmentValue);
                   }
                   //System.out.println("xPoint: " + xPoint);
                   shortestEdge = edge;
               }

               i++;
            }

            edgeSet.clear();
        }


        //add shortest distance vector crosspoint on B graph
        vertexMapB.put(new Point(this.totalSteps,1), xPoint);


        if (graph.addVertex(xPoint))
        {

            //System.out.println(graph.toString() + "sdad:" + shortestEdge.toString());
            DefaultEdge tmp1 = graph.addEdge(graph.getEdgeSource(shortestEdge), xPoint);
            if (tmp1 != null)
            {
                this.gridB.addLine(tmp1);
            }
            DefaultEdge tmp2 = graph.addEdge(xPoint, graph.getEdgeTarget(shortestEdge));
            if (tmp2 != null)
            {
                this.gridB.addLine(tmp2);
            }
            graph.removeEdge(shortestEdge);
            this.gridB.removeLine(shortestEdge);
        }
        else
        {
            //System.out.println(xPoint);
        }


        //System.out.println("shortestDistance: " + shortestDistance);
        //System.out.println();

        return shortestDistance;
    }

    /**
     * Gets from grid edgeSet
     * @param x height
     * @param y width
     * @param rad radius
     * @return edgeSet
     */
    private List<DefaultEdge> makeFindArea(int x, int y, int rad)
    {
        //System.out.println(rad);
        java.util.List<DefaultEdge> edgeSet = new ArrayList<DefaultEdge>();

        for (int i = x-rad; i <= x+rad; i++)
        {
            for (int j = y-rad; j<= y+rad; j++)
            {
               edgeSet.addAll(this.gridB.getPane(i, j));
            }
        }

        return edgeSet;
    }

}