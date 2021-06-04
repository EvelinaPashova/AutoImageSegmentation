/**
 * Class <code>ZhaoAndKoch</code> implements a simplified version of steganographic method proposed by Zhao and Koch
 * and described in "Information Hiding Techniques for Steganography and Digital Watermarking" by S Katzenbeisser.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 14, 2010 3:34:51 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.modules.steganography.domain.YCbCr;

import java.util.Random;


public class ZhaoAndKoch
{
    /**
     * Creates an object used to get access to bits of container interpreted by simplified method proposed by Zhao&Koch.
     *
     * @param image Image to be used as a container.
     *
     */
    
    public ZhaoAndKoch( RasterImage image )
    {
        assert ( image != null ) : "Provide a non-null image please!";

        this.image          = image;
        this.robustness     = 0.25;
        this.strengthPRNG   = new Random();
    }



    /**
     * Returns the number of horizontal blocks in container.
     *
     * @return Number of horizontal blocks in container.
     */

    private int getHorizontalSquares()
    {
        return ( this.image.getWidth() / 8 );
    }



    /**
     * Returns the number of vertical blocks in container.
     *
     * @return Number of vertical blocks in container.
     */

    private int getVerticalSquares()
    {
        return ( this.image.getHeight() / 8 );
    }

    

    /**
     * Returns the number of bits the container is capable to store.
     *
     * @return Capacity of container in bits.
     */

    public int getSize()
    {
        return ( this.getVerticalSquares() * this.getHorizontalSquares() );  
    }



    /**
     * Adjust the strength of the watermark.
     * 
     * @param robustness Strength of the watermark in per cents ( 0 <= robustness <= 1 ).
     */

    public void setRobustness( double robustness )
    {
        assert ( robustness >= 0.0 && robustness <= 1.0 ) : "Give robustness in per cents!";
        
        this.robustness = robustness;

        this.grantedStrength = MIN_STRENGTH + this.robustness * MAX_STRENGTH;
    }

    

    /**
     * Extracts the square of pixels of size 8x8 from image.
     * @param row Upper row to start extraction from.
     * @param col Left column to start extraction from.
     * @return Block of 8x8 YCbCr pixels.
     */
    
    private YCbCr[][] getSquare( int row, int col )
    {
        YCbCr result[][] = new YCbCr[8][8];

        int offsetY = row * 8,
            offsetX = col * 8;

        for ( int y = 0, i = 0; y < 8; y++ )
        {
            for ( int x = 0; x < 8; x++, i++ )
            {
				result[y][x] = new YCbCr( this.image.getRGB( offsetX + x, offsetY + y ) );
            }
        }

        return result;
    }



    /**
     * Replaces a block of 8x8 pixel in image with the provided block.
     *
     * @param square Block of 8x8 pixels.
     * @param row Upper row to start replacement from.
     * @param col Left column to start replacement from.
     */
    
    private void setSquare( YCbCr square[][], int row, int col )
    {
        int offsetY = row * 8,
            offsetX = col * 8;

        for ( int y = 0, i = 0; y < 8; y++ )
        {
            for ( int x = 0; x < 8; x++, i++ )
            {
				this.image.setRGB( offsetX + x, offsetY + y, square[y][x].getRGB() );
            }
        }
    }



    /**
     * Extracts luminance component from block.
     *
     * @param square Block of size 8x8 to extract luminance from.
     * @return Two-dimensional array of size 8x8 carrying luminance components of the block. 
     */

    private static double[][] extractLuminance( YCbCr square[][] )
    {
        double result[][] = new double[8][8];

        for ( int y = 0; y < 8; y++ )
        {
            for ( int x = 0; x < 8; x++ )
            {
                result[y][x] = square[y][x].Y;
            }
        }

        return result;
    }


    
    /**
     * Embeds luminance components to the block.
     *
     * @param block Block of 8x8 YCbCr color pixels.
     * 
     * @param luminance Two-dimensional array of size 8x8 carrying luminance components to embed.
     *
     */

    private static void embedLuminance( YCbCr block[][], double luminance[][] )
    {
        for ( int y = 0; y < 8; y++ )
        {
            for ( int x = 0; x < 8; x++ )
            {
                block[y][x].Y = luminance[y][x];
            }
        }
    }


    /**
     * Returns the state of the <code>index</code>-th bit of the container.
     * @param index Index in the container (0 <= <code>index</code> < <code>this.getSize()</code>).
     * @return State of the <code>index</code>-th bit of the container.
     */

    public boolean get( int index )
    {
        assert ( index >= 0 && index < this.getSize() );

        int row = index / this.getHorizontalSquares();
        int col = index % this.getHorizontalSquares();

        YCbCr square[][] = this.getSquare( row, col );

        double block[][] = transform_DCT( extractLuminance( square ) );

        return block[V_1][U_1] > block[V_2][U_2];
    }

    //public double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;

    public void set( boolean value, int index )
    {
        assert ( index >= 0 && index < this.getSize() );

        int row = index / this.getHorizontalSquares();
        int col = index % this.getHorizontalSquares();

        YCbCr square[][] = this.getSquare( row, col );

        double block[][] = transform_DCT( extractLuminance( square ) );
        
        if ( value )
        {
            if ( block[V_1][U_1] < block[V_2][U_2] )
            {
                double t = block[V_1][U_1];
                block[V_1][U_1] = block[V_2][U_2];
                block[V_2][U_2] = t;
            }

            //if ( block[V_2][U_2] < min ) min = block[V_2][U_2];
            //if ( block[V_1][U_1] > max ) max = block[V_1][U_1];
        }
        else
        {
            if ( block[V_1][U_1] > block[V_2][U_2] )
            {
                double t = block[V_1][U_1];
                block[V_1][U_1] = block[V_2][U_2];
                block[V_2][U_2] = t;
            }

            //if ( block[V_1][U_1] < min ) min = block[V_1][U_1];
            //if ( block[V_2][U_2] > max ) max = block[V_2][U_2];
        }


        double strength = Math.abs( block[V_1][U_1] - block[V_2][U_2] );

        if ( strength <= this.grantedStrength )
        {
            double correctionNeeded = this.grantedStrength - strength;

            double maxCorrection = correctionNeeded * ( 1.0 + MAX_DEVIATION );

            double corrA = Util.random( this.strengthPRNG,
                                                correctionNeeded,
                                                maxCorrection
                                               );

            double corrB = ( corrA >= correctionNeeded ) ? 0 : Util.random( this.strengthPRNG,
                                                                                    correctionNeeded - corrA,
                                                                                    maxCorrection    - corrA
                                                                                   );

            if ( this.strengthPRNG.nextBoolean() )
            {
                double t = corrA;
                corrA = corrB;
                corrB = t;
            }

            assert ( corrA >= 0.0 );
            assert ( corrB >= 0.0 );

            assert ( (strength + corrA + corrB) >= this.grantedStrength - 0.05 );
            assert ( (strength + corrA + corrB) <= this.grantedStrength * (1.0 + MAX_DEVIATION) + 0.05 );

            if ( value )
            {
                block[V_1][U_1] += corrA;
                block[V_2][U_2] -= corrB;
            }
            else
            {
                block[V_1][U_1] -= corrA;
                block[V_2][U_2] += corrB;
            }
        }

        embedLuminance( square, inverse_DCT( block ) );

        this.setSquare( square, row, col );
    }



    /**
     * Performs discrete cosine transform on 8x8 block.
     *
     * @param block Block of 8x8 elements to be transformed.
     * @return Transformed block.
     */

    private static double[][] transform_DCT( double block[][] )
    {
        double result[][] = new double[N][N];

        for ( int u = 0; u < N; u++ )
        {
            for ( int v = 0; v < N; v++ )
            {
                result[v][u] = 0.0;

                for ( int x = 0; x < N; x++ )
                {
                    for ( int y = 0; y < N; y++ )
                    {
                        result[v][u] += block[y][x]
                                        * Math.cos( Math.PI * u * ( 2 * x + 1 ) / ( 2.0 * N ) )
                                        * Math.cos( Math.PI * v * ( 2 * y + 1 ) / ( 2.0 * N ) );
                    }
                }

                result[v][u] *= (2.0 / N) * C(u) * C(v);
            }
        }

        return result;
    }



    /**
     * Performs inverse discrete cosine transform on 8x8 block.
     *
     * @param s Block of 8x8 elements.
     * @return Inversely transformed block.
     */

    private static double[][] inverse_DCT( double s[][] )
    {
        double square[][] = new double[8][8];

        for ( int x = 0; x < N; x++ )
        {
            for ( int y = 0; y < N; y++ )
            {
                square[y][x] = 0.0;

                for ( int u = 0; u < N; u++ )
                {
                    for ( int v = 0; v < N; v++ )
                    {
                        square[y][x] += C(u) * C(v) * s[v][u]
                                    * Math.cos( Math.PI * u * ( 2 * x + 1 ) / ( 2.0 * N ) )
                                    * Math.cos( Math.PI * v * ( 2 * y + 1 ) / ( 2.0 * N ) );
                    }
                }

                square[y][x] *= ( 2.0 / N );
            }
        }

        return square;
    }


    
    /**
     * Help method for transform_DCT and inverse_DCT.
     *
     * @param u 0 <= i <= 7.
     * @return If u = 0, C(u) = 2 ^ -0.5, C(u) = 1 otherwise.
     */
    
    private static double C( int u )
    {
        return ( u == 0 ) ? 0.70710678118654752440084436210485 /* = 2 ^ -0.5 */ : 1.0;
    }



    /**
     * Returns the quantization values exploited be JPEG format.
     *
     * @param u  0 <= u <= 8. See description for details.
     * @param v  0 <= v <= 8. See description for details.
     *
     * @return JPEG luminance quantization value for pair (u,v).
     */

    public static int Q( int u, int v )
    {
        assert ( u >= 0 && u <= 7 );
        assert ( v >= 0 && v <= 7 );

        int t[][] =
                {
                    { 16,  11,  10,  16,  24,  40,  51,  61 },
                    { 12,  12,  14,  19,  26,  58,  60,  55 },
                    { 14,  13,  16,  24,  40,  57,  69,  56 },
                    { 14,  17,  22,  29,  51,  87,  80,  62 },
                    { 18,  22,  37,  56,  68, 109, 103,  77 },
                    { 24,  35,  55,  64,  81, 104, 113,  92 },
                    { 49,  64,  78,  87, 103, 121, 120, 101 },
                    { 72,  92,  95,  98, 112, 100, 103,  99 }
                };

        return t[u][v];
    }


    /**
     * This constant determines minimal strength of watermark allowed.
     */
    private final static double MIN_STRENGTH =  10.0;


    /**
     * This constant determines maximal strength of watermark allowed.
     */
    private final static double MAX_STRENGTH = 500.0;


    /**
     * Determines the variation of strength of the watermark from block to block throw the same robustness settings.
     */
    private final static double MAX_DEVIATION = 0.20;


    /**
     * Side of the block.
     */
    public final static int N = 8;

    /**
     * Index in quantization table.
     */
    public final static int U_1 = 4;

    /**
     * Index in quantization table.
     */
    public final static int V_1 = 1;

    /**
     * Index in quantization table.
     */
    public final static int U_2 = 3;

    /**
     * Index in quantization table.
     */
    public final static int V_2 = 2;

    /*
    public final static int U_1 = 1;
    public final static int V_1 = 2;
    public final static int U_2 = 3;
    public final static int V_2 = 0;
    */


    /**
     * The level of robustness of the watermark.
     */
    private double robustness;


    /**
     * Granted delta between values under this level of robustness.
     */

    private double grantedStrength;

    /**
     * Pseudo-random number generator for strength values. 
     */

    private Random strengthPRNG;


    /**
     * Image over which container is created.
     */
    private RasterImage image;
}




