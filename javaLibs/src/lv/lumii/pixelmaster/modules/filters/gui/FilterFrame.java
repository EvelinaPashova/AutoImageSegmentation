
package lv.lumii.pixelmaster.modules.filters.gui;

import lv.lumii.pixelmaster.core.api.gui.ImageViewer;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import lv.lumii.pixelmaster.core.api.gui.RadioButtonPanel;

/**
 * Median filter frame
 * @author Jevgenijs Jonass
 */
public final class FilterFrame extends JFrame {

	private FilterFrameModel filterFrameModel;

	private Action okAction;

	private ImageViewer iv;
	private JPanel bottomPanel, rightPanel, rightTopPanel, settingsPanel, filterPanel;
	private JButton okButton, origButton, filterButton;
	private JSpinner radiusSpinner, thresholdSpinner;
	private JLabel radiusLabel, thresholdLabel;
	private RadioButtonPanel radioButtonPanel;

	public FilterFrame(FilterFrameModel filterFrameModel, Action okAction) {
		super("Filtering");
		assert filterFrameModel != null;

		this.filterFrameModel = filterFrameModel;
		this.okAction = okAction;

		setVisible(false);
		setSize(500, 400);
		setMinimumSize(new Dimension(300, 300));
		setLocation(600, 100);
		setResizable(true);

		createAndShowGUI();
	}

	private void createAndShowGUI() {
		createPanels();
		createComponents();
		addComponents();
		setVisible(true);
	}

	protected void createPanels() {
		setLayout(new BorderLayout());

		iv=new ImageViewer(filterFrameModel.getImage(), filterFrameModel.getImageViewerModel());

		bottomPanel=new JPanel();
		rightPanel=new JPanel(new BorderLayout());
		add(iv, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		add(rightPanel, BorderLayout.EAST);
		
		rightTopPanel=new JPanel();
		rightTopPanel.setLayout(new BoxLayout(rightTopPanel, BoxLayout.PAGE_AXIS));
		rightTopPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		rightPanel.add(rightTopPanel, BorderLayout.NORTH);

		settingsPanel=new JPanel();
		settingsPanel.setLayout(new GridLayout(2, 2, 5, 5));
		settingsPanel.setBorder(BorderFactory.createTitledBorder("settings"));
		rightTopPanel.add(settingsPanel);

		filterPanel=new JPanel();
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.PAGE_AXIS));
		filterPanel.setBorder(BorderFactory.createTitledBorder("filter"));
		rightTopPanel.add(filterPanel);

		radioButtonPanel = new RadioButtonPanel(filterFrameModel.getRadioButtonPanelModel(),
				"Median filter",
				"Median Bidirectional",
				"Mean filter",
				"Variance filter"
		);
		filterPanel.add(radioButtonPanel);
	}

	private void createComponents() {

		okButton=new JButton();
		origButton=new JButton();
		filterButton=new JButton();

		filterButton.setAlignmentX(CENTER_ALIGNMENT);

		final class ApplyAction extends AbstractAction {
			private ApplyAction() { }
			public void actionPerformed(ActionEvent e) {
//				filterFrameModel.setAlgorithm(getAlgorithm());
				filterFrameModel.applyFilter();
				iv.setImage(filterFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Apply";
				return super.getValue(key);
			}
		}

		final class OrigAction extends AbstractAction {
			private OrigAction() { }
			public void actionPerformed(ActionEvent e) {
				filterFrameModel.loadOriginalImage();
				iv.setImage(filterFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Original image";
				return super.getValue(key);
			}
		}

		filterButton.setAction(new ApplyAction());
		origButton.setAction(new OrigAction());
		okButton.setAction(okAction);

		radiusLabel = new JLabel("radius:");
		thresholdLabel = new JLabel("threshold:");

		radiusSpinner = new JSpinner(filterFrameModel.getRadiusSpinnerModel());
		thresholdSpinner = new JSpinner(filterFrameModel.getThresholdSpinnerModel());

		JFormattedTextField ftf = ((JSpinner.DefaultEditor) (radiusSpinner.getEditor())).getTextField();
		ftf.setColumns(3);
		ftf.setHorizontalAlignment(JTextField.RIGHT);
		DecimalFormat decimalFormat = new DecimalFormat("##0");
		decimalFormat.setMaximumIntegerDigits(3);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setParseBigDecimal(false);
		decimalFormat.setParseIntegerOnly(true);
		NumberFormatter textFormatter = new NumberFormatter(decimalFormat);
		textFormatter.setValueClass(Integer.class);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDefaultFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDisplayFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setEditFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setNullFormatter(textFormatter);
		textFormatter.setAllowsInvalid(false);

		Comparable min = ((SpinnerNumberModel) radiusSpinner.getModel()).getMinimum();
		Comparable max = ((SpinnerNumberModel) radiusSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);

		ftf = ((JSpinner.DefaultEditor) (thresholdSpinner.getEditor())).getTextField();
		ftf.setColumns(3);
		ftf.setHorizontalAlignment(JTextField.RIGHT);
		decimalFormat = new DecimalFormat("##0");
		decimalFormat.setMaximumIntegerDigits(3);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setParseBigDecimal(false);
		decimalFormat.setParseIntegerOnly(true);
		textFormatter = new NumberFormatter(decimalFormat);
		textFormatter.setValueClass(Integer.class);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDefaultFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDisplayFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setEditFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setNullFormatter(textFormatter);
		textFormatter.setAllowsInvalid(false);

		min = ((SpinnerNumberModel) thresholdSpinner.getModel()).getMinimum();
		max = ((SpinnerNumberModel) thresholdSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);
	}

	protected void addComponents() {
		bottomPanel.add(okButton);
		bottomPanel.add(origButton);

		settingsPanel.add(radiusLabel);
		settingsPanel.add(radiusSpinner);
		settingsPanel.add(thresholdLabel);
		settingsPanel.add(thresholdSpinner);

		filterPanel.add(filterButton);
	}
}
