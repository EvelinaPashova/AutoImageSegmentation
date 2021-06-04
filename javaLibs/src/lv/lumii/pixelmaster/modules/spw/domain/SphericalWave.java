
package lv.lumii.pixelmaster.modules.spw.domain;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import java.util.*;
import java.awt.*;
import lv.lumii.pixelmaster.modules.spw.domain.graph.*;

/**
 * Class containing spherical wave algorithm.
 * @author Jevgenijs Jonass
 * @see <a href="http://ocrai.narod.ru/vectory.html">Применение волнового алгоритма для нахождения скелета растрового изображения</a>
 */
public final class SphericalWave {
	private static int color1=0x004f7fb5;
	private static int color2=0x000000;
	private static int color=color1;
	private static int nextColor() {
		if (color==color2) color=color1;
		else color=color2;
		return color;
	}

	/**
	 * Determines how waves will be generated:
	 * <ul><li>1 - using 16 neighbour pixels</li>
	 * <li>2 - using 4-connectivity</li>
	 * <li>3 - using 8-connectivity</li>
	 * </ul>
	 */
	private static int connectivity;

	/**
	 * Paints specified <code>pixels</code> on the given image in specified color.
	 * @param pixels Set of pixels. Cannot be null. (ownership: caller)
	 *		No object contained in <code>pixels</code> is null.
	 *		(ownership of every object in <code>pixels</code>: caller)
	 * @param rImage Image to paint on. Cannot be null. (ownership: caller)
	 * @param color Pixel color. The highest byte must be 0.
	 * @pre class invariant of rImage is true
	 * @pre coordinates of every Point in <code>pixels</code> are in image bounds
	 * @post class invariant of rImage is true
	 * @post <code>pixels</code> or any Point object in it will not be modified
	 */
	public static void paintPixels(Set<Point> pixels, RasterImage rImage, int color) {
		assert !(pixels==null || rImage==null || (color & 0xff000000)!=0);
		int width=rImage.getWidth(), height=rImage.getHeight();
		Iterator <Point> it=pixels.iterator();
		while (it.hasNext()) {
			Point i=it.next();
			assert (i.x>=0  && i.x<width && i.y>=0 && i.y<height);
			rImage.setRGB(i.x, i.y, color);
		}
	}

	/**
	 * Calculates average point. Allocates new memory for the resulting point object.
	 * @param points The points. Cannot be null. (ownership: caller)
	 * @pre points != null and !points.isEmpty()
	 * @post <code>points</code> or any Point object in it will not be modified
	 * @return Average point. Cannot be null. (ownership: caller)
	 */
	public static Point averagePoint(Set<Point> points) {
		assert !(points==null || points.isEmpty());
		Iterator <Point> it=points.iterator();
		int sumX=0, sumY=0;
		while (it.hasNext()) {
			Point i=it.next();
			sumX+=i.x;
			sumY+=i.y;
		}
		int size=points.size();
		int avgX=sumX/size, avgY=sumY/size;
		return new Point(avgX, avgY);
	}

	/**
	 * Paints vertices of the given graph on the image.
	 * @param rImage image to paint on. Cannot be null. (ownership: caller)
	 * @param g Graph. Cannot be null. (ownership: caller)
	 * @pre class invariants of g and rImage are true
	 * @pre coordinates of every Vertex in <code>g</code> are in image bounds
	 * @post class invariant of rImage is true
	 * @post <code>g</code> will not be modified
	 */
	public static void drawVertices(RasterImage rImage, UGraph g) {
		assert !(rImage==null || g==null);
		Iterator <Vertex> it=g.vertices();
		while (it.hasNext()) {
			Vertex v=it.next();
			assert !(v.getX()<0 || v.getX()>=rImage.getWidth() || v.getY()<0 || v.getY()>=rImage.getHeight());
			rImage.setRGB(v.getX(), v.getY(), 0x00ff0000);
		}
	}

