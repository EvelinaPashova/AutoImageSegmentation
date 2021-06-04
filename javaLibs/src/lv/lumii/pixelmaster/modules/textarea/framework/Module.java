
package lv.lumii.pixelmaster.modules.textarea.framework;

import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IModule;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;

import javax.swing.Icon;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;
import lv.lumii.pixelmaster.modules.textarea.domain.Util;

/**
 * 
 */
public class Module implements IModule {

	// actions
	private DrawContrast drawContrast = new DrawContrast();
	private DrawTextBoxes drawTextBoxes = new DrawTextBoxes();

	// workbench
	private IWorkbench workbench;


	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		this.workbench = workbench;
		registry.registerMenuItem(drawContrast);
		registry.registerMenuItem(drawTextBoxes);
	}


	private final class DrawContrast implements IMenuItemAction {

		private final String menuTitle = "Textarea";
		private final String actionName = "Draw contrast";
		private final Icon smallIcon = null;

		private DrawContrast() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.drawContrast(workbench.getActiveImage());

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

	private final class DrawTextBoxes implements IMenuItemAction {

		private final String menuTitle = "Textarea";
		private final String actionName = "Draw text boxes";
		private final Icon smallIcon = null;

		private DrawTextBoxes() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.drawTextBoxes(workbench.getActiveImage());

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
}
