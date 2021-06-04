
package lv.lumii.pixelmaster.modules.spw.domain.graph;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import lv.lumii.pixelmaster.modules.spw.domain.TestUtils;

/**
 * A set of test suites for the class "GraphIO".
 *
 * The first test suite contains "positive" test cases for the method "read",
 * the second test suite tests the method "write",
 * the third test suite contains "negative" test cases for the method "read".
 *
 * These tests do not test the behaviour of the class "GraphIO"
 * when I/O error occurs.
 *
 * In the first test suite, graph is read from a file, then
 * its vertices and edges are checked by constructing
 * the set of vertices and the set of edges (the expected results)
 * and comparing them with the sets returned by UGraph's methods.
 *
 * In the second test suite, graph G1 is read from file, then written to
 * a temporary file. Graph G2 is read from the temporary file. Graphs G1 and
 * G2 are then compared.
 *
 * @author Jonas
 */
public class GraphIOTest {
	private UGraph g;

	/**
	 * Test method "read"; correct file format.
	 */
	@Test public void s1t1() {
		g=GraphIO.read(new File("testdata/graph/graph1.in"));
		Set <Vertex> V=g.getVertices();
		Set <GraphEdge> E = g.getEdges();
		Set <Vertex> expV = new HashSet <Vertex> ();
		expV.add(new Vertex(1, 1));
		expV.add(new Vertex(2, 2));
		expV.add(new Vertex(3, 3));
		expV.add(new Vertex(4, 4));
		Set <GraphEdge> expE=new HashSet <GraphEdge> ();
		expE.add(new GraphEdge(new Vertex(1, 1), new Vertex(2, 2)));
		expE.add(new GraphEdge(new Vertex(3, 3), new Vertex(2, 2)));
		expE.add(new GraphEdge(new Vertex(4, 4), new Vertex(2, 2)));
		expE.add(new GraphEdge(new Vertex(3, 3), new Vertex(4, 4)));
		assertEquals(expE, E);
		assertEquals(expV, V);
	}

	@Test public void s2t1() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph1.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t2() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph2.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t3() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph3.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t4() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph4.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t5() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph5.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t6() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph6.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t7() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph7.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t8() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph8.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t9() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph9.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t10() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph10.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t11() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph11.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t12() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph12.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t13() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph13.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t14() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph14.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t15() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph15.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t16() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph16.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t17() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph17.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	@Test public void s2t18() throws IOException {
		UGraph G1=GraphIO.read(new File("testdata/graph/graph18.in"));
		File tmp = File.createTempFile("test", null);
		GraphIO.write(G1, tmp);
		UGraph G2=GraphIO.read(tmp);
		TestUtils.assertGraphEquals(G1, G2);
	}

	/**
	 * Test method "read"; incorrect file format.
	 * The first vertex of the second edge does not exist.
	 */
	@Test public void s3t1() {
		g=GraphIO.read(new File("testdata/graph/err1.in"));
		assertNull(g);
	}

	/**
	 * Test method "read"; incorrect file format.
	 * Negative number of vertices specified in the file.
	 */
	@Test public void s3t2() {
		g=GraphIO.read(new File("testdata/graph/err2.in"));
		assertNull(g);
	}

	/**
	 * Test method "read"; incorrect file format.
	 * The 4-th row contains only one coordinate, but it must
	 * contain 2 coordinates.
	 */
	@Test public void s3t3() {
		g=GraphIO.read(new File("testdata/graph/err3.in"));
		assertNull(g);
	}

	/**
	 * Test method "read"; incorrect file format.
	 * The first edge is the same as the second.
	 * It cannot be added second time.
	 */
	@Test public void s3t4() {
		g=GraphIO.read(new File("testdata/graph/err4.in"));
		assertNull(g);
	}
}
