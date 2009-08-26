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
package com.syncleus.core.dann.examples.colormap;

import java.util.concurrent.Callable;
import java.util.Map;
import java.util.Random;
import com.syncleus.dann.math.Vector;
import com.syncleus.dann.neural.som.brain.ExponentialDecaySomBrain;
import java.util.Map.Entry;
import java.awt.Color;
import org.apache.log4j.Logger;

public class ColorMap2dCallable implements Callable<Color[][]>
{
	private volatile int iterations;
	private volatile double learningRate;
	private volatile int width;
	private volatile int height;

	private static Random random = new Random();

	private volatile int progress;

	private final static Logger LOGGER = Logger.getLogger(ColorMap2dCallable.class);

	public ColorMap2dCallable(int iterations, double learningRate, int width, int height)
	{
		this.iterations = iterations;
		this.learningRate = learningRate;
		this.width = width;
		this.height = height;
	}

	public Color[][] call()
	{
		try
		{
			//initialize brain
			ExponentialDecaySomBrain brain = new ExponentialDecaySomBrain(3, 2, getIterations(), getLearningRate());

			//create the output latice
			for(double x = 0; x < getWidth(); x++)
				for(double y = 0; y < getHeight(); y++)
					brain.createOutput(new Vector(new double[]{x, y}));

			//run through random training data
			for(int iteration = 0; iteration < getIterations(); iteration++)
			{
				this.progress++;

				brain.setInput(0, random.nextDouble());
				brain.setInput(1, random.nextDouble());
				brain.setInput(2, random.nextDouble());

				brain.getBestMatchingUnit(true);
			}

			//pull the output weight vectors
			Map<Vector, double[]> outputWeightVectors = brain.getOutputWeightVectors();

			//construct the color array
			Color[][] colorPositions = new Color[getWidth()][getHeight()];
			for(Entry<Vector, double[]> weightVector : outputWeightVectors.entrySet())
			{
				Vector currentPoint = weightVector.getKey();
				double[] currentVector = weightVector.getValue();

				//convert the current Vector to a color.
				Color currentColor = new Color((float)currentVector[0], (float)currentVector[1], (float)currentVector[2]);

				//add the current color to the colorPositions
				colorPositions[(int)Math.floor(currentPoint.getCoordinate(1))][(int)Math.floor(currentPoint.getCoordinate(2))] = currentColor;
			}

			//return the color positions
			return colorPositions;
		}
		catch(Exception caught)
		{
			LOGGER.error("Exception was caught", caught);
			throw new RuntimeException("Throwable was caught", caught);
		}
		catch(Error caught)
		{
			LOGGER.error("Error was caught", caught);
			throw new Error("Throwable was caught");
		}
	}



	/**
	 * @return the iterations
	 */
	public int getIterations()
	{
		return iterations;
	}



	/**
	 * @return the learningRate
	 */
	public double getLearningRate()
	{
		return learningRate;
	}



	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}



	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}



	/**
	 * @return the progress
	 */
	public int getProgress()
	{
		return progress;
	}
}
