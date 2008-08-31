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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


/**
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class NciBrain implements java.io.Serializable
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
    private InputNeuronProcessingUnit[][][] inputNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private LayerProcessingUnit inputLayer = new LayerProcessingUnit(sharedDna);
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
//    private NeuronProcessingUnit[] inputHiddenNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
//    private LayerProcessingUnit inputHiddenLayer = new LayerProcessingUnit(sharedDna);
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private CompressionNeuron[] compressedNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private LayerProcessingUnit compressedLayer = new LayerProcessingUnit(sharedDna);
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
//    private NeuronProcessingUnit[] outputHiddenNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
//    private LayerProcessingUnit outputHiddenLayer = new LayerProcessingUnit(sharedDna);
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private OutputNeuronProcessingUnit[][][] outputNeurons = null;
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private LayerProcessingUnit outputLayer = new LayerProcessingUnit(sharedDna);
    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private boolean learning = true;
    private static final int CHANNELS = 1;
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);



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
//        System.out.println("Network: " + (xSize * ySize) + " ->" + compressedNeuronCount + "->" + (xSize * ySize));
//        int hiddenNeuronCount = ((xSize * ySize * 3 - compressedNeuronCount) / 2) + compressedNeuronCount;
//        this.inputNeurons = new InputNeuronProcessingUnit[xSize][ySize][3];
        this.inputNeurons = new InputNeuronProcessingUnit[xSize][ySize][CHANNELS];
//        this.inputHiddenNeurons = new NeuronProcessingUnit[hiddenNeuronCount];
        this.compressedNeurons = new CompressionNeuron[compressedNeuronCount];
//        this.compressedNeurons = new CompressionNeuron[xSize*ySize*CHANNELS];
//        this.outputHiddenNeurons = new NeuronProcessingUnit[hiddenNeuronCount];
//        this.outputNeurons = new OutputNeuronProcessingUnit[xSize][ySize][3];
        this.outputNeurons = new OutputNeuronProcessingUnit[xSize][ySize][CHANNELS];
        this.actualCompression = 1.0 - ((double) this.compressedNeurons.length) / (((double) xSize) * ((double) ySize) * ((double) CHANNELS));

        //create the input and output neurons and add it to the input layer
        for (int yIndex = 0; yIndex < ySize; yIndex++)
            for (int xIndex = 0; xIndex < xSize; xIndex++)
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
                    this.inputNeurons[xIndex][yIndex][rgbIndex] = new InputNeuronProcessingUnit(sharedDna);
                    this.inputLayer.add(this.inputNeurons[xIndex][yIndex][rgbIndex]);

//                    this.compressedNeurons[yIndex*xSize + xIndex] = new CompressionNeuron(sharedDna);
//                    this.compressedLayer.add(this.compressedNeurons[yIndex*xSize + xIndex]);

                    this.outputNeurons[xIndex][yIndex][rgbIndex] = new OutputNeuronProcessingUnit(sharedDna);
                    this.outputLayer.add(this.outputNeurons[xIndex][yIndex][rgbIndex]);

//                    this.inputNeurons[xIndex][yIndex][rgbIndex].connectTo(this.compressedNeurons[yIndex*xSize + xIndex]);
//                    this.compressedNeurons[yIndex*xSize + xIndex].connectTo(this.outputNeurons[xIndex][yIndex][rgbIndex]);
                    /*
                for (int yIndexOld = 0; yIndexOld < yIndex; yIndexOld++)
                for (int xIndexOld = 0; xIndexOld < ((yIndexOld+1) >= ySize? xIndex : xSize); xIndexOld++)
                for (int rgbIndexOld = 0; rgbIndexOld < ((yIndexOld+1) >= ySize? ((xIndexOld+1) >= xSize? rgbIndex : CHANNELS) : CHANNELS); rgbIndexOld++)
                {
                //                              if(yIndexOld < 6)
                //                              {
                this.inputNeurons[xIndex][yIndex][rgbIndex].connectTo(this.compressedNeurons[yIndexOld*xSize + xIndexOld]);
                this.compressedNeurons[yIndex*xSize + xIndex].connectTo(this.outputNeurons[xIndexOld][yIndexOld][rgbIndexOld]);
                //                              }
                }*/

