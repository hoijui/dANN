package edu.uci.ics.jung.visualization.renderers;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.PseudoHypergraph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

/**
 * This class impements a basic hypervertex renderer. Hypervertices are drawn as pie charts.
 * 
 * @author Andrea Francke, Olivier Clerc
 * @param <V> Type of hypervertex 
 * @param <E> Type of hyperedge
 */
public class BasicHypervertexRenderer<V,E> extends BasicVertexRenderer<V,E> {
	
	/**
	 * Render a hypervertex.
	 */
	@Override
	protected void paintIconForVertex(RenderContext<V,E> rc, V v, Layout<V,E> layout) {
		final int RADIUS = 10;
		
		// get graph from layout
		Graph<V,E> g = layout.getGraph();
		
		// get pseudo-hypergraph
		if(!(g instanceof PseudoHypergraph))
			throw new Error("renderer requires pseudo-hypergraph");

		Hypergraph<V, E> hg = ((PseudoHypergraph<V,E>) g).getHypergraph();
		
		// get graphics decorator from render context
		GraphicsDecorator gd = rc.getGraphicsContext();
		
		// construct list of hyperedge indices for all involved hyperedges
		ArrayList<Integer> edgeIndices = new ArrayList<Integer>();
		for(E e : hg.getIncidentEdges(v))
			edgeIndices.add(BasicHypergraphRenderer.indexOfHyperedge(hg, e));
		
		// get point of hypervertex using the layout
		Point2D p = layout.transform(v);
		p = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p);

		// get coordinates of point
		int x = (int) p.getX();
		int y = (int) p.getY();
		
		// draw color indices as pie chart around point		  
		if(edgeIndices.size() > 0) {
			double arcAngle = 360.0 / edgeIndices.size();
			
			// draw 'pieces of pie'
			for(int i = 0; i < edgeIndices.size(); i++) {
				gd.setColor(BasicHypergraphRenderer.colorFromIndex(edgeIndices.get(i), true));
				gd.fillArc(
						x - RADIUS, y - RADIUS,
						RADIUS * 2, RADIUS * 2,
						(int) (arcAngle * i), (int) arcAngle
						);
			}
			
			// draw 'spokes' between pieces
			if(edgeIndices.size() > 1) {		
				for(int i = 0; i < edgeIndices.size(); i++) {
					double spokeAngle = arcAngle * i / 180 * Math.PI; 
					gd.setColor(Color.BLACK);
					gd.drawLine(x, y,
							(int) (x + RADIUS * Math.cos(spokeAngle)),
							(int) (y - RADIUS * Math.sin(spokeAngle)));
				}
			}
		
		} else {
			// draw filled white circle
			gd.setColor(Color.WHITE);
			gd.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
		}
		
		// draw black circle
		gd.setColor(Color.BLACK);
		gd.drawOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
	}
}
