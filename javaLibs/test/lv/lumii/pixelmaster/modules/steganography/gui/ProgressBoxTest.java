/**
 * This is a <code>JUnit</code> test class for <code>ProgressBox</code>.
 *
 * @author Andrey Zhmakin
 *
 * Created on 08.06.2010 18:57:26
 *
 */

package lv.lumii.pixelmaster.modules.steganography.gui;

import lv.lumii.pixelmaster.modules.steganography.gui.ProgressBox;
import org.junit.*;


public class ProgressBoxTest
{
    @Test
    public void s1t1()
    {
        ProgressBox pb = new ProgressBox( null );

        pb.show();

        pb.setIndeterminate( true );

        pb.setTitle( "ProgressBox test" );
        pb.setNote( "Progress..." );

        while ( !pb.isCanceled() )
        {
            // Do nothing!
        }

        pb.hide();
        pb.close();
    }
}




