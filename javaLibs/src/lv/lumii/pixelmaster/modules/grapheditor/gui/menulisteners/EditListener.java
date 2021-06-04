/**
 * Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 1, 2010 3:08:15 PM
 *
 */

package lv.lumii.pixelmaster.modules.grapheditor.gui.menulisteners;

import lv.lumii.pixelmaster.modules.grapheditor.gui.GraphEditorDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class EditListener extends MyListener implements ActionListener
{
    public EditListener( GraphEditorDialog graphEditorDialog)
    {
        super( graphEditorDialog );
    }

    

    public void actionPerformed( ActionEvent event )
    {
        Object source = event.getSource();

        if ( source instanceof JMenuItem)
        {
            JMenuItem item = (JMenuItem)source;

            if ( item.getText().equals( "Add vertex" ) )
            {
                this.graphEditorDialog.workPanel.createVertex();
            }
            else if ( item.getText().equals( "Remove vertex" ) )
            {
                this.graphEditorDialog.workPanel.removeVertex();
            }
            else if ( item.getText().equals( "Add edge" ) )
            {
                this.graphEditorDialog.workPanel.createEdge();
            }
            else if ( item.getText().equals( "Remove edge" ) )
            {
                this.graphEditorDialog.workPanel.removeEdge();
            }
            else if ( item.getText().equals( "Normal mode" ) )
            {
                this.graphEditorDialog.workPanel.setMode( lv.lumii.pixelmaster.modules.grapheditor.gui.WorkPanel.MODE_NORMAL );
            }
            else if ( item.getText().equals( "Auto connection mode" ) )
            {
                this.graphEditorDialog.workPanel.setMode( lv.lumii.pixelmaster.modules.grapheditor.gui.WorkPanel.MODE_AUTO_CONNECT );
            }
            else if ( item.getText().equals( "Drawing mode" ) )
            {
                this.graphEditorDialog.workPanel.setMode( lv.lumii.pixelmaster.modules.grapheditor.gui.WorkPanel.MODE_DRAWING );
            }
        }
    }
}




