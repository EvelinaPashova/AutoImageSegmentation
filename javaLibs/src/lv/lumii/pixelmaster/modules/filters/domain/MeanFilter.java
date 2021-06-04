
package lv.lumii.pixelmaster.modules.filters.domain;

import java.awt.Rectangle;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 * Class providing algorithms for mean and variance filtering.
 * @author Jevgenijs Jonass
 */
public class MeanFilter {

	/**
	 * Mean filter. The inputImage and target may be the same. If the area
	 * specified by <code>selection</code> is closer to one of the image sides
	 * than <code>radius</code>, the area will be clipped.
	 * 
	 * @param inputImage The original image, cannot be null
	 * @param target The target image, cannot be null
	 * @param radius filtering radius (non-negative integer)
	 * @param threshold non-negative integer
	 * @param selection Area to filter. If null, the whole image is filtered.
	 * @pre inputImage.getWidth() == target.getWidth() and inputImage.getHeight()==target.getHeight()
	 * @pre selection != null =&gt; inputImage.inBounds(selection)
	 */
	public static void meanFilter(RasterImage inputImage, RasterImage target,
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
		meanFilter(inputImage, target, selection, radius, threshold, 0);
		meanFilter(inputImage, target, selection, radius, threshold, 8);
		meanFilter(inputImage, target, selection, radius, threshold, 16);
	}

	/**
	 * Mean filter. Must be used separately for each RGB component. Bit offset
	 * specifies the RGB component. If the area specified by <code>selection</code>
	 * is closer to one of the image sides than <code>radius</code>, the area will be clipped.
	 * 
	 * @param inputImage The original image, cannot be null
	 * @param target The target image, cannot be null
	 * @param selection Area to filter. If null, the whole image is filtered.
	 * @param radius filtering radius (non-negative integer)
	 * @param threshold non-negative integer
	 * @param offset 0 for blue, 8 for green, 16 for red
	 * @pre inputImage != null and target != null and target != inputImage and inputImage.pixels != target.pixels
	 * @pre class invariants of inputImage and target are true
	 * @pre inputImage.getWidth() == target.getWidth() and inputImage.getHeight()==target.getHeight()
	 * @pre inputImage.getWidth() &gt;= 2*r+1 and inputImage.getHeight() &gt;= 2*r+1
	 * @pre selection != null =&gt; inputImage.inBounds(selection)
	 * @post Does not modify <code>selection</code> and <code>inputImage</code>
	 * @post dimensions of target are not changed; [old target.pixels] == target.pixels
	 */
	private static void meanFilter(RasterImage inputImage, RasterImage target,
		Rectangle selection, int radius, int threshold, int offset) {
		//System.out.println("private meanFilter");
		if (selection==null) selection=new Rectangle(0, 0, inputImage.getWidth(), inputImage.getHeight());
		assert !(inputImage==null || target==null || radius<0 ||
			inputImage==target || (offset!=0 && offset!=8 && offset!=16) ||
			inputImage.getWidth()<2*radius+1 || inputImage.getHeight()<2*radius+1 ||
			inputImage.getWidth()!=target.getWidth() || inputImage.getHeight()!=target.getHeight() || threshold<0 ||
			!inputImage.inBounds(selection)
		);

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
		selection=null;
		int size=(2*radius+1), selSize=selWidth*selHeight, aptSize=size*size;
		int sum [] = new int [selWidth + (radius << 1)];

		//init first row
		//init sums of first (2*radius+1) elements for (selWidth+2*radius) columns
		for (int rowIndex=0, rowStart=selX-radius+width*(selY-radius); rowIndex<2*radius+1; rowIndex++, rowStart+=width) {
			for (int sumIndex=0, index=rowStart; sumIndex<sum.length; sumIndex++, index++) {
				int value=(inputImage.getRGB(index) >> offset) & 255;
				sum[sumIndex]+=value;
			}
		}//end init sums of first (2*radius+1) elements for (selWidth+2*radius) columns
		int curSum=0;
		for (int i=0; i<size; i++) {
			curSum+=sum[i];
		}
		int curAvg=curSum/aptSize;
		int destPos=selY*width+selX;
		int oldValue=(target.getRGB(destPos) >> offset) & 255;
		//init first column of the first row
		if (Math.abs(curAvg-oldValue)>threshold) {
			target.setRGB(destPos, target.getRGB(destPos) & (~(255 << offset)));
			target.setRGB(destPos, target.getRGB(destPos) | ((curAvg << offset)));
		}//end init first column of the first row
		//init the rest of columns of the first row
		destPos++;
		for (int j=selX+1, subIndex=0, addIndex=size; j<selX+selWidth; j++, subIndex++, addIndex++, destPos++) {
			curSum+=(sum[addIndex]-sum[subIndex]);
			curAvg=curSum/aptSize;
			oldValue=(target.getRGB(destPos) >> offset) & 255;
			if (Math.abs(curAvg-oldValue)>threshold) {
				target.setRGB(destPos, target.getRGB(destPos) & (~(255 << offset)));
				target.setRGB(destPos, target.getRGB(destPos) | ((curAvg << offset)));
			}
		}//end init the rest of columns of the first row
		//end init first row

		//process remaining rows
		for (int i=selY+1; i<selY+selHeight; i++) {
			//update sums of first (2*radius+1) elements for (selWidth+2*radius) columns
			for (int sumIndex=0, subIndex=selX-radius+width*(i-radius-1), addIndex=selX-radius+width*(i+radius);
				sumIndex<selWidth+2*radius; sumIndex++, subIndex++, addIndex++) {
				int subValue=(inputImage.getRGB(subIndex) >> offset) & 255;
				int addValue=(inputImage.getRGB(addIndex) >> offset) & 255;
				sum[sumIndex]+=addValue-subValue;
			}//end update sums of first (2*radius+1) elements for (selWidth+2*radius) columns
			curSum=0;
			for (int k=0; k<size; k++) {
				curSum+=sum[k];
			}
			curAvg=curSum/aptSize;
			destPos=i*width+selX;
			oldValue=(target.getRGB(destPos) >> offset) & 255;
			if (Math.abs(curAvg-oldValue)>threshold) {
				target.setRGB(destPos, target.getRGB(destPos) & (~(255 << offset)));
				target.setRGB(destPos, target.getRGB(destPos) | ((curAvg << offset)));
			}
			destPos++;
			for (int j=selX+1, subIndex=0, addIndex=size; j<selX+selWidth; j++, subIndex++, addIndex++, destPos++) {
				curSum+=(sum[addIndex]-sum[subIndex]);
				curAvg=curSum/aptSize;
				oldValue=(target.getRGB(destPos) >> offset) & 255;
				if (Math.abs(curAvg-oldValue)>threshold) {
					target.setRGB(destPos, target.getRGB(destPos) & (~(255 << offset)));
					target.setRGB(destPos, target.getRGB(destPos) | ((curAvg << offset)));
				}
			}
		}
	}

