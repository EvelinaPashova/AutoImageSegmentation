package com.binary.algorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Arrays;
//import java.math.*;

public class Binarize {

    public static BufferedImage original, grayscale, binarized;
    public static final String[] EXT = {"bmp", "jpeg"};

    /*public static void main(String[] args) throws IOException {

        File original_f = new File(args[0] + ".jpg");
        String output_f = args[0] + "_bin";
        original = ImageIO.read(original_f);
        grayscale = toGray(original);
        binarized = binarize(grayscale);
        writeImage(output_f);

    }*/
    public static void writeImage(String output) throws IOException {
        //System.out.println(output);
        File file = new File(output);
        file.getParentFile().mkdirs();
        file.createNewFile();
        String fileSplit[] = output.split("[.]");
        String ext = fileSplit[fileSplit.length - 1];
        ImageIO.write(binarized, ext, file);
    }

    // Return histogram of grayscale image
    public static int[] imageHistogram(BufferedImage input, int rgb) {
        //rgb=0 : Red
        //rgb=1 : Green
        //rgb=2 : Blue
        int[] histogram = new int[256];

        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }
        for (int i = 0; i < input.getHeight(); i++) {
            for (int j = 0; j < input.getWidth(); j++) {
                switch(rgb){
                    case 0:
                    {
                        int red = new Color(input.getRGB(j, i)).getRed();
                        histogram[red]++;
                        break;
                    }
                    case 1:
                    {
                        int green = new Color(input.getRGB(j, i)).getGreen();
                        histogram[green]++;
                        break;
                    }
                    case 2:
                    {
                        int blue = new Color(input.getRGB(j, i)).getBlue();
                        histogram[blue]++;
                        break;
                    }
                }
                
            }
        }

