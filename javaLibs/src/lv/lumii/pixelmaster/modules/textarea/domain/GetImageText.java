

package lv.lumii.pixelmaster.modules.textarea.domain;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
//import java.util.*;


/*import java.io.*;
import com.sun.image.codec.jpeg.*;
import java.awt.image.*;
import java.util.*;*/



/**
 * Get text from images
 * @author <a href="http://www.abstractnonsense.com">Dr. William Bland</a>
 * @version 1.0
 */
public class GetImageText {
    private BufferedImage image;
    public double merge_densityFactor;
    public int merge_mass;
    public int merge_dist1;
    public double merge_distfac;
    public int merge_dist2;

    public GetImageText (BufferedImage image) {
        this.image = image;
        merge_mass = 15;
        merge_dist1 = 4;
        merge_dist2 = 20;
        merge_distfac = 1;
        merge_densityFactor = 0.5;
    }

    public GetImageText ( BufferedImage image, double merge_densityFactor,
                          int merge_mass, int merge_dist1, int merge_dist2,
                          double merge_distfac) {
        this.image = image;
        this.merge_mass = merge_mass;
        this.merge_dist1 = merge_dist1;
        this.merge_dist2 = merge_dist2;
        this.merge_distfac = merge_distfac;
        this.merge_densityFactor = merge_densityFactor;
    }

    int getRed (int rgb) {
        return (rgb & 0xff0000) >> 16;
    }

    int getGreen (int rgb) {
        return (rgb & 0x00ff00) >> 8;
    }

    int getBlue (int rgb) {
        return (rgb & 0x0000ff);
    }

    int rgb (int red, int green, int blue ) {
        return blue + (green << 8) + (red << 16 );
    }

    /**
     * Discard boxes that do not appear to contai text
     */
    LinkedList discardNonText ( LinkedList boxes, int[][] contrast ) {
        int boxNr = 0;
        while ( boxNr < boxes.size() ) {
            int numberOfStems = 0;
            TextRegion thisBox = (TextRegion)boxes.get(boxNr);
            // Count the stems in this box
            if ( thisBox.yStart != thisBox.yEnd) {
                for ( int width = thisBox.xStart + 1; width < thisBox.xEnd - 1; width++ ) {
                    int thisStemHeight = 0;
                    for ( int height = thisBox.yStart + 1; height < thisBox.yEnd - 1; height++ ) {
                        if ( ( contrast[width][height] != 0
                               || contrast[width - 1][height] != 0
                               || contrast[width + 1][height] != 0)
                              && ( contrast[width][height - 1] != 0
                                   || contrast[width - 1][height - 1] != 0
                                   || contrast[width + 1][height - 1] != 0)
                              && ( contrast[width][height + 1] != 0
                                   || contrast[width - 1][height + 1] != 0
                                   || contrast[width + 1][height + 1] != 0))
                            thisStemHeight++;
                        // a stem must cover at least 70% of a vertical line
                        if ( (100 * thisStemHeight) / thisBox.height() > 70 )
                            numberOfStems++;
                    }
                }
            }
            if ( thisBox.area() < 50
                 || thisBox.aspect() > .2 //0.2
                 || thisBox.height() < 5
                 || thisBox.width() < 20
                 // expect at least one stem for every <height> of <width>
                 || numberOfStems < thisBox.width() / thisBox.height() )
                boxes.remove(boxNr--);
            boxNr++;
        }
        return ( boxes );
    }

