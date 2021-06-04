package lv.lumii.pixelmaster.modules.ridge.domain;
import java.util.Random;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;


public class Image {
    private int width; //image width
    private int height; //image height
    public int[][] pixels;
    Monochrome greyScale;


    public Image (int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException();
        }
        else {
            this.width = width;
            this.height = height;
            pixels = new int[width][height];
            greyScale = new  Monochrome();
        }
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public static Image convertFromRI (RasterImage inputImage) {
       assert !(inputImage == null);
       Image outputImage = new Image(inputImage.getWidth(), inputImage.getHeight());
		for (int height = 0; height < outputImage.getHeight(); height++ ) {
			for (int width = 0; width < outputImage.getWidth(); width++) {
                outputImage.pixels[width][height]
                        = inputImage.getRGB(inputImage.getWidth() * height + width);
			}
		}
       return outputImage;
   }

   public boolean setMonochrome (int cRed, int cGreen, int cBlue) {
       greyScale.set(cRed, cGreen, cBlue);
       return true;
   }

	/**
	 * Gets the red component of the pixel color.
	 * @author Ainārs Kumpiņš.
	 * @param rgb Has format 0xaarrggbb or 0x00rrggbb.
	 * @return The red component (0..255).
	 */
	public static int getRed(int rgb) {
		return (rgb >> 16) & 255;
	}

	/**
	 * Gets the green component of the pixel color.
	 * @author Ainārs Kumpiņš.
	 * @param rgb Has format 0xaarrggbb or 0x00rrggbb.
	 * @return The green component (0..255).
	 */
	public static int getGreen(int rgb) {
		return (rgb >> 8) & 255;
	}

	/**
	 * Gets the blue component of the pixel color.
	 * @author Ainārs Kumpiņš.
	 * @param rgb Has format 0xaarrggbb or 0x00rrggbb.
	 * @return The blue component (0..255).
	 */
	public static int getBlue(int rgb) {
		return rgb & 255;
	}

	/**
	 * Returns an integer pixel in RGB color model BufferedImage.TYPE_INT_RGB
	 * with specified red, green and blue component values.
	 * @author Ainārs Kumpiņš.
	 * @param red The red component (0..255).
	 * @param green The green component (0..255).
	 * @param blue The blue component (0..255).
	 */
	public static int getRGB(int red, int green, int blue) {
		return blue + (green << 8) + (red << 16);
	}

    public int intensity(int rgb) {
		int red = getRed(rgb);
		int green = getGreen(rgb);
		int blue = getBlue(rgb);
		int color = red * greyScale.cRed + green * greyScale.cGreen + blue * greyScale.cBlue;
		color = color / (greyScale.cSumm);
        return color;
	}

    public boolean greyImage () {
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int color = intensity (pixels[w][h]);
                pixels[w][h] = color;
            }
        }
        return true;
    }
    
    //
    public static void drawLine (Image workingImage, int startx, int starty,int endx, int endy, int color) {
        if ( workingImage == null ||startx < 0 || starty < 0
             || endx >= workingImage.getWidth() || endy >= workingImage.getHeight()) {
            throw new IllegalArgumentException("Ridge.Image.drawLine() IllegalArgument");
        }
        if ((starty > endy) ) {
            int tmp = startx;
            startx = endx; endx = tmp;
            tmp = starty;
            starty = endy; endy = tmp;
        }

        if (endx - startx == 0) {
            for (int y = starty; y <= endy; y++) {
                workingImage.pixels[y][startx] = color;
            }
        }
        else {
            double slope = (double) (endy - starty) / (endx - startx);
            double yInt = starty - slope * startx;
            if (slope < 1 && slope > -1) {
                if (startx > endx) {
                    int temp = startx; startx = endx; endx = temp;
                    temp = starty; starty = endy; endy = temp;
                }
                for (int x = startx; x <= endx; x++) {
                    double y = x * slope + yInt;
                    workingImage.pixels[(int)Math.ceil(y)][x] = color;
                }
            }
            else {
                for (int y = starty; y <= endy; y++) {
                    double x = (y - yInt)/ slope;
                    workingImage.pixels[y][(int)Math.ceil(x)] = color;
                }
            }
        }
    }

	/**
	 *
	 * @author Ainars Kumpins.
	 * @return
	 * @since
	 */
	public static int getRandomColor() {
		Random numGen = new Random();
		return getRGB(numGen.nextInt(256), numGen.nextInt(256), numGen.nextInt(256));
	}

}
