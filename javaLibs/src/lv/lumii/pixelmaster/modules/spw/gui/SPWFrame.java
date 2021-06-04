
package lv.lumii.pixelmaster.modules.spw.gui;

import java.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.text.DecimalFormat;
import java.util.*;
import java.io.*;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.gui.ImageViewer;
import lv.lumii.pixelmaster.modules.spw.domain.graph.*;
import lv.lumii.pixelmaster.modules.spw.domain.SphericalWave;
import lv.lumii.pixelmaster.modules.spw.domain.Optimization;
import lv.lumii.pixelmaster.modules.spw.gui.GraphIoDialog;

/**
 * Frame for visualisation of the wave algorithm.
 * @author Jevgenijs Jonass.
 * @since 29.03.2009
 */
public final class SPWFrame extends JFrame {
	private SPWFrameModel spwFrameModel;

	private ImageViewer iv;
	private JLabel label1, label2, label3, label4, label5, label6;
	private JPanel rightPanel, rightTopPanel, buttonPanel, spinnerPanel;
	private MenuBar menuBar;
	private Menu menuGraph;
	private MenuItem saveasItem;
	private JButton svaButton, primOptButton, cneButton, edgeOptButton,
					cutTailsButton;
	private JSpinner sizeSpinner, coefficientSpinner,
					distanceSpinner, varianceSpinner, lengthSpinner;
	private JComboBox connectivityComboBox;

	public SPWFrame(SPWFrameModel spwFrameModel) {
		super("SPW");
		assert spwFrameModel != null && spwFrameModel.getImage().isBinary();

		this.spwFrameModel = spwFrameModel;

		setSize(600, 400);
		setMinimumSize(new Dimension(400, 250));
		setLocation(400, 300);
		setResizable(true);
		setLayout(new BorderLayout());
		createAndShowGUI();
	}

	private void createAndShowGUI() {
		createPanels();
		createComponents();
		addComponents();
		setVisible(true);
	}

	/**
	 * {@inheritDoc}
	 * @author Jevgenijs Jonass.
	 * @since 04.05.2009
	 */
	protected void createPanels() {
		setLayout(new BorderLayout());
		iv=new ImageViewer(spwFrameModel.getOrigImage());
		rightPanel=new JPanel(new BorderLayout());
		add(iv, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);

		rightTopPanel=new JPanel(new BorderLayout());
		rightTopPanel.setLayout(new BoxLayout(rightTopPanel, BoxLayout.LINE_AXIS));
		rightPanel.add(rightTopPanel, BorderLayout.NORTH);

		buttonPanel=new JPanel(new GridLayout(6, 1, 5, 5));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightTopPanel.add(buttonPanel);

		spinnerPanel=new JPanel(new GridLayout(6, 2, 5, 5));
		spinnerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightTopPanel.add(spinnerPanel, BorderLayout.EAST);
	}

