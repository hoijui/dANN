/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.syncleus.dann.graph.jung;

import com.syncleus.dann.neural.InputNeuron;
import com.syncleus.dann.neural.Neuron;
import com.syncleus.dann.neural.OutputNeuron;
import com.syncleus.dann.neural.Synapse;
import com.syncleus.dann.neural.activation.ActivationFunction;
import com.syncleus.dann.neural.activation.SineActivationFunction;
import com.syncleus.dann.neural.backprop.brain.FullyConnectedFeedforwardBrain;
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
 *
 * @author seh
 */
public class BrainWindow<N extends Neuron, S extends Synapse> extends GraphWindow<N, S> {

    static long brainUpdatePeriodMS = 50;
    
    public static class NeuronLabeller extends ToStringLabeller {
        @Override
        public String transform(Object v) {
            if (v instanceof Neuron) {
               Neuron n = (Neuron)v;
               return "N";
            }
            return v.toString();
        }
    }
    
    public static class SynapseLabeller extends ToStringLabeller {
        @Override
        public String transform(Object v) {
            if (v instanceof Synapse) {
               Synapse s = (Synapse)v;
               return "";
            }
            return v.toString();
        }
    }

    public BrainWindow(edu.uci.ics.jung.graph.Graph g, int w, int h) {
        super(g, w, h);        
    }

    @Override
    protected void initVis(VisualizationViewer<N, S> vis) {
        vis.getRenderContext().setVertexLabelTransformer(new NeuronLabeller());
        vis.getRenderContext().setEdgeLabelTransformer(new SynapseLabeller());
        vis.getRenderContext().setVertexFillPaintTransformer(new Transformer<N, Paint>() {
            public Paint transform(N n) {
                if (n instanceof InputNeuron) {
                    return Color.MAGENTA;
                }
                else if (n instanceof OutputNeuron) {
                    return Color.GREEN;
                }
                else {
                    return Color.GRAY;
                }
            }
        });
        vis.getRenderContext().setVertexShapeTransformer(new Transformer<N, Shape>()  {
            public Shape transform(N n) {
                float s = 0.5f;
                if (n instanceof OutputNeuron) {
                    OutputNeuron aan = (OutputNeuron) n;
                    s = (float) Math.abs(aan.getOutput());
                }
                else if (n instanceof InputNeuron) {
                    InputNeuron aan = (InputNeuron) n;
                    s = (float) Math.abs(aan.getInput());
                }
                int w = (int) (s*44.0);
                int h = (int) (s*44.0);
                int x = -w/2;
                int y = -h/2;
                return new Rectangle2D.Float(x, y, w, h);
            }
        });
        vis.getRenderContext().setEdgeStrokeTransformer(new Transformer<S,Stroke>() {
            public Stroke transform(S i) {
                return new BasicStroke((int)((Math.abs(i.getWeight())) * 8.0));
            }
        });
        vis.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<S, Paint>() {
            public Paint transform(S syn) {
                float b = (float)Math.abs(syn.getWeight());
                float s = 0.8f;
                float h = syn.getWeight() < 0 ? 0.6f : 0.9f;
                return Color.getHSBColor(h, s, b);
            }
        });
    }


    public static void main(String[] args) {
        {
            double learningRate = 0.0175;
            ActivationFunction activationFunction = new SineActivationFunction();

            final FullyConnectedFeedforwardBrain brain = new FullyConnectedFeedforwardBrain(new int[]{8, 6, 4}, learningRate, activationFunction);
            {
                //randomize the brain
                for (Synapse s : brain.getEdges()) {
                    s.setWeight(Math.random() * 2.0 - 1.0);
                }
            }

            new BrainWindow(new JungGraph(brain), 800, 600);

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
                    while (true) {

                        randomizeInputs();

                        brain.propagate();
                        
                        try {
                            Thread.sleep(brainUpdatePeriodMS);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BrainWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }).start();
        }

    }
}
