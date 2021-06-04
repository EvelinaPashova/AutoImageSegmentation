

package lv.lumii.pixelmaster.modules.spw.domain.graph;

import java.io.*;
import java.util.Iterator;

/**
 * Class for reading graph from file and writing to file.
 * @author Jevgenijs Jonass
 */
public final class GraphIO {

	/**
	 * Writes graph to a file
	 * @param g graph
	 * @param outFile output file
	 * @return true if graph was successfully written to the file
	 */
	public static boolean write(UGraph g, File outFile) {
		assert !(g==null || outFile==null);
		try {
			PrintWriter out =
				new PrintWriter(
					new BufferedWriter(
						new FileWriter(outFile)));
			out.println(g.vertices.size() + " " + g.nEdges());
			Iterator<Vertex> it=g.vertices();
			while (it.hasNext()) {
				Vertex v=it.next();
				out.println(v.getX() + " " + v.getY());
			}
			Iterator<GraphEdge> edgeIterator = g.edges();
			while (edgeIterator.hasNext()) {
				GraphEdge edge=edgeIterator.next();
				out.println(edge.first.getX() + " " + edge.first.getY() + " " +
						edge.second.getX() + " " + edge.second.getY());
			}
			out.flush();
			out.close();
			return true;
		}
		catch (IOException ex) {
			return false;
		}
	}

	/**
	 * Reads graph from a file. If coordinates of any vertex are not in bounds [0..maxX] and [0..maxY],
	 * null will be returned. If both maxX and maxY have value of 0, any coordinates are allowed.
	 *
	 * @param inFile input file
	 * @param maxX maximum x coordinate
	 * @param maxY maximum y coordinate
	 * @param minX Minimum X coordinate.
	 * @param minY Minimum Y coordinate.
	 * @pre (maxX==0 and maxY==0) or (maxX>0 and maxY>0)
	 * @return graph or null if file has incorrect format
	 */
	public static UGraph read(File inFile, int minX, int minY, int maxX, int maxY) {
		assert !(inFile==null);
		assert (maxX==0 && maxY==0) || (maxX>0 && maxY>0);
		boolean check=maxX!=0;
		try {
			FileReader fr =	new FileReader (inFile);
			BufferedReader br=new BufferedReader(fr);
			String s=br.readLine();
			String [] splitted=s.split(" ", 2);
			if (splitted.length!=2) return null;
			int size=Integer.parseInt(splitted[0]);
			int edges=Integer.parseInt(splitted[1]);
			if (size<0 || edges<0 || size>1000) return null;
			UGraph g=new UGraph();
			
			for (int i=0; i<size; i++) {
				s=br.readLine();
				splitted=s.split(" ", 2);
				if (splitted.length!=2) return null;
				int x=Integer.parseInt(splitted[0]), y=Integer.parseInt(splitted[1]);
				if (check && (x<minX || x>maxX || y<minY || y>maxY)) return null;
				if (g.addVertex(x, y)==null) {
					g.clear();
					return null;
				}
			}
			
			for (int i=0; i<edges; i++) {
				s=br.readLine();
				splitted=s.split(" ", 4);
				if (splitted.length!=4) return null;
				int x1=Integer.parseInt(splitted[0]), y1=Integer.parseInt(splitted[1]);
				int x2=Integer.parseInt(splitted[2]), y2=Integer.parseInt(splitted[3]);
				if (!g.addEdge(x1, y1, x2, y2)) {
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


    
    /**
	 * Reads graph from a file. If coordinates of any vertex are not in bounds [0..maxX] and [0..maxY],
	 * null will be returned. If both maxX and maxY have value of 0, any coordinates are allowed.
	 * TODO: catches NumberFormatException, which is a subclass of RuntimeException (bad style)
	 * @author Jevgenijs Jonass.
	 * @param inFile input file.
	 * @param maxX maximum x coordinate
	 * @param maxY maximum y coordinate
	 * @pre (maxX==0 and maxY==0) or (maxX>0 and maxY>0)
	 * @return graph or null if file has incorrect format.
	 * @since 21.04.2009
	 */
    
    public static UGraph read( File inFile, int maxX, int maxY )
    {
        return read( inFile, 0, 0, maxX, maxY );
    }


    
    /**
     * Loads graph from specified file.
     *
     * @param in Input file descriptor.
     *
     * @return Object of class <code>UGraph</code> containing loaded graph.
     *
     * Added by Andrey Zhmakin, Mar-12-2010
     * Reason for addition: Problems compiling tests. 
     *
     */

    public static UGraph read( File in )
    {
        return read( in, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE );
    }
}
