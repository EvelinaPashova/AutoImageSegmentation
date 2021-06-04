/**
 * Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 1, 2010 3:08:26 PM
 *
 */

package lv.lumii.pixelmaster.modules.grapheditor.gui.menulisteners;

import lv.lumii.pixelmaster.modules.grapheditor.gui.GraphEditorDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class HelpListener extends MyListener implements ActionListener
{
    public HelpListener( GraphEditorDialog graphEditorDialog)
    {
        super( graphEditorDialog );
    }


    
    public void actionPerformed( ActionEvent event )
    {
        Object source = event.getSource();

        if ( source instanceof JMenuItem)
        {
            JMenuItem item = (JMenuItem)source;

            if ( item.getText().equals( "About..." ) )
            {
                JOptionPane.showMessageDialog( super.graphEditorDialog,
                                               "This Graph Editor was initially created by Andrey Zhmakin\n"
                                                    + "\n"
                                                    + "Other Contributors:\n" // Fellow coders add your names below!
                                                    + "   (List still void)\n"
                                                    + "\n\n\n"
                                                    + "The editor utilizes some AtteluApstrade components"
                                                    + "\n\n"
                                                    + "Created in 2010 by Andrey Zhmakin",
                                               "About...",
                                               JOptionPane.INFORMATION_MESSAGE );
            }
        }
    }
}




