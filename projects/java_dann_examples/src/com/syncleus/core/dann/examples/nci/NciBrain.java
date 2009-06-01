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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


/**
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class NciBrain extends Brain implements java.io.Serializable
{
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private double actualCompression = 0.0;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private int xSize = 0;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private int ySize = 0;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private InputBackpropNeuron[][][] inputNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private BackpropNeuronGroup inputLayer = new BackpropNeuronGroup();
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private BackpropNeuron[] inputHiddenNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private BackpropNeuronGroup inputHiddenLayer = new BackpropNeuronGroup();
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private CompressionNeuron[] compressedNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private BackpropNeuronGroup compressedLayer = new BackpropNeuronGroup();
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private BackpropNeuron[] outputHiddenNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private BackpropNeuronGroup outputHiddenLayer = new BackpropNeuronGroup();
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private OutputBackpropNeuron[][][] outputNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private BackpropNeuronGroup outputLayer = new BackpropNeuronGroup();
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private boolean learning = true;
    private static final int CHANNELS = 3;
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    private boolean compressionInputsSet = false;



    /**
     * creates an instance of NciBrain.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @param compression A value between 0.0 (inclusive) and 1.0 (exclusive)
     *		which represents the % of compression.
     */
    public NciBrain(double compression, int xSize, int ySize, boolean extraConnectivity)
    {
        double learningRate = 0.001;


        this.xSize = xSize;
        this.ySize = ySize;
        int compressedNeuronCount = ((int) Math.ceil((((double) xSize) * ((double) ySize) * ((double) CHANNELS)) * (1.0 - compression)));
        int hiddenNeuronCount = xSize*ySize*CHANNELS;
        //int hiddenNeuronCount = ((xSize * ySize * 3 - compressedNeuronCount) / 2) + compressedNeuronCount;
        this.inputNeurons = new InputBackpropNeuron[xSize][ySize][CHANNELS];
        this.inputHiddenNeurons = new BackpropNeuron[hiddenNeuronCount];
        this.compressedNeurons = new CompressionNeuron[compressedNeuronCount];
        this.outputHiddenNeurons = new BackpropNeuron[hiddenNeuronCount];
        this.outputNeurons = new OutputBackpropNeuron[xSize][ySize][CHANNELS];
        this.actualCompression = 1.0 - ((double) this.compressedNeurons.length) / (((double) xSize) * ((double) ySize) * ((double) CHANNELS));

        //create the input and output neurons and add it to the input layer
        int hiddenIndex = 0;
        ActivationFunction activationFunction = new SineActivationFunction();
        for (int yIndex = 0; yIndex < ySize; yIndex++)
            for (int xIndex = 0; xIndex < xSize; xIndex++)
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
                    this.inputNeurons[xIndex][yIndex][rgbIndex] = new InputBackpropNeuron(activationFunction, learningRate);
                    this.inputLayer.add(this.inputNeurons[xIndex][yIndex][rgbIndex]);

                    this.inputHiddenNeurons[hiddenIndex] = new BackpropNeuron(activationFunction, learningRate);
                    this.inputHiddenLayer.add(this.inputHiddenNeurons[hiddenIndex]);
                    
                    this.outputHiddenNeurons[hiddenIndex] = new BackpropNeuron(activationFunction, learningRate);
                    this.outputHiddenLayer.add(this.outputHiddenNeurons[hiddenIndex]);
                    
                    this.outputNeurons[xIndex][yIndex][rgbIndex] = new OutputBackpropNeuron(activationFunction, learningRate);
                    this.outputLayer.add(this.outputNeurons[xIndex][yIndex][rgbIndex]);
                    
                    hiddenIndex++;
                }

        //create the comrpession layer
        for (int compressionIndex = 0; compressionIndex < this.compressedNeurons.length; compressionIndex++)
        {
            this.compressedNeurons[compressionIndex] = new CompressionNeuron(activationFunction, learningRate);
            this.compressedLayer.add(this.compressedNeurons[compressionIndex]);
        }

		try
		{
			//connect the neurons together
			this.inputLayer.connectAllTo(this.compressedLayer);
			this.compressedLayer.connectAllTo(this.outputLayer);
		}
		catch(InvalidConnectionTypeDannException caughtException)
		{
			throw new Error("Could not connect layers", caughtException);
		}
        
