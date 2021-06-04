package lv.lumii.pixelmaster.modules.recognition.domain;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Klase nodrošina attēla izmēra izmaiņu
 * @author Sandra Rivare
 * @since 11.04.2009
 */
public class ResizeImage {
    public static BufferedImage resizeImage(BufferedImage bufOrigImage, int newW, int newH, int w, int h) {
        BufferedImage dimg = dimg = new BufferedImage(newW, newH, bufOrigImage.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(bufOrigImage, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }
    public static int resizePropertiesNewW(RasterImage rImage, int templateSize){
        double tempW;
        int newW,newH;
        if(rImage.getHeight()>=templateSize){
            int tmp = rImage.getHeight()/templateSize;
            return newW = rImage.getWidth()/tmp;
        }else{
            double tmp = (double)templateSize/(double)rImage.getHeight();
            tempW = (double)rImage.getWidth()*tmp;
            return newW = (int)tempW;
        }
    }
    public static int resizePropertiesNewH(RasterImage rImage, int templateSize){
        double tempH;
        int newW,newH;
        if(rImage.getWidth()>=templateSize){
            int tmp = rImage.getWidth()/templateSize;
            return newH = rImage.getHeight()/tmp;
        }else{
            double tmp = (double)templateSize/(double)rImage.getWidth();
            tempH = (double)rImage.getHeight()*tmp;
            return newH = (int)tempH;
        }
    }
}
