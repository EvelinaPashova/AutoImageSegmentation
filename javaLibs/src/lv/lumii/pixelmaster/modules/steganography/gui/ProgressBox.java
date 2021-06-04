/**
 * Implements a progress bar which is more capable than <code>ProgressMonitor</code>.
 *
 * @author Andrey Zhmakin
 *
 * Created on 08.06.2010 15:13:16
 *
 */

package lv.lumii.pixelmaster.modules.steganography.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ProgressBox
{
    public ProgressBox( Component owner )
    {
        this.initComponents( owner );
    }


    public ProgressBox( Component owner, String title, String note, int minimum, int maximum )
    {
        this.initComponents( owner );
        this.setMinimum( minimum );
        this.setMaximum( maximum );
        this.setNote( note );
        this.setTitle( title ) ;
    }


    /**
     * Initializes <code>Swing</code> components, i.e. dialog and its controls.
     *
     * @param owner Parent frame for <code>ProgressBox</code> dialog.
     * 
     */

    private void initComponents( Component owner )
    {
        this.box = new JDialog( (JDialog)owner );

        this.box.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );

        this.box.addWindowListener( new WindowAdapter()
                                    {
                                        public void windowClosing( WindowEvent e )
                                        {
                                            cancelled = true;
                                        }
                                    }
                                   );

        this.box.setSize( 350, 100 );

        //this.setLayout( new BoxLayout( this, BoxLayout.PAGE_AXIS ) );
        //this.setLayout( new GridLayout( 0, 1 ) );
        this.box.setLayout( new BorderLayout() );

        Container pane = this.box.getContentPane();

        this.note = new JLabel();
        pane.add( this.note, BorderLayout.NORTH );

        this.progressBar = new JProgressBar( 0, 100 );
        this.progressBar.setStringPainted( true );
        this.progressBar.setString( "" );
        pane.add( this.progressBar, BorderLayout.CENTER );

        this.cancel = new JButton( "Cancel" );

        this.cancel.addActionListener( new ActionListener()
                                        {
                                            public void actionPerformed( ActionEvent e )
                                            {
                                                cancelled = true;
                                            }
                                        }
                                      );

        pane.add( this.cancel, BorderLayout.SOUTH );
    }


    public void close()
    {
        this.box.dispose();
    }


    public void show()
    {
        this.box.setVisible( true );
    }


    public void hide()
    {
        this.box.setVisible( false );
    }


    public void setIndeterminate( boolean value )
    {
        this.progressBar.setIndeterminate( value );
    }


    public void setTitle( String title )
    {
        this.box.setTitle( title );
    }


    public void setNote( String text )
    {
        this.note.setText( text );
    }


    public void setString( String s )
    {
        this.progressBar.setString( s );
    }


    public void setMinimum( int minimum )
    {
        this.progressBar.setMinimum( minimum );
    }


    public void setMaximum( int maximum )
    {
        this.progressBar.setMaximum( maximum );
    }


    public void setProgress( int progress )
    {
        this.progressBar.setValue( progress );
    }


    public boolean isCanceled()
    {
        return this.cancelled;
    }


    public void setCancelable( boolean value )
    {
        this.cancel.setEnabled( value );
        // TODO: Disable also close control on dialog
    }


    private boolean         cancelled;
    private JDialog         box;
    private JProgressBar    progressBar;
    private JLabel          note;
    private JButton         cancel;
}




