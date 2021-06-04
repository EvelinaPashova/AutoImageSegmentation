/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lv.lumii.pixelmaster.modules.compare.domain;

import lv.lumii.pixelmaster.modules.compare.domain.convert;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import junit.framework.Assert;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author Onty
 */
public class convertTest {

    public convertTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of modifyImage method, of class convert.
     */
    @Test
    public void testModifyImage() {
        System.out.println("modifyImage");
        BufferedImage tmpBufImage=null;
        BufferedImage tmpBufImage1=null;
        
        try {
            tmpBufImage = ImageIO.read(new File("testdata/compare/Characters/C.bmp"));
            tmpBufImage1 = ImageIO.read(new File("testdata/compare/Cconvert.bmp"));
        } catch (IOException ex) {
            Logger.getLogger(compareTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        RasterImage source = new RasterImage(560,344);
        RasterImage expResult = new RasterImage(810,810);

        ImageConverter.convertBufImage(tmpBufImage, source);
        ImageConverter.convertBufImage(tmpBufImage1, expResult);

        source=convert.modifyImage(source);
        Assert.assertEquals(expResult, source);
    }

    /**
     * Test of Rotate method, of class convert.
     */
    @Test
    public void testRotate_RasterImage_char() {
        System.out.println("Rotate");
        BufferedImage tmpBufImage=null;
        BufferedImage tmpBufImage1=null;

        try {
            tmpBufImage = ImageIO.read(new File("testdata/compare/Characters/C.bmp"));
            tmpBufImage1 = ImageIO.read(new File("testdata/compare/Cclock.bmp"));
        } catch (IOException ex) {
            Logger.getLogger(compareTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        RasterImage source = new RasterImage(560,344);
        RasterImage expResult = new RasterImage(344,560);

        ImageConverter.convertBufImage(tmpBufImage, source);
        ImageConverter.convertBufImage(tmpBufImage1, expResult);

        convert.Rotate(source,'c');
        Assert.assertEquals(expResult, source);
    }

    /**
     * Test of Rotate method, of class convert.
     */
    @Test
    public void testRotate_booleanArr_char() {
        System.out.println("Rotate");
        boolean[] input = new boolean[810*810];
        boolean[] output = new boolean[810*810];
        for (int i=0; i<810*810;i++){
                if (i<810*405){
                    input[i]=true;
                    output[i]=false;
                }
                else{
                    input[i]=false;
                    output[i]=true;
                }
        }
        char action = 'f';
        convert.Rotate(input, action);
        boolean answer = Arrays.equals(input, output);
        Assert.assertEquals(answer, true);
    }

}