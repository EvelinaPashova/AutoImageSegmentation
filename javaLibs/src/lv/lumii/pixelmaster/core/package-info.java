
/**
 * <p>This package and its subpackages comprise the core of the application.
 * The core provides the basic functionality: opening an image file,
 * displaying the image in the main window, saving it to a file and closing it.</p>
 *
 * <p>The core defines how the graphical user interface will look like and
 * is responsible for the startup process of the application.
 * It dictates the flow of control (contains the <code>main</code> method)
 * and initializes all modules.</p>
 *
 * <p>The core provides the
 * minimal functionality needed for the application to work. It is responsible
 * only for the image viewer part of the application: loading an image,
 * displaying it in the main window, saving and closing it.
 * Additional functionality is provided by the modules.</p>
 *
 * <p>The core is self-sufficient: even if all modules are turned off, leaving only
 * the core, the application will be able to run but will have only the
 * functionality of an image viewer.</p>
 *
 * <h3>Core GUI: the Main Window</h3>
 *
 * <p>When the application starts, the main window is loaded, which is the
 * user's workbench - the area where user does his work.
 * The main window displays the currently opened image and has the following layout:
 * the image area in the center, the toolbar and the menu.
 * The core is responsible for the main window: it controls what image is currently
 * displayed and lets the user select a rectangular or a polygon area
 * of the image.</p>
 *
 * <p>The layout of the main window is quite simple, however, modules can
 * provide more sophisticated GUIs.</p>
 *
 *
 * <h3>The Core and the Modules</h3>
 *
 * <p>The core provides "extension points" which modules use to add their
 * functionality. During the initialization stage, the modules can
 * add their control elements (toolbar buttons and menu items) and register
 * callbacks that will be called when the user activates the corresponding control element.</p>
 *
 * <p>The typical workflow is as follows: the user opens an image file and
 * invokes some function. At this moment, the corresponding module's callback is
 * called. The module uses the core's {@link lv.lumii.pixelmaster.core.api.framework.IWorkbench#getActiveImage API}
 * to get the currently loaded image that
 * is displayed in the main window. The module then applies its algorithm to
 * the image, possibly opens another graphical window and returns the resulting
 * image (if any) to the core (again, using the core's
 * {@link lv.lumii.pixelmaster.core.api.framework.IWorkbench#setActiveImage API}).
 * The core is responsible for displaying it in the main window.</p>
 *
 *
 * <h3>For Module Developers</h3>
 *
 * <p>The core provides an {@link lv.lumii.pixelmaster.core.api API} to be used by the module developers.
 * Module developers should use only the core classes and interfaces within this API package.
 * Classes and interfaces in the packages <code>lv.lumii.pixelmaster.core.domain</code>,
 * <code>lv.lumii.pixelmaster.core.framework</code> and <code>lv.lumii.pixelmaster.core.gui</code>
 * are internal to the core. Do not use them, since they may change in the future.</p>
 *
 * @author Jevgeny Jonas
 */
package lv.lumii.pixelmaster.core;
