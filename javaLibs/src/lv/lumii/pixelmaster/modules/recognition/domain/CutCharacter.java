package lv.lumii.pixelmaster.modules.recognition.domain;

import lv.lumii.pixelmaster.core.api.domain.RGB;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 * Klase nodrošina teksta zīmes izgriezšanu no attēla
 * @author Sandra Rivare
 * @since 11.04.2009
 **/
public class CutCharacter {
    /**
     * Atrod teksta zīmes robežas
     * @param rImage orģināls attēls
     * @return cutIm parametrs, kas satur robežu koordinātas
     * @return cutIm izgriezts attēls
     **/
    public static RasterImage findBorders (RasterImage rImage){
        int width = rImage.getWidth();
        int height = rImage.getHeight();
        boolean cont = true;
        int top = 0;
        int bottom = 0;
        int left = 0;
        int right = 0;
        //atrod aušējo robežu
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = RGB.getR(rImage.getRGB(y * width + x));
                int g = RGB.getG(rImage.getRGB(y * width + x));
                int b = RGB.getB(rImage.getRGB(y * width + x));
                if (r == 0 && g == 0 && b == 0) {
                    top = y;
                    cont = false;
                }
                if (cont == false) break;
            }
            if (cont == false) break;
        }
        //atrod apakšējo robežu
        cont = true;
        for (int y = height - 1; y > 0; y--) {
            for (int x = 0; x < width; x++) {
                int r = RGB.getR(rImage.getRGB(y * width + x));
                int g = RGB.getG(rImage.getRGB(y * width + x));
                int b = RGB.getB(rImage.getRGB(y * width + x));
                if (r == 0 && g == 0 && b == 0) {
                    bottom = y + 1;
                    cont = false;
                }
                if (cont == false) break;
            }
            if (cont == false) break;
        }
        //atrod kreiso robežu
        cont = true;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int r = RGB.getR(rImage.getRGB(y * width + x));
                int g = RGB.getG(rImage.getRGB(y * width + x));
                int b = RGB.getB(rImage.getRGB(y * width + x));
                if (r == 0 && g == 0 && b == 0) {
                    left = x;
                    cont = false;
                }
                if (cont == false) break;
            }
            if (cont == false) break;
        }
        //atrod labo robežu
        cont = true;
        for (int x = width - 1; x > 0; x--) {
            for (int y = 0; y < height; y++) {
                int r = RGB.getR(rImage.getRGB(y * width + x));
                int g = RGB.getG(rImage.getRGB(y * width + x));
                int b = RGB.getB(rImage.getRGB(y * width + x));
                if (r == 0 && g == 0 && b == 0) {
                    right = x;
                    cont = false;
                }
                if (cont == false) break;
            }
            if (cont == false) break;
        }
        return cutCharacter(top,bottom,right,left,rImage);
    }//end of findBorders

    /**
     * Izveido jaunu raster attēlu un pārkopē tajā teksta zīmes RGB vērtības
     **/
    public static RasterImage cutCharacter(int top, int bottom, int right, int left, RasterImage rImage){
        //izgriež attēlu, ierakstot to jauna RasterImage
        int tmpheight = bottom - top;
        int tmpwidth = right + 1 - left; //rindu pirms melniem pikseļiem
        RasterImage cutIm = new RasterImage(tmpwidth, tmpheight);
        int y2 = 0;
        for (int y = top; y < bottom; y++) {
            int x2 = 0;
            for (int x = left; x <= right; x++) {
                cutIm.setRGB(y2 * cutIm.getWidth() + x2, rImage.getRGB(y * rImage.getWidth() + x));
                //System.out.print(cutIm.pixels[y2 * cutIm.width + x2]+", ");
                x2++;
            }
            y2++;
        }
        System.out.println(cutIm.getWidth()*cutIm.getHeight());
        return cutIm;
    }//end of cutCharacter
}
