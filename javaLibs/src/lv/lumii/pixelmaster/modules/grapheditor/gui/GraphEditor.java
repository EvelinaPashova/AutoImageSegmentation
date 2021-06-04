/**
 * Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 1, 2010 1:39:42 PM
 *
 */

package lv.lumii.pixelmaster.modules.grapheditor.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



public class GraphEditor
{
    public static void main( String[] arguments )
    {
        GraphEditorDialog graphEditorDialog = new GraphEditorDialog();
		graphEditorDialog.addWindowListener(new WindowAdapter() {
                                        public void windowClosing( WindowEvent e )
                                        {
                                            System.exit( 0 );
                                        }
                                    }
        );

    }
}




