/**
 * Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 1, 2010 2:41:24 PM
 *
 */

package lv.lumii.pixelmaster.modules.grapheditor.gui;

import lv.lumii.pixelmaster.modules.spw.domain.graph.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Stack;


public class WorkPanel extends JPanel implements MouseListener, MouseMotionListener
{
    public WorkPanel( UGraph graph )
    {
        this.addMouseListener      ( this );
        this.addMouseMotionListener( this );

        this.scale = 10.0;
        this.markerSide = 10;

        this.graph = graph;

        this.curr = null;
        this.prev = null;
        this.holding = false;

        this.mode = MODE_NORMAL;

        this.bulletsShown       = true;
        this.axesShown          = true;
        this.centerOfMassShown  = false;

        this.offsetX = -0.5;
        this.offsetY = -0.5;

        this.repaint();
    }



    public void setGraph( UGraph graph )
    {
        this.graph = graph;
        this.repaint();
    }



    public UGraph getGraph()
    {
        return this.graph;
    }



    public void createEdge()
    {
        if ( this.curr == null || this.prev == null )
        {
            return;
        }

        this.graph.addEdge( this.prev.getX(), this.prev.getY(),
                            this.curr.getX(), this.curr.getY() );
        
        this.repaint();
    }

    

    public void removeEdge()
    {
        if ( this.curr == null || this.prev == null )
        {
            return;
        }

        this.graph.deleteEdge( this.prev.getX(), this.prev.getY(),
                               this.curr.getX(), this.curr.getY() );

        this.repaint();
    }

    

    public void removeVertex()
    {
        if ( this.curr == null )
        {
            return;
        }

        this.graph.deleteVertex( this.curr );

        this.curr = this.prev;
        this.prev = null;

        this.repaint();
    }



    public void createVertex()
    {
        Vertex vertex;

        if ( this.cursorPosition == null )
        {
            vertex = this.graph.addVertex( (int)( this.offsetX + this.getWidth () * 0.5 / this.scale ),
                                           (int)( this.offsetY + this.getHeight() * 0.5 / this.scale ) );
        }
        else
        {
            vertex = this.graph.addVertex( (int)Math.round( this.offsetX + this.cursorPosition.getX() / this.scale ),
                                           (int)Math.round( this.offsetY + this.cursorPosition.getY() / this.scale ) );
        }

        if ( vertex != null )
        {
            this.prev = this.curr;
            this.curr = vertex;
        }

        if ( this.mode == MODE_AUTO_CONNECT && this.prev != null )
        {
            this.createEdge();
        }

        this.repaint();
    }



    public void setScale( double scale )
    {
        assert (scale != 0) : "Dummy scale!";

        this.scale = scale;
        this.markerSide = (scale < MIN_MARKER_SIDE) ? MIN_MARKER_SIDE : (int)scale;
        this.repaint();
    }


    
    public void setMode( int mode )
    {
        this.mode = mode;
    }



    public void setAxesVisibility( boolean visible )
    {
        this.axesShown = visible;
        this.repaint();
    }



    public boolean getAxesVisibility()
    {
        return this.axesShown;
    }



    public void setBulletsVisible( boolean bulletsShown )
    {
        this.bulletsShown = bulletsShown;
        this.repaint();
    }



    public boolean areBulletsVisible()
    {
        return this.bulletsShown;
    }



    public void setCenterOfMassVisibility( boolean centerShown )
    {
        this.centerOfMassShown = centerShown;
        this.repaint();
    }



    public boolean getCenterOfMassVisibility()
    {
        return this.centerOfMassShown;
    }



    private Vertex getVertexUnderPoint( Point2D screenPoint )
    {
        for ( Iterator<Vertex> it = this.graph.vertices(); it.hasNext(); )
        {
            Vertex vertex = it.next();

            double x = ( vertex.getX() - this.offsetX ) * this.scale - 0.5 * this.markerSide,
                   y = ( vertex.getY() - this.offsetY ) * this.scale - 0.5 * this.markerSide;

            Rectangle2D bullet = new Rectangle2D.Double( x, y, this.markerSide, this.markerSide );

            if ( bullet.contains( screenPoint ) )
            {
                return vertex;
            }
        }

        return null;
    }



    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        // draw background
        if ( this.scale >= 4.0 )
        {
            g.setColor( new Color( 127, 127, 127,  16 ) );

            int width  = (int)(this.getWidth () / this.scale) + 2,
                height = (int)(this.getHeight() / this.scale) + 2;

            double dx = lv.lumii.pixelmaster.modules.grapheditor.domain.Math.fraction( this.offsetX ) + 0.5;
            double dy = lv.lumii.pixelmaster.modules.grapheditor.domain.Math.fraction( this.offsetY ) + 0.5;

            for ( int row = 0; row < height; row++ )
            {
                for ( int col = (row + (int)Math.ceil(this.offsetX) + (int)Math.ceil(this.offsetY)) & 1; col < width; col += 2 )
                {
                    ((Graphics2D)g).fill( new Rectangle2D.Double( ( col - dx ) * this.scale,
                                                                  ( row - dy ) * this.scale,
                                                                  (int)this.scale,
                                                                  (int)this.scale
                                                                 )
                                         );
                }
            }
        }

        // draw axes X and Y
        if ( this.axesShown )
        {
            g.setColor( new Color( 0, 0, 255, 127 ) );

            int y = (int)( -this.offsetY * this.scale );
            g.drawLine( 0, y, this.getWidth(), y );

            int x = (int)( -this.offsetX * this.scale );
            g.drawLine( x, 0, x, this.getHeight() );
        }
        
