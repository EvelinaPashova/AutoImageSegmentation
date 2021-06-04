
package lv.lumii.pixelmaster.modules.spw.domain;

import lv.lumii.pixelmaster.modules.spw.domain.graph.Vertex;
import java.awt.Point;
import lv.lumii.pixelmaster.core.api.domain.Constants;

/**
 * Class representing a coordinate vector with specified dimension
 * @author Jevgenijs Jonass
 */
final public class CoordVector {

	/** Array containing vector elements */
	private double[] vector;

	/** cache of the squares of array elements */
	private double[] squares;

	/**
	 * Creates a vector with specified dimension
	 * @param dim dimension, a non-negative integer
	 */
	public CoordVector(int dim) {
		assert !(dim<0);
		vector=new double[dim];
		squares=new double[dim];
		assert invariant();
	}
	
	/**
	 * Creates a vector containing given elements in the same order as
	 * they are in <code>args</code>
	 * Dimension of the vector is determined by number of elements.
	 * @param args Elements of the vector
	 */
	public CoordVector(double... args) {
		int len=args.length;
		vector=new double[len];
		squares=new double[len];
		int i=0;
		for (double arg: args) {
			vector[i]=arg;
			squares[i++]=arg*arg;
		}
		assert invariant();
	}

	/**
	 * Creates a 2D vector from start point to end point.
	 * @param p start point
	 * @param q end point
	 */
	public CoordVector(Point p, Point q) {
		assert p!=null && q!=null;
		int len=2;
		vector=new double[len];
		squares=new double[len];
		vector[0]=q.x-p.x;
		vector[1]=q.y-p.y;
		squares[0]=vector[0]*vector[0];
		squares[1]=vector[1]*vector[1];
		assert invariant();
	}

	/**
	 * Creates a 2D vector from start point to end point.
	 * @param p start point
	 * @param q end point
	 */
	public CoordVector(Vertex p, Vertex q) {
		assert p!=null && q!=null;
		int len=2;
		vector=new double[len];
		squares=new double[len];
		vector[0]=q.getX()-p.getX();
		vector[1]=q.getY()-p.getY();
		squares[0]=vector[0]*vector[0];
		squares[1]=vector[1]*vector[1];
		assert invariant();
	}

	/**
	 * Sets value of vector element at specified index
	 * @param index Coordinate
	 * @param value New value of the element
	 * @pre <code>index&gt;=0 and index&lt;dimension()</code>
	 */
	public void setElement(int index, double value) {
		assert !(index<0 || index>=vector.length);
		assert invariant();
		vector[index]=value;
		squares[index]=value*value;
		assert invariant();
	}

	/**
	 * Returns dimension of this vector
	 * @return dimension of this vector
	 */
	public int dimension() { return vector.length; }

	/**
	 * Gets value of vector element at specified index.
	 * @param index Coordinate
	 * @pre	<code>index&gt;=0 and index&lt;dimension()</code>
	 * @return Value of the element
	 */
	public double getElement(int index) { return vector[index]; }

	/** @return Length of the vector */
	public double getLength() {
		double sum=0;
		for (int i=0; i<vector.length; i++) sum+=squares[i];
		return Math.sqrt(sum);
	}

	/**
	 * Calculates scalar product of two vectors
	 * @param v The first vector
	 * @param q The second vector
	 * @pre vectors <code>v and q</code> have the same dimension
	 * @return <a href="http://en.wikipedia.org/wiki/Scalar_product">Scalar product</a>
	 */
	public static double scalarProduct(CoordVector v, CoordVector q) {
		assert !(v==null || q==null || v.vector.length!=q.vector.length);
		double sum=0;
		int len=v.vector.length;
		for (int i=0; i<len; i++) {
			sum+=v.vector[i]*q.vector[i];
		}
		return sum;
	}

	/** If the difference is less than PRECISION, treat as equal */
	private static boolean equals(double a, double b, double eps) {
        return Math.abs(a - b) < eps;
    }

	private boolean invariant() {
		assert vector != null && squares != null;
		assert vector.length == squares.length;
		for (int i = 0; i < vector.length; i++)
			assert equals(vector[i] * vector[i], squares[i], Constants.PRECISION);
		return true;
	}

	// for testing purposes only
	double[] _vector() {return vector;}
	double[] _squares() {return squares;}
}
