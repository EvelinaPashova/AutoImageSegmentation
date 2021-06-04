

package lv.lumii.pixelmaster.modules.ridge.domain;

import java.util.UUID;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author
 */
public class Symbol {
    private String name;
    private long id;
    private Point coord;
    private double scale;
    private int[][] grid;
    private static final int DIMENSION = 13;


    public Symbol() {
        grid = new int[DIMENSION][DIMENSION];
        id = UUID.randomUUID().getMostSignificantBits();
        coord = new Point();
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public Point getCoord() {
        return coord;
    }

    public int dimension() {
        return DIMENSION;
    }

    public double getScale() {
        return scale;
    }

    public int[][] getGrid() {
        return grid;
    }

    public boolean setId (long id) {
        if (this.id != id) {
            this.id = id;
            return true;
        }
        return false;
    }

    public boolean setName(String name) {
        if (this.name.endsWith(name)) {
            this.name = name;
            return true;
        }
        return false;
    }

    public boolean set(int width, int height, int value) {
        if (width > DIMENSION || height > DIMENSION
                || width < 0 || height < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        grid[width][height] = value;
        return true;
    }
    
    private boolean addLine (LineSegment2D segment, int minX, int minY, double tmpScale) {
        int startX, startY, endX, endY;
        if ((segment.getStart().y > segment.getEnd().y) ) {
            startX = (int)(tmpScale * (segment.getEnd().x - minX));
            startY = (int)(tmpScale * (segment.getEnd().y - minY));
            endX = (int)(tmpScale * (segment.getStart().x - minX));
            endY = (int)(tmpScale * (segment.getStart().y - minY));
        }
        else {
            startX = (int)(tmpScale * (segment.getStart().x - minX));
            startY = (int)(tmpScale * (segment.getStart().y - minY));
            endX = (int)(tmpScale * (segment.getEnd().x - minX));
            endY = (int)(tmpScale * (segment.getEnd().y - minY));
        }
        if (endX - startX == 0){
            int x = startX;
            for (int y = startY; y <= endY; y++) {
                grid[x][y] = 1;
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
                    grid[x][y] = 1;
                }
            }
            else {
                for (int y = startY; y <= endY; y++) {
                    int x = (int)((y - yInt)/ slope);
                    grid[x][y] = 1;
                }
            }
        }
     return true;
    }

    public boolean toSymbol(ArrayList<LineSegment2D> lines, double maxScale) {
        if (lines == null)
            throw new NullPointerException();
        if (maxScale <=0 ) {
            throw new IllegalArgumentException();
        }
        LineSegment2D first = lines.get(0);
        int maxX = first.getStart().x;
        int minX = maxX;
        int maxY = first.getStart().y;
        int minY = maxY;

        for (LineSegment2D line: lines) {
            Point start = line.getStart();
            Point end = line.getEnd();
            minX = Math.min(minX, Math.min(start.x, end.x));
            minY = Math.min(minY, Math.min(start.y, end.y));
            maxX = Math.max(maxX, Math.max(start.x, end.x));
            maxY = Math.max(maxY, Math.max(start.y, end.y));
        }
        int difX = maxX - minX;
        int difY = maxY - minY;
        coord.move((minX + difX / 2), (minY + difY / 2));
        double tmpScale;
        if (difX > difY) {
            tmpScale =  (double) (DIMENSION - 1) / difX;
        }
        else {
            tmpScale =  (double) (DIMENSION - 1) / difY;
        }
        if ( tmpScale > maxScale) {
            return false;
        }
        //ADD LINES
        for (LineSegment2D line: lines) {
            addLine (line, minX, minY, tmpScale);
        }
        scale = tmpScale;
        return true;

    }

    public int[] lineSum () {
        int[] result = new int[6 * DIMENSION - 2];
        for(int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j ++) {
            result[j + DIMENSION] += grid[i][j];
            result[i] += grid[i][j];
            result[2 * DIMENSION + i + j] += grid[i][j];
            result[5 * DIMENSION  + i - j - 2] += grid[i][j];
            }
        }               
        return result;
    }

    public String toString() {
        int[] lineSum = lineSum();
        int sumSize = 6 * DIMENSION - 2;
        int sum = 0;
        String result = "";
        result = "ID: " + id
                       + " ,DIMENSION: " + DIMENSION
                       + " ,Name: " + name +"\n";
        result += "Symbol centre: " + coord + "\nLine Summ: ";
        for (int i = 0; i < sumSize; i++) {
          result += lineSum[i] + " ";
          sum += lineSum[i];
        }
        result += "\nSum: " + sum + "\n";
        String ch;
        for (int y = 0; y < DIMENSION; y++) {
            result += "\n";
            for (int x = 0; x < DIMENSION; x++) {
                result += grid[x][y] + " ";
            }
        }
        return (result + "\n");
    }

}




















