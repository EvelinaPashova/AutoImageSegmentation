/**
 * Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 1, 2010 3:19:18 PM
 *
 */

package lv.lumii.pixelmaster.modules.grapheditor.gui.menulisteners;

import lv.lumii.pixelmaster.modules.grapheditor.gui.GraphEditorDialog;


public class MyListener
{
    public MyListener()
    {
        this.graphEditorDialog = null;
    }

    
    public MyListener( GraphEditorDialog graphEditorDialog)
    {
        this.graphEditorDialog = graphEditorDialog;
    }


    GraphEditorDialog graphEditorDialog;
}




