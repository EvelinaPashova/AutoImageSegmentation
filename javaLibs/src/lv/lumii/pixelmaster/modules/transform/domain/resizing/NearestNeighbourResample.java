
package lv.lumii.pixelmaster.modules.transform.domain.resizing;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author 
 */
public final class NearestNeighbourResample {

	/**
	 * Stretches image horizontally and vertically.
	 * Allocates memory for the resulting image.
	 *
	 * @param inputImage The original image
	 * @param kx horizontal coefficient. Must be &gt;0
	 * @param ky vertical coefficient. Must be &gt;0
	 * @return The stretched image
	 */
	public static RasterImage nnResample(RasterImage inputImage, int kx, int ky) {
		assert !(inputImage==null || kx<1|| ky<1);
		int newWidth=kx*inputImage.getWidth(), newHeight=ky*inputImage.getHeight();
		RasterImage zoomIm = new RasterImage(newWidth, newHeight);
		for (int i=0, inputRowStart=0, destRowStart=0; i<inputImage.getHeight();
			i++, inputRowStart+=inputImage.getWidth(), destRowStart+=ky*zoomIm.getWidth()) {
			for (int j=0, inputIndex=inputRowStart, destIndex1=destRowStart; j<inputImage.getWidth();
				j++, inputIndex++, destIndex1+=kx) {
				int rgb = inputImage.getRGB(inputIndex);
				for (int k=0, destIndex2=destIndex1; k<ky; k++, destIndex2+=zoomIm.getWidth()) {
					zoomIm.fill(rgb, destIndex2, destIndex2+kx);
				}
			}
		}
		return zoomIm;
	}
}
