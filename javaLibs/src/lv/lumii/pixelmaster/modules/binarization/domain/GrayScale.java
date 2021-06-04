
package lv.lumii.pixelmaster.modules.binarization.domain;

import lv.lumii.pixelmaster.core.api.domain.RGB;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author 
 */
public final class GrayScale {

	/**
	 * Creates grey shaded image using default kr, kg and kb.
	 * The inputImage and target may point to the same objects.
	 *
	 * @param inputImage The original image
	 * @param target The grey-shaded image
	 * @pre inputImage.getWidth()==target.getWidth() and inputImage.getHeight()==target.getHeight()
	 */
	public static void greyImage(RasterImage inputImage, RasterImage target) {
		greyImage(inputImage, target, RGB.DEFAULT_RED, RGB.DEFAULT_GREEN, RGB.DEFAULT_BLUE);
	}

	/**
	 * Creates grey shaded image.
	 * The inputImage and target may point to the same objects.
	 * @param inputImage The original image
	 * @param target The grey-shaded image
	 * @param kr Coefficient of red channel (0..100)
	 * @param kg Coefficient of green channel (0..100)
	 * @param kb Coefficient of blue channel (0..100)
	 * @pre kr+kg+kb=100
	 * @pre inputImage.getWidth()==target.getWidth() and inputImage.getHeight()==target.getHeight()
	 */
	public static void greyImage(RasterImage inputImage, RasterImage target, int kr, int kg, int kb) {
		assert !(inputImage==null || target==null || kr<0 || kr>100 || kg<0 || kg>100 || kb<0 || kb>100 || kr+kg+kb!=100);
		//target.getWidth()=inputImage.getWidth();
		//target.getHeight()=inputImage.getHeight();
		int size=inputImage.getHeight()*inputImage.getWidth();
		for (int i=0; i<size; i++) {
			int gray=RGB.intensity(inputImage.getRGB(i), kr, kg, kb);
			target.setRGB(i, RGB.getRGB(gray, gray, gray));
		}
	}
}
