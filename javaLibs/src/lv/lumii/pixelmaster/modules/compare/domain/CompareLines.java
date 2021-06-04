package lv.lumii.pixelmaster.modules.compare.domain;

import lv.lumii.pixelmaster.core.api.domain.RGB;
import java.util.Iterator;
import lv.lumii.pixelmaster.modules.spw.domain.graph.GraphEdge;
import lv.lumii.pixelmaster.modules.spw.domain.graph.UGraph;
import lv.lumii.pixelmaster.modules.spw.domain.graph.Vertex;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import java.awt.*;
import java.awt.geom.Point2D;

import static java.lang.Math.*;
import static java.lang.Math.abs;
import static lv.lumii.pixelmaster.modules.ridge.domain.Util.drawLine;

/**
 *
 * Compares two non-oriented graphs
 *
 * @author Aivars Šāblis
 *
 * double compare coefficient
 */
public class CompareLines {
    private UGraph graphA, graphB;
    private double xOffset, yOffset;
    private double totalSteps;
    private double onePieceSize;
    private RasterImage resultImg;


    /**
     * 	//Does not allow to create objects with default constructor to avoid errors.
     *
     *  Created by Aivars Šāblis 10.03.2010
     */
    public CompareLines()
    {
      //assert false;
      this.setOnePieceSize(1);
      this.totalSteps = 0;
    }


    /**
     * Constructor
     * @param graphA UGraph first graph
     * @param graphB UGraph second graph
     *
     * @throws graph.UGraph.EdgelessGraphException something
     * @throws graph.UGraph.WeightlessGraphException something
     *
     * Created by Aivars Šāblis 09.03.2010
     */
    public CompareLines(UGraph graphA, UGraph graphB) throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException {
        this.graphA = graphA;
        this.graphB = graphB;
        this.totalSteps = 0;

        this.calculateOffset();

        this.setOnePieceSize(1);
    }


    /**
     * Loads first graph
     *
     * @param graphA UGraph
     * @throws graph.UGraph.EdgelessGraphException something
     * @throws graph.UGraph.WeightlessGraphException something
     */
    public void LoadGraphA(UGraph graphA) throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException
    {
        this.graphA = graphA;
        if (this.graphB != null)
        {
             try
        {
            this.calculateOffset();
        }
        catch (UGraph.EdgelessGraphException e)
        {
            e.printStackTrace();
        }
        catch (UGraph.WeightlessGraphException e)
        {
            e.printStackTrace();
        }  
        }

    }


