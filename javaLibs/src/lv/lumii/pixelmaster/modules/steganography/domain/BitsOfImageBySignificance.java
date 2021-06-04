/**
 * Class for providing access to the bits of image by their significance, least significant first.
 *
 * @author Andrey Zhmakin
 * 
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import java.awt.*;



public class BitsOfImageBySignificance
{
    /**
     * Creates an object of class <code>BitsOfImageBySignificance</code> linked to object of class <code>util.RasterImage</code>;
     * You still have to call initConformityTables( long key ) before using anything.
     *
     * @param image A non-null object of class <code>util.RasterImage</code>.
     *
     */

    public BitsOfImageBySignificance( RasterImage image )
    {
        assert ( image != null );

        this.image = image;
        this.key   = null;
        this.table = null;
    }


    
    /**
     * Creates an object of class <code>BitsOfImageBySignificance</code> linked to object of class <code>util.RasterImage</code>;
     * Every instance is ready to use with encryption key <code>key</code>.
     *
     * @param image             Image watermark to be embedded in.
     * @param key               Encryption key.
     * 
     */
    
    public BitsOfImageBySignificance( RasterImage image, long key )
    {
        assert ( image != null );
        
        this.image = image;

        this.initConformityTables( key );
    }



    /**
     * Initializes conformity tables for message encryption.
     *
     * @param key Encryption key.
     * 
     */
    
    public void initConformityTables( long key )
    {
        if ( this.key != null && key == this.key )
        {
            return;
        }
        
        this.key = key;
        
        this.table = new ConformityTable[NUM_OF_LAYERS];

        for ( int i = 0; i < NUM_OF_LAYERS; i++ )
        {
            this.table[i] = new ConformityTable( 0, this.getLayerSize(), key + i );
        }
    }


    
    /**
     * Gets size of bit container.
     *
     * @return Returns size of bit container.
     *
     */

    public int getSize()
    {
        return image.getWidth() * image.getHeight() * 24;
    }



    /**
     * Gets size of image layer.
     *
     * @return Returns size of image layer.
     *
     */

    public int getLayerSize()
    {
        return image.getWidth() * image.getHeight() * 3;
    }


    
    /**
     * Returns the index in layer.
     *
     * @param index Index in container.
     * @return Returns index in layer.
     * @throws IndexOutOfBoundsException Throws if <code>index</code> is out of layer's bounds.
     */

    public int getIndexInLayer( int index )
        throws IndexOutOfBoundsException
    {
        if ( index < 0 || index >= getSize() )
        {
            throw new IndexOutOfBoundsException();
        }

        int result = index % getLayerSize();

        assert ( result >= 0 && result < getLayerSize() ) : "Index in layer is out of bounds!";

        return result;
    }

    

    /**
     * Used to determinate the layer <code>index</code>-th bit corresponds to.
     *
     * @param index Index in the bit container.
     *
     * @return Returns index of the layer [0..7] corresponding to the bit at the <code>index</code>-th position.
     *
     * @throws IndexOutOfBoundsException Exception is thrown if
     *                                   <code>index</code> is out of [0..<code>getSize()</code> - 1] bounds.
     * 
     */

    public int getLayer( int index )
        throws IndexOutOfBoundsException
    {
        if ( index < 0 || index >= getSize() )
        {
            throw new IndexOutOfBoundsException();
        }

        int layer = index / getLayerSize();

        assert (layer >= 0 && layer < NUM_OF_LAYERS) : "Internal error: Layer index calculated incorrectly!";

        return layer;
    }


    
    /**
     * Used to determinate color component <code>index</code>-th bit corresponds to.
     *
     * @param index Index in the bit container.
     *
     * @return Returns 0, 1 or 2 if index-th bit corresponds to red, green or blue component respectively.
     * 
     * @throws IndexOutOfBoundsException Exception is thrown if <code>index</code> is out of [0..<code>getSize()</code> - 1] bounds.
     * 
     */

    public int getComponent( int index )
        throws IndexOutOfBoundsException
    {
        if ( index < 0 || index >= getSize() )
        {
            throw new IndexOutOfBoundsException();
        }

        int component = index % 3;

        assert ( component >= 0 && component <= 2 ) : "Not an RGB component!";

        return component;
    }



    /**
     * See description of the return value. 
     *
     * @param index Index in the bit container.
     *
     * @return Returns index of the <code>util.RasterImage.pixels</code> array element corresponding to the <code>index</code>-th bit on the container.
     *
     * @throws IndexOutOfBoundsException Exception is thrown if <code>index</code> is out of [0..<code>getSize()</code> - 1] bounds.
     *
     */

    public int getIndexInImage( int index )
        throws IndexOutOfBoundsException
    {
        if ( index < 0 || index >= getSize() )
        {
            throw new IndexOutOfBoundsException();
        }

        int indexInImage = ( index % getLayerSize() ) / 3;

        assert ( indexInImage >= 0 && indexInImage <= getLayerSize() ) : "You are out of image bounds!"; 

        return indexInImage;
    }


    
    /**
     * Used to determinate the state of the certain bit of the container.
     *
     * @param layer Refers to the bit [0..7] of the color component.
     *
     * @param indexInImage Index in the <code>util.RasterImage.pixels</code> array.
     *
     * @param component Is 0, 1 or 2 which stands for red, green or blue respectively.
     *
     * @return Returns state on the corresponding bit of container.
     *
     * @throws IndexOutOfBoundsException Exception is thrown if <code>index</code> is out of [0..<code>getSize()</code> - 1] bounds.
     *
     */

    public boolean get( int layer, int indexInImage, int component )
        throws IndexOutOfBoundsException
    {
        if ( layer     < 0 || layer     >= NUM_OF_LAYERS ) throw new IndexOutOfBoundsException();
        if ( component < 0 && component >= 3             ) throw new IndexOutOfBoundsException();

		Color c = image.get( indexInImage );

		boolean state = false;

		switch ( component )
		{
			case 0: state = ((c.getRed  () >>> layer) & 1) == 1; break;
			case 1: state = ((c.getGreen() >>> layer) & 1) == 1; break;
			case 2: state = ((c.getBlue () >>> layer) & 1) == 1; break;
		}

		return state;
    }


    
    /**
     * Used to change the state of the certain bit of the container.
     *
     * @param layer Refers to the bit [0..7] of the color component.
     *
     * @param indexInImage Index in the <code>util.RasterImage.pixels</code> array.
     *
     * @param component Is 0, 1 or 2 which stands for red, green or blue respectively.
     *
     * @param state The in which the <code>index</code>-th bit currently is.
     *
     * @throws IndexOutOfBoundsException Exception is thrown if <code>index</code> is out of [0..<code>getSize()</code> - 1] bounds.
     * 
     */

    public void set( int layer, int indexInImage, int component, boolean state )
        throws IndexOutOfBoundsException
    {
        if ( layer     < 0 || layer     >= NUM_OF_LAYERS ) throw new IndexOutOfBoundsException();
        if ( component < 0 && component >= 3             ) throw new IndexOutOfBoundsException();

        int setter = state ? (1 << layer) : 0;
        int filter = 0x000000FF ^ (1 << layer);

		Color c = image.get( indexInImage );

		switch ( component )
		{
			case 0:
				int r = (c.getRed() & filter) | setter;
				c = new Color( r, c.getGreen(), c.getBlue() );
				break;

			case 1:
				int g = (c.getGreen() & filter) | setter;
				c = new Color( c.getRed(), g, c.getBlue() );
				break;

			case 2:
				int b = (c.getBlue() & filter) | setter;
				c = new Color( c.getRed(), c.getGreen(), b );
				break;
		}

		image.set( indexInImage, c );
    }


    
    /**
     * Used to determinate the state of the <code>index</code>-th bit of the container.
     *
     * @param index Index in the bit container.
     *
     * @return Returns state of the <code>index</code>-th bit in the bit container.
     *
     * @throws IndexOutOfBoundsException Exception is thrown if <code>index</code> is out of [0..<code>getSize()</code> - 1] bounds.
     * 
     */
    
    public boolean get( int index )
        throws IndexOutOfBoundsException
    {
        int layer        = getLayer         ( index );

        int indexInLayer = this.table[layer].get( this.getIndexInLayer(index) );

        int indexInImage = getIndexInImage  ( indexInLayer );
        int component    = getComponent     ( indexInLayer );

        return get( layer, indexInImage, component );
    }


    
    /**
     * Used to change the state of the <code>index</code>-th bit of the container.
     *
     * @param index Index of the bit in the bit container.
     * 
     * @param state State to be assigned to the <code>index</code>-th bit in the bit container.
     *
     * @throws IndexOutOfBoundsException Exception is thrown if <code>index</code> is out of [0..<code>getSize()</code> - 1] bounds.
     * 
     */
    
    public void set( int index, boolean state )
        throws IndexOutOfBoundsException
    {
        int layer        = getLayer         ( index );

        int indexInLayer = this.table[layer].get( this.getIndexInLayer(index) );

        int indexInImage = getIndexInImage  ( indexInLayer );
        int component    = getComponent     ( indexInLayer );
        
        set( layer, indexInImage, component, state );
    }



    public class ImageTooSmallToHoldDataException  extends Exception { }
    public class BrokenMessageException            extends Exception { }


    /**
     * Encodes a message in container in the most trivial way.
     *
     * @param data Message to hide.
     * @throws ImageTooSmallToHoldDataException If the image is too small for such a large message.
     */
    public void encode_trivially( BitString data )
        throws ImageTooSmallToHoldDataException
    {
        if ( data.size() > this.getSize() )
        {
            throw new ImageTooSmallToHoldDataException();
        }

        BitString message = Util.concatenate( Util.convert(data.size()), data );

        try
        {
            for ( int i = 0; i < message.size(); i++ )
            {
                this.set( i, message.get(i) );
            }
        }
        catch ( IndexOutOfBoundsException e ) { assert false : "Index out of bounds!"; }

    }

    

    /**
     * Extracts the whole contents of the container to <code>BitString</code>.
     *
     * @return Returns contents of the container.
     */
    private BitString toBitString()
    {
        BitString result = new BitString( this.getSize() );

        try
        {
            for ( int i = 0; i < this.getSize(); i++ )
            {
                if ( this.get( i ) )
                {
                    result.set( i );
                }
            }
        }
        catch ( IndexOutOfBoundsException e ) { assert false : "This could not happen!"; }
        
        return result;
    }

    

    /**
     * Extracts message encoded with encode_trivially( BitString data ).
     *
     * @throws ImageTooSmallToHoldDataException Thrown if image is too small to hold message of any size.
     * @throws BrokenMessageException           If there is no message inside or message is broken.
     *
     * @return Returns contents of the message, if found.
     */

    public BitString decode_trivially()
        throws ImageTooSmallToHoldDataException,
               BrokenMessageException
    {
        if ( this.getSize() < 32 )
        {
            throw new ImageTooSmallToHoldDataException();
        }

        BitString message = toBitString();

        int dataSize = Util.toInt( message.get( 0, 31 ) );

        if ( dataSize < 0 || dataSize > this.getSize() - 32 )
        {
            throw new BrokenMessageException();
        }

        BitString result = message.get( 32, 32 + dataSize );

        assert (result.size() == dataSize) : "Wrong result size!";

        return result;
    }



    public class IndexOutOfBoundsException  extends Exception { }



    public final static int NUM_OF_LAYERS = 8;

    private Long            key;

    private RasterImage     image;
    private ConformityTable table[];
}




