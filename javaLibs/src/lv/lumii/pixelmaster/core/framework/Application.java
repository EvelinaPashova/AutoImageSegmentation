
package lv.lumii.pixelmaster.core.framework;

import java.util.HashSet;
import java.util.Set;

import lv.lumii.pixelmaster.core.api.framework.IModule;

/**
 *
 * @author 
 */
final class Application {

	private static Workbench workbench = new Workbench();
	private static ControlElementRegistry controlElementRegistry = new ControlElementRegistry();

	/**
	 * Main function.
	 */
	public static void main(String[] args) {
		workbench.loadMainGUI();
		initCoreAndModules();
		workbench.initControlElements(controlElementRegistry.getMenuBar(), controlElementRegistry.getToolBar());
	}

	private static void initCoreAndModules() {

		new Core().init(controlElementRegistry, workbench);

		Set<IModule> modules = ModuleManager.getModules();

		for (IModule module: modules) {
			module.init(controlElementRegistry, workbench);
		}
	}
}

/**
 * Encapsulates the knowledge of what modules are present in PixelMaster.
 */
final class ModuleManager {

	/**
	 * Returns a set, not a list, to emphasize the fact that the order
	 * of module initialization is not important.
	 */
	static Set<IModule> getModules() {

		// TODO: use reflection to load modules

		Set<IModule> set = new HashSet<IModule>();
		set.add(new lv.lumii.pixelmaster.modules.binarization.framework.Module());
		set.add(new lv.lumii.pixelmaster.modules.filters.framework.Module());
		set.add(new lv.lumii.pixelmaster.modules.spw.framework.Module());
		set.add(new lv.lumii.pixelmaster.modules.transform.framework.Module());
		set.add(new lv.lumii.pixelmaster.modules.steganography.framework.Module());
		set.add(new lv.lumii.pixelmaster.modules.textarea.framework.Module());
		set.add(new lv.lumii.pixelmaster.modules.grapheditor.framework.Module());
		set.add(new lv.lumii.pixelmaster.modules.compare.framework.Module());
		set.add(new lv.lumii.pixelmaster.modules.recognition.framework.Module());
		set.add(new lv.lumii.pixelmaster.modules.ridge.framework.Module());
		set.add(new lv.lumii.pixelmaster.modules.search.framework.Module());
		return set;
	}
}
