
package lv.lumii.pixelmaster.modules.filters.domain;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import javax.imageio.ImageIO;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 * A set of test suites for the class "MedianFilter".
 *
 * @author Jonas
 */
public class MedianFilterTest {

	/**
	 * Test method "medianFilter" with radius 1.
	 */
	@Test public void s1t1() throws IOException {
		RasterImage exp=new RasterImage(740, 515);
		ImageConverter.convertBufImage(ImageIO.read(new File(
			"testdata/MedianFilterTest/s1t1.filtered.gray.paint.bmp")), exp
		);
		RasterImage actual=new RasterImage(740, 515);
		ImageConverter.convertBufImage(ImageIO.read(new File(
			"testdata/MedianFilterTest/s1t1.orig.gray.bmp")), actual
		);
		MedianFilter.medianFilter(actual, actual, 1, 0, null);
		assertEquals(exp, actual);
	}

	/**
	 * Test method "medianFilter" with radius 5.
	 */
	@Test public void s1t2() throws IOException {
		RasterImage exp=new RasterImage(695, 488);
		ImageConverter.convertBufImage(ImageIO.read(new File(
			"testdata/MedianFilterTest/s1t2.filtered.gray.paint.bmp")), exp
		);
		RasterImage actual=new RasterImage(695, 488);
		ImageConverter.convertBufImage(ImageIO.read(new File(
			"testdata/MedianFilterTest/s1t2.orig.gray.bmp")), actual
		);
		MedianFilter.medianFilter(actual, actual, 5, 0, null);
		assertEquals(exp, actual);
	}

	/**
	 * Test method "medianFilter" with radius 20.
	 */
	@Test public void s1t3() throws IOException {
		RasterImage exp=new RasterImage(402, 588);
		ImageConverter.convertBufImage(ImageIO.read(new File(
			"testdata/MedianFilterTest/s1t3.filtered.gray.paint.bmp")), exp
		);
		RasterImage actual=new RasterImage(402, 588);
		ImageConverter.convertBufImage(ImageIO.read(new File(
			"testdata/MedianFilterTest/s1t3.orig.gray.bmp")), actual
		);
		MedianFilter.medianFilter(actual, actual, 20, 0, null);
		assertEquals(exp, actual);
	}
}
