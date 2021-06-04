/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lv.lumii.pixelmaster.modules.recognition.domain;

import lv.lumii.pixelmaster.modules.recognition.domain.ResizeImage;
import java.awt.image.BufferedImage;
import junit.framework.TestCase;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author user
 */
public class ResizeImageTest extends TestCase {
    
    public ResizeImageTest(String testName) {
        super(testName);
    }

    /**
     * Test of resizeImage method, of class ResizeImage.
     */
    /*public void testResizeImage() {
        System.out.println("Recognition.ResizeImage: resizeImage");
        BufferedImage bufOrigImage = null;
        int newW = 0;
        int newH = 0;
        int w = 0;
        int h = 0;
        BufferedImage expResult = null;
        BufferedImage result = ResizeImage.resizeImage(bufOrigImage, newW, newH, w, h);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of resizePropertiesNewW method, of class ResizeImage.
     */
    public void testResizePropertiesNewW() {
        System.out.println("Recognition.ResizeImage: resizePropertiesNewW");
        RasterImage rImage1 = new RasterImage(120,200);
        int templateSize = 100;
        int expResult1 = 60;
        int result1 = ResizeImage.resizePropertiesNewW(rImage1, templateSize);//kur šablona augstums ir mazāks
        RasterImage rImage2 = new RasterImage(70,80);
        int expResult2 = 87;
        int result2 = ResizeImage.resizePropertiesNewW(rImage2, templateSize);//kur šablona augstums ir lielāks
        assertEquals(expResult1, result1);
        assertEquals(expResult2, result2);
    }

    /**
     * Test of resizePropertiesNewH method, of class ResizeImage.
     */
    public void testResizePropertiesNewH() {
         System.out.println("Recognition.ResizeImage: resizePropertiesNewH");
        RasterImage rImage1 = new RasterImage(200,120);
        int templateSize = 100;
        int expResult1 = 60;
        int result1 = ResizeImage.resizePropertiesNewH(rImage1, templateSize);//kur šablona augstums ir mazāks
        RasterImage rImage2 = new RasterImage(80,70);
        int expResult2 = 87;
        int result2 = ResizeImage.resizePropertiesNewH(rImage2, templateSize);//kur šablona augstums ir lielāks
        assertEquals(expResult1, result1);
        assertEquals(expResult2, result2);
    }
}
