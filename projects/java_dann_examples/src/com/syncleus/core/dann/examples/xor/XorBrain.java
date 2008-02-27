/******************************************************************************
*                                                                             *
*  Copyright: (c) Jeffrey Phillips Freeman                                    *
*                                                                             *
*  You may redistribute and modify this source code under the terms and       *
*  conditions of the Open Source Community License - Type C version 1.0       *
*  or any later version as published by syncleus at http://www.syncleus.com.  *
*  There should be a copy of the license included with this file. If a copy   *
*  of the license is not included you are granted no right to distribute or   *
*  otherwise use this file except through a legal and valid license. You      *
*  should also contact syncleus at the information below if you cannot find   *
*  a license:                                                                 *
*                                                                             *
*  Syncleus                                                                   *
*  1116 McClellan St.                                                         *
*  Philadelphia, PA 19148                                                     *
*                                                                             *
******************************************************************************/
package com.syncleus.core.dann.examples.xor;

import java.io.BufferedReader;
import java.util.ArrayList;

import com.syncleus.dann.DNA;
import com.syncleus.dann.InputNeuronProcessingUnit;
import com.syncleus.dann.LayerProcessingUnit;
import com.syncleus.dann.NeuronProcessingUnit;
import com.syncleus.dann.OutputNeuronProcessingUnit;


/**
 * An example class that shows using dANN to solve an XOR problem. An XOR
 * is a circuit that returns true (1) when only one of its inputs is true. It
 * returns false (-1) if none all of its inputs are false or if more then one
 * of its inputs are true.
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 */

public class XorBrain {
	
	private DNA myDNA = new DNA();
	private BufferedReader inReader = null;
	private InputNeuronProcessingUnit inputA = null;
	private InputNeuronProcessingUnit inputB = null;
	private InputNeuronProcessingUnit inputC = null;
	private LayerProcessingUnit firstLayer = null;
	private LayerProcessingUnit secondLayer = null;
	private OutputNeuronProcessingUnit output = null;
	private String saveLocation = "default.dann";

	/**
	 * XorBrain is a modified com.syncleus.core.dann.examples.xor.Main
	 * which is not static -> to use in GUIs
	 * <!-- Author: chickenf -->
	 */

	public XorBrain() {

		//Adjust the learning rate
		myDNA.learningRate = 0.05;
			
		//creates the first layer which holds all the input neurons
		inputA = new InputNeuronProcessingUnit(myDNA);
		inputB = new InputNeuronProcessingUnit(myDNA);
		inputC = new InputNeuronProcessingUnit(myDNA);
		firstLayer = new LayerProcessingUnit(myDNA);
		firstLayer.add(inputA);
		firstLayer.add(inputB);
		firstLayer.add(inputC);

		//creates the second layer of neurons containing 10 neurons.
		secondLayer = new LayerProcessingUnit(myDNA);
		for( int lcv = 0; lcv < 10; lcv++ )
		{
			secondLayer.add(new NeuronProcessingUnit(myDNA));
		}

		//the output layer is just a single neuron
		output = new OutputNeuronProcessingUnit(myDNA);
		
		//connects the network in a feedforward fasion.
		firstLayer.connectAllTo(secondLayer);
		secondLayer.connectAllTo(output);
	}
	
	private void propogateOutput() {
		firstLayer.propogate();
		secondLayer.propogate();
		output.propogate();
	}
	
	private void backPropogateTraining() {
		output.backPropogate();
		secondLayer.backPropogate();
		firstLayer.backPropogate();
	}
	
	private void setCurrentInput(double[] inputToSet) {
		inputA.setInput(inputToSet[0]);
		inputB.setInput(inputToSet[1]);
		inputC.setInput(inputToSet[2]);
	}
	
	public ArrayList<Double> testOutput() {
		
		ArrayList<Double> resultsList = new ArrayList<Double>();  
//		String results = "";
		
        double[] curInput = {0, 0, 0};
        setCurrentInput(curInput);
        propogateOutput();
        double[] curOutput;
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
        
        
        curInput[0] = 1;
        curInput[1] = 0;
        curInput[2] = 0;
        setCurrentInput(curInput);
        propogateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 0;
        curInput[1] = 1;
        curInput[2] = 0;
        setCurrentInput(curInput);
        propogateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 0;
        curInput[1] = 0;
        curInput[2] = 1;
        setCurrentInput(curInput);
        propogateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 1;
        curInput[1] = 1;
        curInput[2] = 0;
        setCurrentInput(curInput);
        propogateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 0;
        curInput[1] = 1;
        curInput[2] = 1;
        setCurrentInput(curInput);
        propogateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 1;
        curInput[1] = 0;
        curInput[2] = 1;
        setCurrentInput(curInput);
        propogateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());
		  
        curInput[0] = 1;
        curInput[1] = 1;
        curInput[2] = 1;
        setCurrentInput(curInput);
        propogateOutput();
        resultsList.add(curInput[0]);
        resultsList.add(curInput[1]);
        resultsList.add(curInput[2]);
        resultsList.add(output.getOutput());

        return resultsList;
	}
	
	public void train(int count) {

		for(int lcv = 0; lcv < count; lcv++) {

			double[] curInput = {0, 0, 0};
            double curTrain = -1;
            setCurrentInput(curInput);
            propogateOutput();
            output.setDesired(curTrain);
            backPropogateTraining();
				
            curInput[0] = 1;
            curInput[1] = 0;
            curInput[2] = 0;
            curTrain = 1;
            setCurrentInput(curInput);
            propogateOutput();
            output.setDesired(curTrain);
            backPropogateTraining();
				
            curInput[0] = 0;
            curInput[1] = 1;
            curInput[2] = 0;
            curTrain = 1;
            setCurrentInput(curInput);
            propogateOutput();
            output.setDesired(curTrain);
            backPropogateTraining();
				
            curInput[0] = 0;
            curInput[1] = 0;
            curInput[2] = 1;
            curTrain = 1;
            setCurrentInput(curInput);
            propogateOutput();
            output.setDesired(curTrain);
            backPropogateTraining();
				
            curInput[0] = 1;
            curInput[1] = 1;
            curInput[2] = 0;
            curTrain = -1;
            setCurrentInput(curInput);
            propogateOutput();
            output.setDesired(curTrain);
            backPropogateTraining();
				
            curInput[0] = 0;
            curInput[1] = 1;
            curInput[2] = 1;
            curTrain = -1;
            setCurrentInput(curInput);
            propogateOutput();
            output.setDesired(curTrain);
            backPropogateTraining();
				
            curInput[0] = 1;
            curInput[1] = 0;
            curInput[2] = 1;
            curTrain = -1;
            setCurrentInput(curInput);
            propogateOutput();
            output.setDesired(curTrain);
            backPropogateTraining();
				
            curInput[0] = 1;
            curInput[1] = 1;
            curInput[2] = 1;
            curTrain = -1;
            setCurrentInput(curInput);
            propogateOutput();
            output.setDesired(curTrain);
            backPropogateTraining();
        }
	}
	
	public LayerProcessingUnit getSecondLayer() {
		return this.secondLayer;
	}
}


