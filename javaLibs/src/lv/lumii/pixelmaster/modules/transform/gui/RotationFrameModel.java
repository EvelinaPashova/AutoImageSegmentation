
package lv.lumii.pixelmaster.modules.transform.gui;

import javax.swing.SpinnerNumberModel;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.gui.*;
import lv.lumii.pixelmaster.modules.transform.domain.rotation.RotateByShear;

/**
 * 
 */
public final class RotationFrameModel {

	// angle in degrees
	private SpinnerNumberModel angle = new SpinnerNumberModel(20, -44, 45, 1);
	private ImageViewerModel imageViewerModel = new ImageViewerModel();
	private RasterImage image, origImage;

	public RotationFrameModel(RasterImage image) {
		origImage = new RasterImage(image);
		this.image = new RasterImage(image);
		
	}

	SpinnerNumberModel getAngle() { return angle; }

	ImageViewerModel getImageViewerModel() {
		return imageViewerModel;
	}

	public RasterImage getImage() {
		return image;
	}

	void loadOriginalImage() {
		image = new RasterImage(origImage);
	}

	void rotate() {
		image = RotateByShear.rotate(image, (Integer) angle.getValue(), 0x00000000);
	}
}
