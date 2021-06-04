/**
 * Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Mar 29, 2010 6:36:59 PM
 *
 */

package lv.lumii.pixelmaster.modules.filters.domain.arbitraryarea;


class Grayscale implements DistinguishedPixelLevelManipulator
{
    public java.awt.Color manipulate( java.awt.Color color )
    {
        int intensity = ( color.getRed() + color.getGreen() + color.getBlue() ) / 3;

        return new java.awt.Color( intensity, intensity, intensity, 255 );
    }
}




