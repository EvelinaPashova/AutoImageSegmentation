
package lv.lumii.pixelmaster.modules.filters.domain;
import java.util.*;

/**
 * Class representing sorted array of constant size. Keeps track of the order in
 * which elements were added and allows to replace the oldest element in the
 * array with specified element.
 * @author Jevgenijs Jonass
 */
final public class SortedArray {

	/** array is sorted in ascending order */
	private int[] arr;

	/**
	 * Elements in q are queued in the same order as they were added to this array:
	 * top element in q was added before all other elements
	 */
	private ArrayDeque<Integer> q;

	/** Sum of elements in the array */
	private int sum;
	
	/**
	 * Constructs an array of <code>len</code> elements.
	 * @param len length of the array (positive integer).
	 *		The length cannot be changed after the array is created.
	 * @post this array is filled with 0
	 */
	public SortedArray(int len) {
		assert len > 0;
		arr=new int[len];
		q=new ArrayDeque<Integer> (len);
		for (int i=0; i<len; i++) q.add(0);
		sum=0;
		assert invariant();
	}

	/**
	 * Returns element at specified position in this sorted array.
	 * @param pos integer in range [0..length-1]
	 * @return array element
	 */
	public int getElement(int pos) {
		assert !(pos<0 || pos>=arr.length);
		return arr[pos];
	}

	/**
	 * Returns the median - element at index length/2
	 * @return the median
	 */
	public int getMedian() { return arr[arr.length >>> 1]; }

	/**
	 * Returns average of the elements
	 * @return average of the elements
	 */
	public double average() { return (double) sum / arr.length; }

	/**
	 * Returns length of this array
	 * @return positive integer
	 */
	public int length() { return arr.length; }

	/**
	 * Initializes the array with [width*height] elements from
	 * <code>buffer</code>, which represents a 2D array of pixels.
	 * Elements are taken from rectangular area, whose left top pixel has
	 * index <code>start</code>. Element values are shifted by
	 * <code>offset</code> bits to the right and then mask 0x000000ff is applied.
	 * The elements are added in the following order:
	 *			1 2 3 4
	 *			5 6 7 8
	 *
	 * @param buffer source buffer
	 * @param start index of the left top pixel in the buffer (non-negative)
	 * @param stepSize distance between one row of pixels and the next row
	 *		in <code>buffer</code> (width of the image, positive integer)
	 * @param width width of the rectangular area of pixels (positive)
	 * @param height height of the rectangular area of pixels (positive)
	 * @param offset specifies color channel (0, 8 or 16)
	 * @pre buffer.length >= start + height * stepSize
	 * @pre stepsize >= width
	 * @pre width * height == length()
	 */
	public void init(int[] buffer, int start, int stepSize, int width, int height, int offset) {
		assert buffer !=null && start >= 0 && stepSize >= 0 && width > 0 && height > 0;
		assert offset ==0 || offset == 8 || offset == 16;
		assert start + arr.length <= buffer.length;
		assert invariant();
		q.clear();
		sum=0;
		int arrIndex=0;
		for (int i=0; i<height; i++, start+=stepSize) {
			for (int j=0, pixelIndex=start; j<width; j++, pixelIndex++) {
				int value=((buffer[pixelIndex]) >> offset) & 255;
				arr[arrIndex++]=value;
				q.add(value);
				sum+=value;
			}
		}
		assert arr.length==arrIndex;
		Arrays.sort(arr);
		assert invariant();
	}

	/**
	 * Replaces the oldest element in the array with specified element.
	 * @param m the new element
	 */
	public void replace(int m) {
		assert invariant();
		int k=q.remove().intValue();
		q.add(m);
		sum+=(m-k);
		if (k==m) return;
		int index1=Arrays.binarySearch(arr, k);
		assert !(index1<0);// Array must contain element k
		int index2=Arrays.binarySearch(arr, m);
		if (index2<0) index2=-index2-1;
		if (index1==index2 || index1+1==index2) {
			arr[index1]=m;
		}
		else if (index1<index2) {
			System.arraycopy(arr, index1+1, arr, index1, index2-index1-1);
			arr[index2-1]=m;
		}
		else {
			// index1>index2
			System.arraycopy(arr, index2, arr, index2+1, index1-index2);
			arr[index2]=m;
		}
		assert invariant();
	}

	private boolean invariant() {
		assert arr != null && q != null && arr.length > 0 && arr.length == q.size();
		for (int i = 1; i < arr.length; i++) assert arr[i] >= arr[i - 1];
		for (int i = 0; i < arr.length; i++) assert q.contains(arr[i]);
		for (Integer i: q) assert Arrays.binarySearch(arr, i.intValue()) >= 0;
		int expSum = 0;
		for (int i = 0; i < arr.length; i++) expSum += arr[i];
		assert expSum == this.sum;
		return true;
	}

	// for testing purposes only
	int[] _arr() {return arr;}
	ArrayDeque<Integer> _q() {return q;}
	int _sum() {return sum;}
}