	/**
	 * {@inheritDoc}
	 * @author Jevgenijs Jonass.
	 * @since 04.05.2009
	 */
	protected void createComponents() {
		menuBar = new MenuBar();
		menuGraph = new Menu("Graph");
		saveasItem=new MenuItem("Save as");
		menuGraph.add(saveasItem);
		saveasItem.addActionListener(new ActionListenerImpl());
		menuBar.add(menuGraph);
		this.setMenuBar(menuBar);

		final class ApplySVA extends AbstractAction {
			private ApplySVA() { }
			public void actionPerformed(ActionEvent e) {
				spwFrameModel.applySVA();
				iv.setImage(spwFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "SVA";
				return super.getValue(key);
			}
		}

		final class PrimaryOptimization extends AbstractAction {
			private PrimaryOptimization() { }
			public void actionPerformed(ActionEvent e) {
				spwFrameModel.primaryOptimization();
				iv.setImage(spwFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Primary optimization";
				return super.getValue(key);
			}
		}

		final class ConnectEdges extends AbstractAction {
			private ConnectEdges() { }
			public void actionPerformed(ActionEvent e) {
				spwFrameModel.connectEdges();
				iv.setImage(spwFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Connect edges";
				return super.getValue(key);
			}
		}

		final class EdgeOptimization extends AbstractAction {
			private EdgeOptimization() { }
			public void actionPerformed(ActionEvent e) {
				spwFrameModel.edgeOptimization();
				iv.setImage(spwFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Edge optimization";
				return super.getValue(key);
			}
		}

		final class CutTails extends AbstractAction {
			private CutTails() { }
			public void actionPerformed(ActionEvent e) {
				spwFrameModel.cutTails();
				iv.setImage(spwFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Cut tails";
				return super.getValue(key);
			}
		}

		svaButton=new JButton("SVA");
		svaButton.setAction(new ApplySVA());

		primOptButton=new JButton("Primary opt.");
		primOptButton.setAction(new PrimaryOptimization());

		cneButton=new JButton("Connect edges");
		cneButton.setAction(new ConnectEdges());

		edgeOptButton=new JButton("Edge opt.");
		edgeOptButton.setAction(new EdgeOptimization());

		cutTailsButton=new JButton("Cut tails");
		cutTailsButton.setAction(new CutTails());

		label1=new JLabel("size:");
		label2=new JLabel("coeff:");
		label3=new JLabel("distance:");
		label4=new JLabel("variance:");
		label5=new JLabel("length:");
		label6=new JLabel("connectivity:");

		connectivityComboBox=new JComboBox(spwFrameModel.getConnectivityComboBoxModel());

		sizeSpinner = new JSpinner(spwFrameModel.getSizeSpinnerModel());
		JFormattedTextField ftf = ((JSpinner.DefaultEditor)(sizeSpinner.getEditor())).getTextField();
		ftf.setColumns(3);
		ftf.setHorizontalAlignment(JTextField.RIGHT);
		ftf.setEditable(true);
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

		Comparable min = ((SpinnerNumberModel) sizeSpinner.getModel()).getMinimum();
		Comparable max = ((SpinnerNumberModel) sizeSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);

		coefficientSpinner = new JSpinner(spwFrameModel.getCoefficientSpinnerModel());
		ftf = ((JSpinner.DefaultEditor)(coefficientSpinner.getEditor())).getTextField();
		ftf.setColumns(3);
		ftf.setHorizontalAlignment(JTextField.RIGHT);
		ftf.setEditable(true);
		decimalFormat = new DecimalFormat("0.000");
		decimalFormat.setMinimumFractionDigits(3);
		decimalFormat.setMaximumFractionDigits(3);
		decimalFormat.setMaximumIntegerDigits(5);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setParseBigDecimal(false);
		decimalFormat.setParseIntegerOnly(false);
		textFormatter = new NumberFormatter(decimalFormat);
		textFormatter.setValueClass(Double.class);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDefaultFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDisplayFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setEditFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setNullFormatter(textFormatter);
		textFormatter.setAllowsInvalid(false);

		min = ((SpinnerNumberModel) coefficientSpinner.getModel()).getMinimum();
		max = ((SpinnerNumberModel) coefficientSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);

		distanceSpinner = new JSpinner(spwFrameModel.getDistanceSpinnerModel());
		ftf = ((JSpinner.DefaultEditor)(distanceSpinner.getEditor())).getTextField();
		ftf.setColumns(3);
		ftf.setHorizontalAlignment(JTextField.RIGHT);
		ftf.setEditable(true);
		decimalFormat = new DecimalFormat("000.000");
		decimalFormat.setMinimumFractionDigits(3);
		decimalFormat.setMaximumFractionDigits(3);
		decimalFormat.setMaximumIntegerDigits(5);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setParseBigDecimal(false);
		decimalFormat.setParseIntegerOnly(false);
		textFormatter = new NumberFormatter(decimalFormat);
		textFormatter.setValueClass(Double.class);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDefaultFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDisplayFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setEditFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setNullFormatter(textFormatter);
		textFormatter.setAllowsInvalid(false);

		min = ((SpinnerNumberModel) distanceSpinner.getModel()).getMinimum();
		max = ((SpinnerNumberModel) distanceSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);

		varianceSpinner = new JSpinner(spwFrameModel.getVarianceSpinnerModel());
		ftf = ((JSpinner.DefaultEditor)(varianceSpinner.getEditor())).getTextField();
		ftf.setColumns(3);
		ftf.setHorizontalAlignment(JTextField.RIGHT);
		ftf.setEditable(true);
		decimalFormat = new DecimalFormat("000.000");
		decimalFormat.setMinimumFractionDigits(3);
		decimalFormat.setMaximumFractionDigits(3);
		decimalFormat.setMaximumIntegerDigits(5);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setParseBigDecimal(false);
		decimalFormat.setParseIntegerOnly(false);
		textFormatter = new NumberFormatter(decimalFormat);
		textFormatter.setValueClass(Double.class);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDefaultFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDisplayFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setEditFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setNullFormatter(textFormatter);
		textFormatter.setAllowsInvalid(false);

		min = ((SpinnerNumberModel) varianceSpinner.getModel()).getMinimum();
		max = ((SpinnerNumberModel) varianceSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);

		lengthSpinner = new JSpinner(spwFrameModel.getLengthSpinnerModel());
		ftf = ((JSpinner.DefaultEditor)(lengthSpinner.getEditor())).getTextField();
		ftf.setColumns(3);
		ftf.setHorizontalAlignment(JTextField.RIGHT);
		ftf.setEditable(true);
		decimalFormat = new DecimalFormat("000.000");
		decimalFormat.setMinimumFractionDigits(3);
		decimalFormat.setMaximumFractionDigits(3);
		decimalFormat.setMaximumIntegerDigits(5);
		decimalFormat.setMinimumIntegerDigits(1);
		decimalFormat.setParseBigDecimal(false);
		decimalFormat.setParseIntegerOnly(false);
		textFormatter = new NumberFormatter(decimalFormat);
		textFormatter.setValueClass(Double.class);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDefaultFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setDisplayFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setEditFormatter(textFormatter);
		((DefaultFormatterFactory)(ftf.getFormatterFactory())).setNullFormatter(textFormatter);
		textFormatter.setAllowsInvalid(false);

		min = ((SpinnerNumberModel) lengthSpinner.getModel()).getMinimum();
		max = ((SpinnerNumberModel) lengthSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);

		label1.setLabelFor(sizeSpinner);
		label2.setLabelFor(coefficientSpinner);
		label3.setLabelFor(distanceSpinner);
		label4.setLabelFor(varianceSpinner);
		label5.setLabelFor(lengthSpinner);
		label6.setLabelFor(connectivityComboBox);
	}

	/**
	 * {@inheritDoc}
	 * @author Jevgenijs Jonass.
	 * @since 04.05.2009
	 */
	protected void addComponents() {
		buttonPanel.add(svaButton);
		buttonPanel.add(primOptButton);
		buttonPanel.add(cneButton);
		buttonPanel.add(edgeOptButton);
		buttonPanel.add(cutTailsButton);
		spinnerPanel.add(label1);
		spinnerPanel.add(sizeSpinner);
		spinnerPanel.add(label2);
		spinnerPanel.add(coefficientSpinner);
		spinnerPanel.add(label3);
		spinnerPanel.add(distanceSpinner);
		spinnerPanel.add(label4);
		spinnerPanel.add(varianceSpinner);
		spinnerPanel.add(label5);
		spinnerPanel.add(lengthSpinner);
		spinnerPanel.add(label6);
		spinnerPanel.add(connectivityComboBox);
	}

	/**
	 * Class to listen button events.
	 * @author Jevgenijs Jonass.
	 * @since 29.03.2009
	 */
	private final class ActionListenerImpl implements ActionListener {
		private ActionListenerImpl() { }
		public void actionPerformed(ActionEvent e) {
			assert !(e==null || e.getActionCommand()==null);
			if (e.getActionCommand().equalsIgnoreCase("Save as")) {
				GraphIoDialog.saveGraph(null, spwFrameModel.getGraph());
			}
			else assert false;
		}
	}
}
