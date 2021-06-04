
package lv.lumii.pixelmaster.modules.filters.domain;

import java.awt.Rectangle;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.RGB;

/**
 * Class providing methods for median filtering
 * @author Jevgenijs Jonass
 * @see <a href="http://www.shellandslate.com/download/fastmedian_5506.pdf">Fast Median and Bilateral Filtering (PDF)</a>
 * @see <a href="http://nomis80.org/ctmf.html">Median Filtering in Constant Time</a>
 */
public final class MedianFilter {

	/**
	 * Calculates the median of the historgam (as described in the article).
	 * @param histogram The histogram
	 * @param n total number of elements in the histogram
	 *		(in the median filter algorithm, n=(2r+1)^2)
	 * @pre <code>histogram.length==256</code>
	 * @pre n is odd integer that is &gt;=1.
	 * @pre number of elements in the histogram is exactly <code>n</code>
	 * @return The median (0..255)
	 */
	private static int histMedian(int [] histogram, int n) {
		assert !(histogram==null || histogram.length!=256 || n<1 || n%2!=1);
		int i, sum=0, k=n>>>1;
		for (i=0; i<256; i++) {
			sum+=histogram[i];
			if (sum>k) break;
		}
		assert !(i==256);
		return i;
	}

	/**
	 * Returns the sum of two histograms: histogram[m] and histogram[n] (as described in the article).
	 * Allocates new memory for the returned histogram.
	 * @param histogram Histograms (ownership: caller)
	 * @param m Index of the first histogram.
	 * @param n Index of the second histogram.
	 * @pre <code>histogram!=null</code>
	 * @pre m&gt;=0 and m&lt;histogram.length and n&gt;=0 and n&lt;histogram.length.
	 * @pre for all i in range [0..histogram.length-1] (histogram[i]!=null and histogram[i].length==256)
	 * @post returned histogram has length 256
	 * @post <code>histogram</code> is not modified and for all i [histogram[i] is not modified].
	 * @return Sum of two histograms
	 */
	private static int[] sum(int[][] histogram, int m, int n) {
		assert !(histogram==null || m<0 || m>=histogram.length || n<0 || n>=histogram.length);
		assert !(histogram[m]==null || histogram[n]==null || histogram[m].length!=256 || histogram[n].length!=256);
		int [] h=new int [256];
		for (int i=0; i<256; i++) {
			h[i]=histogram[m][i]+histogram[n][i];
		}
		return h;
	}

	/**
	 * Returns the histogram H[n] (as described in the article).
	 * If n!=histogram.length/2, the histogram is calculated as the sum of
	 * histograms histogram[histogram.length/2] and histogram[n].
	 * In this case, memory for a new histogram is allocated.
	 * Otherwise, histogram[n] is returned.
	 * 
	 * @param histogram Array of partial histograms
	 * @param n Index of the histogram.
	 * @pre <code>histogram!=null</code>
	 * @pre n&gt;=0 and n&lt;histogram.length.
	 * @pre for all i in range [0..histogram.length-1] (histogram[i]!=null and histogram[i].length==256)
	 * @post returned histogram has length 256
	 * @return Histogram H[n]
	 */
	private static int[] join(int[][] histogram, int n) {
		assert !(histogram==null || n<0 || n>=histogram.length);
		if (n==histogram.length/2) return histogram[n];
		int[] h=sum(histogram, n, histogram.length/2);
		return h;
	}