	/**
	 * Draws structure of the image. The structure is represented by the graph.
	 * @param rImage image to paint on. Cannot be null. (ownership: caller)
	 * @param g Graph. Cannot be null. (ownership: caller)
	 * @pre class invariants of g and rImage are true
	 * @pre coordinates of every Vertex in <code>g</code> are in image bounds
	 * @post class invariant of rImage is true
	 * @post <code>g</code> will not be modified
	 */
	public static void drawStructure(RasterImage rImage, UGraph g) {
		assert !(rImage==null || g==null);
		Iterator<GraphEdge> it=g.edges();
		GraphEdge edge;
		while (it.hasNext()) {
			edge=it.next();
			Vertex v1=edge.first, v2=edge.second;
			assert !(v1.getX()<0 || v1.getX()>=rImage.getWidth() || v1.getY()<0 || v1.getY()>=rImage.getHeight());
			assert !(v2.getX()<0 || v2.getX()>=rImage.getWidth() || v2.getY()<0 || v2.getY()>=rImage.getHeight());
			Bresenham.line(rImage, v1.getX(), v1.getY(), v2.getX(), v2.getY(), 0x00ff0000);
		}
	}

	/**
	 * Finds the connected component in the given area based on 8-connectivity.
	 * Allocates new memory for the resulting set. Pointers in the resulting set
	 * can either point to newly created objects or to objects
	 * that belonged to <code>points</code> at the moment before method call.
	 *
	 * <p>Starts search from the given pixel, adds it to the resulting set and
	 * removes it from <code>points</code>. After that, processes all its
	 * neighbours that belong to <code>points</code> and so on.</p>
	 *
	 * <p>Finally, all points in the connected component will be removed from
	 * <code>points</code> and added to the resulting set.</p>
	 *
	 * <p>Does not modify any of the <code>Point</code> objects in
	 * <code>points</code> and does not modify <code>start</code>.</p>
	 * 
	 * @param points Determines the search area. Cannot be null. (ownership: caller)
	 *		No object contained in <code>points</code> is null.
	 *		(ownership of every object in <code>points</code>: caller)
	 * @param start The start pixel. Cannot be null. (ownership: caller)
	 * @pre points.contains(start); points can store pointer to <code>start</code>
	 *		or to Point object that is equal to <code>start</code>.
	 * @post start will not be modified
	 * @return The connected component. Cannot be null. (ownership: caller)
	 */
	public static Set<Point> BFS(Set<Point> points, Point start) {
		assert !(points==null || start==null || !points.contains(start));
		Set<Point> connected=new HashSet<Point> ();
		Stack <Point> st=new Stack <Point> ();
		st.push(start);
		points.remove(start);
		Point [] a=new Point [8];
		for (int i=0; i<8; i++) a[i]=new Point();
		while (!st.empty()) {
			Point v=st.pop();
			connected.add(v);
			a[0].x=v.x;		a[0].y=v.y-1;
			a[1].x=v.x+1;	a[1].y=v.y-1;
			a[2].x=v.x+1;	a[2].y=v.y;
			a[3].x=v.x+1;	a[3].y=v.y+1;
			a[4].x=v.x;		a[4].y=v.y+1;
			a[5].x=v.x-1;	a[5].y=v.y+1;
			a[6].x=v.x-1;	a[6].y=v.y;
			a[7].x=v.x-1;	a[7].y=v.y-1;
			for (int i=0; i<8; i++) if (points.contains(a[i])) {
				st.push(a[i]);
				points.remove(a[i]);
				a[i]=new Point();
			}
		}
		return connected;
	}

	/**
	 * Divides set of points into 8-connected components and puts them into stack.
	 * Processes all <code>points</code> and removes them (and their 8-connected components)
	 * from the set until it is empty.
	 *
	 * <p>Does not delete or modify existing objects in <code>st</code>.</p>
	 *
	 * <p>New memory for every Set object added to <code>st</code> is allocated.
	 * Every Set added to <code>st</code> contains pointers to either
	 * newly created Point objects or objects that belonged to <code>points</code>
	 * at the moment before method call.</p>
	 *
	 * @param st Stack of wave generations. Cannot be null. (ownership: caller,
	 *		ownership of all objects in <code>st</code>: caller)
	 * @param points set containing points. Cannot be null. (ownership: caller)
	 *		No object contained in <code>points</code> is null.
	 *		(ownership of every object in <code>points</code>: caller)
	 * @post all objects added to <code>st</code> are not null.
	 * @post does not modify Point objects which initially belonged to <code>points</code>
	 */
	public static void divideWave(Stack <Set<Point>> st, Set<Point> points) {
		assert !(st==null || points==null);
		Iterator <Point> it=points.iterator();
		while (it.hasNext()) {
			Point v=it.next();
			Set<Point> gen=BFS(points, v);
		//	PointSet gen=BFS(points, new Point(v));//for test
			st.add(gen);
			it=points.iterator();
		}
	}
	
