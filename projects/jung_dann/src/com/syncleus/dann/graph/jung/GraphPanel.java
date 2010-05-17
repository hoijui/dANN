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
package com.syncleus.dann.graph.jung;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * displays a dANN or JUNG graph in a JUNG visualized window
 */
public class GraphPanel<N, E> extends JPanel {

    double animationPeriod = 0.1;
    public Layout layout;
    public VisualizationViewer<N, E> vis;
    private boolean running;
    private final ShapePickSupport<N,E> shapePicker;
    public final Graph graph;

    public GraphPanel(edu.uci.ics.jung.graph.Graph g, int w, int h) {
        super(new BorderLayout());

        this.graph = g;
        this.running = true;
        
        initLayout(w, h);
        initVis(vis);

        // Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vis.setGraphMouse(gm);

        add(vis, BorderLayout.CENTER);


        shapePicker = new ShapePickSupport<N,E>(vis);
        vis.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                N pickedNode = shapePicker.getVertex(layout, e.getX(), e.getY());
                E pickedEdge = shapePicker.getEdge(layout, e.getX(), e.getY());
                if (pickedNode!=null) {
                    onNodeClicked(pickedNode);
                }
                else if (pickedEdge!=null) {
                    onEdgeClicked(pickedEdge);
                }
            }
        });
        
        if (getAnimationPeriod() > 0) { 
            new Thread(new Runnable() {
                public void run() {
                    //System.out.println("animation starting");

                    while (isRunning()) {
                        if (layout instanceof IterativeContext)
                            ((IterativeContext)layout).step();
                        vis.repaint();
                        try {
                            Thread.sleep((long)(getAnimationPeriod() * 1000.0));
                        } catch (InterruptedException ex) {
                        }
                    }
                    
                    //System.out.println("animation stopping");
                }
            }).start();
        }
    }

    protected void initLayout(int w, int h) {
        layout = new SpringLayout(graph);
        layout.setSize(new Dimension(w, h));

        vis = new VisualizationViewer<N, E>(layout);
        vis.setPreferredSize(new Dimension(w, h));
    }

    /** called when a node is clicked in this panel */
    public void onNodeClicked(N n) {

    }

    /** called when an edge is clicked in this panel */
    public void onEdgeClicked(E e) {
        
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    protected void initVis(VisualizationViewer<N,E> vis) {
        // Show vertex and edge labels
        vis.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vis.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
    }

//    public static void main(String[] args) {
//        {
//            DirectedSparseMultigraph<String, String> g = new DirectedSparseMultigraph();
//            g.addVertex("x");
//            g.addVertex("y");
//            g.addEdge("xy", "x", "y");
//            new GraphPanel(g, 300, 300);
//        }
//
//        {
//            MutableAdjacencyGraph<String, ValueDirectedEdge<String, String>> graph = new MutableAdjacencyGraph();
//            graph.add("x");
//            graph.add("y");
//            graph.add(new ValueDirectedEdge("xy", "x", "y"));
//            new GraphPanel(new JungGraph(graph), 300, 300);
//        }
//
//    }

}
