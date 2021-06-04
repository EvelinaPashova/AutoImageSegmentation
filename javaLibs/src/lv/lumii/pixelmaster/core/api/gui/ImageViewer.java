
package lv.lumii.pixelmaster.core.api.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 * Container that displays RasterImage objects.
 *
 * @author Jevgeny Jonas
 */
public final class ImageViewer extends JPanel {
	private Image im;
	private JCanvas panel;
	private JScrollPane sp;
	private Point start, end;
	private boolean rectangularSelectionEnabled = true;

	private ImageViewerModel imageViewerModel;


	/**
	 * Constructs an image viewer with a default model.
	 *
	 * @param rImage The image to display (can be null; ownership: caller)
	 */
	public ImageViewer(RasterImage rImage) {
		this(rImage, new ImageViewerModel());
	}

	/**
	 * Constructs an image viewer with a user-provided model.
	 *
	 * @param rImage The image to display (can be null; ownership: caller)
	 * @param imageViewerModel the underlying model
	 */
	public ImageViewer(RasterImage rImage, ImageViewerModel imageViewerModel) {
		this.setLayout(new BorderLayout());
//		this.setBorder(BorderFactory.createRaisedBevelBorder());
		panel=new JCanvas();
		this.imageViewerModel = imageViewerModel;
		panel.setBackground(Color.darkGray);
		sp=new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBackground(Color.darkGray);
		add(sp, BorderLayout.CENTER);
		setBackground(Color.darkGray);
		setImage(rImage);
	}

	/**
	 * Returns the panel where the image is drawn. It can be used, for example,
	 * to register event listeners.
	 */
	public Component getCanvas() { return panel; }

	/**
	 * Displays new image.
	 * @param rImage The new image (can be null; ownership: caller)
	 */
	public void setImage(RasterImage rImage) {
		imageViewerModel.setPolygonSelection(new Polygon());
		imageViewerModel.setRectangularSelection(null);

		start=end=null;
		panel.releaseSelection();
		if (rImage!=null) {
			im=createImage(rImage.getSource());
			panel.setPreferredSize(new Dimension (rImage.getWidth(), rImage.getHeight()));
			panel.setSize(new Dimension (rImage.getWidth(), rImage.getHeight()));
		}
		else {
			im=null;
			panel.setPreferredSize(new Dimension (0, 0));
			panel.setSize(new Dimension (0, 0));
		}
		panel.repaint();
	}

	/**
	 * Starts the arbitrary selection.
	 */
	public void startSelection() {
		panel.startSelection();
	}

	/**
	 * Releases the arbitrary selection.
	 */
	public void releaseSelection() {
		panel.releaseSelection();
	}

	/**
	 * Adds a vertex to the polygon.
	 */
	public void addPoint() {
		panel.addPoint();
	}

	/**
	 * Removes the currently held vertex of the polygon.
	 */
	public void removePoint() {
		panel.removePoint();
	}

	/**
	 *  Frame for arbitrary selection markers.
	 *  @author Andrey Zhmakin
	 *  Date: Mar 3, 2010
	 *  Time: 6:38:25 PM
	 *
	 */
	private class ArbitrarySelectionPanel extends JPanel implements MouseListener, MouseMotionListener
	{

		private ArbitrarySelectionPanel()
		{
			this.touched = -1;
			this.holding = false;

			this.setBoundaries( new Point2D.Double( 0, 0 ), new Point2D.Double( 0, 0 ) );
		}

		final void setBoundaries( Point2D.Double leftTop, Point2D.Double rightBottom )
		{
			this.boundaries = new Rectangle2D.Double( leftTop.getX(),
													  leftTop.getY(),
													  rightBottom.getX() - leftTop.getX() + 1,
													  rightBottom.getY() - leftTop.getY() + 1 );
		}

		private final void arrangeFirstThree()
		{
			double side     = 100;
			double height   = 0.86602540378443864676372317075294 /* = Sin( 60° ) */ * side;

			imageViewerModel.polygonSelection.xpoints[0] = (int) Math.round(this.boundaries.getX() + 0.5 * this.boundaries.getWidth() + 0.5 * side);
			imageViewerModel.polygonSelection.ypoints[0] = (int) Math.round(this.boundaries.getY() + 0.5 * this.boundaries.getHeight() + (1.0 / 3.0) * height);
			imageViewerModel.polygonSelection.xpoints[1] = (int) Math.round(this.boundaries.getX() + 0.5 * this.boundaries.getWidth() - 0.5 * side);
			imageViewerModel.polygonSelection.ypoints[1] = (int) Math.round(this.boundaries.getY() + 0.5 * this.boundaries.getHeight() + (1.0 / 3.0) * height);
			imageViewerModel.polygonSelection.xpoints[2] = (int) Math.round(this.boundaries.getX() + 0.5 * this.boundaries.getWidth());
			imageViewerModel.polygonSelection.ypoints[2] = (int) Math.round(this.boundaries.getY() + 0.5 * this.boundaries.getHeight() - (2.0 / 3.0) * height);

			// need to recalculate the cached data of the Polygon
			imageViewerModel.polygonSelection.invalidate();

			this.repaint();
		}