	/**
	 * Generates a spherical wave from every pixel in the given set and returns
	 * the wave generation as a set of pixels. Note: the result may not be an 8-connected component.
	 *
	 * <p>Allocates new memory for the resulting set and for all Point objects in it.</p>
	 * 
	 * <p>Only those pixels will be added to the resulting set whose coordinates
	 * are in image bounds, were not visited (at the start moment) and are not background.</p>
	 * 
	 * <p>Assigns visited[i]=true for all pixels in the resulting set (where i is coordinate of the pixel).</p>
	 * 
	 * @param pixels The initial set of pixels. All pixels must be in image bounds,
	 *		must be visited and must not be background pixels.
	 *		<code>pixels</code> cannot be null and no object contained in <code>pixels</code>
	 *		can be null. (ownership of <code>pixels</code>: caller,
	 *		ownership of every object in <code>pixels</code>: caller)
	 * @param bgr Determines background pixels.
	 *		bgr[i]=true if pixel i is background pixel. Cannot be null. (ownership: caller)
	 * @param visited Determines visited pixels.
	 *		visited[i]=true if pixel i was visited. Cannot be null. (ownership: caller)
	 * @param width Image width (must be &gt;0)
	 * @param height Image height (must be &gt;0)
	 * @pre bgr.length == visited.length == width*height
	 * @pre bgr != visited
	 * @pre for all i from 0 to bgr.length-1 [visited[i] =&gt; !bgr[i]]
	 * @pre start pixel is not background pixel and is not visited
	 * @post bgr will not be modified
	 * @post start will not be modified
	 * @post class invariant of <code>waves</code> is true
	 * @post class invariant of <code>g</code> is true
	 * @post width and height of <code>waves</code> will not be modified
	 * @post Does not modify <code>pixels</code> or any of the <code>Point</code> objects in <code>pixels</code>.
	 * @post does not modify bgr
	 * @return The wave generation. Cannot be null. (ownership: caller)
	 */
	public static Set<Point> generateWave(
		Set<Point> pixels, boolean [] bgr, boolean [] visited, int width, int height) {
		
		assert !(pixels==null || bgr==null || visited==null || bgr==visited || width<1 || height<1 ||
			bgr.length!=width*height || visited.length!=width*height);
		
		Set<Point> newGen=new HashSet<Point> ();
		Iterator <Point> it=pixels.iterator();
		Point [] gen;
		switch (connectivity) {
			case 1: {
				gen=new Point [16];
				for (int i=0; i<16; i++) gen[i]=new Point();
				while (it.hasNext()) {
					Point i=it.next();
					assert !(i.x<0 || i.x>=width || i.y<0 || i.y>=height ||
						!visited[i.y*width+i.x] || bgr[i.y*width+i.x]);
					gen[0].x=i.x;	gen[0].y=i.y-3;
					gen[1].x=i.x+1; gen[1].y=i.y-3;
					gen[2].x=i.x+2; gen[2].y=i.y-2;
					gen[3].x=i.x+3; gen[3].y=i.y-1;
					gen[4].x=i.x+3; gen[4].y=i.y;
					gen[5].x=i.x+3; gen[5].y=i.y+1;
					gen[6].x=i.x+2; gen[6].y=i.y+2;
					gen[7].x=i.x+1; gen[7].y=i.y+3;
					gen[8].x=i.x;	gen[8].y=i.y+3;
					gen[9].x=i.x-1;	gen[9].y=i.y+3;
					gen[10].x=i.x-2; gen[10].y=i.y+2;
					gen[11].x=i.x-3; gen[11].y=i.y+1;
					gen[12].x=i.x-3; gen[12].y=i.y;
					gen[13].x=i.x-3; gen[13].y=i.y-1;
					gen[14].x=i.x-2; gen[14].y=i.y-2;
					gen[15].x=i.x-1; gen[15].y=i.y-3;
					for (int j=0; j<16; j++) {
						int index=gen[j].y*width+gen[j].x;
						if (gen[j].x>=0 && gen[j].x<width && gen[j].y>=0 && gen[j].y<height &&
						!visited[index] && !bgr[index] && !newGen.contains(gen[j])) {
							newGen.add(gen[j]);
							visited[index]=true;
							gen[j]=new Point();
						}
					}
				}
			}
			break;
			case 2: {
				gen=new Point [4];
				for (int i=0; i<4; i++) gen[i]=new Point();
				while (it.hasNext()) {
					Point i=it.next();
					assert !(i.x<0 || i.x>=width || i.y<0 || i.y>=height ||
						!visited[i.y*width+i.x] || bgr[i.y*width+i.x]);
					gen[0].x=i.x;	gen[0].y=i.y-1;
					gen[1].x=i.x;	gen[1].y=i.y+1;
					gen[2].x=i.x-1; gen[2].y=i.y;
					gen[3].x=i.x+1; gen[3].y=i.y;
					for (int j=0; j<4; j++) {
						int index=gen[j].y*width+gen[j].x;
						if (gen[j].x>=0 && gen[j].x<width && gen[j].y>=0 && gen[j].y<height &&
						!visited[index] && !bgr[index] && !newGen.contains(gen[j])) {
							newGen.add(gen[j]);
							visited[index]=true;
							gen[j]=new Point();
						}
					}
				}
				connectivity=3;
			}
			break;
			case 3: {
				gen=new Point [8];
				for (int i=0; i<8; i++) gen[i]=new Point();
				while (it.hasNext()) {
					Point i=it.next();
					assert !(i.x<0 || i.x>=width || i.y<0 || i.y>=height ||
						!visited[i.y*width+i.x] || bgr[i.y*width+i.x]);
					gen[0].x=i.x;	gen[0].y=i.y-1;
					gen[1].x=i.x;	gen[1].y=i.y+1;
					gen[2].x=i.x-1; gen[2].y=i.y;
					gen[3].x=i.x+1; gen[3].y=i.y;
					gen[4].x=i.x+1; gen[4].y=i.y+1;
					gen[5].x=i.x-1; gen[5].y=i.y+1;
					gen[6].x=i.x+1; gen[6].y=i.y-1;
					gen[7].x=i.x-1; gen[7].y=i.y-1;
					for (int j=0; j<8; j++) {
						int index=gen[j].y*width+gen[j].x;
						if (gen[j].x>=0 && gen[j].x<width && gen[j].y>=0 && gen[j].y<height &&
						!visited[index] && !bgr[index] && !newGen.contains(gen[j])) {
							newGen.add(gen[j]);
							visited[index]=true;
							gen[j]=new Point();
						}
					}
				}
				connectivity=2;
			}
			break;
		}
		return newGen;
	}

