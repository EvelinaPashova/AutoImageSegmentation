
package lv.lumii.pixelmaster.modules.binarization.framework;

import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IModule;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;

import javax.swing.Icon;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;

import lv.lumii.pixelmaster.modules.binarization.domain.*;
import lv.lumii.pixelmaster.modules.binarization.gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

/**
 * 
 */
public class Module implements IModule {

	// actions
	private OpenBinarizationWindowAction openBinarizationWindowAction = new OpenBinarizationWindowAction();
	private ConvertToGrayscale convertToGrayscale = new ConvertToGrayscale();

	// workbench
	private IWorkbench workbench;

	// gui
	private BinarizationFrameModel binarizationFrameModel;
	private BinarizationFrame binarizationFrame = null;


	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		this.workbench = workbench;
		registry.registerMenuItem(openBinarizationWindowAction);
		registry.registerMenuItem(convertToGrayscale);
	}


	private final class OpenBinarizationWindowAction implements IMenuItemAction {

		private final String menuTitle = "Tools";
		private final String actionName = "Binarization";
		private final Icon smallIcon = null;

		private OpenBinarizationWindowAction() { }

		private final class CopyImageToMainWindow extends AbstractAction {
			private CopyImageToMainWindow() { }
			public void actionPerformed(ActionEvent e) {
				try {
					workbench.setActiveImage(binarizationFrameModel.getImage(), IWorkbench.OWNERSHIP_CALLER);
				}
				catch (SizeConstraintViolationException exc) {
					assert false;
				}
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Copy to the main window";
				return super.getValue(key);
			}
		}

		@Override
		public void actionPerformed() {

			/*
			 * Keeping track of the window instances.
			 * Only one window may be opened at a time.
			 */
			if (binarizationFrame == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }
					public void windowClosing(WindowEvent e) { binarizationFrame = null; }
					public void windowClosed(WindowEvent e) { }
					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

				binarizationFrameModel = new BinarizationFrameModel(workbench.getActiveImage());
				binarizationFrame = new BinarizationFrame(binarizationFrameModel, new CopyImageToMainWindow());

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				binarizationFrame.addWindowListener(new WindowListenerImpl());
			}
			else {
				binarizationFrame.toFront();
			}
		}

		@Override
		public boolean isEnabled() {
			return workbench.imageIsLoaded();
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

	private final class ConvertToGrayscale implements IMenuItemAction {

		private final String menuTitle = "Edit";
		private final String actionName = "Convert to grayscale";
		private final Icon smallIcon = null;

		private ConvertToGrayscale() { }

		@Override
		public Icon getSmallIcon() {
			return smallIcon;
		}

		@Override
		public boolean isEnabled() {
			return workbench.imageIsLoaded();
		}

		@Override
		public String getName() {
			return actionName;
		}

		@Override
		public String getMenuTitle() {
			return menuTitle;
		}

		@Override
		public void actionPerformed() {
			RasterImage tmp = new RasterImage(workbench.getActiveImage().getWidth(), workbench.getActiveImage().getHeight());
			GrayScale.greyImage(workbench.getActiveImage(), tmp);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}
	}
}
