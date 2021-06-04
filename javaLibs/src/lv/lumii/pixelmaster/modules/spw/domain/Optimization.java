
package lv.lumii.pixelmaster.modules.spw.domain;
import java.util.*;
import lv.lumii.pixelmaster.modules.spw.domain.graph.*;
import lv.lumii.pixelmaster.modules.spw.domain.Line;
import lv.lumii.pixelmaster.modules.spw.domain.CoordVector;

/**
 * Class containing methods for image structure analysis.
 * @author Jevgenijs Jonass
 */
public final class Optimization {
	/**
	 * Connects ends of the edges which are closer than dist.
	 * @param g Graph representing image structure
	 * @param dist distance threshold (must be &gt;= 0)
	 */
	public static void connectEnds(UGraph g, double dist) {
		assert !(g==null || dist<0);
		Set <Vertex> s=new HashSet <Vertex> ();
		Iterator <Vertex> it=g.vertices();
		while (it.hasNext()) {
			Vertex v=it.next();
			if (v.degree()==1) s.add(v);
		}
		for (Vertex v: s) {
			for (Vertex w: s) {
				CoordVector a=new CoordVector(v.getX()-w.getX(), v.getY()-w.getY());
				if (a.getLength()<dist) {
					v.addNeighbour(w);
				}
			}
		}
	}

	/**
	 * Finds key points in the graph.
	 * New memory is allocated for the Set, but not for objects in it.
	 * It means that the returned set contains pointers to vertices that
	 * belong to <code>g</code>.
	 *
	 * @param g Graph representing image structure
	 * @param k threshold of the angle cosinus (must be in range [-1..1])
	 * @return Hash set containing key points
	 */
	public static Set <Vertex> findKeyPoints(UGraph g, double k) {
		assert g!=null;
		Set <Vertex> keyPoints=new HashSet <Vertex> ();
		Iterator <Vertex> it=g.vertices();
		while (it.hasNext()) {
			Vertex v=it.next();
			int deg=v.degree();
			if (deg==1 || deg>2) keyPoints.add(v);
			if (deg==2) {
				Iterator <Vertex> neighbours=v.neighbours();
				Vertex p=neighbours.next();
				Vertex q=neighbours.next();
				double cos=Line.angleBetweenVectorsCos(p, v, q);
				if (cos>=k) keyPoints.add(v);
			}
		}
		return keyPoints;
	}

	/**
	 * Primary optimization of the graph.
	 * @param g Graph representing image structure
	 * @param k threshold of the angle cosinus (must be in range [-1..1])
	 */
	public static void primaryOptimization(UGraph g, double k) {
		assert g!=null;
		Set <Vertex> deg3=degree3(g);
		Iterator <Vertex> it=deg3.iterator();
		while (it.hasNext()) {
			Vertex v=it.next();
			Iterator <Vertex> neighbours=v.neighbours();
			Vertex n1=neighbours.next();
			Vertex n2=neighbours.next();
			Vertex n3=neighbours.next();
			assert neighbours.hasNext()==false;
			final double cos12=Line.angleBetweenVectorsCos(n1, v, n2);
			int f=3;	double minCos=cos12;
			final double cos13=Line.angleBetweenVectorsCos(n1, v, n3);
			if (cos13<minCos) {f=2; minCos=cos13;}
			final double cos23=Line.angleBetweenVectorsCos(n2, v, n3);
			if (cos23<minCos) {f=1; minCos=cos23;}
			//System.out.print("primaryOptimization:");
			//System.out.print("\nv: " + v + " n1: " + n1 + " n2: " + n2 + " n3: " + n3);
			//System.out.println("\ncos12 = " + cos12 + " cos13 = " + cos13 + " cos23 = " + cos23 + " minCos = " + minCos);
			if (minCos<k) {
				switch (f) {
					case 1: {
						Vertex newVertex=g.addVertex((n2.getX()+n3.getX())/2, (n2.getY()+n3.getY())/2);
						if (newVertex!=null) {
							v.delete();
							newVertex.addNeighbour(n3);
							newVertex.addNeighbour(n2);
							newVertex.addNeighbour(n1);
						}
						else {
							//System.out.println("primaryOptimization: replaced vertex is null (NOT BUG)");
						}
					}
					break;
					case 2: {
						Vertex newVertex=g.addVertex((n1.getX()+n3.getX())/2, (n1.getY()+n3.getY())/2);
						if (newVertex!=null) {
							v.delete();
							newVertex.addNeighbour(n3);
							newVertex.addNeighbour(n2);
							newVertex.addNeighbour(n1);
						}
						else {
							//System.out.println("primaryOptimization: replaced vertex is null (NOT BUG)");
						}
					}
					break;
					case 3: {
						Vertex newVertex=g.addVertex((n2.getX()+n1.getX())/2, (n2.getY()+n1.getY())/2);
						if (newVertex!=null) {
							v.delete();
							newVertex.addNeighbour(n3);
							newVertex.addNeighbour(n2);
							newVertex.addNeighbour(n1);
						}
						else {
							//System.out.println("primaryOptimization: replaced vertex is null (NOT BUG)");
						}
					}
					break;
				}
			}
			deg3.remove(v);
			it=deg3.iterator();
		}
	}