	/**
	 * Initiates propagation of a spherical wave through binary image starting
	 * from the specified pixel and adds information about structure of the
	 * image to the graph. If <code>waves</code>!=null, paints all wave generations
	 * on the <code>waves</code> image.
	 * 
	 * <p>Assigns visited[i]=true for all pixels that the wave propagated through
	 * (where i is the coordinate of the pixel), including the start pixel.</p>
	 * 
	 * @param bgr Determines background pixels.
	 *		bgr[i]=true if pixel i is background pixel. Cannot be null. (ownership: caller)
	 * @param visited Determines visited pixels.
	 *		visited[i]=true if pixel i was visited. Cannot be null. (ownership: caller)
	 * @param width Image width (must be &gt;0)
	 * @param height Image height (must be &gt;0)
	 * @param waves Image that all wave generations will be painted on.
	 *		If <code>waves</code> is null, it will be ignored. (ownership: caller)
	 * @param g Graph representing image structure. Cannot be null. (ownership: caller)
	 * @param start Point object containing coordinates of start pixel. Cannot be null. (ownership: caller)
	 * @param n Minimum size of wave generation (must be &gt;0)
	 * @pre <code>waves</code> != null =&gt; class invariant of <code>waves</code> is true
	 * @pre <code>waves</code> != null =&gt; <code>waves.getWidth() == width && waves.getHeight() == height</code>
	 * @pre class invariant of <code>g</code> is true
	 * @pre bgr.length == visited.length == width*height
	 * @pre bgr != visited
	 * @pre for all i from 0 to bgr.length-1 [visited[i] =&gt; !bgr[i]]
	 * @pre <code>start</code> is in image bounds
	 * @pre start pixel is not background pixel and is not visited
	 * @post bgr will not be modified
	 * @post start will not be modified
	 * @post width and height of <code>waves</code> will not be modified
	 */
	public static void sphericalWave(
			boolean [] bgr, boolean [] visited, int width, int height,
			RasterImage waves, UGraph g, Point start, int n) {
		
		assert !(bgr==null || visited==null || bgr==visited || width<1 || height<1 ||
			bgr.length!=width*height || bgr.length!=visited.length ||
			g==null || start==null || n<=0 ||
			start.x<0 || start.x>=width || start.y<0 || start.y>=height ||
			bgr[start.y*width+start.x] || visited[start.y*width+start.x]);

		boolean drawWaves=waves!=null;
		if (drawWaves) assert waves.getWidth()==width && waves.getHeight()==height;
		if (drawWaves) {
		//	waves.getWidth()=width;
		//	waves.getHeight()=height;
		}
		
		Stack <Pair <Vertex, Set<Point>>> stack=new Stack <Pair <Vertex, Set<Point>>> ();
		Pair <Vertex, Set<Point>> tmpPair;
		Set<Point> generation=new HashSet<Point> ();
		Stack <Set<Point>> tmpStack=new Stack <Set<Point>> ();
		
		visited[start.y*width+start.x]=true;
		generation.add(start);
		Vertex curVertex;
		if (1>=n) {
			curVertex=g.get(start.x, start.y);
			if (curVertex==null) curVertex=g.addVertex(start.x, start.y);
		}
		else curVertex=null;
		stack.push(new Pair <Vertex, Set<Point>> (curVertex, generation));
		
		while (!stack.empty()) {
			tmpPair=stack.pop();
		//	if (drawWaves) paintPixels(tmpPair.second, waves, ImageProcessor.getRandomColor());
			if (drawWaves) paintPixels(tmpPair.second, waves, nextColor());
			generation=generateWave(tmpPair.second, bgr, visited, width, height);
			divideWave(tmpStack, generation);
			while (!tmpStack.isEmpty()) {
				generation=tmpStack.pop();
				int size2=generation.size();
				if (size2>=n) {
					Point avg=averagePoint(generation);
					curVertex=g.get(avg.x, avg.y);
					if (curVertex==null) curVertex=g.addVertex(avg.x, avg.y);
					if (tmpPair.first!=null) {
						tmpPair.first.addNeighbour(curVertex);
					}
					stack.push(new Pair <Vertex, Set<Point>> (curVertex, generation));
				}
				else stack.push(new Pair <Vertex, Set<Point>> (null, generation));
			}
		}
	}
	
