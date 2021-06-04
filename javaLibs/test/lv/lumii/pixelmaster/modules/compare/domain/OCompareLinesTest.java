package lv.lumii.pixelmaster.modules.compare.domain;


import lv.lumii.pixelmaster.modules.compare.domain.OCompareLines;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraph;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraphReader;
import org.junit.Test;
import java.io.*;

/**
 * Test class.
 * @author Aivars Šāblis.
 * @since 11.03.2010
 */
public class OCompareLinesTest {
	private OGraph A;
    private OGraph B;
    private OCompareLines Comp;

	/**
	 * @since 11.03.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
	@Test
	public void s1t1() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		A = OGraphReader.read(new File("testdata/graph/agraph1.in"), 100, 100);
        //B = GraphReader.read(new File("testdata/graph/graphB1.in"));
		B=(OGraph)A.clone();
        Comp = new OCompareLines(A,B);

        System.out.println(Comp.DoCompareA(1));
        System.out.println("-----------------------------------");
        System.out.println(Comp.DoCompareB(1) + "\n");
		//assertEquals(g.getEdges(), g2.getEdges());
		//assertEquals(g.getVertices(), g2.getVertices());
	}

	/**
	 * @since 11.03.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
	@Test
	public void s1t2() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		A = OGraphReader.read(new File("testdata/graph/agraph1.in"), 100, 100);
        B = OGraphReader.read(new File("testdata/graph/bgraph1.in"), 100, 100);

        Comp = new OCompareLines(A,B);
        //Comp(A,B);
        System.out.println(Comp.DoCompareA(1));
        System.out.println(Comp.DoCompareB(1) + "\n");
		//assertEquals(g.get(1, 1), new Vertex(1, 1));
	}

    /**
	 * @since 31.03.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
	@Test
	public void s1t3() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		A = OGraphReader.read(new File("testdata/graph/agraph2.in"), 100, 100);
        B = OGraphReader.read(new File("testdata/graph/bgraph2.in"), 100, 100);

        Comp = new OCompareLines(A,B);
        System.out.println(Comp.DoCompareA(1));
        System.out.println(Comp.DoCompareB(1) + "\n");
	}

    /**
	 * @since 31.03.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
	@Test
	public void s1t4() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		A = OGraphReader.read(new File("testdata/graph/agraph3.in"), 100, 100);
        B = OGraphReader.read(new File("testdata/graph/bgraph3.in"), 100, 100);

        Comp = new OCompareLines(A,B);
        System.out.println(Comp.DoCompareA(1));
        System.out.println(Comp.DoCompareB(1) + "\n");
	}

    /**
	 * @since 31.03.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
	@Test
	public void s1t5() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		A = OGraphReader.read(new File("testdata/graph/agraph4.in"), 100, 100);
        B = OGraphReader.read(new File("testdata/graph/bgraph4.in"), 100, 100);

        Comp = new OCompareLines(A,B);
        System.out.println(Comp.DoCompareA(1));
        System.out.println(Comp.DoCompareB(1) + "\n");
	}

    /**
	 * @since 31.03.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
	@Test
	public void s1t6() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		A = OGraphReader.read(new File("testdata/graph/agraph5.in"), 100, 100);
        B = OGraphReader.read(new File("testdata/graph/bgraph5.in"), 100, 100);

        Comp = new OCompareLines(A,B);
        System.out.println(Comp.DoCompareA(1));
        System.out.println(Comp.DoCompareB(1) + "\n");
	}


    /**
	 * @since 31.03.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
	@Test
	public void s1t7() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		A = OGraphReader.read(new File("testdata/graph/agraph6.in"), 100, 100);
        B = OGraphReader.read(new File("testdata/graph/bgraph6.in"), 100, 100);

        Comp = new OCompareLines(A,B);
        System.out.println(Comp.DoCompareA(1));
        System.out.println(Comp.DoCompareB(1) + "\n");
	}

    /**
	 * @since 31.03.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
	@Test
	public void s1t8() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		A = OGraphReader.read(new File("testdata/graph/agraph7.in"), 100, 100);
        B = OGraphReader.read(new File("testdata/graph/bgraph7.in"), 100, 100);

        Comp = new OCompareLines(A,B);
        System.out.println(Comp.DoCompareA(1));
        System.out.println(Comp.DoCompareB(1) + "\n");
	}


    /**
	 * @since 31.03.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
	@Test
	public void s1t9() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		A = OGraphReader.read(new File("testdata/graph/agraph8.in"), 100, 100);
        B = OGraphReader.read(new File("testdata/graph/bgraph8.in"), 100, 100);

        Comp = new OCompareLines(A,B);
        System.out.println(Comp.DoCompareA(1));
        System.out.println(Comp.DoCompareB(1) + "\n");
	}


    /**
	 * @since 31.03.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
	@Test
	public void s1t10() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		A = OGraphReader.read(new File("testdata/graph/agraph9.in"), 100, 100);
        B = OGraphReader.read(new File("testdata/graph/bgraph9.in"), 100, 100);

        Comp = new OCompareLines(A,B);
        System.out.println(Comp.DoCompareA(1));
        System.out.println(Comp.DoCompareB(1) + "\n");
	}



    /**
	 * @since 14.04.2010
     * @throws newgraph.OGraph.EdgelessGraphException test
     * @throws newgraph.OGraph.WeightlessGraphException test
	 */
//	@Test
//	public void s1t11() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
//		A = OGraphReader.read(new File("testdata/graph/agraph10.in"), 1000, 1000);
//        B = OGraphReader.read(new File("testdata/graph/bgraph10.in"), 1000, 1000);
//
//        Comp = new OCompareLines(A,B);
//        System.out.println(Comp.DoCompareA(1));
//        System.out.println(Comp.DoCompareB(1) + "\n");
//	}
//
//    @Test
//    public void s2t1() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
//        A = OGraphReader.read(new File("testdata/graph/agraph11.in"), 1000, 1000);
//        B = OGraphReader.read(new File("testdata/graph/bgraph11.in"), 1000, 1000);
//
//        Comp = new OCompareLines(A,B);
//        System.out.println(Comp.DoCompareA(1));
//        System.out.println(Comp.DoCompareB(1) + "\n");
//    }
//
//    @Test
//    public void s2t2() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
//        A = OGraphReader.read(new File("testdata/graph/agraph12.in"), 1000, 1000);
//        B = OGraphReader.read(new File("testdata/graph/bgraph12.in"), 1000, 1000);
//
//        Comp = new OCompareLines(A,B);
//        System.out.println(Comp.DoCompareA(1));
//        System.out.println(Comp.DoCompareB(1) + "\n");
//    }
//
//    @Test
//    public void s2t3() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
//        A = OGraphReader.read(new File("testdata/graph/agraph13.in"), 1000, 1000);
//        B = OGraphReader.read(new File("testdata/graph/bgraph13.in"), 1000, 1000);
//
//        Comp = new OCompareLines(A,B);
//        System.out.println(Comp.DoCompareA(1));
//        System.out.println(Comp.DoCompareB(1) + "\n");
//    }
//
//
//    @Test
//    public void s3t1() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
//        A = OGraphReader.read(new File("testdata/graph/agraph11.in"), 1000, 1000);
//        B = OGraphReader.read(new File("testdata/graph/bgraph11.in"), 1000, 1000);
//
//        Comp = new OCompareLines(A,B);
//        System.out.println(Comp.DoCompareA(1));
//        System.out.println(Comp.DoCompareB(1) + "\n");
//        System.out.println("-----------------------------------");
//        Comp.setOnePieceSize(2.5);
//        System.out.println(Comp.DoCompareA(1));
//        System.out.println(Comp.DoCompareB(1) + "\n");
//        System.out.println("-----------------------------------");
//        Comp.setOnePieceSize(5);
//        System.out.println(Comp.DoCompareA(1));
//        System.out.println(Comp.DoCompareB(1) + "\n");
//        System.out.println("-----------------------------------");
//        Comp.setOnePieceSize(10);
//        System.out.println(Comp.DoCompareA(1));
//        System.out.println(Comp.DoCompareB(1) + "\n");
//        System.out.println("-----------------------------------");
//        Comp.setOnePieceSize(12.5);
//        System.out.println(Comp.DoCompareA(1));
//        System.out.println(Comp.DoCompareB(1) + "\n");
//        System.out.println("-----------------------------------");
//        Comp.setOnePieceSize(25);
//        System.out.println(Comp.DoCompareA(1));
//        System.out.println(Comp.DoCompareB(1) + "\n");
//    }
}
