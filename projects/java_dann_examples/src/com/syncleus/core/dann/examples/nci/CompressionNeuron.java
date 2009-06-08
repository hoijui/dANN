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
package com.syncleus.core.dann.examples.nci;

import com.syncleus.dann.*;
import com.syncleus.dann.backprop.*;
import com.syncleus.dann.activation.*;


/**
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Syncleus, Inc.
 * @since 1.0
 */
public class CompressionNeuron extends BackpropNeuron implements java.io.Serializable
{
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    protected byte input = 0;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    protected boolean inputSet = false;



    /**
     * Creates a new instance of InputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public CompressionNeuron()
    {
        super();
    }
    
    /**
     * Creates a new instance of InputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public CompressionNeuron(ActivationFunction activationFunction)
    {
        super(activationFunction);
    }

	public CompressionNeuron(double learningRate)
	{
		super(learningRate);
	}

	public CompressionNeuron(ActivationFunction activationFunction, double learningRate)
	{
		super(activationFunction, learningRate);
	}



    /**
     * This method sets the current input on the neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     * @param inputToSet The value to set the current input to.
     */
    public void setInput(byte inputToSet)
    {
        this.input = inputToSet;
        this.inputSet = true;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public void unsetInput()
    {
        this.inputSet = false;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public byte getChannelOutput()
    {
        return (byte) Math.ceil(super.getOutput() * 127.5);
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    private double getDoubleInput()
    {
        return ((double) this.input) / 127.5;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    protected void setOutput(double newOutput)
    {
        if (this.inputSet == false)
            super.setOutput(newOutput);
        else
        {
            super.output = this.getDoubleInput();

            for (Synapse current : this.destinations)
                current.setInput(newOutput);
        }
    }
}
