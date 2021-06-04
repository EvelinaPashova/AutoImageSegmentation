
package lv.lumii.pixelmaster.core.api.framework;

/**
 * <p>This interface represents a module of PixelMaster.
 * Each module should implement this interface.
 * The implementing class should be named <code>Module</code> and should be
 * placed in the package {@link lv.lumii.pixelmaster.modules}<code>.[module_name].framework</code>.</p>
 *
 * <p>The implementing class must be conrete (not abstract) and must have a
 * public constructor <code>Module()</code> with no arguments. The core will
 * use this constructor to create an instance of the module during application start-up.</p>
 *
 * @author Jevgeny Jonas
 */
public interface IModule {

	/**
	 * Initialization routine that is called during application start-up.
	 * The core provides instances of the <code>IControlElementRegistry</code>
	 * and <code>IWorkbench</code> interfaces. Modules can keep a reference to
	 * the workbench so that they can use it later. However, modules shouldn't
	 * use the registry outside of this method's body: they should register
	 * their control elements within this method.
	 *
	 * @param registry The registry that keeps track of all control elements
	 *		in the main window. Any calls of the methods of this object that
	 *		occur after this method has returned have undefined behaviour.
	 * @param workbench The workbench that provides access to the main window context.
	 */
	public void init(IControlElementRegistry registry, IWorkbench workbench);
}
