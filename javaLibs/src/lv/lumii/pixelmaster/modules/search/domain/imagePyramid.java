package lv.lumii.pixelmaster.modules.search.domain;

import lv.lumii.pixelmaster.core.api.domain.RGB;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.modules.binarization.domain.GrayScale;

/**
 * Class provides image pyramid for NGC and CHC searches
 * Pyramid consists of 3 levels, each level is 2 times smaller than prewious to reduce computing time
 * @author Janis Kozulis
 * @since 17.04.2009
 */
public class imagePyramid {
	
	public RasterImage imageOriginal;
	public RasterImage imageLevel0;
	public RasterImage imageLevel1;
	public RasterImage imageLevel2;
	public double [] chLevel0x;
	public double [] chLevel0y;
	public double [] chLevel1x;
	public double [] chLevel1y;
	public double [] chLevel2x;
	public double [] chLevel2y;
	
	/**
	 * Does not allow to create objects with default constructor to avoid errors.
	 * @author Janis Kozulis
	 * @since 17.04.2009
	 */
	private imagePyramid() {}
	
	/**
	 * Constructor creates image pyramid
	 * @author Janis Kozulis
	 * @param rImage raster image
	 * @since 17.04.2009
	 */
	public imagePyramid(RasterImage rImage){
		
		imageOriginal = rImage;
		imageLevel0 = new RasterImage(imageOriginal.getWidth(), imageOriginal.getHeight());
		GrayScale.greyImage(imageOriginal, imageLevel0);
		imageLevel1 = createLevel(imageLevel0);
		imageLevel2 = createLevel(imageLevel1);
		chLevel0x = createHistogramX(imageLevel0);
		chLevel0y = createHistogramY(imageLevel0);
		chLevel1x = createHistogramX(imageLevel1);
		chLevel1y = createHistogramY(imageLevel1);
		chLevel2x = createHistogramX(imageLevel2);
		chLevel2y = createHistogramY(imageLevel2);	
	}
	
	/**
	 * Creates levels for pyramid
	 * @author Janis Kozulis
	 * @param rImage raster image
	 * @return tmp raster image
	 * @since 17.04.2009
	 */
	private RasterImage createLevel(RasterImage rImage){
		RasterImage tmp = new RasterImage(rImage.getWidth()/2, rImage.getHeight()/2);
		//System.out.println("size "+tmp.width+" x "+tmp.height);
		int gray;
		for(int j=0;j<tmp.getHeight();j++)
	    for(int i=0;i<tmp.getWidth();i++){
	    	gray = (RGB.getR(rImage.getRGB(2*j*rImage.getWidth()+2*i))
	    	       +RGB.getR(rImage.getRGB(2*j*rImage.getWidth()+2*i+1))
	    	       +RGB.getR(rImage.getRGB((2*j+1)*rImage.getWidth()+2*i))
	    	       +RGB.getR(rImage.getRGB((2*j+1)*rImage.getWidth()+2*i+1)))/4;
	    	tmp.setRGB(j*tmp.getWidth()+i, RGB.getRGB(gray, gray, gray));
	    }
		return tmp;
	} 
	
	/**
	 * Creates histogram of images x axis
	 * @author Janis Kozulis
	 * @param rImage raster image
	 * @return tmp double array
	 * @since 17.04.2009
	 */
	private double [] createHistogramX(RasterImage rImage){
		double [] tmp = new double[rImage.getHeight()];
		for(int j=0;j<rImage.getHeight();j++){
			for(int i=0;i<rImage.getWidth();i++){
				tmp[j]=tmp[j]+rImage.getRGB(j*rImage.getWidth()+i);
			}
			tmp[j]=tmp[j]/rImage.getWidth();
		}
		return tmp;
	}
	
	/**
	 * Creates histogram of images y axis
	 * @author Janis Kozulis
	 * @param rImage raster image
	 * @return tmp double array
	 * @since 17.04.2009
	 */
	private double [] createHistogramY(RasterImage rImage){
		double [] tmp = new double[rImage.getWidth()];
		for(int i=0;i<rImage.getWidth();i++){
			for(int j=0;j<rImage.getHeight();j++){
				tmp[i]=tmp[i]+rImage.getRGB(j*rImage.getWidth()+i);
			}
			tmp[i]=tmp[i]/rImage.getHeight();
		}
		return tmp;
	}
	
}