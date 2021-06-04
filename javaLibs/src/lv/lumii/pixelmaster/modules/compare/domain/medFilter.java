/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lv.lumii.pixelmaster.modules.compare.domain;


import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import java.util.Arrays;

/**
	 * Median filter. Must be used separately for each RGB component. Bit shift
	 * specifies the RGB component. If pixel doesn't exist it wil be mirrored from
     * the source pixel.
	 *
	 * @param inputImage The original image (ownership: caller)
	 * @param target The target image (ownership: caller)
	 * @param radius filtering radius (must be &gt;= 0)
	 * @param diff Only those pixels are replaced with the mean value, whose
	 *		value differs from the mean value by more than diff. (must be &gt;= 0)
	 * @pre inputImage != null and target != null and target != inputImage and inputImage.pixels != target.pixels
	 * @pre class invariants of inputImage and target are true
	 * @pre inputImage.getWidth() == target.getWidth() and inputImage.getHeight()==target.getHeight()
	 * @pre inputImage.getWidth() &gt;= 2*r+1 and inputImage.getHeight() &gt;= 2*r+1
	 * @pre selection != null =&gt; inputImage.inBounds(selection)
	 * @post Does not modify inputImage
	 * @author Jevgenijs Shibko.
	 * @since 21.05.2009
	 */
public class medFilter {
    public static void medFilter(RasterImage source,RasterImage target, int radius, int diff){

        assert !(
			source==null || target==null || radius<0 ||
			source.getWidth()!=target.getWidth() || source.getHeight()!=target.getHeight() || diff<0
		);

		int width=source.getWidth(), height=source.getHeight();
		if (width<2*radius+1 || height<2*radius+1/* || r==0*/) {
			if (source!=target) source.copyTo(target);
			return;
		}
		if (source==target) {
			source=new RasterImage(source);
		}
        
        medFilter(source,target,radius,diff,'r');
        medFilter(source,target,radius,diff,'g');
        medFilter(source,target,radius,diff,'b');
        



     /**
     * Median filter for every RGB component.
	 * @param RasterImage - image that will be filtered
     * @param radius - amount of pixels from the source pixel that will be
     *     used for filter mask
     * @param diff - Only those pixels are replaced with the mean value, whose
	 *		value differs from the median value by more than diff.
     * @param color - specifies which component is filtered.
     * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */

    }
    private static void medFilter(RasterImage source, RasterImage target, int radius, int diff, char color){
        int shift=0, col=0;

        switch (color){
                case ('r'):{shift=16;break;}
                case ('g'):{shift=8;break;}
                case ('b'):{shift=0;break;}
                default:{break;}
         }

         for (int i=0; i<source.getWidth(); i++){
             for (int j=0;j<source.getHeight();j++){
                 int[] arr= new int[(radius*2+1)*(radius*2+1)];
                 int count=0,kR1=1,kR2=1;
                 //for every pixel goes trough pixel mask.
                 for (int R1=(-radius);R1<radius+1;R1++ ){
                     for (int R2=(-radius);R2<radius+1;R2++){
                         //if pixel from mask doesn't exist in image then mirors it from the source pixel
                         //uses koefficients for that.
                        if ((R2+i<0)||(R2+i>(source.getWidth()-1))){kR2=(-1);}
                        else{kR2=1;}
                        if ((R1+j<0)||(R1+j>(source.getHeight()-1))){kR1=(-1);}
                        else{kR1=1;}
                        arr[count]=(source.getRGB(j*source.getWidth()+i+(R1*kR1*source.getWidth())+(R2*kR2))>>shift) & 255;
                        count++;

                     }
                 }
                 col=getMedian(arr);
                 if (Math.abs(((target.getRGB(j*target.getWidth()+i)>>shift) & 255)-col)>diff){
                     target.setRGB(j*source.getWidth()+i, target.getRGB(j*source.getWidth()+i) & (~(255 << shift)));
                     target.setRGB(j*source.getWidth()+i, target.getRGB(j*source.getWidth()+i) | ((col << shift)));
                 }
             }
         }

    }


    /**
     * Finds median value of the array
     * @param int array.
     * @return median value - int
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static int getMedian(int[] arr){
        Arrays.sort(arr);
        int median = arr.length/2;
       
        return arr[median];

    }

}