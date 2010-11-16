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

import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.HyperEdge;
import com.syncleus.dann.graph.HyperGraph;
import com.syncleus.dann.graph.ImmutableHyperEdge;
import com.syncleus.dann.graph.ImmutableUndirectedEdge;
import com.syncleus.dann.graph.MutableAdjacencyGraph;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import com.syncleus.dann.graph.MutableHyperAdjacencyGraph;
import com.syncleus.dann.graph.jung.BayesianPanel;
import com.syncleus.dann.graph.jung.BrainPanel;
import com.syncleus.dann.graph.jung.GraphPanel;
import com.syncleus.dann.graph.jung.HypergraphPanel;
import com.syncleus.dann.graph.jung.JungGraph;
import com.syncleus.dann.graph.jung.ValueDirectedEdge;
import com.syncleus.dann.graph.jung.example.ExampleBayesianNetwork;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianNetwork;
import com.syncleus.dann.neural.Synapse;
import com.syncleus.dann.neural.activation.ActivationFunction;
import com.syncleus.dann.neural.activation.SineActivationFunction;
import com.syncleus.dann.neural.backprop.InputBackpropNeuron;
import com.syncleus.dann.neural.backprop.OutputBackpropNeuron;
import com.syncleus.dann.neural.backprop.BackpropNeuron;
import com.syncleus.dann.neural.backprop.brain.BackpropBrain;
import com.syncleus.dann.neural.backprop.brain.FullyConnectedFeedforwardBrain;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author seh
 */
public class Main extends JPanel {

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

    public Main() {
        super(new BorderLayout());

        JPanel menu = new JPanel(new FlowLayout());
        add(menu, BorderLayout.NORTH);

        {
            JButton undirectedGraph = new ViewGraphButton("Undirected", newUndirectedGraph());
            menu.add(undirectedGraph);

            JButton directedGraph = new ViewGraphButton("Directed", newDirectedGraph());
            menu.add(directedGraph);

            JButton hyperGraph = new ViewGraphButton("Hypergraph", newHyperGraph());
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

        graphPanel = null;

        if (graph != null) {
            if (graph instanceof HyperGraph) {
                graphPanel = new HypergraphPanel((HyperGraph)graph, graphPanelWidth, graphPanelHeight);
            } else if (graph instanceof BackpropBrain) {
                graphPanel = new BrainPanel((BackpropBrain) graph, graphPanelWidth, graphPanelHeight);
            } else if (graph instanceof BayesianNetwork) {
                graphPanel = new BayesianPanel((BayesianNetwork) graph, graphPanelWidth, graphPanelHeight);
            } else if (graph instanceof Graph) {
                graphPanel = new GraphPanel(new JungGraph(graph), graphPanelWidth, graphPanelHeight);
            }
        }

        if (graphPanel == null) {
            contentPanel.add(new JLabel("not implemented yet"), BorderLayout.CENTER);
        }
        else {
            contentPanel.add(graphPanel, BorderLayout.CENTER);
        }

        contentPanel.updateUI();
    }

    public static Graph newDirectedGraph() {
        MutableDirectedAdjacencyGraph<String, ValueDirectedEdge<String, String>> graph = new MutableDirectedAdjacencyGraph();
        graph.add("x");
        graph.add("y");
        graph.add(new ValueDirectedEdge("xy", "x", "y"));
        return graph;
    }

    public static Graph newUndirectedGraph() {
        MutableAdjacencyGraph<String, Edge<String>> graph = new MutableAdjacencyGraph();
        graph.add("x");
        graph.add("y");
        graph.add(new ImmutableUndirectedEdge<String>("x", "y"));
        return graph;
    }

    public static MutableHyperAdjacencyGraph newHyperGraph() {
        MutableHyperAdjacencyGraph<Integer, HyperEdge<Integer>> hg = new MutableHyperAdjacencyGraph();

		// add hypervertices
		hg.add(1);
		hg.add(2);
		hg.add(3);
		hg.add(4);
		hg.add(5);
		hg.add(6);
		hg.add(7);

		// add hyperedges
		hg.add(new ImmutableHyperEdge(Arrays.asList(6)));
		hg.add(new ImmutableHyperEdge(Arrays.asList(1, 5)));
        hg.add(new ImmutableHyperEdge(Arrays.asList(2, 3, 4)));
        hg.add(new ImmutableHyperEdge(Arrays.asList(1, 2, 3)));
        hg.add(new ImmutableHyperEdge(Arrays.asList(4, 3)));
        hg.add(new ImmutableHyperEdge(Arrays.asList(1, 2)));
        hg.add(new ImmutableHyperEdge(Arrays.asList(7, 6)));
        hg.add(new ImmutableHyperEdge(Arrays.asList(2, 4)));
        hg.add(new ImmutableHyperEdge(Arrays.asList(1, 2, 3, 4, 5)));

        return hg;
    }

    public static Graph newFeedForwardNetwork() {
        double learningRate = 0.0175;
        ActivationFunction activationFunction = new SineActivationFunction();

        final FullyConnectedFeedforwardBrain<? extends InputBackpropNeuron, ? extends OutputBackpropNeuron, ? extends BackpropNeuron, ? extends Synapse<?>> brain = new FullyConnectedFeedforwardBrain<InputBackpropNeuron, OutputBackpropNeuron, BackpropNeuron, Synapse<BackpropNeuron>>(new int[]{8, 6, 4}, learningRate, activationFunction);
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
        f.getContentPane().add(new Main());
        f.setSize(800, 600);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
