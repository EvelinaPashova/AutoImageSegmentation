package lv.lumii.pixelmaster.modules.compare.domain.graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

/**
 *
 */
public class OGrid
{
    private int width;
    private int height;
    private int gridWidth;
    private int gridHeight;
    private int paneSize;
    private ArrayList<List<DefaultEdge>> panes;  // gridHeight*gridWidth<Object>>
    private List<DefaultEdge> edgeSet;
    private OGraph graph;


    public OGrid (OGraph graph, int sourceWidth, int sourceHeight, int paneSize)
    {
        this.graph = graph;
        this.paneSize = paneSize;
        width = sourceWidth;
        height = sourceHeight;
        gridWidth = (int) Math.ceil((double)sourceWidth / paneSize);
        gridHeight = (int) Math.ceil((double)sourceHeight / paneSize);
        int size = gridWidth * gridHeight;

        edgeSet = new ArrayList<DefaultEdge>();
        panes = new ArrayList<List<DefaultEdge>>(size);

        for (int j = 0; j < size; j++) {
            panes.add(new ArrayList<DefaultEdge>());
        }
    }

    public void makeGrid()
    {
        List<DefaultEdge> edgeSet = graph.edgeSet();
        for (DefaultEdge edge: edgeSet)
        {
            addLine(edge);
        }
    }

    public int gridWidth() {
        return gridWidth;
    }

    public int gridHeight() {
        return gridHeight;
    }

    public int paneSize() {
        return paneSize;
    }

    public int numOfLines() {
        return edgeSet.size();
    }

    public List<DefaultEdge> getPane (int width, int height) {
        if (width >= this.gridWidth || height >= this.gridHeight || width < 0 || height < 0) {
            return new ArrayList<DefaultEdge>();
            //throw new IllegalArgumentException("IllegalArgument in Ridge.Grid.getPane");
        }
        List<DefaultEdge> paneLines = new ArrayList<DefaultEdge>();

        for (DefaultEdge key: panes.get(gridWidth * height + width)) {
             paneLines.add(key);
        }
        return paneLines;
    }

    public boolean inGridRange(DefaultEdge edge) {
        if ( inGridRange(graph.getEdgeSource(edge))
          && inGridRange(graph.getEdgeTarget(edge)) )
            return true;
        return false;
    }

    public boolean inGridRange (Point2D.Double point) {
        if (point.x > width || point.y > height || point.x < 0 || point.y < 0)
            return false;
        return true;
    }

//================================================================
    public boolean addLinePoints (DefaultEdge edge) {
        if (!inGridRange(edge)) {
            return false;
        }
        if (edgeSet.contains(edge)) {
            return false;
        }
        edgeSet.add(edge);
        double startX, startY, endX, endY;
        startX = graph.getEdgeSource(edge).getX() / paneSize;
        startY = graph.getEdgeSource(edge).getY() / paneSize;
        endX = graph.getEdgeTarget(edge).getX() / paneSize;
        endY = graph.getEdgeTarget(edge).getY() / paneSize;

        // ja nogriežņa sākumpunkts un beigu punkts ir vienā rūtī
        // tad to pievieno tikai vienu reizi
        if (startX == endX && startY == endY) {
            panes.get((int)round(gridWidth * startY + startX)).add(edge);
            return true;
        }
        // pievieno abus punktus, ja tie katrs atrodas savā rūtī
        else {
           panes.get((int)round(gridWidth * startY + startX)).add(edge);
           panes.get((int)round(gridWidth * endY + endX)).add(edge);
        }
        return true;
    }
    
//===========================================================
    public boolean addLine (DefaultEdge edge) {
        if (!inGridRange(edge)) {
            return false;
        }
        if (edgeSet.contains(edge)) {
            return false;
        }
        edgeSet.add(edge);
        int startX, startY, endX, endY;
        if ((graph.getEdgeSource(edge).getY() > graph.getEdgeTarget(edge).getY()) ) {
            startX = (int)round(graph.getEdgeTarget(edge).getX()) / paneSize;
            startY = (int)round(graph.getEdgeTarget(edge).getY()) / paneSize;
            endX = (int)round(graph.getEdgeSource(edge).getX()) / paneSize;
            endY = (int)round(graph.getEdgeSource(edge).getY()) / paneSize;
        }
        else {
             startX = (int)round(graph.getEdgeSource(edge).getX()) / paneSize;
             startY = (int)round(graph.getEdgeSource(edge).getY()) / paneSize;
             endX = (int)round(graph.getEdgeTarget(edge).getX()) / paneSize;
             endY = (int)round(graph.getEdgeTarget(edge).getY()) / paneSize;
        }
        if (endX - startX == 0){
            int x = startX;
            for (int y = startY; y <= endY; y++) {
                panes.get(gridWidth * y + x).add(edge);
            }
        }
        else {
            double slope = (double) (endY - startY) / (endX - startX);
            double yInt = startY - slope * startX;
            if (slope < 1 && slope > -1) {
                if (startX > endX) {
                    int temp = startX; startX = endX; endX = temp;
                    temp = startY; startY = endY; endY = temp;
                }
                for (int x = startX; x <= endX; x++) {
                    int y = (int)(x * slope + yInt);
                    panes.get(gridWidth * y + x).add(edge);
                }
            }
            else {
                for (int y = startY; y <= endY; y++) {
                    int x = (int)((y - yInt)/ slope);
                    panes.get(gridWidth * y + x).add(edge);
                }
            }
        }
     return true;
    }

