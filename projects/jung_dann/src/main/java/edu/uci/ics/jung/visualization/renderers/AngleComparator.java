package edu.uci.ics.jung.visualization.renderers;

import java.util.Comparator;
import java.awt.geom.Point2D;

/**
 * Sorts Point2Ds by their angle around a lowest Point2D pivot.
 * 
 * @author Andrea Francke, Olivier Clerc
 */
class AngleComparator implements Comparator<Point2D> {
	Point2D pivot;
	
	/**
	 * Constructs a new angle comparator using (0,0) as pivot point 
	 */	
	AngleComparator(){
		pivot = new Point2D.Double(0,0);
	}
	
	/**
	 * Constructs a new angle comparator using given pivot point
	 * @param pivot
	 */
	AngleComparator(Point2D pivot){
		this.pivot = pivot; 
	}
	
	/**
	 * Sorts Point2Ds by the angle they form with Point2D pivot
	 * respective to the x-axis.
	 * 
	 * @param p1 First point
	 * @param p2 Second point
	 * @return -1 if p1 < p2; <br>
	 * 0 if p1 == p2; <br>
	 * 1 if p1 > p2  
	 */
	public int compare(Point2D p1, Point2D p2) {
		
		double d = BasicHyperedgeRenderer.checkOrientation(pivot, p1, p2);
		
		if (d < 0) /* p2 lies to the right of p1*/{
			return 1;
		} else if (d > 0){
			return -1; 
		} else /*d==0*/{
			double dist1, dist2;
			dist1 = Math.pow(p1.getY()-pivot.getY(), 2) + Math.pow(p1.getX()-pivot.getX(), 2);
			dist2 = Math.pow(p2.getY()-pivot.getY(), 2) + Math.pow(p2.getX()-pivot.getX(), 2);
			if (dist1 < dist2){
				return 1;
			} else if (dist1 > dist2){
				return -1; 
			} else {
				return 0;
			}
		}
	}
}