	/**
	 * Substracts a row from the histogram (as described in the article).
	 * Must be used separately for each RGB component, which is specified by bit offset.
	 * 
	 * <p><code>pixels</code> at indexes [start..start+width-1] are substracted
	 * from the histogram.</p>
	 * 
	 * @param histogram The histogram
	 * @param pixels Array of pixels. Must have format which is described in RasterImage.
	 * @param start Index of the first element in the row.
	 * @param r Radius.
	 * @param width Width of the row. (In the median filtering algorithm, width must be equal to selWidth+2*r.)
	 * @param offset Bit offset (0, 8 or 16).
	 * @pre histogram!=null
	 * @pre histogram.length=width-2*r
	 * @pre for all i in range [0..histogram.length-1] (histogram[i]!=null and histogram[i].length==256)
	 * @pre pixels!=null
	 * @pre r&gt;=0
	 * @pre	start&gt;=0
	 * @pre width&gt;=2*r+1
	 * @pre pixels.length&gt;=start+width
	 * @pre offset=0 or offset=8 or offset=16
	 * @post Does not modify <code>pixels</code>
	 */
	private static void substractRow(int [][] histogram, int [] pixels, int start, int r, int width, int offset) {
		assert !(histogram==null || histogram.length!=width-2*r || pixels==null ||
			start<0 || r<0 || width<2*r+1 || pixels.length<start+width ||
			(offset!=0 && offset!=8 && offset!=16));

		int n=width-2*r;
		int center=n/2+r, c1=center-r, c2=center+r+1;
		for (int i=0; i<n/2; i++) {
			int i1=2*r+i+1; if (i1>n/2) i1=n/2;
			for (int j=i; j<i1; j++) {
				int value=((pixels[j+start]) >> offset) & 255;
				histogram[i][value]--;
			}

			int c=i1-i; // how many elements were substracted
			for (int j=c2-c; j<c2; j++) {
				int value=((pixels[j+start]) >> offset) & 255;
				histogram[i][value]++;
			}
		}
		for (int j=c1; j<c2; j++) {
			int value=((pixels[j+start]) >> offset) & 255;
			histogram[n/2][value]--;
		}
		for (int i=n/2+1; i<n; i++) {
			int i2=c2; if (i>i2) i2=i;
			for (int j=i2; j<i+2*r+1; j++) {
				int value=((pixels[j+start]) >> offset) & 255;
				histogram[i][value]--;
			}

			int c=i+2*r+1-i2; // how many elements were added
			for (int j=c1; j<c1+c; j++) {
				int value=((pixels[j+start]) >> offset) & 255;
				histogram[i][value]++;
			}
		}
	}

	/**
	 * Adds a row to the histogram (as described in the article).
	 * Must be used separately for each RGB component, which is specified by bit offset.
	 *
	 * <p><code>pixels</code> at indexes [start..start+width-1] are added
	 * to the histogram.</p>
	 *
	 * @param histogram The histogram
	 * @param pixels Array of pixels. Must have format which is described in RasterImage. (ownership: caller)
	 * @param start Index of the first element in the row.
	 * @param r Radius
	 * @param width Width of the row. (In the median filtering algorithm, width must be equal to selWidth+2*r.)
	 * @param offset 
	 * @pre histogram.length=width-2*r
	 * @pre for all i in range [0..histogram.length-1] (histogram[i]!=null and histogram[i].length==256)
	 * @pre pixels!=null
	 * @pre r&gt;=0
	 * @pre	start&gt;=0
	 * @pre width&gt;=2*r+1
	 * @pre pixels.length&gt;=start+width
	 * @pre offset=0 or offset=8 or offset=16
	 */
	private static void addRow(int[][] histogram, int[] pixels, int start, int r, int width, int offset) {
		assert !(histogram==null || histogram.length!=width-2*r || pixels==null ||
			start<0 || r<0 || width<2*r+1 || pixels.length<start+width ||
			(offset!=0 && offset!=8 && offset!=16));

		int n=width-2*r;
		int center=n/2+r, c1=center-r, c2=center+r+1;
		for (int i=0; i<n/2; i++) {
			int i1=2*r+i+1; if (i1>n/2) i1=n/2;
			for (int j=i; j<i1; j++) {
				int value=((pixels[j+start]) >> offset) & 255;
				histogram[i][value]++;
			}

			int c=i1-i; // how many elements were added
			for (int j=c2-c; j<c2; j++) {
				int value=((pixels[j+start]) >> offset) & 255;
				histogram[i][value]--;
			}
		}
		for (int j=c1; j<c2; j++) {
			int value=((pixels[j+start]) >> offset) & 255;
			histogram[n/2][value]++;
		}
		for (int i=n/2+1; i<n; i++) {
			int i2=c2; if (i>i2) i2=i;
			for (int j=i2; j<i+2*r+1; j++) {
				int value=((pixels[j+start]) >> offset) & 255;
				histogram[i][value]++;
			}

			int c=i+2*r+1-i2; // how many elements were substracted
			for (int j=c1; j<c1+c; j++) {
				int value=((pixels[j+start]) >> offset) & 255;
				histogram[i][value]--;
			}
		}
	}
	
