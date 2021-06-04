
/**
 * <p>This package is the root of the PixelMaster package structure.</p>
 *
 *
 * <h3>The Core and the Modules</h3>
 *
 * <p>The application consists of the core and the modules.
 * The core resides in the package {@link lv.lumii.pixelmaster.core},
 * and the modules reside in the package {@link lv.lumii.pixelmaster.modules},
 * wherein each module has its own subpackage.</p>
 *
 *
 * <h3><code>*.framework</code>, <code>*.domain</code> and <code>*.gui</code></h3>
 *
 * <p>Both the core and the modules have their packages structured in three layers:
 * <ul>
 * <li>domain layer</li>
 * <li>GUI layer</li>
 * <li>framework layer</li>
 * </ul>
 * The names of packages of the domain layer end in <code>.domain</code>,
 * framework layer - <code>.framework</code>, and the GUI layer - <code>.gui</code>.</p>
 *
 * <p>The core's and the modules' packages that end in <code>.framework</code>
 * together comprise the framework of the application.
 * This framework defines the overall architecture of the PixelMaster:
 * the application has the {@link lv.lumii.pixelmaster.core core} and can be
 * extended with {@link lv.lumii.pixelmaster.modules modules}.</p>
 *
 * <p>The domain layer contains the data structures and algorithms related to
 * the problem domain (image processing).</p>
 *
 * <p>The GUI layer contains the graphical user interface elements and event listeners.</p>
 *
 * <p>The framework layer creates the GUI (thus, it is dependant on the GUI layer).
 * The GUI layer and the framework layer are dependant on the domain layer.
 * The domain layer should not be dependant on other layers,
 * and the GUI layer should not be dependant on the framework layer.</p>
 *
 */
package lv.lumii.pixelmaster;
