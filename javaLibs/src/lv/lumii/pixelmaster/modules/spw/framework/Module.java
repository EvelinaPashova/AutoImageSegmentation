
package lv.lumii.pixelmaster.modules.spw.framework;

import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IModule;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import lv.lumii.pixelmaster.modules.spw.gui.*;

/**
 * 
 */
public class Module implements IModule {

	// actions
	private OpenSpwWindowAction openSpwWindowAction = new OpenSpwWindowAction();

	// workbench
	private IWorkbench workbench;

	// gui
	private SPWFrame spwFrame = null;
	private SPWFrameModel spwFrameModel = null;


	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		this.workbench = workbench;
		registry.registerMenuItem(openSpwWindowAction);
	}


	private final class OpenSpwWindowAction implements IMenuItemAction {

		private final String menuTitle = "Tools";
		private final String actionName = "Spherical wave";
		private final Icon smallIcon = null;

		private OpenSpwWindowAction() { }

		@Override
		public void actionPerformed() {
			assert workbench.getActiveImage().isBinary();

			/*
			 * Keeping track of the window instances.
			 * Only one window may be opened at a time.
			 */
			if (spwFrame == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }
					public void windowClosing(WindowEvent e) { spwFrame = null; }
					public void windowClosed(WindowEvent e) { }
					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

				spwFrameModel = new SPWFrameModel(workbench.getActiveImage());
				spwFrame = new SPWFrame(spwFrameModel);

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				spwFrame.addWindowListener(new WindowListenerImpl());
			}
			else {
				spwFrame.toFront();
			}
		}

		@Override
		public boolean isEnabled() {
			return workbench.imageIsLoaded() && workbench.getActiveImage().isBinary();
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