	/**
	 * Median filter. Must be used separately for each RGB component.
	 * Bit offset specifies the RGB component.
	 * @param inputImage The original image
	 * @param target The target image
	 * @param radius non-negative integer
	 * @param offset 
	 * @param threshold non-negative integer
	 * @param selection Area to filter. If null, the whole image is filtered.
	 *		NULL is interpreted as a wildcard value that specifies all pixels.
	 * @pre target != inputImage
	 * @pre inputImage.getWidth() == target.getWidth() and inputImage.getHeight()==target.getHeight()
	 * @pre inputImage.getWidth() &gt;= 2*r+1 and inputImage.getHeight() &gt;= 2*r+1
	 * @pre selection != null =&gt; inputImage.inBounds(selection)
	 */
	private static void medianFilter(RasterImage inputImage, RasterImage target,
		int radius, int offset, int threshold, Rectangle selection) {
		if (selection==null) selection=new Rectangle(0, 0, inputImage.getWidth(), inputImage.getHeight());
		assert !(inputImage==null || target==null || radius<0 ||
			inputImage==target || (offset!=0 && offset!=8 && offset!=16) ||
			inputImage.getWidth()<2*radius+1 || inputImage.getHeight()<2*radius+1 ||
			inputImage.getWidth()!=target.getWidth() || inputImage.getHeight()!=target.getHeight() || threshold<0 ||
			!inputImage.inBounds(selection)
		);
		
		//if (r==0) return;
		int width=inputImage.getWidth(), height=inputImage.getHeight();

		int selX=selection.x, selY=selection.y, selWidth=selection.width, selHeight=selection.height;
		if (selX<radius) {
			selWidth-=(radius-selX);
			selX=radius;
		}
		if (selX+selWidth>width-radius) selWidth=width-radius-selX;
		if (selY<radius) {
			selHeight-=(radius-selY);
			selY=radius;
		}
		if (selY+selHeight>height-radius) selHeight=height-radius-selY;
		if (selHeight<1 || selWidth<1) return;
		
		int[][] histogram=new int[selWidth][256];
//		for (int i=0; i<selWidth; i++) {
//			Arrays.fill(histogram[i], 0);
//		}
		
		for (int i=selY-radius; i<selY+radius+1; i++) {
			addRow(histogram, inputImage.getPixels(), i*width+selX-radius, radius, selWidth+2*radius, offset);
		}
		int total=(2*radius+1)*(2*radius+1);
		for (int j=selX; j<selX+selWidth; j++) {
			int [] h=join(histogram, j-selX);
			int m=histMedian(h, total);
			int destPos=selY*width+j;
			int pixel=(target.getRGB(destPos) >> offset) & 255;
			if (Math.abs(m-pixel)>threshold) {
				target.setRGB(destPos, target.getRGB(destPos) & (~(255 << offset)));
				target.setRGB(destPos, target.getRGB(destPos) | ((m << offset)));
			}
		}
		for (int i=selY+1; i<selY+selHeight; i++) {
			addRow(histogram, inputImage.getPixels(), (i+radius)*width+selX-radius, radius, selWidth+2*radius, offset);
			substractRow(histogram, inputImage.getPixels(), (i-radius-1)*width+selX-radius, radius, selWidth+2*radius, offset);
			for (int j=selX; j<selX+selWidth; j++) {
				int [] h=join(histogram, j-selX);
				int m=histMedian(h, total);
				int destPos=i*width+j;
				int pixel=(target.getRGB(destPos) >> offset) & 255;
				if (Math.abs(m-pixel)>threshold) {
					target.setRGB(destPos, target.getRGB(destPos) & (~(255 << offset)));
					target.setRGB(destPos, target.getRGB(destPos) | ((m << offset)));
				}
			}
		}
	}
	
