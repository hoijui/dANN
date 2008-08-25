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
        int compressedNeuronCount = ((int) Math.ceil((((double) xSize) * ((double) ySize) * ((double)CHANNELS)) * (1.0 - compression)));
        int hiddenNeuronCount = ((xSize*ySize*3 - compressedNeuronCount)/2)+compressedNeuronCount;
//        this.inputNeurons = new InputNeuronProcessingUnit[xSize][ySize][3];
        this.inputNeurons = new InputNeuronProcessingUnit[xSize][ySize][CHANNELS];
//        this.inputHiddenNeurons = new NeuronProcessingUnit[hiddenNeuronCount];
        this.compressedNeurons = new CompressionNeuron[compressedNeuronCount];
//        this.outputHiddenNeurons = new NeuronProcessingUnit[hiddenNeuronCount];
//        this.outputNeurons = new OutputNeuronProcessingUnit[xSize][ySize][3];
        this.outputNeurons = new OutputNeuronProcessingUnit[xSize][ySize][CHANNELS];
        this.actualCompression = 1.0 - ((double) this.compressedNeurons.length) / (((double) xSize) * ((double) ySize) * ((double)CHANNELS) );

        //create the input and output neurons and add it to the input layer
        for (int xIndex = 0; xIndex < xSize; xIndex++)
            for (int yIndex = 0; yIndex < ySize; yIndex++)
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
                    this.inputNeurons[xIndex][yIndex][rgbIndex] = new InputNeuronProcessingUnit(sharedDna);
                    this.inputLayer.add(this.inputNeurons[xIndex][yIndex][rgbIndex]);

                    this.outputNeurons[xIndex][yIndex][rgbIndex] = new OutputNeuronProcessingUnit(sharedDna);
                    this.outputLayer.add(this.outputNeurons[xIndex][yIndex][rgbIndex]);
                }

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



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private void propagate()
    {
        this.inputLayer.propagate();
//        this.inputHiddenLayer.propagate();
        this.compressedLayer.propagate();
//        this.outputHiddenLayer.propagate();
        this.outputLayer.propagate();
    }



    /**
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    private void backPropagate()
    {
        this.outputLayer.backPropagate();
//        this.outputHiddenLayer.backPropagate();
        this.compressedLayer.backPropagate();
//        this.inputHiddenLayer.backPropagate();
        this.inputLayer.backPropagate();
    }
    
    
    public BufferedImage test(BufferedImage originalImage)
    {
        int[] originalRgbArray = new int[xSize*ySize];
        originalImage.getRGB(0, 0, (originalImage.getWidth() < xSize ? originalImage.getWidth() : xSize), (originalImage.getHeight() < ySize ? originalImage.getHeight() : ySize), originalRgbArray, 0, xSize);
        
        
        //set the image onto the inputs
        for (int xIndex = 0; (xIndex < xSize) && (xIndex < originalImage.getWidth()); xIndex++)
            for (int yIndex = 0; (yIndex < ySize) && (yIndex < originalImage.getHeight()); yIndex++)
            {
//                int rgbCurrent = originalImage.getRGB(xIndex, yIndex);
                int rgbCurrent = originalRgbArray[yIndex*xSize + xIndex];
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
                    byte channel = (byte) (((rgbCurrent >> (rgbIndex * 8)) & 0x000000FF) - 127);

                    this.inputNeurons[xIndex][yIndex][rgbIndex].setInput(channel);
                }
            }
         

        //propogate the output
        this.propagate();
        
        
        int[] finalRgbArray = new int[xSize*ySize];
        BufferedImage uncompressedImage = new BufferedImage(this.xSize, this.ySize, BufferedImage.TYPE_INT_RGB);
        for (int xIndex = 0; (xIndex < xSize) && (xIndex < uncompressedImage.getWidth()); xIndex++)
            for (int yIndex = 0; (yIndex < ySize) && (yIndex < uncompressedImage.getHeight()); yIndex++)
            {
                //int rgbCurrent = imageToCompress.getRGB(xIndex, yIndex);
                int rgbCurrent = 0;
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
                    double output = this.outputNeurons[xIndex][yIndex][rgbIndex].getOutput();
                    int channel = (int) (Math.ceil((output + 1.0) * 128.0) - 1.0);

                    rgbCurrent |= (channel & 0x000000FF) << (rgbIndex * 8);
                }
                //uncompressedImage.setRGB(xIndex, yIndex, rgbCurrent);
                finalRgbArray[xSize*yIndex + (xIndex)] = rgbCurrent;
            }
        uncompressedImage.setRGB(0, 0, xSize, ySize, finalRgbArray, 0, xSize);
        
        
        if (this.learning == false)
            return uncompressedImage;

        //since we are learning set the original image as the training data
        for (int xIndex = 0; (xIndex < xSize) && (xIndex < originalImage.getWidth()); xIndex++)
            for (int yIndex = 0; (yIndex < ySize) && (yIndex < originalImage.getHeight()); yIndex++)
            {
//                int rgbCurrent = originalImage.getRGB(xIndex, yIndex);
                int rgbCurrent = originalRgbArray[yIndex*xSize + xIndex];
                for (int rgbIndex = 0; rgbIndex < CHANNELS; rgbIndex++)
                {
                    int channel = ((rgbCurrent >> (rgbIndex * 8)) & 0x000000FF);
                    double input = ((((double) channel) * 2) / 255.0) - 1;

                    this.outputNeurons[xIndex][yIndex][rgbIndex].setDesired(input);
                }
            }

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
