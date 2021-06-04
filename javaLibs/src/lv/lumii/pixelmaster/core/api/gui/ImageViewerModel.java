
package lv.lumii.pixelmaster.core.api.gui;

import java.awt.Polygon;
import java.awt.Rectangle;

/**
 * This class represents a model for the {@link ImageViewer}.
 * The model's state is defined by the currently selected rectangular and
 * the polygon areas.
 *
 * @author Jevgeny Jonas
 */
public final class ImageViewerModel {

	private Rectangle rectangularSelection = null;
	Polygon polygonSelection = new Polygon();

	/**
	 * Constructs a new model in which no rectangukar and no polygon area is selected.
	 */
	public ImageViewerModel() { }

	/**
	 * Returns the curretly selected rectangular area.
	 * @return the selected area, or null if no rectangular area is selected (ownership: caller)
	 */
	public Rectangle getRectangularSelection() {
		if (rectangularSelection == null) return null;
		return new Rectangle(rectangularSelection);
	}

	void setRectangularSelection(Rectangle selection) {
		this.rectangularSelection = selection;
	}

	/**
	 * Returns the curretly selected polygon area.
	 * @return the area (non-null pointer; ownership: caller).
	 *		If no polygon area is selected,
	 *		this method returns an empty polygon.
	 */
	public Polygon getPolygonSelection() {
		return new Polygon(polygonSelection.xpoints, polygonSelection.ypoints, polygonSelection.npoints);
	}

	void setPolygonSelection(Polygon selection) {
		this.polygonSelection = selection;
	}
}
