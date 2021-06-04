
package lv.lumii.pixelmaster.core.api.gui;

/**
 * This class represents a model for the {@link RadioButtonPanel}.
 * The model's state is defined by the number of buttons and the currently selected radio button.
 *
 * @author Jevgeny Jonas
 */
public class RadioButtonPanelModel {

	private int n, selectedIndex;

	/**
	 * Constructs a new model of <code>n</code> radio buttons.
	 *
	 * @param n the number of radio buttons, <code>n &gt; 0</code>
	 * @param selectedIndex the index of the selected radio button, <code>n &gt; selectedIndex &gt;= 0</code>
	 */
	public RadioButtonPanelModel(int n, int selectedIndex) {
		this.n = n;
		this.selectedIndex = selectedIndex;
		assert invariant();
	}

	/**
	 * Returns the number 
	 */
	public int getNumberOfButtons() {
		return n;
	}

	/**
	 * Returns the index of the curretly selected radio button.
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		assert invariant();
	}

	private boolean invariant() {
		assert n > selectedIndex && selectedIndex >= 0;
		return true;
	}
}