    /*public boolean removeLine (DefaultEdge edge) {
        if (edgeSet.contains(edge)) {
            edgeSet.remove(edge);
            panes.remove(edge);
            return true;
        }
        return false;
    }   */

     public boolean removeLine (DefaultEdge edge) {
        if (!inGridRange(edge)) {
            return false;
        }
        if (edgeSet.contains(edge)) {
            edgeSet.remove(edge);
            //return false;
        }
        //edgeSet.add(edge);
        int startX, startY, endX, endY;
        if ((graph.getEdgeSource(edge).getY() > graph.getEdgeTarget(edge).getY()) ) {
            startX = (int)round(graph.getEdgeTarget(edge).getX()) / paneSize;
            startY = (int)round(graph.getEdgeTarget(edge).getY()) / paneSize;
            endX = (int)round(graph.getEdgeSource(edge).getX()) / paneSize;
            endY = (int)round(graph.getEdgeSource(edge).getY()) / paneSize;
        }
        else {
             startX = (int)round(graph.getEdgeSource(edge).getX()) / paneSize;
             startY = (int)round(graph.getEdgeSource(edge).getY()) / paneSize;
             endX = (int)round(graph.getEdgeTarget(edge).getX()) / paneSize;
             endY = (int)round(graph.getEdgeTarget(edge).getY()) / paneSize;
        }
        if (endX - startX == 0){
            int x = startX;
            for (int y = startY; y <= endY; y++) {
                panes.get(gridWidth * y + x).remove(edge);
                //System.out.println("test");
            }
        }
        else {
            double slope = (double) (endY - startY) / (endX - startX);
            double yInt = startY - slope * startX;
            if (slope < 1 && slope > -1) {
                if (startX > endX) {
                    int temp = startX; startX = endX; endX = temp;
                    temp = startY; startY = endY; endY = temp;
                }
                for (int x = startX; x <= endX; x++) {
                    int y = (int)(x * slope + yInt);
                    panes.get(gridWidth * y + x).remove(edge);
                    //System.out.println("test1");
                }
            }
            else {
                for (int y = startY; y <= endY; y++) {
                    int x = (int)((y - yInt)/ slope);
                    panes.get(gridWidth * y + x).remove(edge);
                    //System.out.println("test2");
                }
            }
        }
     return true;
    }
}

