
package lv.lumii.pixelmaster.modules.transform.domain.resizing;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 * Class containing spline algorithms.
 * @author Jevgenijs Jonass
 */
public final class Spline {

	/**
	 * Resamples the image using bilinear spline algorithm.
	 * @param inputImage input image
	 * @param newwidth new width (must be &gt;1)
	 * @param newheight new height (must be &gt;1)
	 * @return The resulting image
	 * @see <a href="http://alglib.sources.ru/interpolation/spline2d.php">Двухмерная сплайн-интерполяция</a>
	 */
	public static RasterImage resizeImage(RasterImage inputImage, int newwidth, int newheight) {
		assert !(inputImage==null || inputImage.getWidth()<=1 || inputImage.getHeight()<=1);
		assert !(newwidth<=1 || newheight<=1);
		RasterImage dest=new RasterImage(newwidth, newheight);
	//	Arrays.fill(dest.pixels, 0);
		bilinearResampleCartesian(inputImage, dest, 0);
		bilinearResampleCartesian(inputImage, dest, 8);
		bilinearResampleCartesian(inputImage, dest, 16);
		return dest;
	}

	/**
	 * Bilinear interpolation algorithm. Function must be used separately
	 * for each RGB component, which is specified by <code>offset</code>.
	 * Bits that correspond to the specified rgb component in the target
	 * array can be uninitialized.
	 *
	 * @param source source image, both dimensions must be greater than 1
	 * @param target target image, both dimensions must be greater than 1
	 * @param offset
	 * @pre target != source
	 * @see <a href="http://alglib.sources.ru/interpolation/spline2d.php">Двухмерная сплайн-интерполяция</a>
	 */
	private static void bilinearResampleCartesian(
		RasterImage source, RasterImage target, int offset) {
		assert offset == 0 || offset == 8 || offset == 16;
		assert source != null && target != null && source != target;
		assert source.getHeight() > 1 && source.getWidth() > 1 &&
			target.getHeight() > 1 && target.getWidth() > 1;

		int i, j, l, c;
		float t, u;

		for (i=0; i<target.getHeight(); i++) {
			for (j=0; j<target.getWidth(); j++) {
				l=i*(source.getHeight()-1)/(target.getHeight()-1);
				if(l==source.getHeight()-1) l = source.getHeight()-2;
				u=(float)i/(float)(target.getHeight()-1)*(source.getHeight()-1)-l;
				c=j*(source.getWidth()-1)/(target.getWidth()-1);
				if(c==source.getWidth()-1) c=source.getWidth()-2;
				t=(float)(j*(source.getWidth()-1))/(float)(target.getWidth()-1)-c;

				int v1=(source.getRGB(c, l)>>offset) & 255;
				int v2=(source.getRGB(c + 1, l)>>offset) & 255;
				int v3=(source.getRGB(c + 1, l + 1)>>offset) & 255;
				int v4=(source.getRGB(c, l + 1)>>offset) & 255;

				/*
				int v1=(a[l*source.getWidth()+c]>>offset) & 255;
				int v2=(a[l*source.getWidth()+c+1]>>offset) & 255;
				int v3=(a[(l+1)*source.getWidth()+c+1]>>offset) & 255;
				int v4=(a[(l+1)*source.getWidth()+c]>>offset) & 255;
				*/
				int value=(int)((1-t)*(1-u)*v1+t*(1-u)*v2+t*u*v3+(1-t)*u*v4);
				if (value>255) value=255; else if (value<0) value=0;

				// init byte to 0
				target.setRGB(j, i, target.getRGB(j, i) & (~(255 << offset)));

				// assign byte value
				target.setRGB(j, i, target.getRGB(j, i) | ((value)<<offset));
			}
		}
	}
}
