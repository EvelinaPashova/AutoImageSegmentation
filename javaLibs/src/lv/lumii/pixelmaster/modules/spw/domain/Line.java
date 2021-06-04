
package lv.lumii.pixelmaster.modules.spw.domain;
import lv.lumii.pixelmaster.modules.spw.domain.graph.Vertex;
import java.awt.Point;
import java.util.*;

/**
 * Class containing methods for analytical geometry
 * @author Jevgenijs Jonass
 */
final public class Line {

	/**
	 * Returns cosinus of the angle between lines that are parallel to vectors (qp) and (qr).
	 * @param p The first point, cannot be null
	 * @param q The second point, cannot be null
	 * @param r The third point, cannot be null
	 * @pre vectors pq and qr are not null vectors
	 * @return value in range [0..1] (since angle value lies in range [0..90 degrees])
	 */
	public static double angleBetweenLinesCos(Point p, Point q, Point r) {
		return angleBetweenLinesCos(p.x, p.y, q.x, q.y, r.x, r.y);
	}

	/**
	 * Returns cosinus of the angle between lines that are parallel to vectors (qp) and (qr)
	 * @param p The first point, cannot be null
	 * @param q The second point, cannot be null
	 * @param r The third point, cannot be null
	 * @pre vectors pq and qr are not null vectors
	 * @return value in range [0..1] (since angle value lies in range [0..90 degrees])
	 */
	public static double angleBetweenLinesCos(Vertex p, Vertex q, Vertex r) {
		return angleBetweenLinesCos(p.getX(), p.getY(), q.getX(), q.getY(), r.getX(), r.getY());
	}

	/**
	 * Returns cosinus of the angle between lines that are parallel to vectors (qp) and (qr).
	 * @param x1 X coordinate of p
	 * @param y1 Y coordinate of p
	 * @param x2 X coordinate of q
	 * @param y2 Y coordinate of q
	 * @param x3 X coordinate of r
	 * @param y3 Y coordinate of r
	 * @pre vectors pq and qr are not null vectors
	 * @return value in range [0..1] (since angle value lies in range [0..90 degrees])
	 */
	public static double angleBetweenLinesCos(double x1, double y1, double x2, double y2, double x3, double y3) {
		return Math.abs(angleBetweenVectorsCos(x1, y1, x2, y2, x3, y3));
	}

	/**
	 * Returns cosinus of the angle between vectors (qp) and (qr).
	 * @param p The first point, cannot be null
	 * @param q The second point, cannot be null
	 * @param r The third point, cannot be null
	 * @pre vectors pq and qr are not null vectors
	 * @return value in range [-1..1] (since angle value lies in range [0..180 degrees])
	 */
	public static double angleBetweenVectorsCos(Point p, Point q, Point r) {
		return angleBetweenVectorsCos(p.x, p.y, q.x, q.y, r.x, r.y);
	}

	/**
	 * Returns cosinus of the angle between vectors (qp) and (qr)
	 * @param p The first point, cannot be null
	 * @param q The second point, cannot be null
	 * @param r The third point, cannot be null
	 * @pre vectors pq and qr are not null vectors
	 * @return value in range [-1..1] (since angle value lies in range [0..180 degrees])
	 */
	public static double angleBetweenVectorsCos(Vertex p, Vertex q, Vertex r) {
		return angleBetweenVectorsCos(p.getX(), p.getY(), q.getX(), q.getY(), r.getX(), r.getY());
	}

	/**
	 * Returns cosinus of the angle between vectors (qp) and (qr)
	 * @param x1 X coordinate of p
	 * @param y1 Y coordinate of p
	 * @param x2 X coordinate of q
	 * @param y2 Y coordinate of q
	 * @param x3 X coordinate of r
	 * @param y3 Y coordinate of r
	 * @pre vectors pq and qr are not null vectors
	 * @return value in range [-1..1] (since angle value lies in range [0..180 degrees])
	 */
	public static double angleBetweenVectorsCos(double x1, double y1, double x2, double y2, double x3, double y3) {
		CoordVector n1=new CoordVector(x1-x2, y1-y2);
		CoordVector n2=new CoordVector(x3-x2, y3-y2);
		double len1=n1.getLength(), len2=n2.getLength();
		assert len1 != 0 && len2 != 0;
		double p=CoordVector.scalarProduct(n1, n2);
		return p/(len1*len2);
	}

	/**
	 * Returns distance from point a to line that passes through points b and c
	 * @param a
	 * @param b
	 * @param c
	 * @pre points b and c have different locations
	 * @return non-negative double value - the distance
	 */
	public static double distanceFromLineToPoint(Vertex a, Vertex b, Vertex c) {
		assert b != null && c != null && !b.equals(c);
		double A = c.getY() - b.getY(), B = b.getX() - c.getX(),
			C = -b.getX() * c.getY() + b.getX() * b.getY() +
			b.getY() * c.getX() - b.getX() * b.getY();
		double sqrt = Math.hypot(A, B);
		return Math.abs(A * a.getX() + B * a.getY() + C) / sqrt;
	}

	/**
	 * Returns variance which is defined as sum of squares of distances from line
	 * connecting the first and the last vertices in <code>vect</code> to every
	 * inner vertex of the <code>vect</code>, divided by [number of elements in <code>vect</code> - 1].
	 * 
	 * @param vect vector containing non-null elements.
	 *		Must have at least 2 elements, cannot be null.
	 * @pre first and last vertices in the vector have different locations
	 * @return non-negative double value - the variance
	 */
	public static double variance(Vector<Vertex> vect) {
		assert vect!=null && vect.size()>1;
		double sum=0;
		Vertex last=vect.lastElement();
		assert last!=null;
		Iterator <Vertex> it=vect.iterator();
		Vertex first=it.next();
		assert first!=null;
		assert !(first.equals(last));
		while (it.hasNext()) {
			Vertex v=it.next();
			assert v!=null;
			if (!it.hasNext()) break;
			double dist=distanceFromLineToPoint(v, first, last);
			sum+=dist*dist;
		}
		return sum/(vect.size()-1);
	}

	/**
	 * Returns distance from one vertex to another
	 * @param v first vertex, cannot be null
	 * @param w second vertex, cannot be null
	 * @return non-negative value - the distance
	 */
	public static double distance(Vertex v, Vertex w) {
		int dx=v.getX()-w.getX(), dy=v.getY()-w.getY();
		return Math.sqrt(dx*dx+dy*dy);
	}
}

