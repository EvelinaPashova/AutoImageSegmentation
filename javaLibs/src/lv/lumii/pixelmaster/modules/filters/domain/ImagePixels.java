package lv.lumii.pixelmaster.modules.filters.domain;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.round;

/**
 */
public final class ImagePixels {

  private int[] pixelMatrix = new int[256];
  private  int mask =7;
  private int offset = mask/2;
  BufferedImage interpretImg;

      public void cdf(int width, int height) {
          int min_value = pixelMatrix[0];
          int M = width * height;
          int K = 256; // intensitāšu vērtību skaits

          for (int i = 1; i < 256; i ++) {
              pixelMatrix[i] = pixelMatrix[i-1] + pixelMatrix[i];
          }

          //minimālā vērtība - masīva pirmā vērtība

          for (int i = 0; i < 256; i++) {
              int tmp = (pixelMatrix[i] - min_value) * (K - 1);
              pixelMatrix[i] = round(tmp / M);
          }
      }

      public void getPixels (int i, int j, BufferedImage image) {
          float[] hsb;
          float brightness;
          int rgb_val;

          for (int x = i; x < i+mask; x++) {
              for(int y = j; y < j+mask; y++) {
                  int rgb = image.getRGB(x,y);
                  int red = ((rgb >> 16) & 0xff);
                  int green = ((rgb >> 8) & 0xff);
                  int blue = ((rgb ) & 0xff);

                  hsb = Color.RGBtoHSB(red, green, blue, null);
                  brightness = hsb[2];
                 //sarēķinam pozīciju priekš viendimensiju masīva un ieliekam iekšā attiecīgo vērtību
                  rgb_val = round(brightness*255);

                  pixelMatrix[rgb_val] ++;
              }
          }
           cdf(mask,mask);

          // atgriežās maska x maska veertibu apgabals un nem tikai vidējo vērtību

          float[] hsb2;
          float hue2;
          float saturation2;
          float brightness2;
          int replaceB;
          float repl;

          //nolasa no attēla aizvietojamos pikseļus un noskaidro tam h, s un b vērtības
          int rgb2 = image.getRGB(i+offset,j+offset);
          int red2 = ((rgb2 >> 16) & 0xff);
          int green2 = ((rgb2 >> 8) & 0xff);
          int blue2 = ((rgb2 ) & 0xff);
          hsb2 = Color.RGBtoHSB(red2, green2, blue2, null);
          hue2 = hsb2[0];
          saturation2 = hsb2[1];
          brightness2 = hsb2[2];
          //noskaidro kuru pikseli aizvietot
          replaceB = round(brightness2*255);
          replaceB = pixelMatrix[replaceB];
          repl = (float)replaceB/(float)255;
          int rgb = Color.HSBtoRGB(hue2, saturation2, repl);

          interpretImg.setRGB(i+offset,j+offset,rgb);

          for (int k = 0; k < 256; k++) {
              pixelMatrix[k] = 0;
          }
      }


      public BufferedImage marchThroughImage(BufferedImage image) {
          int w,h, s;
          int[] rgb = new int[image.getWidth()*image.getHeight()];
          image.getRGB(0, 0, image.getWidth() ,image.getHeight(), rgb, 0, image.getWidth());
          //izveido lielāku bildi, lai apstrādātu arī malas (oriģinālā + maska)
          w = image.getWidth()+mask;
          h = image.getHeight()+mask;
          BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
          interpretImg = new BufferedImage(newImage.getWidth(), newImage.getHeight(), BufferedImage.TYPE_INT_RGB);
          newImage.setRGB(offset,offset,image.getWidth(),image.getHeight(),rgb,0, image.getWidth());

          for (int x = 0; x < newImage.getWidth()-(mask-1); x++ )  {
             for (int y = 0; y < newImage.getHeight()-(mask-1); y++) {
                 getPixels(x,y, newImage);
             }
          }

         // image = newImage.getSubimage(offset,offset,image.getWidth(),image.getHeight());
         return interpretImg;
      }
}