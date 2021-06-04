
package lv.lumii.pixelmaster.core.api.framework;

import javax.swing.Icon;

/**
 * <p>This interface represents a callback action which will be called when
 * the user selects an item in the menu of the main window.</p>
 *
 * <p>Each action is assigned to a module's control element and is fired when the user
 * activates this control element. Actions are extension points which allow
 * PixelMaster's modules to add functionality by providing callbacks.
 * Each module should decide what to do when a particular action is
 * performed.</p>
 *
 * <p>A small icon can be provided, but is not required.</p>
 *
 * @author Jevgeny Jonas
 */
public interface IMenuItemAction extends IAction {

	/**
	 * Returns the icon which will be visible in the corresponding menu item.
	 * If not provided, the menu item will only have a title and no icon.
	 *
	 * @return <code>null</code> if icon in the menu element is not needed.
	 */
	public Icon getSmallIcon();

	/**
	 * Returns the name of this action which will be displayed as the
	 * menu item title.
	 * E.g., if you want this action to be called by selecting the menu item
	 * "Edit > My Action", then this method should return "My Action".
	 *
	 * @return Non-null pointer.
	 */
	public String getName();

	/**
	 * Returns the title of the top-level menu.
	 * The menu item used to invoke this action will appear under this menu.
	 * E.g., if you want this action to be called by selecting the menu item
	 * "Edit > My Action", then this method should return "Edit".
	 *
	 * @return Non-null pointer.
	 */
	public String getMenuTitle();
}
