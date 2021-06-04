package lv.lumii.pixelmaster.modules.compare.domain;

import lv.lumii.pixelmaster.modules.compare.domain.medFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import org.junit.Assert;
import org.junit.Test;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author Onty
 */
public class medFilterTest {

    
    @Test
    public void testMedFilter() {
         System.out.println("medFilter");
        BufferedImage tmpBufImage=null;
        BufferedImage tmpBufImage1=null;
        try {
            tmpBufImage = ImageIO.read(new File("testdata/compare/Characters/C.bmp"));
            tmpBufImage1 = ImageIO.read(new File("testdata/compare/Cmedian.bmp"));

        } catch (IOException ex) {
            Logger.getLogger(compareTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        RasterImage source = new RasterImage(560,344);
        ImageConverter.convertBufImage(tmpBufImage, source);
        RasterImage target = new RasterImage(source);
        RasterImage expResult = new RasterImage(560,344);
        ImageConverter.convertBufImage(tmpBufImage1, expResult);


        int radius = 3;
        int diff = 15;
        medFilter.medFilter(source, target, radius, diff);
        Assert.assertEquals(expResult, target);
    }

}