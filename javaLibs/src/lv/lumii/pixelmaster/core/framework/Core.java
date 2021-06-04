
package lv.lumii.pixelmaster.core.framework;

import java.awt.image.BufferedImage;

import javax.swing.*;

import lv.lumii.pixelmaster.core.api.framework.*;

import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;

import lv.lumii.pixelmaster.core.api.gui.ImageIoDialog;

/**
 * This class exists in order to allow the core to add its functionality to the
 * main window. For this purpose it implements the IModule interface; however,
 * the core is <b>not</b> a module.
 *
 * The core does not consist of this class alone; the core is everything in the package
 * lv.lumii.pixelmaster.core.
 *
 * This class uses non-public interface (some package-private methods and impelementation details)
 * of the core.
 *
 * @author 
 */
final class Core implements IModule {

	// actions
	private OpenAction openAction = new OpenAction();
	private SaveAsAction saveAsAction = new SaveAsAction();
	private CloseAction closeAction = new CloseAction();
	private StartArbitrarySelection startArbitrarySelection = new StartArbitrarySelection();
	private ReleaseArbitrarySelection releaseArbitrarySelection = new ReleaseArbitrarySelection();
	private AddVertex addVertex = new AddVertex();
	private RemoveVertex removeVertex = new RemoveVertex();
	private UndoAllAction undoAllAction = new UndoAllAction();
	private UndoAction undoAction = new UndoAction();
	private RedoAction redoAction = new RedoAction();
	private RedoAllAction redoAllAction = new RedoAllAction();

	// workbench
	private Workbench workbench = null;


	Core() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		this.workbench = (Workbench) workbench;

		registry.registerMenuItem(openAction);
		registry.registerMenuItem(saveAsAction);
		registry.registerMenuItem(closeAction);
		registry.registerMenuItem(startArbitrarySelection);
		registry.registerMenuItem(releaseArbitrarySelection);
		registry.registerMenuItem(addVertex);
		registry.registerMenuItem(removeVertex);
		registry.registerMenuItem(undoAllAction);
		registry.registerMenuItem(undoAction);
		registry.registerMenuItem(redoAction);
		registry.registerMenuItem(redoAllAction);

		registry.registerToolbarButton(openAction);
		registry.registerToolbarButton(saveAsAction);
		registry.registerToolbarButton(closeAction);
		registry.registerToolbarButton(undoAllAction);
		registry.registerToolbarButton(undoAction);
		registry.registerToolbarButton(redoAction);
		registry.registerToolbarButton(redoAllAction);
	}


	private final class OpenAction implements IToolbarButtonAction, IMenuItemAction {

		private final Icon smallIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Open_16.png"));
		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Open_32.png"));
		private final String menuTitle = "File";
		private final String actionName = "Open";

		private OpenAction() { }

		@Override
		public void actionPerformed() {

			BufferedImage tmpBufImage=ImageIoDialog.loadImage(null);

			// the user pressed "Cancel"
			if (tmpBufImage==null) return;

			RasterImage tmp=new RasterImage(tmpBufImage.getWidth(), tmpBufImage.getHeight());
			ImageConverter.convertBufImage(tmpBufImage, tmp);

			try {
				workbench.openImage(tmp);
			}
			catch (SizeConstraintViolationException exc) {
				JOptionPane.showMessageDialog(null, exc.getMessage());
			}
		}

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
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

	private final class SaveAsAction implements IToolbarButtonAction, IMenuItemAction {

		private final Icon smallIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Save As_16.png"));
		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Save As_32.png"));
		private final String menuTitle = "File";
		private final String actionName = "Save as";

		private SaveAsAction() { }

		@Override
		public void actionPerformed() {
			ImageIoDialog.saveImage(null, workbench.getActiveImage());
		}

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
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

	private final class CloseAction implements IToolbarButtonAction, IMenuItemAction {

		private final Icon smallIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/fileclose_16.png"));
		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/fileclose_32.png"));
		private final String menuTitle = "File";
		private final String actionName = "Close";

		private CloseAction() { }

		@Override
		public void actionPerformed() {

			Object[] options = {"Yes", "No"};

			int n = JOptionPane.showOptionDialog(null,
				"Are you sure you want to close the image?",
				"Confirmation",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,						//don't use a custom Icon
				options,					//the titles of buttons
				options[0]);				//the title of the default button

			if (n == 0) {
				workbench.closeImage();
			}
		}

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
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

	private final class StartArbitrarySelection implements IMenuItemAction {

		private final Icon smallIcon = null;
		private final String menuTitle = "Arbitrary selection";
		private final String actionName = "Start selection";

		private StartArbitrarySelection() { }

		@Override
		public void actionPerformed() {
			workbench.mainWindow.iv.startSelection();
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

	private final class ReleaseArbitrarySelection implements IMenuItemAction {

		private final Icon smallIcon = null;
		private final String menuTitle = "Arbitrary selection";
		private final String actionName = "Release selection";

		private ReleaseArbitrarySelection() { }

		@Override
		public void actionPerformed() {
			workbench.mainWindow.iv.releaseSelection();
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

	private final class AddVertex implements IMenuItemAction {

		private final Icon smallIcon = null;
		private final String menuTitle = "Arbitrary selection";
		private final String actionName = "Add vertex";

		private AddVertex() { }

		@Override
		public void actionPerformed() {
			workbench.mainWindow.iv.addPoint();
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

	private final class RemoveVertex implements IMenuItemAction {

		private final Icon smallIcon = null;
		private final String menuTitle = "Arbitrary selection";
		private final String actionName = "Remove vertex";

		private RemoveVertex() { }

		@Override
		public void actionPerformed() {
			workbench.mainWindow.iv.removePoint();
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

	private final class UndoAction implements IToolbarButtonAction, IMenuItemAction {

		private final Icon smallIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Undo_16.png"));
		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Undo_32.png"));
		private final String menuTitle = "Edit";
		private final String actionName = "Undo";

		private UndoAction() { }

		@Override
		public void actionPerformed() {
			workbench.undo();
		}

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
		}

		@Override
		public boolean isEnabled() {
			return workbench.mainWindowModel.getUndoRedoManager().canUndo();
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

	private final class RedoAction implements IToolbarButtonAction, IMenuItemAction {

		private final Icon smallIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Redo_16.png"));
		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Redo_32.png"));
		private final String menuTitle = "Edit";
		private final String actionName = "Redo";

		private RedoAction() { }

		@Override
		public void actionPerformed() {
			workbench.redo();
		}

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
		}

		@Override
		public boolean isEnabled() {
			return workbench.mainWindowModel.getUndoRedoManager().canRedo();
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

	private final class UndoAllAction implements IToolbarButtonAction, IMenuItemAction {

		private final Icon smallIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Undo All_16.png"));
		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Undo All_32.png"));
		private final String menuTitle = "Edit";
		private final String actionName = "Undo all";

		private UndoAllAction() { }

		@Override
		public void actionPerformed() {
			workbench.undoAll();
		}

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
		}

		@Override
		public boolean isEnabled() {
			return workbench.mainWindowModel.getUndoRedoManager().canUndo();
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

	private final class RedoAllAction implements IToolbarButtonAction, IMenuItemAction {

		private final Icon smallIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Redo All_16.png"));
		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/core/gui/Redo All_32.png"));
		private final String menuTitle = "Edit";
		private final String actionName = "Redo all";

		private RedoAllAction() { }

		@Override
		public void actionPerformed() {
			workbench.redoAll();
		}

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
		}

		@Override
		public boolean isEnabled() {
			return workbench.mainWindowModel.getUndoRedoManager().canRedo();
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
