
package lv.lumii.pixelmaster.modules.compare.domain;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.Point2D;

import lv.lumii.pixelmaster.core.api.domain.RGB;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraph;
import lv.lumii.pixelmaster.modules.ridge.domain.*;

/**
 * This class contains methods that were previously in the class ImageProcessor.
 */
public class Util {

    /*
     * Modified printLine method, lines ar added to OGraph, not drawing on image
     *
     * @return OGraph
     */
    public static OGraph printLineToGraph (RasterImage inputImage, int act) {
		assert !(inputImage == null || act < 1 || act > 12);
        Ridge ridge = new Ridge();
        OGraph graph = new OGraph();
        Image ridgeI = Image.convertFromRI(inputImage);
        // PARAM TO PASS
        int smoothingRadius = 2;
        double nonCircularity = 1.2;
        int upperThreshold = 5;
        int lowerThreshold = 2;
        // max attālums starp pixeli un nogriezni, lai tas piederētu nogrieznim
        double maxGetLinesDistance = 1;
        // maksimālais attālums kādā meklē blakus esošās nogriežņu galapunktus, lai nogriežņus mēģinātu savienot
        double concatRadius = 3;
        // max punkta attālums līdz nogrieznim, lai tas piederētu nogrieznim (nogriežņu apvienošanas gadījumā)
        double maxConcatDistance = 1;
        // maksimālais attālums starp diviem nogriežņiem, lai tie tiktu ieskaitīti vienā nogriežņu kopā
        double maxClusterLineDistance = 4;
        // maksimālā attiecība starp nogriežņu veidoto kopu(garākā dimensija) un tās struktūras mērogojuma izmēru
        double maxSymbolScale = 1.5;
        //ja jointSegment = false, tad, meklējot nogriežņus, nākošā nogriežņa sākumpunkts būs nākošais punkts
        // aiz iepriekšējā nogriežņa beigu punkta (nosacijums protams darbojas tikai vienas līnijas ietvaros)
        boolean jointSegments = true;
        RidgeSettings sett
                = new RidgeSettings( smoothingRadius, nonCircularity, upperThreshold, lowerThreshold,
                    maxGetLinesDistance,  maxConcatDistance, concatRadius,
                    maxClusterLineDistance, maxSymbolScale, jointSegments);
        ridge.setSettings(sett);


		if (act == 1 || act == 4 || act == 7 || act == 10)
            ridge.detectRidges(ridgeI, sett);
        else if (act == 2 || act == 5 || act == 8 || act == 11)
            ridge.detectValleys(ridgeI, sett);
        else if (act == 3 || act == 6 || act == 9 || act == 12)
            ridge.detectEdges(ridgeI, sett);

//        RasterImage outputImage = new RasterImage(inputImage.getWidth(), inputImage.getHeight());
        //WriteTextFile fileWrite = new WriteTextFile();
        if (act < 4) {
            List<Edge> edgeList = new ArrayList<Edge>();
            edgeList = ridge.traceLines();

            for (Edge edgeItem: edgeList) {
                int color = RGB.getRandomColor();

                Point tmpStartPoint = null;
                Point tmpEndPoint = null;
                int i = 0;
                for (Point pointItem: edgeItem.points) {
                    //outputImage.setRGB(outputImage.getWidth() * pointItem.y + pointItem.x, color);
                    if (i==0)
                    {
                        tmpStartPoint = pointItem;
                    }
                    tmpEndPoint = pointItem;
                    i++;
                }
                Point2D.Double sourceVertex = new Point2D.Double(tmpStartPoint.x, tmpStartPoint.y);
                graph.addVertex(sourceVertex);

                Point2D.Double targetVertex = new Point2D.Double(tmpEndPoint.x, tmpEndPoint.y);
                graph.addVertex(targetVertex);

                graph.addEdge(sourceVertex, targetVertex);


            }
            return graph;
        }

        List<LineSegment2D> lineSegments = new ArrayList<LineSegment2D>();
        lineSegments = ridge.getLineSegments(maxGetLinesDistance, jointSegments);
        if (act > 6) {
            ridge.concatSegments(lineSegments, concatRadius, maxConcatDistance);
        }
        if (act < 10) {
            for (LineSegment2D lineItem: lineSegments) {
//                drawLine(outputImage, lineItem.getStart(), lineItem.getEnd(), RGB.getRandomColor());
                Point2D.Double sourceVertex = new Point2D.Double(lineItem.getStart().getX(), lineItem.getStart().getY());
                graph.addVertex(sourceVertex);

                Point2D.Double targetVertex = new Point2D.Double(lineItem.getEnd().getX(), lineItem.getEnd().getY());
                graph.addVertex(targetVertex);

                graph.addEdge(sourceVertex, targetVertex);
                //System.out.println("x:" + lineItem.getStart() + " y:" + lineItem.getEnd());
                //fileWrite.writeInFile("x:" + lineItem.getStart() + " y:" + lineItem.getEnd() + "\n");
            }
            //fileWrite.closeFile();
			return graph;
        }
        else {
            ArrayList<LineSet> lineClusters = null;
            lineClusters = ridge.lineGroup(lineSegments, maxClusterLineDistance);

            // print symbols to console
            for (LineSet set: lineClusters) {
                Symbol symb = new Symbol();
                if (symb.toSymbol(set.getLines(), maxSymbolScale)) {
                    SymbolInfo pr = new SymbolInfo(symb);
                    System.out.println(pr.stringTo());
                }
            }
            // draw symbols
            for (LineSet set: lineClusters) {
                int color = RGB.getRandomColor();
                for (LineSegment2D lineItem: set.getLines()) {
//                    drawLine(outputImage, lineItem.getStart(), lineItem.getEnd(), color);
                    Point2D.Double sourceVertex = new Point2D.Double(lineItem.getStart().getX(), lineItem.getStart().getY());
                    graph.addVertex(sourceVertex);

                    Point2D.Double targetVertex = new Point2D.Double(lineItem.getEnd().getX(), lineItem.getEnd().getY());
                    graph.addVertex(targetVertex);

                    graph.addEdge(sourceVertex, targetVertex);
                    //System.out.println("x:" + lineItem.getStart() + " y:" + lineItem.getEnd());
                    //fileWrite.writeInFile("x:" + lineItem.getStart() + " y:" + lineItem.getEnd() + "\n");
                }
            }
            //fileWrite.closeFile();
            return graph;
        }
	   }
}
