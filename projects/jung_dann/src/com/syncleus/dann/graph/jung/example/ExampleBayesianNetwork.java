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
package com.syncleus.dann.graph.jung.example;

import com.syncleus.dann.graph.jung.GraphPanel;
import com.syncleus.dann.graph.jung.JungGraph;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianNode;
import com.syncleus.dann.graphicalmodel.bayesian.MutableBayesianAdjacencyNetwork;
import com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianEdge;
import com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianNode;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * An example Bayesian Network, adapted directly from dANN's Unit Tests
 * @author seh
 */
public class ExampleBayesianNetwork extends MutableBayesianAdjacencyNetwork {

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
    
    //create nodes
    private BayesianNode<SeasonState> season = new SimpleBayesianNode<SeasonState>(SeasonState.WINTER, this);
    private BayesianNode<AgeState> age = new SimpleBayesianNode<AgeState>(AgeState.BABY, this);
    private BayesianNode<BooleanState> stuffyNose = new SimpleBayesianNode<BooleanState>(BooleanState.TRUE, this);
    private BayesianNode<FeverState> fever = new SimpleBayesianNode<FeverState>(FeverState.HOT, this);
    private BayesianNode<BooleanState> tired = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, this);
    private BayesianNode<BooleanState> sick = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, this);

    public ExampleBayesianNetwork() {
        MutableBayesianAdjacencyNetwork network = this;
        
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

        
        new GraphPanel(new JungGraph(network), 800, 600) {

        };

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
        learnStates();
        fever.setState(FeverState.NONE);
        sick.setState(BooleanState.FALSE);
        learnStates();
        fever.setState(FeverState.NONE);
        sick.setState(BooleanState.FALSE);
        learnStates();
        fever.setState(FeverState.NONE);
        sick.setState(BooleanState.FALSE);
        learnStates();
        fever.setState(FeverState.NONE);
        sick.setState(BooleanState.TRUE);
        learnStates();

        fever.setState(FeverState.LOW);
        sick.setState(BooleanState.FALSE);
        learnStates();
        fever.setState(FeverState.LOW);
        sick.setState(BooleanState.FALSE);
        learnStates();
        fever.setState(FeverState.LOW);
        sick.setState(BooleanState.FALSE);
        learnStates();
        fever.setState(FeverState.LOW);
        sick.setState(BooleanState.TRUE);
        learnStates();
        fever.setState(FeverState.LOW);
        sick.setState(BooleanState.TRUE);
        learnStates();

        fever.setState(FeverState.WARM);
        sick.setState(BooleanState.FALSE);
        learnStates();
        fever.setState(FeverState.WARM);
        sick.setState(BooleanState.FALSE);
        learnStates();
        fever.setState(FeverState.WARM);
        sick.setState(BooleanState.TRUE);
        learnStates();
        fever.setState(FeverState.WARM);
        sick.setState(BooleanState.TRUE);
        learnStates();
        fever.setState(FeverState.WARM);
        sick.setState(BooleanState.TRUE);
        learnStates();

        fever.setState(FeverState.HOT);
        sick.setState(BooleanState.FALSE);
        learnStates();
        fever.setState(FeverState.HOT);
        sick.setState(BooleanState.TRUE);
        learnStates();
        fever.setState(FeverState.HOT);
        sick.setState(BooleanState.TRUE);
        learnStates();
        fever.setState(FeverState.HOT);
        sick.setState(BooleanState.TRUE);
        learnStates();
        fever.setState(FeverState.HOT);
        sick.setState(BooleanState.TRUE);
        learnStates();
    }
}
