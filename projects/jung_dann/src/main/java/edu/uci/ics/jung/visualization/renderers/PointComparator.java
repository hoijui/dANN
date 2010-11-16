package edu.uci.ics.jung.visualization.renderers;

import java.util.Comparator;
import java.awt.geom.Point2D;

/**
 * Lexicographical ordering of Point2Ds. Sorting by y-coordinate first, 
 * then by x-coordinate
 * 
 * @author Andrea Francke, Olivier Clerc
 */
class PointComparator implements Comparator<Point2D> {

	/**
	 * Lexicographical comparison of points. Sorting by y-coordinate first, 
	 * then by x-coordinate.
	 * @param p1 first point
	 * @param p2 second point
	 * @return -1 if p1 < p2; <br>
	 * 0 if p1 == p2; <br>
	 * 1 if p1 > p2  
	 */
	public int compare(Point2D p1, Point2D p2) {
		
	    if (p1.getY() < p2.getY()){
	    	return -1;
	    } else if (p1.getY() == p2.getY()){
	    	if (p1.getX() < p2.getX()){
	    		return -1;
	    	} else if (p1.getX() == p2.getX()){
	    		return 0;
	    	} else /*p1.Y == p2.Y AND p1.X > p2.X*/ {
	    		return 1;
	    	}
	    } else /*p1.getY() > p2.getY()*/ {
	    	return 1;
	    }
	}
}