	/**
	 * Variance filter. The inputImage and target may be the same. If the area
	 * specified by <code>selection</code> is closer to one of the image sides
	 * than <code>radius</code>, the area will be clipped.
	 *
	 * @param inputImage The original image (ownership: caller)
	 * @param target The target image (ownership: caller)
	 * @param meanFilteredImage The mean-filtered image with threshold=0. Can be null. (ownership: caller)
	 * @param radius filtering radius (must be &gt;= 0)
	 * @param threshold Only those pixels are replaced with the mean value, whose
	 *		value differs from the mean value by more than <code>threshold</code>. (must be &gt;= 0)
	 * @param selection Area to filter. If null, the whole image is filtered. (ownership: caller)
	 * @pre inputImage != null and target != null and inputImage.pixels != target.pixels
	 * @pre meanFilteredImage!= target and meanFilteredImage != inputImage
	 * @pre meanFilteredImage != null =&gt; meanFilteredImage.pixels != target.pixels
	 *		and meanFilteredImage.pixels != inputImage.pixels
	 * @pre class invariants of inputImage and target are true
	 * @pre meanFilteredImage!=null =&gt; class invariant of meanFilteredImage is true
	 * @pre inputImage.getWidth() == target.getWidth() and inputImage.getHeight()==target.getHeight()
	 * @pre selection != null =&gt; inputImage.inBounds(selection)
	 * @post Does not modify <code>selection</code>
	 * @post inputImage!=target =&gt; <code>inputImage</code> will not be mofified
	 * @post meanFilteredImage will not be modified
	 * @post class invariant of target is true
	 * @post dimensions of target are not changed
	 * @author Jevgenijs Jonass.
	 * @since 21.05.2009
	 */
	public static void varianceFilter(RasterImage inputImage, RasterImage target, RasterImage meanFilteredImage,
		int radius, int threshold, Rectangle selection) {
		if (selection==null) selection=new Rectangle(0, 0, inputImage.getWidth(), inputImage.getHeight());
		if (meanFilteredImage==null) {
			meanFilteredImage=new RasterImage(inputImage.getWidth(), inputImage.getHeight());
			meanFilter(inputImage, meanFilteredImage, radius, 0, selection);
		}
		assert !(
			inputImage==null || target==null || radius<0 ||
			inputImage.getWidth()!=target.getWidth() || inputImage.getHeight()!=target.getHeight() || threshold<0 ||
			!inputImage.inBounds(selection)
		);

		int width=inputImage.getWidth(), height=inputImage.getHeight();
		if (width<2*radius+1 || height<2*radius+1) {
			if (inputImage!=target) inputImage.copyTo(target);
			return;
		}
		if (inputImage==target) {
			inputImage=new RasterImage(inputImage);
		}

	//	target.getWidth()=width;
	//	target.getHeight()=height;
		varianceFilter(inputImage, target, meanFilteredImage, selection, radius, threshold, 0);
		varianceFilter(inputImage, target, meanFilteredImage, selection, radius, threshold, 8);
		varianceFilter(inputImage, target, meanFilteredImage, selection, radius, threshold, 16);
	}

