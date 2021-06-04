
package lv.lumii.pixelmaster.modules.compare.domain;

import lv.lumii.pixelmaster.modules.compare.domain.CompareLines;
import lv.lumii.pixelmaster.modules.spw.domain.graph.GraphEdge;
import lv.lumii.pixelmaster.modules.spw.domain.graph.GraphIO;
import lv.lumii.pixelmaster.modules.spw.domain.graph.Vertex;
import lv.lumii.pixelmaster.modules.spw.domain.graph.UGraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

/**
 * Test class.
 * @author Aivars Šāblis.
 * @since 11.03.2010
 */
public class CompareLinesTest {
	private UGraph A;
    private UGraph B;
    private CompareLines Comp;

	/**
	 * @since 11.03.2010
     * @throws graph.UGraph.EdgelessGraphException  test
     * @throws graph.UGraph.WeightlessGraphException  test
	 */
	@Test
	public void s1t1() throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException {
		A = GraphIO.read(new File("testdata/graph/graph1.in"), 100, 100);
        //B = GraphIO.read(new File("testdata/graph/graphB1.in"));
		B=(UGraph)A.clone();
        Comp = new CompareLines(A,B);

        //System.out.println(Comp.DoCompare());
		//assertEquals(g.getEdges(), g2.getEdges());
		//assertEquals(g.getVertices(), g2.getVertices());
	}

	/**
	 * @since 11.03.2010
     * @throws graph.UGraph.EdgelessGraphException  test
     * @throws graph.UGraph.WeightlessGraphException  test
	 */
	@Test
	public void s1t2() throws UGraph.EdgelessGraphException, UGraph.WeightlessGraphException {
		A = GraphIO.read(new File("testdata/graph/agraph1.in"), 100, 100);
        B = GraphIO.read(new File("testdata/graph/bgraph3.in"), 100, 100);

        Comp = new CompareLines(A,B);
        //Comp(A,B);
        //System.out.println(Comp.DoCompare());
		//assertEquals(g.get(1, 1), new Vertex(1, 1));
	}
}
