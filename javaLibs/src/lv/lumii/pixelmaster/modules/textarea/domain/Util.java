
package lv.lumii.pixelmaster.modules.textarea.domain;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 * This class contains methods that were previously in the class ImageProcessor.
 */
public class Util {

	public static RasterImage drawTextBoxes(RasterImage inputImage) {
		assert !(inputImage == null);
		BufferedImage tempImage = ImageConverter.toBuffered(inputImage);
		GetImageText myGet = new GetImageText (tempImage);
		LinkedList boxes = myGet.getTextBoxes();
		tempImage = myGet.isolateText(boxes);
		RasterImage outputImage = new RasterImage(inputImage.getWidth(), inputImage.getHeight());
		ImageConverter.convertBufImage(tempImage, outputImage);
		return outputImage;
	}

	/**
	 * Temp function.
	 * @param inputImage The original image (ownership: caller)
	 * @pre inputImage != null
	 * @pre inputImage.invariant()==true
	 * @post inputImage will not be modified
	 * @post (returned image).invariant()==true
	 * @post (returned image).getWidth()==inputImage.getWidth() and (returned image).getHeight()==inputImage.getHeight()
	 * @return The resulting RasterImage (ownership: caller)
	 * @author Ainars Kumpins.
	 * @since 23.03.2009
	 */
	public static RasterImage drawContrast (RasterImage inputImage) {
		assert !(inputImage == null);
		BufferedImage tempImage = ImageConverter.toBuffered(inputImage);
		GetImageText myGet = new GetImageText (tempImage);
		int[][] contrast = myGet.getContrast();
		RasterImage outputImage = new RasterImage(inputImage.getWidth(), inputImage.getHeight());
	//	outputImage.pixels = new int[outputImage.getHeight() * outputImage.getWidth()];	//lieks


		for ( int height = 0; height < outputImage.getHeight(); height++) {
			for ( int width = 0; width < outputImage.getWidth(); width++ ) {
				int coordinate = outputImage.getWidth() * height + width;
				outputImage.setRGB(coordinate, contrast[width][height] * 0xffffff);
				}
		}
		return outputImage;
	}
}
