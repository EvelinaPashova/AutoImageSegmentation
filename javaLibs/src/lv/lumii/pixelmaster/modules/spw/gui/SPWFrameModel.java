
package lv.lumii.pixelmaster.modules.spw.gui;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.modules.spw.domain.*;
import lv.lumii.pixelmaster.modules.spw.domain.graph.UGraph;

/**
 *
 * @author Jonas
 */
public final class SPWFrameModel {

	final static String CONNECTIVITY_16 = "16";
	final static String CONNECTIVITY_4_8 = "4/8";

	private SpinnerNumberModel sizeSpinnerModel = new SpinnerNumberModel(1, 1, 999, 1),
			coefficientSpinnerModel = new SpinnerNumberModel(-0.7, -1.0, 1.0, 0.05),
			distanceSpinnerModel = new SpinnerNumberModel(10.0, 0.0, 999.0, 0.5),
			varianceSpinnerModel = new SpinnerNumberModel(5.8, 0.0, 999.0, 0.05),
			lengthSpinnerModel = new SpinnerNumberModel(10.2, 0.0, 999.0, 0.05);
	private RasterImage image, waveImage, origImage;
	private UGraph g;
	private boolean [] bgr;
	private String[] elements = {CONNECTIVITY_16, CONNECTIVITY_4_8};
	private ComboBoxModel connectivityComboBoxModel = new DefaultComboBoxModel(elements);

	public SPWFrameModel(RasterImage image) {
		origImage = new RasterImage(image);
		this.image = new RasterImage(image.getWidth(), image.getHeight());
		bgr = new boolean [image.getSize()];
		for (int i = 0; i < bgr.length; i++) bgr[i] = image.getRGB(i) != 0;

		waveImage=new RasterImage(image.getWidth(), image.getHeight());
		g = new UGraph();
	}

	SpinnerNumberModel getSizeSpinnerModel() {
		return sizeSpinnerModel;
	}

	SpinnerNumberModel getCoefficientSpinnerModel() {
		return coefficientSpinnerModel;
	}

	SpinnerNumberModel getDistanceSpinnerModel() {
		return distanceSpinnerModel;
	}

	SpinnerNumberModel getVarianceSpinnerModel() {
		return varianceSpinnerModel;
	}

	SpinnerNumberModel getLengthSpinnerModel() {
		return lengthSpinnerModel;
	}

	ComboBoxModel getConnectivityComboBoxModel() {
		return connectivityComboBoxModel;
	}

	RasterImage getImage() {
		return image;
	}

	RasterImage getOrigImage() {
		return origImage;
	}

	UGraph getGraph() {
		return g;
	}

	void applySVA() {
		int mode;
		if (connectivityComboBoxModel.getSelectedItem().equals(CONNECTIVITY_16)) {
			mode = 1;
		}
		else {
			assert connectivityComboBoxModel.getSelectedItem().equals(CONNECTIVITY_4_8);
			mode = 2;
		}
		g = new UGraph();
		SphericalWave.buildStructure(bgr, origImage.getWidth(), origImage.getHeight(),
			waveImage, g, (Integer) sizeSpinnerModel.getValue(), mode);
		origImage.copyTo(image);
		SphericalWave.drawStructure(image, g);
	}

	void primaryOptimization() {
		Optimization.primaryOptimization(g, (Double) coefficientSpinnerModel.getValue());
		origImage.copyTo(image);
		SphericalWave.drawStructure(image, g);
	}

	void connectEdges() {
		Optimization.connectEnds(g, (Double) distanceSpinnerModel.getValue());
		origImage.copyTo(image);
		SphericalWave.drawStructure(image, g);
	}

	void edgeOptimization() {
		Optimization.optimizeEdges(g, (Double) varianceSpinnerModel.getValue());
		origImage.copyTo(image);
		SphericalWave.drawStructure(image, g);
	}

	void cutTails() {
		Optimization.cutTails(g, (Double) lengthSpinnerModel.getValue());
		origImage.copyTo(image);
		SphericalWave.drawStructure(image, g);
	}
}
