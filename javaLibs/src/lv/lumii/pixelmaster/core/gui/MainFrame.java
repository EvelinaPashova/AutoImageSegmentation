
package lv.lumii.pixelmaster.core.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import lv.lumii.pixelmaster.core.api.gui.*;
import lv.lumii.pixelmaster.core.api.domain.*;

/**
 * Represents main window
 * @author 
 */
public final class MainFrame extends JFrame {

	/**
	 * The name of the application, which is displayed as a title when
	 * it is run.
	 */
	public static final String APP_NAME = "PixelMaster";

	private JPanel mainPanel;
	private JPanel toolbarPanel;

	public ImageViewer iv;

	private MainFrameModel mainWindowModel;

	/** Window constructor */
	public MainFrame(MainFrameModel mainWindowModel) {
		this.mainWindowModel = mainWindowModel;

		this.setTitle(APP_NAME);
//		this.setBackground(Color.darkGray);
		this.setSize(700, 600);
		this.setLocation(200, 200);
		this.setMinimumSize(new Dimension(300, 200));

		mainPanel=new JPanel(new BorderLayout());
//		mainPanel.setBackground(Color.darkGray);
		add(mainPanel);

		toolbarPanel=new JPanel(new BorderLayout());
		mainPanel.add(toolbarPanel, BorderLayout.NORTH);

		iv=new ImageViewer(mainWindowModel.getUndoRedoManager().getActiveImage(), mainWindowModel.getImageViewerModel());
		mainPanel.add(iv, BorderLayout.CENTER);
        mainPanel.setComponentZOrder(iv, 0);

        StatusBar statusBar = new StatusBar(iv.getCanvas());
		getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
        statusBar.setStatus("Welcome to " + APP_NAME + "!");

		/* listen to window actions with anonymous inner class */
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { System.exit(0); }
		});

		setVisible(false);
	}

	public void undo() {
		assert mainWindowModel.getUndoRedoManager().canUndo();

		mainWindowModel.getUndoRedoManager().undo();
		iv.setImage(mainWindowModel.getUndoRedoManager().getActiveImage());
	}

	public void redo() {
		assert mainWindowModel.getUndoRedoManager().canRedo();

		mainWindowModel.getUndoRedoManager().redo();
		iv.setImage(mainWindowModel.getUndoRedoManager().getActiveImage());
	}

	public void undoAll() {
		assert mainWindowModel.getUndoRedoManager().canUndo();

		mainWindowModel.getUndoRedoManager().undoAll();
		iv.setImage(mainWindowModel.getUndoRedoManager().getActiveImage());
	}

	public void redoAll() {
		assert mainWindowModel.getUndoRedoManager().canRedo();

		mainWindowModel.getUndoRedoManager().redoAll();
		iv.setImage(mainWindowModel.getUndoRedoManager().getActiveImage());
	}

	public void openImage(RasterImage image) {
		assert image.getWidth() >= Constants.MIN_IMAGE_WIDTH && image.getHeight() >= Constants.MIN_IMAGE_HEIGHT;
		assert image.getWidth() <= Constants.MAX_IMAGE_WIDTH && image.getHeight() <= Constants.MAX_IMAGE_HEIGHT;

		mainWindowModel.getUndoRedoManager().clear();
		mainWindowModel.getUndoRedoManager().setActiveImage(image);
		iv.setImage(mainWindowModel.getUndoRedoManager().getActiveImage());
	}

	public void closeImage() {
		assert mainWindowModel.getUndoRedoManager().getActiveImage() != null;

		mainWindowModel.getUndoRedoManager().clear();
		iv.setImage(mainWindowModel.getUndoRedoManager().getActiveImage());
	}

	/**
	 *
	 * @param image the currently displayed image.
	 */
	public void setActiveImage(RasterImage image) {

		assert image != null;
		assert image.getWidth() >= Constants.MIN_IMAGE_WIDTH && image.getHeight() >= Constants.MIN_IMAGE_HEIGHT;
		assert image.getWidth() <= Constants.MAX_IMAGE_WIDTH && image.getHeight() <= Constants.MAX_IMAGE_HEIGHT;

		mainWindowModel.getUndoRedoManager().setActiveImage(image);
		iv.setImage(mainWindowModel.getUndoRedoManager().getActiveImage());
	}

	public void setToolbar(JToolBar toolBar) {
		toolbarPanel.add(toolBar, BorderLayout.WEST);
	}
}