	/**
	 * Edge optimization.
	 * @param g graph
	 * @param k variance threshold (must be &gt;=0).
	 *		Smaller k value means less changes will be made.
	 */
	public static void optimizeEdges(UGraph g, double k) {
		assert g!=null && k>=0;
		Set <GraphEdge> set=g.getEdges();
		Iterator <GraphEdge> it=set.iterator();
		Vector <Vertex> vect=new Vector <Vertex> ();
		while (it.hasNext()) {
			vect.clear();
			GraphEdge ge=it.next();
			vect.add(ge.first);
			vect.add(ge.second);
			do {
				boolean added1=false;
				boolean added2=false;
				if (vect.lastElement().degree()==2) {
					Iterator <Vertex> vi=vect.lastElement().neighbours();
					Vertex neigh1, neigh2;
					neigh1=vi.next();	neigh2=vi.next();
					if (!vect.contains(neigh1)) {
						if (set.contains(new GraphEdge(vect.lastElement(), neigh1))) {
							set.remove(new GraphEdge(vect.lastElement(), neigh1));
							vect.add(neigh1);
							added1=true;
						}
					}
					else {
						if (!vect.contains(neigh2)) {
							if (set.contains(new GraphEdge(vect.lastElement(), neigh2))) {
								set.remove(new GraphEdge(vect.lastElement(), neigh2));
								vect.add(neigh2);
								added1=true;
							}
						}
					}
					if (added1) {
						double variance=Line.variance(vect);
						//System.out.println("variance: " + variance);
						if (variance>k) {
							vect.remove(vect.size()-1);
							added1=false;
						}
					}
				}
				if (vect.firstElement().degree()==2) {
					Iterator <Vertex> vi=vect.firstElement().neighbours();
					Vertex neigh1, neigh2;
					neigh1=vi.next();	neigh2=vi.next();
					if (!vect.contains(neigh1)) {
						if (set.contains(new GraphEdge(vect.firstElement(), neigh1))) {
							set.remove(new GraphEdge(vect.firstElement(), neigh1));
							vect.add(0, neigh1);
							added2=true;
						}
					}
					else {
						if (!vect.contains(neigh2)) {
							if (set.contains(new GraphEdge(vect.firstElement(), neigh2))) {
								set.remove(new GraphEdge(vect.firstElement(), neigh2));
								vect.add(0, neigh2);
								added2=true;
							}
						}
					}
					if (added2) {
						double variance=Line.variance(vect);
						//System.out.println("variance: " + variance);
						if (variance>k) {
							vect.remove(0);
							added2=false;
						}
					}
				}
				if (!added1 && !added2) {
					//replace vect with one edge
					Iterator <Vertex> removeItr=vect.iterator();
					//System.out.println("first: " + vect.firstElement() + " last: " + vect.lastElement());
					removeItr.next();
					Vertex v=removeItr.next();
					//delete vertices at indexes 1..size-1, where index starts from 0
					while (removeItr.hasNext()) {
						v.delete();
						//System.out.println("delete: "+v);
						v=removeItr.next();
						assert v!=null;
					}
					boolean b=vect.firstElement().addNeighbour(vect.lastElement());
					assert vect.size()>1;
					break;
				}
			} while (true);
			set.remove(new GraphEdge(vect.firstElement(), vect.lastElement()));
			it=set.iterator();
		}
	}

	/**
	 * Cuts "tails" of the graph, or edge sequences which are shorter than maxLen.
	 * @param g graph
	 * @param maxLen length threshold (must be &gt;=0)
	 */
	public static void cutTails(UGraph g, double maxLen) {
		assert g!=null && maxLen>=0;
		Set <Vertex> s=g.getVertices();
		for (Vertex v: s) if (v.degree()==0) v.delete();
		Set <Vertex> K=degree1(g);
		Iterator <Vertex> it=K.iterator();
		Vector <Vertex> L=new Vector <Vertex> ();
		while (it.hasNext()) {
			Vertex V=it.next();
			if (V.degree()!=1) {
				//vertex neighbour may have been deleted during iteration
				assert V.degree()==0;
				continue;
			}
			L.clear();
			L.add(V);
			Vertex W=V.neighbours().next();
			double len=Line.distance(V, W);
			while (len<maxLen) {
				L.add(W);
				if (W.degree()!=2) break;
				Iterator <Vertex> vi=W.neighbours();
				Vertex N=vi.next();
				if (N.equals(L.elementAt(L.size()-2))) N=vi.next();
				len+=Line.distance(W, N);
			}
			L.removeElementAt(L.size()-1);
			for (Vertex v: L) v.delete();
		}
		s=g.getVertices();
		for (Vertex v: s) if (v.degree()==0) v.delete();
	}

	/**
	 * Returns a set containing vertices of the graph which have degree 1.
	 *
	 * <p>The returned Set will contain pointers to Vertex objects that
	 * belong to g.vertices (and not to their copies).</p>
	 *
	 * @param g graph
	 * @return set containing all vertices with degree of 1
	 */
	public static Set <Vertex> degree1(UGraph g) {
		Iterator <Vertex> it=g.vertices();
		Vertex v;
		Set <Vertex> s=new HashSet <Vertex> ();
		while (it.hasNext()) {
			v=it.next();
			if (v.degree()==1) s.add(v);
		}
		return s;
	}

	/**
	 * Returns a set containing vertices of the graph which have degree 3.
	 *
	 * <p>The returned Set will contain pointers to Vertex objects that
	 * belong to g.vertices (and not to their copies).</p>
	 *
	 * @param g graph
	 * @return set containing all vertices with degree of 3
	 */
	public static Set <Vertex> degree3(UGraph g) {
		Iterator <Vertex> it=g.vertices();
		Vertex v;
		Set <Vertex> s=new HashSet <Vertex> ();
		while (it.hasNext()) {
			v=it.next();
			if (v.degree()==3) s.add(v);
		}
		return s;
	}
}
