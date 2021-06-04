
package lv.lumii.pixelmaster.modules.transform.domain.rotation;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import static java.lang.Math.*;

final public class RotateByShear {
	private static void horizontalSkew(RasterImage src, RasterImage dst, int row, int iOffset, double weight, int bkcolor) {
		int iXPos;
		int src_width  = src.getWidth();
		int dst_width  = dst.getWidth();
		int pxlSrc, pxlLeft, pxlOldLeft;

		// background
		final int pxlBkg = bkcolor;

		// fill gap left of skew with background
		for(int k = 0; k < iOffset; k++) {
			dst.setRGB(k, row, bkcolor);
		}
		pxlOldLeft = bkcolor;

		for(int i = 0; i < src_width; i++) {
			// loop through row pixels
			pxlSrc = src.getRGB(i, row);
			// calculate weights

			pxlLeft = (int) ((pxlBkg & 255) + ((pxlSrc & 255) - (pxlBkg & 255)) * weight + 0.5);
			if (pxlLeft > 255) pxlLeft = 255;
			if (pxlLeft < 0) pxlLeft = 0;
			int g = (int) (((pxlBkg & 0xff00) >> 8) + (((pxlSrc & 0xff00) >> 8) - ((pxlBkg & 0xff00) >> 8)) * weight + 0.5);
			if (g > 255) g = 255;
			if (g < 0) g = 0;
			pxlLeft |= (g << 8);
			int r = (int) (((pxlBkg & 0xff0000) >> 16) + (((pxlSrc & 0xff0000) >> 16) - ((pxlBkg & 0xff0000) >> 16)) * weight + 0.5);
			if (r > 255) r = 255;
			if (r < 0) r = 0;
			pxlLeft |= (r << 16);

			// check boundaries
			iXPos = i + iOffset;
			if((iXPos >= 0) && (iXPos < dst_width)) {
				// update left over on source
				int newPixel = (pxlSrc & 255) - ((pxlLeft & 255) - (pxlOldLeft & 255));
				if (newPixel > 255) newPixel = 255;
				if (newPixel < 0) newPixel = 0;
				g = ((pxlSrc & 0xff00) >> 8) - (((pxlLeft & 0xff00) >> 8) - ((pxlOldLeft & 0xff00) >> 8));
				if (g > 255) g = 255;
				if (g < 0) g = 0;
//				if (g < 0) {
//					System.out.println(g);
//					g = 0;
//				}
				newPixel |= (g << 8);
				r = ((pxlSrc & 0xff0000) >> 16) - (((pxlLeft & 0xff0000) >> 16) - ((pxlOldLeft & 0xff0000) >> 16));
				if (r > 255) r = 255;
				if (r < 0) r = 0;
				newPixel |= (r << 16);

				dst.setRGB(iXPos, row, newPixel);
			}
			// save leftover for next pixel in scan
			pxlOldLeft = pxlLeft;
		}

		// go to rightmost point of skew
		iXPos = src_width + iOffset;

		if((iXPos >= 0) && (iXPos < dst_width)) {

			// If still in image bounds, put leftovers there
			dst.setRGB(iXPos, row, pxlOldLeft);

			// clear to the right of the skewed line with background
			for(int i = 0; i < dst_width - iXPos - 1; i++) {
				dst.setRGB(i + iXPos + 1, row, bkcolor);
			}
		}
	}

