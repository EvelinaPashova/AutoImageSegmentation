
package lv.lumii.pixelmaster.modules.filters.framework;

import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;
import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IModule;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;
import lv.lumii.pixelmaster.modules.filters.domain.ImagePixels;
import lv.lumii.pixelmaster.modules.filters.domain.Invert;
import lv.lumii.pixelmaster.modules.filters.domain.arbitraryarea.ArbitraryAreaManipulation;
import lv.lumii.pixelmaster.modules.filters.gui.FilterFrame;
import lv.lumii.pixelmaster.modules.filters.gui.FilterFrameModel;

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
	private OpenFilterWindowAction openFilterWindowAction = new OpenFilterWindowAction();
	private InvertAction invertAction = new InvertAction();
	private BlackOutAction blackOutAction = new BlackOutAction();
	private ConvertToGrayscale convertToGrayscale = new ConvertToGrayscale();
	private CustomFilter customFilter = new CustomFilter();
    private LocalHistogramEqualization histEqualization = new LocalHistogramEqualization();

	// workbench
	private IWorkbench workbench;

	// gui
	private FilterFrameModel filterFrameModel;
	private FilterFrame filterFrame = null;


	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		this.workbench = workbench;
		registry.registerMenuItem(openFilterWindowAction);
		registry.registerMenuItem(invertAction);
		registry.registerMenuItem(blackOutAction);
		registry.registerMenuItem(convertToGrayscale);
		registry.registerMenuItem(customFilter);
        registry.registerMenuItem(histEqualization);
	}


	private final class OpenFilterWindowAction implements IMenuItemAction {

		private final String menuTitle = "Tools";
		private final String actionName = "Filtering";
		private final Icon smallIcon = null;

		private OpenFilterWindowAction() { }

		private final class CopyImageToMainWindow extends AbstractAction {
			private CopyImageToMainWindow() { }
			public void actionPerformed(ActionEvent e) {
				try {
					workbench.setActiveImage(filterFrameModel.getImage(), IWorkbench.OWNERSHIP_CALLER);
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
			if (filterFrame == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }
					public void windowClosing(WindowEvent e) { filterFrame = null; }
					public void windowClosed(WindowEvent e) { }
					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

				filterFrameModel = new FilterFrameModel(workbench.getActiveImage());
				filterFrame = new FilterFrame(filterFrameModel, new CopyImageToMainWindow());

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				filterFrame.addWindowListener(new WindowListenerImpl());
			}
			else {
				filterFrame.toFront();
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

	private final class InvertAction implements IMenuItemAction {

		private final String menuTitle = "Edit";
		private final String actionName = "Invert Colors";
		private final Icon smallIcon = null;

		private InvertAction() { }

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
			Invert.invertColors(workbench.getActiveImage(), tmp);

			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}
	}

	private final class BlackOutAction implements IMenuItemAction {

		private final String menuTitle = "Arbitrary selection";
		private final String actionName = "Black out";
		private final Icon smallIcon = null;

		private BlackOutAction() { }

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
			if (workbench.getPolygonSelection().npoints == 0) {
				JOptionPane.showMessageDialog(null, "No area selected.");
				return;
			}
			RasterImage workingImage = new RasterImage(workbench.getActiveImage());
			ArbitraryAreaManipulation.manipulateAtDistinguishedPixelLevel(workingImage,
					workbench.getPolygonSelection(),
					ArbitraryAreaManipulation.BLACK_OUT);
			try {
				workbench.setActiveImage(workingImage, IWorkbench.OWNERSHIP_CALLEE);
			} catch (SizeConstraintViolationException ex) {
				assert false;
			}
		}
	}

	private final class ConvertToGrayscale implements IMenuItemAction {

		private final String menuTitle = "Arbitrary selection";
		private final String actionName = "Convert to Grayscale";
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
			if (workbench.getPolygonSelection().npoints == 0) {
				JOptionPane.showMessageDialog(null, "No area selected.");
				return;
			}
			RasterImage workingImage = new RasterImage(workbench.getActiveImage());
			ArbitraryAreaManipulation.manipulateAtDistinguishedPixelLevel(workingImage,
					workbench.getPolygonSelection(),
					ArbitraryAreaManipulation.GRAYSCALE);
			try {
				workbench.setActiveImage(workingImage, IWorkbench.OWNERSHIP_CALLEE);
			} catch (SizeConstraintViolationException ex) {
				assert false;
			}
		}
	}

	private final class CustomFilter implements IMenuItemAction {

		private final String menuTitle = "Arbitrary selection";
		private final String actionName = "Custom Filter";
		private final Icon smallIcon = null;

		private CustomFilter() { }

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
			if (workbench.getPolygonSelection().npoints == 0) {
				JOptionPane.showMessageDialog(null, "No area selected.");
				return;
			}

			Color color = JColorChooser.showDialog(null, "Choose Filter Color", java.awt.Color.RED);
			if (color == null) return;

			RasterImage workingImage = new RasterImage(workbench.getActiveImage());
			ArbitraryAreaManipulation.manipulateAtDistinguishedPixelLevel(workingImage,
					workbench.getPolygonSelection(),
					new ArbitraryAreaManipulation.CustomFilter(color));
			try {
				workbench.setActiveImage(workingImage, IWorkbench.OWNERSHIP_CALLEE);
			} catch (SizeConstraintViolationException ex) {
				assert false;
			}
		}
	}

    private final class LocalHistogramEqualization implements IMenuItemAction {

		private final String menuTitle = "Edit";
		private final String actionName = "Local Histogram Equalization";
		private final Icon smallIcon = null;

		private LocalHistogramEqualization() { }

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

            ImagePixels imagePixels = new ImagePixels();

            BufferedImage tmpImg = ImageConverter.toBuffered(workbench.getActiveImage());

            tmpImg = imagePixels.marchThroughImage(tmpImg);

            RasterImage activeImg = new RasterImage(tmpImg.getWidth(), tmpImg.getHeight());
            ImageConverter.convertBufImage(tmpImg,activeImg);

                try {
                    workbench.setActiveImage(activeImg, IWorkbench.OWNERSHIP_CALLEE);
                } catch (SizeConstraintViolationException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

		}

    }
}