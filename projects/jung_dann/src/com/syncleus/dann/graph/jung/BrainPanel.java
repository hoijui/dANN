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

import com.syncleus.dann.neural.InputNeuron;
import com.syncleus.dann.neural.Neuron;
import com.syncleus.dann.neural.OutputNeuron;
import com.syncleus.dann.neural.Synapse;
import com.syncleus.dann.neural.backprop.InputBackpropNeuron;
import com.syncleus.dann.neural.backprop.OutputBackpropNeuron;
import com.syncleus.dann.neural.backprop.BackpropNeuron;
import com.syncleus.dann.neural.backprop.brain.BackpropBrain;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections15.Transformer;

/**
 * Visualizes a BackpropBrain.  Note: this demo presently automatically propagate()'s the visualized network.
 * @author seh
 */
public class BrainPanel<N extends Neuron, S extends Synapse> extends GraphPanel<N, S> {

    static long brainUpdatePeriodMS = 30;
    private final BackpropBrain<? extends InputBackpropNeuron, ? extends OutputBackpropNeuron, ? extends BackpropNeuron, ? extends Synapse<?>> brain;

    public static class NeuronLabeller extends ToStringLabeller {

        @Override
        public String transform(Object v) {
            if (v instanceof Neuron) {
                Neuron n = (Neuron) v;
                return "N";
            }
            return v.toString();
        }
    }

    public static class SynapseLabeller extends ToStringLabeller {

        @Override
        public String transform(Object v) {
            if (v instanceof Synapse) {
                Synapse s = (Synapse) v;
                return "";
            }
            return v.toString();
        }
    }

    public BrainPanel(BackpropBrain b, int w, int h) {
        super(new JungGraph(b), w, h);
        this.brain = b;
        new Thread(new Runnable() {

            protected void randomizeInputs() {
                double dx = 0.2f;

                for (InputNeuron n : brain.getInputNeurons()) {
                    double i = n.getInput();
                    i += (-1.0 + 2.0 * Math.random()) * dx;
                    i = Math.max(-1, i);
                    i = Math.min(1, i);
                    n.setInput(i);
                }
            }

            public void run() {
                while (isRunning()) {

                    randomizeInputs();

                    brain.propagate();

                    try {
                        Thread.sleep(brainUpdatePeriodMS);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BrainPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //System.out.println("BRAIN STOPPED");
            }
        }).start();
    }

    @Override
    protected void initVis(VisualizationViewer<N, S> vis) {
        vis.getRenderContext().setVertexLabelTransformer(new NeuronLabeller());
        vis.getRenderContext().setEdgeLabelTransformer(new SynapseLabeller());
        vis.getRenderContext().setVertexFillPaintTransformer(new Transformer<N, Paint>() {

            public Paint transform(N n) {
                if (n instanceof InputNeuron) {
                    return Color.MAGENTA;
                } else if (n instanceof OutputNeuron) {
                    return Color.GREEN;
                } else {
                    return Color.GRAY;
                }
            }
        });
        vis.getRenderContext().setVertexShapeTransformer(new Transformer<N, Shape>() {

            public Shape transform(N n) {
                float s = 0.5f;
                if (n instanceof OutputNeuron) {
                    OutputNeuron aan = (OutputNeuron) n;
                    s = (float) Math.abs(aan.getOutput());
                } else if (n instanceof InputNeuron) {
                    InputNeuron aan = (InputNeuron) n;
                    s = (float) Math.abs(aan.getInput());
                }
                int w = (int) (s * 44.0);
                int h = (int) (s * 44.0);
                int x = -w / 2;
                int y = -h / 2;
                return new Rectangle2D.Float(x, y, w, h);
            }
        });
        vis.getRenderContext().setEdgeStrokeTransformer(new Transformer<S, Stroke>() {

            public Stroke transform(S i) {
                return new BasicStroke((int) ((Math.abs(i.getWeight())) * 8.0));
            }
        });
        vis.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<S, Paint>() {

            public Paint transform(S syn) {
                float b = (float) Math.abs(syn.getWeight());
                float s = 0.8f;
                float h = syn.getWeight() < 0 ? 0.6f : 0.9f;
                return Color.getHSBColor(h, s, b);
            }
        });


    }
}
