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

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * displays a dANN or JUNG graph in a JUNG visualized window
 */
public class GraphPanel<N, E> extends JPanel {

    double animationPeriod = 0.1;
    public final SpringLayout layout;
    public final VisualizationViewer<N, E> vis;
    private boolean running;

    public GraphPanel(edu.uci.ics.jung.graph.Graph g, int w, int h) {
        super(new BorderLayout());

        running = true;
        
        layout = new SpringLayout(g);
        layout.setSize(new Dimension(w, h));

        vis = new VisualizationViewer<N, E>(layout);
        vis.setPreferredSize(new Dimension(w, h));

        initVis(vis);

        // Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vis.setGraphMouse(gm);

        add(vis, BorderLayout.CENTER);

        if (getAnimationPeriod() > 0) { 
            new Thread(new Runnable() {
                public void run() {
                    //System.out.println("animation starting");

                    while (isRunning()) {
                        layout.step();
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
