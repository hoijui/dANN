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

import com.syncleus.dann.graph.search.pathfinding.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import com.syncleus.dann.graph.*;
import java.util.List;

/**
 * Demonstrates Path Finding across a Weighted Grid.  The Grid's node and edge weights can be adjusted by clicking.
 * The start and stop positions of the path to be found can also be specified.
 * @author seh
 */
public class PathFindDemoPanel extends JPanel
{

    private double[][] gridWeights;
    private AbstractGridCanvas gridCanvas;
    private int w;
    private int h;
    private WeightedGrid grid;
    private PathFindControlPanel controlPanel;
    private List<SimpleWeightedUndirectedEdge<GridNode>> path;
    private GridNode endNode;
    private GridNode startNode;
    private double paintWeight = 0;

    private static final double INF = Double.POSITIVE_INFINITY;
    private static final double MAX_NONINFINITE_CELL_WEIGHT = 12.0;
    private static final int DEFAULT_NODE_SIZE = 24;
    private static final int DEFAULT_EDGE_SIZE = 8;
    private static final int DEFAULT_GRID_SIZE = 18;
    private static final double MIN_GRID_WEIGHT = 1.0;
    private static final double INITIAL_GRID_WEIGHT = MIN_GRID_WEIGHT;

    private static final double PAINT_WEIGHTS [] =
    {
        1, 2, 4, 6, 8, 10, INF
    };

    /** Distance heuristic used by the AStarPathFinding algorithm. */
    private static class DistanceHeuristic implements HeuristicPathCost<GridNode>
    {

        public double getHeuristicPathCost(final GridNode begin, final GridNode end)
        {
            return begin.calculateRelativeTo(end).getDistance();
        }

        public boolean isOptimistic()
        {
            return true;
        }

        public boolean isConsistent()
        {
            return true;
        }
    }

    /**
     * A panel that provides buttons for each of the drawable "weights".
     */
    private class DrawingPanel extends JPanel
    {

        private class PaintButton extends JToggleButton implements ActionListener
        {

            private static final int BUTTON_BORDER_SIZE = 4;
            private final double weight;

            public PaintButton(final double drawWeight)
            {
                super("  ");
                this.weight = drawWeight;
                setForeground(getColor(weight));
                addActionListener(this);
            }

            @Override
            public void paint(final Graphics g1)
            {
                super.paint(g1);
                final int b = BUTTON_BORDER_SIZE;
                Graphics2D g = (Graphics2D) g1;
                g.setColor(getColor(weight));
                g.fillRect(b, b, getWidth() - b * 2, getHeight() - b * 2);
            }

            public double getWeight()
            {
                return weight;
            }

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                if (isSelected())
                {
                    paintWeight = weight;
                    unpopOthers(this);
                }
            }
        }

        private final List<PaintButton> paintButtons = new LinkedList();

        public DrawingPanel(final double[] paintWeights)
        {
            super(new FlowLayout(FlowLayout.LEFT));

            for (double d : paintWeights)
            {
                final PaintButton pb = new PaintButton(d);
                paintButtons.add(pb);
                add(pb);
            }

        }

        protected void unpopOthers(final PaintButton otherThan)
        {
            for (PaintButton pb : paintButtons)
            {
                if (pb != otherThan)
                {
                    pb.setSelected(false);
                }
            }
        }

