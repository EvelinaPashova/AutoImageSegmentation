
package lv.lumii.pixelmaster.modules.transform.framework;

import java.awt.Rectangle;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import lv.lumii.pixelmaster.core.api.domain.*;
import lv.lumii.pixelmaster.core.api.framework.*;

import lv.lumii.pixelmaster.modules.transform.domain.*;
import lv.lumii.pixelmaster.modules.transform.domain.resizing.*;
import lv.lumii.pixelmaster.modules.transform.domain.rotation.RotateBy90Degrees;
import lv.lumii.pixelmaster.modules.transform.gui.*;

/**
 * 
 */
public class Module implements IModule {

	// actions
	private CropAction cropAction = new CropAction();
	private RotateCwAction rotateCwAction = new RotateCwAction();
	private RotateAcwAction rotateAcwAction = new RotateAcwAction();
	private HorizontalFlip horizontalFlip = new HorizontalFlip();
	private VerticalFlip verticalFlip = new VerticalFlip();
	private StretchHorizontally stretchHorizontally = new StretchHorizontally();
	private StretchVertically stretchVertically = new StretchVertically();
	private ZoomIn zoomIn = new ZoomIn();
	private ZoomOut zoomOut = new ZoomOut();
	private Rotate rotate = new Rotate();
	private Resize resize = new Resize();

	// workbench
	private IWorkbench workbench;

