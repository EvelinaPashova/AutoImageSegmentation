
package lv.lumii.pixelmaster.modules.grapheditor.framework;

import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IModule;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import lv.lumii.pixelmaster.modules.grapheditor.gui.GraphEditorDialog;
import lv.lumii.pixelmaster.modules.spw.gui.SPWFrame;

/**
 * 
 */
public class Module implements IModule {

	// actions
	private OpenGraphEditor openGraphEditor = new OpenGraphEditor();

	// gui
	private GraphEditorDialog graphEditorDialog = null;


	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		registry.registerMenuItem(openGraphEditor);
	}


	private final class OpenGraphEditor implements IMenuItemAction {

		private final String menuTitle = "Tools";
		private final String actionName = "Graph Editor";
		private final Icon smallIcon = null;

		private OpenGraphEditor() { }

		@Override
		public void actionPerformed() {

			/*
			 * Keeping track of the window instances.
			 * Only one window may be opened at a time.
			 */
			if (graphEditorDialog == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }
					public void windowClosing(WindowEvent e) { graphEditorDialog = null; }
					public void windowClosed(WindowEvent e) { }
					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

				graphEditorDialog = new GraphEditorDialog();

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				graphEditorDialog.addWindowListener(new WindowListenerImpl());
			}
			else {
				graphEditorDialog.toFront();
			}
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public Icon getSmallIcon() {
			return smallIcon;
		}

		@Override
		public String getName() {
			return actionName;
		}

		@Override
		public String getMenuTitle() {
			return menuTitle;
		}
	}
}