        public List<PaintButton> getPaintButtons()
        {
            return paintButtons;
        }

    }

    private class PathFindControlPanel extends JTabbedPane
    {

        public PathFindControlPanel()
        {
            super();

            addTab("Edit", new DrawingPanel(PAINT_WEIGHTS));
            addTab("Start Position", new JLabel("Click a start position"));
            addTab("Stop Position", new JLabel("Click a stop position"));
            //addTab("Settings", new SettingsPanel());

        }
    }

    /**
     * Creates a new Swing component with an empty square grid of size width*width.
     * @param width number of grid nodes wide and high
     */
    public PathFindDemoPanel(final int width)
    {
        super(new BorderLayout());

        reinit(width);

    }

    /**
     * Reconstructs the grid and the panel (and its components) with new grid dimensions.
     * @param nextWidth the width and height (square) dimensions of the grid to (re-)initialize.
     */
    protected void reinit(final int nextWidth)
    {
        removeAll();

        this.w = nextWidth;
        this.h = nextWidth;

        gridWeights = new double[h][];
        for (int i = 0; i < h; i++)
        {
            gridWeights[i] = new double[w];
        }

        grid = new WeightedGrid(gridWeights);
        grid.setAll(INITIAL_GRID_WEIGHT);

        startNode = grid.getNode(0, 0);
        endNode = grid.getNode(w - 1, h - 1);

        updatePath();

        controlPanel = new PathFindControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        gridCanvas = new AbstractGridCanvas(grid, path, DEFAULT_NODE_SIZE, DEFAULT_EDGE_SIZE)
        {
            private boolean mouseDown = false;

            public boolean isMouseDown()
            {
                return mouseDown;
            }

            @Override
            public Color getNodeColor(final GridNode node)
            {
                return getColor(node.getWeight());
            }

            @Override
            protected Color getEdgeColor(final WeightedBidirectedEdge<GridNode> edge)
            {
                return getColor(edge.getWeight());
            }

            protected void paintCells()
            {
                final int is = controlPanel.getSelectedIndex();

                if (mouseDown)
                {
                    if (is == 0)
                    {
                        if (getTouchedNode() != null)
                        {
                            getTouchedNode().setWeight(paintWeight);
                            updatePath();
                        }
                        else if (getTouchedEdge() != null)
                        {
                            getTouchedEdge().setWeight(paintWeight);
                            updatePath();
                        }
                    }
                }

            }

            @Override
            public void mouseDragged(final MouseEvent e)
            {
                super.mouseDragged(e);
                paintCells();
            }

            @Override
            public void mouseMoved(final MouseEvent e)
            {
                super.mouseMoved(e);
                paintCells();
            }

            @Override
            public void mousePressed(final MouseEvent e)
            {
                super.mousePressed(e);
                mouseDown = true;
            }

            @Override
            public void mouseReleased(final MouseEvent e)
            {
                super.mouseReleased(e);
                mouseDown = false;
            }

            @Override
            public void mouseClicked(final MouseEvent e)
            {
                int is = controlPanel.getSelectedIndex();

                GridNode touchedNode = getTouchedNode();

                //starting position
                if (is == 1)
                {
                    if (touchedNode != null)
                    {
                        if (getTouchedNode() != endNode)
                        {
                            startNode = touchedNode;
                            updatePath();
                        }
                        else
                        {
                            warnDifferentLocations();
                        }
                    }
                } //ending position
                else if (is == 2)
                {
                    if (touchedNode != null)
                    {
                        if (touchedNode != startNode)
                        {
                            endNode = touchedNode;
                            updatePath();
                        }
                        else
                        {
                            warnDifferentLocations();
                        }
                    }
                } //paint the map

                mouseDown = true;
                paintCells();
                mouseDown = false;
            }

            private void warnDifferentLocations()
            {
                JOptionPane.showMessageDialog(this, "Start and stop locations must be different");
            }
        };


        add(new JScrollPane(gridCanvas), BorderLayout.CENTER);

    }

    /**
     * Returns a color associated with a specific weight value.  This is used to draw a grayscale gradient for the grid's weights.
     * @param weight value
     * @return a color value
     */
    public Color getColor(final double weight)
    {
        if (weight == MIN_GRID_WEIGHT)
        {
            return Color.WHITE;
        }
        if (weight == Double.POSITIVE_INFINITY)
        {
            return Color.BLACK;
        }
        else if (weight < MAX_NONINFINITE_CELL_WEIGHT)
        {
            float ii = 1f - (float) (weight / MAX_NONINFINITE_CELL_WEIGHT) / 2f;
            return new Color(ii, ii, ii);
        }
        return Color.WHITE;
    }

    /**
     * Updates the path when the starting or ending node changes, or when the AStarPathFinder parameters are changed.
     */
    protected void updatePath()
    {
        AstarPathFinder<GridNode, SimpleWeightedUndirectedEdge<GridNode>> pathFinder = new AstarPathFinder<GridNode, SimpleWeightedUndirectedEdge<GridNode>>(grid, new DistanceHeuristic());
        path = pathFinder.getBestPath(startNode, endNode);
        if (gridCanvas != null)
        {
            gridCanvas.setPath(path);
        }
    }

    /**
     * Entrypoint.
     * @param args (not presently used)
     */
    public static void main(final String[] args)
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            public void run()
            {
                JFrame jf = new JFrame("dANN Path Finding Demo");
                jf.getContentPane().add(new PathFindDemoPanel(DEFAULT_GRID_SIZE));
                jf.addWindowListener(new WindowAdapter()
                {

                    @Override
                    public void windowClosing(final WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                jf.setVisible(true);
                jf.setExtendedState(JFrame.MAXIMIZED_BOTH);

            }
        }
        );
    }

    static
    {

        // Install the look and feel
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
        }
    }
}
