package lv.lumii.pixelmaster.modules.compare.domain;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.RGB;
/**
  * @author Jevgenijs Sibko
 */

public  class CompareByPixelBlocks {

    //block size 1,3,9,27,81
    private final static int blockSize = 9;
    private final static int imSize = 810;
    private static boolean sourceFon[] = new boolean[imSize*imSize];
    private static boolean targetFon[] = new boolean[imSize*imSize];
    /**
	 * Returns image of size 810*810 with colored equal blocks.
	 * @param source - rasterImage, target - rasterImage
     * @return RasterImage.
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    public static RasterImage CompareObj(RasterImage source, RasterImage target, StringBuffer info){
    if ((source!=null)&&(target!=null)){
        RasterImage workSource = new RasterImage(source.getWidth(), source.getHeight());
        RasterImage workTarget = new RasterImage(target.getWidth(), target.getHeight());
        source.copyTo(workSource);
        target.copyTo(workTarget);

        workSource= convert.modifyImage(workSource);
        workTarget= convert.modifyImage(workTarget);

        FillFonArray(sourceFon,workSource);
        FillFonArray(targetFon,workTarget);
        

        
        int arr[] = findCompare(workSource,workTarget);
        float[] val = findTop(workSource,workTarget);
        int count=compare(workSource,workTarget,'n');

        System.out.println("value1: "+arr[1]);
        System.out.println("value2: "+count);
        if (arr[1]>count){
            source.copyTo(workSource);
            workSource= convert.modifyImage(workSource);
            FillFonArray(sourceFon,workSource);

            switch (arr[0]){
                case 0:{
                    break;
                }

                case 1:{
                    convert.Rotate(workSource, 'a');
                    convert.Rotate(sourceFon, 'a');
                    break;
                }

                case 2:{
                    convert.Rotate(workSource, 'f');
                    convert.Rotate(sourceFon, 'f');
                    break;
                }
                case 3:{
                    convert.Rotate(workSource, 'c');
                    convert.Rotate(sourceFon, 'c');
                    break;
                }
                default:{
                    break;
                }

            }
            compare(workSource,workTarget,'c');
            if (info!=null){
                info.append(arr[1]+"% are even\n");
                info.append("\n\nAction performed:\n");
                switch (Math.round(arr[0])){
                    case 1:{info.append("rotated 90deg \n clockwise\n"); break;}
                    case 2:{info.append("rotated 180deg \n"); break;}
                    case 3:{info.append("rotated 90deg \n anticlockwise\n"); break;}
                    default:{info.append("None\n"); break;}
                }
                info.append("\nObjects are:");
                if (arr[1]>85){
                    info.append("\n Same.");
                }else if ((arr[1]<85)&&(arr[1]>60)){
                    info.append("\nUndefined");
                }else{
                    info.append("\nDifferent");
                }
            }
        }else{
            compare(workSource,workTarget,'c');
            if (info!=null){
            info.append(count+"% are even\n");
            info.append("\nIntens Diff:\n"+val[0]);
            info.append("\n\nAction performed:\n");
            switch (Math.round(val[1])){
                case 1:{info.append("rotated 90deg \n clockwise\n"); break;}
                case 2:{info.append("rotated 180deg \n"); break;}
                case 3:{info.append("rotated 90deg \n anticlockwise\n"); break;}
                default:{info.append("None\n"); break;}
            }

            info.append("\nObjects are:");
                if (count>85){
                    info.append("\n Same.");
                }else if ((count<85)&&(count>60)){
                    info.append("\nUndefined");
                }else{
                    info.append("\nDifferent");
                }

          }




        }
             

   
        
        return workSource;
    }
    else return null;

    }

     /**
     * converts imege to blockimage with block = mid color
     * compares blocks from first image and second
     * returns  amount of equal blocks
      * @see <atantion> algoritm works bad with 1 color and 1 binar images! <atantion>
	 * @param source - rasterImage, target - rasterImage
     * @param action -char, if = 'c' then color equal zones, else not.
     * @return int - count of blocks that equals and are not fon.
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static int compare (RasterImage source, RasterImage target, char action){
        
        
        int count=0, blockCount=0, first=0, second=0;
        RasterImage temp= new RasterImage(imSize,imSize);
        for (int i=0; i<source.getHeight(); i=i+blockSize) {
			for (int j=0; j<source.getWidth(); j=j+blockSize) {

                first=getBlock(i*source.getWidth()+j, source);
                second=getBlock(i*source.getWidth()+j, target);
                boolean Fon=(isFon(i*source.getWidth()+j,sourceFon)&&
                                isFon(i*source.getWidth()+j,targetFon));
                if (!Fon) {blockCount++;}
                if ((colorEven(first,second))&&(!Fon)){
                    count++;
                    colorBlock(i*source.getWidth()+j,temp,first);
                    
                }
                else {colorBlock(i*source.getWidth()+j,temp,0x00FFFFFF);}
           }
		}
        count = count*100/blockCount;
                if (action=='c'){
            temp.copyTo(source);
        }
    return count;
    }

    /**
     * if color doesn't differ by more then 5 at each and every component then
     * it's even
	 * @param  int - color1. int - color 2
     * @return boolean - true or not
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static boolean colorEven(int color1, int color2){
        int redVal = Math.abs(RGB.getR(color1)-RGB.getR(color2));
        int greenVal = Math.abs(RGB.getG(color1)-RGB.getG(color2));
        int blueVal = Math.abs(RGB.getB(color1)-RGB.getB(color2));
        boolean answ=true;
        if ((blueVal<5)&&(redVal<5)&&(greenVal<5)){
            answ = true;
        }else{
            answ = false;
        }
        return answ;
    }

    /**
     * Rotates image 4 times and compares to other images
	 * @param  RasterImage - rotated, RasterImage - example
     * @return equal% and rotation action
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static int[] findCompare(RasterImage source, RasterImage target){
        int[] answer = {0,0};
        int tmp=0;
        convert.Rotate(source, 'c');
        convert.Rotate(sourceFon, 'c');
        for (int i=0; i<4; i++){
            convert.Rotate(source, 'a');
            convert.Rotate(sourceFon, 'a');
            tmp = compare(source,target,'n');
            if (tmp>answer[1]){
                answer[0]=i;
                answer[1]=tmp;
            }
        }
     convert.Rotate(source, 'a');
     convert.Rotate(sourceFon, 'a');
  



        return answer;
    }

    /**
     * gets Mid Block color
	 * @param start - position of the block (upper top corner), RasterImage
     * @return Mid RGB color
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static int getBlock(int start, RasterImage source){
        int color=0,rMid=0,gMid=0,bMid=0;
        for (int i=0; i<blockSize;i++){
            for (int j=0; j<blockSize;j++){
               rMid+=RGB.getR(source.getRGB(start+(source.getWidth()*i)+j));
               gMid+=RGB.getG(source.getRGB(start+(source.getWidth()*i)+j));
               bMid+=RGB.getB(source.getRGB(start+(source.getWidth()*i)+j));
            }
        }
        color=RGB.getRGB(rMid/pow(blockSize), gMid/pow(blockSize), bMid/pow(blockSize));
        
        //for testing
        /*for (int i=0; i<blockSize;i++){
            for (int j=0; j<blockSize;j++){
               source.setRGB(start+(source.getWidth()*i)+j, color);
            }
        }
        */
        return color;
    }

    /**
     * sets color to whole block
	 * @param start - position of the block (upper top corner), RasterImage, color
     * @return
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static void colorBlock(int start, RasterImage tmp,int color){

        for (int i=0; i<blockSize;i++){
            for (int j=0; j<blockSize;j++){
               tmp.setRGB(start+(tmp.getWidth()*i)+j, color);
            }
        }
   }
    /**
     * sets value to background array,
     * if one part is not white then whole block in not background
	 * @param start - position of the block (upper top corner), Background array, bool value
     * @return
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
   private static void colorBlock(int start, boolean[] FonArr, boolean val){
      for (int i=0; i<blockSize;i++){
            for (int j=0; j<blockSize;j++){
               FonArr[start+(imSize*i)+j]=val;
            }
        }
   }

    /**
     * Square of the number
	 * @param number - int
     * @return Square of the number
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static int pow(int num){
        return num*num;
    }

   /**
     * checks if the position is background or not
	 * @param color - int
     * @return boolean
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static boolean isFon(int pos, boolean[] FonArr){
        return FonArr[pos];
    }

    /**
     * Rotates image so that source image would be positioned
     * with minimal intensity diffarance on the top,
     * compared to intensity on target image
	 * @param RasterImage, RasterImage
     * @return diffNumber - int which is min diffarance in color intensity
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static float[] findTop(RasterImage source, RasterImage target){

        float Val[]= {0,0,0,0};
        float TopValue=0;
        
        for (int i=0; i<(source.getHeight()/2); i=i+blockSize) {
            for (int j=0; j<source.getWidth(); j=j+blockSize) {
               if (!sourceFon[i*source.getWidth()+j]) Val[0]+=RGB.intensity(getBlock(i*source.getWidth()+j, source));
               if (!sourceFon[(i+source.getHeight()/2)*source.getWidth()+j])
                        Val[2]+=RGB.intensity(getBlock((i+source.getHeight()/2)*source.getWidth()+j, source));
               if (!targetFon[i*target.getWidth()+j]) TopValue+=RGB.intensity(getBlock(i*target.getWidth()+j, target));
            }
        }

        for (int i=0; i<source.getWidth()/2;i=i+blockSize){
            for (int j=0; j<source.getHeight();j=j+blockSize){
                if (!sourceFon[j*source.getWidth()+i]) Val[1]+=RGB.intensity(getBlock(j*source.getWidth()+i, source));
                if (!sourceFon[j*source.getWidth()+(i+source.getWidth()/2)])
                        Val[3]+=RGB.intensity(getBlock(j*source.getWidth()+(i+source.getWidth()/2), source));
            }
        }
        
        
        Val[0]=Val[0]/((imSize/blockSize)*(imSize/blockSize/2));
        Val[1]=Val[1]/((imSize/blockSize)*(imSize/blockSize/2));
        Val[2]=Val[2]/((imSize/blockSize)*(imSize/blockSize/2));
        Val[3]=Val[3]/((imSize/blockSize)*(imSize/blockSize/2));
        TopValue=TopValue/((imSize/blockSize)*(imSize/blockSize/2));
        float min[]={255,0};
        for (int i=0;i<4;i++){
            if (Math.abs(Val[i]-TopValue)<min[0]){
                min[0]=Math.abs(Val[i]-TopValue);
                min[1]=i;
            }
        }

        System.out.println("Value TOP: "+Val[0]);
        System.out.println("Value Bottom: "+Val[2]);
        System.out.println("Value Left: "+Val[1]);
        System.out.println("Value Right: "+Val[3]);
        System.out.println("Value TOP new: "+TopValue);
        System.out.println("Min: "+min[1]+" Actio: "+min[0]);
        switch (Math.round(min[1])){
            case 0:{
                break;
            }

            case 1:{
                convert.Rotate(source, 'a');
                convert.Rotate(sourceFon, 'a');
                break;
            }

            case 2:{
                convert.Rotate(source, 'f');
                convert.Rotate(sourceFon, 'f');
                break;
            }
            case 3:{
                convert.Rotate(source, 'c');
                convert.Rotate(sourceFon, 'c');
                break;
            }
            default:{
                break;
            }

        }
        
        return min;
    }

    
    /**
     *Fills background array by using binarization
     * Optimizes background array by blocks
     * @param boolean[] - bakcground array, RasterImage
     * @return
	 * @author Jevgenijs Sibko.
	 * @since 13.05.2009
	 */
    private static void FillFonArray(boolean [] FonArray, RasterImage source){
        RasterImage temp= new RasterImage(imSize,imSize);
        source.copyTo(temp);
        int koeff = lv.lumii.pixelmaster.modules.binarization.domain.Binarization.OtsuThresholding(temp, 30, 59, 11);
        lv.lumii.pixelmaster.modules.binarization.domain.Binarization.binary(temp, temp, koeff);
        for (int i=0; i<source.getHeight(); i=i+blockSize) {
			for (int j=0; j<source.getWidth(); j=j+blockSize) {
                 int color = getBlock(i*temp.getWidth()+j, temp);
                 if (color==0x00FFFFFF){
                     colorBlock(i*temp.getWidth()+j,FonArray,true);
                 }else{
                     colorBlock(i*temp.getWidth()+j,FonArray,false);
                 }

            }
        }
    }



}