		//------------------------------------------------------------------------

		final void addPoint()
		{
			imageViewerModel.polygonSelection.addPoint((int) Math.round(0.5 * this.boundaries.getWidth()), (int) Math.round(0.5 * this.boundaries.getHeight()));

			this.touched++;

			this.repaint();
		}





		//------------------------------------------------------------------------

		final void removePoint()
		{
			if (imageViewerModel.polygonSelection.npoints == 0)
			{
				return;
			}
			else if (imageViewerModel.polygonSelection.npoints == 1)
			{
				imageViewerModel.polygonSelection.npoints = 0;
				this.touched = -1;
				this.repaint();
				return;
			}

			// removing the touched point and shifting the rest of the points to the left by one position
			for (int i = touched; i < imageViewerModel.polygonSelection.npoints - 1; ++i) {
				imageViewerModel.polygonSelection.xpoints[i] = imageViewerModel.polygonSelection.xpoints[i + 1];
				imageViewerModel.polygonSelection.ypoints[i] = imageViewerModel.polygonSelection.ypoints[i + 1];
			}

			imageViewerModel.polygonSelection.npoints--;

			// need to recalculate the cached data of the Polygon
			imageViewerModel.polygonSelection.invalidate();

			this.touched = (this.touched == 0) ? (imageViewerModel.polygonSelection.npoints - 1) : (this.touched - 1);

			this.repaint();
		}





		//------------------------------------------------------------------------

		final void startSelection()
		{
			this.releaseSelection();

			for ( int i = 3; --i >= 0; )
			{
				this.addPoint();
			}

			this.arrangeFirstThree();
		}





		//------------------------------------------------------------------------

		final void releaseSelection()
		{
			while (imageViewerModel.polygonSelection.npoints != 0)
			{
				this.removePoint();
			}
		}





		//------------------------------------------------------------------------

		public void paint( Graphics g )
		{
			super.paint( g );
		}





		//------------------------------------------------------------------------

		final void paintArbitrarySelection( Graphics g )
		{
			if (imageViewerModel.polygonSelection.npoints == 0) { return; }

			Graphics2D g2 = (Graphics2D)g;



			// -------------------- fill selected area --------------------

			g2.setColor( new Color( 0, 255, 0, 50 ) );
			g2.fill( imageViewerModel.polygonSelection );



			// -------------------- draw lines --------------------

			GeneralPath shape = new GeneralPath();

			shape.moveTo( (float)imageViewerModel.polygonSelection.xpoints[0], (float)imageViewerModel.polygonSelection.ypoints[0] );

			for ( int i = imageViewerModel.polygonSelection.npoints; --i >= 1; )
			{
				shape.lineTo( (float)imageViewerModel.polygonSelection.xpoints[i], (float)imageViewerModel.polygonSelection.ypoints[i] );
			}

			shape.closePath();

			g2.setColor( new Color( 0, 0, 0, 127 ) );
			g2.draw( shape );



			// -------------------- draw bullets --------------------

			for ( int i = imageViewerModel.polygonSelection.npoints; --i >= 0; )
			{
				double x = imageViewerModel.polygonSelection.xpoints[i] - BULLET_SIDE * 0.5,
					   y = imageViewerModel.polygonSelection.ypoints[i] - BULLET_SIDE * 0.5;

				g2.setColor( ( i == this.touched ) ? new Color( 255, 0, 0, 127 ) : new Color( 0, 0, 0, 127 ) );
				g2.fill( new Rectangle2D.Double( x, y, BULLET_SIDE, BULLET_SIDE ) );
			}
		}





		//------------------------------------------------------------------------

		public void mousePressed( MouseEvent event )
		{
			if ( imageViewerModel.polygonSelection.npoints == 0 )
			{
				return;
			}

			Point p = event.getPoint();

			for ( int i = 0; i < imageViewerModel.polygonSelection.npoints; i++ )
			{
				double x = imageViewerModel.polygonSelection.xpoints[i] - BULLET_SIDE * 0.5,
					   y = imageViewerModel.polygonSelection.ypoints[i] - BULLET_SIDE * 0.5;

				Rectangle2D rect = new Rectangle2D.Double( x, y, BULLET_SIDE, BULLET_SIDE );

				if ( rect.contains( p ) )
				{
					this.touched = i;
					this.holding = true;
					this.repaint();
					return;
				}
			}
		}





		//------------------------------------------------------------------------

		public void mouseReleased( MouseEvent event )
		{
			this.holding = false;
		}





		//------------------------------------------------------------------------

