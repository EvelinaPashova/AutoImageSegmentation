
package lv.lumii.pixelmaster.modules.transform.gui;

import javax.naming.SizeLimitExceededException;
import javax.swing.SpinnerNumberModel;
import lv.lumii.pixelmaster.core.api.gui.SliderWithSpinnerModel;
import lv.lumii.pixelmaster.core.api.domain.Constants;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;
import lv.lumii.pixelmaster.modules.transform.domain.resizing.NearestNeighbourResample;
import lv.lumii.pixelmaster.modules.transform.domain.resizing.Spline;

/**
 * 
 */
public final class ResizingFrameModel {

	public final class NotApplicable extends Exception {
		private NotApplicable(String message) {
			super(message);
		}
	}

	// settings for the Nearest Neighbour algorithm
	private SpinnerNumberModel kxSpinnerModel = new SpinnerNumberModel(1, 1, Constants.MAX_IMAGE_WIDTH, 1);
	private SpinnerNumberModel kySpinnerModel = new SpinnerNumberModel(1, 1, Constants.MAX_IMAGE_HEIGHT, 1);

	// settings for the Bilinear Resample algorithm
	private SliderWithSpinnerModel newWidthSliderWithSpinnerModel = new SliderWithSpinnerModel(2, 2, Constants.MAX_IMAGE_WIDTH);
	private SliderWithSpinnerModel newHeightSliderWithSpinnerModel = new SliderWithSpinnerModel(2, 2, Constants.MAX_IMAGE_HEIGHT);

	public ResizingFrameModel() { }

	SpinnerNumberModel getKxSpinnerModel() {
		return kxSpinnerModel;
	}

	SpinnerNumberModel getKySpinnerModel() {
		return kySpinnerModel;
	}

	SliderWithSpinnerModel getNewWidthSliderWithSpinnerModel() {
		return newWidthSliderWithSpinnerModel;
	}

	SliderWithSpinnerModel getNewHeightSliderWithSpinnerModel() {
		return newHeightSliderWithSpinnerModel;
	}

	public RasterImage applyNearestNeighbourResample(RasterImage image) throws NotApplicable, SizeConstraintViolationException {
		if (image == null) throw new NotApplicable("No image loaded");

		int kx = (Integer) kxSpinnerModel.getValue();
		int ky = (Integer) kySpinnerModel.getValue();
		int newWidth = kx * image.getWidth();
		int newHeight = ky * image.getHeight();

		if (newWidth > Constants.MAX_IMAGE_WIDTH || newHeight > Constants.MAX_IMAGE_HEIGHT) {
			throw new SizeConstraintViolationException("Image too large");
		}

		return NearestNeighbourResample.nnResample(image, kx, ky);
	}

	public RasterImage applyBilinearResample(RasterImage image) throws NotApplicable {
		if (image == null) throw new NotApplicable("No image loaded");
		if (image.getWidth() == 1 || image.getHeight() == 1) {
			throw new NotApplicable("Bilinear resample algorithm can only be applied to an image of size 2x2 or bigger");
		}

		return Spline.resizeImage(image, newWidthSliderWithSpinnerModel.getValue(), newHeightSliderWithSpinnerModel.getValue());
	}
}