	/**
	 * Generates a spherical wave from each black pixel and builds structure of
	 * the image. If <code>waves</code>!=null, paints all wave generations on the <code>waves</code> image.
	 * 
	 * @param bgr Determines background pixels.	bgr[i]=true if pixel i is background pixel.
	 *		Cannot be null. (ownership: caller)
	 * @param width Image width (must be &gt;0)
	 * @param height Image height (must be &gt;0)
	 * @param waves Image that all wave generations will be painted on.
	 *		If <code>waves</code> is null, it will be ignored. (ownership: caller)
	 * @param g Graph representing image structure. Initially, graph will be cleared.
	 *		Cannot be null. (ownership: caller)
	 * @param n Minimum size of wave generation (must be &gt;0)
	 * @param mode Determines how waves will be generated:
	 *		<ul><li>1 - using 16 neighbour pixels</li>
	 *		<li>2 - combining 4-connectivity and 8-connectivity</li></ul>
	 * @pre <code>waves</code> != null =&gt; class invariant of <code>waves</code> is true
	 * @pre <code>waves</code> != null =&gt; <code>waves.getWidth() == width && waves.getHeight() == height</code>
	 * @pre class invariant of <code>g</code> is true
	 * @pre bgr.length == width*height
	 * @post bgr will not be modified
	 * @post class invariant of <code>waves</code> is true
	 * @post class invariant of <code>g</code> is true
	 * @post width and height of <code>waves</code> will not be modified
	 */
	public static void buildStructure(boolean [] bgr, int width, int height, RasterImage waves, UGraph g, int n, int mode) {
		assert !(bgr==null || width<1 || height<1 || bgr.length!=width*height || g==null || n<=0);
		assert mode==1 || mode==2;
		switch (mode) {
			case 1: connectivity=1;
			break;
			case 2: connectivity=2;
			break;
		}
		
		boolean drawWaves=waves!=null;
		if (drawWaves) assert waves.getWidth()==width && waves.getHeight()==height;
		if (drawWaves) waves.fill(0x00ffffff);
		int size=bgr.length;
		boolean[] visited=new boolean[size];
	//	Arrays.fill(visited, false);
		g.clear();
		Point p=new Point();
		for (int i=0; i<size; i++) if (!bgr[i] && !visited[i]) {
			p.x=i%width;
			p.y=i/width;
			sphericalWave(bgr, visited, width, height, waves, g, p, n);
		}
	}
}
