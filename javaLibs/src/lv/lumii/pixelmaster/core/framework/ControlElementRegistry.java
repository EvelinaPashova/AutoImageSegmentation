
package lv.lumii.pixelmaster.core.framework;

import java.awt.Dimension;
import java.util.*;

import javax.swing.*;

import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IToolbarButtonAction;

/**
 * This class is responsible for registering modules' and the core's control
 * elements and callbacks, creating the main window's menu bar and the tool bar.
 */
final class ControlElementRegistry implements IControlElementRegistry {

	private static class MenuStructure implements Iterable<List<IMenuItemAction>> {

		private List<List<IMenuItemAction>> menus = new ArrayList<List<IMenuItemAction>>();

		private MenuStructure() { }

		private void addMenuItem(IMenuItemAction action) {
			Iterator<List<IMenuItemAction>> it = menus.iterator();

			List<IMenuItemAction> menu = null;
			boolean createNewMenu = true;

			// find where to insert the menu item - there must be no duplicate menu titles
			while (it.hasNext()) {
				menu = it.next();
				String menuTitle = menu.get(0).getMenuTitle();
				if (menuTitle.equals(action.getMenuTitle())) {
					createNewMenu = false;
					break;
				}
			}

			// there is no menu with such title yet - create a new menu
			if (createNewMenu == true) {
				menu = new ArrayList<IMenuItemAction>();
				menu.add(action);
				menus.add(menu);
			}

			// found a menu with this title
			else {
				menu.add(action);
			}
		}

		@Override
		public Iterator<List<IMenuItemAction>> iterator() {
			return menus.iterator();
		}
	}

	/**
	 * Menu items of the core and all modules.
	 */
	private MenuStructure menuStructure = new MenuStructure();

	/**
	 * Toolbar buttons of the core and all modules.
	 */
	private List<IToolbarButtonAction> toolbarButtons = new ArrayList<IToolbarButtonAction>();

	@Override
	public void registerMenuItem(IMenuItemAction callback) {
		menuStructure.addMenuItem(callback);
	}

	@Override
	public void registerToolbarButton(IToolbarButtonAction callback) {
		toolbarButtons.add(callback);
	}

	JToolBar getToolBar() {

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);

		for (IToolbarButtonAction action: toolbarButtons) {
//			JButton button = new JButton(new ToolbarButtonActionWrapper(action));
			JButton button = toolBar.add(new ToolbarButtonActionWrapper(action));
			button.setPreferredSize(new Dimension(32, 32));
			button.setSize(new Dimension(32, 32));
		}

		return toolBar;
	}

	JMenuBar getMenuBar() {

		JMenuBar menuBar = new JMenuBar();

		for (List<IMenuItemAction> menuItemList: menuStructure) {

			assert !menuItemList.isEmpty();

			JMenu menu = new JMenu(menuItemList.get(0).getMenuTitle());

			for (IMenuItemAction action: menuItemList) {
				JMenuItem menuItem = new JMenuItem(action.getName());
				menuItem.setAction(new MenuItemActionWrapper(action));
//				int width = menuItem.getPreferredSize().width;
//				menuItem.setPreferredSize(new Dimension(width, 16));
//				menuItem.setSize(new Dimension(width, 16));
				menu.add(menuItem);
			}
			menuBar.add(menu);
		}

		return menuBar;
	}
}
