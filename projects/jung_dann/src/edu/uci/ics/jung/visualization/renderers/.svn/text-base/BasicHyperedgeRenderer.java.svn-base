package edu.uci.ics.jung.visualization.renderers;

import java.awt.BasicStroke;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.PseudoHypergraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeRenderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

/**
 * This class implements a basic hyperedge render. Hyperedges are drawn as organic shapes.
 * 
 * @author Andrea Francke, Olivier Clerc
 * @param <V> Type of hypervertex 
 * @param <E> Type of hyperedge
 */
public class BasicHyperedgeRenderer<V,E> extends BasicEdgeRenderer<V,E> {
	
	/**
	 * Render a hyperedge.
	 */
	@Override
	protected void drawSimpleEdge(RenderContext<V,E> rc, Layout<V,E> layout, E e) {		
		
		// get graph from layout
		Graph<V,E> g = layout.getGraph();
		
		// get pseudo-hypergraph
		if(!(g instanceof PseudoHypergraph))
			throw new Error("renderer requires pseudo-hypergraph");

		Hypergraph<V, E> hg = ((PseudoHypergraph<V,E>) g).getHypergraph();
		
		// get index of hyperedge
		int edgeIndex = BasicHypergraphRenderer.indexOfHyperedge(hg, e);
		
		// determine radius		
		int MAX_RADIUS = 60;
		int MIN_RADIUS = 20;
		
		int radius = (int) ((double) MIN_RADIUS + ((double) (MAX_RADIUS - MIN_RADIUS) * (double) edgeIndex / (double) hg.getEdgeCount()));
		
		// if this hyperedge shares no hypervertices, there is nothing to draw
		if(hg.getIncidentVertices(e).size() == 0)
			return;
		
		// get graphics decorator from render context
		GraphicsDecorator gd = rc.getGraphicsContext();

		// construct list of points that correspond to the hypervertices using the layout
		List<Point2D> points = new ArrayList<Point2D>();
		for(V v : hg.getIncidentVertices(e)) {
			Point2D p = layout.transform(v);
			p = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p);
			points.add(p);
		}
				
		Shape shape = organicShape(points, radius);
		
