
package lv.lumii.pixelmaster.core.gui;

import java.util.Stack;

import lv.lumii.pixelmaster.core.api.domain.Constants;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.gui.ImageViewerModel;

/**
 *
 * @author 
 */
public final class MainFrameModel {

	private UndoRedoManager undoRedoManager = new UndoRedoManager();
	private ImageViewerModel imageViewerModel = new ImageViewerModel();

	public MainFrameModel() { }

	public boolean imageIsLoaded() { return undoRedoManager.getActiveImage() != null; }

	public UndoRedoManager getUndoRedoManager() {
		return undoRedoManager;
	}

	public ImageViewerModel getImageViewerModel() {
		return imageViewerModel;
	}


	/**
	 * Manages the undo/redo stack.
	 */
	public final class UndoRedoManager {

		private RasterImage activeImage = null;
		private Stack <RasterImage> undoStack = new Stack <RasterImage>(),
				redoStack = new Stack <RasterImage>();

		void clear() {
			undoStack.clear();
			redoStack.clear();
			activeImage = null;
		}

		public boolean canUndo() {
			return !undoStack.empty();
		}

		public boolean canRedo() {
			return !redoStack.empty();
		}

		void redo() {
			assert canRedo();
			undoStack.push(activeImage);
			activeImage = redoStack.pop();
		}

		void undo() {
			assert canUndo();
			redoStack.push(activeImage);
			activeImage = undoStack.pop();
		}

		void setActiveImage(RasterImage image) {

			assert image != null;
			assert image.getWidth() >= Constants.MIN_IMAGE_WIDTH && image.getHeight() >= Constants.MIN_IMAGE_HEIGHT;
			assert image.getWidth() <= Constants.MAX_IMAGE_WIDTH && image.getHeight() <= Constants.MAX_IMAGE_HEIGHT;

			redoStack.clear();

			if (activeImage != null) {
				undoStack.push(activeImage);
			}

			this.activeImage = image;
		}

		public RasterImage getActiveImage() {
			return activeImage;
		}

		void undoAll() {
			assert canUndo();
			redoStack.push(activeImage);
			while (undoStack.size() > 1) redoStack.push(undoStack.pop());
			activeImage = undoStack.pop();
		}

		void redoAll() {
			assert canRedo();
			undoStack.push(activeImage);
			while (redoStack.size() > 1) undoStack.push(redoStack.pop());
			activeImage = redoStack.pop();
		}
	}
}