    /**
     * Srink each box as much as posible
     * @param LinkedList boxes
     * @param int[][] contrast
     * @return LinkedList
     */
    LinkedList shrinkBoxes (LinkedList boxes, int[][] contrast) {
        int boxNr = 0;
        while (boxNr < boxes.size() ) {
           TextRegion thisBox = (TextRegion)boxes.get(boxNr);
           if (thisBox.xStart != thisBox.xEnd && thisBox.yStart != thisBox.yEnd) {
               int total = 0;
               for (int height = thisBox.yStart; height < thisBox.yEnd; height++)
                   for (int width = thisBox.xStart; width < thisBox.xEnd; width++ )
                       total += contrast[width][height];
               double averageX = total / thisBox.height();
               double averageY = total / thisBox.width();
               int newXstart = thisBox.xStart;
               int newXend = thisBox.xEnd;
               int newYstart = thisBox.yStart;
               int newYend = thisBox.yEnd;
               boolean moved = true;
               while (newXstart < newXend && moved) {
                   moved = false;
                   int t1 = 0;
                   int t2 = 0;
                   for ( int height = thisBox.yStart; height < thisBox.yEnd; height++ ) {
                       t1 += contrast[newXstart][height];
                       t2 += contrast[newXend][height];
                   }
                   if (t1 < averageY ) {
                       newXstart++;
                       moved = true;
                   }
                   if (t2 < averageY ) {
                       newXend--;
                       moved = true;
                   }
               }
               moved = true;
               while (newYstart < newYend && moved) {
                   moved = false;
                   int t1 = 0;
                   int t2 = 0;
                   for (int width = thisBox.xStart; width < thisBox.xEnd; width++ ) {
                       t1 += contrast[width][newYstart];
                       t2 += contrast[width][newYend];
                   }
                   if ( t1 < averageX ) {
                       newYstart++;
                       moved = true;
                   }
                   if ( t2 < averageX ) {
                       newYend--;
                       moved = true;
                   }
               }
               thisBox.xStart = newXstart;
               thisBox.xEnd = newXend;
               thisBox.yStart = newYstart;
               thisBox.yEnd = newYend;
           }
           boxNr++;
        }
        return (boxes );
    }


    LinkedList mergeBoxes (LinkedList boxes) {
        boolean change = true;
        while ( change == true ) {
            change = false;
            int boxNr1 = 0;
            while ( boxNr1 < boxes.size() ) {
                int boxNr2 = 0;
                while ( boxNr1 < boxes.size() && boxNr2 < boxes.size() ) {
                    if (boxNr1 != boxNr2) {
                        TextRegion thisBox = (TextRegion) boxes.get(boxNr1);
                        TextRegion thatBox = (TextRegion) boxes.get(boxNr2);
                        change = mergeBoxes ( thisBox, thatBox );
                        if ( change ) {
                            boxes.set ( boxNr1, thisBox );
                            boxes.remove( boxNr2 );
                            boxNr2--;
                        }
                    }
                    boxNr2++;
                }
                boxNr1++;
            }
        }
        return ( boxes );
    }

    boolean mergeBoxes (TextRegion thisBox, TextRegion thatBox) {
        int mergeXstart = Math.min( thisBox.xStart, thatBox.xStart );
        int mergeXend = Math.max( thisBox.xEnd, thatBox.xEnd );
        int mergeYstart = Math.min(thisBox.yStart, thatBox.yStart);
        int mergeYend = Math.max(thisBox.yEnd, thatBox.yEnd);
        double mergeMass = thisBox.mass + thatBox.mass;
        double mergeDensity
                = mergeMass
                / ((mergeXend - mergeXstart) * (mergeYend - mergeYstart));
        double mergeAspect
                = ((double)mergeYend - mergeYstart)
                / ((double)mergeXend - mergeXstart);

        double reasonsToMerge = 0;
        if ( mergeDensity > merge_densityFactor * thisBox.density() )
            reasonsToMerge++;
        if ( mergeDensity > merge_densityFactor * thatBox.density() )
            reasonsToMerge++;
        if ( mergeAspect < thisBox.aspect() )
            reasonsToMerge++;
        if ( mergeAspect < thatBox.aspect() )
            reasonsToMerge++;
        if ( thisBox.mass > merge_mass && thatBox.mass > merge_mass )
            reasonsToMerge++;
        int maxBoxWidth = Math.max( thisBox.width(), thatBox.width() );
        if ( Math.abs( thisBox.yStart - thatBox.yStart ) < merge_dist1
             && Math.abs(thisBox.yEnd - thatBox.yStart) < merge_dist1
             && ( Math.abs(thisBox.xStart - thatBox.xEnd) < merge_distfac * maxBoxWidth
                  || Math.abs(thisBox.xEnd - thatBox.xStart) < merge_distfac * maxBoxWidth ) )
            reasonsToMerge++;
        if ( ( Math.abs( thisBox.yStart - thatBox.yStart) < merge_dist2
               || Math.abs(thisBox.yEnd - thatBox.yEnd) < merge_dist2 )
             && ( Math.abs(thisBox.xStart - thatBox.xEnd) < merge_distfac * maxBoxWidth
                  || Math.abs(thisBox.xEnd - thatBox.xStart) < merge_distfac * maxBoxWidth ) )
            reasonsToMerge++;
        if (reasonsToMerge > 3 ) { // 7 reasons max
            thisBox.xStart = mergeXstart;
            thisBox.xEnd = mergeXend;
            thisBox.yStart = mergeYstart;
            thisBox.yEnd = mergeYend;
            thisBox.mass = mergeMass;
            return true;
        }
        return false;
    }


