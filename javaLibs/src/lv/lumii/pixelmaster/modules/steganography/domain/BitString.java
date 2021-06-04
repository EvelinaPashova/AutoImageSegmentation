/**
 * Object of class <code>BitString</code> implements a string of bits with strictly defined size.
 *
 * @author Andrey Zhmakin
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.steganography.domain.FinalActions;
import lv.lumii.pixelmaster.modules.steganography.domain.LinearProgressEstimation;

import javax.swing.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.BitSet;



public class BitString extends BitSet
{

    /**
     * Creates a new object of class <code>BitString</code> with initial size of 0 (zero) bits.
     *
     */

    public BitString()
    {
        super();
        
        this.size = 0;
    }



    /**
     * Creates an object of class <code>BitString</code> with initial size of <code>size</code> bits.
     * 
     * @param size Initial size of <code>BitString</code>.
     *
     */

    public BitString( int size )
    {
        super( size );
        
        this.size = size;
    }



    /**
     * Creates a replica of <code>rhr</code> object with exactly the same size.
     *
     * @param rhr Object to be replicated.
     *
     */

    public BitString( BitSet rhr )
    {
        super();
        
        for ( int i = 0; i < rhr.length(); i++ )
        {
            if ( rhr.get( i ) )
            {
                this.set( i );
            }
        }

        this.size = rhr.size();
    }


    
    /**
     * Creates an object of class <code>BitString</code> on the basis of class <code>String</code>.
     *
     * @param string <code>String</code> object to be converted.
     * 
     * @return Object of class <code>BitString</code> created from <code>String</code>; bit order is little-endian.
     *
     */

    public static BitString encodeString( String string )
    {
        BitString result = new BitString( string.length() << 4 );

        int k = 0;
        for ( int i = 0; i < string.length(); i++ )
        {
            for ( int j = 0; j < 16; j++ )
            {
                if ( ((string.charAt(i) >> j) & 1) == 1 ) { result.set  ( k++ ); }
                else                                      { result.clear( k++ ); }
            }
        }
        
        return result;
    }



    /**
     * Decodes an object of class <code>BitString</code> as </code>String<code>.
     * Bit order is assumed to be little-endian. BitString is padded with zero bits to be integer dividend of 16.
     *
     * @see BitString BitString.encodeString( String string ) 
     *
     * @param bits <code>BitString</code> object to be converted.
     *
     * @return String composed from <code>bits</code> object.
     * 
     */
    
    public static String decodeString( BitString bits )
    {
        char array[] = new char[bits.size() >> 4];

        for ( int b = 0, c = 0, limit = bits.size() & 0xFFFFFFF0; b < limit; )
        {
            char ch = 0;

            for ( int i = 0; i < 16; i++, b++ )
            {
                if ( bits.get(b) )
                {
                    ch |= 1 << i;
                }
            }

            array[c++] = ch;
        }

        return new String( array );
    }



    /**
     * The basic mean to load a file to <code>BitString</code>.
     *
     * @param file File to load.
     * 
     * @return Content of the file; Bit order in bytes is assumed to be little-endian.
     *
     * @throws IOException Thrown if IO error occurs.
     * 
     */

    public static BitString loadFile( File file )
        throws IOException
    {
        InputStream in = new FileInputStream( file );

        int fileSize = in.available();

        BitString result = new BitString( fileSize << 3 );

        for ( int i = 0, j = 0; j < fileSize; j++ )
        {
            int v = in.read();

            for ( int k = 0; k < 8; i++, k++ )
            {
                if ( ((v >> k) & 1) == 1 ) { result.set  ( i ); }
              //else                       { result.clear( i ); }
            }
        }

        in.close();

        return result;
    }



    /**
     * Saves the content of <code>BitString</code> object to file; the string is padded with zero bits to be placed
     * in a file consisting of 8 bit bytes.
     *
     * @param file Specifies the file the <code>BitString</code> object should be saves to.
     *
     * @throws IOException An exception of class <code>IOException</code> is thrown is case of failure.
     *
     */

    public void saveToFile( File file )
            throws IOException
    {
        OutputStream out = new FileOutputStream( file );

        int fileSize = ( this.size() >> 3 ) + ( ((this.size() & 7) != 0) ? 1 : 0 );

        for ( int i = 0, j = 0; j < fileSize; j++ )
        {
            int buffer = 0;

            for ( int k = 0; k < 8; k++, i++ )
            {
                if ( this.get(i) )
                {
                    buffer |= 1 << k;
                }
            }

            out.write( buffer );
        }

        out.close();
    }



    /**
     * Set the <code>index<code> bit to the state of <code>value</code>.
     *
     * @param value Value to be assigned.
     * @param index Index of the bit to get a new value.
     *
     */
    
    public void set( boolean value, int index )
    {
        if ( index >= this.size )
        {
            this.size = index + 1;
        }

        if ( value )
        {
            super.set( index );
        }
        else if ( index < super.length() )
        {
            super.clear( index );
        }
    }



    /**
     * Sets the <code>index</code>-th bit of <code>BitString</code> to true;
     * If <code>index</code> >= <code>this.size()</code>, <code>BitString</code> is extended automatically.
     *
     * @param index Index of the bit to be set to true.
     * 
     */
    
    public void set( int index )
    {
        if ( index >= this.size )
        {
            this.size = index + 1;
        }
        
        super.set( index );
    }



    /**
     * Create object of class <code>BitString</code> and fills it with the content of this [fromIndex..toIndex-1].
     * 
     *
     * @param fromIndex Start index to cut out.
     * @param toIndex   Index of the first behind the copied segment. 
     *
     * @return Segment of size (toIndex - fromIndex) with the content of this [fromIndex..toIndex-1].
     *  
     */

    public BitString get( int fromIndex, int toIndex )
    {
        BitString result = new BitString( super.get( fromIndex, toIndex ) );

        result.size = toIndex - fromIndex;

        return result;
    }



    /**
     * Returns current size of <code>BitString</code> object. 
     *
     * @return Current size of <code>BitString</code> object.
     *
     */

    public int size()
    {
        return this.size;
    }



    /**
     * Changes size <code>BitString</code> object. 
     *
     * @param size New size of the object.
     * 
     */

    public void setSize( int size )
    {
        this.size = size;
    }


    
    /**
     * Compares <code>this</code> with other <code>BitString</code> object.
     *
     * @param bits <code>BitString</code> object <code>this</code> should be compared with.
     *
     * @return Returns <code>true</code> iff <code>this</code> and <code>bits</code> are of equal size and contents;
     *         <code>false</code> otherwise.
     *
     */

    public boolean equals( BitString bits )
    {
        if ( this.size != bits.size )
        {
            return false;
        }

        for ( int i = 0; i < this.size; i++ )
        {
            if ( this.get(i) != bits.get(i) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Attaches the contents of <code>bits</code> to <code>this</code>.
     * @param bits <code>BitString</code> to attach.
     */

    public void attach( BitString bits )
    {
        for ( int i = bits.size, j = this.size; --i >= 0; )
        {
            this.set( bits.get( i ), i + j );
        }
    }

    

    /**
     * Converts <code>BitString</code> to the string of "1"-s and "0"-s.
     *
     * @return <code>String</code>
     */
    public String toString()
    {
        char[] buffer = new char[this.size];

        for ( int i = 0; i < this.size; i++ )
        {
            buffer[i] = this.get(i) ? '1' : '0';
        }

        return new String( buffer );
    }



    private int size;
}




