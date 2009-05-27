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
package com.syncleus.core.dann.examples.nci;

import com.syncleus.dann.*;
import com.syncleus.dann.backprop.*;
import com.syncleus.dann.activation.*;


/**
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class CompressionNeuron extends BackpropNeuron implements java.io.Serializable
{
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected byte input = 0;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected boolean inputSet = false;



    /**
     * Creates a new instance of InputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param OwnedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public CompressionNeuron(DNA OwnedDNAToSet)
    {
        super(OwnedDNAToSet);
    }
    
    /**
     * Creates a new instance of InputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param OwnedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public CompressionNeuron(DNA ownedDNAToSet, ActivationFunction activationFunction)
    {
        super(ownedDNAToSet, activationFunction);
    }



    /**
     * This method sets the current input on the neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param inputToSet The value to set the current input to.
     */
    public void setInput(byte inputToSet)
    {
        this.input = inputToSet;
        this.inputSet = true;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public void unsetInput()
    {
        this.inputSet = false;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public byte getChannelOutput()
    {
        return (byte) Math.ceil(super.getOutput() * 127.5);
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private double getDoubleInput()
    {
        return ((double) this.input) / 127.5;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
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
