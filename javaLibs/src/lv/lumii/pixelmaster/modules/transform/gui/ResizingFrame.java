
package lv.lumii.pixelmaster.modules.transform.gui;

import javax.swing.*;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import lv.lumii.pixelmaster.core.api.gui.SliderWithSpinner;

/**
 * Image resizing frame.
 * @author Jevgenijs Jonass.
 * @since 25.03.2009
 */
public final class ResizingFrame extends JFrame {

	private ResizingFrameModel resizingFrameModel;

	private Action applyNearestNeighbourResample, applyBilinearResample;

	private JPanel panel, currentWidthPanel, currentHeightPanel, nnResamplePanel, bilinearResamplePanel,
			newWidthPanel, newHeightPanel, kxPanel, kyPanel;
	private JTabbedPane tabbedPane;
	private JTextArea currentWidthTextArea, currentHeightTextArea;
	private JLabel newWidthLabel, newHeightLabel, currentWidthLabel, currentHeightLabel, kxLabel, kyLabel;
	private JSpinner kxSpinner, kySpinner;
	private SliderWithSpinner newWidthSliderWithSpinner, newHeightSliderWithSpinner;
	private JButton nnResampleButton, bilinearResampleButton;

	public ResizingFrame(ResizingFrameModel resizingFrameModel,
			Action applyNearestNeighbourResample, Action applyBilinearResample,
			RasterImage image) {

		super("Resizing");

		assert resizingFrameModel != null && applyNearestNeighbourResample != null && applyBilinearResample != null;

		this.resizingFrameModel = resizingFrameModel;
		this.applyNearestNeighbourResample = applyNearestNeighbourResample;
		this.applyBilinearResample = applyBilinearResample;

		setVisible(false);
		setSize(250, 230);
		setLocation(100, 300);
		setResizable(false);
		createAndShowGUI(getWidth(image), getHeight(image));
	}

	private static int getWidth(RasterImage image) {
		if (image == null) return 0;
		return image.getWidth();
	}

	private static int getHeight(RasterImage image) {
		if (image == null) return 0;
		return image.getHeight();
	}

	private void createAndShowGUI(int imageWidth, int imageHeight) {
		createPanels();
		createComponents(imageWidth, imageHeight);
		addComponents();
		setVisible(true);
	}

	private void createPanels() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		currentWidthPanel = new JPanel();
		currentWidthPanel.setLayout(new BoxLayout(currentWidthPanel, BoxLayout.LINE_AXIS));

		currentHeightPanel = new JPanel();
		currentHeightPanel.setLayout(new BoxLayout(currentHeightPanel, BoxLayout.LINE_AXIS));

		nnResamplePanel = new JPanel();
		nnResamplePanel.setLayout(new BoxLayout(nnResamplePanel, BoxLayout.PAGE_AXIS));

		bilinearResamplePanel = new JPanel();
		bilinearResamplePanel.setLayout(new BoxLayout(bilinearResamplePanel, BoxLayout.PAGE_AXIS));

		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Nearest Neighbour", nnResamplePanel);
		tabbedPane.addTab("Bilinear", bilinearResamplePanel);

		panel.add(currentWidthPanel);
		panel.add(currentHeightPanel);
		panel.add(tabbedPane);

		newWidthPanel = new JPanel();
		newWidthPanel.setLayout(new BoxLayout(newWidthPanel, BoxLayout.LINE_AXIS));

		newHeightPanel = new JPanel();
		newHeightPanel.setLayout(new BoxLayout(newHeightPanel, BoxLayout.LINE_AXIS));

		bilinearResamplePanel.add(newWidthPanel);
		bilinearResamplePanel.add(newHeightPanel);

		kxPanel = new JPanel();
		kxPanel.setLayout(new BoxLayout(kxPanel, BoxLayout.LINE_AXIS));

		kyPanel = new JPanel();
		kyPanel.setLayout(new BoxLayout(kyPanel, BoxLayout.LINE_AXIS));

		nnResamplePanel.add(kxPanel);
		nnResamplePanel.add(kyPanel);

		add(panel);
	}

	private void createComponents(int imageWidth, int imageHeight) {

		newWidthSliderWithSpinner = new SliderWithSpinner(
				resizingFrameModel.getNewWidthSliderWithSpinnerModel(),
				SwingConstants.HORIZONTAL,
				true);
		newHeightSliderWithSpinner = new SliderWithSpinner(
				resizingFrameModel.getNewHeightSliderWithSpinnerModel(),
				SwingConstants.HORIZONTAL,
				true);
		kxSpinner = new JSpinner(resizingFrameModel.getKxSpinnerModel());
		kySpinner = new JSpinner(resizingFrameModel.getKySpinnerModel());

		JFormattedTextField ftf = ((JSpinner.DefaultEditor) (kxSpinner.getEditor())).getTextField();
		ftf.setEditable(false);

		ftf = ((JSpinner.DefaultEditor) (kySpinner.getEditor())).getTextField();
		ftf.setEditable(false);

        currentWidthTextArea = new JTextArea();
		currentWidthTextArea.setRows(1);
        currentWidthTextArea.setEditable(false);

        currentHeightTextArea = new JTextArea();
		currentHeightTextArea.setRows(1);
        currentHeightTextArea.setEditable(false);

		updateTextFields(imageWidth, imageHeight);

		newWidthLabel = new JLabel("new width:");
		newHeightLabel = new JLabel("new height:");
		kxLabel = new JLabel("stretch horizontally N times:");
		kyLabel = new JLabel("stretch vertically N times:");
		currentWidthLabel = new JLabel("current width:");
		currentHeightLabel = new JLabel("current height:");

		newWidthLabel.setLabelFor(newWidthSliderWithSpinner);
		newHeightLabel.setLabelFor(newHeightSliderWithSpinner);
		kxLabel.setLabelFor(kxSpinner);
		kyLabel.setLabelFor(kySpinner);
		currentWidthLabel.setLabelFor(currentWidthTextArea);
		currentHeightLabel.setLabelFor(currentHeightTextArea);

		nnResampleButton = new JButton(applyNearestNeighbourResample);
		bilinearResampleButton = new JButton(applyBilinearResample);
	}

	private void addComponents() {

		currentWidthPanel.add(currentWidthLabel);
		currentWidthPanel.add(currentWidthTextArea);

		currentHeightPanel.add(currentHeightLabel);
		currentHeightPanel.add(currentHeightTextArea);

		newWidthPanel.add(newWidthLabel);
		newWidthPanel.add(newWidthSliderWithSpinner);
		newHeightPanel.add(newHeightLabel);
		newHeightPanel.add(newHeightSliderWithSpinner);
		bilinearResamplePanel.add(bilinearResampleButton);

		kxPanel.add(kxLabel);
		kxPanel.add(kxSpinner);
		kyPanel.add(kyLabel);
		kyPanel.add(kySpinner);
		nnResamplePanel.add(nnResampleButton);
	}

	private void updateTextFields(int currentWidth, int currentHeight) {
		if (currentWidth == 0) {
			assert currentHeight == 0;
			currentWidthTextArea.setText("N/A");
			currentHeightTextArea.setText("N/A");
		}
		else {
			currentWidthTextArea.setText(currentWidth + "px");
			currentHeightTextArea.setText(currentHeight + "px");
		}
	}

	public void updateImageSize(int newWidth, int newHeight) {
		if (newWidth == 0) assert newHeight == 0;

		updateTextFields(newWidth, newHeight);
	}
}
