/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lv.lumii.pixelmaster.modules.ridge.domain;

import java.util.ArrayList;
import java.util.UUID;
/**
 *
 * @author Kumpa
 */
public class LineSet implements Cloneable{
    private long id;
    private ArrayList<LineSegment2D> segments;

    LineSet() {
        segments = new ArrayList<LineSegment2D>();
        id = UUID.randomUUID().getMostSignificantBits();
    }

    LineSet(long id) {
        segments = new ArrayList<LineSegment2D>();
        this.id = id;
    }
    
    public void setID (long value) {
        id = value;
    }

    public long getID() {
        return id;
    }

    public ArrayList<LineSegment2D> getLines() {
        return segments;
    }
    public boolean add(LineSegment2D line) {
        segments.add(line);
        return true;
    }

    public boolean addAll (ArrayList<LineSegment2D> lines) {
        this.segments.addAll(lines);
        return true;
    }

    public boolean contains(LineSegment2D line) {
        if (segments.contains(line))
            return true;
        return false;
    }

  	public Object clone() {
    	try {
			LineSet lSet=(LineSet)super.clone();
            lSet.id = this.id;
            for (int i = 0; i < segments.size(); i++) {
                lSet.add((LineSegment2D)segments.get(i).clone());
			}
			return lSet;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}
}
