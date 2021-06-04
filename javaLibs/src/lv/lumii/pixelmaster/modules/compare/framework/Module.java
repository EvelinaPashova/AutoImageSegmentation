
package lv.lumii.pixelmaster.modules.compare.framework;

import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;
import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IModule;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.gui.ImageIoDialog;
import lv.lumii.pixelmaster.modules.compare.domain.BlurFilter;
import lv.lumii.pixelmaster.modules.compare.gui.*;
import lv.lumii.pixelmaster.modules.ridge.domain.Util;
import lv.lumii.pixelmaster.modules.spw.gui.SPWFrame;

/**
 * 
 */
public class Module implements IModule {

	// actions
	private OpenHandwritingRecognitionFrame openHandwritingRecognitionFrame = new OpenHandwritingRecognitionFrame();
	private CompareByPixelBlocks compareByPixelBlocks = new CompareByPixelBlocks();
	private CompareUsingGraphs compareUsingGraphs = new CompareUsingGraphs();
    private Blur blur = new Blur();
    private GaussianBlur gBlur = new GaussianBlur();
    private ConvolveAction convolveAction = new ConvolveAction();

	// workbench
	private IWorkbench workbench;

	// gui
	private RecognizeHandwritingFrame handwritingRecognitionFrame = null;
	private CompareByPixelBlocksFrame compareByPixelBlocksFrame = null;
	private CompareUsingGraphsFrame compareUsingGraphsFrame = null;
    private BlurFrame blurFrame = null;
    private GaussianFrame gaussianFrame = null;


	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		this.workbench = workbench;
		registry.registerMenuItem(openHandwritingRecognitionFrame);
		registry.registerMenuItem(compareByPixelBlocks);
		registry.registerMenuItem(compareUsingGraphs);
        registry.registerMenuItem(blur);
        registry.registerMenuItem(gBlur);
        registry.registerMenuItem(convolveAction);
	}


	private final class OpenHandwritingRecognitionFrame implements IMenuItemAction {

		private final String menuTitle = "Recognition";
		private final String actionName = "Recognize handwritten text";
		private final Icon smallIcon = null;

		private OpenHandwritingRecognitionFrame() { }

		@Override
		public void actionPerformed() {

			/*
			 * Keeping track of the window instances.
			 * Only one window may be opened at a time.
			 */
			if (handwritingRecognitionFrame == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }

					// activated when the user presses "Close" icon in the right top corner
					public void windowClosing(WindowEvent e) { handwritingRecognitionFrame = null; }

					// activated when the user presses "Close" button
					public void windowClosed(WindowEvent e) { handwritingRecognitionFrame = null; }

					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

				handwritingRecognitionFrame = new RecognizeHandwritingFrame(workbench.getActiveImage());

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				handwritingRecognitionFrame.addWindowListener(new WindowListenerImpl());
			}
			else {
				handwritingRecognitionFrame.toFront();
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

	private final class CompareByPixelBlocks implements IMenuItemAction {

		private final String menuTitle = "Compare";
		private final String actionName = "By pixel blocks";
		private final Icon smallIcon = null;

		private CompareByPixelBlocks() { }

		@Override
		public void actionPerformed() {

			/*
			 * Keeping track of the window instances.
			 * Only one window may be opened at a time.
			 */
			if (compareByPixelBlocksFrame == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }

					// activated when the user presses "Close" icon in the right top corner
					public void windowClosing(WindowEvent e) { compareByPixelBlocksFrame = null; }

					// activated when the user presses "Close" button
					public void windowClosed(WindowEvent e) { compareByPixelBlocksFrame = null; }

					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

				BufferedImage tmpBufImage = ImageIoDialog.loadImage(null);
				if (tmpBufImage == null) return;
				RasterImage source = new RasterImage(tmpBufImage.getWidth(), tmpBufImage.getHeight());
				ImageConverter.convertBufImage(tmpBufImage, source);

				compareByPixelBlocksFrame = new CompareByPixelBlocksFrame(source);

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				compareByPixelBlocksFrame.addWindowListener(new WindowListenerImpl());
			}
			else {
				compareByPixelBlocksFrame.toFront();
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

    	private final class ConvolveAction implements IMenuItemAction {

		private final String menuTitle = "Compare";
		private final String actionName = "Convolve";
		private final Icon smallIcon = null;

		private ConvolveAction() { }

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

            File[] tmpBufImage = ImageIoDialog.loadMultipleFile();
            BufferedImage loadImg = null;
            try {
                loadImg = ImageIO.read(tmpBufImage[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            BlurFilter blurFilter = new BlurFilter();


            BufferedImage tmpImg = ImageConverter.toBuffered(workbench.getActiveImage());


            tmpImg = blurFilter.filter(tmpImg, tmpImg, loadImg);

            //System.out.println("tmpImg" + tmpImg.getRGB(0,2));

            RasterImage activeImg = new RasterImage(tmpImg.getWidth(), tmpImg.getHeight());
            ImageConverter.convertBufImage(tmpImg,activeImg);


            try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
			}
		}
	}

    private final class GaussianBlur implements IMenuItemAction {

        private final String menuTitle = "Compare";
        private final String actionName = "GaussianBlur";
        private final Icon smallIcon = null;

        private GaussianBlur() { }

        public void actionPerformed() {
            /*
			 * Keeping track of the window instances.
			 * Only one window may be opened at a time.
			 */
			if (gaussianFrame == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }

					// activated when the user presses "Close" icon in the right top corner
					public void windowClosing(WindowEvent e) { gaussianFrame = null; }

					// activated when the user presses "Close" button
					public void windowClosed(WindowEvent e) { gaussianFrame = null; }

					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

                //TODO: To Frame
                
				gaussianFrame = new GaussianFrame(workbench);

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				gaussianFrame.addWindowListener(new WindowListenerImpl());
			}
			else {
				gaussianFrame.toFront();
			}
        }

		public boolean isEnabled() {
			return true;
		}

		public Icon getSmallIcon() {
			return smallIcon;
		}

		public String getName() {
			return actionName;
		}

		public String getMenuTitle() {
			return menuTitle;
		}
    }


    private final class Blur implements IMenuItemAction {

        private final String menuTitle = "Compare";
        private final String actionName = "Blur";
        private final Icon smallIcon = null;

        private Blur() { }

        public void actionPerformed() {
            /*
			 * Keeping track of the window instances.
			 * Only one window may be opened at a time.
			 */
			if (blurFrame == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }

					// activated when the user presses "Close" icon in the right top corner
					public void windowClosing(WindowEvent e) { blurFrame = null; }

					// activated when the user presses "Close" button
					public void windowClosed(WindowEvent e) { blurFrame = null; }

					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

                //TODO: To Frame

				blurFrame = new BlurFrame(workbench);

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				blurFrame.addWindowListener(new WindowListenerImpl());
			}
			else {
				blurFrame.toFront();
			}
        }

		public boolean isEnabled() {
			return true;
		}

		public Icon getSmallIcon() {
			return smallIcon;
		}

		public String getName() {
			return actionName;
		}

		public String getMenuTitle() {
			return menuTitle;
		}
    }

	private final class CompareUsingGraphs implements IMenuItemAction {

		private final String menuTitle = "Compare";
		private final String actionName = "Using graphs";
		private final Icon smallIcon = null;

		private CompareUsingGraphs() { }

		@Override
		public void actionPerformed() {

			/*
			 * Keeping track of the window instances.
			 * Only one window may be opened at a time.
			 */
			if (compareUsingGraphsFrame == null) {

				final class WindowListenerImpl implements WindowListener {
					private WindowListenerImpl() { }
					public void windowOpened(WindowEvent e) { }

					// activated when the user presses "Close" icon in the right top corner
					public void windowClosing(WindowEvent e) { compareUsingGraphsFrame = null; }

					// activated when the user presses "Close" button
					public void windowClosed(WindowEvent e) { compareUsingGraphsFrame = null; }

					public void windowIconified(WindowEvent e) { }
					public void windowDeiconified(WindowEvent e) { }
					public void windowActivated(WindowEvent e) { }
					public void windowDeactivated(WindowEvent e) { }
				}

				compareUsingGraphsFrame = new CompareUsingGraphsFrame();

				/*
				 * This listener assigns null to the pointer when the window
				 * closes so that the garbage collector can free the memory.
				 */
				compareUsingGraphsFrame.addWindowListener(new WindowListenerImpl());
			}
			else {
				compareUsingGraphsFrame.toFront();
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