        return histogram;

    }

  
    public static BufferedImage toGray(BufferedImage original) {

        int alpha, red, green, blue;
        int newPixel;

        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);

                // Write pixels into image
                lum.setRGB(i, j, newPixel);

            }
        }

        return lum;

    }

    // Convert R, G, B, Alpha to standard 8 bit
    public static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

     }
    //Convert RGB to HSI color model
   public static void RGBtoHSI (int height,int width,BufferedImage In,HSIPIXEL Out[][]){
    int j,k;
    double r,g,b,h,s,i,min;
    double temp;//, Hue, Sat, Inten;
    for(j=0;j<width;j++)
    for (k=0; k<height;k++) {
    //normalizirane na RGB
    
    r=(double)(new Color(In.getRGB(j, k)).getRed())/((double)(new Color(In.getRGB(j, k))).getRed()+(double)(new Color(In.getRGB(j, k))).getGreen()+(double)(new Color(In.getRGB(j, k))).getBlue());
    g=(double)(new Color(In.getRGB(j, k)).getGreen())/((double)(new Color(In.getRGB(j, k))).getRed()+(double)(new Color(In.getRGB(j, k))).getGreen()+(double)(new Color(In.getRGB(j, k))).getBlue());
    b=(double)(new Color(In.getRGB(j, k)).getBlue())/((double)(new Color(In.getRGB(j, k))).getRed()+(double)(new Color(In.getRGB(j, k))).getGreen()+(double)(new Color(In.getRGB(j, k))).getBlue());
    //g=(double)In[j].Green/((double)In[j].Red+(double)In[j].Green+(double)In[j].Blue);
    //b=(double)In[j].Blue/((double)In[j].Red+(double)In[j].Green+(double)In[j].Blue);
    temp=(r-g)*(r-g)+(r-b)*(g-b);
    if(temp==0)temp=Double.MIN_VALUE;//DBL_MIN ;
    temp=(0.5*((r-g)+(r-b))) / Math.pow(temp,0.5);
    if(b>g)
    h=2*Math.PI - Math.acos(temp);
    else h=Math.acos(temp);
    if (r<g) min=r;
    else min=g;
    if (b<min) min=b;

    s=1-3*min;
    i=((double)(new Color(In.getRGB(j, k))).getRed()+(double)(new Color(In.getRGB(j, k))).getGreen()+(double)(new Color(In.getRGB(j, k))).getBlue()) / (3*255);
    //normalizirane na HSI
    Out[j][k].Hue=h*(180/Math.PI);
    Out[j][k].Saturation=s*100;
    Out[j][k].Intensity=i*255;

   

    Out[j][k].R=s*Math.cos(h);
    Out[j][k].J=s*Math.sin(h);
    
    }
}
public static BufferedImage Multilevel_Two_Threshold(int height,int width,HSIPIXEL In[][], int mode,int segmented_colors){
    
    int val, newPixel=0,i,j,k,p=0,z=0,b=0;Color cSeg=new Color(Color.BLACK.hashCode());
    double hsi[]=new double[height*width],sat[]=new double[height*width],hue[]=new double[height*width];
    double thresholdIntensity, thresholdIntensity2,thresholdIntensity3, thresholdSaturation,thresholdSaturation2,thresholdSaturation3,thresholdHue2,thresholdHue,thresholdHue3;
    
    
    //transform two-dim arr to one-dim
    for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { hsi[p]=In[j][k].Intensity; p++; }
    
    Arrays.sort(hsi);
      for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { hue[z]=In[j][k].Hue; z++; }
    
    Arrays.sort(hue);
    
    for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { sat[b]=In[j][k].Saturation; b++; }
    
    Arrays.sort(sat);
   
    thresholdHue=hue[height*width/3];
    thresholdHue2=hue[2*height*width/3];
    thresholdSaturation=sat[height*width/3];
    thresholdSaturation2=sat[2*height*width/3];
    thresholdIntensity=hsi[height*width/3];
    thresholdIntensity2=hsi[2*height*width/3];

    
    if (mode==1){
    thresholdHue=hue[height*width/3];
    thresholdHue2=hue[height*width/3];
    thresholdSaturation=sat[height*width/3];
    thresholdSaturation2=sat[2*height*width/3];
    thresholdIntensity=hsi[height*width/3];
    thresholdIntensity2=hsi[2*height*width/3];
    }
    else if (mode==2){
    thresholdHue=hue[height*width/3];
    thresholdHue2=hue[2*height*width/3];
    thresholdSaturation=sat[height*width/3];
    thresholdSaturation2=sat[2*height*width/3];
    thresholdIntensity=hsi[height*width/3];
    thresholdIntensity2=hsi[2*height*width/3];
    }
    BufferedImage Out = new BufferedImage(Binarize.original.getWidth(), Binarize.original.getHeight(), Binarize.original.getType());
    for(i=0;i<width;i++)
    for (j=0; j<height;j++) {
         val = new Color(original.getRGB(i, j)).getRed();
                    int alpha = new Color(original.getRGB(i, j)).getAlpha();
                    if ((In[i][j].Intensity < thresholdIntensity))  {
                    if((In[i][j].Saturation > thresholdSaturation2))
                             if((In[i][j].Hue < thresholdHue))
                                 if(segmented_colors==0)
                                    newPixel = 0;
                                 else
                                            cSeg=new Color(Color.YELLOW.getRed(),Color.YELLOW.getGreen(),Color.YELLOW.getBlue());
                             else
                                    if((In[i][j].Hue < thresholdHue2))
                                        if( segmented_colors==0)
                                          newPixel = 150;
                                        else
                                            cSeg=new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue());
                                    else
                                             if(segmented_colors==0)
                                                newPixel = 190;
                                             else
                                            cSeg=new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue());
                                         
                        else
                            if((In[i][j].Saturation < thresholdSaturation2))
                                        if( segmented_colors==0)
                                          newPixel = 150;
                                        else
                                            cSeg=new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue());
                                else
                                          if(segmented_colors==0)
                                              newPixel = 190;
                                             else
                                            cSeg=new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue());
           
                    } else { 
                        if(In[i][j].Intensity > thresholdIntensity)
                           if( segmented_colors==0)
                              newPixel = 150;
                            else
                                cSeg=new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue());
                        else
                                 if(segmented_colors==0)
                                   newPixel = 190;
                                   else
                                   cSeg=new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue());

                    }
            
                    if(segmented_colors==0)
                    newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                    else 
                        newPixel=colorToRGB(alpha,cSeg.getRed(),cSeg.getGreen(),cSeg.getBlue());
                    Out.setRGB(i, j, newPixel);
    }
  
    return Out;

}
public static BufferedImage Multilevel_One_Threshold(int height,int width,HSIPIXEL In[][], int mode,int segmented_colors){
    
    int val, newPixel=0,i,j,k,p=0,z=0,b=0;Color cSeg=new Color(Color.BLACK.hashCode());
    double hsi[]=new double[height*width],sat[]=new double[height*width],hue[]=new double[height*width];
    double thresholdIntensity, thresholdIntensity2,thresholdIntensity3, thresholdSaturation,thresholdSaturation2,thresholdSaturation3,thresholdHue2,thresholdHue,thresholdHue3;
    
    
    //transform two-dim arr to one-dim
    for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { hsi[p]=In[j][k].Intensity; p++; }
    
    Arrays.sort(hsi);
      for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { hue[z]=In[j][k].Hue; z++; }
    
    Arrays.sort(hue);
    
    for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { sat[b]=In[j][k].Saturation; b++; }
    
    Arrays.sort(sat);
   
    thresholdHue=hue[height*width/3];
    thresholdSaturation=sat[height*width/3];
    thresholdIntensity=hsi[height*width/3];
       
    if (mode==1){
    thresholdHue=hue[height*width/3];
    thresholdSaturation=sat[height*width/3];
    thresholdIntensity=hsi[height*width/3];
    }
    else if (mode==2){
    thresholdHue=hue[height*width/3];
    thresholdSaturation=sat[height*width/3];
    thresholdIntensity=hsi[height*width/3];
    }
    BufferedImage Out = new BufferedImage(Binarize.original.getWidth(), Binarize.original.getHeight(), Binarize.original.getType());
    for(i=0;i<width;i++)
    for (j=0; j<height;j++) {
         val = new Color(original.getRGB(i, j)).getRed();
                    int alpha = new Color(original.getRGB(i, j)).getAlpha();
                    if ((In[i][j].Intensity < thresholdIntensity))  {
                    if((In[i][j].Saturation > thresholdSaturation))
                             if((In[i][j].Hue < thresholdHue))
                                 if(segmented_colors==0)
                                    newPixel = 0;
                                 else
                                            cSeg=new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue());
                             else
                                   
                                        if( segmented_colors==0)
                                          newPixel = 150;
                                        else
                                             cSeg=new Color(Color.ORANGE.getRed(),Color.ORANGE.getGreen(),Color.ORANGE.getBlue());
                                   
                        else
                            
                                        if( segmented_colors==0)
                                          newPixel = 150;
                                        else
                                          cSeg=new Color(Color.ORANGE.getRed(),Color.ORANGE.getGreen(),Color.ORANGE.getBlue());
                                
                    } else { 
                      
                           if( segmented_colors==0)
                              newPixel = 150;
                            else
                                cSeg=new Color(Color.ORANGE.getRed(),Color.ORANGE.getGreen(),Color.ORANGE.getBlue());
                        

                    }
            
                    if(segmented_colors==0)
                    newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                    else 
                        newPixel=colorToRGB(alpha,cSeg.getRed(),cSeg.getGreen(),cSeg.getBlue());
                    Out.setRGB(i, j, newPixel);
    }
  
    return Out;

}
public static BufferedImage Multilevel_Three_Threshold(int height,int width,HSIPIXEL In[][], int mode,int segmented_colors)
 {
    int val, newPixel=0,i,j,k,p=0,z=0,b=0;Color cSeg=new Color(Color.BLACK.hashCode());
    double hsi[]=new double[height*width],sat[]=new double[height*width],hue[]=new double[height*width];
    double thresholdIntensity, thresholdIntensity2,thresholdIntensity3, thresholdSaturation,thresholdSaturation2,thresholdSaturation3,thresholdHue2,thresholdHue,thresholdHue3;
    
    
    //transform two-dim arr to one-dim
    for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { hsi[p]=In[j][k].Intensity; p++; }
    
    Arrays.sort(hsi);
      for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { hue[z]=In[j][k].Hue; z++; }
    
    Arrays.sort(hue);
    
    for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { sat[b]=In[j][k].Saturation; b++; }
    
    Arrays.sort(sat);
   
    thresholdHue=hue[height*width/4];
    thresholdHue2=hue[height*width/2];
    thresholdHue3=hue[3*height*width/4];
    thresholdSaturation=sat[height*width/4];
    thresholdSaturation2=sat[height*width/2];
    thresholdSaturation3=sat[3*height*width/4];
    thresholdIntensity=hsi[height*width/4];
    thresholdIntensity2=hsi[height*width/2];
    thresholdIntensity3=hsi[3*height*width/4];
    
    if (mode==1){
    thresholdHue=hue[3*height*width/8];
    thresholdHue2=hue[height*width/2];
    thresholdHue3=hue[5*height*width/8];
    thresholdSaturation=sat[3*height*width/8];
    thresholdSaturation2=sat[height*width/2];
    thresholdSaturation3=sat[5*height*width/8];
    thresholdIntensity=hsi[3*height*width/8];
    thresholdIntensity2=hsi[height*width/2];
    thresholdIntensity3=hsi[5*height*width/8];
    }
    else if (mode==2){
    thresholdHue=hue[1*height*width/8];
    thresholdHue2=hue[height*width/2];
    thresholdHue3=hue[7*height*width/8];
    thresholdSaturation=sat[1*height*width/8];
    thresholdSaturation2=sat[height*width/2];
    thresholdSaturation3=sat[7*height*width/8];
    thresholdIntensity=hsi[1*height*width/8];
    thresholdIntensity2=hsi[height*width/2];
    thresholdIntensity3=hsi[7*height*width/8];
    }
    BufferedImage Out = new BufferedImage(Binarize.original.getWidth(), Binarize.original.getHeight(), Binarize.original.getType());
    for(i=0;i<width;i++)
    for (j=0; j<height;j++) {
         val = new Color(original.getRGB(i, j)).getRed();
                    int alpha = new Color(original.getRGB(i, j)).getAlpha();
                    if ((In[i][j].Intensity < thresholdIntensity))  {
                    if((In[i][j].Saturation > thresholdSaturation3))
                             if((In[i][j].Hue < thresholdHue))
                                 if(segmented_colors==0)
                                    newPixel = 0;
                                 else
                                            cSeg=new Color(Color.YELLOW.getRed(),Color.YELLOW.getGreen(),Color.YELLOW.getBlue());
                             else
                                    if((In[i][j].Hue < thresholdHue2))
                                        if( segmented_colors==0)
                                          newPixel = 150;
                                        else
                                            cSeg=new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue());
                                    else
                                         if((In[i][j].Hue < thresholdHue3))
                                             if(segmented_colors==0)
                                                newPixel = 190;
                                             else
                                            cSeg=new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue());
                                         else
                                             if(segmented_colors==0)
                                                newPixel = 255;
                                             else
                                            cSeg=new Color(Color.BLUE.getRed(),Color.BLUE.getGreen(),Color.BLUE.getBlue());
                        else
                            if((In[i][j].Saturation > thresholdSaturation2))
                                        if( segmented_colors==0)
                                          newPixel = 150;
                                        else
                                            cSeg=new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue());
                                else
                                    if((In[i][j].Saturation > thresholdSaturation))
                                        if(segmented_colors==0)
                                              newPixel = 190;
                                             else
                                            cSeg=new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue());
                                    else
                                        if(segmented_colors==0)
                                             newPixel = 255;
                                             else
                                            cSeg=new Color(Color.BLUE.getRed(),Color.BLUE.getGreen(),Color.BLUE.getBlue());
                       
                    } else { 
                        if(In[i][j].Intensity < thresholdIntensity2)
                           if( segmented_colors==0)
                              newPixel = 150;
                            else
                                cSeg=new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue());
                        else
                            if(In[i][j].Intensity < thresholdIntensity3)
                                if(segmented_colors==0)
                                   newPixel = 190;
                                   else
                                   cSeg=new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue());
                            else
                               if(segmented_colors==0)
                                  newPixel = 255;
                              else
                                  cSeg=new Color(Color.BLUE.getRed(),Color.BLUE.getGreen(),Color.BLUE.getBlue());
                    }
            
                    if(segmented_colors==0)
                    newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                    else 
                        newPixel=colorToRGB(alpha,cSeg.getRed(),cSeg.getGreen(),cSeg.getBlue());
                    Out.setRGB(i, j, newPixel);
    }
  
    return Out;
 }   
