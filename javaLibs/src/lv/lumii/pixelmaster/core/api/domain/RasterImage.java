
package lv.lumii.pixelmaster.core.api.domain;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.image.*;
import java.util.Arrays;

/**
 * Class representing raster image.
 *
 * @author MII team
 */
public final class RasterImage implements Cloneable {

	/**
	 * Represents a two-dimensional array of pixels. First dimension
	 * corresponds to the horizontal axis x (from left to the right) and can
	 * have values 0..getWidth()-1. Second dimension corresponds to the vertical
	 * axis y (from top to bottom) and can have values 0..getHeight()-1.
	 *
	 * <p>Pixel array must have width*height elements. Pixel at
	 * coordinate [i, j] can be accessed as pixels[width*j+i].
	 * Two-dimensional array is modelled by a one-dimensional array as follows:
	 *
	 * pixels[0..width-1]:				M[0, 0] M[1, 0] ... M[width-1, 0]
	 * pixels[width..2*width-1]:		M[0, 1] M[1, 1] ... M[width-1, 1]
	 *								...
	 * </p>
	 *
	 * <p>Pixel values should lie in range [0..0x00ffffff]. Pixel values have
	 * format 0x00rrggbb (alpha is not present, the highest byte must always be 0).</p>
	 *
	 * <p>The image is grayscale if all RGB component values are equal:
	 * for example, 0x004f4f4f or 0x003a3a3a. The image is binary if all pixels have
	 * value 0x00000000 or 0x00ffffff. Each pixel takes 4 bytes.
	 *
	 * <p>If the image is grayscale, this representation takes 4 times more memory
	 * than needed, and if it is binary, even 32 times more.
	 * But currently it is used in the application (hoping the undo/redo stack
	 * won't be too deep).</p>
	 */
	private int[] pixels;

	private int width, height;

	/** used for fast output to the screen */
	private RasterImageProducer ip;
	
	/**
	 * Constructor that creates image with specified width and height.
	 * Initializes pixels with 0 (black)
	 * @param width a positive integer
	 * @param height a positive integer
	 */
	public RasterImage(int width, int height) {
		assert !(width<1 || height<1);
		this.width = width;
		this.height = height;
		pixels = new int[width * height]; // initializes pixels with 0
		ip=new RasterImageProducer();
		assert invariant();
	}
	
	/**
	 * Constructor that creates a copy of the image
	 * @param rImage The image that must be copied, cannot be null
	 */
	public RasterImage(RasterImage rImage) {
		assert !(rImage==null);
		width=rImage.getWidth();
		height=rImage.getHeight();
		pixels=new int[width*height];
		System.arraycopy(rImage.pixels, 0, pixels, 0, height*width);
		ip=new RasterImageProducer();
		assert invariant();
	}