    public int [][] getContrast() {
         // Find pixels that stand out from the background
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int[][] contrast = new int[imageWidth][imageHeight];
        int[][] temp = new int[imageWidth][imageHeight];

        for (int height = 2; height < imageHeight - 2; height++) {
            for (int width = 2; width < imageWidth - 2; width++) {
                int thisPixel = image.getRGB(width, height);
                int leftPixel = image.getRGB(width - 1, height);
                int left2Pixel = image.getRGB(width - 2, height);
                int rightPixel = image.getRGB(width + 1, height);
                int right2Pixel = image.getRGB(width + 2, height);
                int upPixel = image.getRGB(width, height - 1);
                int downPixel = image.getRGB(width, height + 1);
                int t1 = 60; // threshold
                int t2 = 80;

                if (   Math.abs( getBlue(thisPixel) - getBlue(rightPixel) ) > t1
                    || Math.abs( getBlue(thisPixel) - getBlue(leftPixel) ) >t1
                    || Math.abs( getBlue(thisPixel) - getBlue(upPixel) ) >t1
                    || Math.abs( getBlue(thisPixel) - getBlue(downPixel) ) >t1
                    || Math.abs( getBlue(thisPixel) - getBlue(right2Pixel) ) >t2
                    || Math.abs( getBlue(thisPixel) - getBlue(left2Pixel) ) >t2
                    || Math.abs( getGreen(thisPixel) - getGreen(rightPixel) ) > t1
                    || Math.abs( getGreen(thisPixel) - getGreen(leftPixel) ) >t1
                    || Math.abs( getGreen(thisPixel) - getGreen(upPixel) ) >t1
                    || Math.abs( getGreen(thisPixel) - getGreen(downPixel) ) >t1
                    || Math.abs( getGreen(thisPixel) - getGreen(right2Pixel) ) >t2
                    || Math.abs( getGreen(thisPixel) - getGreen(left2Pixel) ) >t2
                    || Math.abs( getRed(thisPixel) - getRed(rightPixel) ) > t1
                    || Math.abs( getRed(thisPixel) - getRed(leftPixel) ) >t1
                    || Math.abs( getRed(thisPixel) - getRed(upPixel) ) >t1
                    || Math.abs( getRed(thisPixel) - getRed(downPixel) ) >t1
                    || Math.abs( getRed(thisPixel) - getRed(right2Pixel) ) >t2
                    || Math.abs( getRed(thisPixel) - getRed(left2Pixel) ) >t2 )
                    temp[width][height] = 1;
            }
        }

        // Look for areas of contrast that extend vertically and norizontally
        // but not too far, to eliminate long straight lines (e.g. borders);
        int lineThreshold = 100;
        for ( int height = 2; height < imageHeight - 2; height++) {
            for ( int width = 2; width < imageWidth - 2; width++) {
                if (temp[width][height] == 1) {
                    int areaWidth = 0;
                    int areaHeight = 0;
                    for ( int i = 0;
                          height + i < imageHeight - 2
                          && height - i > 2
                          && (temp[width][height + i] == 1 || temp[width][height - i] == 1)
                          && areaHeight++ < lineThreshold;
                          i++ );
                         //{ areaHeight++; }
                    for ( int i = 0;
                          width + i < imageWidth -2
                          && width - i > 2
                          && (temp[width + i][height] == 1 || temp[width - i][height] == 1)
                          && areaWidth++ < lineThreshold;
                          i++ );
                         //{ areaWidth++; }
                    int totalOnLine = 0;
                    for ( int i = Math.max( 2, width - 40 );
                          i < Math.min( imageWidth - 2, width + 40 );
                          i++ )
                        totalOnLine += temp[i][height];
                    if ( totalOnLine > 7
                         && areaWidth < lineThreshold
                         && areaHeight < lineThreshold )
                       contrast[width][height] = 1;
                }
            }
        }
        return contrast;
    }


