
package lv.lumii.pixelmaster.modules.transform.domain;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author 
 */
public final class Flip {

	/**
	 * Flips the image horizontally.
	 * The inputImage and target may point to the same objects.
	 * @param inputImage The original image, cannot be null
	 * @param target The flipped image, cannot be null
	 * @pre inputImage.getWidth()==target.getWidth() and inputImage.getHeight()==target.getHeight()
	 * @post inputImage.getWidth()==target.getWidth() and inputImage.getHeight()==target.getHeight()
	 */
	public static void horizontalFlip(RasterImage inputImage, RasterImage target) {
		assert !(inputImage==null || target==null);
		int width=inputImage.getWidth(), height=inputImage.getHeight();
		if (inputImage==target) {
			for(int i=0; i<height; i++) {
				for(int j=0; j<width/2; j++) {
					int index1=i*width+j;
					int index2=(i+1)*width-j-1;
					int tmp=inputImage.getRGB(index1);
					inputImage.setRGB(index1, inputImage.getRGB(index2));
					inputImage.setRGB(index2, tmp);
				}
			}
		}
		else {
			//target.getWidth()=width;
			//target.getHeight()=height;
			for(int i=0; i<target.getHeight(); i++) {
				for(int j=0; j<target.getWidth(); j++) {
					int index1=i*width+j;
					int index2=(i+1)*width-j-1;
					target.setRGB(index1, inputImage.getRGB(index2));
				}
			}
		}
	}

	/**
	 * Flips the image vertically.
	 * The inputImage and target may point to the same objects.
	 * @param inputImage The original image
	 * @param target The flipped image
	 * @pre inputImage.getWidth()==target.getWidth() and
	 *		inputImage.getHeight()==target.getHeight()
	 */
	public static void verticalFlip(RasterImage inputImage, RasterImage target) {
		assert !(inputImage==null || target==null);
		int width=inputImage.getWidth(), height=inputImage.getHeight();
		if (inputImage==target) {
			int[] row=new int[width];
			for(int i=0; i<height/2; i++) {
				inputImage.copyTo(row, i*width, 0, width);
				target.copyFrom(row, 0, (height-i-1)*width, width);
				target.copyTo(target, (height-i-1)*width, i*width, width);
			}
		}
		else {
			//target.getWidth()=width;
			//target.getHeight()=height;
			for(int i=0; i<height; i++) {
				inputImage.copyTo(target, i*width, (height-i-1)*width, width);
			}
		}
	}
}
