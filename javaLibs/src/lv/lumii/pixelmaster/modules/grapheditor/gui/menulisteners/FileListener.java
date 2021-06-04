/**
 * Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 1, 2010 2:53:32 PM
 *
 */

package lv.lumii.pixelmaster.modules.grapheditor.gui.menulisteners;

import lv.lumii.pixelmaster.modules.spw.domain.graph.GraphIO;
import lv.lumii.pixelmaster.modules.spw.domain.graph.UGraph;
import lv.lumii.pixelmaster.modules.grapheditor.gui.GraphEditorDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;



public class FileListener extends MyListener implements ActionListener
{
    public FileListener( GraphEditorDialog graphEditorDialog)
    {
        super( graphEditorDialog );
    }


    
    public void actionPerformed( ActionEvent event )
    {
        Object source = event.getSource();

        if ( source instanceof JMenuItem )
        {
            JMenuItem item = (JMenuItem)source;

            if ( item.getText().equals( "New" ) )
            {
                super.graphEditorDialog.workPanel.setGraph( new UGraph() );
                super.graphEditorDialog.filename = null;
                super.graphEditorDialog.setTitle();
            }
            else if ( item.getText().equals( "Open" ) )
            {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode( JFileChooser.FILES_ONLY );

                if ( fc.showOpenDialog( super.graphEditorDialog ) == JFileChooser.APPROVE_OPTION )
                {
                    UGraph graph = GraphIO.read( new File( fc.getSelectedFile().getAbsolutePath() ) );

                    if ( graph != null )
                    {
                        super.graphEditorDialog.workPanel.setGraph( graph );
                        super.graphEditorDialog.filename = fc.getSelectedFile().getAbsolutePath();
                        super.graphEditorDialog.setTitle();
                    }
                }
            }
            else if ( item.getText().equals( "Save" ) )
            {
                if ( super.graphEditorDialog.filename != null )
                {
                    GraphIO.write( super.graphEditorDialog.workPanel.getGraph(), new File( super.graphEditorDialog.filename ) );
                }
                else
                {
                    this.saveAs();
                }
            }
            else if ( item.getText().equals( "Save as..." ) )
            {
                this.saveAs();
            }
            else if ( item.getText().equals( "Exit" ) )
            {
                System.exit( 0 );
            }
        }
    }

    private void saveAs()
    {
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode( JFileChooser.FILES_ONLY );

        if ( fc.showSaveDialog( super.graphEditorDialog) == JFileChooser.APPROVE_OPTION )
        {
            GraphIO.write( super.graphEditorDialog.workPanel.getGraph(), new File( fc.getSelectedFile().getAbsolutePath() ) );
            super.graphEditorDialog.filename = fc.getSelectedFile().getAbsolutePath();
            super.graphEditorDialog.setTitle();
        }
    }
}




