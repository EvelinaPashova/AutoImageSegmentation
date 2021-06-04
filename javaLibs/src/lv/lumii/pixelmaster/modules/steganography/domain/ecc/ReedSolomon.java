/**
 * Objects of class <code>ReedSolomon</code> provide implementation of Reed-Solomon error correction codes.
 * This class currently works on only models RS(15,k), where k in {1; 3; 5; 7; 9; 11; 13}. 
 *
 * Based on code developed by Simon Rockliff.
 *
 * @see <a href="http://www.eccpage.com/rs.c">Original C code by Simon Rockliff</a>
 *
 * @author Andrey Zhmakin
 *
 * Created on May 21, 2010 2:26:18 AM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.domain.ecc;


public class ReedSolomon
{
    /**
     * Initializes RS codes with N=15, K=9 over Galois field GF(2^4).
     *  
     */

    public ReedSolomon()
    {
        this.N = 15;
        this.T =  3;
        this.K =  this.N - 2 * this.T;

        assert ( this.K == 9 );

        this.initialize();
    }



    /**
     * Initializes Reed-Solomon RS(n,k) model with arbitrary (almost) n and k. 
     *
     * @param n The number of symbols per block, always 15.
     * @param k The number of data symbols per block, even integer in range [1..13].
     *
     */
    
    public ReedSolomon( int n, int k )
    {
        //assert ( n >= 2 && n <= 255 ) : "n should be in bounds [2..255]";
        //assert ( k >= 1 && k <= n   ) : "k should be in bounds [1..n]";
        assert ( n == 15 )           : "n should always be equal to 15";
        assert ( k >= 1 && k <= 13 ) : "k should be in bounds [1..13]";
        assert ( k % 2 == 1 )        : "k should be even";

        this.N = n;
        this.T = (n - k) / 2;
        this.K = n - 2 * this.T;

        this.initialize();
    }



    /**
     * Produces parity symbols for <code>data</code>.
     *
     * @param data Group of K M-bit symbols in polynomial (i.e. usual) form, parity to be produced for.
     *
     * @return Group of (N - K) M-bit symbols in polynomial form.
     *
     */

    public int[] encode( int[] data )
    {
        assert ( data.length == this.K );

        int[] b = new int[ (N - K) ];

        for ( int i = K - 1; i >= 0; i-- )
        {
            int feedback = index_of[ data[i] ^ b[(N - K) - 1] ];

            if ( feedback != -1 )
            {
                for ( int j = (N - K) - 1; j > 0; j-- )
                {
                    if ( g[j] != -1 )
                    {
                        b[j] = b[j - 1] ^ alpha_to[ (g[j] + feedback) % N ];
                    }
                    else
                    {
                        b[j] = b[j - 1];
                    }
                }

                b[0] = alpha_to[ (g[0] + feedback) % N ];
            }
            else
            {
                for ( int j = N - K - 1; j > 0; j-- )
                {
                    b[j] = b[j - 1];
                }
                
                b[0] = 0;
            }
        }

        return b;
    }



    /**
     * Tries to find and fix errors in a group of N M-bit wide symbols, where first (N - K) symbols are parity symbols
     * and the following K symbols are data symbols.
     *
     * If there are errors found, an attempt to repair the <code>data</code> is taken.
     * If an attempt fails, <code>data</code> is left as it is.
     *
     * @param data Group of N M-bit symbols of the the format described above.
     *
     * @return Zero if no errors found, 1 if errors are believed to be repaired, -1 on unrepairable errors.  
     *
     */
    
    public int decode( int[] data )
    {
        int[] recd = new int[N];

        for ( int i = 0; i < N; i++ )
        {
            recd[i] = index_of[data[i]];
        }

        int[] s = new int[(N - K) + 1];

        boolean syn_error = false;

        // form the syndromes
        for ( int i = 1; i <= (N - K); i++ )
        {
            s[i] = 0;

            for ( int j = 0; j < N; j++ )
            {
                if ( recd[j] != -1 )
                {
                    s[i] ^= alpha_to[(recd[j] + i * j) % N];
                }
            }

            // non-zero syndrome means error
            if ( s[i] != 0 )
            {
                syn_error = true;
            }

            // convert syndrome from polynomial to index form
            s[i] = index_of[s[i]];
        }

        // if errors are found, try to correct them
        if ( syn_error )
        {
            // compute error location polynomial via Berlekamp iterative algorithm
            int[] d = new int[N - K + 2];
            
            //d[0] = 0;
            d[1] = s[1];


            int[][] elp = new int[N - K + 2][N - K];

            //elp[0][0] = 0;
            elp[1][0] = 1;

            for ( int i = 1; i < (N - K); i++ )
            {
                elp[0][i] = -1;
                //elp[1][i] =  0;
            }


            int[] l = new int[N - K + 2];

            //l[0] = 0;
            //l[1] = 0;


            int[] u_lu = new int[N - K + 2];
            u_lu[0] = -1;
            //u_lu[1] =  0;

            int u = 0;


            do
            {
                u++;

                if ( d[u] == -1 )
                {
                    l[u + 1] = l[u];

                    for ( int i = 0; i <= l[u]; i++ )
                    {
                        elp[u + 1][i] = elp[u][i];
                        elp[u][i] = index_of[elp[u][i]];
                    }
                }
                else
                {
                    // search for words with greatest u_lu[q] for which d[q] != 0
                    int q = u - 1;

                    while ( (d[q] == -1) && (q > 0) )
                    {
                        q--;
                    }

                    // have found first non-zero d[q]
                    if ( q > 0 )
                    {
                        int j = q;

                        do
                        {
                            j--;

                            if ( (d[j] != -1) && (u_lu[q] < u_lu[j]) )
                            {
                                q = j;
                            }
                        } while ( j > 0 );
                    }


                    // have now found q such that d[u] != 0 and u_lu[q] is maximum
                    // store degree of new elp polynomial
                    l[u + 1] = ( l[u] > (l[q] + u - q) ) ? l[u] : ( l[q] + u - q );

                    // form new elp(x)
                    for ( int i = 0; i < (N - K); i++ )
                    {
                        elp[u + 1][i] = 0;
                    }

                    for ( int i = 0; i <= l[q]; i++ )
                    {
                        if ( elp[q][i] != -1 )
                        {
                            elp[u + 1][i + u - q] = alpha_to[ ( d[u] + N - d[q] + elp[q][i] ) % N ];
                        }
                    }

                    for ( int i = 0; i <= l[u]; i++ )
                    {
                        elp[u + 1][i] ^= elp[u][i];
                        // convert old elp value to index
                        elp[u][i] = index_of[elp[u][i]];
                    }
                }

                u_lu[u + 1] = u - l[u + 1];

                // form (u+1)th discrepancy
                if ( u < (N - K) )
                {
                    if ( s[u + 1] != -1 )
                    {
                        d[u + 1] = alpha_to[s[u + 1]];
                    }
                    else
                    {
                        d[u + 1] = 0;
                    }

                    for ( int i = 1; i <= l[u + 1]; i++ )
                    {
                        if ( (s[u + 1 - i] != -1) && (elp[u + 1][i] != 0) )
                        {
                            d[u + 1] ^= alpha_to[ ( s[u + 1 - i] + index_of[elp[u + 1][i]] ) % N ];
                        }
                    }

                    d[u + 1] = index_of[d[u + 1]];
                }
            } while ( ( u < (N - K) ) && ( l[u + 1] <= T ) );

            u++;

            // can correct error
            if ( l[u] <= T )
            {
                for ( int i = 0; i <= l[u]; i++ )
                {
                    elp[u][i] = index_of[elp[u][i]];
                }

                
                int[] reg = new int[T + 1];

                for ( int i = 1; i <= l[u]; i++ )
                {
                    reg[i] = elp[u][i];
                }

                int count = 0;

                int[] root = new int[T];
                int[] loc  = new int[T];

                for ( int i = 1; i <= N; i++ )
                {
                    int q = 1;

                    for ( int j = 1; j <= l[u]; j++ )
                    {
                        if ( reg[j] != -1 )
                        {
                            reg[j] = (reg[j] + j) % N;
                            q ^= alpha_to[reg[j]];
                        }
                    }

                    // store root and error location number indices
                    if ( q == 0 )
                    {
                        root[count] = i;
                        loc[count] = N - i;
                        count++;
                    }
                }

                int[] z = new int[T + 1];

                // no. roots = degree of elp hence <= T errors
                if ( count == l[u] )
                {
                    for ( int i = 1; i <= l[u]; i++ )
                    {
                        if      ( s[i] != -1 && elp[u][i] != -1 ) z[i] = alpha_to[s[i]] ^ alpha_to[elp[u][i]];
                        else if ( s[i] != -1 && elp[u][i] == -1 ) z[i] = alpha_to[ s[i] ];
                        else if ( s[i] == -1 && elp[u][i] != -1 ) z[i] = alpha_to[ elp[u][i] ];
                        else                                      z[i] = 0;

                        for ( int j = 1; j < i; j++ )
                        {
                            if ( s[j] != -1 && elp[u][i - j] != -1 )
                            {
                                z[i] ^= alpha_to[ ( elp[u][i - j] + s[j] ) % N ];
                            }
                        }

                        // put into index form
                        z[i] = index_of[z[i]];
                    }


                    int[] err = new int[N];

                    for ( int i = 0; i < N; i++ )
                    {
                        //err[i] = 0;

                        recd[i] = ( recd[i] != -1 ) ? alpha_to[recd[i]] : 0;
                    }


                    // compute the numerator or error term first
                    // accounts for z[0]
                    for ( int i = 0; i < l[u]; i++ )
                    {
                        err[loc[i]] = 1;

                        for ( int j = 1; j <= l[u]; j++ )
                        {
                            if ( z[j] != -1 )
                            {
                                err[loc[i]] ^= alpha_to[ ( z[j] + j * root[i] ) % N ];
                            }
                        }

                        if ( err[loc[i]] != 0 )
                        {
                            err[loc[i]] = index_of[ err[loc[i]] ];

                            // form denominator of error term
                            int q = 0;
                            for ( int j = 0; j < l[u]; j++ )
                            {
                                if ( j != i )
                                {
                                    q += index_of[ 1 ^ alpha_to[( loc[j] + root[i] ) % N] ];
                                }
                            }

                            q = q % N;

                            err[loc[i]] = alpha_to[(err[loc[i]] - q + N) % N];

                            // recd[i] must be in polynomial form
                            recd[loc[i]] ^= err[loc[i]];
                        }
                    }

                    System.arraycopy( recd, 0, data, 0, data.length );

                    return DIAGNOSIS_REPAIRED;
                }
                else // no. roots != degree of elp => >T errors and cannot solve
                {
                    return DIAGNOSIS_UNREPAIRABLE;
                }
            }
            else // elp has degree >T hence cannot solve
            {
                return DIAGNOSIS_UNREPAIRABLE;
            }
        }
        else // no non-zero syndromes => no errors
        {
            // Healthy!
            return DIAGNOSIS_HEALTHY;
        }
    }



    /**
     * Initializes inner data structures of the class.
     * It's assumed N, K and T are properly initialized.
     * 
     */
    
    private void initialize()
    {
        this.M = (int)( Math.log( this.N + 1 ) / Math.log( 2 ) ); 

        if ( this.N < 16 )
        {
            this.ip = new int[POLY_4.length];
            //assert ( POLY_4.length == this.ip.length );
            System.arraycopy( POLY_4, 0, this.ip, 0, POLY_4.length );
        }
        else if ( this.N < 256 )
        {
            this.ip = new int[POLY_8.length];
            //assert ( POLY_8.length == this.ip.length );
            System.arraycopy( POLY_8, 0, this.ip, 0, POLY_8.length );
        }
        else
        {
            assert false : "N is too large for this implementation!";
        }

        //assert ( M ==  4 );
        assert ( N == 15 );
        //assert ( K ==  9 );
        //assert ( T ==  3 );

        gen_tabs();
        gen_poly();
    }



    public void debug_print_tables()
    {
        assert ( alpha_to.length == index_of.length );

        System.out.println( "--------------------------------------------------------------" );

        for ( int i = 0; i < alpha_to.length; i++ )
        {
            System.out.println( i + "\t" + alpha_to[i] + "\t" + index_of[i] );
        }

        System.out.println( "--------------------------------------------------------------" );
    }



    public void debug_print_polynomial()
    {
        System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );

        for ( int e : g )
        {
            System.out.print( e + " " );
        }
        System.out.println();
        System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
    }



    /**
     * Generates lookup tables corresponding to Galois field GF(2^M) from the irreducible polynomial.
     *
     */

    private void gen_tabs()
    {
        index_of = new int[N + 1];
        alpha_to = new int[N + 1];

        int mask = 1 ;
        
        alpha_to[M] = 0 ;

        for ( int i = 0; i < M; i++ )
        {
            alpha_to[i] = mask ;
            index_of[alpha_to[i]] = i ;
            
            if ( ip[i] != 0 )
                alpha_to[M] ^= mask ;

            mask <<= 1 ;
        }

        index_of[alpha_to[M]] = M;
        
        mask >>= 1 ;

        for ( int i = M + 1; i < N; i++ )
        {
            if ( alpha_to[i - 1] >= mask )
            {
                alpha_to[i] = alpha_to[M] ^ ( (alpha_to[i - 1] ^ mask) << 1 );
            }
            else
            {
                alpha_to[i] = alpha_to[i - 1] << 1;
            }

            index_of[alpha_to[i]] = i ;
        }
        
        index_of[0] = -1 ;
    }



    /**
     * Obtains the generator polynomial. 
     *
     */

    private void gen_poly()
    {
        g = new int[N - K + 1];

        //assert( ip.length == M + 1 );

        g[0] = 2;
        g[1] = 1;

        for ( int i = 2; i <= N - K; i++ )
        {
            g[i] = 1;

            for ( int j = i - 1; j > 0; j-- )
            {
                if ( g[j] != 0 )
                {
                    g[j] = g[j - 1] ^ alpha_to[ (index_of[g[j]] + i) % N ];
                }
                else
                {
                    g[j] = g[j - 1];
                }
            }

            g[0] = alpha_to[ (index_of[g[0]] + i) % N ];
        }
        
        for ( int i = 0; i <= (N - K); i++ )
        {
            g[i] = index_of[g[i]];
        }
    }



    private int[] alpha_to;
    private int[] index_of;
    private int[] g;

    public int N;
    public int T;
    public int K;

    public int M;

    private int[] ip;

    
    /**
     * These are two irreducible polynomials
     * This one is from the original paper,
     */

    private final static int[] POLY_4 = { 1, 1, 0, 0, 1 };

    /**
     * And this is from ECMA-130.
     *
     * @see <a href="ttp://www.ecma-international.org/publications/files/ECMA-ST/Ecma-130.pdf">Standard ECMA-130. See page 35 of 57 (-25-).</a>
     *
     */
    
    private final static int[] POLY_8 = { 1, 0, 1, 1, 1, 0, 0, 0, 1 };

    
    public final static int DIAGNOSIS_HEALTHY        =  0;
    public final static int DIAGNOSIS_REPAIRED       =  1;
    public final static int DIAGNOSIS_UNREPAIRABLE   = -1;
}




