
package lv.lumii.pixelmaster.core.framework;

import java.awt.Component;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import lv.lumii.pixelmaster.core.api.domain.Constants;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;

import lv.lumii.pixelmaster.core.api.framework.IWorkbench;

import lv.lumii.pixelmaster.core.gui.MainFrame;
import lv.lumii.pixelmaster.core.gui.MainFrameModel;
import lv.lumii.pixelmaster.core.api.framework.ImageChangeListener;

/**
 *
 * @author 
 */
final class Workbench implements IWorkbench {

	MainFrame mainWindow;
	MainFrameModel mainWindowModel;

	/**
	 * references to these components are stored in order to enable/disable the
	 * menu items and toolbar buttons when the image that is displayed in the main window changes
	 */
	private JMenuBar menuBar;
	private JToolBar toolBar;

	private Set<ImageChangeListener> listeners = new HashSet<ImageChangeListener>();

	Workbench() {
		this.mainWindowModel = new MainFrameModel();
	}

	void initControlElements(JMenuBar menuBar, JToolBar toolBar) {
		this.menuBar = menuBar;
		this.toolBar = toolBar;
		mainWindow.setJMenuBar(menuBar);
		mainWindow.setToolbar(toolBar);
		mainWindow.setVisible(true);
	}

	void loadMainGUI() {
		mainWindow = new MainFrame(mainWindowModel);
	}

	void undo() {
		mainWindow.undo();
		notifyAndUpdate();
	}

	void redo() {
		mainWindow.redo();
		notifyAndUpdate();
	}

	void undoAll() {
		mainWindow.undoAll();
		notifyAndUpdate();
	}

	void redoAll() {
		mainWindow.redoAll();
		notifyAndUpdate();
	}

	void openImage(RasterImage image) throws SizeConstraintViolationException {
		assert image != null;

		if (image.getWidth() < Constants.MIN_IMAGE_WIDTH || image.getHeight() < Constants.MIN_IMAGE_HEIGHT) {
			throw new SizeConstraintViolationException("Image too small");
		}

		if (image.getWidth() > Constants.MAX_IMAGE_WIDTH || image.getHeight() > Constants.MAX_IMAGE_HEIGHT) {
			throw new SizeConstraintViolationException("Image too large");
		}

		mainWindow.openImage(image);
		notifyAndUpdate();
	}

	void closeImage() {
		mainWindow.closeImage();
		notifyAndUpdate();
	}

	/**
	 * Notifies all listeners that the image changed and
	 * updates the enabled/disabled status of every
	 * control element in the main window (the core's and the modules' control elements).
	 */
	private void notifyAndUpdate() {

		// notify the listeners
		for (ImageChangeListener listener: listeners) {
			listener.imageChanged();
		}

		// update the enabled/disabled status of all menu items
		for (int i = 0; i < menuBar.getMenuCount(); ++i) {
			JMenu menu = menuBar.getMenu(i);
			for (int j = 0; j < menu.getItemCount(); ++j) {
				JMenuItem item = menu.getItem(j);
				Action action = item.getAction();
				action.setEnabled(action.isEnabled());
			}
		}

//		System.out.println("Components in the toolbar: " + toolBar.getComponents().length);

		// update the enabled/disabled status of all toolbar buttons
		for (Component c: toolBar.getComponents()) {
			if (c instanceof JButton) {
				JButton button = (JButton) c;
				Action action = button.getAction();
				action.setEnabled(action.isEnabled());
			}
		}
	}

	@Override
	public RasterImage getActiveImage() {
		return mainWindowModel.getUndoRedoManager().getActiveImage();
	}

	@Override
	public void setActiveImage(RasterImage image, int ownership) throws SizeConstraintViolationException {

		assert image != null;
		assert ownership == OWNERSHIP_CALLEE || ownership == OWNERSHIP_CALLER;

		if (ownership == OWNERSHIP_CALLER) {
			image = (RasterImage) image.clone();
		}

		if (image.getWidth() < Constants.MIN_IMAGE_WIDTH || image.getHeight() < Constants.MIN_IMAGE_HEIGHT) {
			throw new SizeConstraintViolationException("Image too small");
		}

		if (image.getWidth() > Constants.MAX_IMAGE_WIDTH || image.getHeight() > Constants.MAX_IMAGE_HEIGHT) {
			throw new SizeConstraintViolationException("Image too large");
		}

		mainWindow.setActiveImage(image);

		notifyAndUpdate();
	}

	@Override
	public boolean imageIsLoaded() {
		return mainWindowModel.imageIsLoaded();
	}

	@Override
	public Rectangle getRectangularSelection() {
		Rectangle currentSelection = mainWindowModel.getImageViewerModel().getRectangularSelection();
		if (currentSelection == null) return null;
		return currentSelection;
	}

	@Override
	public Polygon getPolygonSelection() {
		return mainWindowModel.getImageViewerModel().getPolygonSelection();
	}

	@Override
	public void registerImageChangeListener(ImageChangeListener listener) {
		assert listener != null;
		listeners.add(listener);
	}
}
