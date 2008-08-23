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
//	private NeuronProcessingUnit[][][] inputHiddenNeurons = null;
	
	/**
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
//	private LayerProcessingUnit inputHiddenLayer = new LayerProcessingUnit(sharedDna);
	
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
//	private NeuronProcessingUnit[][][] outputHiddenNeurons = null;
	
	/**
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
//	private LayerProcessingUnit outputHiddenLayer = new LayerProcessingUnit(sharedDna);
	
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
	
	/**
	 * creates an instance of NciBrain.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param compression A value between 0.0 (inclusive) and 1.0 (exclusive)
	 *		which represents the % of compression.
	 */
	public NciBrain(double compression, int xSize, int ySize, boolean extraConnectivity)
	{
		this.xSize = xSize;
		this.ySize = ySize;
		this.inputNeurons = new InputNeuronProcessingUnit[xSize][ySize][4];
//		this.inputHiddenNeurons = new NeuronProcessingUnit[xSize][ySize][4];
		this.compressedNeurons = new CompressionNeuron[ ((int) Math.ceil(  (((double)xSize) * ((double)ySize) * 4.0) * (1.0 - compression)  )) ];
//		this.outputHiddenNeurons = new NeuronProcessingUnit[xSize][ySize][4];
		this.outputNeurons = new OutputNeuronProcessingUnit[xSize][ySize][4];
		this.actualCompression = 1.0 - ((double)this.compressedNeurons.length) / ( ((double)xSize) * ((double)ySize) *4.0 );
		
		//create the input and output neurons and add it to the input layer
		for(int xIndex = 0; xIndex < xSize; xIndex++)
		{
			for(int yIndex = 0; yIndex < ySize; yIndex++)
			{
				for(int rgbIndex = 0; rgbIndex < 4; rgbIndex++)
				{
					this.inputNeurons[xIndex][yIndex][rgbIndex] = new InputNeuronProcessingUnit(sharedDna);
					this.inputLayer.add(this.inputNeurons[xIndex][yIndex][rgbIndex]);
					
//					this.inputHiddenNeurons[xIndex][yIndex][rgbIndex] = new NeuronProcessingUnit(sharedDna);
//					this.inputHiddenLayer.add(this.inputHiddenNeurons[xIndex][yIndex][rgbIndex]);
					
//					this.outputHiddenNeurons[xIndex][yIndex][rgbIndex] = new NeuronProcessingUnit(sharedDna);
//					this.outputHiddenLayer.add(this.outputHiddenNeurons[xIndex][yIndex][rgbIndex]);
					
					this.outputNeurons[xIndex][yIndex][rgbIndex] = new OutputNeuronProcessingUnit(sharedDna);
					this.outputLayer.add(this.outputNeurons[xIndex][yIndex][rgbIndex]);
				}
			}
		}
		
		//create the comrpession layer
		for(int compressionIndex = 0; compressionIndex < this.compressedNeurons.length; compressionIndex++)
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
		if( extraConnectivity == true )
		{
//			this.inputLayer.connectAllTo(this.compressedLayer);
//			this.compressedLayer.connectAllTo(this.outputLayer);
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
	private void propogate()
	{
		this.inputLayer.propogate();
//		this.inputHiddenLayer.propogate();
		this.compressedLayer.propogate();
//		this.outputHiddenLayer.propogate();
		this.outputLayer.propogate();
	}
	
	/**
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	private void backPropogate()
	{
		this.outputLayer.backPropogate();
//		this.outputHiddenLayer.backPropogate();
		this.compressedLayer.backPropogate();
//		this.inputHiddenLayer.backPropogate();
		this.inputLayer.backPropogate();
	}
	
	/**
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	public BufferedImage uncompress(int[] compressedData)
	{
		BufferedImage uncompressedImage = new BufferedImage(this.xSize, this.ySize, BufferedImage.TYPE_4BYTE_ABGR);
		
		//set the comrpessed data on the compressed layer
		for( int compressedIndex = 0; compressedIndex < compressedData.length; compressedIndex++)
		{
			int currentData = compressedData[compressedIndex];
			int shiftLcv = 0;
			for(int lcv = (compressedIndex*4); lcv < this.compressedNeurons.length; lcv++ )
			{
				this.compressedNeurons[lcv].setInput( (currentData >> shiftLcv) & 0x000000FF );
				shiftLcv++;
			}
		}
		
		//propogate the output
		this.propogate();
		
		//obtain the image from the output
		for( int xIndex = 0; (xIndex < xSize) && (xIndex < uncompressedImage.getWidth()); xIndex++ )
		{
			for( int yIndex = 0; (yIndex < ySize) && (yIndex < uncompressedImage.getHeight()); yIndex++ )
			{
				//int rgbCurrent = imageToCompress.getRGB(xIndex, yIndex);
				int rgbCurrent = 0;
				for( int rgbIndex = 0; rgbIndex < 4; rgbIndex++ )
				{
					double output = this.outputNeurons[xIndex][yIndex][rgbIndex].getOutput();
					int channel = (int) Math.ceil(output * 256.0);
					if( channel >= 256 )
						channel = 255;
					
					rgbCurrent |= (channel & 0x000000FF) << rgbIndex;
				}
				uncompressedImage.setRGB(xIndex, yIndex, rgbCurrent);
			}
		}
		
		//unset the inputs on the compression layer
		for( int compressedIndex = 0; compressedIndex < this.compressedNeurons.length; compressedIndex++ )
		{
			this.compressedNeurons[compressedIndex].unsetInput();
		}
		
		return uncompressedImage;
	}
	
	/**
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	public int[] compress(BufferedImage imageToCompress)
	{
		//set the image onto the inputs
		for( int xIndex = 0; (xIndex < xSize) && (xIndex < imageToCompress.getWidth()); xIndex++ )
		{
			for( int yIndex = 0; (yIndex < ySize) && (yIndex < imageToCompress.getHeight()); yIndex++ )
			{
				int rgbCurrent = imageToCompress.getRGB(xIndex, yIndex);
				for( int rgbIndex = 0; rgbIndex < 4; rgbIndex++ )
				{
					int channel = ((rgbCurrent >> rgbIndex) & 0x000000FF);
					double input = ((double)channel) / 255.0;
					
					this.inputNeurons[xIndex][yIndex][rgbIndex].setInput(input);
				}
			}
		}
		
		//propogate the output
		this.propogate();

		//obtain the compressed image
		int[] compressedImage = new int[((int)Math.ceil(this.compressedNeurons.length/4))];
		for( int compressedIndex = 0; compressedIndex < compressedImage.length; compressedIndex++)
		{
			compressedImage[compressedIndex] = 0;
			int shiftLcv = 0;
			for(int lcv = (compressedIndex*4); lcv < this.compressedNeurons.length; lcv++ )
			{
				compressedImage[compressedIndex] |= this.compressedNeurons[lcv].getChannelOutput() << shiftLcv;
				shiftLcv++;
			}
		}
		
		//if we arent activly learning just return
		if( this.learning == false )
			return compressedImage;
		
		//since we are learning set the original image as the training data
		for( int xIndex = 0; (xIndex < xSize) && (xIndex < imageToCompress.getWidth()); xIndex++ )
		{
			for( int yIndex = 0; (yIndex < ySize) && (yIndex < imageToCompress.getHeight()); yIndex++ )
			{
				int rgbCurrent = imageToCompress.getRGB(xIndex, yIndex);
				for( int rgbIndex = 0; rgbIndex < 4; rgbIndex++ )
				{
					int channel = ((rgbCurrent >> rgbIndex) & 0x000000FF);
					double input = ((double)channel) / 255.0;
					
					this.outputNeurons[xIndex][yIndex][rgbIndex].setDesired(input);
				}
			}
		}
		
		//now back propogate
		this.backPropogate();
		
		//all done
		return compressedImage;
	}
}