    /** {@inheritDoc} */
	@Override
	public Object clone() {
		try {
			RasterImage tmp=(RasterImage)(super.clone());
			tmp.pixels = tmp.pixels.clone();
			tmp.ip=tmp.new RasterImageProducer();
			assert tmp.invariant();
			return tmp;
		}
		catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}
	}

	/**
	 * Determines whether or not two images are equal. Two instances of
	 * <code>RasterImage</code> are equal if they have the same width, height
	 * and pixel values.
	 *
	 * @param obj {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RasterImage)) { return super.equals(obj); }
		final RasterImage other = (RasterImage) obj;
		if (this.getWidth() != other.getWidth() || this.getHeight() != other.getHeight()) {
			return false;
		}
		return Arrays.equals(this.pixels, other.pixels);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + this.getWidth();
		hash = 11 * hash + this.getHeight();
		return hash;
	}

	/**
	 * {@inheritDoc}
	 * @return a string containing information about width and height of the image
	 */
	@Override
	public String toString() { return "width: " + width + ", height: " + height; }

	/**
	 * Sets pixel value.
	 * @param x The horizontal coordinate in range [0..getWidth()-1]
	 * @param y The vertical coordinate in range [0..getHeight()-1]
	 * @param value Pixel value in format 0x00rrggbb
	 */
	public void setRGB(int x, int y, int value) {
		assert !(x<0 || x>=width || y<0 || y>=height || (value & 0xff000000)!=0);
//		assert invariant();
		this.setRGB(this.getWidth() * y + x, value);
	}

	/**
	 * Sets pixel value
	 * @param i the index in range [0..getSize()-1]
	 * @param value Pixel value in format 0x00rrggbb
	 */
	public void setRGB(int i, int value) {
		assert (i >= 0 && i < getSize() && (value & 0xff000000) == 0);
//		assert invariant();
		this.pixels[i] = value;
		assert invariant();
	}

	/**
	 * gets pixel value
	 * @param x The horizontal coordinate in range [0..getWidth()-1]
	 * @param y The vertical coordinate in range [0..getHeight()-1]
	 * @return 0x00rrggbb
	 */
	public int getRGB(int x, int y) {
		assert !(x<0 || x>=width || y<0 || y>=height);
		return pixels[this.getWidth() * y + x];
	}

	/**
	 * gets pixel value
	 * @param i the index in range [0..getSize()-1]
	 * @return 0x00rrggbb
	 */
	public int getRGB(int i) {
		assert (i >= 0 && i < getSize());
		return this.pixels[i];
	}

	public int[] getPixels() {
		return pixels.clone();
	}

	/**
	 * Copies pixels of this image to the specified image
	 * @param to may point to the same object
	 * @param srcPos
	 * @param destPos
	 * @param length
	 * @see System#arraycopy(java.lang.Object, int, java.lang.Object, int, int)
	 */
	public void copyTo(RasterImage to, int srcPos, int destPos, int length) {
		System.arraycopy(pixels, srcPos, to.pixels, destPos, length);
	}

	/**
	 * Copies pixels of this image to the specified array
	 * @param to
	 * @param srcPos
	 * @param destPos
	 * @param length
	 * @see System#arraycopy(java.lang.Object, int, java.lang.Object, int, int)
	 */
	public void copyTo(int[] to, int srcPos, int destPos, int length) {
		System.arraycopy(pixels, srcPos, to, destPos, length);
	}

	/**
	 * Copies pixels from the specified array
	 * @param from
	 * @param srcPos
	 * @param destPos
	 * @param length
	 * @see System#arraycopy(java.lang.Object, int, java.lang.Object, int, int)
	 */
	public void copyFrom(int[] from, int srcPos, int destPos, int length) {
		System.arraycopy(from, srcPos, pixels, destPos, length);
		assert invariant();
	}

    private boolean isValid( int row, int col )
    {
        return ( row >= 0 && col >= 0 && row < this.height && col < this.width );
    }

    private int getPixelIndex( int row, int col )
    {
        assert isValid( row, col ) : ( "Pixel at " + row + ":" + col + " is out of image boundaries!" );
        
        return ( row * this.width + col );
    }

    public Color get( int row, int col )
    {
        int rgb = this.pixels[ this.getPixelIndex( row, col ) ];

        return new Color( (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, 255 );
    }



    public Color get( int index )
    {
        if ( index < 0 || index > (this.width * this.height - 1) )
        {
            throw new IndexOutOfBoundsException();
        }

        int value = this.pixels[index];

        return new Color( ( value >>> 16 ) & 0xFF,
                          ( value >>>  8 ) & 0xFF,
                          ( value        ) & 0xFF,
                          255 );
    }



    public void set( int index, Color color )
    {
        if ( index < 0 || index > (this.width * this.height - 1) )
        {
            throw new IndexOutOfBoundsException();
        }

        this.pixels[index] = ( color.getRed  () << 16 ) & 0x00FF0000
                           | ( color.getGreen() <<  8 ) & 0x0000FF00
                           | ( color.getBlue ()       ) & 0x000000FF;
    }



    public void set( int row, int col, Color color )
    {
        this.pixels[ this.getPixelIndex( row, col ) ] = ( color.getRed  () << 16 ) & 0x00FF0000
                                                      | ( color.getGreen() <<  8 ) & 0x0000FF00
                                                      | ( color.getBlue ()       ) & 0x000000FF;
    }

	/**
	 * Copies this image to the destination image.
	 * Destination image will have the same size as <code>this</code> image.
	 *
	 * @param target The target image (not <code>null</code>).
	 *		If <code>source == this</code>, the call has no effect.
	 */
	public void copyTo(RasterImage target) {
		assert !(target==null);
		if (this==target) return;
		int size=pixels.length;
		if (target.pixels.length!=size) target.pixels=new int [size];
		target.width=width;
		target.height=height;
		System.arraycopy(pixels, 0, target.pixels, 0, size);
		assert target.invariant();
	}

	/**
	 * Checks if the image is binary (pixel values are either 0 or 0x00ffffff).
	 * @return true if the image is binary.
	 */
	public boolean isBinary() {
		int size=width*height;
		for (int i=0; i<size; i++)
			if (pixels[i] != 0x00ffffff && pixels[i] != 0) return false;
		return true;
	}

	/**
	 * <p>Returns intensity of the pixel at specified coordinates using default
	 * red, green and blue coefficients.</p>
	 *
	 * <p>Precondition: <code>x</code> is in range [0..<code>width</code>-1] and <code>y</code> is in range [0..<code>height</code>-1].</p>
	 *
	 * @param x The horizontal coordinate.
	 * @param y The vertical coordinate.
	 * @return the grey value (intensity)
	 */
	public int greyValueAt(int x, int y) {
		assert !(x<0 || x>=width || y<0 || y>=height);
		return greyValueAt(this.width*y+x);
	}

	/**
	 * <p>Returns intensity of the pixel at specified index.</p>
	 *
	 * <p>Precondition: <code>i</code> is in range [0..<code>width * height - 1</code>].</p>
	 *
	 * @param i The array index.
	 * @return intensity in range [0..255]
	 */
	public int greyValueAt(int i) {
		assert !(i<0 || i>=width*height);
		return RGB.intensity(pixels[i]);
	}

	/**
	 * Checks if <code>selection</code> is in bounds of this image
	 * @param selection rectangular area
	 * @return true if selection.getWidth() &gt;= 0, selection.getHeight() &gt;= 0,
	 *		width &gt; selection.x &gt;= 0, height &gt; selection.y &gt;= 0,
	 *		width &gt;= selection.x+selection.getWidth(), height &gt;= selection.y+selection.getHeight()
	 */
	public boolean inBounds(Rectangle selection) {
		return selection.width >= 0 && selection.height >= 0 &&
			width > selection.x && selection.x>= 0 && height > selection.y &&
			selection.y>= 0 &&
	 		width >= selection.x+selection.width &&
			height >= selection.y+selection.height;
	}

	/**
	 * image producer is used to draw image on the screen
	 * @return image producer
	 */
	public ImageProducer getSource() { return ip; }

	/** @return width */
	public int getWidth() { return width; }

	/** @return height */
	public int getHeight() { return height; }

	/** @return size (the number of pixels) */
	public int getSize() { return width * height; }

	/**
	 * Recreates the pixel buffer. Pixel values are undefined.
	 * @param newWidth a positive integer
	 * @param newHeight a positive integer
	 */
	public void resize(int newWidth, int newHeight) {
		assert newWidth > 0 && newHeight > 0;
		assert invariant();
		width = newWidth; height = newHeight;
		int size = newWidth * newHeight;
		if (size != pixels.length) pixels = new int[size]; // init to zero
		assert invariant();
	}

	/**
	 * Paints all pixels of the image with specified color.
	 * @param color has format 0x00rrggbb
	 */
	public void fill(int color) {
		assert (color & 0xff000000) == 0;
		assert invariant();
		Arrays.fill(pixels, color);
		assert invariant();
	}

	/**
	 * Paints pixels of the image with the specified color
	 * @param color has format 0x00rrggbb
	 * @param fromIndex the index of the first pixel (inclusive) to be
     *		painted with the specified color
	 * @param toIndex the index of the last pixel (exclusive) to be
	 *		painted with the specified color
	 * @see Arrays#fill(int[], int, int, int)
	 */
	public void fill(int color, int fromIndex, int toIndex) {
		assert (color & 0xff000000) == 0;
		assert invariant();
		Arrays.fill(pixels, fromIndex, toIndex, color);
		assert invariant();
	}

	private boolean invariant() {
		assert !(width<1 || height<1 || ip==null || pixels==null);
		int size=width*height;
		assert pixels.length == size;

		/*
		 * highest byte must be 0
		 * this is very important since many algorithms
		 * rely on this assumption
		 * and will break if it does not hold
		 */
		if (Config.FULL_ASSERTION_CHECKS) {
			for (int i=0; i<size; i++)
				assert (pixels[i] & 0xff000000) == 0;
		}
		return true;
	}

	/**
	 * Implementation of {@link java.awt.image.ImageProducer}
	 * @author Jevgenijs Jonass
	 */
	final private class RasterImageProducer implements ImageProducer {

		/**
		 * Used by {@link java.awt.Component#createImage(java.awt.image.ImageProducer)}
		 * to create instance of {@link java.awt.Image}
		 */
		@Override
		public void startProduction(ImageConsumer ic) {
			assert !(ic==null);
			ic.setDimensions(width, height);
			ColorModel defaultCM=new DirectColorModel(32,
						0x00ff0000,	// Red
						0x0000ff00,	// Green
						0x000000ff,	// Blue
						0x00000000	// Alpha
			);
			ic.setColorModel(defaultCM);
			ic.setHints(ImageConsumer.SINGLEPASS);
			ic.setPixels(0, 0, width, height, defaultCM, pixels, 0, width);
			ic.imageComplete(ImageConsumer.STATICIMAGEDONE);
		}

		public void addConsumer(ImageConsumer ic) {}
		public boolean isConsumer(ImageConsumer ic) { return true; }
		public void removeConsumer(ImageConsumer ic) {}
		public void requestTopDownLeftRightResend(ImageConsumer ic) {}
	}

	private final static class Config {

		/**
		 * This option controls whether time consuming assertion checks will
		 * be performed in class RasterImage.
		 *
		 * This parameter only applies when program is run with -ea
		 * (or -enableassertions) VM option.
		 * If set to true, all assertions will be checked, including
		 * time consuming checks.
		 * If false, then assertions will be checked only partially.
		 *
		 * Assertion checks are always performed when run with
		 * VM option -ea. But some checks might take too long.
		 * This parameter can be used to enable/disable such assertions.
		 * If FULL_ASSERTION_CHECKS == false, then checks that do not take
		 * much time will still be performed.
		 */
		private static final boolean FULL_ASSERTION_CHECKS = false;
	}
}