	// gui
	private RotationFrameModel rotationFrameModel;
	private RotationFrame rotationFrame = null;
	private ResizingFrameModel resizingFrameModel;
	private ResizingFrame resizingFrame = null;


	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, final IWorkbench workbench) {
		this.workbench = workbench;

		final class ImageChangeListenerImpl implements ImageChangeListener {
			public void imageChanged() {
				if (resizingFrame != null) {
					int newWidth = 0, newHeight = 0;
					if (workbench.getActiveImage() != null) {
						newWidth = workbench.getActiveImage().getWidth();
						newHeight = workbench.getActiveImage().getHeight();
					}
					resizingFrame.updateImageSize(newWidth, newHeight);
				}
			}
		}

		workbench.registerImageChangeListener(new ImageChangeListenerImpl());

		registry.registerMenuItem(cropAction);
		registry.registerMenuItem(rotateAcwAction);
		registry.registerMenuItem(rotateCwAction);
		registry.registerMenuItem(horizontalFlip);
		registry.registerMenuItem(verticalFlip);
		registry.registerMenuItem(stretchHorizontally);
		registry.registerMenuItem(stretchVertically);
		registry.registerMenuItem(rotate);
		registry.registerMenuItem(resize);

		registry.registerToolbarButton(rotateAcwAction);
		registry.registerToolbarButton(rotateCwAction);
		registry.registerToolbarButton(zoomIn);
		registry.registerToolbarButton(zoomOut);
	}


	private final class CropAction implements IMenuItemAction {

		private final String menuTitle = "Edit";
		private final String actionName = "Crop";
		private final Icon smallIcon = null;

		private CropAction() { }

		@Override
		public void actionPerformed() {

			Rectangle selection = workbench.getRectangularSelection();

			if (selection == null) {
				JOptionPane.showMessageDialog(null, "No area selected.");
				return;
			}

			RasterImage tmp = Crop.crop(workbench.getActiveImage(), selection);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class RotateAcwAction implements IToolbarButtonAction, IMenuItemAction {

		private final String menuTitle = "Edit";
		private final String actionName = "Rotate Anti-Clockwise";
		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/modules/transform/gui/rotate_acw_32.png"));
		private final Icon smallIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/modules/transform/gui/rotate_acw_16.png"));

		private RotateAcwAction() { }

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
		}

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
			RotateBy90Degrees.rotateACW(workbench.getActiveImage(), tmp);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}
	}

	private final class RotateCwAction implements IToolbarButtonAction, IMenuItemAction {

		private final String menuTitle = "Edit";
		private final String actionName = "Rotate Clockwise";
		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/modules/transform/gui/rotate_cw_32.png"));
		private final Icon smallIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/modules/transform/gui/rotate_cw_16.png"));

		private RotateCwAction() { }

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
		}

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
			RotateBy90Degrees.rotateCW(workbench.getActiveImage(), tmp);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}
	}

	private final class HorizontalFlip implements IMenuItemAction {

		private final String menuTitle = "Edit";
		private final String actionName = "Horizontal Flip";
		private final Icon smallIcon = null;

		private HorizontalFlip() { }

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
			Flip.horizontalFlip(workbench.getActiveImage(), tmp);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}
	}

	private final class VerticalFlip implements IMenuItemAction {

		private final String menuTitle = "Edit";
		private final String actionName = "Vertical Flip";
		private final Icon smallIcon = null;

		private VerticalFlip() { }

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
			Flip.verticalFlip(workbench.getActiveImage(), tmp);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}
	}

	private final class StretchHorizontally implements IMenuItemAction {

		private final String menuTitle = "Edit";
		private final String actionName = "Stretch Horizontally";
		private final Icon smallIcon = null;

		private StretchHorizontally() { }

		@Override
		public Icon getSmallIcon() {
			return smallIcon;
		}

		@Override
		public boolean isEnabled() {
			return workbench.imageIsLoaded() && workbench.getActiveImage().getWidth() * 2 <= Constants.MAX_IMAGE_WIDTH;
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
			RasterImage tmp = NearestNeighbourResample.nnResample(workbench.getActiveImage(), 2, 1);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}
	}

	private final class StretchVertically implements IMenuItemAction {

		private final String menuTitle = "Edit";
		private final String actionName = "Stretch Vertically";
		private final Icon smallIcon = null;

		private StretchVertically() { }

		@Override
		public Icon getSmallIcon() {
			return smallIcon;
		}

		@Override
		public boolean isEnabled() {
			return workbench.imageIsLoaded() && workbench.getActiveImage().getHeight() * 2 <= Constants.MAX_IMAGE_HEIGHT;
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
			RasterImage tmp = NearestNeighbourResample.nnResample(workbench.getActiveImage(), 1, 2);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}
	}

	private final class ZoomIn implements IToolbarButtonAction {

		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/modules/transform/gui/Zoom-in_32.png"));

		private ZoomIn() { }

		@Override
		public boolean isEnabled() {
			if (!workbench.imageIsLoaded()) return false;

			int width = workbench.getActiveImage().getWidth(),
				height = workbench.getActiveImage().getHeight();

			int newWidth = (int) (width * 1.5), newHeight = (int) (height * 1.5);
			assert newWidth >= width;
			assert newHeight >= height;

			return newWidth <= Constants.MAX_IMAGE_WIDTH && newHeight <= Constants.MAX_IMAGE_HEIGHT &&
				width > 1 && height > 1;
		}

		@Override
		public void actionPerformed() {
			int width = workbench.getActiveImage().getWidth(),
				height = workbench.getActiveImage().getHeight();
			int newWidth = (int) (width * 1.5), newHeight = (int) (height * 1.5);
			RasterImage tmp = Spline.resizeImage(workbench.getActiveImage(), newWidth, newHeight);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
		}
	}

	private final class ZoomOut implements IToolbarButtonAction {

		private final Icon largeIcon = new ImageIcon(getClass().getResource("/lv/lumii/pixelmaster/modules/transform/gui/Zoom-out_32.png"));

		private ZoomOut() { }

		@Override
		public boolean isEnabled() {
			if (!workbench.imageIsLoaded()) return false;

			int width = workbench.getActiveImage().getWidth(),
				height = workbench.getActiveImage().getHeight();

			int newWidth = (int) (width * 0.67), newHeight = (int) (height * 0.67);
			assert newWidth <= width;
			assert newHeight <= height;

			return newWidth > 1 && newHeight > 1;
		}

		@Override
		public void actionPerformed() {
			int width = workbench.getActiveImage().getWidth(),
				height = workbench.getActiveImage().getHeight();
			int newWidth = (int) (width * 0.67), newHeight = (int) (height * 0.67);
			RasterImage tmp = Spline.resizeImage(workbench.getActiveImage(), newWidth, newHeight);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}

		@Override
		public Icon getLargeIcon() {
			return largeIcon;
		}
	}

	private final class Rotate implements IMenuItemAction {

		private final String menuTitle = "Tools";
		private final String actionName = "Rotate";
		private final Icon smallIcon = null;

		private Rotate() { }

		private final class CopyImageToMainWindow extends AbstractAction {
			private CopyImageToMainWindow() { }
			public void actionPerformed(ActionEvent e) {
				try {
					workbench.setActiveImage(rotationFrameModel.getImage(), IWorkbench.OWNERSHIP_CALLER);
				}
				catch (SizeConstraintViolationException exc) {
					JOptionPane.showMessageDialog(null, exc.getMessage());
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
			if (rotationFrame == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }
					public void windowClosing(WindowEvent e) { rotationFrame = null; }
					public void windowClosed(WindowEvent e) { }
					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

				rotationFrameModel = new RotationFrameModel(workbench.getActiveImage());
				rotationFrame = new RotationFrame(rotationFrameModel, new CopyImageToMainWindow());

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				rotationFrame.addWindowListener(new WindowListenerImpl());
			}
			else {
				rotationFrame.toFront();
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

	private final class Resize implements IMenuItemAction {

		private final String menuTitle = "Tools";
		private final String actionName = "Resize";
		private final Icon smallIcon = null;

		private Resize() { }

		private final class ApplyNearestNeighbourResample extends AbstractAction {
			private ApplyNearestNeighbourResample() { }
			public void actionPerformed(ActionEvent e) {
				try {
					RasterImage image = resizingFrameModel.applyNearestNeighbourResample(workbench.getActiveImage());
					workbench.setActiveImage(image, IWorkbench.OWNERSHIP_CALLEE);
				}
				catch (SizeConstraintViolationException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
				catch (ResizingFrameModel.NotApplicable na) {
					JOptionPane.showMessageDialog(null, na.getMessage());
				}
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Apply";
				return super.getValue(key);
			}
		}

		private final class ApplyBilinearResample extends AbstractAction {
			private ApplyBilinearResample() { }
			public void actionPerformed(ActionEvent e) {
				try {
					RasterImage image = resizingFrameModel.applyBilinearResample(workbench.getActiveImage());
					workbench.setActiveImage(image, IWorkbench.OWNERSHIP_CALLEE);
				}
				catch (SizeConstraintViolationException ex) {
					assert false;
				}
				catch (ResizingFrameModel.NotApplicable na) {
					JOptionPane.showMessageDialog(null, na.getMessage());
				}
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Apply";
				return super.getValue(key);
			}
		}

		@Override
		public void actionPerformed() {

			/*
			 * Keeping track of the window instances.
			 * Only one window may be opened at a time.
			 */
			if (resizingFrame == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }
					public void windowClosing(WindowEvent e) { resizingFrame = null; }
					public void windowClosed(WindowEvent e) { }
					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

				resizingFrameModel = new ResizingFrameModel();
				resizingFrame = new ResizingFrame(resizingFrameModel,
						new ApplyNearestNeighbourResample(),
						new ApplyBilinearResample(),
						workbench.getActiveImage());

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				resizingFrame.addWindowListener(new WindowListenerImpl());
			}
			else {
				resizingFrame.toFront();
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
