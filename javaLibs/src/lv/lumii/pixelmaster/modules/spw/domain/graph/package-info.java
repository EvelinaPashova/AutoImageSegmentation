
/**
 * Provides implementation of graphs to represent structure
 * of a text image, as well as facilities for graph I/O. The main data
 * structure is an {@linkplain pm.graph.UGraph unoriented graph},
 * whose {@linkplain pm.graph.Vertex vertices} represent pixels of an
 * {@linkplain pm.util.RasterImage image}.
 * A graph is represented as a set of vertices,
 * which may be connected with each other by {@linkplain pm.graph.GraphEdge edges}.
 * This package also includes specific iterator classes
 * to iterate through all vertices or edges.
 * A graph may be used as a representation of a text image, providing facilities for
 * analyzing its structure. The graph I/O functionality can
 * be used to store structure of a text image in a file and retrieve it
 * for analysis.
 *
 * @author Jevgeny Jonas
 */
package lv.lumii.pixelmaster.modules.spw.domain.graph;
