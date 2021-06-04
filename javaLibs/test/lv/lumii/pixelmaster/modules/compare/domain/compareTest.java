package lv.lumii.pixelmaster.modules.compare.domain;




import lv.lumii.pixelmaster.modules.compare.domain.CompareByPixelBlocks;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import junit.framework.Assert;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import org.junit.Test;


/**
 *
 * @author Onty
 */
public class compareTest {

  
    /**
     * Test of CompareObj method, of class compare.
     */
    @Test
    public void testCompareObj() {
        System.out.println("CompareObj");
        BufferedImage tmpBufImage=null;
        BufferedImage tmpBufImage1=null;
        BufferedImage tmpBufImage2=null;
        try {
            tmpBufImage = ImageIO.read(new File("testdata/compare/Characters/C.bmp"));
            tmpBufImage1 = ImageIO.read(new File("testdata/compare/Characters/O.bmp"));
            tmpBufImage2 = ImageIO.read(new File("testdata/compare/C.bmp"));
        } catch (IOException ex) {
            Logger.getLogger(compareTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        RasterImage source = new RasterImage(560,344);
        RasterImage target = new RasterImage(560,344);
        RasterImage expResult = new RasterImage(810,810);
        
        ImageConverter.convertBufImage(tmpBufImage, source);
        ImageConverter.convertBufImage(tmpBufImage1, target);
        ImageConverter.convertBufImage(tmpBufImage2, expResult);

        source=CompareByPixelBlocks.CompareObj(source, target,null);
        Assert.assertEquals(expResult, source);
        
    }

}