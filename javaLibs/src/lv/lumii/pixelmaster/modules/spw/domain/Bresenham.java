
package lv.lumii.pixelmaster.modules.spw.domain;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 * Bresenham's line algorithm
 * @author Jevgenijs Jonass
 * @see <a href="http://algolist.manual.ru/graphics/painting/line.php">Генерация отрезка</a>
 */
public final class Bresenham {

	/**
	 * Draws line from point (x1; y1) to (x2; y2)
	 * @param rImage The image, cannot be null
	 * @param x1 X coordinate of the first point
	 * @param y1 Y coordinate of the first point
	 * @param x2 X coordinate of the second point
	 * @param y2 Y coordinate of the second point
	 * @param color Color of the line in format 0x00rrggbb
	 * @pre points (x1, y1) and (x2, y2) are in image bounds
	 */
	public static void line(RasterImage rImage, int x1, int y1, int x2, int y2, int color) {
		assert !(rImage==null || rImage.getWidth()<=x1 || x1<0 || rImage.getWidth()<=x2 || x2<0
			|| rImage.getHeight()<=y1 || y1<0 || rImage.getHeight()<=y2 || y2<0 || (color & 0xff000000)!=0);
		
		int width=rImage.getWidth(), height=rImage.getHeight(), size=width*height;
		int dx, dy, s, sx, sy, kl, swap, incr1, incr2;

		/* Вычисление приращений и шагов */
		sx=0;
		if ((dx= x2-x1) < 0) {dx= -dx; --sx;} else if (dx>0) ++sx;
		sy=0;
		if ((dy= y2-y1) < 0) {dy= -dy; --sy;} else if (dy>0) ++sy;
		/* Учет наклона */
		swap= 0;
		if ((kl= dx) < (s= dy)) {
			dx= s;dy= kl;kl= s; ++swap;
		}
		s= (incr1= 2*dy)-dx;	/* incr1 - констан. перевычисления	*/
								/* разности если текущее s < 0 и	*/
								/* s - начальное значение разности	*/
		incr2= 2*dx;			/* Константа для перевычисления		*/
								/* разности если текущее s >= 0		*/
		//PutPixLn (x1,y1,Pix_C);	/* Первый пиксел вектора		*/
		rImage.setRGB(x1, y1, color);
		while (--kl >= 0) {
			if (s >= 0) {
				if (swap!=0) x1+= sx; else y1+= sy;
				s-= incr2;
			}
			if (swap!=0) y1+= sy; else x1+= sx;
			s+=incr1;
			rImage.setRGB(x1, y1, color); /* Текущая точка вектора */
		}
	}
}
