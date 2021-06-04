
package lv.lumii.pixelmaster.modules.ridge.domain;

import java.awt.Point;
import java.util.UUID;
/**
 *
 * @author 
 */
public class LineSegment2D implements Cloneable{
    private long id;
    private Point start;
    private Point end;
    private boolean haveSlope;
    private double slope;
    private double yIntercept; // y-intercept value;

    public LineSegment2D (int startX, int startY, int endX, int endY) {
        id = UUID.randomUUID().getMostSignificantBits();
        start = new Point(startX, startY);
        end = new Point(endX, endY);
        haveSlope = setValues();
    }

    public LineSegment2D(Point start, Point end) {
        id = UUID.randomUUID().getMostSignificantBits();
        this.start = start;
        this.end = end;
        haveSlope = setValues();
    }

    public LineSegment2D() {
        id = UUID.randomUUID().getMostSignificantBits();
        start = new Point();
        end = new Point();
        haveSlope = false;
    }

    private boolean setValues() {
        if (end.x - start.x == 0) {
           if (start.x == 0) yIntercept = 0; // not used yet;
           return false;
        }
        else {
            slope = (double) (end.y - start.y) / (end.x - start.x);
            yIntercept = start.y - slope * start.x;
        }
        return true;
    }

    public boolean setID (long id) {
        this.id = id;
        return true;
    }

    @Override
    public int hashCode() {
        long hash;
        hash = start.hashCode() + end.hashCode();
        return (int)hash;
    }

    @Override
	public boolean equals(Object aThat) {
        if (this == aThat) return true;
		if (!(aThat instanceof LineSegment2D)) {
			return super.equals(aThat);
		}
        LineSegment2D that = (LineSegment2D) aThat;
        if (this.getStart().equals(that.getStart()) && this.getEnd().equals(that.getEnd()))
            return true;
        else
            return false;
    }

    public boolean haveSlope() {
        return haveSlope;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public long getID() {
        return id;
    }
    
    public double getSlope () {
        if (!haveSlope) {
            throw new NullPointerException("Variable slope not set. Use boolean haveSlope()");
        }
        return slope;
    }

    public double getYintercept() {
        if (!haveSlope) {
            throw new NullPointerException("Variable not set. Use boolean haveSlope()");
        }
        return yIntercept;
    }

    public void move (Point start, Point end) {
        this.start = start;
        this.end = end;
        haveSlope = setValues();
    }

    public void move (int startX, int startY, int endX, int endY) {
        start.move(startX, startY);
        end.move(endX, endY);
        haveSlope = setValues();
    }

    public double distance(Point point) {
        double distance;
        if (start.equals(end)) {
            distance = start.distance(point);
            return Math.abs(distance);
        }
        int spX = start.x - point.x;
        int seX = end.x - start.x;
        int spY = start.y - point.y;
        int seY = end.y - start.y;
        double t = (-1)* ((double)( spX * seX + spY * seY))
                           / (seX * seX + seY * seY);
        if (t >= 0 && t <= 1) {
            double xFact = t * seX + spX;
            double yFact = t * seY + spY;
            distance = Math.sqrt( xFact * xFact + yFact * yFact );
            return Math.abs(distance);
        }
        else if (t < 0) {
            distance = start.distance(point);
            return Math.abs(distance);
        }
        else
            distance = end.distance(point);
        return Math.abs(distance);
    }

/*

You might be wondering how does this help? Think of two line segments AB, and CD.
 These intersect if and only if points A and B are separated by segment CD
 and points C and D are separated by segment AB.
 If points A and B are separated by segment CD then ACD and BCD should have
 opposite orientation meaning either ACD or BCD is counterclockwise but not both.
 Therefore calculating if two line segments AB and CD intersect is as follows:
 http://compgeom.cs.uiuc.edu/~jeffe/teaching/373/notes/x06-sweepline.pdf
 http://compgeom.cs.uiuc.edu/~jeffe/teaching/373/notes/x05-convexhull.pdf
 */
    public static boolean counterClockWise (Point p1, Point p2, Point p3) {
        return (p3.y - p1.y) * (p2.x - p1.x) > (p2.y - p1.y)*(p3.x - p1.x);
    }

    public boolean intersect (LineSegment2D targetLine) {
        if (targetLine == null || start == null || end == null) {
            return false;
        }
        Point tEnd = targetLine.getEnd();
        Point tStart = targetLine.getStart();
        if (counterClockWise (start, tStart, tEnd) != counterClockWise(end, tStart, tEnd)) {
            return false;
        }
        if (counterClockWise (start, end, tStart) != counterClockWise(start, end, tEnd)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return (" START: " + start + " END: " + end );
    }

    public Object clone() {
        try {
			LineSegment2D line = (LineSegment2D)super.clone();
            line.move((Point)start.clone(),(Point)end.clone());
			return line;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

}
