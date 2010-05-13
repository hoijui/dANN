package edu.uci.ics.jung.algorithms.layout;

import edu.uci.ics.jung.graph.*;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.ArrayList;
import java.awt.Dimension;


//TODO for testing, can be removed later on
import java.awt.geom.Point2D;

/** 
 * This class implements the layout algorithm for hypergraphs. It provides a 
 * framework which allows to apply layout algorithms for normal graphs to 
 * hypergraphs.
 *  
 * @author Andrea Francke, Olivier Clerc
 * @param <V> Type of hypervertex 
 * @param <E> Type of hyperedge
 */
public class HypergraphLayout<V,E> extends AbstractLayout<V,E> {	
	private Layout<V,Integer> underlyingLayout;
	
	/**
	 * Applies provided layout algorithm layoutclass to Hypergraph<V,E> hg and 
	 * provides, besides positional data for the hypergraph vertices, a Graph<V,E> 
	 * accessible by getGraph() which in turn provides access to the laid out 
	 * hypergraph by getGraph().getHypergraph()
	 * 
	 * @param hg Hypergraph that is to be laid out
	 * @param layoutclass Class object of a normal graph layout, which is to be applied 
	 * to the hypergraph.
	 */	
	@SuppressWarnings("unchecked") // suppress unavoidable warnings related to generics
	public HypergraphLayout(Hypergraph<V,E> hg, Class<? extends AbstractLayout> layoutclass) {
		
		// call constructor of superclass with pseudo-hypergraph as argument
		super(new PseudoHypergraph<V, E>(hg));
		
		// generate corresponding clique graph of hypergraph
		Graph<V,Integer> cliqueGraph = getCliqueGraphFromHypergraph(hg);
		
		try {			
			// get constructor that takes a graph as only argument
			Constructor<? extends Layout> constructor = layoutclass.getConstructor(Graph.class);
			
			// instantiate the layout using this constructor
			this.underlyingLayout = constructor.newInstance(cliqueGraph);
			
		} catch (Exception e){
			System.out.println("Instantiation of AbstractLayout "+layoutclass.getName()+" failed. \n Using default layout FRLayout instead.");
			underlyingLayout = new FRLayout<V,Integer>(cliqueGraph);
		}
	}
	
	/**
	 * Method to delegate vertex-to-point-transformation to the underlying layout. 
	 */
	@Override
	public Point2D transform(V v) {
		Point2D result = underlyingLayout.transform(v);
		return result;
	}	
	
	/**
	 * Constructs an auxiliary normal graph that reproduces the adjacencies
	 * in a hypergraph in terms of binary edges: each hyperedge of size k is represented
	 * by a subgraph on the same vertices isomorphic to a k-clique. 
	 * As for each hyperedge of size k, O(k^2) edges are added, usage is 
	 * not recommended for graphs with large hyperedges. 
	 * 
	 * @param hg A hypergraph that is to be laid out
	 * @return A graph g which can be laid out using a standard layout; 
	 * V(g) = V(hg), E(hg) c E(g)
	 */
	public static <V,E> Graph<V,Integer> getCliqueGraphFromHypergraph(Hypergraph<V,E> hg){
		Graph<V,Integer> result = new SparseMultigraph<V,Integer>();
		for (V v: (Collection<V>) hg.getVertices()){
			result.addVertex(v);
		}
		
		for (E e:hg.getEdges()){
			ArrayList<V> a = new ArrayList<V>(hg.getIncidentVertices(e));
			for (int i = 0; i < a.size(); i++){
				for (int j = i+1; j < a.size(); j++){
					/* 
					 * as java just truncates too large numbers, 
					 * overflow doesn't have to be treated for our purposes
					 */
					Integer k = a.get(i).hashCode()*a.get(j).hashCode();
					while (result.containsEdge(k)){
						k++;
					}
					result.addEdge(k,a.get(i),a.get(j));
				}
			}
		}
		return result;
	}
	
	/**
	 * Initialize the layout. 
	 */
	public void initialize() {
		this.underlyingLayout.initialize();
	}

	/**
	 * Set the size of the canvas for the layout. 
	 */
	@Override
	public void setSize(Dimension size) {
		this.underlyingLayout.setSize(size); 
	}

	/**
	 * Reset the layout 
	 */
	public void reset() {
		this.underlyingLayout.reset();
	}
}