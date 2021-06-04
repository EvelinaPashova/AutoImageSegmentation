/**
 * TODO: Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 1, 2010 6:44:19 PM
 *
 */

package lv.lumii.pixelmaster.modules.grapheditor.gui.interfacelisteners;

import lv.lumii.pixelmaster.modules.grapheditor.gui.GraphEditorDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class ComboScaleListener implements ActionListener
{
    public ComboScaleListener( GraphEditorDialog graphEditorDialog)
    {
        this.graphEditorDialog = graphEditorDialog;
    }



    public void actionPerformed( ActionEvent event )
    {
        this.graphEditorDialog.workPanel.setScale( new Double( ((String)this.graphEditorDialog.comboScale.getSelectedItem()).substring( 8 ) ) );
    }


    
    private GraphEditorDialog graphEditorDialog;
}




