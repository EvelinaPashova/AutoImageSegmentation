
package lv.lumii.pixelmaster.modules.binarization.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;
import lv.lumii.pixelmaster.core.api.domain.RGB;

import lv.lumii.pixelmaster.core.api.gui.*;
import lv.lumii.pixelmaster.modules.binarization.domain.Binarization;

/**
 * Binarization frame.
 * @author Jevgenijs Jonass.
 * @since 19.03.2009
 */
public final class BinarizationFrame extends JFrame {

	private static final class RGBPanel extends JPanel {
		private final BinarizationFrameModel.RGBPanelModel model;
		private JButton defaultButton;

		private SliderWithSpinner r, g, b;
		private JPanel panel = new JPanel(new GridLayout(3, 2));

		// listeners of the three sliders with spinners
		ChangeListener[] listeners = new ChangeListener[3];		// 0 - r; 1 - g; 2 - b

		private RGBPanel(final BinarizationFrameModel.RGBPanelModel model) {
			this.model = model;
			r = new SliderWithSpinner(model.getRedSliderWithSpinnerModel(), SwingConstants.HORIZONTAL, false);
			g = new SliderWithSpinner(model.getGreenSliderWithSpinnerModel(), SwingConstants.HORIZONTAL, false);
			b = new SliderWithSpinner(model.getBlueSliderWithSpinnerModel(), SwingConstants.HORIZONTAL, true);

			listeners[0] = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int green = 100 - model.getRed() - model.getBlue();
					if (green < 0) {
						green = 0;
						b.removeChangeListener(listeners[2]);
						b.setValue(100 - model.getRed());
						b.addChangeListener(listeners[2]);
					}
					g.removeChangeListener(listeners[1]);
					g.setValue(green);
					g.addChangeListener(listeners[1]);
				}
			};

