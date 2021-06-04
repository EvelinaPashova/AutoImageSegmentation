
package lv.lumii.pixelmaster.core.api.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * This class represents a panel with radio buttons. At any time moment, exactly one
 * of them is selected.
 *
 * @author Jevgeny Jonas
 */
public class RadioButtonPanel extends JPanel {

	private RadioButtonPanelModel radioButtonPanelModel;
	private ButtonGroup buttonGroup;
	private ActionListenerImpl actionListener = new ActionListenerImpl();

	/**
	 * <p>Creates a new radio button panel which uses the supplied model.
	 * The number of radio buttons that the panel will contain is equal
	 * to <code>radioButtonPanel.getNumberOfButtons()</code>.</p>
	 *
	 * <p>Precondition: <code>labels.length == radioButtonPanel.getNumberOfButtons()</code>.</p>
	 *
	 * @param labels labels near the radio buttons
	 */
	public RadioButtonPanel(RadioButtonPanelModel radioButtonPanelModel, String... labels) {

		super(new GridLayout(0, 1));
		this.radioButtonPanelModel = radioButtonPanelModel;

		assert radioButtonPanelModel.getSelectedIndex() >= 0 && radioButtonPanelModel.getSelectedIndex() < labels.length;

		buttonGroup = new ButtonGroup();

		for (int i = 0; i < labels.length; ++i) {
			JRadioButton radioButton = new JRadioButton(labels[i], radioButtonPanelModel.getSelectedIndex() == i);
			radioButton.addActionListener(actionListener);
			buttonGroup.add(radioButton);
			add(radioButton);
		}
	}

	private final class ActionListenerImpl implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// updating the index of the selected button
			int i = 0;
			Enumeration buttons = buttonGroup.getElements();
			while (buttons.hasMoreElements()) {
				JRadioButton radioButton = (JRadioButton) buttons.nextElement();
				if (radioButton.isSelected()) {
					radioButtonPanelModel.setSelectedIndex(i);
				}
				++i;
			}

			assert radioButtonPanelModel.getNumberOfButtons() == i;
		}
	}
}
