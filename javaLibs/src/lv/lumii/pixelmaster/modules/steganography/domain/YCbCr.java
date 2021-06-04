/**
 * Objects of class <code>YCbCr</code> store a color in YCbCr color space.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 14, 2010 2:18:51 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import java.awt.*;



public class YCbCr
{
    /**
     * Creates new <code>YCbCr</code> object.
     */

    public YCbCr()
    {
        this.Y  = 0;
        this.Cb = 0;
        this.Cr = 0;
    }



    /**
     * Creates new <code>YCbCr</code> object and initializes it from <code>YCbCr</code>.
     *
     * @param ycc Paragon object.
     * 
     */
    
    public YCbCr( YCbCr ycc )
    {
        this.Y  = ycc.Y  ;
        this.Cb = ycc.Cb ;
        this.Cr = ycc.Cr ;
    }


    /**
     * Creates new <code>YCbCr</code> object and initializes it from <code>rgb</code>.
     *
     * @param rgb A color in RGB color space; the format is 0x00RRGGBB.
     *
     */

    public YCbCr( int rgb )
    {
        this.setRGB( rgb );
    }


    /**
     * Creates new <code>YCbCr</code> object and initializes it from <code>rgb</code>.
     *
     * @param rgb An object of class <code>java.awt.Color</code>.
     *
     * @see java.awt.Color
     *
     */
    
    public YCbCr( Color rgb )
    {
        this.setRGB( rgb.getRed(), rgb.getGreen(), rgb.getBlue() );
    }



    /**
     * Compares two objects of class <code>YCbCr</code> form equality.
     *
     * @param ycc Object to compare with.
     *
     * @return Returns true if objects store the same color; false otherwise.
     * 
     */

    public boolean equals( YCbCr ycc )
    {
        final int accuracy = 5;

        return (    ( lv.lumii.pixelmaster.modules.grapheditor.domain.Math.round( this.Y , accuracy ) == lv.lumii.pixelmaster.modules.grapheditor.domain.Math.round( ycc.Y , accuracy ) )
                 && ( lv.lumii.pixelmaster.modules.grapheditor.domain.Math.round( this.Cb, accuracy ) == lv.lumii.pixelmaster.modules.grapheditor.domain.Math.round( ycc.Cb, accuracy ) )
                 && ( lv.lumii.pixelmaster.modules.grapheditor.domain.Math.round( this.Cr, accuracy ) == lv.lumii.pixelmaster.modules.grapheditor.domain.Math.round( ycc.Cr, accuracy ) ) );
    }

    

    /**
     * Initializes an <code>YCbCr</code> object from the metrics of RGB color space. 
     *
     * @param red   Intensity of red in bounds of [0..255]; Bits higher than 7th are omitted.
     * @param green Intensity of green in bounds of [0..255]; Bits higher than 7th are omitted.
     * @param blue  Intensity of blue in bounds of [0..255]; Bits higher than 7th are omitted.
     */

    public void setRGB( int red, int green, int blue )
    {
        red     &= 0xFF;
        green   &= 0xFF;
        blue    &= 0xFF;

        this.Y  = 0.299 * red + 0.587 * green + 0.144 * blue;
        this.Cb = 0.5 + ( blue - this.Y ) * 0.5;
        this.Cr = 0.5 + ( red  - this.Y ) * 0.625;
    }



    /**
     * Initializes an <code>YCbCr</code> object with a color of RGB color space.
     *
     * @param rgb The format is 0x00RRGGBB.
     *
     */
    
    public void setRGB( int rgb )
    {
        this.setRGB( rgb >> 16, rgb >> 8, rgb );
    }


    /**
     * Returns the color of <code>YCbCr</code> object converted to RGB color space.
     *
     * @return Returns RGB color in format 0x00RRGGBB. 
     *
     */

    public int getRGB()
    {
        return this.toColor().getRGB();
    }



    /**
     * Converts color stored in <code>YCbCr</code> object to RGB color space and returns
     * it in <code>java.awt.Color</code> object.  
     *
     * @return Return object of type <code>java.awt.Color</code> containing color of <code>this</code>.
     * 
     */
    
    public Color toColor()
    {
        double blue = (this.Cb - 0.5) * 2.0 + this.Y,
               red  = (this.Cr - 0.5) * 1.6 + this.Y;
        
        double green = (this.Y - 0.299 * red - 0.144 * blue ) / 0.587;

        if ( red   > 255.0 ) red    = 255.0;
        if ( red   <   0.0 ) red    =   0.0;
        
        if ( green > 255.0 ) green  = 255.0;
        if ( green <   0.0 ) green  =   0.0;

        if ( blue  > 255.0 ) blue   = 255.0;
        if ( blue  <   0.0 ) blue   =   0.0;
        
        return new Color( (int)java.lang.Math.round( red   ),
                          (int)java.lang.Math.round( green ),
                          (int)java.lang.Math.round( blue  ) );
    }



    /**
     * Visualizes color metrics stored in <code>YCbCr</code> object.
     *
     * @return Returns <code>String</code> object with class name and color metrics.
     *
     */

    public String toString()
    {
        return this.getClass().getCanonicalName() + "[Y=" + this.Y + ",Cb=" + this.Cb + ",Cr=" + this.Cr + "]";
    }



    /**
     * Relative luminosity.
     */
    public double Y;


    /**
     * Blue-difference chroma component.
     */
    public double Cb;


    /**
     * Red-difference chroma component.
     */
    public double Cr;
}




