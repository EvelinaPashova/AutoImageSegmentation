
package lv.lumii.pixelmaster.modules.binarization.gui;

import javax.swing.SpinnerNumberModel;
import lv.lumii.pixelmaster.core.api.domain.RGB;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.gui.SliderWithSpinnerModel;
import lv.lumii.pixelmaster.modules.binarization.domain.*;

/**
 * Binarization frame model
 */
public final class BinarizationFrameModel {

	static final class RGBPanelModel {
		private SliderWithSpinnerModel r, g, b;

		private RGBPanelModel() {
			r = new SliderWithSpinnerModel(RGB.DEFAULT_RED, 0, 100);
			g = new SliderWithSpinnerModel(RGB.DEFAULT_GREEN, 0, 100);
			b = new SliderWithSpinnerModel(RGB.DEFAULT_BLUE, 0, 100);
		}

		int getRed() { return getRedSliderWithSpinnerModel().getValue(); }
		int getGreen() { return getGreenSliderWithSpinnerModel().getValue(); }
		int getBlue() { return getBlueSliderWithSpinnerModel().getValue(); }

		SliderWithSpinnerModel getRedSliderWithSpinnerModel() { return r; }
		SliderWithSpinnerModel getGreenSliderWithSpinnerModel() { return g; }
		SliderWithSpinnerModel getBlueSliderWithSpinnerModel() { return b; }
	}

	// constants that define default settings
	private static final int DEFAULT_GLOBAL_THRESHOLD = 125;
	private static final int DEFAULT_BERNSEN_THRESHOLD = 10;
	private static final int DEFAULT_BERNSEN_RADIUS = 15;
	private static final double DEFAULT_NIBLACK_COEFFICIENT = -0.2;
	private static final int DEFAULT_NIBLACK_RADIUS = 10;

	// constants enumerating different algorithms
	static final int GLOBAL_BINARIZATION = 0;
	static final int GLOBAL_GRAYSCALE = 1;
	static final int BERNSEN_BINARIZATION = 2;
	static final int NIBLACK_BINARIZATION = 3;

	// original and working images
	private RasterImage image, origImage;

	// RGB settings
	private RGBPanelModel rgbPanelModel = new RGBPanelModel();

	// settings for the global binarization algorithm
	private SliderWithSpinnerModel globalThresholdSpinnerModel = new SliderWithSpinnerModel(DEFAULT_GLOBAL_THRESHOLD, 0, 255);

	// settings for the Bernsen algorithm
	private SpinnerNumberModel bernsenRadiusSpinnerModel = new SpinnerNumberModel(DEFAULT_BERNSEN_RADIUS, 0, 999, 1);
	private SpinnerNumberModel bernsenThresholdSpinnerModel = new SpinnerNumberModel(DEFAULT_BERNSEN_THRESHOLD, 0, 255, 1);

	// settings for the Niblack algorithm
	private SpinnerNumberModel niblackRadiusSpinnerModel = new SpinnerNumberModel(DEFAULT_NIBLACK_RADIUS, 0, 999, 1);
	private SpinnerNumberModel niblackCoefficientSpinnerModel = new SpinnerNumberModel(DEFAULT_NIBLACK_COEFFICIENT, -999.00, 999.00, 0.1);


	public BinarizationFrameModel(RasterImage image) {
		origImage = new RasterImage(image);
		this.image = new RasterImage(image);
	}

	SliderWithSpinnerModel getGlobalThresholdSliderWithSpinnerModel() {
		return globalThresholdSpinnerModel;
	}

	SpinnerNumberModel getBernsenRadiusSpinnerModel() {
		return bernsenRadiusSpinnerModel;
	}

	SpinnerNumberModel getBernsenThresholdSpinnerModel() {
		return bernsenThresholdSpinnerModel;
	}

	SpinnerNumberModel getNiblackRadiusSpinnerModel() {
		return niblackRadiusSpinnerModel;
	}

	SpinnerNumberModel getNiblackCoefficientSpinnerModel() {
		return niblackCoefficientSpinnerModel;
	}

	RGBPanelModel getRGBPanelModel() { return rgbPanelModel; }

	public RasterImage getImage() {
		return image;
	}

	void loadOriginalImage() {
		image = new RasterImage(origImage);
	}

	void applyBernsenAlgorithm() {
		Binarization.BernsenBinarization(image, image, rgbPanelModel.getRed(), rgbPanelModel.getGreen(), rgbPanelModel.getBlue(),
				(Integer) bernsenRadiusSpinnerModel.getValue(),
				(Integer) bernsenThresholdSpinnerModel.getValue()
		);
	}

	void applyNiblackAlgorithm() {
		Binarization.NiblackBinarization(image, image, rgbPanelModel.getRed(), rgbPanelModel.getGreen(), rgbPanelModel.getBlue(),
				(Integer) niblackRadiusSpinnerModel.getValue(),
				(Double) niblackCoefficientSpinnerModel.getValue());
	}

	void applyGlobalBinarization() {
		Binarization.binary(image, image, globalThresholdSpinnerModel.getValue(), rgbPanelModel.getRed(), rgbPanelModel.getGreen(), rgbPanelModel.getBlue());
	}

	void convertToGrayscale() {
		GrayScale.greyImage(image, image, rgbPanelModel.getRed(), rgbPanelModel.getGreen(), rgbPanelModel.getBlue());
	}
}
