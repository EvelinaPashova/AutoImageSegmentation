package lv.lumii.pixelmaster.modules.recognition.domain;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Klase atbild par teksta zīmes attēla modifikāciju
 * @author Sandra Rivare
 * @since 03.04.2009
 */
public class ImageModification {     
    /**
     *
     **/
    public static RasterImage drawWhiteBack(RasterImage rImage, int templateSize) {
        try {
            int w = rImage.getWidth();
            int h = rImage.getHeight();
            RasterImage backIm = new RasterImage(templateSize, templateSize);
            if (h == templateSize) {
                int dif = templateSize - w;
                dif = dif / 2;
                for (int y = 0; y < templateSize; y++) {
                    for (int x = 0; x < templateSize; x++) {
                        if (x < dif) {
                            int r = 255;
                            int g = 255;
                            int b = 255;
                            backIm.setRGB(y * templateSize + x, b + (g << 8) + (r << 16));
                        } else {
                            if (x >= rImage.getWidth() + dif) {
                                int r = 255;
                                int g = 255;
                                int b = 255;
                                backIm.setRGB(y * templateSize + x, b + (g << 8) + (r << 16));
                            } else {
                                backIm.setRGB(y * templateSize + x, rImage.getRGB(y * w + x - dif));
                            }
                        }
                    }
                }
            } else {
                int dif = templateSize - h;
                dif = dif / 2;
                for (int y = 0; y < templateSize; y++) {
                    for (int x = 0; x < templateSize; x++) {
                        if (y < dif) {
                            int r = 255;
                            int g = 255;
                            int b = 255;
                            backIm.setRGB(y * templateSize + x, b + (g << 8) + (r << 16));
                        } else {
                            if (y >= rImage.getHeight() + dif) {
                                int r = 255;
                                int g = 255;
                                int b = 255;
                                backIm.setRGB(y * templateSize + x, b + (g << 8) + (r << 16));
                            } else {
                                backIm.setRGB(y * templateSize + x, rImage.getRGB((y - dif) * w + x));
                            }
                        }
                    }
                }
            }
            return backIm;
        } catch (Exception ex) {
            Logger.getLogger(ImageModification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }    
}//end of ImageModification
