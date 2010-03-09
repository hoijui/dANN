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
import com.syncleus.dann.neural.backprop.brain.*;
import com.syncleus.dann.neural.activation.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Syncleus, Inc.
 * @since 1.0
 */
public class NciBrain extends AbstractFullyConnectedFeedforwardBrain implements java.io.Serializable
{
    private double actualCompression = 0.0;
    private int xSize = 0;
    private int ySize = 0;
    private InputBackpropNeuron[][][] inputNeurons = null;
    private ArrayList<CompressionNeuron> compressedNeurons = new ArrayList<CompressionNeuron>();
    private OutputBackpropNeuron[][][] outputNeurons = null;
    private boolean learning = true;
    private static final int CHANNELS = 3;
    private boolean compressionInputsSet = false;
	private ActivationFunction activationFunction;
	private double learningRate;



    /**
     * creates an instance of NciBrain.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @param compression A value between 0.0 (inclusive) and 1.0 (exclusive)
     *		which represents the % of compression.
     */
    public NciBrain(double compression, int xSize, int ySize, boolean extraConnectivity)
    {
		super();

        this.learningRate = 0.001;
		this.activationFunction = new SineActivationFunction();


        this.xSize = xSize;
        this.ySize = ySize;
        int compressedNeuronCount = ((int) Math.ceil((((double) xSize) * ((double) ySize) * ((double) CHANNELS)) * (1.0 - compression)));
        this.inputNeurons = new InputBackpropNeuron[xSize][ySize][CHANNELS];
        this.outputNeurons = new OutputBackpropNeuron[xSize][ySize][CHANNELS];
        this.actualCompression = 1.0 - ((double) compressedNeuronCount) / (((double) xSize) * ((double) ySize) * ((double) CHANNELS));
		int blockSize = xSize*ySize*CHANNELS;

		this.initalizeNetwork(new int[]{blockSize, compressedNeuronCount, blockSize});

        //assign inputs to pixels
		ArrayList<InputNeuron> inputs = new ArrayList<InputNeuron>(this.getInputNeurons());
		ArrayList<OutputNeuron> outputs = new ArrayList<OutputNeuron>(this.getOutputNeurons());
        for (int yIndex = 0; yIndex < ySize; yIndex++)
            for (int xIndex = 0; xIndex < xSize; xIndex++)
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
					int overallIndex = yIndex*xSize*CHANNELS + xIndex*CHANNELS + rgbIndex;
					this.inputNeurons[xIndex][yIndex][rgbIndex] = (InputBackpropNeuron)inputs.get(overallIndex);
					this.outputNeurons[xIndex][yIndex][rgbIndex] =(OutputBackpropNeuron)outputs.get(overallIndex);
                }
    }

	protected BackpropNeuron createNeuron(int layer, int index)
	{
		if( layer == 0 )
			return new InputBackpropNeuron(this, this.activationFunction, this.learningRate);
		else if(layer >= (this.getLayerCount() - 1))
			return new OutputBackpropNeuron(this, this.activationFunction, this.learningRate);
		else if(layer == 1)
		{
			CompressionNeuron compressionNeuron = new CompressionNeuron(this, this.activationFunction, this.learningRate);
			this.compressedNeurons.add(compressionNeuron);
			return compressionNeuron;
		}
		else
			return new BackpropNeuron(this, this.activationFunction, this.learningRate);
	}



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public double getCompression()
    {
        return this.actualCompression;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public boolean getLearning()
    {
        return this.learning;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public void setLearning(boolean learningToSet)
    {
        this.learning = learningToSet;
    }



    public double getAverageWeight()
    {
        double weightSum = 0.0;
        double weightCount = 0.0;

        for (Neuron child : this.getNodes())
        {
			try
			{
				List<Synapse> childSynapses = this.getOutEdges(child);

				for (Synapse childSynapse : childSynapses)
				{
					weightSum += childSynapse.getWeight();
					weightCount++;
				}
			}
			catch(ClassCastException caughtException)
			{
				throw new AssertionError(caughtException);
			}
        }

        return weightSum / weightCount;
    }



    public double getAverageAbsoluteWeight()
    {
        double weightSum = 0.0;
        double weightCount = 0.0;

        for (Neuron child : this.getNodes())
        {
			try
			{
				List<Synapse> childSynapses = this.getOutEdges(child);

				for (Synapse childSynapse : childSynapses)
				{
					weightSum += Math.abs(childSynapse.getWeight());
					weightCount++;
				}
			}
			catch(ClassCastException caughtException)
			{
				throw new AssertionError(caughtException);
			}
        }

        return weightSum / weightCount;
    }



    public BufferedImage test(BufferedImage originalImage)
    {
        int[] originalRgbArray = new int[xSize * ySize];
        originalImage.getRGB(0, 0, (originalImage.getWidth() < xSize ? originalImage.getWidth() : xSize), (originalImage.getHeight() < ySize ? originalImage.getHeight() : ySize), originalRgbArray, 0, xSize);


        //set the image onto the inputs
        for (int yIndex = 0; (yIndex < ySize) && (yIndex < originalImage.getHeight()); yIndex++)
            for (int xIndex = 0; (xIndex < xSize) && (xIndex < originalImage.getWidth()); xIndex++)
            {
                int rgbCurrent = originalRgbArray[yIndex * xSize + xIndex];
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {


                    int channel = (int) (((rgbCurrent >> (rgbIndex * 8)) & 0x000000FF));
                    double input = (((double) channel) / 127.5) - 1.0;

                    this.inputNeurons[xIndex][yIndex][rgbIndex].setInput(input);
                    this.outputNeurons[xIndex][yIndex][rgbIndex].setDesired(input);
                }
            }

        if (this.compressionInputsSet == true)
        {
            for (CompressionNeuron compressionNeuron : this.compressedNeurons)
                compressionNeuron.unsetInput();
            this.compressionInputsSet = false;
        }


        //propogate the output
        this.propagate();


        int[] finalRgbArray = new int[xSize * ySize];
        BufferedImage uncompressedImage = new BufferedImage(this.xSize, this.ySize, BufferedImage.TYPE_INT_RGB);
        for (int yIndex = 0; (yIndex < ySize) && (yIndex < uncompressedImage.getHeight()); yIndex++)
            for (int xIndex = 0; (xIndex < xSize) && (xIndex < uncompressedImage.getWidth()); xIndex++)
            {
                //int rgbCurrent = imageToCompress.getRGB(xIndex, yIndex);
                int rgbCurrent = 0;
                for (int rgbIndex = 0; rgbIndex < 4; rgbIndex++)
                {
                    double output;

                    if (rgbIndex >= CHANNELS)
                        output = this.outputNeurons[xIndex][yIndex][0].getOutput();
                    else
                        output = this.outputNeurons[xIndex][yIndex][rgbIndex].getOutput();

                    int channel = (int)((output + 1.0d) * 127.5d);

                    rgbCurrent |= (((int) channel) & 0x000000FF) << (rgbIndex * 8);
                }
                finalRgbArray[xSize * yIndex + (xIndex)] = rgbCurrent;
            }
        uncompressedImage.setRGB(0, 0, xSize, ySize, finalRgbArray, 0, xSize);



        if (this.learning == false)
            return uncompressedImage;

        //now back propogate
        this.backPropagate();

        //all done
        return uncompressedImage;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public byte[] compress(final BufferedImage originalImage)
    {
        int[] originalRgbArray = new int[xSize * ySize];
        originalImage.getRGB(0, 0, (originalImage.getWidth() < xSize ? originalImage.getWidth() : xSize), (originalImage.getHeight() < ySize ? originalImage.getHeight() : ySize), originalRgbArray, 0, xSize);


        //set the image onto the inputs
        for (int yIndex = 0; (yIndex < ySize) && (yIndex < originalImage.getHeight()); yIndex++)
            for (int xIndex = 0; (xIndex < xSize) && (xIndex < originalImage.getWidth()); xIndex++)
            {
                int rgbCurrent = originalRgbArray[yIndex * xSize + xIndex];
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {


                    int channel = (int) (((rgbCurrent >> (rgbIndex * 8)) & 0x000000FF));
                    double input = (((double) channel) / 127.5) - 1.0;

                    this.inputNeurons[xIndex][yIndex][rgbIndex].setInput(input);
                }
            }

        if (this.compressionInputsSet == true)
        {
            for (CompressionNeuron compressionNeuron : this.compressedNeurons)
                compressionNeuron.unsetInput();
            this.compressionInputsSet = false;
        }


        //propogate the output
		this.propagate();

        int compressedDataIndex = 0;
        byte[] compressedData = new byte[this.compressedNeurons.size()];
        for (CompressionNeuron compressionNeuron : this.compressedNeurons)
            compressedData[compressedDataIndex++] = compressionNeuron.getChannelOutput();

        return compressedData;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 1.0
     */
    public BufferedImage uncompress(final byte[] compressedData)
    {
        int compressedDataIndex = 0;
        for (CompressionNeuron compressionNeuron : this.compressedNeurons)
            compressionNeuron.setInput(compressedData[compressedDataIndex++]);

        this.compressionInputsSet = true;

		this.propagate();

        int[] finalRgbArray = new int[xSize * ySize];
        BufferedImage uncompressedImage = new BufferedImage(this.xSize, this.ySize, BufferedImage.TYPE_INT_RGB);
        for (int yIndex = 0; (yIndex < ySize) && (yIndex < uncompressedImage.getHeight()); yIndex++)
            for (int xIndex = 0; (xIndex < xSize) && (xIndex < uncompressedImage.getWidth()); xIndex++)
            {
                //int rgbCurrent = imageToCompress.getRGB(xIndex, yIndex);
                int rgbCurrent = 0;
                for (int rgbIndex = 0; rgbIndex < 4; rgbIndex++)
                {
                    double output;

                    if (rgbIndex >= CHANNELS)
                        output = this.outputNeurons[xIndex][yIndex][0].getOutput();
                    else
                        output = this.outputNeurons[xIndex][yIndex][rgbIndex].getOutput();

                    int channel = (int)((output + 1.0d) * 127.5d);

                    rgbCurrent |= (((int) channel) & 0x000000FF) << (rgbIndex * 8);
                }
                finalRgbArray[xSize * yIndex + (xIndex)] = rgbCurrent;
            }
        uncompressedImage.setRGB(0, 0, xSize, ySize, finalRgbArray, 0, xSize);

        return uncompressedImage;
    }
}
