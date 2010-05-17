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
package com.syncleus.dann.graph.jung.test;

import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.MutableAdjacencyGraph;
import com.syncleus.dann.graph.jung.BayesianPanel;
import com.syncleus.dann.graph.jung.BrainPanel;
import com.syncleus.dann.graph.jung.GraphPanel;
import com.syncleus.dann.graph.jung.JungGraph;
import com.syncleus.dann.graph.jung.ValueDirectedEdge;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianNetwork;
import com.syncleus.dann.neural.Synapse;
import com.syncleus.dann.neural.activation.ActivationFunction;
import com.syncleus.dann.neural.activation.SineActivationFunction;
import com.syncleus.dann.neural.backprop.brain.BackpropBrain;
import com.syncleus.dann.neural.backprop.brain.FullyConnectedFeedforwardBrain;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author seh
 */
public class DemoJungGraphVis extends JPanel {

    private final JPanel contentPanel;
    private Graph graph;
    private GraphPanel graphPanel;
    int graphPanelWidth = 600;
    int graphPanelHeight = 400;

    public class ViewGraphButton extends JButton implements ActionListener {

        private final Graph graph;

        public ViewGraphButton(String name, Graph g) {
            super(name);

            this.graph = g;

            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            setGraph(graph);
        }
    }

    public DemoJungGraphVis() {
        super(new BorderLayout());

        JPanel menu = new JPanel(new FlowLayout());
        add(menu, BorderLayout.NORTH);

        {
            JButton undirectedGraph = new ViewGraphButton("Undirected", null);
            menu.add(undirectedGraph);

            JButton directedGraph = new ViewGraphButton("Directed", newDirectedGraph());
            menu.add(directedGraph);

            JButton hyperGraph = new ViewGraphButton("Hypergraph", null);
            menu.add(hyperGraph);

            JButton feedForward = new ViewGraphButton("Feedforward", newFeedForwardNetwork());
            menu.add(feedForward);

            JButton bayesian = new ViewGraphButton("Bayesian", new ExampleBayesianNetwork());
            menu.add(bayesian);

            JButton markov = new ViewGraphButton("Markov", null);
            menu.add(markov);

        }

        contentPanel = new JPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    public void setGraph(Graph graph) {
        if (graphPanel != null) {
            graphPanel.stop();
        }
        contentPanel.removeAll();

        this.graph = graph;


        if (graph != null) {
            if (graph instanceof BackpropBrain) {
                graphPanel = new BrainPanel((BackpropBrain) graph, graphPanelWidth, graphPanelHeight);
            } else if (graph instanceof BayesianNetwork) {
                graphPanel = new BayesianPanel((BayesianNetwork)graph, graphPanelWidth, graphPanelHeight);
            } else {
                graphPanel = new GraphPanel(new JungGraph(graph), graphPanelWidth, graphPanelHeight);
            }

            contentPanel.add(graphPanel, BorderLayout.CENTER);
        } else {
            contentPanel.add(new JLabel("not implemented yet"), BorderLayout.CENTER);
        }

        contentPanel.updateUI();
    }

    public static Graph newDirectedGraph() {
        MutableAdjacencyGraph<String, ValueDirectedEdge<String, String>> graph = new MutableAdjacencyGraph();
        graph.add("x");
        graph.add("y");
        graph.add(new ValueDirectedEdge("xy", "x", "y"));
        return graph;
    }

    public static Graph newFeedForwardNetwork() {
        double learningRate = 0.0175;
        ActivationFunction activationFunction = new SineActivationFunction();

        final FullyConnectedFeedforwardBrain brain = new FullyConnectedFeedforwardBrain(new int[]{8, 6, 4}, learningRate, activationFunction);
        {
            //randomize the brain
            for (Synapse s : brain.getEdges()) {
                s.setWeight(Math.random() * 2.0 - 1.0);
            }
        }
        return brain;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("dANN Graph Visualization Demo");
        f.getContentPane().add(new DemoJungGraphVis());
        f.setSize(800, 600);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