     /**
     * Looks for areas of text in an image.
     * @return a LinkedList of boxes that are likely to contain text.
     */
    public LinkedList getTextBoxes() {
        LinkedList textBoxes = new LinkedList();

        int minBoxHeight = 10;
        int contrast[][] = getContrast();

        //************* TEMP for testing
//        try {
//        FileOutputStream out = new FileOutputStream( "contrast.jpg" );
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( out );
//        BufferedImage contrastjpg
//        = new BufferedImage( image.getWidth(),
//        image.getHeight(),
//        BufferedImage.TYPE_INT_RGB );
//        for( int i = 0; i < image.getWidth(); i++ )
//        for( int j = 0; j < image.getHeight(); j++ )
//        contrastjpg.setRGB(i,j,0xffffff * contrast[i][j]);
//        encoder.encode( contrastjpg );
//        out.close();
//        }
//        catch( Exception e ) {
//        System.out.println( "getTexBox Exception: " + e );
//        }
        // End of TEMP ************************************

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int contrastOnLine[] = new int[imageHeight];
        for (int y = 1; y < imageHeight - 1; y++ ) {
            contrastOnLine[y] = 0;
            //int count = 0; // unused // original
            for (int width = 0; width < imageWidth; width++) {
                contrastOnLine[y] += contrast[width][y];
                //count += contrast[width][y]; // unused // original
            }
        }
        for ( int i = 0; i < 2; i++) {
            for (int y = 1; y < imageHeight - 1; y++ ) {
                contrastOnLine[y] = ( contrastOnLine[y - 1]
                                     + contrastOnLine[y]
                                     + contrastOnLine[y + 1]) / 3;
            }
        }
        int averageOnLine = 0;
        for (int height = 1; height < imageHeight - 1; height ++)
            averageOnLine += contrastOnLine[height];
        averageOnLine /= (imageHeight - 2);
        boolean intext = false;
        int boxStartY = 0;
        int boxAverage = 0;
        int boxLines = 0;
        for ( int height = 1; height < imageHeight - 1; height++ ) {
            if (contrastOnLine[height] > averageOnLine && !intext ) {
                intext = true;
                boxStartY = height;
                boxAverage = contrastOnLine[height];
                boxLines = 1;
            }
            else if (contrastOnLine[height] > averageOnLine) {
                boxAverage += contrastOnLine[height];
                boxLines++;
            }
            else if (contrastOnLine[height] <= averageOnLine && intext) {
                // found vertical limits, now find Horizontal.
                intext = false;
                int boxEndY = height;
                if ( boxEndY - boxStartY > minBoxHeight) { // text must be higher that minBoxHeight variable
                    boxAverage /= boxLines;
                    int contrastOnColumn[] = new int[imageWidth];
                    for ( int width = 1; width < imageWidth - 1; width++ ) {
                        for (int inBoxHeight = boxStartY; inBoxHeight < boxEndY; inBoxHeight++ ) {
                            contrastOnColumn[width] += contrast[width][inBoxHeight];
                        }
                    }
                    for ( int i = 0; i < 2; i++) {
                        for (int width = 1; width < imageWidth - 1; width++ ) {
                            contrastOnColumn[width]
                                    = ( contrastOnColumn[width - 1]
                                      + contrastOnColumn[width]
                                      + contrastOnColumn[width + 1]) / 3;
                        }
                    }
                    int averageOnColumn = 0;
                    for (int width = 1; width < imageWidth - 1; width++ ) 
                        averageOnColumn += contrastOnColumn[width];
                    averageOnColumn /= ( imageWidth - 2 );
                    boolean intextx = false;
                    int boxStartX = 0;
                    for ( int width = 1; width < imageWidth - 1; width++ ) {
                        if (contrastOnColumn[width] > averageOnColumn / 2 && !intextx ) {
                            intextx = true;
                            boxStartX = width;
                        }
                       else if ( contrastOnColumn[width] <= averageOnColumn / 2 && intextx ) {
                            intextx = false;
                            int boxEndX = width;
                            // found horizontal limits,
                            // now (if necessary) shrink
                            // vertical limits
                            int newCount = 0;
                            int tempBoxStartY = boxStartY;
                            int tempBoxEndY = boxEndY;
                            while (tempBoxStartY < boxEndY && newCount == 0 ) {
                                for ( int i = boxStartX; i < boxEndX; i++ )
                                    newCount +=contrast[i][tempBoxStartY];
                                if (newCount < 2 )
                                    tempBoxStartY++;
                            }
                            newCount = 0;
                            while (tempBoxStartY < boxEndY && newCount == 0) {
                                for (int i = boxStartX; i < boxEndX; i++ )
                                    newCount += contrast[i][tempBoxEndY];
                                if (newCount < 2 )
                                    tempBoxEndY--;
                            }
                            TextRegion thisBox
                                    = new TextRegion ( boxStartX,
                                                       tempBoxStartY,
                                                       boxEndX,
                                                       tempBoxEndY,
                                                       imageWidth,
                                                       imageHeight,
                                                       boxAverage );
                            textBoxes.add( thisBox );
                        }
                    }
                }
            }
        }
        //for testing purposes
        //shrinkBoxes( textBoxes, contrast );
        textBoxes = mergeBoxes( textBoxes );
        //shrinkBoxes( textBoxes, contrast );
        textBoxes = discardNonText( textBoxes, contrast );
       // return( shrinkBoxes( textBoxes, contrast ) );
        return textBoxes;
    }


