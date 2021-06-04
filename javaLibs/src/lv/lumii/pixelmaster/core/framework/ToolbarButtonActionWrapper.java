
package lv.lumii.pixelmaster.core.framework;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import lv.lumii.pixelmaster.core.api.framework.IToolbarButtonAction;

/**
 * @author 
 */
final class ToolbarButtonActionWrapper extends AbstractAction {

	private IToolbarButtonAction wrappedAction;

	ToolbarButtonActionWrapper(IToolbarButtonAction action) {
		this.wrappedAction = action;
		super.setEnabled(action.isEnabled());
		super.putValue(LARGE_ICON_KEY, wrappedAction.getLargeIcon());
	}

	public boolean isEnabled() {
		return wrappedAction.isEnabled();
	}

	public void actionPerformed(ActionEvent e) {
		wrappedAction.actionPerformed();
	}
}
