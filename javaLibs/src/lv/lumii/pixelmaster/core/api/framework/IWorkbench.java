
package lv.lumii.pixelmaster.core.api.framework;

import java.awt.Polygon;
import java.awt.Rectangle;

import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.Constants;

/**
 * This interface represents the user's workbench - the area where he does his
 * work. This area consists of the working image and the selected rectangular area.
 * The working image is displayed in the main window.
 * Every module can query the core for the currently displayed image or the currently
 * selected area, as well as change the currently displayed image.
 * An implementation of this interface will be provided by the core at the
 * {@link IModule#init initialization stage}.
 *
 * @author Jevgeny Jonas
 */
public interface IWorkbench {

	/**
	 * Indicates that the owner of the passed object is the calling method ("client").
	 *
	 * @see <a href="http://www.ibm.com/developerworks/java/library/j-jtp06243.html">
	 *		Java theory and practice: Whose object is it, anyway?</a>
	 */
	public static final int OWNERSHIP_CALLER = 0;

	/**
	 * Indicates that the owner of the passed object is the called method ("supplier").
	 */
	public static final int OWNERSHIP_CALLEE = 1;

	/**
	 * Lets the modules query the core for the image that is currently displayed
	 * in the main window. Returns pointer to the object owned by the core
	 * (so if a module needs to modify the image, it must make a defensive copy).
	 *
	 * @return the currently displayed image, or <code>null</code>
	 *		if no image is currently displayed (the user has not yet loaded an image or
	 *		loaded and closed it).
	 */
	public RasterImage getActiveImage();

	/**
	 * Lets the modules change the image that is currently displayed in the main window.
	 *
	 * @param image the currently displayed image (non-null object).
	 * @param ownership Indicates the ownership of the passed object. If <code>owbership</code>
	 *		is equal to OWNERSHIP_CALLER, then the core will make a defensive copy of the image object.
	 *		If it is equal to OWNERSHIP_CALLEE, the core will keep a reference to
	 *		the passed object.
	 *		If the ownership is transferred to the core, then the module cannot
	 *		change the image object any more.
	 * @throws SizeConstraintViolationException if the image size violates the contraints
	 *		defined by the {@link Constants#MAX_IMAGE_HEIGHT}, {@link Constants#MAX_IMAGE_WIDTH},
	 *		{@link Constants#MIN_IMAGE_HEIGHT} and {@link Constants#MIN_IMAGE_WIDTH}.
	 */
	public void setActiveImage(RasterImage image, int ownership) throws SizeConstraintViolationException;

	/**
	 * Returns <code>true</code> if some image is currently displayed in the main window.
	 *
	 * @return <code>true</code> if <code>getActiveImage() != null</code>
	 */
	public boolean imageIsLoaded();

	/**
	 * Returns the rectangular selection in the main window.
	 * Returns a defensive copy of the object.
	 *
	 * @return the selected area, or <code>null</code> if no rectangular area is selected (ownership: caller)
	 */
	public Rectangle getRectangularSelection();

	/**
	 * Returns the polygon selection in the main window.
	 * Returns a defensive copy of the object.
	 *
	 * @return the selected area (non-null pointer; ownership: caller).
	 *		If no polygon area is selected,
	 *		this method returns an empty polygon.
	 */
	public Polygon getPolygonSelection();

	/**
	 * Lets the modules register a listener that will be notified when the image
	 * in the main window changes (if the user presses undo/redo, modifies or closes
	 * the image or opens another image).
	 */
	public void registerImageChangeListener(ImageChangeListener listener);
}
