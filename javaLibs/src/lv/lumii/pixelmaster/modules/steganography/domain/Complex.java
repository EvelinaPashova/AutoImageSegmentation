/**
 * Object of class <code>Complex</code> represents a complex number. 
 *
 * @author Andrey Zhmakin
 *
 * Created on May 7, 2010 4:43:21 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.grapheditor.domain.Math;



public class Complex
{
    public final static Complex M_I     = new Complex( 0,  1.0 );
    public final static Complex M_NI    = new Complex( 0, -1.0 );
    public final static Complex M_PI    = new Complex( java.lang.Math.PI );
    public final static Complex M_E     = new Complex( java.lang.Math.E  );



    public Complex()
    {
        this.re = 0;
        this.im = 0;
    }



    public Complex( Complex r )
    {
        this.re = r.re;
        this.im = r.im;
    }



    public Complex( double re )
    {
        this.re = re;
        this.im = 0;
    }



    public Complex( double re, double im )
    {
        this.re = re;
        this.im = im;
    }



    public Complex copy()
    {
        return new Complex( this );
    }



    public String toString()
    {
        return this.re + " + i" + this.im;
    }



    public void set( Complex r )
    {
        this.re = r.re;
        this.im = r.im;
    }



    public void set( double re, double im )
    {
        this.re = re;
        this.im = im;
    }



    public double getRe()
    {
        return this.re;
    }


    
    public double getIm()
    {
        return this.im;
    }

    

    public void setRe( double re )
    {
        this.re = re;
    }



    public void setIm( double im )
    {
        this.im = im;
    }



    public Complex add( Complex r )
    {
        return new Complex( this.re + r.re, this.im + r.im );
    }



    public Complex add( double r )
    {
        return new Complex( this.re + r, this.im );
    }



    public Complex sub( Complex r )
    {
        return new Complex( this.re - r.re, this.im - r.im );
    }



    public Complex sub( double r )
    {
        return new Complex( this.re - r, this.im );
    }



    public Complex mul( Complex r )
    {
        return new Complex( this.re * r.re - this.im * r.im,
                            this.re * r.im + this.im * r.re );
    }



    public Complex mul( double r )
    {
        return new Complex( this.re * r, this.im * r );
    }



    public Complex div( Complex r )
    {
        double k = 1.0 / ( Math.sqr(r.re) - Math.sqr(r.im) );

        Complex result = this.mul( new Complex( r.re, -r.im ) );
        result.mul( k );

        return this;
    }



    public Complex div( double r )
    {
        r = 1.0 / r;

        return new Complex( this.re * r, this.im * r );
    }



    public static Complex add( Complex a, Complex b ) { return a.add( b ); }
    public static Complex sub( Complex a, Complex b ) { return a.sub( b ); }
    public static Complex mul( Complex a, Complex b ) { return a.mul( b ); }
    public static Complex div( Complex a, Complex b ) { return a.div( b ); }



    public Complex exp()
    {
        Complex result = new Complex( java.lang.Math.sin( this.im ), java.lang.Math.cos( this.im ) );
        result = result.mul( java.lang.Math.exp( this.re ) );

        return result;
    }



    private double re;
    private double im;
}