	private static void verticalSkew(RasterImage src, RasterImage dst, int col, int iOffset, double weight, int bkcolor) {
		int iYPos;
		int src_height  = src.getHeight();
		int dst_height  = dst.getHeight();
		int pxlSrc, pxlLeft, pxlOldLeft;

		// background
		final int pxlBkg = bkcolor;

		// fill gap above skew with background
		for(int k = 0; k < iOffset; k++) {
			dst.setRGB(col, k, bkcolor);
		}
		pxlOldLeft = bkcolor;

		for(int i = 0; i < src_height; i++) {
			// loop through column pixels
			pxlSrc = src.getRGB(col, i);
			
			// calculate weights

			pxlLeft = (int) ((pxlBkg & 255) + ((pxlSrc & 255) - (pxlBkg & 255)) * weight + 0.5);
			if (pxlLeft > 255) pxlLeft = 255;
			if (pxlLeft < 0) pxlLeft = 0;
			int g = (int) (((pxlBkg & 0xff00) >> 8) + (((pxlSrc & 0xff00) >> 8) - ((pxlBkg & 0xff00) >> 8)) * weight + 0.5);
			if (g > 255) g = 255;
			if (g < 0) g = 0;
			pxlLeft |= (g << 8);
			int r = (int) (((pxlBkg & 0xff0000) >> 16) + (((pxlSrc & 0xff0000) >> 16) - ((pxlBkg & 0xff0000) >> 16)) * weight + 0.5);
			if (r > 255) r = 255;
			if (r < 0) r = 0;
			pxlLeft |= (r << 16);

			// check boundaries
			iYPos = i + iOffset;
			if((iYPos >= 0) && (iYPos < dst_height)) {
				// update left over on source
				int newPixel = (pxlSrc & 255) - ((pxlLeft & 255) - (pxlOldLeft & 255));
				if (newPixel > 255) newPixel = 255;
				if (newPixel < 0) newPixel = 0;
				g = ((pxlSrc & 0xff00) >> 8) - (((pxlLeft & 0xff00) >> 8) - ((pxlOldLeft & 0xff00) >> 8));
				if (g > 255) g = 255;
				if (g < 0) g = 0;
//				if (g < 0) {
//					System.out.println(g);
//					g = 0;
//				}
				newPixel |= (g << 8);
				r = ((pxlSrc & 0xff0000) >> 16) - (((pxlLeft & 0xff0000) >> 16) - ((pxlOldLeft & 0xff0000) >> 16));
				if (r > 255) r = 255;
				if (r < 0) r = 0;
				newPixel |= (r << 16);

				dst.setRGB(col, iYPos, newPixel);
			}
			// save leftover for next pixel in scan
			pxlOldLeft = pxlLeft;
		}

		// go to bottom point of skew
		iYPos = src_height + iOffset;

		if((iYPos >= 0) && (iYPos < dst_height)) {

			// If still in image bounds, put leftovers there
			dst.setRGB(col, iYPos, pxlOldLeft);

			// clear to the right of the skewed line with background
			for(int i = 0; i < dst_height - iYPos - 1; i++) {
				dst.setRGB(col, i + iYPos + 1, bkcolor);
			}
		}
	}

	 public static RasterImage rotate(RasterImage src, double dAngle, int bkcolor) {
		final double ROTATE_PI = 3.1415926535897932384626433832795;
		double dRadAngle = dAngle * ROTATE_PI / 180; // Angle in radians
		double dSinE = sin(dRadAngle);
		double dTan = tan(dRadAngle / 2);
		int src_width  = src.getWidth();
		int src_height = src.getHeight();

		// Calc first shear (horizontal) destination image dimensions
		int width_1  = src_width + (int) (src_height * abs(dTan) + 0.5);
		int height_1 = src_height;

		// Perform 1st shear (horizontal)
		// ----------------------------------------------------------------------

		// Allocate image for 1st shear
		RasterImage dst1 = new RasterImage(width_1, height_1);

		for(int u = 0; u < height_1; u++) {
			double dShear;

			if(dTan >= 0)	{
				// Positive angle
				dShear = (u + 0.5) * dTan;
			}
			else {
				// Negative angle
				dShear = (u - height_1 + 0.5) * dTan;
			}
			int iShear = (int) (floor(dShear));
			horizontalSkew(src, dst1, u, iShear, dShear - iShear, bkcolor);
		}
//return dst1;

		// Perform 2nd shear  (vertical)
		// ----------------------------------------------------------------------

		// Calc 2nd shear (vertical) destination image dimensions
		int width_2  = width_1;
		int height_2 = (int) (src_width * abs(dSinE) + (double)src_height * cos(dRadAngle) + 0.5) + 1;

		// Allocate image for 2nd shear
		RasterImage dst2 = new RasterImage(width_2, height_2);

		double dOffset;     // Variable skew offset
		if(dSinE > 0)	{
			// Positive angle
			dOffset = (src_width - 1.0) * dSinE;
		}
		else {
			// Negative angle
			dOffset = -dSinE * (src_width - width_2);
		}

		for(int u = 0; u < width_2; u++, dOffset -= dSinE) {
			int iShear = (int) (floor(dOffset));
			verticalSkew(dst1, dst2, u, iShear, dOffset - iShear, bkcolor);
		}

		// Perform 3rd shear (horizontal)
		// ----------------------------------------------------------------------

		// Calc 3rd shear (horizontal) destination image dimensions
		int width_3  = (int) (src_height * abs(dSinE) + src_width * cos(dRadAngle) + 0.5) + 1;
		int height_3 = height_2;

		// Allocate image for 3rd shear
		RasterImage dst3 = new RasterImage(width_3, height_3);

		if(dSinE >= 0) {
			// Positive angle
			dOffset = (src_width - 1.0) * dSinE * -dTan;
		}
		else {
			// Negative angle
			dOffset = dTan * ( (src_width - 1.0) * -dSinE + (1.0 - height_3) );
		}
		for(int u = 0; u < height_3; u++, dOffset += dTan) {
			int iShear = (int) (floor(dOffset));
			horizontalSkew(dst2, dst3, u, iShear, dOffset - iShear, bkcolor);
		}

		// Return result of 3rd shear
		return dst3;
	}
}
