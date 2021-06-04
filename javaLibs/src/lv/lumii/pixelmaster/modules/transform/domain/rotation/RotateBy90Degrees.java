
package lv.lumii.pixelmaster.modules.transform.domain.rotation;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author 
 */
final public class RotateBy90Degrees {

	/**
	 * Rotates the image by 90 degrees anticlockwise.
	 * The inputImage and target may point to the same objects.
	 * @param inputImage The original image, cannot be null
	 * @param target The rotated image, cannot be null
	 * @pre inputImage.getSize() == target.getSize()
	 */
	public static void rotateACW(RasterImage inputImage, RasterImage target) {
		assert (inputImage != null && target != null && target.getSize() == inputImage.getSize());
		if (inputImage==target) inputImage=new RasterImage(inputImage);
		target.resize(inputImage.getHeight(), inputImage.getWidth());
		int newY;
		for (int i=0; i<inputImage.getHeight(); i++) {
			for (int j=0; j<inputImage.getWidth(); j++) {
				newY=inputImage.getWidth() - j - 1;
				target.setRGB(i, newY, inputImage.getRGB(j, i));
			}
		}
	}

	/**
	 * Rotates the image by 90 degrees clockwise.
	 * The inputImage and target may point to the same objects.
	 * @param inputImage The original image, cannot be null
	 * @param target The rotated image, cannot be null
	 * @pre inputImage.getSize() == target.getSize()
	 */
	public static void rotateCW(RasterImage inputImage, RasterImage target) {
		assert (target.getSize() == inputImage.getSize());
		if (inputImage==target) inputImage=new RasterImage(inputImage);
		target.resize(inputImage.getHeight(), inputImage.getWidth());
		int newX;
		for (int i=0; i<inputImage.getHeight(); i++) {
			newX=inputImage.getHeight()-i-1;
			for (int j=0; j<inputImage.getWidth(); j++) {
				target.setRGB(newX, j, inputImage.getRGB(j, i));
			}
		}
	}
}