	/**
	 * Median filter. The inputImage and target may be the same.
	 * @param inputImage The original image (ownership: caller)
	 * @param target The target image (ownership: caller)
	 * @param radius filtering radius (must be &gt;= 0)
	 * @param threshold Only those pixels are replaced with the median, whose
	 *		value differs from the median value by more than <code>threshold</code>. (must be &gt;= 0)
	 * @param selection Area to filter. If null, the whole image is filtered. (ownership: caller)
	 * @pre inputImage != null and target != null
	 * @pre class invariants of inputImage and target are true
	 * @pre inputImage.getWidth() == target.getWidth() and inputImage.getHeight()==target.getHeight()
	 * @pre selection != null =&gt; inputImage.inBounds(selection)
	 * @post Does not modify <code>selection</code>
	 * @post inputImage!=target =&gt; <code>inputImage</code> will not me mofified
	 * @post class invariant of target is true
	 * @post dimensions of target are not changed
	 * @author Jevgenijs Jonass.
	 * @since 02.04.2009
	 */
	public static void medianFilter(RasterImage inputImage, RasterImage target,
		int radius, int threshold, Rectangle selection) {
		if (selection==null) selection=new Rectangle(0, 0, inputImage.getWidth(), inputImage.getHeight());
		assert !(
			inputImage==null || target==null || radius<0 ||
			inputImage.getWidth()!=target.getWidth() || inputImage.getHeight()!=target.getHeight() || threshold<0 ||
			!inputImage.inBounds(selection)
		);
		
		int width=inputImage.getWidth(), height=inputImage.getHeight();
		if (width<2*radius+1 || height<2*radius+1/* || r==0*/) {
			if (inputImage!=target) inputImage.copyTo(target);
			return;
		}
		if (inputImage==target) {
			inputImage=new RasterImage(inputImage);
		}
		
	//	target.getWidth()=width;
	//	target.getHeight()=height;
		medianFilter(inputImage, target, radius, 0, threshold, selection);
		medianFilter(inputImage, target, radius, 8, threshold, selection);
		medianFilter(inputImage, target, radius, 16, threshold, selection);
	}

