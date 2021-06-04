package lv.lumii.pixelmaster.modules.compare.domain;

import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

/**
 *
 * Convolution with FFT
 *
 * @author Aivars Šāblis
 *
 */

public class Convolution {

    private BufferedImage src, dst;
    private float[][] mask;
    private int maskSize;
    private int[] rgb;
    private float[][] gb;
    private float[][] ar;
    private float[][] alpha, red, green, blue;
    private float radius = 10;
    public int w, h, log2rows, log2cols, cols, rows;

    public void setSrcImage(BufferedImage src) {
        this.src = src;
    }

    public BufferedImage getDstImage() {
        return this.dst;
    }

    public void setKernel(float[][] mask, int size) {
        this.mask = mask;
        this.maskSize = size;
    }


    /**
     * Adjust kernel to adjusted image size
     */
    private void adjustKernel() {
        if (w != maskSize) {
            float[][] tmp = new float[2][w*h];
            
            int padding = (w - maskSize) / 2;
            int i = 0;
            int pos = w*padding+padding;
            for (int y = 0; y < maskSize; y++) {
                for (int x = 0; x < maskSize; x++) {
                    tmp[0][pos] = mask[0][i];
                    i++;
                    pos++;
                }
                pos = pos + (2*padding);
            }

            this.mask = tmp;
            this.maskSize = w;   
        }

    }

    public void setKernel(BufferedImage kernel) {
        Kernel picKernel = null;

        //TODO: convert image to kernel
//        kernel = new BufferedImage(kernel.getWidth(), kernel.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
        float[][] tmp = new float[2][kernel.getWidth() * kernel.getHeight()];
        int[] g = new int[kernel.getWidth()* kernel.getHeight()];
        kernel.getRGB(0,0,kernel.getWidth(), kernel.getHeight(), g, 0, kernel.getWidth());
        int total = 0;
        for (int i = 0; i < g.length; i++) {
            int p = ((g[i] >> 16) & 0xff);
                tmp[0][i] = p;
                tmp[1][i] = 0;
                total += p;

        }

        for (int i = 0; i < g.length; i++) {
                tmp[0][i] /= total;           
        }

        this.mask = tmp;
        this.maskSize = kernel.getWidth();
    }

    private void convertImage()
    {
        int width = src.getWidth();
        int height = src.getHeight();

        if ( dst == null )
            dst = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );


        int adjustedWidth = nextPow2(width);
        //int adjustedHeight = nextPow2(height);
        int adjustedHeight = adjustedWidth;

        //System.out.println("adjustedWidth" + adjustedWidth + "adjustedHeight" + adjustedHeight);

        this.w = adjustedWidth;
        this.h = adjustedHeight;
        this.cols = w;
        this.rows = h;
        this.log2rows = log2(w);
        this.log2cols = log2(h);

        rgb = new int[w*h];
        //this.gb = new float[2][w*h];
        //this.ar = new float[2][w*h];
        this.alpha = new float[2][w*h];
        this.red = new float[2][w*h];
        this.green = new float[2][w*h];
        this.blue = new float[2][w*h];

        //copy image into arrays
        src.getRGB(0, 0, width ,height, rgb, 0, w);

        for (int i = 0; i < rgb.length; i++) {
           //this.ar[0][i] = ((rgb[i] >> 24) & 0xff);
           //this.ar[1][i] = ((rgb[i] >> 16) & 0xff);
           //this.gb[0][i] = ((rgb[i] >> 8) & 0xff);
           //this.gb[1][i] = (rgb[i] & 0xff);
           this.alpha[0][i] = ((rgb[i] >> 24) & 0xff);
           this.red[0][i] = ((rgb[i] >> 16) & 0xff);
           this.green[0][i] = ((rgb[i] >> 8) & 0xff);
           this.blue[0][i] = (rgb[i] & 0xff);
        }
    }

    public void convolve()
    {
        int width = src.getWidth();
        int height = src.getHeight();

        //make destination image
        if ( dst == null )
            dst = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        
        //TODO: convert image
        this.convertImage();
        this.adjustKernel();

       /* for (int i = 0; i < mask[0].length; i++) {
            if (mask[0][i] > 0) {
                System.out.println(":" + mask[0][i]);
            }
        }   */


        FFT fft = new FFT( Math.max(log2rows, log2cols) );
        // Transform into frequency space
        fft.transform2D( mask[0], mask[1], cols, rows, true );


        fft.transform2D( alpha[0], alpha[1], cols, rows, true );
        fft.transform2D( red[0], red[1], cols, rows, true );
        fft.transform2D( green[0], green[1], cols, rows, true );
        fft.transform2D( blue[0], blue[1], cols, rows, true );

        //Multiply the transformed pixels by transformed kernel
        int i = 0;
        for ( int y = 0; y < h; y++ ) {
            for ( int x = 0; x < w; x++ ) {
                float re = alpha[0][i];
                float im = alpha[1][i];
                float rem = mask[0][i];
                float imm = mask[1][i];
                alpha[0][i] = re*rem-im*imm;
                alpha[1][i] = re*imm+im*rem;

                re = red[0][i];
                im = red[1][i];
                red[0][i] = re*rem-im*imm;
                red[1][i] = re*imm+im*rem;

                re = green[0][i];
                im = green[1][i];
                green[0][i] = re*rem-im*imm;
                green[1][i] = re*imm+im*rem;

                re = blue[0][i];
                im = blue[1][i];
                blue[0][i] = re*rem-im*imm;
                blue[1][i] = re*imm+im*rem;

                i++;
            }
        }

        //Transform back
        fft.transform2D( alpha[0], alpha[1], cols, rows, false );
        fft.transform2D( red[0], red[1], cols, rows, false );
        fft.transform2D( green[0], green[1], cols, rows, false );
        fft.transform2D( blue[0], blue[1], cols, rows, false );

        // Convert back to RGB pixels, with quadrant remapping
        int row_flip = w >> 1;
        int col_flip = h >> 1;
        int index = 0;

        //FIXME-don't bother converting pixels off image edges
        for ( int y = 0; y < w; y++ ) {
            int ym = y ^ row_flip;
            int yi = ym*cols;
            for ( int x = 0; x < w; x++ ) {
                int xm = yi + (x ^ col_flip);
                int a = (int)alpha[0][xm];
                int r = (int)red[0][xm];
                int g = (int)green[0][xm];
                int b = (int)blue[0][xm];

                //System.out.println("a:" + a + "r:" + r + "g:" + g + "b:" + b);

                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                rgb[index++] = argb;
            }
        }

        dst.setRGB(0, 0, src.getWidth(), src.getHeight(), rgb, 0, w );
    }

    private int nextPow2( int x ) {
        --x;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return ++x;
    }

    private int log2( int n ) {
        int m = 1;
        int log2n = 0;

        while (m < n) {
            m *= 2;
            log2n++;
        }
        return m == n ? log2n : -1;
    }
}
