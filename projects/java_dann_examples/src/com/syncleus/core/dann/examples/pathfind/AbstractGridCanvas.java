/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.core.dann.examples.pathfind;

import com.syncleus.dann.graph.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Displays a WeightedGrid as a Swing component, with support for hiliting individual nodes and edges (borders between nodes).
 * @author seh
 */
public abstract class AbstractGridCanvas extends JPanel implements MouseListener, MouseMotionListener
{

    private final WeightedGrid grid;
    private int nodeSize;
    private int edgeSize;
    private List<SimpleWeightedUndirectedEdge<GridNode>> path;
    private int pathThickness;
    private GridNode touchedNode = null;
    private SimpleWeightedUndirectedEdge<GridNode> touchedEdge = null;
    private final int selectedThickness;
    /** Number of times smaller that the drawn path is than nodes (which it runs through). */
    private final int pathFraction = 4;
    /** Number of times smaller that the drawn border hiliting is than nodes and edges (which it surrounds). */
    private final int borderFraction = 4;
    private static final Color PATH_COLOR = new Color(0.5f, 0f, 0f);

    /**
     * Constructs a Swing component representing a grid and a path according to pixel-sizing parameters.
     * @param shownGrid  the grid displayed by this component
     * @param initialPath the initial path displayed by this component, which may be null
     * @param initialNodeSize size (in pixels) of each displayed node square
     * @param initialEdgeSize size (in pixels) of the width or height of each edge surrounding each node square
     */
    public AbstractGridCanvas(final WeightedGrid shownGrid, final List<SimpleWeightedUndirectedEdge<GridNode>> initialPath, final int initialNodeSize, final int initialEdgeSize)
    {
        super();

        this.grid = shownGrid;
        this.nodeSize = initialNodeSize;
        this.edgeSize = initialEdgeSize;
        this.path = initialPath;
        this.pathThickness = Math.max(1, nodeSize / pathFraction);
        this.selectedThickness = (int) Math.max(1, Math.min(edgeSize, nodeSize) / borderFraction);

        int pw = grid.getWidth() * nodeSize + (grid.getWidth() + 1) * edgeSize;
        int ph = grid.getHeight() * nodeSize + (grid.getHeight() + 1) * edgeSize;
        setPreferredSize(new Dimension(pw, ph));

        addMouseListener(this);
        addMouseMotionListener(this);
    }


