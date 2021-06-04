package lv.lumii.pixelmaster.modules.compare.domain.graph;


import lv.lumii.pixelmaster.modules.compare.domain.graph.DefaultEdge;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraphReader;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraph;
import org.junit.*;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.*;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class OGraphTest {

    private OGraph imageGraph;
    private OGraph graphA, graphB;

    @Test
	public void s1t0() {
		/*g= GraphReader.read(new File("testdata/graph/graph1.in"));
		UGraph g2=(UGraph)g.clone();
		assertEquals(g.getEdges(), g2.getEdges());
		assertEquals(g.getVertices(), g2.getVertices()); */

        // create a graph based on URL objects
        //imageGraph = imageGraph.createImageGraph();

        this.imageGraph = new OGraph();

        try {
            Point2D.Double P1 = new Point2D.Double(1,1);
            Point2D.Double P2 = new Point2D.Double(2,2);
            Point2D.Double P3 = new Point2D.Double(3,3);

            // add the vertices
            this.imageGraph.addVertex(P1);
            this.imageGraph.addVertex(P2);
            this.imageGraph.addVertex(P3);

            // add edges to create linking structure
            this.imageGraph.addEdge(P1, P2);
            DefaultEdge edge1 = this.imageGraph.addEdge(P2, P3);
            //imageGraph.addEdge(P3, P1);

            //Point ptest = g.getEdgeSource(edge1);

            //System.out.println(ptest);

            //DefaultEdge edge = g.getEdge(P1, P2);

            //System.out.println(edge.toString());

            //g.removeVertex(P2);

            java.util.List<DefaultEdge> edgeS = this.imageGraph.edgeSet();

            for (DefaultEdge test: edgeS)
            {
              System.out.println(test);
              //System.out.println(getEdgeLength(test));
            }

            java.util.List<Point2D.Double> vertexS = this.imageGraph.vertexSet();

            for (Point2D.Double test: vertexS)
            {
              System.out.println(test);
              System.out.println(this.imageGraph.inDegreeOf(test) + ";" + this.imageGraph.outDegreeOf(test));
            }

            //System.out.println(g.inDegreeOf(P2));

            System.out.println(this.imageGraph.getCenterOfMass());


            System.out.println("-----------------------------------");
            /*java.util.List<DefaultEdge> edgeSetB = graphA.edgeSet();

            for (DefaultEdge edge: edgeSetB)
            {
                System.out.println(edge.toString());
            }                                       */

            System.out.println("-----------------------------------");


        } catch (Exception e) {
            e.printStackTrace();
        }

        // note directed edges are printed as: (<v1>,<v2>)
        System.out.println(this.imageGraph.toString());
	}

    @Test
    public void testEdgesOf() throws Exception {
    }


    @Test
    public void testIncomingEdgesOf() throws Exception {
    }


    @Test
    public void testOutgoingEdgesOf() throws Exception {
    }


    /**
	 */
	@Test
	public void s1t1() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph1.in"));
		OGraph g2 = (OGraph)imageGraph.clone();
		assertEquals(imageGraph.edgeSet(), g2.edgeSet());
		assertEquals(imageGraph.vertexSet(), g2.vertexSet());
	}

	/**
	 */
	@Test
	public void s1t2() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph1.in"));

        Point2D.Double p1 = new Point2D.Double(1,1);
		assertEquals(imageGraph.containsVertex(p1), true);
	}

	/**
	 */
	@Test
	public void s1t3() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph2.in"));
		DefaultEdge edge = imageGraph.addEdge (new Point2D.Double(6, 6), new Point2D.Double(1, 1));
		//assertEquals(edge, new DefaultEdge(new Point(1, 2), new Point(1, 1)));

		assertEquals(imageGraph.outDegreeOf(new Point2D.Double(1,1)) + imageGraph.inDegreeOf(new Point2D.Double(1,1)), 3);
	}

    @Test
	public void s1t4() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph2.in"));

        List<Point2D.Double> S = imageGraph.neighborListOf(new Point2D.Double(3,3));

		List<Point2D.Double> expected = new ArrayList<Point2D.Double>();

		expected.add(new Point2D.Double(2,2));
		expected.add(new Point2D.Double(6,6));
        expected.add(new Point2D.Double(5,5));
		expected.add(new Point2D.Double(7,7));

		assertEquals(S, expected);
	}

    @Test
	public void s1t5() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph5.in"));
		imageGraph.removeVertex(new Point2D.Double(4,4));


		List<Point2D.Double> expected = new ArrayList<Point2D.Double>();
		expected.add(new Point2D.Double(1,1));
		expected.add(new Point2D.Double(2,2));
		expected.add(new Point2D.Double(3,3));
		assertEquals(imageGraph.vertexSet(), expected);
		assertEquals(imageGraph.edgeSet(), new ArrayList<DefaultEdge>());
	}

    @Test
	public void s1t6() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph2.in"));
        assertEquals(imageGraph.edgeSet().size(), 11);
	}

    @Test
	public void s1t7() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph3.in"));


		//List<Point2D.Double> S = imageGraph.vertexSet();
		imageGraph.clear();
        assertEquals(imageGraph.vertexSet(), new ArrayList<Point2D.Double>());
        assertEquals(imageGraph.edgeSet(), new ArrayList<DefaultEdge>());
		/*for (Point v: S) {
			assertEquals(v.owner, null);
			assertEquals(v.neighbours, null);
		}   */
	}

    @Test
	public void s1t8() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph4.in"));


		assertEquals(imageGraph.addVertex(new Point2D.Double(1,1)), false);


        List<Point2D.Double> expectedV = new ArrayList<Point2D.Double>();
		expectedV.add(new Point2D.Double(1,1));
		expectedV.add(new Point2D.Double(2,2));
		expectedV.add(new Point2D.Double(3,3));
		assertEquals(imageGraph.vertexSet(), expectedV);
		assertEquals(imageGraph.edgeSet(), new ArrayList<DefaultEdge>());
	}


	@Test
	public void s1t9() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph5.in"));

       /* OGraph graphB =  new OGraph();

        EdgeCreator<Point2D.Double, DefaultEdge> edgeCreator = graphB.getEdgeCreator();

        List <DefaultEdge> expected = new ArrayList <DefaultEdge>();
		Point2D.Double v1=new Point2D.Double(1, 1);
		Point2D.Double v2=new Point2D.Double(2, 2);
		Point2D.Double v3=new Point2D.Double(3, 3);
		Point2D.Double v4=new Point2D.Double(4, 4);
		expected.add(edgeCreator.createEdge(v1, v4));
		expected.add(edgeCreator.createEdge(v2, v4));
		expected.add(edgeCreator.createEdge(v3, v4));       */
		assertEquals(3, imageGraph.edgeSet().size()); 
	}

    @Test
	public void s1t10() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph5.in"));

		List<Point2D.Double> expected = new ArrayList<Point2D.Double>();
		Point2D.Double v1 = new Point2D.Double(1,1);
		Point2D.Double v2 = new Point2D.Double(2,2);
		Point2D.Double v3 = new Point2D.Double(3,3);
		Point2D.Double v4 = new Point2D.Double(4,4);
		expected.add(v1);
		expected.add(v2);
		expected.add(v3);
		expected.add(v4);
		assertEquals(expected, imageGraph.vertexSet());
	}

    @Test
    public void s1t11() {
        imageGraph = OGraphReader.read(new File("testdata/graph/graph5.in"));

        DefaultEdge edge = imageGraph.getEdge(new Point2D.Double(1,1), new Point2D.Double(4,4));

        assertEquals(new Point2D.Double(1,1), edge.getSource());
        assertEquals(new Point2D.Double(4,4), edge.getTarget());

    }

    @Test
    public void s1t12() {
        imageGraph = OGraphReader.read(new File("testdata/graph/graph8.in"));


        DefaultEdge edge = imageGraph.getEdge(new Point2D.Double(1,1), new Point2D.Double(5,5));

        assertEquals(imageGraph.getEdgeLength(edge), 5.6, 0.1);
    }

    @Test
    public void s1t13() {
        imageGraph = OGraphReader.read(new File("testdata/graph/graph8.in"));

        DefaultEdge edge = imageGraph.getEdge(new Point2D.Double(1,1), new Point2D.Double(5,5));

        assertEquals(imageGraph.containsEdge(edge), true);
    }

    @Test
    public void s1t14() {
        imageGraph = OGraphReader.read(new File("testdata/graph/graph8.in"));


        DefaultEdge edge = imageGraph.removeEdge(new Point2D.Double(1,1), new Point2D.Double(5,5));

        assertEquals(imageGraph.edgeSet(), new ArrayList<DefaultEdge>());
        assertEquals(false, imageGraph.removeEdge(edge));
    }

    @Test
	public void s1t15() {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph9.in"));
        assertEquals(imageGraph.getGraphLength(), 5, 0.01);
	}


    @Test
	public void s1t16() throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException {
		imageGraph = OGraphReader.read(new File("testdata/graph/graph10.in"));
        assertEquals(imageGraph.getCenterOfMass(), new Point2D.Double(0.5, 0.5));
	}


    @Test
    public void s1t17() {
        imageGraph = OGraphReader.read(new File("testdata/graph/graph8.in"));

        DefaultEdge edge = imageGraph.getEdge(new Point2D.Double(1,1), new Point2D.Double(5,5));

        assertEquals(imageGraph.getEdgeMiddlePoint(edge), new Point2D.Double(3, 3));
    }

    @Test
    public void s1t18() throws Exception {
        graphA = OGraphReader.read(new File("testdata/graph/graph5.in"));
        graphB = OGraphReader.read(new File("testdata/graph/graph4.in"));

        graphA.addGraph(graphB);


        List<Point2D.Double> expected = new ArrayList<Point2D.Double>();
        Point2D.Double v1 = new Point2D.Double(1,1);
		Point2D.Double v2 = new Point2D.Double(2,2);
		Point2D.Double v3 = new Point2D.Double(3,3);
		Point2D.Double v4 = new Point2D.Double(4,4);
		expected.add(v1);
		expected.add(v2);
		expected.add(v3);
		expected.add(v4);
		assertEquals(expected, graphA.vertexSet());
        assertEquals(graphA.edgeSet().size(), 3);
    }
}

