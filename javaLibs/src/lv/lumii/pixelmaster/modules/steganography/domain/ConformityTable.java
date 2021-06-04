/**
 * Conformity table for indexes in range [fromIndex..toIndex-1]; a unique number in the range is given for each member of the range.
 *
 * @author Andrey Zhmakin
 *
 * Created on Mar 26, 2010 1:00:23 AM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import java.util.Random;



public class ConformityTable
{
    /**
     * Creates an array of elements. Element value is equal to its position. 
     * 
     * @param fromIndex The lowest integer to be included.
     * @param toIndex   The first integer not included.
     */
    
    public ConformityTable( int fromIndex, int toIndex )
    {
        int size = toIndex - fromIndex;

        this.table = new int[size];

        for ( int i = 0; i < size; i++ )
        {
            this.table[i] = i + fromIndex;
        }

        this.fromIndex = fromIndex;
        this.toIndex   = toIndex;
    }


    /**
     * Creates random permutation of integers.
     *
     * @param fromIndex The lowest integer to be included in permutation.
     * @param toIndex   The first integer not included in permutation.
     * @param seed      Random seed.
     */
    
    public ConformityTable( int fromIndex, int toIndex, long seed )
    {
        if ( toIndex <= fromIndex )
        {
            throw new IndexOutOfBoundsException();
        }

        int size = toIndex - fromIndex;

        Random generator = new Random( seed );
        
        this.table = new int[size];

        for ( int i = size; --i >= 0; )
        {
            this.table[i] = fromIndex + i;
        }

        for ( int i = size; --i >= 1; )
        {
            int swapped         = generator.nextInt(i);
            int temp            = this.table[swapped];
            this.table[swapped] = this.table[i];
            this.table[i]       = temp;
        }

        this.fromIndex = fromIndex;
        this.toIndex   = toIndex;
    }

    

    /**
     * Returns an element corresponding to <code>index</code> in permutation.
     *
     * @param index Index in array (fromIndex <= <code>index</code> < toIndex).
     * @return Random value in range [fromIndex;toIndex[.
     */
    
    public int get( int index )
    {
        if ( ! ( index >= this.fromIndex && index < this.toIndex ) )
        {
            throw new IndexOutOfBoundsException();
        }

        return this.table[ index - this.fromIndex ];
    }



    private int fromIndex;
    private int toIndex;

    private int table[];
}




