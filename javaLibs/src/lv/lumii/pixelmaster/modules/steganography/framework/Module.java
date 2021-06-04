
package lv.lumii.pixelmaster.modules.steganography.framework;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import lv.lumii.pixelmaster.modules.filters.gui.FilterFrame;
import lv.lumii.pixelmaster.modules.filters.gui.FilterFrameModel;

import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IToolbarButtonAction;
import lv.lumii.pixelmaster.core.api.framework.IModule;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;
import lv.lumii.pixelmaster.modules.filters.domain.arbitraryarea.ArbitraryAreaManipulation;
import lv.lumii.pixelmaster.modules.steganography.gui.EmbedmentDialog;
import lv.lumii.pixelmaster.modules.steganography.gui.ExtractionDialog;

/**
 * 
 */
public class Module implements IModule {

	// actions
	private OpenExtractionDialog openExtractionDialog = new OpenExtractionDialog();
	private OpenEmbedmentDialog openEmbedmentDialog = new OpenEmbedmentDialog();

	// workbench
	private IWorkbench workbench;

	// gui
	private ExtractionDialog extractionDialog = null;
	private EmbedmentDialog embedmentDialog = null;


	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		this.workbench = workbench;

		registry.registerMenuItem(openEmbedmentDialog);
		registry.registerMenuItem(openExtractionDialog);
	}


	private final class OpenEmbedmentDialog implements IMenuItemAction {

		private final String menuTitle = "Steganography";
		private final String actionName = "Embed Message";
		private final Icon smallIcon = null;

		private OpenEmbedmentDialog() { }

		private final class Apply extends AbstractAction {
			private Apply() { }
			public void actionPerformed(ActionEvent e) {
				try {
					workbench.setActiveImage(embedmentDialog.getImage(), IWorkbench.OWNERSHIP_CALLER);
				}
				catch (SizeConstraintViolationException exc) {
					assert false;
				}
			}
		}

		@Override
		public void actionPerformed() {

			if (embedmentDialog == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }
					public void windowClosing(WindowEvent e) { embedmentDialog = null; }
					public void windowClosed(WindowEvent e) { }
					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

				embedmentDialog = new EmbedmentDialog(null, workbench.getActiveImage(), new Apply());

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				embedmentDialog.addWindowListener(new WindowListenerImpl());
			}
			else {
				embedmentDialog.toFront();
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

	private final class OpenExtractionDialog implements IMenuItemAction {

		private final String menuTitle = "Steganography";
		private final String actionName = "Extract Message";
		private final Icon smallIcon = null;

		private OpenExtractionDialog() { }

		@Override
		public void actionPerformed() {

			/*
			 * Dialog is modal - the menu item "Extract Message" cannot be
			 * selected while it is opened; so there won't appear two dialogs
			 * at a time.
			 */
			new ExtractionDialog(null, workbench.getActiveImage());
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
}
