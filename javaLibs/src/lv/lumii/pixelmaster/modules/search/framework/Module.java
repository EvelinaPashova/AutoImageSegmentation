
package lv.lumii.pixelmaster.modules.search.framework;

import java.awt.image.BufferedImage;
import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IModule;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;
import lv.lumii.pixelmaster.core.api.gui.ImageIoDialog;
import lv.lumii.pixelmaster.modules.search.domain.imagePyramid;
import lv.lumii.pixelmaster.modules.search.domain.ngcCorrelation;

/**
 * 
 */
public class Module implements IModule {

	// actions
	private LoadTargetImage loadTargetImage = new LoadTargetImage();
	private StartNgcSearch startNgcSearch = new StartNgcSearch();

	// workbench
	private IWorkbench workbench;

	// target image to search for
	private RasterImage targetImage = null;

	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		this.workbench = workbench;
		registry.registerMenuItem(loadTargetImage);
		registry.registerMenuItem(startNgcSearch);
	}


	private final class LoadTargetImage implements IMenuItemAction {

		private final String menuTitle = "Search";
		private final String actionName = "Load target image";
		private final Icon smallIcon = null;

		private LoadTargetImage() { }

		@Override
		public void actionPerformed() {
			BufferedImage tmpBufImage = ImageIoDialog.loadImage(null);
			if (tmpBufImage==null) return;
			if (tmpBufImage.getWidth() < 1 || tmpBufImage.getHeight() < 1) {
				JOptionPane.showMessageDialog(null, "Image too small");
				return;
			}
			targetImage=new RasterImage(tmpBufImage.getWidth(), tmpBufImage.getHeight());
			ImageConverter.convertBufImage(tmpBufImage, targetImage);
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

	private final class StartNgcSearch implements IMenuItemAction {

		private final String menuTitle = "Search";
		private final String actionName = "Start NGC Search";
		private final Icon smallIcon = null;

		private StartNgcSearch() { }

		@Override
		public void actionPerformed() {

			if (targetImage == null) {
				JOptionPane.showMessageDialog(null, "Load target image");
				return;
			}

			imagePyramid[] mem = new imagePyramid[2];

			mem[0] = new imagePyramid(workbench.getActiveImage());
			mem[1] = new imagePyramid(targetImage);
			RasterImage tmp = ngcCorrelation.ngcCorrelation(mem[0], mem[1]);
			if (tmp != null) {
				try {
					workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
				}
				catch (SizeConstraintViolationException exc) {
					assert false;
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Target image not found");
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
}
