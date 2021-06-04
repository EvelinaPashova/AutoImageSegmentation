
package lv.lumii.pixelmaster.modules.transform.gui;

import lv.lumii.pixelmaster.core.api.gui.ImageViewer;
import lv.lumii.pixelmaster.core.api.gui.RadioButtonPanel;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * Rotation frame
 * @author Jevgenijs Jonass
 */
public final class RotationFrame extends JFrame {

	private RotationFrameModel rotationFrameModel;

	private Action okAction;

	private ImageViewer iv;
	private JPanel bottomPanel, rightPanel, rightTopPanel, settingsPanel, rotationPanel, radioPanel;
	private JButton okButton, origButton, rotateButton;
	private JSpinner angleSpinner;
	private JLabel angleLabel;

	public RotationFrame(RotationFrameModel rotationFrameModel, Action okAction) {
		super("Rotation");
		assert rotationFrameModel != null;

		this.rotationFrameModel = rotationFrameModel;
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

		iv=new ImageViewer(rotationFrameModel.getImage(), rotationFrameModel.getImageViewerModel());

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
		settingsPanel.setLayout(new GridLayout(1, 2, 5, 5));
		settingsPanel.setBorder(BorderFactory.createTitledBorder("settings"));
		rightTopPanel.add(settingsPanel);

		rotationPanel=new JPanel();
		rotationPanel.setLayout(new GridLayout(1, 1, 5, 5));
		rotationPanel.setBorder(BorderFactory.createTitledBorder("rotate by shear"));
		rightTopPanel.add(rotationPanel);
	}

	private void createComponents() {

		okButton=new JButton();
		origButton=new JButton();
		rotateButton=new JButton();

		rotateButton.setAlignmentX(CENTER_ALIGNMENT);

		final class ApplyAction extends AbstractAction {
			private ApplyAction() { }
			public void actionPerformed(ActionEvent e) {
				rotationFrameModel.rotate();
				iv.setImage(rotationFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Apply";
				return super.getValue(key);
			}
		}

		final class OrigAction extends AbstractAction {
			private OrigAction() { }
			public void actionPerformed(ActionEvent e) {
				rotationFrameModel.loadOriginalImage();
				iv.setImage(rotationFrameModel.getImage());
			}
			public Object getValue(String key) {
				if (key.equals(NAME)) return "Original image";
				return super.getValue(key);
			}
		}

		rotateButton.setAction(new ApplyAction());
		origButton.setAction(new OrigAction());
		okButton.setAction(okAction);

		angleLabel = new JLabel("angle:");
		angleSpinner = new JSpinner(rotationFrameModel.getAngle());

		JFormattedTextField ftf = ((JSpinner.DefaultEditor) (angleSpinner.getEditor())).getTextField();
		ftf.setColumns(3);
		ftf.setHorizontalAlignment(JTextField.RIGHT);
		DecimalFormat decimalFormat = new DecimalFormat("#0");
		decimalFormat.setMaximumIntegerDigits(2);
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

		Comparable min = ((SpinnerNumberModel) angleSpinner.getModel()).getMinimum();
		Comparable max = ((SpinnerNumberModel) angleSpinner.getModel()).getMaximum();
		textFormatter.setMinimum(min);
		textFormatter.setMaximum(max);
	}

	protected void addComponents() {
		bottomPanel.add(okButton);
		bottomPanel.add(origButton);

		settingsPanel.add(angleLabel);
		settingsPanel.add(angleSpinner);
		
		rotationPanel.add(rotateButton);
	}
}