	/**
	 * Filters the image using bidirectional median filter.
	 * @param inputImage The original image.
	 * @param selection Area to filter. If null, the whole image is filtered. (ownership: caller)
	 * @param radius filtering radius. Must be &gt;=0.
	 * @param threshold Only those pixels are replaced with the median, whose
	 *		value differs from the median value by more than <code>threshold</code>. Must be &gt;=0.
	 * @pre inputImage != null
	 * @pre class invariant of inputImage is true
	 * @pre selection != null =&gt; inputImage.inBounds(selection)
	 * @post Does not modify <code>selection</code>
	 * @post class invariant of inputImage is true
	 * @author Jevgenijs Jonass.
	 * @since 02.04.2009
	 * @version 10.04.2009 Added <code>selection</code> parameter.
	 * @version 01.05.2009 Added <code>radius</code> and <code>threshold</code> parameters.
	 */
	public static void medianFilterBidirectional(RasterImage inputImage, Rectangle selection, int radius, int threshold) {
		if (selection==null) selection=new Rectangle(0, 0, inputImage.getWidth(), inputImage.getHeight());
		assert !(inputImage==null || selection==null ||
			!inputImage.inBounds(selection) || radius<0 || threshold<0);
		int width=inputImage.getWidth(), height=inputImage.getHeight();
		int size=2*radius+1;

		//horizontal pass
		int selX=selection.x, selY=selection.y, selWidth=selection.width, selHeight=selection.height;
		if (selX<radius) {
			selWidth-=(radius-selX);
			selX=radius;
		}
		if (selX+selWidth>width-radius) selWidth=width-radius-selX;
		//System.out.println("medianFilterBidirectional horizontal pass, selWidth=" + selWidth + "; selX: " + selX);
		if (selWidth>=1) {
			SortedArray r=new SortedArray(size), g=new SortedArray(size), b=new SortedArray(size);
			for (int i=0, rowStart=selY*width+selX; i<selHeight; i++, rowStart+=width) {
				r.init(inputImage.getPixels(), rowStart-radius, width, size, 1, 16);
				g.init(inputImage.getPixels(), rowStart-radius, width, size, 1, 8);
				b.init(inputImage.getPixels(), rowStart-radius, width, size, 1, 0);

				int oldR=RGB.getR(inputImage.getRGB(rowStart));
				int oldG=RGB.getG(inputImage.getRGB(rowStart));
				int oldB=RGB.getB(inputImage.getRGB(rowStart));
				int medianR=r.getMedian();
				int medianG=g.getMedian();
				int medianB=b.getMedian();
				int newR, newG, newB;
				if (Math.abs(medianR-oldR)>threshold) newR=medianR;
				else newR=oldR;
				if (Math.abs(medianG-oldG)>threshold) newG=medianG;
				else newG=oldG;
				if (Math.abs(medianB-oldB)>threshold) newB=medianB;
				else newB=oldB;
				inputImage.setRGB(rowStart, RGB.getRGB(newR, newG, newB));

				for (int j=1, index=rowStart+1; j<selWidth; j++, index++) {
					r.replace(RGB.getR(inputImage.getRGB(index+radius)));
					g.replace(RGB.getG(inputImage.getRGB(index+radius)));
					b.replace(RGB.getB(inputImage.getRGB(index+radius)));
					oldR=RGB.getR(inputImage.getRGB(index));
					oldG=RGB.getG(inputImage.getRGB(index));
					oldB=RGB.getB(inputImage.getRGB(index));
					medianR=r.getMedian();
					medianG=g.getMedian();
					medianB=b.getMedian();
					if (Math.abs(medianR-oldR)>threshold) newR=medianR;
					else newR=oldR;
					if (Math.abs(medianG-oldG)>threshold) newG=medianG;
					else newG=oldG;
					if (Math.abs(medianB-oldB)>threshold) newB=medianB;
					else newB=oldB;
					inputImage.setRGB(index, RGB.getRGB(newR, newG, newB));
				}
			}
		}

		//vertical pass
		selX=selection.x; selY=selection.y; selWidth=selection.width; selHeight=selection.height;
		if (selY<radius) {
			selHeight-=(radius-selY);
			selY=radius;
		}
		if (selY+selHeight>height-radius) selHeight=height-radius-selY;
		//System.out.println("medianFilterBidirectional vertical pass, selHeight=" + selHeight + "; selY: " + selY);
		if (selHeight>=1) {
			SortedArray r=new SortedArray(size), g=new SortedArray(size), b=new SortedArray(size);
			for (int i=selX+selY*width; i<selX+selY*width+selWidth; i++) {
				r.init(inputImage.getPixels(), i-radius*width, width, 1, size, 16);
				g.init(inputImage.getPixels(), i-radius*width, width, 1, size, 8);
				b.init(inputImage.getPixels(), i-radius*width, width, 1, size, 0);
				int oldR=RGB.getR(inputImage.getRGB(i));
				int oldG=RGB.getG(inputImage.getRGB(i));
				int oldB=RGB.getB(inputImage.getRGB(i));
				int medianR=r.getMedian();
				int medianG=g.getMedian();
				int medianB=b.getMedian();
				int newR, newG, newB;
				if (Math.abs(medianR-oldR)>threshold) newR=medianR;
				else newR=oldR;
				if (Math.abs(medianG-oldG)>threshold) newG=medianG;
				else newG=oldG;
				if (Math.abs(medianB-oldB)>threshold) newB=medianB;
				else newB=oldB;
				inputImage.setRGB(i, RGB.getRGB(newR, newG, newB));

				for (int j=1, index=i+width; j<selHeight; j++, index+=width) {
					r.replace(RGB.getR(inputImage.getRGB(index+radius*width)));
					g.replace(RGB.getG(inputImage.getRGB(index+radius*width)));
					b.replace(RGB.getB(inputImage.getRGB(index+radius*width)));

					oldR=RGB.getR(inputImage.getRGB(index));
					oldG=RGB.getG(inputImage.getRGB(index));
					oldB=RGB.getB(inputImage.getRGB(index));
					medianR=r.getMedian();
					medianG=g.getMedian();
					medianB=b.getMedian();
					if (Math.abs(medianR-oldR)>threshold) newR=medianR;
					else newR=oldR;
					if (Math.abs(medianG-oldG)>threshold) newG=medianG;
					else newG=oldG;
					if (Math.abs(medianB-oldB)>threshold) newB=medianB;
					else newB=oldB;
					inputImage.setRGB(index, RGB.getRGB(newR, newG, newB));
				}
			}
		}
	}
}
