
package lv.lumii.pixelmaster.modules.filters.gui;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import lv.lumii.pixelmaster.modules.filters.domain.MeanFilter;
import lv.lumii.pixelmaster.modules.filters.domain.MedianFilter;

import java.awt.Rectangle;
import javax.swing.SpinnerNumberModel;
import lv.lumii.pixelmaster.core.api.gui.ImageViewerModel;
import lv.lumii.pixelmaster.core.api.gui.RadioButtonPanelModel;

/**
 *
 * @author 
 */
public final class FilterFrameModel {

	// constants that define default settings
	private static final int DEFAULT_THRESHOLD = 15;
	private static final int DEFAULT_RADIUS = 3;

	// constants enumerating different filtering algorithms
	static final int MEDIAN_FILTER_SQUARE = 0;
	static final int MEDIAN_FILTER_BIDIRECTIONAL = 1;
	static final int MEAN_FILTER_SQUARE = 2;
	static final int VARIANCE_FILTER = 3;

	/*
	 * filtering radius, filter threshold, algorithm to use and
	 * the area which needs to be filtered
	 */
	private RasterImage image, origImage;
	private RadioButtonPanelModel algorithmPanelModel = new RadioButtonPanelModel(4, MEDIAN_FILTER_SQUARE);
	private ImageViewerModel imageViewerModel = new ImageViewerModel();
	private SpinnerNumberModel thresholdSpinnerModel = new SpinnerNumberModel(DEFAULT_THRESHOLD, 0, 255, 1),
			radiusSpinnerModel = new SpinnerNumberModel(DEFAULT_RADIUS, 0, 999, 1);


	public FilterFrameModel(RasterImage image) {
		origImage = new RasterImage(image);
		this.image = new RasterImage(image);
	}

	SpinnerNumberModel getThresholdSpinnerModel() {
		return thresholdSpinnerModel;
	}

	ImageViewerModel getImageViewerModel() {
		return imageViewerModel;
	}

	SpinnerNumberModel getRadiusSpinnerModel() {
		return radiusSpinnerModel;
	}

	RadioButtonPanelModel getRadioButtonPanelModel() {
		return algorithmPanelModel;
	}

	public RasterImage getImage() {
		return image;
	}

	void loadOriginalImage() {
		image = new RasterImage(origImage);
	}

	void applyFilter() {
		if (algorithmPanelModel.getSelectedIndex() == MEDIAN_FILTER_SQUARE) MedianFilter.medianFilter(image, image, (Integer) radiusSpinnerModel.getValue(), (Integer) thresholdSpinnerModel.getValue(), imageViewerModel.getRectangularSelection());
		else if (algorithmPanelModel.getSelectedIndex() == MEDIAN_FILTER_BIDIRECTIONAL) MedianFilter.medianFilterBidirectional(image, imageViewerModel.getRectangularSelection(), (Integer) radiusSpinnerModel.getValue(), (Integer) thresholdSpinnerModel.getValue());
		else if (algorithmPanelModel.getSelectedIndex() == MEAN_FILTER_SQUARE) MeanFilter.meanFilter(image, image, (Integer) radiusSpinnerModel.getValue(), (Integer) thresholdSpinnerModel.getValue(), imageViewerModel.getRectangularSelection());
		else if (algorithmPanelModel.getSelectedIndex() == VARIANCE_FILTER) MeanFilter.varianceFilter(image, image, null, (Integer) radiusSpinnerModel.getValue(), (Integer) thresholdSpinnerModel.getValue(), imageViewerModel.getRectangularSelection());
	}
}
