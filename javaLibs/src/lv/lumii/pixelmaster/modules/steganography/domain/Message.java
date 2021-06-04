/**
 * Provides methods for encoding and decoding structures of a low-level message.
 *
 * @author Andrey Zhmakin
 *
 * Created on May 27, 2010 1:25:32 AM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain;

import lv.lumii.pixelmaster.modules.steganography.domain.ecc.ReedSolomon;



public class Message
{
    /**
     * Produces low-level message which is ready for embedment into container.
     * 
     * @param message Original user message.
     * @param ECC_strength Strength of error correction codes in per cents ( 0 <= ECC_strength <= 100 ).
     * @return Low-level message which is ready for embedment into container.
     */
    
    public static BitString produceLowLevelMessage( BitString message, int ECC_strength )
    {
        // bring ECC_strength to inner constraints
        ECC_strength /= 10;
        if ( ECC_strength % 2 == 1 )
        {
            ECC_strength++;
        }
        ECC_strength *= 10;

        BitString result = new BitString( 60 );

        // Encode ECC_strength in header
        {
            assert ( ECC_strength >= 0 && ECC_strength <= 100 ) : "ECC_strength is out of bounds!";
            assert ( (ECC_strength / 10 ) % 2 == 0 ) : "Illegal ECC strength!";

            ReedSolomon hc = new ReedSolomon( SERVICE_N, SERVICE_K );

            int[] raw = new int[SERVICE_K];

            for ( int i = 0; i < SERVICE_K; i++ )
            {
                raw[i] = (ECC_strength / 10) & 15;
            }

            int[] parity = hc.encode( raw );

            int[] cdd = new int[hc.N];

            System.arraycopy( parity, 0, cdd, 0            , parity.length );
            System.arraycopy( raw   , 0, cdd, parity.length, raw.length    );

            assert ( hc.M == 4 );

            for ( int i = 0, j = 0; i < cdd.length; i++ )
            {
                for ( int k = 0; k < hc.M; k++, j++ )
                {
                    if ( ((cdd[i] >> k) & 1) == 1 )
                    {
                        result.set( j );
                    }
                }
            }
        }

        // Encode message length
        {
            ReedSolomon sc = new ReedSolomon( SERVICE_N, SERVICE_K );

            int size = message.size();

            int[] raw = new int[SERVICE_K];

            for ( int i = 0; i < (32 / 4); i++ )
            {
                raw[i] = ( size >> (i * 4) ) & 15;
            }

            int[] parity = sc.encode( raw );

            int index = SIZE_OF_ECC_STRENGTH;

            for ( int i : parity )
            {
                for ( int j = 0; j < 4; j++ )
                {
                    result.set( ((i >> j) & 1) == 1, index++ );
                }
            }

            for ( int i : raw )
            {
                for ( int j = 0; j < 4; j++ )
                {
                    result.set( ((i >> j) & 1) == 1, index++ );
                }
            }
        }

        // now encode the body of the message
        if ( ECC_strength == 0 )
        {
            result.attach( message );
        }
        else
        {
            int K = BODY_N - ECC_strength / 10;

            if ( K < BODY_N )
            {
                ReedSolomon bc = new ReedSolomon(BODY_N, K );

                assert ( result.size() == SERVICE_DATA_SIZE );

                int index = SERVICE_DATA_SIZE;

                // split message into cardinal number of blocks
                int blocks = message.size() / (K * bc.M) + ( ( message.size() % (K * bc.M) != 0 ) ? 1 : 0 );

                assert ( bc.M == 4 );

                for ( int b = 0; b < blocks; b++ )
                {
                    BitString block = message.get( b * (K * bc.M), (b + 1) * (K * bc.M) );

                    int[] raw = new int[K];

                    for ( int i = 0, j = 0; i < K; i++ )
                    {
                        for ( int m = 0; m < bc.M; m++, j++ )
                        {
                            if ( block.get(j) )
                            {
                                raw[i] |= 1 << m;
                            }
                        }
                    }

                    int[] parity = bc.encode( raw );

                    for ( int i : parity )
                    {
                        for ( int j = 0; j < bc.M; j++, index++ )
                        {
                            result.set( ((i >> j) & 1) == 1, index );
                        }
                    }

                    for ( int i : raw )
                    {
                        for ( int j = 0; j < bc.M; j++, index++ )
                        {
                            result.set( ((i >> j) & 1) == 1, index );
                        }
                    }
                }
            }
            else
            {
                result.attach( message );
            }
        }

        return result;
    }



    /**
     * Decodes clause containing the strength of error correction codes.
     * 
     * @param clause <code>SIZE_OF_ECC_STRENGTH</code>-bit long clause containing the strength
     *               of error correction codes.
     * 
     * @return Strength of error correction codes. If it's impossible to repair "-1" ir returned.
     * 
     */
    
    public static int getEccStrength( BitString clause )
    {
        assert ( clause.size() == SIZE_OF_ECC_STRENGTH ) : "Wrong size of ECC strength clause!";

        int[] block = new int[BODY_N];

        for ( int i = 0, j = 0; i < BODY_N; i++ )
        {
            for ( int k = 0; k < 4; k++, j++ )
            {
                if ( clause.get(j) )
                {
                    block[i] |= 1 << k;
                }
            }
        }

        ReedSolomon hc = new ReedSolomon(SERVICE_N, SERVICE_K);

        int diagnosis = hc.decode( block );

        switch ( diagnosis )
        {
            case ReedSolomon.DIAGNOSIS_REPAIRED:
            case ReedSolomon.DIAGNOSIS_HEALTHY:
                {
                    assert ( block[14] == block[13] );
                    return block[14];
                }

            case ReedSolomon.DIAGNOSIS_UNREPAIRABLE:
                {
                    int reg[] = new int[4];

                    for ( int i = (hc.N - hc.K); i < hc.N; i++ )
                    {
                        for ( int j = 0; j < hc.M; j++ )
                        {
                            if ( ( (block[i] >> j) & 1 ) == 1 ) reg[j]++;
                            else                                reg[j]--;
                        }
                    }

                    int strengthOfEcc = 0;

                    for ( int i = 0; i < 4; i++ )
                    {
                        if ( Math.abs( reg[i] ) >= RESTORE_THRESHOLD )
                        {
                            if ( reg[i] > 0 )
                            {
                                strengthOfEcc |= 1 << i;
                            }
                        }
                        else
                        {
                            return -1;
                        }
                    }

                    return strengthOfEcc;
                }

            default:
                return -1;
        }
    }



    public static int decodeMessageSize( BitString sizeClause )
    {
        assert ( sizeClause.size() == SIZE_OF_SIZE_CLAUSE );

        int[] block = new int[SERVICE_N];

        for ( int i = 0, j = 0; i < SERVICE_N; i++ )
        {
            for ( int k = 0; k < 4; k++, j++ )
            {
                if ( sizeClause.get(j) )
                {
                    block[i] |= 1 << k;
                }
            }
        }

        ReedSolomon sc = new ReedSolomon( SERVICE_N, SERVICE_K );

        sc.decode( block );

        int result = 0;

        for ( int i = SERVICE_N - SERVICE_K, k = 0; i < SERVICE_N; i++ )
        {
            for ( int j = 0; j < 4; j++, k++ )
            {
                if ( ( (block[i] >> j) & 1 ) == 1 )
                {
                    result |= 1 << k;
                }
            }
        }

        return result;
    }



    /**
     * Decodes and corrects all the possible errors in user message on the basis on the info provided.
     *
     * @param body          Body of the low level-message.
     * @param strengthOfEcc Strength of error correction codes.
     * @param messageSize   Size of the user message.
     * @param message       The place user message will be copied to.
     *
     * @return Returns the status of repair process. See <code>ReedSolomon.DIAGNOSIS_*</code>.
     *
     */
    
    public static int decodeBody( BitString body, int strengthOfEcc, int messageSize, BitString message )
    {
        if ( strengthOfEcc == 0 )
        {
            message.setSize( 0 );
            message.attach( body );
            return 0;
        }

        int diagnosis = ReedSolomon.DIAGNOSIS_HEALTHY;

        int K = BODY_N - strengthOfEcc;

        ReedSolomon bc = new ReedSolomon( BODY_N, K );

        int blocks = body.size() / (BODY_N * 4) + ( (body.size() % (BODY_N * 4) != 0) ? 1 : 0 );

        message.setSize( messageSize );
        int index = 0;
        
        for ( int b = 0; b < blocks; b++ )
        {
            BitString bits = body.get( b * (BODY_N * 4), (b + 1) * (BODY_N * 4) );

            int[] block = new int[BODY_N];

            for ( int i = 0, j = 0; i < BODY_N; i++ )
            {
                for ( int k = 0; k < 4; k++, j++ )
                {
                    if ( bits.get(j) )
                    {
                        block[i] |= 1 << k;
                    }
                }
            }


            switch ( bc.decode( block ) )
            {
                case ReedSolomon.DIAGNOSIS_HEALTHY:
                    //System.out.print( 'H' );
                    break;
                
                case ReedSolomon.DIAGNOSIS_REPAIRED:
                    //System.out.print( 'R' );
                    if ( diagnosis != ReedSolomon.DIAGNOSIS_UNREPAIRABLE )
                    {
                        diagnosis = ReedSolomon.DIAGNOSIS_REPAIRED;
                    }
                    break;
                
                case ReedSolomon.DIAGNOSIS_UNREPAIRABLE:
                    //System.out.print( 'U' );
                    diagnosis = ReedSolomon.DIAGNOSIS_UNREPAIRABLE;
                    break;
            }

            for ( int i = BODY_N - K; i < BODY_N; i++ )
            {
                for ( int j = 0; j < 4; j++, index++ )
                {
                    message.set( (( block[i] >> j ) & 1) == 1, index );
                }
            }
        }

        message.setSize( messageSize );

        return diagnosis;
    }



    /**
     * Calculates the amount of ECC codes to be embedded under given message size and ECC strength.
     * 
     * @param messageSize   Size of the user message.
     * @param strengthOfEcc Desired strength of ECC codes in percents ( 0 <= <code>strengthOfEcc</code> <= 100 )
     *
     * @return Number of bits padding and error correction codes will bring to the message.
     * 
     */
    
    public static int predictEccOverhead( int messageSize, int strengthOfEcc )
    {
        if ( strengthOfEcc / 10 == 0 )
        {
            return 0;
        }

        int K = BODY_N - strengthOfEcc / 10;

        
        int blocks = messageSize / (4 * K);

        int result = 0;

        if ( ( messageSize % (4 * K) ) != 0 )
        {
            result += (4 * K) - ( messageSize % (4 * K) );
            
            blocks++;
        }

        result += blocks * 4 * (BODY_N - K);

        return result;
    }



    /**
     * Number of duplicating bits that should match to take a decision to interpret ECC strength mark as sensible.
     */

    private final static int RESTORE_THRESHOLD = 7;



    public final static int SERVICE_N = 15;
    public final static int SERVICE_K =  9;

    public final static int BODY_N    = 15;


    //public final static int ECC_SIZE = SERVICE_N * 4;


    public final static int SIZE_OF_ECC_STRENGTH = 60;
    public final static int SIZE_OF_SIZE_CLAUSE  = 60;
    public final static int SERVICE_DATA_SIZE    = SIZE_OF_ECC_STRENGTH + SIZE_OF_SIZE_CLAUSE;


}