		//store original, non-transparent Composite
		Composite originalComposite = gd.getComposite();
		gd.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15F));
		
		// set line thickness
		gd.setStroke(new BasicStroke(2));

		// draw filled shape
		
		gd.setColor(BasicHypergraphRenderer.colorFromIndex(edgeIndex, true));
		gd.fill(shape);
		
		// draw shape border
		gd.setColor(BasicHypergraphRenderer.colorFromIndex(edgeIndex, true));
		gd.draw(shape);
		
		//reset Composite to original Composite in order to draw
		// vertices et al. in a non-transparent way
		gd.setComposite(originalComposite);
	}

	/**
	 * Return a pair of points, that is shifted in parallel by a certain amount. Looking 
	 * from a to b, the points are shifted to the left hand side. (in the Swing coordinate 
	 * system).
	 * 
	 * @param a The first point
	 * @param b The second point
	 * @param amount The amount of shifting 
	 * @return The shifted pair of points
	 */
	private static Pair<Point2D> shiftPoints(Point2D a, Point2D b, double amount) {
		
		// calculate d = a - b
		Point2D d = new Point2D.Double(a.getX() - b.getX(), a.getY() - b.getY()); 
		
		// calculate d2 = d rotated by 90 degrees counter-clockwise
		Point2D d2 = new Point2D.Double(-d.getY(), d.getX());
		
		// calculate norm of d2
		double norm = Math.sqrt(d2.getX() * d2.getX() + d2.getY() * d2.getY());
		
		// calculate shift vector
		Point2D s = new Point2D.Double(
				d2.getX() / norm * amount,
				d2.getY() / norm * amount);
		
		// add shift vector to points
		Point2D aNew = new Point2D.Double(a.getX() + s.getX(), a.getY() + s.getY());
		Point2D bNew = new Point2D.Double(b.getX() + s.getX(), b.getY() + s.getY());
		
		return new Pair<Point2D>(aNew, bNew);
	}

	/**
	 * Return the angle of a vector. The angle starts at the positive x-axis and increases
	 * clockwise (in the Swing coordinate system).
	 * 
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @return The angle (from 0 to 2*pi)
	 */
	private static double angle(double x, double y) {
		double result = Math.atan2(y, x);
		
		if(result < 0)
			result += 2 * Math.PI;
		
		return result;
	}
	
	/**
	 * Computes the orientation of an angle (j, k, l) at k.
	 * 
	 * @param j point on first ray (k,j) of angle to be checked
	 * @param k vertex of angle to be checked
	 * @param l point on second ray (k,l) of angle to be checked
	 * @return < 0 if l lies to the right of (j,k) (in a standard coordinate system); <br>
	 * = 0 if (j,l) and (j,k) are collinear; <br>
	 * > 0 if l lies to the left  of (j,k) (in a standard coordinate system)
	 */
	static double checkOrientation(Point2D j, Point2D k, Point2D l){
		/*
		 * to compute the orientation of angle (j,k,l) at k, 
		 * the cross product (k-j)x(l-j) is used
		 */
		return (((k.getX()-j.getX())*(l.getY()-j.getY())) - ((k.getY()-j.getY())*(l.getX()-j.getX())));
	}
	
	/** 
	 * Sorts a list of points in place by their angle around a pivot 
	 * with minimal y-coordinate among all points.
	 * for pairs of points that have the same angle with the pivot, 
	 * the point nearer to the pivot is removed.
	 * @param points List of points to sort
	 * pivot::(sortedList of (points \ {pivot}))
	 */
	private static void sortPoints(List<Point2D> points){
		
		if (points.size() <= 1)
			return; 
		/*points.size() >= 2*/
		
		//find and remove pivot element (removed once, double occurrences aren't removed)
		Point2D pivot = (Point2D) Collections.min(points, new PointComparator());
		points.remove(pivot);
		
		//sort points \ {pivot} by angles around pivot
		if (points.size() > 1){
			AngleComparator angleComp = new AngleComparator(pivot);
			Collections.sort(points, angleComp);
			
			//eliminating nearer point of pairs with equal angle
			double dist1, dist2; 
			int i = 0; 
			while (i< (points.size()-1)) {
				if (checkOrientation(pivot,points.get(i), points.get(i+1))==0){
					dist1 = Math.pow(points.get(i).getY()-pivot.getY(), 2) + Math.pow(points.get(i).getX()-pivot.getX(), 2);
					dist2 = Math.pow(points.get(i+1).getY()-pivot.getY(), 2) + Math.pow(points.get(i+1).getX()-pivot.getX(), 2);
					if (dist1 <= dist2){
						points.remove(i);
					} else {
						points.remove(i+1);
					}
				} else {
					i++;
				}
			}
		}
		points.add(0,pivot);
		return;
	}
	
	/**
	 * Computes the convex hull of a list of points.
	 * 
	 * @param points A list of points (not necessarily in general position)
	 * @return The convex hull as a list of points sorted counter-clockwise.
	 */
	private static List<Point2D> convexHull(List<Point2D> points) {
		/*
		 * uses Graham's algorithm to compute the convex hull
		 */
		
		List<Point2D> result = new LinkedList<Point2D>(points);
		sortPoints(result);		
		
		/*
		 * for result.size() <= 3, the conv. hull is determined
		 * (clear for 0, 1 and 2; 
		 * if result.size()==3, the points can't be collinear, 
		 * otherwise the point in the middle would have been removed while sorting
		 * -> all 3 points on convex hull -> hull is determined, too)
		 */
		if (result.size()<= 3)
			return result;
		/* points.size() >= 4*/
			
		/*
		 * eliminate points from result that do not belong to the convex hull
		 * by "successive local repair". 
		 * convexity of the first vertex is guaranteed by the lexicographical 
		 * minimality of the pivot. Also, the second vertex always belongs to the 
		 * convex hull. Thus, the algorithm can start at i=0 (instead of i=-1), 
		 * and backtracking beyond i=0 won't be necessary
		 */
		int i = 0; 
		while((result.size()>2)&& (i<result.size()-2)){
			if (checkOrientation(result.get(i), result.get(i+1), result.get(i+2))<= 0){
				result.remove(i+1);
				if (i>0){
					i--;
				}
			} else {
				i++;
			}
		}
		return result;
	}
	
	/**
	 * Return a closed organic shape around a list of points.
	 * 
	 * @param points A list of points
	 * @param radius The maximum distance the shape can have from the points.
	 * @return The shape
	 */
	private static Shape organicShape(List<Point2D> points, double radius) {
		final int ARC_STEPS = 36;
		final int BORDER_STEPS = 36;
		final double DISPLACEMENT_FACTOR = -0.5;
		
		if(points.size() == 1) {
			Point2D p = points.get(0);
			
			// for a single point a circle is returned
			return new Ellipse2D.Double(
					p.getX() - radius,
					p.getY() - radius,
					radius * 2,
					radius * 2);
		}
		
		// points.size() >= 2
		
		// calculate convex hull of points
		points = convexHull(points);
		
		// construct list of shifted point pairs
		List<Pair<Point2D>> shiftedPairs = new ArrayList<Pair<Point2D>>();
		for(int i = 0; i < points.size(); i++) {
			Point2D a = points.get(i);
			Point2D b = points.get((i + 1) % points.size());
			
			// calculate shifted points
			Pair<Point2D> pair = shiftPoints(a, b, radius);
			
			shiftedPairs.add(pair);
		}
		
		Polygon result = new Polygon();
		
		for(int i = 0; i < points.size(); i++) {
			
			// add points for arc
			Point2D p = points.get(i);
			
			Point2D a = shiftedPairs.get((i - 1 + shiftedPairs.size()) % shiftedPairs.size()).getSecond();
			Point2D b = shiftedPairs.get(i).getFirst();
			
			double angleA = angle(a.getX() - p.getX(), a.getY() - p.getY());
			double angleB = angle(b.getX() - p.getX(), b.getY() - p.getY());
			
			if(angleB < angleA)
				angleB += 2 * Math.PI;
							
			for(int j = 1; j < ARC_STEPS; j++) {
				double angle = angleA + (angleB - angleA) / ARC_STEPS * j;
				
				Point2D c = new Point2D.Double(
						p.getX() + Math.cos(angle) * radius,
						p.getY() + Math.sin(angle) * radius
						); 
			
				result.addPoint((int) c.getX(), (int) c.getY());
			}
			
			// add points for border
			Point2D u = shiftedPairs.get(i).getFirst();
			Point2D v = shiftedPairs.get(i).getSecond();
			
			result.addPoint((int) u.getX(), (int) u.getY());

			Point2D r = new Point2D.Double(
				     v.getY() - u.getY(), 
				     -v.getX() + u.getX()
				);
			
			double norm = Math.sqrt(r.getX() * r.getX() + r.getY() * r.getY());
			
			Point2D d = new Point2D.Double(
					r.getX() / norm * radius,
					r.getY() / norm * radius
				);
			
			for(int j = 1; j < BORDER_STEPS; j++) {
				double displacement = Math.sin((double) j / BORDER_STEPS * Math.PI);
				displacement = DISPLACEMENT_FACTOR * displacement * displacement;
				
				Point2D s = new Point2D.Double(
					     u.getX() + (v.getX() - u.getX()) * j / BORDER_STEPS,
					     u.getY() + (v.getY() - u.getY()) * j / BORDER_STEPS
				);
				
				Point2D c = new Point2D.Double(
						s.getX() + displacement * d.getX(),
						s.getY() + displacement * d.getY()
				);
				
				result.addPoint((int) c.getX(), (int) c.getY());
			}
			
			result.addPoint((int) v.getX(), (int) v.getY());
		}
		
		return result;
	}
	
}