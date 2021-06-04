
package lv.lumii.pixelmaster.modules.steganography.gui;

import lv.lumii.pixelmaster.modules.steganography.domain.FinalActions;
import lv.lumii.pixelmaster.modules.steganography.domain.LinearProgressEstimation;

import javax.swing.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.BitSet;
import lv.lumii.pixelmaster.modules.steganography.domain.BitString;

/**
 * File loading thread; A helper class for loadFile( File file, BitString result,
 *                                                   java.awt.Container owner, FinalActions actions ).
 *
 * @see static void BitStringLoader.loadFile( File file, BitString result, java.awt.Container owner, FinalActions actions )
 *
 */
public class BitStringLoader extends Thread
{

	public BitStringLoader( File file, BitString result, ProgressMonitor pm, FinalActions actions )
	{
		this.result     = result;
		this.pm         = pm;
		this.actions    = actions;
		this.file   = file;
	}



    /**
     * A more sophisticated mean to load a file.
     *
     * @see FinalActions
     * @see ProgressMonitor
     *
     * @param file The file to be loaded.
     * @param result   Object of class <code>BitString</code> to receive the content of the file.
     * @param owner    Parent frame for ProgressMonitor window.
     * @param actions  Actions to perform on different ending scenarios.
     */

    public static void loadFile( File file, BitString result, java.awt.Container owner, FinalActions actions )
    {
        ProgressMonitor progressMonitor = new ProgressMonitor( owner, "Loading message from file", "", 0, 100 );
        progressMonitor.setMillisToPopup( 0 );
        progressMonitor.setMillisToDecideToPopup( 0 );

        BitStringLoader fileLoader = new BitStringLoader( file, result, progressMonitor, actions );
        fileLoader.start();
    }



	public void run()
	{
		try
		{
			InputStream in = new FileInputStream( this.file );
			int fileSize = in.available();

			this.result.setSize( fileSize << 3 );

			this.pm.setMinimum( 0 );
			this.pm.setMaximum( fileSize );

			this.progress = new LinearProgressEstimation( 0, fileSize );

			for ( int i = 0, j = 0; j < fileSize; j++ )
			{
				// update progress and estimate completion time on every 128 bytes read
				if ( (j & 127) == 0 )
				{
					this.updateProgress( j );
				}

				if ( this.pm.isCanceled() )
				{
					in.close();
					this.pm.close();

					this.result.setSize(0);
					this.actions.doOnAbort();
					return;
				}

				int v = in.read();

				for ( int k = 0; k < 8; i++, k++ )
				{
					if ( ((v >> k) & 1) == 1 ) { this.result.set  ( i ); }
					else                       { this.result.clear( i ); }
				}
			}

			this.updateProgress( fileSize );

			in.close();
			this.pm.close();

			this.actions.doOnSuccess();
		}
		catch( IOException e )
		{
			this.actions.doOnFailure();
		}
	}



	private void updateProgress( int position )
	{
		progress.setProgress( position );

				this.pm.setProgress( position );
				this.pm.setNote( "Progress: " + BitStringLoader.formatProgress( this.progress.getProgress() )
									+ ". Estimated time left: "
									+ LinearProgressEstimation.toTime( this.progress.estimateTimeLeft() )
								);
	}


	private final static DecimalFormat progressFormat = new DecimalFormat( "0.0%" );

	private static String formatProgress( double progress )
	{
		return BitStringLoader.progressFormat.format( progress );
	}


	private LinearProgressEstimation progress;

	private ProgressMonitor pm;
	private BitString       result;
	private File          file;
	private FinalActions    actions;
}
