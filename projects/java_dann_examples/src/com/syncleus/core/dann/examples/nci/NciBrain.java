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
import com.syncleus.dann.activation.*;
import java.awt.image.BufferedImage;
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
    private static final DNA sharedDna = new DNA();
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
    private InputNeuron[][][] inputNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private NeuronGroup inputLayer = new NeuronGroup(sharedDna);
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
//    private Neuron[] inputHiddenNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
//    private NeuronGroup inputHiddenLayer = new NeuronGroup(sharedDna);
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private CompressionNeuron[] compressedNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private NeuronGroup compressedLayer = new NeuronGroup(sharedDna);
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
//    private Neuron[] outputHiddenNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
//    private NeuronGroup outputHiddenLayer = new NeuronGroup(sharedDna);
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private OutputNeuron[][][] outputNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private NeuronGroup outputLayer = new NeuronGroup(sharedDna);
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
        this.sharedDna.learningRate = 0.001;


        this.xSize = xSize;
        this.ySize = ySize;
        int compressedNeuronCount = ((int) Math.ceil((((double) xSize) * ((double) ySize) * ((double) CHANNELS)) * (1.0 - compression)));
//        int hiddenNeuronCount = ((xSize * ySize * 3 - compressedNeuronCount) / 2) + compressedNeuronCount;
        this.inputNeurons = new InputNeuron[xSize][ySize][CHANNELS];
//        this.inputHiddenNeurons = new Neuron[hiddenNeuronCount];
        this.compressedNeurons = new CompressionNeuron[compressedNeuronCount];
//        this.outputHiddenNeurons = new Neuron[hiddenNeuronCount];
        this.outputNeurons = new OutputNeuron[xSize][ySize][CHANNELS];
        this.actualCompression = 1.0 - ((double) this.compressedNeurons.length) / (((double) xSize) * ((double) ySize) * ((double) CHANNELS));

        //create the input and output neurons and add it to the input layer
        ActivationFunction activationFunction = new SineActivationFunction();
        for (int yIndex = 0; yIndex < ySize; yIndex++)
            for (int xIndex = 0; xIndex < xSize; xIndex++)
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
                    this.inputNeurons[xIndex][yIndex][rgbIndex] = new InputNeuron(sharedDna, activationFunction);
                    this.inputLayer.add(this.inputNeurons[xIndex][yIndex][rgbIndex]);

                    this.outputNeurons[xIndex][yIndex][rgbIndex] = new OutputNeuron(sharedDna, activationFunction);
                    this.outputLayer.add(this.outputNeurons[xIndex][yIndex][rgbIndex]);
                }

        //create the comrpession layer
        for (int compressionIndex = 0; compressionIndex < this.compressedNeurons.length; compressionIndex++)
        {
            this.compressedNeurons[compressionIndex] = new CompressionNeuron(sharedDna, activationFunction);
            this.compressedLayer.add(this.compressedNeurons[compressionIndex]);
        }

        //connect the neurons together
        this.inputLayer.connectAllTo(this.compressedLayer);
        this.compressedLayer.connectAllTo(this.outputLayer);

        this.addChild(this.inputLayer);
        this.addChild(this.compressedLayer);
        this.addChild(this.outputLayer);


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



    public double getAverageWeight()
    {
        double weightSum = 0.0;
        double weightCount = 0.0;

        Set<Neuron> allChildren = this.getChildrenNeuronsRecursivly();
        for (Neuron child : allChildren)
        {
            Set<Synapse> childSynapses = child.getDestinations();

            for (Synapse childSynapse : childSynapses)
            {
                weightSum += childSynapse.getWeight();
                weightCount++;
            }
        }

        return weightSum / weightCount;
    }



    public double getAverageAbsoluteWeight()
    {
        double weightSum = 0.0;
        double weightCount = 0.0;

        Set<Neuron> allChildren = this.getChildrenNeuronsRecursivly();
        for (Neuron child : allChildren)
        {
            Set<Synapse> childSynapses = child.getDestinations();

            for (Synapse childSynapse : childSynapses)
            {
                weightSum += Math.abs(childSynapse.getWeight());
                weightCount++;
            }
        }

        return weightSum / weightCount;
    }



    private void propagateLayer(NeuronGroup layer)
    {
        ArrayBlockingQueue<FutureTask> processing = new ArrayBlockingQueue<FutureTask>(this.xSize * this.ySize * CHANNELS, true);

        Set<Neuron> units = layer.getChildrenNeuronsRecursivly();
        for (Neuron unit : units)
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



    private void backPropagateLayer(NeuronGroup layer)
    {
        ArrayBlockingQueue<FutureTask> processing = new ArrayBlockingQueue<FutureTask>(this.xSize * this.ySize * CHANNELS, true);

        Set<Neuron> units = layer.getChildrenNeuronsRecursivly();
        for (Neuron unit : units)
        {
            BackPropagateRun backPropagateRun = new BackPropagateRun(unit);
            FutureTask<Void> futureBackPropagateRun = new FutureTask<Void>(backPropagateRun, null);

            processing.add(futureBackPropagateRun);
            executor.execute(futureBackPropagateRun);
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

                    int channel = (new Double((output + 1.0) * 127.5)).intValue();

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

                    int channel = (new Double((output + 1.0) * 127.5)).intValue();

                    rgbCurrent |= (((int) channel) & 0x000000FF) << (rgbIndex * 8);
                }
                finalRgbArray[xSize * yIndex + (xIndex)] = rgbCurrent;
            }
        uncompressedImage.setRGB(0, 0, xSize, ySize, finalRgbArray, 0, xSize);

        return uncompressedImage;
    }
}