		public void mouseDragged( MouseEvent event )
		{
			if ( this.isHeld() )
			{
				if ( this.boundaries.contains( event.getPoint() ) )
				{
					imageViewerModel.polygonSelection.xpoints[this.touched] = event.getPoint().x;
					imageViewerModel.polygonSelection.ypoints[this.touched] = event.getPoint().y;

					// need to recalculate the cached data of the Polygon
					imageViewerModel.polygonSelection.invalidate();

					this.repaint();
				}
				else
				{
					int x = event.getPoint().x,
						   y = event.getPoint().y;

					if      ( x < this.boundaries.getMinX() ) { x = (int) Math.round(this.boundaries.getMinX()); }
					else if ( x > this.boundaries.getMaxX() ) { x = (int) Math.round(this.boundaries.getMaxX()); }

					if      ( y < this.boundaries.getMinY() ) { y = (int) Math.round(this.boundaries.getMinY()); }
					else if ( y > this.boundaries.getMaxY() ) { y = (int) Math.round(this.boundaries.getMaxY()); }

					imageViewerModel.polygonSelection.xpoints[this.touched] = x;
					imageViewerModel.polygonSelection.ypoints[this.touched] = y;

					// need to recalculate the cached data of the Polygon
					imageViewerModel.polygonSelection.invalidate();

					this.repaint();
				}
			}
		}





		//------------------------------------------------------------------------

		public final int getPointCount()
		{
			return imageViewerModel.polygonSelection.npoints;
		}





		//------------------------------------------------------------------------

		private final boolean isHeld()
		{
			return this.holding;
		}



		public final void mouseEntered( MouseEvent event ) { }
		public final void mouseExited ( MouseEvent event ) { }
		public void mouseClicked( MouseEvent event ) { }
		public final void mouseMoved  ( MouseEvent event ) { }


		private boolean invariant() {
			
			return true;
		}

		private final static    int             BULLET_SIDE = 10;
		private                 int             touched;
		private                 boolean         holding;
		private                 Rectangle2D     boundaries;
	}

	/**
	 * Canvas.
	 * @author Jevgenijs Jonass
	 */
	private final class JCanvas extends ArbitrarySelectionPanel {

		private JCanvas()
		{
			this.addMouseListener      ( this );
			this.addMouseMotionListener( this );
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (im!=null) {
				g.drawImage(im, 0, 0, null);

				setBoundaries( new java.awt.geom.Point2D.Double( 0, 0 ),
							   new java.awt.geom.Point2D.Double( im.getWidth(null) - 1, im.getHeight(null) - 1 ) );

				if ( getPointCount() > 0 )
				{
					paintArbitrarySelection( g );
					rectangularSelectionEnabled = false;
				}
				else if (rectangularSelectionEnabled && imageViewerModel.getRectangularSelection()!=null) {
					g.setColor(new Color(100, 100, 200));
					g.drawRect(imageViewerModel.getRectangularSelection().x, imageViewerModel.getRectangularSelection().y, imageViewerModel.getRectangularSelection().width-1, imageViewerModel.getRectangularSelection().height-1);
				}
			}
			else
			{
				super.releaseSelection();
			}
		}

		public void mouseClicked(MouseEvent e) {
			super.mouseClicked( e );
			panel.repaint();
		}

		public void mousePressed(MouseEvent e) {
			super.mousePressed( e );
			if ( this.getPointCount() <= 0 )
			{
				rectangularSelectionEnabled = true;
			}
			imageViewerModel.setRectangularSelection(null);
			Point p=e.getPoint();
			//System.out.println("mousePressed: "+p);
			if (im==null || p.x<0 || p.x>=im.getWidth(null) || p.y<0 || p.y>=im.getHeight(null))
				start=null;
			else start=p;
			panel.repaint();
		}

		public void mouseReleased(MouseEvent e) {
			super.mouseReleased( e );
			end=e.getPoint();
			//System.out.println("mouseReleased: "+end);
			if (im==null || start==null || end==null) {
				imageViewerModel.setRectangularSelection(null);
				return;
			}
			if (end.x<0) end.x=0; else if (end.x>=im.getWidth(null)) end.x=im.getWidth(null)-1;
			if (end.y<0) end.y=0; else if (end.y>=im.getHeight(null)) end.y=im.getHeight(null)-1;
			
			int x1=end.x, y1=end.y, x2=start.x, y2=start.y;
			if (x1>x2) {
				int tmp=x1;
				x1=x2;
				x2=tmp;
			}
			if (y1>y2) {
				int tmp=y1;
				y1=y2;
				y2=tmp;
			}
			imageViewerModel.setRectangularSelection(new Rectangle(x1, y1, x2-x1+1, y2-y1+1));
			panel.repaint();
		}

		public void mouseDragged(MouseEvent e) {
			super.mouseDragged( e );
			end=e.getPoint();
			if (im==null || start==null || end==null) {
				imageViewerModel.setRectangularSelection(null);
				return;
			}
			if (end.x<0) end.x=0; else if (end.x>=im.getWidth(null)) end.x=im.getWidth(null)-1;
			if (end.y<0) end.y=0; else if (end.y>=im.getHeight(null)) end.y=im.getHeight(null)-1;

			int x1=end.x, y1=end.y, x2=start.x, y2=start.y;
			if (x1>x2) {
				int tmp=x1;
				x1=x2;
				x2=tmp;
			}
			if (y1>y2) {
				int tmp=y1;
				y1=y2;
				y2=tmp;
			}
			imageViewerModel.setRectangularSelection(new Rectangle(x1, y1, x2-x1+1, y2-y1+1));
			panel.repaint();
		}
	}
}
