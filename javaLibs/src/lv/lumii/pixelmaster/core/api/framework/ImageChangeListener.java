
package lv.lumii.pixelmaster.core.api.framework;

import java.util.EventListener;

/**
 * This interface represents a listener for image change events.
 * It allows modules to be notified when the image in the main window changes
 * (if the user presses undo/redo, modifies or closes the image or opens another image).
 *
 * @author Jevgeny Jonas
 */
public interface ImageChangeListener extends EventListener {

	/**
	 * The method that will be called when the image has changed.
	 */
	public void imageChanged();
}
