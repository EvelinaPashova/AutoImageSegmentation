package com.image.grayscale;

/*
 *
 * Description/Описание:
 * Converts color image into grayscale image
 *
 * Преобразува цветно изображение в черно-бяло
 *
 * Date/Дата: 05-06-2015
 *
 */

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Grayscale{
   // @SuppressWarnings({"UnusedAssignment", "null"})
  /*  public static void main(String args[])throws IOException{
        BufferedImage img = null;
        File f;
        f = null;
        
        //отваря изображение и го запаметява в паметта
        try{
            f = new File("C:\\Output.jpg");
            img = ImageIO.read(f);
        }catch(IOException e){
            System.out.println(e);
        }
        
        @SuppressWarnings("null")
        int width;
        width = img.getWidth();
        int height;
        height = img.getHeight();
        
        //конвертира в черно-бяло
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);
                
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;
                
                //пресмята средна стойност
                int avg = (r+g+b)/3;
                
                //сменя стойностите на RGB с avg
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;
                
                img.setRGB(x, y, p);
            }
        }
        
        //създава ново изображение
        try{
            f = new File("C:\\Output1.jpg");
            ImageIO.write(img, "jpg", f);
        }catch(IOException e){
            System.out.println(e);
        }
    }*/
}
