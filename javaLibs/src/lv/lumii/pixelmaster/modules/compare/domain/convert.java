package lv.lumii.pixelmaster.modules.compare.domain;


import java.awt.image.BufferedImage;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.modules.recognition.domain.ResizeImage;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author Onty
 */
public class convert {

     /**
	 * Returns smoothened and cutted an resized image
     * creates and uses boolean array of background
	 * @param source - rasterImage
     * @return RasterImage.
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    public static RasterImage modifyImage (RasterImage image) {
        lv.lumii.pixelmaster.modules.compare.domain.medFilter.medFilter(image, image, 1, 1);

        RasterImage temp= new RasterImage(image.getWidth(), image.getHeight());
        image.copyTo(temp);
        boolean binarArray[]= new boolean[temp.getWidth()*temp.getHeight()];
        int koeff = lv.lumii.pixelmaster.modules.binarization.domain.Binarization.OtsuThresholding(temp, 30, 59, 11);
        lv.lumii.pixelmaster.modules.binarization.domain.Binarization.binary(temp, temp, koeff);
        for (int i=0; i<temp.getHeight(); i++){
            for (int j=0; j<temp.getWidth(); j++){
                
                if (temp.getRGB(i*temp.getWidth()+j)==0x00FFFFFF) {
                    binarArray[i*temp.getWidth()+j]=true;
                }else {
                    binarArray[i*temp.getWidth()+j]=false;
                }
            }
        }
        image=outCut(image,binarArray);
        image = resize(image);
        return image;

    }


     /**
	 * Finds upper, lower, right and l;eft border and cuts out image
     * uses boolean array of background
	 * @param source - rasterImage, FonArray - boolean array background
     * @return RasterImage.
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static RasterImage outCut (RasterImage source, boolean[] FonArray){
        int xMin=-1, xMax=-1, yMin=-1, yMax=-1;

        //Find TOP of the picture
        //Pixel color change in coments for trace.
        for (int i=0; i<source.getHeight(); i++) {
			for (int j=0; j<source.getWidth(); j++) {
               boolean Fon =FonArray[source.getWidth()*i+j];
                if ((!Fon)&&(yMin==-1)){
                    yMin=i;
                    //System.out.println("yMin: " + i+ " " +j);
                    //source.pixels[i*source.width+j]=0x0000ff00;
               }
           }
		}

        //Find Bottom of The Picture
        for (int i=source.getHeight()-1; i>-1; i--) {
			for (int j=source.getWidth()-1; j>-1; j--) {
                boolean Fon =FonArray[source.getWidth()*i+j];
                if ((!Fon)&&(yMax==-1)){
                    yMax=i;
                    //System.out.println("yMax: " + i+ " " +j);
                    //source.pixels[i*source.width+j]=0x0000ff00;
                }
           }
		}


       //find LEFT border
       for (int i=0; i<source.getWidth(); i++) {
			for (int j=0; j<source.getHeight(); j++) {
                boolean Fon =FonArray[source.getWidth()*j+i];
                if ((!Fon)&&(xMin==-1)){
                        xMin=i;
                        //source.pixels[j*source.width+i]=0x00ff0000;
                }
            }
        }

        // Find Right Border
        for (int i=source.getWidth()-1; i>-1; i--) {
			for (int j=0; j<source.getHeight(); j++) {
                boolean Fon =FonArray[source.getWidth()*j+i];
                if ((!Fon)&&(xMax==-1)){
                        xMax=i;
                      // source.pixels[j*source.width+i]=0x00ff0000;

                }
            }
        }

        RasterImage cutted = new RasterImage(xMax-xMin+1, yMax-yMin+1);
    

        for (int i=0; i<cutted.getHeight();i++){
            for (int j=0; j<cutted.getWidth();j++){

                cutted.setRGB(i*cutted.getWidth()+j, source.getRGB(((i+yMin)*source.getWidth() +(j+xMin))));

            }
        }
        return cutted;
    }



     /**
	 * Returns image of size 810*810
	 * @param source - rasterImage
     * @return RasterImage.
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static RasterImage resize (RasterImage source){
        BufferedImage temp = ImageConverter.toBuffered(source);
        temp=ResizeImage.resizeImage(temp, 810, 810, source.getWidth(), source.getHeight());
        RasterImage temp1= new RasterImage(temp.getWidth(),temp.getHeight());
        ImageConverter.convertBufImage(temp, temp1);
        return temp1;
    }


    /**
	 * Rotates image 90 or 180 degree
	 * @param input - rasterImage
     * @param action - char ( value: 'c' = clockwise, 'a' = anticlockwise, 'f' = flip
     * @return
	 * @author Jevgenijs Sibko.
	 * @since 15.05.2009
	 */
    public static void Rotate(RasterImage input, char action){
        int len = input.getWidth() * input.getHeight();
        RasterImage tmp = new RasterImage(input.getWidth(), input.getHeight());
        input.copyTo(tmp);

        switch (action){

            case 'f':{
                for (int i=0; i<len; i++){
                    input.setRGB(i, tmp.getRGB(len-i-1));
                }
                break;
            }
            case 'a':{
                int newPos=0;
                input.resize(tmp.getHeight(), tmp.getWidth());
                for (int i=0; i<tmp.getWidth(); i++){
                    for ( int j=tmp.getHeight()-1; j>-1;j--){
                        input.setRGB(newPos, tmp.getRGB(tmp.getWidth()*j+i));
                        newPos++;
                    }
                }
                break;
            }
            case 'c':{
                int newPos=0;
                input.resize(tmp.getHeight(), tmp.getWidth());
                for (int i=tmp.getWidth()-1; i>-1; i--){
                    for ( int j=0; j<tmp.getHeight();j++){
                        input.setRGB(newPos, tmp.getRGB(tmp.getWidth()*j+i));
                        newPos++;
                    }
                }
                break;
            }
        }

    }


     /**
	 * Rotates background array 90 or 180 degree
	 * @param input - rasterImage
     * @param action - char ( value: 'c' = clockwise, 'a' = anticlockwise, 'f' = flip
     * @return
	 * @author Jevgenijs Sibko.
	 * @since 15.05.2009
	 */

    public static void Rotate(boolean[] input, char action){
        int side=810, len = side*side;
        boolean output[] = new boolean[len];
        System.arraycopy(input, 0, output, 0, len);
        switch (action){

            case 'f':{

                for (int i=0; i<len; i++){
                    input[i]=output[len-i-1];
                }
                break;
            }
            case 'a':{
                int newPos=0;
                for (int i=0; i<side; i++){
                    for ( int j=side-1; j>-1;j--){
                        input[newPos]=output[side*j+i];
                        newPos++;
                    }
                }
                break;
            }
            case 'c':{
                int newPos=0;
                for (int i=side-1; i>-1; i--){
                    for ( int j=0; j<side;j++){
                        input[newPos]=output[side*j+i];
                        newPos++;
                    }
                }
                break;
            }
        }

    }


}