    /**
     * Isolate text
     * @return a <code>BufferedImage</code> value
     */

    public BufferedImage isolateText ( LinkedList boxes ) {
        BufferedImage outputImage = new BufferedImage ( image.getWidth(),
                                                       image.getHeight(),
                                                       BufferedImage.TYPE_INT_RGB );
        // mage everythimg monochrome
        for ( int height = 0; height < image.getHeight(); height ++) {
            for ( int width = 0; width < image.getWidth(); width ++) {
                int colour = image.getRGB(width, height);
                int gray = (3*getRed(colour) + 6*getGreen(colour) + getBlue(colour)) / 10;
                outputImage.setRGB(width, height, rgb(gray, gray, gray));
            }
        }
        // fill tex boxes with colour
        for ( int boxNr = 0; boxNr < boxes.size(); boxNr++) {
            TextRegion thisBox = (TextRegion)boxes.get(boxNr);
            int xStart = Math.max(1, thisBox.xStart);
            int xEnd = Math.min(image.getWidth() - 2, thisBox.xEnd);
            int yStart = Math.max(1, thisBox.yStart);
            int yEnd = Math.min(image.getHeight() - 2, thisBox.yEnd);
            for ( int height = yStart; height < yEnd; height ++ ) {
                for ( int width = xStart; width < xEnd; width++ ) {
                    outputImage.setRGB(width, height, image.getRGB(width, height));
                }
            }
        }
        // draw red border around each text box
        int RED = 0xff0000;
        for ( int boxNr = 0; boxNr < boxes.size(); boxNr++ ) {
            TextRegion thisBox = (TextRegion)boxes.get(boxNr);
            int xStart = Math.max(1, thisBox.xStart);
            int xEnd = Math.min(image.getWidth() - 2, thisBox.xEnd);
            int yStart = Math.max(1, thisBox.yStart);
            int yEnd = Math.min(image.getHeight() - 2, thisBox.yEnd);
            for ( int width = xStart; width < xEnd; width++ ) {
                outputImage.setRGB( width, thisBox.yStart, RED );
                outputImage.setRGB( width, thisBox.yEnd, RED );
            }
            for ( int height = yStart; height < yEnd; height++ ) {
                outputImage.setRGB( thisBox.xStart, height, RED );
                outputImage.setRGB( thisBox.xEnd, height, RED );
            }
        }
        return ( outputImage );
    }
}

// ************** END OF GetImageText **************





























