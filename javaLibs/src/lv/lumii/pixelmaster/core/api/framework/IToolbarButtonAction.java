
package lv.lumii.pixelmaster.core.api.framework;

import javax.swing.Icon;

/**
 * <p>This interface represents a callback action which will be called when
 * the user presses a toolbar button.</p>
 *
 * <p>Each action is assigned to a module's control element and is fired when the user
 * activates this control element. Actions are extension points which allow
 * PixelMaster's modules to add functionality by providing callbacks.
 * Each module should decide what to do when a particular action is
 * performed.</p>
 *
 * <p>A large icon must be provided: it will be visible in the toolbar of the
 * main window.</p>
 *
 * @author Jevgeny Jonas
 */
public interface IToolbarButtonAction extends IAction {

	/**
	 * Returns the icon of the corresponding button in the toolbar.
	 *
	 * @return Non-null pointer.
	 */
	public Icon getLargeIcon();
}

interface IAction {

	/**
	 * Called when user activates the control element.
	 */
	public void actionPerformed();

	/**
	 * <p>Determines whether performing this action is possible in the current
	 * context. Each module should decide when to enable/disable control
	 * elements (e.g., based on whether an image is loaded in the main window)
	 * and should implement this method accordingly.</p>
	 *
	 * <p>The core provides guarantee that the enabled/disabled status of every
	 * control element will be updated:
	 * 1) when the control element is first created;
	 * 2) when the image in the main window changes (if the user presses
	 * undo/redo, modifies or closes the image or opens another image).</p>
	 *
	 * <p>This method's return value may change over time. For example,
	 * the module's control element may be enabled or disabled with respect to
	 * whether an image is loaded in the main window. When the image changes,
	 * the core will call this method and update the enabled/disabled status
	 * according to the value returned in that particular call.</p>
	 *
	 * <p>The modules do not have to update the enabled/disabled status themselves.</p>
	 *
	 * @return true if the control element is enabled
	 */
	public boolean isEnabled();
}