			listeners[1] = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int blue = 100 - model.getRed() - model.getGreen();
					if (blue < 0) {
						blue = 0;
						r.removeChangeListener(listeners[0]);
						r.setValue(100 - model.getGreen());
						r.addChangeListener(listeners[0]);
					}
					b.removeChangeListener(listeners[2]);
					b.setValue(blue);
					b.addChangeListener(listeners[2]);
				}
			};

			listeners[2] = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int green = 100 - model.getRed() - model.getBlue();
					if (green < 0) {
						green = 0;
						r.removeChangeListener(listeners[0]);
						r.setValue(100 - model.getBlue());
						r.addChangeListener(listeners[0]);
					}
					g.removeChangeListener(listeners[1]);
					g.setValue(green);
					g.addChangeListener(listeners[1]);
				}
			};

			r.addChangeListener(listeners[0]);
			g.addChangeListener(listeners[1]);
			b.addChangeListener(listeners[2]);

			panel.add(new JLabel("red:"));
			panel.add(r);
			panel.add(new JLabel("green:"));
			panel.add(g);
			panel.add(new JLabel("blue:"));
			panel.add(b);

			final class DefaultRGBCoefficients extends AbstractAction {
				private DefaultRGBCoefficients() { }
				public void actionPerformed(ActionEvent e) {
					setDefaults();
				}
				public Object getValue(String key) {
					if (key.equals(NAME)) return "Default";
					return super.getValue(key);
				}
			}

			defaultButton = new JButton();
			defaultButton.setAction(new DefaultRGBCoefficients());

			add(defaultButton);
			add(panel);
		}

		private void setDefaults() {
			r.removeChangeListener(listeners[0]);
			g.removeChangeListener(listeners[1]);
			b.removeChangeListener(listeners[2]);

			r.setValue(RGB.DEFAULT_RED);
			g.setValue(RGB.DEFAULT_GREEN);
			b.setValue(RGB.DEFAULT_BLUE);

			r.addChangeListener(listeners[0]);
			g.addChangeListener(listeners[1]);
			b.addChangeListener(listeners[2]);
		}
	}

	private BinarizationFrameModel binarizationFrameModel;

	private Action copyAction;

	private JPanel rightPanel, bernsenTopPanel, niblackTopPanel, buttonPanel,
			globalThrPanel, bernsenPanel, niblackPanel,
			globalBinarizationPanel, globalGrayscalePanel;

	private JTabbedPane tabbedPane;
	private ImageViewer iv;
	private JLabel niblackRadiusLabel, niblackCoeffLabel, bernsenRadiusLabel, bernsenThresholdLabel;

	private JSpinner bernsenRadiusSpinner, bernsenThrSpinner,
			niblackRadiusSpinner, niblackCoeffSpinner;
	private JButton copyButton, otsuButton, niblackButton, bernsenButton,
			globalBinarizationButton, globalGrayscaleButton, origButton;

	private SliderWithSpinner globalThresholdSliderWithSpinner;
	private RGBPanel rgbPanel;

	public BinarizationFrame(BinarizationFrameModel binarizationFrameModel, Action copyAction) {
		super("Binarization");
		assert binarizationFrameModel != null && copyAction != null;

		this.copyAction = copyAction;
		this.binarizationFrameModel = binarizationFrameModel;

		setVisible(false);
		setSize(700, 600);
		setMinimumSize(new Dimension(500, 450));
		setLocation(400, 100);
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

		iv=new ImageViewer(binarizationFrameModel.getImage());
		add(iv, BorderLayout.CENTER);

		rightPanel=new JPanel(new BorderLayout());
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		add(rightPanel, BorderLayout.EAST);

		rgbPanel = new RGBPanel(binarizationFrameModel.getRGBPanelModel());

		rightPanel.add(rgbPanel, BorderLayout.NORTH);

		globalThrPanel=new JPanel();
		globalThrPanel.setLayout(new BoxLayout(globalThrPanel, BoxLayout.PAGE_AXIS));
		globalThrPanel.setBorder(BorderFactory.createTitledBorder("threshold"));
		globalThrPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		bernsenPanel=new JPanel(new BorderLayout());
		niblackPanel = new JPanel(new BorderLayout());
		globalBinarizationPanel = new JPanel();
		globalGrayscalePanel = new JPanel();

		bernsenTopPanel = new JPanel(new GridLayout(3, 2, 5, 5));
		niblackTopPanel=new JPanel(new GridLayout(3, 2, 5, 5));

		buttonPanel=new JPanel();
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		add(buttonPanel, BorderLayout.SOUTH);

		globalBinarizationPanel.add(globalThrPanel);

		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Bernsen", bernsenPanel);
		tabbedPane.addTab("Niblack", niblackPanel);
		tabbedPane.addTab("Global binarization", globalBinarizationPanel);
		tabbedPane.addTab("Global grayscale", globalGrayscalePanel);
		rightPanel.add(tabbedPane, BorderLayout.SOUTH);
	}

	protected void createComponents() {

		niblackButton=new JButton();
		bernsenButton=new JButton();
		globalBinarizationButton = new JButton();
		globalGrayscaleButton = new JButton();
		otsuButton=new JButton();
		origButton = new JButton();
		copyButton=new JButton();

		final class ApplyNiblackAlgorithm extends AbstractAction {
			private ApplyNiblackAlgorithm() { }
			public void actionPerformed(ActionEvent e) {
				binarizationFrameModel.applyNiblackAlgorithm();
				iv.setImage(binarizationFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Apply";
				return super.getValue(key);
			}
		}

		final class ApplyBernsenAlgorithm extends AbstractAction {
			private ApplyBernsenAlgorithm() { }
			public void actionPerformed(ActionEvent e) {
				binarizationFrameModel.applyBernsenAlgorithm();
				iv.setImage(binarizationFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Apply";
				return super.getValue(key);
			}
		}

		final class ApplyGlobalBinarization extends AbstractAction {
			private ApplyGlobalBinarization() { }
			public void actionPerformed(ActionEvent e) {
				binarizationFrameModel.applyGlobalBinarization();
				iv.setImage(binarizationFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Apply";
				return super.getValue(key);
			}
		}

		final class ConvertToGrayscale extends AbstractAction {
			private ConvertToGrayscale() { }
			public void actionPerformed(ActionEvent e) {
				binarizationFrameModel.convertToGrayscale();
				iv.setImage(binarizationFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Apply";
				return super.getValue(key);
			}
		}

		final class Otsu extends AbstractAction {
			private Otsu() { }
			public void actionPerformed(ActionEvent e) {
				globalThresholdSliderWithSpinner.setValue(
						Binarization.OtsuThresholding(binarizationFrameModel.getImage(),
						binarizationFrameModel.getRGBPanelModel().getRed(),
						binarizationFrameModel.getRGBPanelModel().getGreen(),
						binarizationFrameModel.getRGBPanelModel().getBlue())
				);
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Otsu";
				return super.getValue(key);
			}
		}

		final class OrigAction extends AbstractAction {
			private OrigAction() { }
			public void actionPerformed(ActionEvent e) {
				binarizationFrameModel.loadOriginalImage();
				iv.setImage(binarizationFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Original image";
				return super.getValue(key);
			}
		}


		niblackButton.setAction(new ApplyNiblackAlgorithm());
		bernsenButton.setAction(new ApplyBernsenAlgorithm());
		globalBinarizationButton.setAction(new ApplyGlobalBinarization());
		globalGrayscaleButton.setAction(new ConvertToGrayscale());
		otsuButton.setAction(new Otsu());
		origButton.setAction(new OrigAction());
		copyButton.setAction(copyAction);

		niblackRadiusLabel = new JLabel("radius:");
		niblackCoeffLabel = new JLabel("coeff:");
		bernsenRadiusLabel = new JLabel("radius:");
		bernsenThresholdLabel = new JLabel("threshold:");

		globalThresholdSliderWithSpinner = new SliderWithSpinner(
				binarizationFrameModel.getGlobalThresholdSliderWithSpinnerModel(),
				SwingConstants.VERTICAL,
				true
		);

		JFormattedTextField hor_ftf;
		DecimalFormat decimalFormat = new DecimalFormat("##0");
		NumberFormatter textFormatter = new NumberFormatter(decimalFormat);

		bernsenRadiusSpinner = new JSpinner(binarizationFrameModel.getBernsenRadiusSpinnerModel());
		bernsenRadiusLabel.setLabelFor(bernsenRadiusSpinner);
		hor_ftf = ((JSpinner.DefaultEditor)(bernsenRadiusSpinner.getEditor())).getTextField();
		hor_ftf.setColumns(3);
		hor_ftf.setHorizontalAlignment(JTextField.RIGHT);
		decimalFormat = new DecimalFormat("##0");
		decimalFormat.setMaximumIntegerDigits(3);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setParseBigDecimal(false);
		decimalFormat.setParseIntegerOnly(true);
		textFormatter = new NumberFormatter(decimalFormat);
		textFormatter.setValueClass(Integer.class);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setDefaultFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setDisplayFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setEditFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setNullFormatter(textFormatter);
		textFormatter.setAllowsInvalid(false);

		Comparable min = ((SpinnerNumberModel) bernsenRadiusSpinner.getModel()).getMinimum();
		Comparable max = ((SpinnerNumberModel) bernsenRadiusSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);

		bernsenThrSpinner = new JSpinner(binarizationFrameModel.getBernsenThresholdSpinnerModel());
		bernsenThresholdLabel.setLabelFor(bernsenThrSpinner);
		hor_ftf = ((JSpinner.DefaultEditor)(bernsenThrSpinner.getEditor())).getTextField();
		hor_ftf.setColumns(3);
		hor_ftf.setHorizontalAlignment(JTextField.RIGHT);
		decimalFormat = new DecimalFormat("##0");
		decimalFormat.setMaximumIntegerDigits(3);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setParseBigDecimal(false);
		decimalFormat.setParseIntegerOnly(true);
		textFormatter = new NumberFormatter(decimalFormat);
		textFormatter.setValueClass(Integer.class);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setDefaultFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setDisplayFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setEditFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setNullFormatter(textFormatter);
		textFormatter.setAllowsInvalid(false);

		min = ((SpinnerNumberModel) bernsenThrSpinner.getModel()).getMinimum();
		max = ((SpinnerNumberModel) bernsenThrSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);

		niblackRadiusSpinner = new JSpinner(binarizationFrameModel.getNiblackRadiusSpinnerModel());
		niblackRadiusLabel.setLabelFor(niblackRadiusSpinner);
		hor_ftf = ((JSpinner.DefaultEditor)(niblackRadiusSpinner.getEditor())).getTextField();
		hor_ftf.setColumns(3);
		hor_ftf.setHorizontalAlignment(JTextField.RIGHT);
		decimalFormat = new DecimalFormat("##0");
		decimalFormat.setMaximumIntegerDigits(3);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setParseBigDecimal(false);
		decimalFormat.setParseIntegerOnly(true);
		textFormatter = new NumberFormatter(decimalFormat);
		textFormatter.setValueClass(Integer.class);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setDefaultFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setDisplayFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setEditFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setNullFormatter(textFormatter);
		textFormatter.setAllowsInvalid(false);

		min = ((SpinnerNumberModel) niblackRadiusSpinner.getModel()).getMinimum();
		max = ((SpinnerNumberModel) niblackRadiusSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);

		niblackCoeffSpinner = new JSpinner(binarizationFrameModel.getNiblackCoefficientSpinnerModel());
		niblackCoeffLabel.setLabelFor(niblackCoeffSpinner);
		hor_ftf = ((JSpinner.DefaultEditor)(niblackCoeffSpinner.getEditor())).getTextField();
		hor_ftf.setColumns(5);
		hor_ftf.setHorizontalAlignment(JTextField.RIGHT);
		decimalFormat = new DecimalFormat("##0.00");
		decimalFormat.setMinimumFractionDigits(2);
		decimalFormat.setMaximumFractionDigits(2);
		decimalFormat.setMaximumIntegerDigits(3);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setParseBigDecimal(false);
		decimalFormat.setParseIntegerOnly(false);
		textFormatter = new NumberFormatter(decimalFormat);
		textFormatter.setValueClass(Double.class);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setDefaultFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setDisplayFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setEditFormatter(textFormatter);
		((DefaultFormatterFactory)(hor_ftf.getFormatterFactory())).setNullFormatter(textFormatter);
		textFormatter.setAllowsInvalid(false);

		min = ((SpinnerNumberModel) niblackCoeffSpinner.getModel()).getMinimum();
		max = ((SpinnerNumberModel) niblackCoeffSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);
	}

	protected void addComponents() {

		globalThrPanel.add(globalThresholdSliderWithSpinner);
		globalThrPanel.add(otsuButton);

		buttonPanel.add(copyButton);
		buttonPanel.add(origButton);

		bernsenPanel.add(bernsenTopPanel, BorderLayout.NORTH);
		niblackPanel.add(niblackTopPanel, BorderLayout.NORTH);

		bernsenTopPanel.add(bernsenRadiusLabel, BorderLayout.SOUTH);
		bernsenTopPanel.add(bernsenRadiusSpinner);
		bernsenTopPanel.add(bernsenThresholdLabel);
		bernsenTopPanel.add(bernsenThrSpinner);
		bernsenTopPanel.add(bernsenButton);

		niblackTopPanel.add(niblackRadiusLabel);
		niblackTopPanel.add(niblackRadiusSpinner);
		niblackTopPanel.add(niblackCoeffLabel);
		niblackTopPanel.add(niblackCoeffSpinner);
		niblackTopPanel.add(niblackButton);

		globalBinarizationPanel.add(globalBinarizationButton);
		globalGrayscalePanel.add(globalGrayscaleButton);
	}
}
