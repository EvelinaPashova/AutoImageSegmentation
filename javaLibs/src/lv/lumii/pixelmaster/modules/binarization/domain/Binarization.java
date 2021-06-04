
package lv.lumii.pixelmaster.modules.binarization.domain;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import java.util.*;
import lv.lumii.pixelmaster.core.api.domain.RGB;
import lv.lumii.pixelmaster.modules.filters.domain.MeanFilter;

/**
 * Binarization algorithms
 * @author Jevgenijs Jonass
 * @see <a href="http://www.philippovich.ru/Library/Books/ITS/wwwbook/ist4b/its4/fyodorov.htm">А. Федоров. Бинаризация</a>
 */
public final class Binarization {

	/**
	 * Returns optimal threshold which divides pixels
	 * in two classes: [0..t-1] and [t..255]
	 * 
	 * @param inputImage cannot be null
	 * @param kr Coefficient of red channel (0..100)
	 * @param kg Coefficient of green channel (0..100)
	 * @param kb Coefficient of blue channel (0..100)
	 * @pre kr+kg+kb=100
	 * @return threshold in range [1..255]
	 *
	 */
	public static int OtsuThresholding(RasterImage inputImage, int kr, int kg, int kb) {
		assert !(inputImage==null || kr<0 || kr>100 || kg<0 || kg>100 || kb<0 || kb>100 || kr+kg+kb!=100);
		int width=inputImage.getWidth(), height=inputImage.getHeight(), size=height*width;
		int histogram [] = new int [256];

		//build the histogram
		for (int i=0; i<size; i++) {
			int intensity=RGB.intensity(inputImage.getRGB(i), kr, kg, kb);
			histogram[intensity]++;
		}

		//find min and max indexes
		int minIndex=0;
		for ( ; minIndex<256; minIndex++) {
			if (histogram[minIndex]!=0) break;
		}
		assert minIndex<256;
		int maxIndex=255;
		for ( ; maxIndex>=0; maxIndex--) {
			if (histogram[maxIndex]!=0) break;
		}
		assert minIndex>=0;
		if (maxIndex==minIndex) return 1;	// impossible to divide in 2 classes

		//System.out.println("minIndex: " + minIndex + "; maxIndex: " + maxIndex);
		long n0=histogram[minIndex], n1=size-n0;	// number of elements in 2 classes
		assert n1>0 && n0>0;

		// calculate respective cluster means for threshold=minIndex+1
		// first cluster consists of pixels with intensity in range [0..minIndex],
		// second cluster consists of pixels with intensity in range [minIndex+1..255]
		double u0=(minIndex+1)*histogram[minIndex]/n0;
		double u1=0;
		for (int i=minIndex+1; i<=maxIndex; i++) {
			u1+=histogram[i]*(i+1);
		}
		u1/=n1;
		
		// find optimal threshold
		int threshold=minIndex+1;
		double maxVariance=n0*n1*(u0-u1)*(u0-u1);
		//System.out.println("maxVariance: " + maxVariance);
		for (int i=minIndex+2; i<=maxIndex; i++) {
			if (histogram[i-1]==0) continue;
			n0+=histogram[i-1];
			n1-=histogram[i-1];
			u0=(u0*(n0-histogram[i-1])+histogram[i-1]*(i))/(n0);
			u1=(u1*(n1+histogram[i-1])-histogram[i-1]*(i))/(n1);
			//System.out.println("n0=" + n0 + "; n1=" + n1 + "; u0=" + u0 + "; u1=" + u1);
			double variance=n0*n1*(u0-u1)*(u0-u1);
			if (variance>maxVariance) {
				maxVariance=variance;
				threshold=i;
			}
		}
		return threshold;
	}

	/**
	 * Creates a binary image using default kr, kg and kb.
	 * Threshold will divide pixel intensity in two classes: [0..t-1] and [t..255].
	 * The inputImage and target may point to the same objects.
	 *
	 * @param inputImage The original image, cannot be null
	 * @param target The binary image, cannot be null
	 * @param threshold threshold parameter (must be in range [0..255])
	 * @pre inputImage.getWidth()=target.getWidth() and inputImage.getHeight()=target.getHeight()
	 */
	public static void binary(RasterImage inputImage, RasterImage target, int threshold) {
		binary(inputImage, target, threshold, RGB.DEFAULT_RED, RGB.DEFAULT_GREEN, RGB.DEFAULT_BLUE);
	}

	/**
	 * Creates a binary image using given threshold and kr, kg, kb.
	 * Threshold will divide pixel intensity in two classes: [0..t-1] and [t..255].
	 * The inputImage and target may point to the same objects.
	 * 
	 * @param inputImage The original image, cannot be null
	 * @param target The binary image, cannot be null
	 * @param threshold integer in range [0..255]
	 * @param kr Coefficient of red channel (0..100)
	 * @param kg Coefficient of green channel (0..100)
	 * @param kb Coefficient of blue channel (0..100)
	 * @pre kr+kg+kb=100
	 * @pre inputImage.getWidth() == target.getWidth() and inputImage.getHeight()==target.getHeight()
	 */
	public static void binary(RasterImage inputImage, RasterImage target, int threshold, int kr, int kg, int kb) {
		assert !(inputImage==null || target==null || threshold<0 || threshold>255 ||
			inputImage.getWidth()!=target.getWidth() || inputImage.getHeight()!=target.getHeight() ||
			kr<0 || kr>100 || kg<0 || kg>100 || kb<0 || kb>100 || kr+kg+kb!=100);
		//	target.getWidth()=inputImage.getWidth();
		//	target.getHeight()=inputImage.getHeight();
		int size=inputImage.getHeight()*inputImage.getWidth();
		for(int k=0; k<size; k++) {
			int rgb=inputImage.getRGB(k);
			if (RGB.intensity(rgb, kr, kg, kb)<threshold) target.setRGB(k, 0);
			else target.setRGB(k, 0x00ffffff);
		}
	}

