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

public class CompressionNeuron extends NeuronProcessingUnit implements java.io.Serializable
{
	/**
	 * Holds the current input value for this neuron<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	protected int input = 0;
	
	protected boolean inputSet = false;
    
    /**
     * Creates a new instance of InputNeuronProcessingUnit<BR>
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
	  * This method sets the current input on the neuron.<BR>
	  * <!-- Author: Jeffrey Phillips Freeman -->
	  * @since 0.1
	  * @param inputToSet The value to set the current input to.
	  */
    public void setInput(int inputToSet)
    {
		 if( (inputToSet > 255)||(inputToSet < 0) )
			 throw new IllegalArgumentException("the input to set but be a value between 0 (inclusive) and 256 (exclusive)");
		 
        this.input = inputToSet;
		  this.inputSet = true;
    }
	 
	 public void unsetInput()
	 {
		 this.inputSet = false;
	 }
	 
	 public int getChannelOutput()
	 {
		 int newChannel = (int) Math.ceil(super.getOutput() * 256.0);
		 if( newChannel >= 256 )
			 newChannel = 255;
		 
		 return newChannel;
	 }
	 
	 private double getDoubleInput()
	 {
		 return ((double)this.input) / 255.0;
	 }
	 
	 protected void setOutput(double newOutput)
	 {
		 if( this.inputSet == false )
			super.setOutput(newOutput);
		 else
		 {
			 super.output = this.getDoubleInput();
			 
			 for( Synapse current : this.destination )
			 {
				 current.setInput(newOutput);
			 }
		 }
	 }
}
