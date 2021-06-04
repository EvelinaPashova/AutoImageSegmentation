
package lv.lumii.pixelmaster.core.framework;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;

/**
 * @author 
 */
final class MenuItemActionWrapper extends AbstractAction {

	/*
	 * Action Wrappers
	 *
	 * The classes MenuItemActionWrapper and ToolbarButtonActionWrapper are used by
	 * the core to wrap the callbacks provided by the application's modules. The
	 * responsibility of these wrappers is to adapt the application-specific
	 * callback interfaces to the Swing Action interface.
	 *
	 * The core of the application needs to pass an object of type
	 * javax.swing.Action to the Swing components, but it (the core) receives
	 * objects of type IMenuItemAction and IToolbarButtonAction from the modules.
	 * The wrapper classes are used by the core to wrap an IMenuItemAction or an
	 * IToolbarButtonAction. The wrappers implement the Action interface and
	 * thus can be set as the actions associated with the main menu's items and
	 * the main toolbar's buttons. Thus, the functionality provided by the modules
	 * through their callbacks (IMenuItemAction and IToolbarButtonAction) can be
	 * accessed from the main menu and the main toolbar.
	 *
	 */

	private IMenuItemAction wrappedAction;

	MenuItemActionWrapper(IMenuItemAction action) {
		this.wrappedAction = action;
		super.setEnabled(action.isEnabled());
		super.putValue(SMALL_ICON, wrappedAction.getSmallIcon());
		super.putValue(NAME, wrappedAction.getName());
	}

	public boolean isEnabled() {
		return wrappedAction.isEnabled();
	}

	public void actionPerformed(ActionEvent e) {
		wrappedAction.actionPerformed();
	}
}
