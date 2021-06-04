
package lv.lumii.pixelmaster.core.api.domain;

import java.util.Random;

/**
 * This class contains methods for manipulation with the RGB color model.
 *
 * @author Jevgeny Jonas
 */
public final class RGB {

	/** Default red coefficient */
	public static final int DEFAULT_RED = 30;

	/** Default green coefficient */
	public static final int DEFAULT_GREEN = 59;

	/** Default blue coefficient */
	public static final int DEFAULT_BLUE = 11;

	/**
	 * Gets the red component of the pixel color
	 * @param rgb Has format 0xaarrggbb or 0x00rrggbb.
	 * @return The red component (0..255)
	 */
	public static int getR(int rgb) { return (rgb >> 16) & 255; }

	/**
	 * Gets the green component of the pixel color
	 * @param rgb Has format 0xaarrggbb or 0x00rrggbb
	 * @return The green component (0..255)
	 */
	public static int getG(int rgb) { return (rgb >> 8) & 255; }

	/**
	 * Gets the blue component of the pixel color
	 * @param rgb Has format 0xaarrggbb or 0x00rrggbb
	 * @return The blue component (0..255)
	 */
	public static int getB(int rgb) { return rgb & 255; }

	/**
	 * Returns an integer pixel in RGB color model BufferedImage.TYPE_INT_RGB
	 * with specified red, green and blue component values
	 * @param r The red component (0..255)
	 * @param g The green component (0..255)
	 * @param b The blue component (0..255)
	 * @return The pixel color in format 0x00rrggbb
	 */
	public static int getRGB(int r, int g, int b) { return b + (g<<8) + (r<<16); }

	/**
	 * Calculates intensity of the pixel using default kr, kg and kb
	 * @param rgb pixel value (has format 0xaarrggbb or 0x00rrggbb)
	 * @return intensity (lies in range [0..255])
	 */
	public static int intensity(int rgb) { return intensity(rgb, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE); }

	/**
	 * Calculates intensity of the pixel. Precondition: <code>kr + kg + kb == 100</code>.
	 *
	 * @param rgb pixel value (has format 0xaarrggbb or 0x00rrggbb)
	 * @param kr Coefficient of red channel (0..100)
	 * @param kg Coefficient of green channel (0..100)
	 * @param kb Coefficient of blue channel (0..100)
	 * @return intensity (lies in range [0..255])
	 */
	public static int intensity(int rgb, int kr, int kg, int kb) {
		assert !(kr<0 || kr>100 || kg<0 || kg>100 || kb<0 || kb>100 || kr+kg+kb!=100);
		int r=RGB.getR(rgb);
		int g=RGB.getG(rgb);
		int b=RGB.getB(rgb);
		int c=kr*r+kg*g+kb*b;
		return c/100;
	}

	/**
	 * Returns random RGB color.
	 * @author Ainārs Kumpiņš
	 * @return color in format 0x00rrggbb
	 */
	public static int getRandomColor() {
		Random numGen = new Random();
		return RGB.getRGB(numGen.nextInt(256), numGen.nextInt(256), numGen.nextInt(256));
	}
}