        if ( this.graph == null )
        {
            return;
        }

        g.setColor( Color.BLACK );

        // draw edges
        for ( Iterator<GraphEdge> it = this.graph.edges(); it.hasNext(); )
        {
            GraphEdge edge = it.next();

            g.drawLine( (int)( (edge.first .getX() - this.offsetX) * this.scale ),
                        (int)( (edge.first .getY() - this.offsetY) * this.scale ),
                        (int)( (edge.second.getX() - this.offsetX) * this.scale ),
                        (int)( (edge.second.getY() - this.offsetY) * this.scale ) );
        }

        // draw bullets for vertices
        if ( this.bulletsShown)
        {
            for ( Iterator<Vertex> it = this.graph.vertices(); it.hasNext(); )
            {
                Vertex vertex = it.next();

                double x = ( vertex.getX() - this.offsetX ) * this.scale - 0.5 * this.markerSide,
                       y = ( vertex.getY() - this.offsetY ) * this.scale - 0.5 * this.markerSide;

                if      ( vertex == curr ) { g.setColor( new Color( 255,   0,   0, 127 )  ); }
                else if ( vertex == prev ) { g.setColor( new Color(   0, 255,   0, 127 )  ); }
                else                       { g.setColor( new Color(   0,   0,   0, 127 )  ); }

                ((Graphics2D)g).fill( new Rectangle2D.Double( x, y, markerSide, markerSide) );
            }
        }

        // draw center of mass
        if ( this.centerOfMassShown )
        {
            try
            {
                Point2D com = this.graph.getCenterOfMass();

                g.setColor( Color.YELLOW );

                int x = (int)( (com.getX() - this.offsetX) * this.scale );
                int y = (int)( (com.getY() - this.offsetY) * this.scale );

                g.drawLine( x - 5, y - 5, x + 5, y + 5 );
                g.drawLine( x - 5, y + 5, x + 5, y - 5 );
            }
            catch ( UGraph.EdgelessGraphException   e ) { }
            catch ( UGraph.WeightlessGraphException e ) { }
        }
    }



    public void mouseDragged( MouseEvent event )
    {
        if ( this.holding )
        {
            this.curr.setLocation( (int)( this.offsetX + (event.getPoint().getX() + 0.5 * this.scale ) / this.scale ),
                                   (int)( this.offsetY + (event.getPoint().getY() + 0.5 * this.scale ) / this.scale ) );
        }
        else
        {
            double deltaX = this.draggedFrom.getX() - event.getPoint().getX(),
                   deltaY = this.draggedFrom.getY() - event.getPoint().getY();

            switch ( this.buttonHeld )
            {
                case MouseEvent.BUTTON1:
                {
                    this.offsetX += deltaX / this.scale;
                    this.offsetY += deltaY / this.scale;
                }
                break;

                case MouseEvent.BUTTON3:
                    {
                        Stack<Vertex> stack = new Stack<Vertex>();

                        for ( Iterator<Vertex> it = this.graph.vertices(); it.hasNext(); )
                        {
                            Vertex vertex = it.next();

                            stack.push( vertex );
                        }

                        while ( !stack.empty() )
                        {
                            Vertex vertex = stack.pop();
                            
                            vertex.setLocation( (int)Math.round( vertex.getX() - deltaX ),
                                                (int)Math.round( vertex.getY() - deltaY ) );
                        }
                    }
                    break;
            }
            
            this.draggedFrom = event.getPoint();
        }

        this.repaint();
    }



    public void mouseReleased( MouseEvent event )
    {
        this.holding = false;
    }



    public void mousePressed( MouseEvent event )
    {
        this.buttonHeld = event.getButton();
        
        Vertex vertex = this.getVertexUnderPoint( event.getPoint() );

        if ( vertex != null )
        {
            if ( vertex != this.curr )
            {
                this.prev = this.curr;
                this.curr = vertex;
                
                this.repaint();
            }

            this.holding = true;
        }

        this.draggedFrom = event.getPoint();
    }



    public void mouseMoved( MouseEvent event )
    {
        if ( this.cursorPosition != null )
        {
            this.cursorPosition.setLocation( event.getPoint().getX(), event.getPoint().getY() );
        }

        if ( this.mode == MODE_DRAWING )
        {
            this.createVertex();
            this.createEdge();
        }
    }



    public void mouseEntered( MouseEvent event )
    {
        this.cursorPosition = new Point2D.Double( event.getPoint().getX(), event.getPoint().getY() );
    }



    public void mouseExited( MouseEvent event )
    {
        this.cursorPosition = null;
    }



    public void mouseClicked( MouseEvent event )
    {
        if ( this.getVertexUnderPoint( event.getPoint() ) == null )
        {
            this.curr = null;
            this.prev = null;
            this.repaint();
        }
    }



    private Point2D cursorPosition;
    private int buttonHeld;

    private Point2D draggedFrom;
    private double offsetX;
    private double offsetY;

    private double scale;
    private UGraph graph;

    private Vertex curr;
    private Vertex prev;
    private boolean holding; 

    private int markerSide;

    private int mode;

    private boolean bulletsShown;
    private boolean axesShown;
    private boolean centerOfMassShown;

    private final static int MIN_MARKER_SIDE = 10;

    public final static int MODE_NORMAL         = 0;
    public final static int MODE_AUTO_CONNECT   = 1;
    public final static int MODE_DRAWING        = 2;
}




