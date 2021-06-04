/**
 * Status bar for Image Processing application.
 *
 * @author Andrey Zhmakin
 *
 * Created on Mar 26, 2010 6:39:13 PM
 *
 */

package lv.lumii.pixelmaster.core.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;



public class StatusBar extends JComponent
{

    /**
     * Using this constructor is the proper way to attach the Status Bar to the window.
     *
     * @param canvas Canvas that displays the image in the main window.
	 *		It is used to register mouse listeners for this Status Bar.
     */

    StatusBar( Component canvas )
    {
        super();
        super.setPreferredSize( new Dimension( 100, 16 ) );

        PositionUpdaterListener positionUpdaterListener = new PositionUpdaterListener();
        MyListener              myListener              = new MyListener();

        this.labelStatus = new JLabel();
        this.labelStatus.setBorder( BorderFactory.createLineBorder( /*Color.lightGray*/ new Color( 163, 184, 204 ) ) );
        this.add( this.labelStatus );

        this.labelPosition = new JLabel();
        this.labelPosition.setBorder( BorderFactory.createLineBorder( /*Color.lightGray*/ new Color( 163, 184, 204 ) ) );
        this.add( this.labelPosition );

        this.progressBar = new JProgressBar();
        this.progressBar.setValue( 0 );
        this.progressBar.setStringPainted( true );
        this.add( this.progressBar );

        this.buttonRunGarbageCollector = new JButton( new ImageIcon( getClass().getResource( "/lv/lumii/pixelmaster/core/gui/CollectGarbage.png" ) ) );
        this.setPreferredSize( new Dimension( 16, 16 ) );
        this.buttonRunGarbageCollector.setToolTipText( "Run Garbage collector" );
        this.buttonRunGarbageCollector.setBorder( BorderFactory.createLineBorder( /*Color.lightGray*/ new Color( 163, 184, 204 ) ) );
        this.buttonRunGarbageCollector.addMouseListener( myListener );
        this.add( this.buttonRunGarbageCollector );

        canvas.addMouseMotionListener( positionUpdaterListener );
        canvas.addMouseListener      ( positionUpdaterListener );

        this.addComponentListener( new PositionSpotter() );

        Timer timer = new Timer();
        timer.scheduleAtFixedRate( new ShowHeapState(), (long)0, HEAP_STATE_UPDATE_RATE );
    }



    public void setStatus( String message )
    {
        this.labelStatus.setText( message );
    }


    /*
    public String getStatus()
    {
        return this.labelStatus.getText();
    }
    */


    private JLabel          labelStatus;
    private JLabel          labelPosition;
    private JProgressBar    progressBar;
    private JButton         buttonRunGarbageCollector;

    private final static long HEAP_STATE_UPDATE_RATE = 100;



    private class ShowHeapState extends TimerTask
    {
        public void run()
        {
            Runtime rt = Runtime.getRuntime();

            long free  = rt.freeMemory(),
                 total = rt.totalMemory();

            long occupied = total - free;

            long totalM = ( total    / 1000000 /* 1048576*/ ),
                 usedM  = ( occupied / 1000000 /* 1048576*/ );

            progressBar.setValue( (int)( occupied * 100 / total ) );
            progressBar.setString( usedM + "M of " + totalM + "M" );
            progressBar.setToolTipText( "Total heap size: " + totalM + "M Used: " + usedM + "M" );
        }
    }



    private class MyListener implements MouseListener
    {
        public void mouseClicked ( MouseEvent e )
        {
            if ( e.getComponent() == buttonRunGarbageCollector )
            {
                System.runFinalization();
                System.gc();
            }
        }

        public void mouseExited  ( MouseEvent e ) { }
        public void mouseEntered ( MouseEvent e ) { }
        public void mouseReleased( MouseEvent e ) { }
        public void mousePressed ( MouseEvent e ) { }
    }



    private class PositionUpdaterListener implements MouseMotionListener, MouseListener
    {
        public void mouseExited( MouseEvent e )
        {
            update = false;
            labelPosition.setText( "" );
        }

        public void mouseEntered( MouseEvent e )
        {
            update = true;
        }

        private void updatePosition( Point p )
        {
            if ( update )
            {
                labelPosition.setText( (int)p.getY() + ":" + (int)p.getX() );
            }
        }

        public void mouseMoved  ( MouseEvent e ) { updatePosition( e.getPoint() ); }
        public void mouseDragged( MouseEvent e ) { updatePosition( e.getPoint() ); }

        private boolean update = false;

        public void mouseReleased( MouseEvent e ) { }
        public void mousePressed ( MouseEvent e ) { }
        public void mouseClicked ( MouseEvent e ) { }
    }



    private class PositionSpotter implements ComponentListener
    {
        public void componentResized( ComponentEvent e )
        {
            Rectangle mainRectangle = e.getComponent().getBounds();

            int maxX = mainRectangle.width - 1;

            labelStatus                 .setBounds( 0           , 0 , maxX - 192 , 16 );
            labelPosition               .setBounds( maxX - 191  , 0 , 74         , 16 );
            progressBar                 .setBounds( maxX - 116  , 0 , 99         , 16 );
            buttonRunGarbageCollector   .setBounds( maxX - 16   , 0 , 16         , 16 );
        }

        public void componentHidden ( ComponentEvent e ) { }
        public void componentMoved  ( ComponentEvent e ) { }
        public void componentShown  ( ComponentEvent e ) { }
    }
}



 