package lv.lumii.pixelmaster.modules.search.domain;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.modules.search.domain.imagePyramid;

/**
 * Class provides methods for calculating normalized gray-scale
 * correlation between search and target images
 * @author Janis Kozulis
 * @since 23.04.2009
 */
public class ngcCorrelation {
	
	/**
	 * Calculates exact target image position in search image
	 * @author Janis Kozulis
	 * @param searchImage image pyramid
	 * @param targetImage image pyramid
	 * @return the resulting image, or <code>null</code> if the target image was
	 *		not found in the search image.
	 */
	public static RasterImage ngcCorrelation(imagePyramid searchImage, imagePyramid targetImage) {

		double thresholdScore = 0.5;
		double [] correlationScore = new double[3];

		correlationScore = ngcCorrelation.Correlation(searchImage.imageLevel2, targetImage.imageLevel2, correlationScore, false);
		if (correlationScore[0] < thresholdScore) return null;

		correlationScore = ngcCorrelation.Correlation(searchImage.imageLevel1, targetImage.imageLevel1, correlationScore, true);
		if (correlationScore[0] < thresholdScore) return null;

		correlationScore = ngcCorrelation.Correlation(searchImage.imageLevel0, targetImage.imageLevel0, correlationScore, true);
		if (correlationScore[0] < thresholdScore) return null;

		RasterImage tmp = new RasterImage(searchImage.imageOriginal);
		ngcCorrelation.drawLines(tmp, targetImage.imageOriginal, correlationScore);
		return tmp;
	}
	
	/**
	 * Calculates correlation coefficient between target and search images
	 * @author Janis Kozulis
	 * @param searchImage raster image
	 * @param targetImage raster image
	 * @param 
	 * @return result double array
	 * @since 19.05.2009
	 */
	private static double [] Correlation(RasterImage searchImage, RasterImage targetImage,double [] startPosition, boolean check){
		double [] result = new double[3];
		result[0] = 0; // correlation coeficient 
		result[1] = 0; // x coordinate
		result[2] = 0; // y coordinate
		double Tx = 0;
		double Sx = 0;
		double T = 0;
		double S = 0;
		double U = 0;
		double D = 0;
		double Dr = 0;
		double Dl = 0;
		double tmp = 0;
		int startX = 0;
		int startY = 0;
		int tempW = searchImage.getWidth() - targetImage.getWidth() + 1;
		int tempH = searchImage.getHeight() - targetImage.getHeight() + 1;
		//System.out.println("startPos: " +startPosition[1]+" - "+startPosition[2]);
		if(startPosition[1]!=0 || startPosition[2]!=0){
			startX = (((int)startPosition[1])*2)-1;
			startY = (((int)startPosition[2])*2)-1;
			if(startX<0){
				startX = 0;
			}
			if(startY<0){
				startY = 0;
			}
		}
		
		if(check == true){
			tempW = startX+1;
			tempH = startY+1;
		}
		
		for(int i=startY; i<tempH; i++) {
			for(int j=startX; j<tempW; j++) {
				for(int a=0; a<targetImage.getHeight(); a++) {
					for(int b=0; b<targetImage.getWidth(); b++) {
						Sx = Sx+((searchImage.getRGB(i*searchImage.getWidth()+j+a*searchImage.getWidth()+b))
							/(targetImage.getWidth()*targetImage.getHeight()));
						Tx = Tx+((targetImage.getRGB(a*targetImage.getWidth()+b))
							/(targetImage.getWidth()*targetImage.getHeight()));
					}
				}
				for(int k=0; k<targetImage.getHeight(); k++) {
					for(int l=0; l<targetImage.getWidth(); l++) {
						S = searchImage.getRGB(i*searchImage.getWidth()+j+k*searchImage.getWidth()+l);
						T = targetImage.getRGB(k*targetImage.getWidth()+l);
						U = U + (S-Sx)*(T-Tx);
						Dl = Math.pow((S-Sx),2);
						for(int c=0; c<targetImage.getHeight(); c++) {
							for(int d=0; d<targetImage.getWidth(); d++) {
								Dr = Math.pow((T-Tx),2);
								D = D + Dl*Dr;
							}
						}
					}
				}
				tmp = U/Math.sqrt(D);
				if(tmp > result[0]){
					result[0] = tmp;
					result[1] = j;
					result[2] = i;
				}
				Sx = 0;
				Tx = 0;
				S = 0;
				T = 0;
				U = 0;
				D = 0;
				Dl = 0;
				Dr = 0;
			}
		}
		//System.out.println("res " +result[0]+" : "+result[1] +" x "+result[2] );
		return result;
	}
	
	/**
	 * Draws a target images location in search image
	 * @author Janis Kozulis
	 * @param searchImage raster image
	 * @param targetImage raster image
	 * @param correlationScore
	 * @since 19.05.2009
	 */
	private static void drawLines(RasterImage searchImage, RasterImage targetImage, double [] correlationScore){
		int startPos = (int)correlationScore[2]*searchImage.getWidth() + (int)correlationScore[1];
		System.out.println("Start drawing lines!");
		for(int i=0; i<targetImage.getWidth(); i++) {
			searchImage.setRGB(startPos+i, 0x00ff0000);
			searchImage.setRGB(searchImage.getWidth()*(targetImage.getHeight()-1)+startPos+i, 0x00ff0000);
		}
		for(int l=0; l<targetImage.getHeight()-2; l++) {
			searchImage.setRGB(searchImage.getWidth()*l+searchImage.getWidth()+startPos, 0x00ff0000);
			searchImage.setRGB(targetImage.getWidth()-1+searchImage.getWidth()*l+searchImage.getWidth()+startPos, 0x00ff0000);
		}
	}

}
