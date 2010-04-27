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

import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.backprop.*;
import com.syncleus.dann.neural.activation.*;
import java.io.Serializable;


/**
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 */
public class CompressionNeuron extends SimpleBackpropNeuron implements Serializable
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
    public CompressionNeuron(Brain brain)
    {
        super(brain);
    }
    
    /**
     * Creates a new instance of InputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public CompressionNeuron(Brain brain, ActivationFunction activationFunction)
    {
        super(brain, activationFunction);
    }

	public CompressionNeuron(Brain brain, double learningRate)
	{
		super(brain, learningRate);
	}

	public CompressionNeuron(Brain brain, ActivationFunction activationFunction, double learningRate)
	{
		super(brain, activationFunction, learningRate);
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

	@Override
	public void propagate()
	{
        if (this.inputSet == false)
            super.propagate();
		else
		{
			// TODO we shouldnt be calling setOutput, try getting rid of the protected in the parent and instead make some abstracts
            this.setOutput(this.getDoubleInput());
            for (Synapse current : this.getBrain().getTraversableEdges(this))
                current.setInput(this.getOutput());
		}
	}
}
