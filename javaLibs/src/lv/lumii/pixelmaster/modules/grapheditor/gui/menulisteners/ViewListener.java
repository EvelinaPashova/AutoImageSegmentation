/**
 * TODO: Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 3, 2010 8:18:14 PM
 *
 */

package lv.lumii.pixelmaster.modules.grapheditor.gui.menulisteners;

import lv.lumii.pixelmaster.modules.grapheditor.gui.GraphEditorDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ViewListener extends MyListener implements ActionListener
{
    public ViewListener( GraphEditorDialog graphEditorDialog)
    {
        super( graphEditorDialog );
    }



    public void actionPerformed( ActionEvent event )
    {
        Object source = event.getSource();

        if ( source instanceof JMenuItem)
        {
            JMenuItem item = (JMenuItem)source;

            if ( item.getText().equals( "Bullets on Vertices" ) )
            {
                this.graphEditorDialog.workPanel.setBulletsVisible( !this.graphEditorDialog.workPanel.areBulletsVisible() );
            }
            else if ( item.getText().equals( "Axes X and Y" ) )
            {
                this.graphEditorDialog.workPanel.setAxesVisibility( !this.graphEditorDialog.workPanel.getAxesVisibility() );
            }
            else if ( item.getText().equals( "Center of Mass" ) )
            {
                this.graphEditorDialog.workPanel.setCenterOfMassVisibility( !this.graphEditorDialog.workPanel.getCenterOfMassVisibility() );
            }
        }
    }

}




