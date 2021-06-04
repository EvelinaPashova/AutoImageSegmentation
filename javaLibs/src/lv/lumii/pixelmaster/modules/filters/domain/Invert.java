
package lv.lumii.pixelmaster.modules.filters.domain;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author 
 */
public final class Invert {

	/**
	 * Converts image to its negative.
	 * The inputImage and target may point to the same objects.
	 *
	 * @param inputImage The original image (ownership: caller)
	 * @param target The negative image (ownership: caller)
	 * @pre inputImage != null and target != null
	 * @pre inputImage.invariant()==true
	 * @pre target.invariant()==true
	 * @pre inputImage.getWidth()==target.getWidth() and inputImage.getHeight()==target.getHeight()
	 * @post inputImage.invariant()==true
	 * @post target.invariant()==true
	 * @post inputImage.getWidth()==target.getWidth() and inputImage.getHeight()==target.getHeight()
	 * @post inputImage != target &gt;= inputImage will not be modified
	 */
	public static void invertColors(RasterImage inputImage, RasterImage target) {
		//target.getWidth()=inputImage.getWidth();
		//target.getHeight()=inputImage.getHeight();
		int size=inputImage.getHeight()*inputImage.getWidth();
		for(int k=0; k<size; k++) {
			target.setRGB(k, 0x00ffffff-inputImage.getRGB(k));
		}
	}
}