	/**
	 * Variance filter. Must be used separately for each RGB component. Bit offset
	 * specifies the RGB component. If the area specified by <code>selection</code>
	 * is closer to one of the image sides than <code>radius</code>, the area will be clipped.
	 *
	 * @param inputImage The original image (ownership: caller)
	 * @param target The target image (ownership: caller)
	 * @param meanFilteredImage The mean-filtered image with threshold=0. Cannot be null. (ownership: caller)
	 * @param selection Area to filter. If null, the whole image is filtered. (ownership: caller)
	 * @param radius filtering radius (must be &gt;= 0)
	 * @param threshold Only those pixels are replaced with the mean value, whose
	 *		value differs from the mean value by more than <code>threshold</code>. (must be &gt;= 0)
	 * @param offset Bit offset (0, 8 or 16).
	 * @pre inputImage != null and target != null and target != inputImage and inputImage.pixels != target.pixels
	 * @pre class invariants of inputImage, meanFilteredImage and target are true
	 * @pre inputImage.getWidth() == target.getWidth() and inputImage.getHeight()==target.getHeight()
	 * @pre inputImage.getWidth() &gt;= 2*r+1 and inputImage.getHeight() &gt;= 2*r+1
	 * @pre selection != null =&gt; inputImage.inBounds(selection)
	 * @post Does not modify <code>selection</code>, meanFilteredImage and <code>inputImage</code>
	 * @post class invariant of target is true
	 * @post dimensions of target are not changed; [old target.pixels] == target.pixels
	 * @author Jevgenijs Jonass.
	 * @since 21.05.2009
	 */
	private static void varianceFilter(RasterImage inputImage, RasterImage target, RasterImage meanFilteredImage,
		Rectangle selection, int radius, int threshold, int offset) {
		//System.out.println("private varianceFilter");
		if (selection==null) selection=new Rectangle(0, 0, inputImage.getWidth(), inputImage.getHeight());
		assert !(inputImage==null || target==null || meanFilteredImage==null || radius<0 ||
			inputImage==target || (offset!=0 && offset!=8 && offset!=16) ||
			inputImage.getWidth()<2*radius+1 || inputImage.getHeight()<2*radius+1 ||
			inputImage.getWidth()!=target.getWidth() || inputImage.getHeight()!=target.getHeight() || threshold<0 ||
			!inputImage.inBounds(selection)
		);

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
		selection=null;
		int size=(2*radius+1), selSize=selWidth*selHeight, aptSize=size*size;
		int sum [] = new int [selWidth + (radius << 1)];

		//init first row
		//init sums of first (2*radius+1) elements for (selWidth+2*radius) columns
		for (int rowIndex=0, rowStart=selX-radius+width*(selY-radius); rowIndex<2*radius+1; rowIndex++, rowStart+=width) {
			for (int sumIndex=0, index=rowStart; sumIndex<sum.length; sumIndex++, index++) {
				int value=(inputImage.getRGB(index) >> offset) & 255;
				sum[sumIndex]+=value*value;
			}
		}//end init sums of first (2*radius+1) elements for (selWidth+2*radius) columns
		int curSum=0;
		for (int i=0; i<size; i++) {
			curSum+=sum[i];
		}
		int curAvg=curSum/aptSize;
		int destPos=selY*width+selX;
		//
		int meanValue=(meanFilteredImage.getRGB(destPos) >> offset) & 255;
		int newValue=curAvg-meanValue*meanValue;
		if (newValue<0) newValue=0;	else if (newValue>255) newValue=255;
		//
		int oldValue=(target.getRGB(destPos) >> offset) & 255;
		//init first column of the first row
		if (Math.abs(newValue-oldValue)>threshold) {
			target.setRGB(destPos, target.getRGB(destPos) & (~(255 << offset)));
			target.setRGB(destPos, target.getRGB(destPos) | ((newValue << offset)));
		}//end init first column of the first row
		//init the rest of columns of the first row
		destPos++;
		for (int j=selX+1, subIndex=0, addIndex=size; j<selX+selWidth; j++, subIndex++, addIndex++, destPos++) {
			curSum+=(sum[addIndex]-sum[subIndex]);
			curAvg=curSum/aptSize;
			//
			meanValue=(meanFilteredImage.getRGB(destPos) >> offset) & 255;
			newValue=curAvg-meanValue*meanValue;
			if (newValue<0) newValue=0;	else if (newValue>255) newValue=255;
			//
			oldValue=(target.getRGB(destPos) >> offset) & 255;
			if (Math.abs(newValue-oldValue)>threshold) {
				target.setRGB(destPos, target.getRGB(destPos) & (~(255 << offset)));
				target.setRGB(destPos, target.getRGB(destPos) | ((newValue << offset)));
			}
		}//end init the rest of columns of the first row
		//end init first row

		//process remaining rows
		for (int i=selY+1; i<selY+selHeight; i++) {
			//update sums of first (2*radius+1) elements for (selWidth+2*radius) columns
			for (int sumIndex=0, subIndex=selX-radius+width*(i-radius-1), addIndex=selX-radius+width*(i+radius);
				sumIndex<selWidth+2*radius; sumIndex++, subIndex++, addIndex++) {
				int subValue=(inputImage.getRGB(subIndex) >> offset) & 255;
				int addValue=(inputImage.getRGB(addIndex) >> offset) & 255;
				sum[sumIndex]+=addValue*addValue-subValue*subValue;
			}//end update sums of first (2*radius+1) elements for (selWidth+2*radius) columns
			curSum=0;
			for (int k=0; k<size; k++) {
				curSum+=sum[k];
			}
			curAvg=curSum/aptSize;
			destPos=i*width+selX;
			//
			meanValue=(meanFilteredImage.getRGB(destPos) >> offset) & 255;
			newValue=curAvg-meanValue*meanValue;
			if (newValue<0) newValue=0;	else if (newValue>255) newValue=255;
			//
			oldValue=(target.getRGB(destPos) >> offset) & 255;
			if (Math.abs(newValue-oldValue)>threshold) {
				target.setRGB(destPos, target.getRGB(destPos) & (~(255 << offset)));
				target.setRGB(destPos, target.getRGB(destPos) | ((newValue << offset)));
			}
			destPos++;
			for (int j=selX+1, subIndex=0, addIndex=size; j<selX+selWidth; j++, subIndex++, addIndex++, destPos++) {
				curSum+=(sum[addIndex]-sum[subIndex]);
				curAvg=curSum/aptSize;
				meanValue=(meanFilteredImage.getRGB(destPos) >> offset) & 255;
				newValue=curAvg-meanValue*meanValue;
				if (newValue<0) newValue=0;	else if (newValue>255) newValue=255;
				oldValue=(target.getRGB(destPos) >> offset) & 255;
				if (Math.abs(newValue-oldValue)>threshold) {
					target.setRGB(destPos, target.getRGB(destPos) & (~(255 << offset)));
					target.setRGB(destPos, target.getRGB(destPos) | ((newValue << offset)));
				}
			}
		}
	}
}
