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

import com.syncleus.dann.graph.MutableAdjacencyGraph;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * displays a dANN or JUNG graph in a JUNG visualized window
 */
public class GraphWindow<N, E> {

    double animationPeriod = 0.1;
    public final SpringLayout2 layout;
    public final VisualizationViewer<N, E> vis;

    public GraphWindow(edu.uci.ics.jung.graph.Graph g, int w, int h) {
        layout = new SpringLayout2(g);
        layout.setSize(new Dimension(w, h));

        vis = new VisualizationViewer<N, E>(layout);
        vis.setPreferredSize(new Dimension(w, h));

        initVis(vis);

        // Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vis.setGraphMouse(gm);

        JFrame frame = new JFrame("Interactive Graph View 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vis);
        frame.pack();
        frame.setVisible(true);

        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    layout.step();
                    vis.repaint();
                    try {
                        Thread.sleep((long)(animationPeriod * 1000.0));
                    } catch (InterruptedException ex) {
                    }
                }

            }
        }).start();
    }

    protected void initVis(VisualizationViewer<N,E> vis) {
        // Show vertex and edge labels
        vis.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vis.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
    }

    public static void main(String[] args) {
        {
            DirectedSparseMultigraph<String, String> g = new DirectedSparseMultigraph();
            g.addVertex("x");
            g.addVertex("y");
            g.addEdge("xy", "x", "y");
            new GraphWindow(g, 300, 300);
        }

        {
            MutableAdjacencyGraph<String, ValueDirectedEdge<String, String>> graph = new MutableAdjacencyGraph();
            graph.add("x");
            graph.add("y");
            graph.add(new ValueDirectedEdge("xy", "x", "y"));
            new GraphWindow(new JungGraph(graph), 300, 300);
        }

    }
}