public static BufferedImage Multilevel_Four_Threshold(int height,int width,HSIPIXEL In[][], int mode,int segmented_colors)
 {
    int val, newPixel=0,i,j,k,p=0,z=0,b=0;Color cSeg=new Color(Color.BLACK.hashCode());
    double hsi[]=new double[height*width],sat[]=new double[height*width],hue[]=new double[height*width];
    double thresholdIntensity, thresholdIntensity2,thresholdIntensity3,thresholdIntensity4, thresholdSaturation,thresholdSaturation2,thresholdSaturation3,thresholdSaturation4, thresholdHue2,thresholdHue,thresholdHue3,thresholdHue4;
    
    
    //transform two-dim arr to one-dim
    for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { hsi[p]=In[j][k].Intensity; p++; }
    
    Arrays.sort(hsi);
      for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { hue[z]=In[j][k].Hue; z++; }
    
    Arrays.sort(hue);
    
    for(j=0;j<width;j++)
      for (k=0; k<height;k++)
      { sat[b]=In[j][k].Saturation; b++; }
    
    Arrays.sort(sat);
   
    thresholdHue=hue[height*width/5];
    thresholdHue2=hue[2*height*width/5];
    thresholdHue3=hue[3*height*width/5];
    thresholdHue4=hue[4*height*width/5];
    
    thresholdSaturation=sat[height*width/5];
    thresholdSaturation2=sat[2*height*width/5];
    thresholdSaturation3=sat[3*height*width/5];
     thresholdSaturation4=sat[4*height*width/5];
    
    thresholdIntensity=hsi[height*width/5];
    thresholdIntensity2=hsi[2*height*width/5];
    thresholdIntensity3=hsi[3*height*width/5];
    thresholdIntensity4=hsi[4*height*width/5];
    
    if (mode==1){
    thresholdHue=hue[height*width/5];
    thresholdHue2=hue[2*height*width/5];
    thresholdHue3=hue[3*height*width/5];
    thresholdHue4=hue[4*height*width/5];
    
    thresholdSaturation=sat[height*width/5];
    thresholdSaturation2=sat[2*height*width/5];
    thresholdSaturation3=sat[3*height*width/5];
     thresholdSaturation4=sat[4*height*width/5];
    
    thresholdIntensity=hsi[height*width/5];
    thresholdIntensity2=hsi[2*height*width/5];
    thresholdIntensity3=hsi[3*height*width/5];
    thresholdIntensity4=hsi[4*height*width/5];;
    }
    else if (mode==2){
   thresholdHue=hue[height*width/5];
    thresholdHue2=hue[2*height*width/5];
    thresholdHue3=hue[3*height*width/5];
    thresholdHue4=hue[4*height*width/5];
    
    thresholdSaturation=sat[height*width/5];
    thresholdSaturation2=sat[2*height*width/5];
    thresholdSaturation3=sat[3*height*width/5];
     thresholdSaturation4=sat[4*height*width/5];
    
    thresholdIntensity=hsi[height*width/5];
    thresholdIntensity2=hsi[2*height*width/5];
    thresholdIntensity3=hsi[3*height*width/5];
    thresholdIntensity4=hsi[4*height*width/5];
    }
    BufferedImage Out = new BufferedImage(Binarize.original.getWidth(), Binarize.original.getHeight(), Binarize.original.getType());
    for(i=0;i<width;i++)
    for (j=0; j<height;j++) {
         val = new Color(original.getRGB(i, j)).getRed();
                    int alpha = new Color(original.getRGB(i, j)).getAlpha();
                    if ((In[i][j].Intensity < thresholdIntensity))  {
                    if((In[i][j].Saturation > thresholdSaturation4))
                            if((In[i][j].Hue < thresholdHue))
                                 if(segmented_colors==0)
                                    newPixel = 0;
                                 else
                                       cSeg=new Color(Color.YELLOW.getRed(),Color.YELLOW.getGreen(),Color.YELLOW.getBlue());
                            else
                                if((In[i][j].Hue < thresholdHue2))
                                        if( segmented_colors==0)
                                          newPixel = 150;
                                        else
                                            cSeg=new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue());
                               else
                                     if((In[i][j].Hue < thresholdHue3))
                                             
                                             if(segmented_colors==0)
                                                newPixel = 190;
                                             else
                                            cSeg=new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue());
                                         
                                    else
                                        if((In[i][j].Hue < thresholdHue4))
                                             if(segmented_colors==0)
                                                newPixel = 180;
                                             else
                                            cSeg=new Color(Color.ORANGE.getRed(),Color.ORANGE.getGreen(),Color.ORANGE.getBlue());
                                         else  
                                             if(segmented_colors==0)
                                                newPixel = 255;
                                             else
                                            cSeg=new Color(Color.BLUE.getRed(),Color.BLUE.getGreen(),Color.BLUE.getBlue());
                        else
                            if((In[i][j].Saturation < thresholdSaturation2))
                                        if( segmented_colors==0)
                                          newPixel = 150;
                                        else
                                            cSeg=new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue());
                                else
                                    if((In[i][j].Saturation < thresholdSaturation))
                                            if(segmented_colors==0)
                                              newPixel = 190;
                                             else
                                            cSeg=new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue());
                                    else 
                                        if((In[i][j].Saturation < thresholdSaturation3))
                                             if(segmented_colors==0)
                                                newPixel = 180;
                                             else
                                             cSeg=new Color(Color.ORANGE.getRed(),Color.ORANGE.getGreen(),Color.ORANGE.getBlue());
                                        else  
                                             if(segmented_colors==0)
                                                newPixel = 255;
                                             else
                                            cSeg=new Color(Color.BLUE.getRed(),Color.BLUE.getGreen(),Color.BLUE.getBlue());
                                   
                       
                    } else { 
                        if(In[i][j].Intensity < thresholdIntensity2)
                           if( segmented_colors==0)
                              newPixel = 150;
                            else
                                cSeg=new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue());
                        else
                            if(In[i][j].Intensity < thresholdIntensity3)
                                if(segmented_colors==0)
                                   newPixel = 190;
                                 else
                                   cSeg=new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue());
                            else
                                if((In[i][j].Intensity < thresholdIntensity4))
                                        if(segmented_colors==0)
                                             newPixel = 180;
                                         else
                                            cSeg=new Color(Color.ORANGE.getRed(),Color.ORANGE.getGreen(),Color.ORANGE.getBlue());
                                 else  
                                        if(segmented_colors==0)
                                            newPixel = 255;
                                        else
                                            cSeg=new Color(Color.BLUE.getRed(),Color.BLUE.getGreen(),Color.BLUE.getBlue());
                    }
            
                    if(segmented_colors==0)
                    newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                    else 
                        newPixel=colorToRGB(alpha,cSeg.getRed(),cSeg.getGreen(),cSeg.getBlue());
                    Out.setRGB(i, j, newPixel);
    }
  
    return Out;
 }   

   public static BufferedImage HSItoRGB (int height, int width, HSIPIXEL In[][]){
       BufferedImage Out = new BufferedImage(Binarize.original.getWidth(), Binarize.original.getHeight(), Binarize.original.getType());
int j,k;
double x,y,z,h,s,i,r,b,g;
double temp;
for (j=0;j<width;j++)
for (k=0;k<height;k++){
//Normalising
h=(double)In[j][k].Hue*Math.PI/180;
s=(double)In[j][k].Saturation/100;
i=(double)In[j][k].Intensity/255;

if (h < 2*Math.PI/3){
x=i*(1-s);
y=i*(1+((s*Math.cos(h))/ Math.cos(Math.PI/3 -h)));
z=3*i -(x+y);
b=x;
r=y;
g=z;
}
else if (h>=2*Math.PI/3 && h<4*Math.PI/3){
h=h-2*Math.PI/3;
x=i*(1-s);
y=i*(1+((s*Math.cos(h))/ Math.cos(Math.PI/3 -h)));
z=3*i -(x+y);
r=x;
g=y;
b=z;
}
else {
h=h-4*Math.PI/3;
x=i*(1-s);
y=i*(1+((s*Math.cos(h))/ Math.cos(Math.PI/3 -h)));
z=3*i -(x+y);
g=x;
b=y;
r=z;
}
/*
b=(In[j].Intensity-In[j].Intensity*In[j].Saturation)/3;
g=In[j].Hue*(In[j].Intensity-3*b)+b;
r=In[j].Intensity-g-b;
*/
int alpha = new Color(original.getRGB(j, k)).getAlpha();
int newPixel = colorToRGB(alpha, (int)(255.0*r), (int)(255.0*g), (int)(255.0*b));
Out.setRGB(j, k, newPixel);
//Out[j].Red=255*r;
//Out[j].Green=255*g;
//Out[j].Blue=255*b;
}
return Out;
}


}