    /**
     * Loads second graph
     *
     * @param graphB  UGraph
     * @throws graph.UGraph.EdgelessGraphException something
     * @throws graph.UGraph.WeightlessGraphException something
     */
    public void LoadGraphB(UGraph graphB) throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException
    {
        this.graphB = graphB;

        if (this.graphA != null)
        {
             try
        {
            this.calculateOffset();
        }
        catch (UGraph.EdgelessGraphException e)
        {
            e.printStackTrace();
        }
        catch (UGraph.WeightlessGraphException e)
        {
            e.printStackTrace();
        }
        }
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


    /* Sets on step size of line segment, between two points on edge
     *
     * @param size double - one step size
     * Added by Aivars Šāblis 15.04.2010
     */
    public void setOnePieceSize(double size)
    {
        this.onePieceSize = size;
    }


    /**
     * Calculate offset between graphA and graphB
     *
     * @throws UGraph.EdgelessGraphException something
     * @throws UGraph.WeightlessGraphException something
     *
     * Added by Aivars Šāblis.
     */
    private void calculateOffset() throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException
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
        if (xOffset != -1)
        {
            this.xOffset = xOffset;
        }
        if (yOffset != -1)
        {
            this.yOffset = yOffset;
	    }
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
        int colorWeight = 15*(int)round(segmentCoefficient);

        if (colorWeight > 255)
        {
            colorWeight = 255;
        }

        int color = RGB.getRGB(colorWeight,0,255-colorWeight);

        drawLine(this.resultImg, C1, C2, color);
    }

    
    /**
     *  Compute the dot product AB ⋅ BC
     *  If this value is greater than 0, it means that the angle between AB and BC is between -90 and 90,
     *  and therefore the nearest point on the segment AB will be B
     *  @param A Vertex first vertex of edge
     *  @param B Vertex second vertex of edge
     *  @param C Point2D representing point from where distance is found
     *
     *  @return dot product
     * 
     * Added by Madara Augstkalne. Modified by Aivars Šāblis
     */
    private double getDot(Vertex A, Vertex B, Point2D C)
    {
        double vectorABx = B.getX() - A.getX();
        double vectorABy = B.getY() - A.getY();

        double vectorBCx = C.getX() - B.getX();
        double vectorBCy = C.getY() - B.getY();

        double dot;
        dot = (vectorABx * vectorBCx) + (vectorABy * vectorBCy);

        return dot;
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
    private double getCross(Vertex A, Vertex B, Point2D C)
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
     *
     * Gets distance between vertex C of graph A and edge of graph B
     * First check if nearest point is one of the endpoints A or B.
     * If both dot products are negative, then the nearest point to C is somewhere along the segment.
     * @param  A Vertex first vertex of edge
     * @param  B Vertex second vertex of edge
     * @param  C Point2D representing point from where distance is found
     * @return distance between vertex and edge
     * Added by Madara Augstkalne. Modified by Aivars Šāblis.
     */
    private double getDistanceBetween(Vertex A, Vertex B, Point2D C)
    {
        double distance = 0;

        //System.out.println("isRight" + isRight + " isLeft" + isLeft);

        //Find whenever shortest distance to line segment is one of the endpoints
        double isRight = getDot(A, B, C);
        if(isRight > 0)
        {
            return getDistance(B.getX(), B.getY(), C.getX(), C.getY());
        }
        double isLeft = getDot(B, A, C);
        if(isLeft > 0)
        {
            return getDistance(A.getX(), A.getY(), C.getX(), C.getY());
        }
        // Else shortest distance is somewhere along the segment
        // Find distance between point and line. This gives the distance as (AB x AC)/|AB|
        if (isLeft <= 0 && isRight <= 0)
        {
            distance = getCross(A,B,C) / getDistance(A.getX(), A.getY(), B.getX(), B.getY());
        }

        //System.out.println("distance" + distance);
        return abs(distance);
    }

    /*
     *
     * @param  A Vertex first vertex of edge
     * @param  B Vertex second vertex of edge
     * @param  C Point2D representing point from where distance is found
     * @return distance between vertex and edge
     * Added by Aivars Šāblis 13.04.2010.
     */
    private double getDistanceBetweenP(Vertex A, Vertex B, Point2D C)
    {
        double distance;

        //System.out.println("Edge: " + A.getX() + "," + A.getY() + ":" + B.getX() + "," + B.getY() + "\n");
        //System.out.println("Point: " + C.getX() + "," + C.getY() + "\n");
        double BAx = B.getX() - A.getX();
        double BAy = B.getY() - A.getY();

        double inSegment = (((C.getX() - A.getX()) * (BAx)) + ((C.getY() - A.getY()) * (BAy))) /
                (BAx * BAx + BAy * BAy);

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
     * Added by Aivars Šāblis.
     */
    private double getShortestDistance(Point2D C, UGraph graph)
    {
        Iterator<GraphEdge> i = graph.edges();

        double shortestDistance = Double.POSITIVE_INFINITY;
        double tmpDistance;

        while (i.hasNext() && (shortestDistance != 0))
        {
            GraphEdge edge = i.next();
            Vertex av1 = edge.vertexFirst();
            Vertex av2 = edge.vertexSecond();


            tmpDistance = getDistanceBetweenP(av1, av2, C);
            if (tmpDistance < shortestDistance)
            {
                shortestDistance = tmpDistance;
            }
        }

        //System.out.println("shortestDistance: " + shortestDistance);
        //System.out.println();
        return shortestDistance;
    }


    /**
     * Iterating through graph to find distance to second graph
     *
     * @param graphA UGraph Graph from who is finding distance
     * @param graphB UGraph Graph
     * @return coefficient double
     *
     * Added by Aivars Šāblis.
     */
    private double IterThroughGraphEdges(UGraph graphA, UGraph graphB)
    {
        GraphEdge edge;
        double totalCoefficient = 0;
        Point2D C = new Point2D.Double();

        Iterator<GraphEdge> i = graphA.edges();
        while (i.hasNext())
        {
            edge = i.next();
            Vertex av1 = edge.vertexFirst();
            Vertex av2 = edge.vertexSecond();
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
            double stepLengthX = aX * this.onePieceSize / edge.getLength();
            double stepLengthY = aY * this.onePieceSize / edge.getLength();

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



    private double IterThroughGraphEdgesDraw(UGraph graphA, UGraph graphB)
    {
        GraphEdge edge;
        double totalCoefficient = 0;
        Point2D C = new Point2D.Double();

        Iterator<GraphEdge> i = graphA.edges();
        while (i.hasNext())
        {
            edge = i.next();
            Vertex av1 = edge.vertexFirst();
            Vertex av2 = edge.vertexSecond();
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
            double stepLengthX = aX * this.onePieceSize / edge.getLength();
            double stepLengthY = aY * this.onePieceSize / edge.getLength();

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
     * Calculates similarity coefficient of two graphs A->B
     * Using dividing graph into smaller pieces method
     *
     * @param option int option switch
     * @return totalCoefficient
     * @throws UGraph.EdgelessGraphException  something
     * @throws UGraph.WeightlessGraphException   something
     *
     * Added by Aivars Šāblis.
     */

    public double DoCompareA(int option) throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException
    {
        //long dividedPartNum = 1;
        double totalCoefficient = 0;
        this.totalSteps = 0;

        Iterator<GraphEdge> a = this.graphA.edges();
        Iterator<GraphEdge> b = this.graphB.edges();

        if ( !a.hasNext() || !b.hasNext())
        {
            assert false;
            //throw new UGraph.EdgelessGraphException();
        }


        //Gets total length of Graph A
        double aLength = graphA.getGraphLength();
        //Gets total length of Graph B
        double bLength = graphB.getGraphLength();


        //Total length of Graph A and Graph B divided by two
        double totalLength = (aLength + bLength);
        //double onePieceSize = totalLength / dividedPartNum;
        //onePieceSize = 1;


        //Calculate coefficient from GraphA to GraphB
        //System.out.println("DCA");
        switch (option)
        {
            case 1:  totalCoefficient += IterThroughGraphEdges(this.graphA, this.graphB);
                     break;
            case 2:  totalCoefficient += IterThroughGraphEdgesDraw(this.graphA, this.graphB);
                     break;
            default: totalCoefficient += IterThroughGraphEdges(this.graphA, this.graphB);
                     break;
        }

        if (totalCoefficient == 0)
        {
            return totalCoefficient;
        }
        else
        {
            return totalCoefficient / this.totalSteps / totalLength * 100;
        }
    }

    /**
     * Calculates similarity coefficient of two graphs B->A
     * Using dividing graph into smaller pieces method
     *
     * @param option int option switch
     * @return totalCoefficient
     * @throws UGraph.EdgelessGraphException  something
     * @throws UGraph.WeightlessGraphException   something
     *
     * Added by Aivars Šāblis.
     */
    public double DoCompareB(int option) throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException
    {
        //long dividedPartNum = 1;
        double totalCoefficient = 0;
        this.totalSteps = 0;

        Iterator<GraphEdge> a = this.graphA.edges();
        Iterator<GraphEdge> b = this.graphB.edges();

        if ( !a.hasNext() || !b.hasNext())
        {
            assert false;
            //throw new UGraph.EdgelessGraphException();
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


        //Calculate coefficient from GraphA to GraphB
        this.xOffset = -this.xOffset;
        this.yOffset = -this.yOffset;
        //System.out.println("DCB");
        switch (option)
        {
            case 1:  totalCoefficient += IterThroughGraphEdges(this.graphB, this.graphA);
                     break;
            case 2:  totalCoefficient += IterThroughGraphEdgesDraw(this.graphB, this.graphA);
                     break;
            default: totalCoefficient += IterThroughGraphEdges(this.graphB, this.graphA);
                     break;
        }


        this.xOffset = -this.xOffset;
        this.yOffset = -this.yOffset;

        
        if (totalCoefficient == 0)
        {
            return totalCoefficient;
        }
        else
        {
            return totalCoefficient / this.totalSteps / totalLength * 100;
        }
    }

    /**
        * Calculates similarity coefficient of two graphs B->A
        * Using dividing graph into smaller pieces method
        * Consider only B graph length
        *
        * @return totalCoefficient
        * @throws UGraph.EdgelessGraphException  something
        * @throws UGraph.WeightlessGraphException   something
        *
        * Modified by Madara Augstkalne.
        */
       public double DoCompareBvsA() throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException
       {
           //long dividedPartNum = 1;
           double totalCoefficient = 0;
           this.totalSteps = 0;

           Iterator<GraphEdge> a = this.graphA.edges();
           Iterator<GraphEdge> b = this.graphB.edges();

           if ( !a.hasNext() || !b.hasNext())
           {
               assert false;
               //throw new UGraph.EdgelessGraphException();
           }

           //Gets total length of Graph B
           double bLength = graphB.getGraphLength();

           //Calculate coefficient from GraphA to GraphB
           this.xOffset = -this.xOffset;
           this.yOffset = -this.yOffset;
           //System.out.println("DCB");
           
           totalCoefficient += IterThroughGraphEdges(this.graphB, this.graphA);


           this.xOffset = -this.xOffset;
           this.yOffset = -this.yOffset;


           if (totalCoefficient == 0)
           {
               return totalCoefficient;
           }
           else
           {
               return totalCoefficient / this.totalSteps / (bLength*2) * 100;
           }
       }
    


    /* Not yet working

     */
    public double GetDistanceViaSequence(UGraph graphA, UGraph graphB, double onePieceSize)
    {
        return 0;
    }

    /* Not yet working

     */
    public double DoCompareViaSequence() throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException
    {
        int dividedPartNum = 100;
        double totalCoefficient = 0;

        Iterator<GraphEdge> a = this.graphA.edges();
        Iterator<GraphEdge> b = this.graphB.edges();

        if (!a.hasNext() || !b.hasNext())
        {
            assert false;
        }

        //Gets total length of Graph A
        double aLength = graphA.getGraphLength();
        //Gets total length of Graph B
        double bLength = graphB.getGraphLength();


        //Total length of Graph A and Graph B divided by two
        double totalLength = (aLength + bLength);
        //System.out.println("totalLength" + totalLength);
        double onePieceSize = totalLength / dividedPartNum;


        //Calculate coefficient from GraphA to GraphB
        totalCoefficient += GetDistanceViaSequence(this.graphA, this.graphB, onePieceSize);
        totalCoefficient += GetDistanceViaSequence(this.graphB, this.graphA, onePieceSize);

        //System.out.println("totalCoefficient" + totalCoefficient);
        return totalCoefficient / totalLength;
    }
}