//                    this.inputNeurons[xIndex][yIndex][rgbIndex].connectTo(this.outputNeurons[xIndex][yIndex][rgbIndex]);
                }









        /*
        //create the input and output neurons and add it to the input layer
        for (int yIndex = 0; yIndex < ySize; yIndex++)
        for (int xIndex = 0; xIndex < xSize; xIndex++)
        for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
        {
        this.outputNeurons[xIndex][yIndex][rgbIndex] = new OutputNeuronProcessingUnit(sharedDna);
        this.outputLayer.add(this.outputNeurons[xIndex][yIndex][rgbIndex]);
        
        for (int yIndexIn = 0; yIndexIn < ySize; yIndexIn++)
        for (int xIndexIn = 0; xIndexIn < xSize; xIndexIn++)
        for (int rgbIndexIn = 0; rgbIndexIn < CHANNELS; rgbIndexIn++)
        {
        this.inputNeurons[xIndexIn][yIndexIn][rgbIndexIn].connectTo(this.outputNeurons[xIndex][yIndex][rgbIndex]);
        }
        }
         */








        /*
        for (int hiddenIndex = 0; hiddenIndex < this.inputHiddenNeurons.length; hiddenIndex++)
        {
        this.inputHiddenNeurons[hiddenIndex] = new NeuronProcessingUnit(sharedDna);
        this.inputHiddenLayer.add(this.inputHiddenNeurons[hiddenIndex]);
        }
        
        for (int hiddenIndex = 0; hiddenIndex < this.outputHiddenNeurons.length; hiddenIndex++)
        {
        this.outputHiddenNeurons[hiddenIndex] = new NeuronProcessingUnit(sharedDna);
        this.outputHiddenLayer.add(this.outputHiddenNeurons[hiddenIndex]);
        }
         */

        //create the comrpession layer
        for (int compressionIndex = 0; compressionIndex < this.compressedNeurons.length; compressionIndex++)
        {
            this.compressedNeurons[compressionIndex] = new CompressionNeuron(sharedDna);
            this.compressedLayer.add(this.compressedNeurons[compressionIndex]);
        }

        //connect the neurons together
        this.inputLayer.connectAllTo(this.compressedLayer);
        this.compressedLayer.connectAllTo(this.outputLayer);
