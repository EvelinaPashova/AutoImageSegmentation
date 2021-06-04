package lv.lumii.pixelmaster.modules.compare.domain.graph;

import java.awt.geom.Point2D;
import java.io.*;

import static java.lang.Math.round;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class OGraphReader
{

    /**
	 * Writes graph to a file.
     *
	 * @param g graph.
	 * @param outFile output file.
	 * @return true if graph was successfully written to the file.
	 * @since 19.05.2010
	 */
	public static boolean write(OGraph g, File outFile)
    {
		assert !(g == null || outFile == null);
		try
        {
			PrintWriter out =
				new PrintWriter(
					new BufferedWriter(
						new FileWriter(outFile)));


            java.util.List<Point2D.Double> vertexSet = g.vertexSet();

            java.util.List<DefaultEdge> edgeSet = g.edgeSet();

			out.println(vertexSet.size() + " " + edgeSet.size());

            for (Point2D.Double vertex: vertexSet)
            {
                out.println((int)round(vertex.getX()) + " " + round((int)vertex.getY()));
            }

            for (DefaultEdge edge: edgeSet)
            {
                Point2D.Double v1 = g.getEdgeSource(edge);
                Point2D.Double v2 = g.getEdgeTarget(edge);

                out.println((int)round(v1.getX()) + " " + (int)round(v1.getY()) + " " +
						    (int)round(v2.getX()) + " " + (int)round(v2.getY()));
            }

			out.flush();
			out.close();
			return true;
		}
		catch (IOException ex) {
			return false;
		}
	}

    public static OGraph read(BufferedReader br, int minX, int minY, int maxX, int maxY)
    {
		assert !(br == null);
		assert (maxX == 0 && maxY == 0) || (maxX > 0 && maxY > 0);
		boolean check = maxX != 0;

		try {
			String s = br.readLine();
			String [] split = s.split(" ", 2);

			if (split.length != 2) {
                return null;
            }

			int size = Integer.parseInt(split[0]);
			int edges = Integer.parseInt(split[1]);

			//System.out.println("size: " + size + "; edges: " + edges);
			if (size < 0 || edges < 0 || size > 1000) {
                return null;
            }

            //make new graph
			OGraph g = new OGraph();

			for (int i=0; i<size; i++) {
				s=br.readLine();
				split=s.split(" ", 2);
				if (split.length!=2) {
                    return null;
                }
				int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
				if (check && (x<minX || x>maxX || y<minY || y>maxY)) {
                    return null;
                }

                Point2D.Double v = new Point2D.Double(x,y);

				if (!g.addVertex(v)) {
					g.clear();
					//System.out.println("Error adding vertex " + x + " " + y);
					return null;
				}
			}

			for (int i = 0; i < edges; i++) {
				s = br.readLine();
				split = s.split(" ", 4);
				if (split.length!=4) {
                    return null;
                }
				int x1 = Integer.parseInt(split[0]);
                int y1 = Integer.parseInt(split[1]);
				int x2 = Integer.parseInt(split[2]);
                int y2 = Integer.parseInt(split[3]);


                Point2D.Double v1 = new Point2D.Double(x1,y1);
                Point2D.Double v2 = new Point2D.Double(x2,y2);

				if (g.addEdge(v1, v2) == null) {
					g.clear();
					//System.out.println("Error adding edge " + x1 + " " + y1 + " " + x2  + " " + y2);
					return null;
				}
			}
            
			br.close();
			return g;
		}
		catch (IOException ex) {
			return null;
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

    public static OGraph read(File inFile, int minX, int minY, int maxX, int maxY)
    {
		try {
			return read(new BufferedReader(new FileReader(inFile)), minX, minY, maxX, maxY);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(OGraphReader.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

    public static OGraph read( File inFile, int maxX, int maxY )
    {
		return read(inFile, Integer.MIN_VALUE, Integer.MIN_VALUE, maxX, maxY);
    }

    public static OGraph read( File in )
    {
        return read( in, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE );
    }

	public static OGraph read(BufferedReader bufferedReader) {
		return read(bufferedReader, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
}