	/**
	 * Niblack algorithm. Does not process edges.
	 * The inputImage and target may point to the same objects.
	 *
	 * @param inputImage The original image, cannot be null
	 * @param target The binary image, cannot be null
	 * @param kr Coefficient of red channel (0..100)
	 * @param kg Coefficient of green channel (0..100)
	 * @param kb Coefficient of blue channel (0..100)
	 * @param radius non-negative integer
	 * @param k coefficient in range [-infinity..infinity]
	 * @pre kr+kg+kb=100
	 * @pre inputImage.getWidth() == target.getWidth() and inputImage.getHeight()==target.getHeight()
	 */
	public static void NiblackBinarization(RasterImage inputImage, RasterImage target,
		int kr, int kg, int kb, int radius, double k) {
		assert !(inputImage==null || target==null ||
			inputImage.getWidth()!=target.getWidth() || inputImage.getHeight()!=target.getHeight() ||
			kr<0 || kr>100 || kg<0 || kg>100 || kb<0 || kb>100 || kr+kg+kb!=100 || radius<0
		);
		int width=inputImage.getWidth(), height=inputImage.getHeight();
		if (2*radius+1>width || 2*radius+1>height) return;
		if (inputImage==target) inputImage=new RasterImage(inputImage);
		RasterImage grayImage=new RasterImage(inputImage.getWidth(), inputImage.getHeight());
		RasterImage meanFilteredImage=new RasterImage(inputImage.getWidth(), inputImage.getHeight());
		RasterImage variance=new RasterImage(inputImage.getWidth(), inputImage.getHeight());
		GrayScale.greyImage(inputImage, grayImage, kr, kg, kb);
		MeanFilter.meanFilter(grayImage, meanFilteredImage, radius, 0, null);
		MeanFilter.varianceFilter(grayImage, variance, meanFilteredImage, radius, radius, null);
		
		for (int i=radius; i<width-radius; i++) {
			for (int j=radius; j<height-radius; j++) {
				double threshold=RGB.getR(meanFilteredImage.getRGB(i, j))+
					k*RGB.getR(variance.getRGB(i, j));
				int intensity=RGB.getR(grayImage.getRGB(i, j));
				if (intensity>=threshold) target.setRGB(i, j, 0x00ffffff);
				else target.setRGB(i, j, 0);
			}
		}
	}

	/**
	 * Bernsen algorithm. Does not process edges.
	 * If maxIntensity-minIntensity&gt;=threshold,
	 * pixel becomes black. The inputImage and target may point to the same objects.
	 * 
	 * @param inputImage The original image, cannot be null
	 * @param target The binary image, cannot be null
	 * @param kr Coefficient of red channel (0..100)
	 * @param kg Coefficient of green channel (0..100)
	 * @param kb Coefficient of blue channel (0..100)
	 * @param radius (must be &gt;=0)
	 * @param threshold contrast threshold. Must be in range [0..255].
	 * @pre kr+kg+kb=100
	 * @pre inputImage.getWidth() == target.getWidth() and inputImage.getHeight()==target.getHeight()
	 */
	public static void BernsenBinarization(RasterImage inputImage, RasterImage target,
		int kr, int kg, int kb, int radius, int threshold) {
		//System.out.println("Bernsen");
		assert !(inputImage==null || target==null ||
			inputImage.getWidth()!=target.getWidth() || inputImage.getHeight()!=target.getHeight() ||
			kr<0 || kr>100 || kg<0 || kg>100 || kb<0 || kb>100 || kr+kg+kb!=100 || radius<0
		);
		int width=inputImage.getWidth(), height=inputImage.getHeight();
		if (2*radius+1>width || 2*radius+1>height) return;
		if (inputImage==target) inputImage=new RasterImage(inputImage);
		RasterImage grayImage=new RasterImage(inputImage.getWidth(), inputImage.getHeight());
		GrayScale.greyImage(inputImage, grayImage, kr, kg, kb);

		int[] arr= new int[(radius*2+1)*(radius*2+1)];
		for (int i=radius; i<width-radius; i++) {
			for (int j=radius; j<height-radius; j++) {
				int count=0;
				for (int R1=(-radius); R1<radius+1; R1++ ) {
				for (int R2=(-radius); R2<radius+1; R2++) {
						arr[count++]=RGB.getR(grayImage.getRGB(j*width+i+(R1*width)+R2));
					}
				}
				Arrays.sort(arr);
				int min=arr[0], max=arr[arr.length-1];
				int newValue=RGB.getRGB(RGB.getR(grayImage.getRGB(i, j)), min, max);
				grayImage.setRGB(i, j, newValue);
			}
		}
		for (int i=radius; i<width-radius; i++) {
			for (int j=radius; j<height-radius; j++) {
				int var=RGB.getB(grayImage.getRGB(i, j))-RGB.getR(grayImage.getRGB(i, j));
				if (var<threshold) target.setRGB(i, j, 0x00ffffff);
				else target.setRGB(i, j, 0);
			}
		}
	}
}