//        this.inputLayer.connectAllTo(this.outputLayer);

        /*
        this.inputLayer.connectAllTo(this.inputHiddenLayer);
        this.inputHiddenLayer.connectAllTo(this.compressedLayer);
        this.compressedLayer.connectAllTo(this.outputHiddenLayer);
        this.outputHiddenLayer.connectAllTo(this.outputLayer);
         * */


        //if you want to add an extra level of connectivity.
        if (extraConnectivity == true)
        {
//            this.inputLayer.connectAllTo(this.compressedLayer);
//            this.compressedLayer.connectAllTo(this.outputLayer);
        }
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
            
            LayerProcessingUnit currentLayer = this.inputLayer;
            while(currentLayer != null)
            {
                ArrayList<ProcessingUnit> children = currentLayer.getChildrenRecursivly();
                for(ProcessingUnit child : children)
                {
                    if(child instanceof NeuronProcessingUnit)
                    {
                        NeuronProcessingUnit childNeuron = (NeuronProcessingUnit) child;
                        Synapse[] childSynapses = childNeuron.getDestinations();
                        
                        for(Synapse childSynapse : childSynapses)
                        {
                            weightSum += childSynapse.getWeight();
                            weightCount++;
                        }
                    }
                }
                
                if(currentLayer == this.inputLayer)
                    currentLayer = this.compressedLayer;
                else
                    currentLayer = null;
            }
            
            return weightSum / weightCount;
    }
    
    public double getAverageAbsoluteWeight()
    {
            double weightSum = 0.0;
            double weightCount = 0.0;
            
            LayerProcessingUnit currentLayer = this.inputLayer;
            while(currentLayer != null)
            {
                ArrayList<ProcessingUnit> children = currentLayer.getChildrenRecursivly();
                for(ProcessingUnit child : children)
                {
                    if(child instanceof NeuronProcessingUnit)
                    {
                        NeuronProcessingUnit childNeuron = (NeuronProcessingUnit) child;
                        Synapse[] childSynapses = childNeuron.getDestinations();
                        
                        for(Synapse childSynapse : childSynapses)
                        {
                            weightSum += Math.abs(childSynapse.getWeight());
                            weightCount++;
                        }
                    }
                }
                
                if(currentLayer == this.inputLayer)
                    currentLayer = this.compressedLayer;
                else
                    currentLayer = null;
            }
            
            return weightSum / weightCount;
    }
    
    
    private void propagateLayer(LayerProcessingUnit layer)
    {
        ArrayBlockingQueue<FutureTask> processing = new ArrayBlockingQueue<FutureTask>(this.xSize * this.ySize * CHANNELS, true);

        ArrayList<ProcessingUnit> units = layer.getChildrenRecursivly();
        for (ProcessingUnit unit : units)
        {
            PropagateRun propagateRun = new PropagateRun(unit);
            FutureTask futurePropagateRun = new FutureTask(propagateRun, null);

            processing.add(futurePropagateRun);
            executor.execute(futurePropagateRun);
        }
        
        while (processing.peek() != null)
        {
            try
            {
                FutureTask currentRun = processing.take();
                currentRun.get();

//                while(currentRun.isDone() == false)
//                    Thread.sleep(1);
            }
            catch(Exception e)
            {
                System.out.println("Danger Will Robinson, Danger! : " + e);
                e.printStackTrace();
                return;
            }
        }
    }
    
    
    
    private void backPropagateLayer(LayerProcessingUnit layer)
    {
        ArrayBlockingQueue<FutureTask> processing = new ArrayBlockingQueue<FutureTask>(this.xSize * this.ySize * CHANNELS, true);

        ArrayList<ProcessingUnit> units = layer.getChildrenRecursivly();
        for (ProcessingUnit unit : units)
        {
            BackPropagateRun backPropagateRun = new BackPropagateRun(unit);
            FutureTask futureBackPropagateRun = new FutureTask(backPropagateRun, null);

            processing.add(futureBackPropagateRun);
            executor.execute(futureBackPropagateRun);
        }
        
        while (processing.peek() != null)
        {
            try
            {
                FutureTask currentRun = processing.take();
                currentRun.get();

//                while(currentRun.isDone() == false)
//                    Thread.sleep(1);
            }
            catch(Exception e)
            {
                System.out.println("Danger Will Robinson, Danger! : " + e);
                e.printStackTrace();
                return;
            }
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

    /*
    this.inputLayer.propagate();
    //        this.inputHiddenLayer.propagate();
    this.compressedLayer.propagate();
    //        this.outputHiddenLayer.propagate();
    this.outputLayer.propagate();
     */
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
        
        /*
        this.outputLayer.backPropagate();
//        this.outputHiddenLayer.backPropagate();
        this.compressedLayer.backPropagate();
//        this.inputHiddenLayer.backPropagate();
        this.inputLayer.backPropagate();
         */
    }



    public BufferedImage test(BufferedImage originalImage)
    {
        int[] originalRgbArray = new int[xSize * ySize];
        originalImage.getRGB(0, 0, (originalImage.getWidth() < xSize ? originalImage.getWidth() : xSize), (originalImage.getHeight() < ySize ? originalImage.getHeight() : ySize), originalRgbArray, 0, xSize);

        boolean DEBUG = false;
        if (this.learning == false)
            DEBUG = false;


        //set the image onto the inputs
        for (int yIndex = 0; (yIndex < ySize) && (yIndex < originalImage.getHeight()); yIndex++)
            for (int xIndex = 0; (xIndex < xSize) && (xIndex < originalImage.getWidth()); xIndex++)
            {
                int rgbCurrent = originalRgbArray[yIndex * xSize + xIndex];
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {


                    int channel = (int) (((rgbCurrent >> (rgbIndex * 8)) & 0x000000FF));
                    double input = (((double) channel) / 127.5) - 1.0;

                    if ((yIndex == 0) && (xIndex == 0) && (DEBUG))
                    {
                        System.out.println("applying inputs...");
                        System.out.println("rgbCurrent: " + rgbCurrent + " : " + Integer.toHexString(rgbCurrent));
                        System.out.println("channel: " + channel + " : " + Integer.toHexString(channel));
                        System.out.println("input: " + input + " : " + Double.toHexString(input));
                        System.out.println();
                    }

                    this.inputNeurons[xIndex][yIndex][rgbIndex].setInput(input);
                    this.outputNeurons[xIndex][yIndex][rgbIndex].setDesired(input);
                }
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

//                    int channel = (int) (Math.ceil((output + 1.0) * 128.0) - 1.0);
                    rgbCurrent |= (((int) channel) & 0x000000FF) << (rgbIndex * 8);

                    if ((yIndex == 0) && (xIndex == 0) && (DEBUG))
                    {
                        System.out.println("getting outputs...");
                        System.out.println("output: " + output + " : " + Double.toHexString(output));
                        System.out.println("channel: " + channel + " : " + Integer.toHexString(channel));
                        System.out.println("rgbCurrent: " + rgbCurrent + " : " + Integer.toHexString(rgbCurrent));
                        System.out.println();
                    }
                }

                //uncompressedImage.setRGB(xIndex, yIndex, rgbCurrent);
                finalRgbArray[xSize * yIndex + (xIndex)] = rgbCurrent;
            }
        uncompressedImage.setRGB(0, 0, xSize, ySize, finalRgbArray, 0, xSize);
        


        if (this.learning == false)
            return uncompressedImage;

        //since we are learning set the original image as the training data
        /*
        for (int yIndex = 0; (yIndex < ySize) && (yIndex < originalImage.getHeight()); yIndex++)
        for (int xIndex = 0; (xIndex < xSize) && (xIndex < originalImage.getWidth()); xIndex++)
        {
        //                int rgbCurrent = originalImage.getRGB(xIndex, yIndex);
        int rgbCurrent = originalRgbArray[yIndex * xSize + xIndex];
        for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
        {
        //                    int channel = ((rgbCurrent >> (rgbIndex * 8)) & 0x000000FF);
        //                    double input = ((((double) channel) * 2) / 255.0) - 1;
        
        //                    byte channel = (byte) (((rgbCurrent >> (rgbIndex * 8)) & 0x000000FF));
        
        //                    double input = ((double) channel) / 128.0;
        
        int channel = (int) (((rgbCurrent >> (rgbIndex * 8)) & 0x000000FF));
        double input = (((double) channel) / 127.5) - 1.0;
        
        this.outputNeurons[xIndex][yIndex][rgbIndex].setDesired(input);
        
        if ((yIndex == 0) && (xIndex == 0) && (DEBUG))
        {
        System.out.println("applying training...");
        System.out.println("rgbCurrent: " + rgbCurrent + " : " + Integer.toHexString(rgbCurrent));
        System.out.println("channel: " + channel + " : " + Integer.toHexString(channel));
        System.out.println("input: " + input + " : " + Double.toHexString(input));
        System.out.println();
        }
        }
        }*/

        //now back propogate
        this.backPropagate();

        //all done
        return uncompressedImage;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public BufferedImage uncompress(byte[] compressedData)
    {
        System.out.println("Oh no! uncompressing");
        BufferedImage uncompressedImage = new BufferedImage(this.xSize, this.ySize, BufferedImage.TYPE_INT_RGB);

        //set the comrpessed data on the compressed layer
        for (int compressedIndex = 0; compressedIndex < compressedData.length; compressedIndex++)
        {
            byte currentData = compressedData[compressedIndex];
            this.compressedNeurons[compressedIndex].setInput(currentData);
        }

        //propogate the output
//        this.propagate();
        this.compressedLayer.propagate();
//        this.outputHiddenLayer.propagate();
        this.outputLayer.propagate();

        //obtain the image from the output
        for (int xIndex = 0; (xIndex < xSize) && (xIndex < uncompressedImage.getWidth()); xIndex++)
            for (int yIndex = 0; (yIndex < ySize) && (yIndex < uncompressedImage.getHeight()); yIndex++)
            {
                //int rgbCurrent = imageToCompress.getRGB(xIndex, yIndex);
                int rgbCurrent = 0;
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
                    double output = this.outputNeurons[xIndex][yIndex][rgbIndex].getOutput();
                    int channel = (int) (Math.ceil((output + 1.0) * 128.0) - 1.0);

                    if ((xIndex == 2) && (yIndex == 2))
                        if (rgbIndex == 0)
                            System.out.println();

                    rgbCurrent |= (channel & 0x000000FF) << (rgbIndex * 8);

                    if ((xIndex == 2) && (yIndex == 2))
                        System.out.println("getting final: " + output + " -> " + Integer.toHexString(channel) + " -> " + Integer.toHexString(rgbCurrent));
                }
                uncompressedImage.setRGB(xIndex, yIndex, rgbCurrent);
            }

        //unset the inputs on the compression layer
        for (int compressedIndex = 0; compressedIndex < this.compressedNeurons.length; compressedIndex++)
            this.compressedNeurons[compressedIndex].unsetInput();

        return uncompressedImage;
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    public byte[] compress(BufferedImage imageToCompress)
    {
        System.out.println("Oh no! compressing");
        //set the image onto the inputs
        for (int xIndex = 0; (xIndex < xSize) && (xIndex < imageToCompress.getWidth()); xIndex++)
            for (int yIndex = 0; (yIndex < ySize) && (yIndex < imageToCompress.getHeight()); yIndex++)
            {
                int rgbCurrent = imageToCompress.getRGB(xIndex, yIndex);
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
                    byte channel = (byte) (((rgbCurrent >> (rgbIndex * 8)) & 0x000000FF) - 127);
                    //int channel = (int) Math.ceil((output + 1.0) * 128) - 1;

                    this.inputNeurons[xIndex][yIndex][rgbIndex].setInput(channel);
                }
                if ((xIndex == 2) && (yIndex == 2))
                    System.out.println("original as input: " + Integer.toHexString(rgbCurrent));
            }

        //propogate the output
//        this.propagate();
        this.inputLayer.propagate();
//        this.inputHiddenLayer.propagate();

        //obtain the compressed image
        byte[] compressedImage = new byte[this.compressedNeurons.length];
        for (int compressedIndex = 0; compressedIndex < compressedImage.length; compressedIndex++)
            compressedImage[compressedIndex] = this.compressedNeurons[compressedIndex].getChannelOutput();

        //if we arent activly learning just return
        if (this.learning == false)
            return compressedImage;

        this.uncompress(compressedImage);

        //since we are learning set the original image as the training data
        for (int xIndex = 0; (xIndex < xSize) && (xIndex < imageToCompress.getWidth()); xIndex++)
            for (int yIndex = 0; (yIndex < ySize) && (yIndex < imageToCompress.getHeight()); yIndex++)
            {
                int rgbCurrent = imageToCompress.getRGB(xIndex, yIndex);
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
                    int channel = ((rgbCurrent >> (rgbIndex * 8)) & 0x000000FF);
                    double input = ((((double) channel) * 2) / 255.0) - 1;

                    if ((xIndex == 2) && (yIndex == 2))
                        System.out.println("original as training: " + Integer.toHexString(rgbCurrent) + " -> " + Integer.toHexString(channel) + " -> " + input);

                    this.outputNeurons[xIndex][yIndex][rgbIndex].setDesired(input);
                }
            }

        //now back propogate
        this.backPropagate();

        //all done
        return compressedImage;
    }
}
