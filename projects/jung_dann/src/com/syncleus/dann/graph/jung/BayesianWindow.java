/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.syncleus.dann.graph.jung;

import com.syncleus.dann.graphicalmodel.bayesian.BayesianEdge;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianNode;
import com.syncleus.dann.graphicalmodel.bayesian.MutableBayesianAdjacencyNetwork;
import com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianEdge;
import com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianNode;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author seh
 */
public class BayesianWindow {

    private static enum BooleanState {

        TRUE, FALSE
    }

    private static enum SeasonState {

        WINTER, SUMMER, SPRING, FALL
    }

    private static enum AgeState {

        BABY, CHILD, TEENAGER, ADULT, SENIOR
    }

    private static enum FeverState {

        LOW, NONE, WARM, HOT
    }
    private static final Random RANDOM = new Random();
    private MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
    //create nodes
    private BayesianNode<SeasonState> season = new SimpleBayesianNode<SeasonState>(SeasonState.WINTER, this.network);
    private BayesianNode<AgeState> age = new SimpleBayesianNode<AgeState>(AgeState.BABY, this.network);
    private BayesianNode<BooleanState> stuffyNose = new SimpleBayesianNode<BooleanState>(BooleanState.TRUE, this.network);
    private BayesianNode<FeverState> fever = new SimpleBayesianNode<FeverState>(FeverState.HOT, this.network);
    private BayesianNode<BooleanState> tired = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, this.network);
    private BayesianNode<BooleanState> sick = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, this.network);

    public BayesianWindow() {
        //add nodes
        network.add(this.season);
        network.add(this.age);
        network.add(this.stuffyNose);
        network.add(this.fever);
        network.add(this.tired);
        network.add(this.sick);

        {
            //others
//            network.add(new SimpleBayesianNode<SeasonState>(SeasonState.FALL, this.network));
//            network.add(new SimpleBayesianNode<SeasonState>(SeasonState.SPRING, this.network));
//            network.add(new SimpleBayesianNode<SeasonState>(SeasonState.SUMMER, this.network));
        }
        
        //connect nodes
        network.add(new SimpleBayesianEdge<BayesianNode>(this.season, this.stuffyNose));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.season, this.fever));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.season, this.tired));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.season, this.sick));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.age, this.stuffyNose));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.age, this.fever));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.age, this.tired));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.age, this.sick));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.tired, this.fever));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.tired, this.stuffyNose));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.tired, this.sick));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.stuffyNose, this.fever));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.stuffyNose, this.sick));
        network.add(new SimpleBayesianEdge<BayesianNode>(this.fever, this.sick));


        System.out.println("NODES: " + network.getNodes() + " (" + network.getNodes().size());

        //let the network learn
        for (int sampleCount = 0; sampleCount < 10; sampleCount++) {
            this.sampleState();
        }
        //lets check some probabilities
        final Set<BayesianNode> goals = new HashSet<BayesianNode>();
        goals.add(this.sick);
        final Set<BayesianNode> influences = new HashSet<BayesianNode>();
        influences.add(this.fever);
        sick.setState(BooleanState.TRUE);
        fever.setState(FeverState.LOW);
        final double lowPercentage = network.conditionalProbability(goals, influences);
        fever.setState(FeverState.NONE);
        final double nonePercentage = network.conditionalProbability(goals, influences);
        fever.setState(FeverState.WARM);
        final double warmPercentage = network.conditionalProbability(goals, influences);
        fever.setState(FeverState.HOT);
        final double hotPercentage = network.conditionalProbability(goals, influences);

        
        new GraphWindow(new JungGraph(network), 800, 600) {

            @Override
            protected void initVis(VisualizationViewer vis) {
                super.initVis(vis);
                vis.getRenderContext().setVertexLabelTransformer(new ToStringLabeller() {

                    @Override
                    public String transform(Object v) {
                        if (v instanceof BayesianNode) {
                            BayesianNode bn = (BayesianNode) v;
                            return bn.getState() + " " + bn.stateProbability();
                        }
                        return super.transform(v);
                    }
                });
                vis.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller() {

                    @Override
                    public String transform(Object v) {
                        if (v instanceof BayesianEdge) {
                            return "->";
                        }
                        return super.transform(v);
                    }
                });

            }
        };

    }

    public static void main(String[] args) {
        new BayesianWindow();
    }

    private void sampleState() {
        final SeasonState seasonState = (SeasonState.values())[RANDOM.nextInt(SeasonState.values().length)];
        season.setState(seasonState);

        final AgeState ageState = (AgeState.values())[RANDOM.nextInt(AgeState.values().length)];
        age.setState(ageState);

        final BooleanState noseState = (BooleanState.values())[RANDOM.nextInt(BooleanState.values().length)];
        stuffyNose.setState(noseState);

        final BooleanState tiredState = (BooleanState.values())[RANDOM.nextInt(BooleanState.values().length)];
        tired.setState(tiredState);


        fever.setState(FeverState.NONE);
        sick.setState(BooleanState.FALSE);
        network.learnStates();
        fever.setState(FeverState.NONE);
        sick.setState(BooleanState.FALSE);
        network.learnStates();
        fever.setState(FeverState.NONE);
        sick.setState(BooleanState.FALSE);
        network.learnStates();
        fever.setState(FeverState.NONE);
        sick.setState(BooleanState.FALSE);
        network.learnStates();
        fever.setState(FeverState.NONE);
        sick.setState(BooleanState.TRUE);
        network.learnStates();

        fever.setState(FeverState.LOW);
        sick.setState(BooleanState.FALSE);
        network.learnStates();
        fever.setState(FeverState.LOW);
        sick.setState(BooleanState.FALSE);
        network.learnStates();
        fever.setState(FeverState.LOW);
        sick.setState(BooleanState.FALSE);
        network.learnStates();
        fever.setState(FeverState.LOW);
        sick.setState(BooleanState.TRUE);
        network.learnStates();
        fever.setState(FeverState.LOW);
        sick.setState(BooleanState.TRUE);
        network.learnStates();

        fever.setState(FeverState.WARM);
        sick.setState(BooleanState.FALSE);
        network.learnStates();
        fever.setState(FeverState.WARM);
        sick.setState(BooleanState.FALSE);
        network.learnStates();
        fever.setState(FeverState.WARM);
        sick.setState(BooleanState.TRUE);
        network.learnStates();
        fever.setState(FeverState.WARM);
        sick.setState(BooleanState.TRUE);
        network.learnStates();
        fever.setState(FeverState.WARM);
        sick.setState(BooleanState.TRUE);
        network.learnStates();

        fever.setState(FeverState.HOT);
        sick.setState(BooleanState.FALSE);
        network.learnStates();
        fever.setState(FeverState.HOT);
        sick.setState(BooleanState.TRUE);
        network.learnStates();
        fever.setState(FeverState.HOT);
        sick.setState(BooleanState.TRUE);
        network.learnStates();
        fever.setState(FeverState.HOT);
        sick.setState(BooleanState.TRUE);
        network.learnStates();
        fever.setState(FeverState.HOT);
        sick.setState(BooleanState.TRUE);
        network.learnStates();
    }
}