    @Override
    public void paint(final Graphics g1)
    {
        super.paint(g1);

        Graphics2D g = (Graphics2D) g1;

        int px = 0, py = 0;
        for (int y = 0; y < grid.getHeight(); y++)
        {
            px = 0;
            for (int x = 0; x < grid.getWidth(); x++)
            {

                GridNode gn = grid.getNode(x, y);

                if (y != 0)
                {
                    GridNode to = grid.getNode(x, y - 1);

                    //draw filled rect for edge
                    WeightedBidirectedEdge<GridNode> upEdge = grid.getEdgeBetween(x, y, x, y - 1);
                    g.setColor(getEdgeColor(upEdge));
                    g.fillRect(px + edgeSize, py, nodeSize, edgeSize);

                    if (touchedEdge != null)
                    {
                        if ((touchedEdge.getRightNode() == gn) && (touchedEdge.getLeftNode() == to))
                        {
                            //draw border of selected edge
                            g.setStroke(new BasicStroke(selectedThickness));
                            g.setColor(/*getTouchedNodeBorderColor()*/Color.ORANGE);
                            g.drawRect(px + edgeSize, py, nodeSize, edgeSize);
                        }
                    }

                }

                if (x != 0)
                {
                    GridNode to = grid.getNode(x - 1, y);

                    //draw filled rect for edge
                    WeightedBidirectedEdge<GridNode> rightEdge = grid.getEdgeBetween(x, y, x - 1, y);
                    g.setColor(getEdgeColor(rightEdge));
                    g.fillRect(px, py + edgeSize, edgeSize, nodeSize);

                    if (touchedEdge != null)
                    {
                        if ((touchedEdge.getRightNode() == gn) && (touchedEdge.getLeftNode() == to))
                        {
                            //draw border of selected edge
                            g.setStroke(new BasicStroke(selectedThickness));
                            g.setColor(/*getTouchedNodeBorderColor()*/Color.ORANGE);
                            g.drawRect(px, py + edgeSize, edgeSize, nodeSize);
                        }
                    }

                }

                g.setColor(getNodeColor(gn));
                g.fillRect(px + edgeSize, py + edgeSize, nodeSize, nodeSize);

                if (gn == touchedNode)
                {
                    g.setStroke(new BasicStroke(selectedThickness));
                    g.setColor(/*getTouchedNodeBorderColor()*/Color.ORANGE);
                    g.drawRect(px + edgeSize, py + edgeSize, nodeSize, nodeSize);
                }

                px += nodeSize + edgeSize;
            }
            py += nodeSize + edgeSize;
        }

        if (path != null)
        {
            g.setColor(PATH_COLOR);

            g.setStroke(new BasicStroke(pathThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int centerOffset = (edgeSize + nodeSize / 2);

            for (BidirectedEdge<GridNode> edge : path)
            {
                int lastX = edge.getLeftNode().getX();
                int lastY = edge.getLeftNode().getY();

                int curX = edge.getRightNode().getX();
                int curY = edge.getRightNode().getY();

                int curPX = getNodePosition(curX) + centerOffset;
                int curPY = getNodePosition(curY) + centerOffset;

                int lastPX = getNodePosition(lastX) + centerOffset;
                int lastPY = getNodePosition(lastY) + centerOffset;



                g.drawLine(lastPX, lastPY, curPX, curPY);

                lastX = curX;
                lastY = curY;
            }
        }
    }

    /**
     * Returns the color associated with a specific grid edge.
     * @param edge the edge to get the color of
     * @return the edge's color
     */
    protected abstract Color getEdgeColor(WeightedBidirectedEdge<GridNode> edge);

    /**
     * Returns the color associated with a specific grid node.
     * @param node the node to get the color of
     * @return the node's color
     */
    protected abstract Color getNodeColor(GridNode node);

    /**
     * Yields the pixel coordinate for a given grid coordinate of a node.  The coordinate may represent either X or Y, since the grid is drawn in 1:1 "square" aspect ratio.
     * @param c the grid coordinate
     * @return pixel coordinate of a given grid position, which may be outside of the grid's bounds
     */
    private int getNodePosition(final int c)
    {
        return c * (nodeSize + edgeSize);
    }

    /**
     * Yields a node's grid coordinate for a given drawn pixel coordinate of a node.  The coordinate may represent either X or Y, since the grid is drawn in 1:1 "square" aspect ratio.
     * @param x pixel coordinate
     * @return grid coordinate of a given pixel position, which may be outside of the grid's bounds
     */
    protected int getNodePositionPixel(final int x)
    {
        return (int) Math.floor(((float) (x)) / ((float) (nodeSize + edgeSize)));
    }

    /**
     * Yields the touched edge corresponding to certain pixel coordinates in the drawn component.
     * @param px x pixel coordinate
     * @param py y pixel coordinate
     * @return the edge corresponding to pixel (px, py), or null if no edge was drawn there
     */
    public SimpleWeightedUndirectedEdge<GridNode> getTouchedEdge(final int px, final int py)
    {
        final int nx = getNodePositionPixel(px);
        final int ny = getNodePositionPixel(py);

        if (nx * (nodeSize + edgeSize) > px + edgeSize)
        {
            return null;
        }

        if (ny * (nodeSize + edgeSize) > py + edgeSize)
        {
            return null;
        }

        if (Math.abs((px - nx * (nodeSize + edgeSize)) - (py - ny * (nodeSize + edgeSize))) < edgeSize)
        {
            return null;
        }

        boolean upOrLeft = (px - nx * (nodeSize + edgeSize) > py - ny * (nodeSize + edgeSize)) ? true : false;

        if (nx >= ((upOrLeft) ? 0 : 1))
        {
            if (ny >= ((!upOrLeft) ? 0 : 1))
            {
                if (nx < grid.getWidth())
                {
                    if (ny < grid.getHeight())
                    {

                        LinkedList<SimpleWeightedUndirectedEdge<GridNode>> thisEdges, otherEdges;
                        thisEdges = new LinkedList(grid.getEdges(grid.getNode(nx, ny)));
                        //System.err.println(nx + " " + ny + " : " + thisEdges);

                        if (upOrLeft)
                        {
                            //up
                            otherEdges = new LinkedList(grid.getEdges(grid.getNode(nx, ny - 1)));
                            //System.err.println(nx + " " + (ny-1) + " : " + otherEdges);
                        }
                        else
                        {
                            //left
                            otherEdges = new LinkedList(grid.getEdges(grid.getNode(nx - 1, ny)));
                            //System.err.println((nx-1) + " " + ny + " : " + otherEdges);
                        }

                        SimpleWeightedUndirectedEdge<GridNode> sharedEdge = null;
                        for (SimpleWeightedUndirectedEdge<GridNode> eedge : thisEdges)
                        {
                            if (otherEdges.contains(eedge))
                            {
                                sharedEdge = eedge;
                                break;
                            }
                        }

                        if (sharedEdge != null)
                        {
                            return sharedEdge;
                        }
                        else
                        {
                            return null;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Yields the touched node corresponding to certain pixel coordinates in the drawn component.
     * @param px x pixel coordinate
     * @param py y pixel coordinate
     * @return the node corresponding to pixel (px, py), or null if no node was drawn there
     */
    public GridNode getTouchedNode(final int px, final int py)
    {
        final int nx = getNodePositionPixel(px);
        final int ny = getNodePositionPixel(py);

        if (nx * (nodeSize + edgeSize) + edgeSize > px)
        {
            return null;
        }
        if (ny * (nodeSize + edgeSize) + edgeSize > py)
        {
            return null;
        }

        if (nx >= 0)
        {
            if (ny >= 0)
            {
                if (nx < grid.getWidth())
                {
                    if (ny < grid.getHeight())
                    {
                        return grid.getNode(nx, ny);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void mouseClicked(final MouseEvent e)
    {
    }

    @Override
    public void mousePressed(final MouseEvent e)
    {
    }

    @Override
    public void mouseReleased(final MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(final MouseEvent e)
    {
    }

    @Override
    public void mouseExited(final MouseEvent e)
    {
    }

    @Override
    public void mouseDragged(final MouseEvent e)
    {
        updateMouseMoved(e);
    }

    /**
     * Updates the current touchedNode or touchedEdge when the mouse is either moved or dragged.
     * @param e mouse event to update according to
     */
    protected void updateMouseMoved(final MouseEvent e)
    {
        GridNode tNode = getTouchedNode(e.getX(), e.getY());

        SimpleWeightedUndirectedEdge<GridNode> tEdge = getTouchedEdge(e.getX(), e.getY());

        this.touchedNode = null;
        this.touchedEdge = null;

        if (tNode != null)
        {
            this.touchedNode = tNode;
        }
        else if (tEdge != null)
        {
            this.touchedEdge = tEdge;
        }

        //System.out.println("touchedNode: " + touchedNode + " , touchedEdge: " + touchedEdge);

        repaint();

    }

    @Override
    public void mouseMoved(final MouseEvent e)
    {
        updateMouseMoved(e);
    }

    /**
     * Sets a different path to draw when the component is redrawn, and then repaint()'s.
     * @param nextPath the next path to draw
     */
    void setPath(final List<SimpleWeightedUndirectedEdge<GridNode>> nextPath)
    {
        this.path = nextPath;
        repaint();
    }

    /**
     * Returns the currently pointer-touched grid Edge.
     * @return currently touched edge, or null if none is touched
     */
    public SimpleWeightedUndirectedEdge<GridNode> getTouchedEdge()
    {
        return touchedEdge;
    }

    /**
     * Returns the currently pointer-touched GridNode.
     * @return currently touched GridNode, or null if none is touched
     */
    public GridNode getTouchedNode()
    {
        return touchedNode;
    }
}
