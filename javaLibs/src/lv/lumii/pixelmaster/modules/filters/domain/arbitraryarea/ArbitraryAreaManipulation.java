/**
 * Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Mar 29, 2010 6:36:59 PM
 *
 */

package lv.lumii.pixelmaster.modules.filters.domain.arbitraryarea;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;



public class ArbitraryAreaManipulation
{
    public final static BlackOut BLACK_OUT = new BlackOut ();
    public final static Grayscale GRAYSCALE = new Grayscale();



    public static void manipulateAtDistinguishedPixelLevel( lv.lumii.pixelmaster.core.api.domain.RasterImage image, java.awt.Polygon selectionArea, DistinguishedPixelLevelManipulator manipulator )
    {
        java.awt.Rectangle bounds = selectionArea.getBounds();

        for ( int row = (int)bounds.getMinY(); row <= (int)bounds.getMaxY(); row++ )
        {
            for ( int col = (int)bounds.getMinX(); col <= (int)bounds.getMaxX(); col++ )
            {
                if ( selectionArea.contains( col, row ) )
                {
					image.set( row, col, manipulator.manipulate( image.get( row, col ) ) );
                }
            }
        }
    }

    /*
    public static void blackOut( util.RasterImage image, java.awt.Polygon selectionArea )
    {
        java.awt.Rectangle bounds = selectionArea.getBounds();

        for ( int row = (int)bounds.getMinY(); row <= (int)bounds.getMaxY(); row++ )
        {
            for ( int col = (int)bounds.getMinX(); col <= (int)bounds.getMaxX(); col++ )
            {
                if ( selectionArea.contains( col, row ) )
                {
                    image.set( row, col, java.awt.Color.BLACK );
                    //System.out.println( "Set black at " + row + ":" + col );
                }
            }
        }
    }
    */




    public static class CustomFilter implements DistinguishedPixelLevelManipulator
    {
        public CustomFilter()
        {
            assert false : "Needed initialization for CustomFilter via constructor CustomFilter( java.awt.Color )";
        }



        public CustomFilter( java.awt.Color color )
        {
            this.color = color;
        }



        public java.awt.Color manipulate( java.awt.Color color )
        {
            return new java.awt.Color( ( color.getRed  () * this.color.getRed  () ) / 255,
                                       ( color.getGreen() * this.color.getGreen() ) / 255,
                                       ( color.getBlue () * this.color.getBlue () ) / 255
            );
        }



        private java.awt.Color color;
    }
}


class BlackOut implements DistinguishedPixelLevelManipulator
{
    public java.awt.Color manipulate( java.awt.Color color )
    {
        return new java.awt.Color( 0, 0, 0, 255 );
    }
}