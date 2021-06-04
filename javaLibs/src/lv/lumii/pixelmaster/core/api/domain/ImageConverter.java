
package lv.lumii.pixelmaster.core.api.domain;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * This class contains methods for conversion between the {@link RasterImage} and
 * {@link java.awt.image.BufferedImage} formats.
 *
 * @author Jevgeny Jonas
 */
public final class ImageConverter {

	private ImageConverter() { }

	/**
	 * <p>Converts <code>BufferedImage</code> to <code>RasterImage</code> of the same size. Alpha channel information will be discarded.</p>
	 *
	 * </p>Precondition: dimensions of the <code>inputImage</code> are not zero.</p>
	 *
	 * @param inputImage can have any size and type
	 *		(see {@link java.awt.image.BufferedImage#getType()}) , cannot be <code>null</code>
	 * @param target can have any size, cannot be <code>null</code>
	 */
	public static void convertBufImage(BufferedImage inputImage, RasterImage target) {
		assert !(inputImage==null || target==null || inputImage.getWidth()<1 || inputImage.getHeight()<1);
		target.resize(inputImage.getWidth(), inputImage.getHeight());

		/*
		 * !! Workaround !!
		 *
		 * bug id: [001]
		 *
		 * The following code is necessary to work around a bug
		 * in BufferedImage.getRGB that appears when image has
		 * type BufferedImage.TYPE_BYTE_GRAY.
		 */
		if (inputImage.getType()==BufferedImage.TYPE_BYTE_GRAY) {
			WritableRaster raster=inputImage.getRaster();
			byte[] gray = (byte []) raster.getDataElements(
					0, 0, target.getWidth(), target.getHeight(), null);
			for (int i=0; i<target.getSize(); i++) {
				int grayInt=gray[i] & 0x000000ff;				// gray[i]==-40 => grayInt==216
				target.setRGB(i, RGB.getRGB(grayInt, grayInt, grayInt));
			}
		}

		else {
			for(int k=0; k<target.getHeight(); k++) {
				for(int i=0; i<target.getWidth(); i++) {
					int argb=inputImage.getRGB(i,k);
					target.setRGB(i, k, argb & 0x00ffffff);
				}
			}
		}
	}

	/**
	 * Converts <code>RasterImage</code> to <code>BufferedImage</code> of type {@link BufferedImage#TYPE_INT_RGB}.
	 * @return <code>BufferedImage</code> of the same size
	 */
	public static BufferedImage toBuffered(RasterImage image) {
		BufferedImage outputImage=null;
		outputImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

		for (int i=0, rowStart=0; i<image.getHeight(); i++, rowStart+=image.getWidth()) {
			for (int index=rowStart, j=0; j<image.getWidth(); j++, index++) {

				//loop invariant: index==i*width+j
				outputImage.setRGB(j, i, image.getRGB(index));
			}
		}

		return outputImage;
	}

	/**
	 * Converts <code>RasterImage</code> to <code>BufferedImage</code> of type BufferedImage.TYPE_BYTE_GRAY.
	 * Gray value for each pixel is calculated using default kr, kg and kb.
	 * @return <code>BufferedImage</code> of the same size
	 */
	public static BufferedImage toGrayscaleBuffered(RasterImage image) {
		BufferedImage outputImage=null;
		outputImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster=outputImage.getRaster();
		for (int i=0, rowStart=0; i<image.getHeight(); i++, rowStart+=image.getWidth()) {
			for (int index=rowStart, j=0; j<image.getWidth(); j++, index++) {
				//loop invariant: index==i*width+j
				int gray= RGB .intensity(image.getRGB(index));
				raster.setDataElements(j, i, new byte[] {(byte) gray});
				//outputImage.setRGB(j, i, RGB.getRGB(gray, gray, gray));
			}
		}
		return outputImage;
	}
}
