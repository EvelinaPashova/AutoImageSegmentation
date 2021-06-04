
package lv.lumii.pixelmaster.modules.ridge.domain;
import java.util.ArrayList; // ArrayList;
import java.awt.Point;      //Point



/*
 *
 * @author 
 */
//TEMP
// TO DO: papildināt, pielabot
public class Grid {
    private int width;
    private int height;
    private int gridWidth;
    private int gridHeight;
    private int paneSize;
    private ArrayList<ArrayList<LineSegment2D>> panes;  // gridHeight*gridWidth<Object>>
    private ArrayList<LineSegment2D> lineSegments;



    public Grid (int sourceWidth, int sourceHeight, int paneSize) {
        this.paneSize = paneSize;
        width = sourceWidth;
        height = sourceHeight;
        gridWidth = (int) Math.ceil((double)sourceWidth / paneSize);
        gridHeight = (int) Math.ceil((double)sourceHeight / paneSize);
        int size = gridWidth * gridHeight;

        lineSegments = new ArrayList<LineSegment2D>();
        panes = new ArrayList<ArrayList<LineSegment2D>>(size);
            for (int j = 0; j < size; j++) {
                panes.add(new ArrayList<LineSegment2D>());
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
        return lineSegments.size();
    }
    
    public ArrayList<LineSegment2D> getPane (int width, int height) {
        if (width > this.width || height > this.height || width < 0 || height < 0) {
            throw new IllegalArgumentException("IllegalArgument in Ridge.Grid.getPane");
        }
        ArrayList<LineSegment2D> paneLines = new ArrayList<LineSegment2D>();
         for (LineSegment2D key: panes.get(gridWidth * height + width)) {
             paneLines.add(key);
        }
        return paneLines;
    }

    public boolean inGridRange(LineSegment2D segment) {
        if ( inGridRange(segment.getStart())
          && inGridRange(segment.getEnd()) )
            return true;;
        return false;
    }

    public boolean inGridRange (Point point) {
        if (point.x > width || point.y > height || point.x < 0 || point.y < 0)
            return false;
        return true;
    }

//================================================================
    public boolean addLinePoints (LineSegment2D segment) {
        if (!inGridRange(segment)) {
            return false;
        }
        if (lineSegments.contains(segment)) {
            return false;
        }
        lineSegments.add(segment);
        int startX, startY, endX, endY;
        startX = segment.getStart().x / paneSize;
        startY = segment.getStart().y / paneSize;
        endX = segment.getEnd().x / paneSize;
        endY = segment.getEnd().y / paneSize;
        // ja nogriežņa sākumpunkts un beigu punkts ir vienā rūtī
        // tad to pievieno tikai vienu reizi
        if (startX == endX && startY == endY) {
            panes.get(gridWidth * startY + startX).add(segment);
            return true;
        }
        // pievieno abus punktus, ja tie katrs atrodas savā rūtī
        else {
           panes.get(gridWidth * startY + startX).add(segment);
           panes.get(gridWidth * endY + endX).add(segment);
        }
        return true;
    }
//===========================================================
    public boolean addLine (LineSegment2D segment) {
        if (!inGridRange(segment)) {
            return false;
        }
        if (lineSegments.contains(segment)) {
            return false;
        }
        lineSegments.add(segment);
        int startX, startY, endX, endY;
        if ((segment.getStart().y > segment.getEnd().y) ) {
            startX = segment.getEnd().x / paneSize;
            startY = segment.getEnd().y / paneSize;
            endX = segment.getStart().x / paneSize;
            endY = segment.getStart().y / paneSize;
        }
        else {
            startX = segment.getStart().x / paneSize;
            startY = segment.getStart().y / paneSize;
            endX = segment.getEnd().x / paneSize;
            endY = segment.getEnd().y / paneSize;
        }
        if (endX - startX == 0){
            int x = startX;
            for (int y = startY; y <= endY; y++) {
                panes.get(gridWidth * y + x).add(segment);
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
                    panes.get(gridWidth * y + x).add(segment);
                }
            }
            else {
                for (int y = startY; y <= endY; y++) {
                    int x = (int)((y - yInt)/ slope);
                    panes.get(gridWidth * y + x).add(segment);
                }
            }
        }
     return true;
    }

    public boolean removeLine (LineSegment2D segment) {
        if (lineSegments.contains(segment)) {
            lineSegments.remove(segment);
            panes.remove(segment);
            return true;
        }
        return false;
    }

}





