//        this.inputLayer.connectAllTo(this.inputHiddenLayer);
//        this.inputHiddenLayer.connectAllTo(this.compressedLayer);
//        this.compressedLayer.connectAllTo(this.outputHiddenLayer);
//        this.outputHiddenLayer.connectAllTo(this.outputLayer);
//        this.addChild(this.inputHiddenLayer);
//        this.addChild(this.outputHiddenLayer);

        this.addNeurons(this.inputLayer.getChildrenNeuronsRecursivly());
        this.addNeurons(this.compressedLayer.getChildrenNeuronsRecursivly());
        this.addNeurons(this.outputLayer.getChildrenNeuronsRecursivly());


    //if you want to add an extra level of connectivity.
        /*
    if (extraConnectivity == true)
    {
    this.inputLayer.connectAllTo(this.compressedLayer);
    this.compressedLayer.connectAllTo(this.outputLayer);
    }
     */
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public double getCompression()
    {
        return this.actualCompression;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public boolean getLearning()
    {
        return this.learning;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public void setLearning(boolean learningToSet)
    {
        this.learning = learningToSet;
    }



	@SuppressWarnings("unchecked")
    public double getAverageWeight()
    {
        double weightSum = 0.0;
        double weightCount = 0.0;

        for (Neuron child : this.getNeurons())
        {
			try
			{
				Set<Synapse> childSynapses = child.getDestinations();

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



	@SuppressWarnings("unchecked")
    public double getAverageAbsoluteWeight()
    {
        double weightSum = 0.0;
        double weightCount = 0.0;

        for (Neuron child : this.getNeurons())
        {
			try
			{
				Set<Synapse> childSynapses = child.getDestinations();

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



    private void propagateLayer(BackpropNeuronGroup layer)
    {
        ArrayBlockingQueue<FutureTask> processing = new ArrayBlockingQueue<FutureTask>(this.xSize * this.ySize * CHANNELS, true);

        Set<BackpropNeuron> units = layer.getChildrenNeuronsRecursivly();
        for (BackpropNeuron unit : units)
        {
            PropagateRun propagateRun = new PropagateRun(unit);
            FutureTask<Void> futurePropagateRun = new FutureTask<Void>(propagateRun, null);

            processing.add(futurePropagateRun);
            executor.execute(futurePropagateRun);
        }

        while (processing.peek() != null)
            try
            {
                FutureTask currentRun = processing.take();
                currentRun.get();
            }
            catch (Exception e)
            {
                System.out.println("Danger Will Robinson, Danger! : " + e);
                e.printStackTrace();
                return;
            }
    }



	@SuppressWarnings("unchecked")
    private void backPropagateLayer(NeuronGroup layer)
    {
        ArrayBlockingQueue<FutureTask> processing = new ArrayBlockingQueue<FutureTask>(this.xSize * this.ySize * CHANNELS, true);

		try
		{
			Set<BackpropNeuron> units = layer.getChildrenNeuronsRecursivly();
			for (BackpropNeuron unit : units)
			{
				BackPropagateRun backPropagateRun = new BackPropagateRun(unit);
				FutureTask<Void> futureBackPropagateRun = new FutureTask<Void>(backPropagateRun, null);

				processing.add(futureBackPropagateRun);
				executor.execute(futureBackPropagateRun);
			}
		}
		catch(ClassCastException caughtException)
		{
			throw new AssertionError(caughtException);
		}

        while (processing.peek() != null)
            try
            {
                FutureTask currentRun = processing.take();
                currentRun.get();
            }
            catch (Exception e)
            {
                System.out.println("Danger Will Robinson, Danger! : " + e);
                e.printStackTrace();
                return;
            }
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private void propagate()
    {
        this.propagateLayer(this.inputLayer);
        this.propagateLayer(this.compressedLayer);
        this.propagateLayer(this.outputLayer);
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private void backPropagate()
    {
        this.backPropagateLayer(this.outputLayer);
        this.backPropagateLayer(this.compressedLayer);
        this.backPropagateLayer(this.inputLayer);
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


        //BufferedImage uncompressedImage = this.uncompress(this.compress(originalImage));



        if (this.learning == false)
            return uncompressedImage;

        //now back propogate
        this.backPropagate();

        //all done
        return uncompressedImage;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
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
        this.propagateLayer(this.inputLayer);
        this.propagateLayer(this.compressedLayer);

        int compressedDataIndex = 0;
        byte[] compressedData = new byte[this.compressedNeurons.length];
        for (CompressionNeuron compressionNeuron : this.compressedNeurons)
            compressedData[compressedDataIndex++] = compressionNeuron.getChannelOutput();

        return compressedData;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public BufferedImage uncompress(final byte[] compressedData)
    {
        int compressedDataIndex = 0;
        for (CompressionNeuron compressionNeuron : this.compressedNeurons)
            compressionNeuron.setInput(compressedData[compressedDataIndex++]);

        this.compressionInputsSet = true;

        this.propagateLayer(this.compressedLayer);
        this.propagateLayer(this.outputLayer);

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

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.defaultWriteObject();
	}
}
