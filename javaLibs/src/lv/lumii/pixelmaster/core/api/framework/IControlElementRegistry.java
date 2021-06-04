
package lv.lumii.pixelmaster.core.api.framework;

/**
 * This interface lets the modules add their control elements to the main window.
 * The control elements include toolbar buttons and menu items.
 * An implementation of this interface will be provided by the core at the
 * {@link IModule#init initialization stage}.
 *
 * @author Jevgeny Jonas
 */
public interface IControlElementRegistry {

	/**
	 * Lets a module register its menu item and the corresponding callback.
	 * The core of PixelMaster will add this item to the menu of the main window.
	 */
	public void registerMenuItem(IMenuItemAction callback);

	/**
	 * Lets a module register its button and the corresponding callback.
	 * The core of PixelMaster will add this button to the toolbar of the main window.
	 */
	public void registerToolbarButton(IToolbarButtonAction callback);
}
