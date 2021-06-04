package lv.lumii.pixelmaster.modules.compare.domain;

/**
 * Created by IntelliJ IDEA.
 * User: murcielago
 * Date: Dec 20, 2010
 * Time: 11:14:13 PM
 * To change this template use File | Settings | File Templates.
 */
/*
** Copyright 2005 Huxtable.com. All rights reserved.
*/


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.color.*;
import java.io.File;
import java.io.IOException;

public class BlurFilter {

    private float radius = 10;
	private float bloom = 2;
	private float bloomThreshold = 192;
    private float angle = 0;
	private int sides = 5;

	/**
	 * Set the radius of the kernel, and hence the amount of blur.
	 * @param radius the radius of the blur in pixels.
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

	/**
	 * Get the radius of the kernel.
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

	public void setSides(int sides) {
		this.sides = sides;
	}

	public int getSides() {
		return sides;
	}

	public void setBloom(float bloom) {
		this.bloom = bloom;
	}

	public float getBloom() {
		return bloom;
	}

	public void setBloomThreshold(float bloomThreshold) {
		this.bloomThreshold = bloomThreshold;
	}

	public float getBloomThreshold() {
		return bloomThreshold;
	}



    public static Kernel makeEmboss() {
        float[] matrix = new float[4*2+1];
        matrix[0] = 0.5f;
        matrix[1] = 0.6f;
        matrix[2] = 0.7f;
        matrix[3] = 0.8f;
        matrix[4] = 1.0f;
        matrix[5] = 1.5f;
        matrix[6] = 2.0f;
        matrix[7] = 2.0f;
        matrix[8] = 2.0f;

      /*  matrix[15] = 2.0f;
        matrix[13] = 2.0f;
        matrix[14] = 2.0f; */

        return new Kernel(9, 1, matrix);
    }

    /**
	 * Make a Gaussian blur kernel.
	 */
	public static Kernel makeKernel(float radius) {
		int r = (int)Math.ceil(radius);
		int rows = r*2+1;
        System.out.println(rows);
		float[] matrix = new float[rows];
		float sigma = radius/3;
		float sigma22 = 2*sigma*sigma;
		float sigmaPi2 = 2* ImageMath.PI*sigma;
		float sqrtSigmaPi2 = (float)Math.sqrt(sigmaPi2);
		float radius2 = radius*radius;
		float total = 0;
		int index = 0;
		for (int row = -r; row <= r; row++) {
			float distance = row*row;
			if (distance > radius2)
				matrix[index] = 0;
			else
				matrix[index] = (float)Math.exp(-(distance)/sigma22) / sqrtSigmaPi2;
			total += matrix[index];
			index++;
		}

        //System.out.println(total);
		for (int i = 0; i < rows; i++)
        {	matrix[i] /= total;
            //System.out.println(matrix[i]);
        }

		return new Kernel(rows, 1, matrix);
	}


    public float[][] createFilter( )
    {

        int w = 128;
        int h = 128;

   //     int[] rgb = new int[w*h];
        float[][] mask = new float[2][w*h];

        // Create the kernel
		double polyAngle = Math.PI/sides;
		double polyScale = 1.0f / Math.cos(polyAngle);
		double r2 = radius*radius;
		double rangle = Math.toRadians(angle);
		float total = 0;
        int i = 0;
        for ( int y = 0; y < h; y++ ) {
            for ( int x = 0; x < w; x++ ) {
                double dx = x-w/2f;
                double dy = y-h/2f;
				double r = dx*dx+dy*dy;
				double f = r < r2 ? 1 : 0;
				if (f != 0) {
					r = Math.sqrt(r);
					if ( sides != 0 ) {
						double a = Math.atan2(dy, dx)+rangle;
						a = ImageMath.mod(a, polyAngle*2)-polyAngle;
						f = Math.cos(a) * polyScale;
					} else
						f = 1;
					f = f*r < radius ? 1 : 0;
				}
				total += (float)f;

				mask[0][i] = (float)f;
                mask[1][i] = 0;
                i++;
            }
        }

        // Normalize the kernel
        i = 0;
        for ( int y = 0; y < h; y++ ) {
            for ( int x = 0; x < w; x++ ) {
                mask[0][i] /= total;
                i++;
            }
        }

        return mask;
    }

    
    public BufferedImage filter( BufferedImage src, BufferedImage dst, int type) {
        Convolution Convol = new Convolution();
        Convol.setSrcImage(src);

        if (type == 1) {
            int r = (int)Math.ceil(radius);
            float[][] mask  = new float[2][r*2+1];
            Kernel kernel = makeKernel(radius);
            mask[0] = kernel.getKernelData( null );
            int size = (int)Math.floor(Math.sqrt(r*2+1));

        Convol.setKernel(mask, size);
        }
        /*int r = 4; //(int)Math.ceil(radius);
        float[][] mask  = new float[2][r*2+1];
        Kernel kernel = makeEmboss();
        mask[0] = kernel.getKernelData( null );
        int size = (int)Math.floor(Math.sqrt(r*2+1));

        Convol.setKernel(mask, size);           */
        if (type == 2)
        {
            Convol.setKernel(createFilter(), 128);
        }

        Convol.convolve();

        dst = Convol.getDstImage();

        return dst;
    }

    public BufferedImage filter( BufferedImage src, BufferedImage dst, BufferedImage load) {
        Convolution Convol = new Convolution();
        Convol.setSrcImage(src);

        Convol.setKernel(load);

        Convol.convolve();

        dst = Convol.getDstImage();

        return dst;
    }

	public String toString() {
		return "Blur/Lens Blur...";
	}
}

