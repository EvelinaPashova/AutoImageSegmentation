
package lv.lumii.pixelmaster.modules.transform.domain;

import java.awt.Rectangle;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author 
 */
public final class Crop {

	/**
	 * Crops the image. Allocates memory for the resulting image.
	 * @param inputImage The original image (ownership: caller)
	 * @param selection area that needs to be cropped (ownership: caller)
	 * @pre inputImage != null and selection != null
	 * @pre inputImage.invariant()==true
	 * @pre selection is not smaller that 1x1 square
	 * @pre selection fits into image bounds
	 * @post inputImage will not be modified
	 * @post (returned image).invariant()==true
	 * @return cropped image of the same size as selection
	 */
	public static RasterImage crop(RasterImage inputImage, Rectangle selection) {
		assert !(inputImage==null || selection==null ||
			selection.x<0 || selection.x>=inputImage.getWidth() || selection.y<0 || selection.y>=inputImage.getHeight() ||
			selection.width<1 || selection.height<1 ||
			selection.x+selection.width>inputImage.getWidth() || selection.y+selection.height>inputImage.getHeight());
		RasterImage tmp=new RasterImage(selection.width, selection.height);
		for (int i=0, srcPos=selection.x+selection.y*inputImage.getWidth(), destPos=0;
			i<selection.height; i++, srcPos+=inputImage.getWidth(), destPos+=tmp.getWidth())
			inputImage.copyTo(tmp, srcPos, destPos, tmp.getWidth());
		return tmp;
	}
}
