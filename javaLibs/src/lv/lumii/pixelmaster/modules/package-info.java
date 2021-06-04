
/**
 * <p>This package is the root of the PixelMaster's module packages.
 * It contains all modules of the application. Each module is placed in its own
 * subpackage.</p>
 *
 * <p>Packages of each module should be structured as follows:
 * <ul>
 * <li><code>[module_name].domain</code></li>
 * <li><code>[module_name].gui</code></li>
 * <li><code>[module_name].framework</code></li>
 * </ul>
 *
 * The <code>[module_name].domain</code> package should contain the domain logic
 * (the domain is image processing): algorithms and data structures.
 *
 * The <code>[module_name].gui</code> package should contain the graphical user
 * interface components.
 *
 * The <code>[module_name].framework</code> package should contain the code
 * responsible for interfacing with the core. It should contain the class
 * <code>Module</code> that implements the interface
 * {@link lv.lumii.pixelmaster.core.api.framework.IModule}. Classes implementing
 * other interfaces from the package {@link lv.lumii.pixelmaster.core.api.framework}
 * should also go there.</p>
 *
 */
package lv.lumii.pixelmaster.modules;
