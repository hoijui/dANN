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

import java.awt.Color;
import java.util.concurrent.Callable;
import java.util.Map;
import java.util.Random;
import com.syncleus.dann.math.Hyperpoint;
import com.syncleus.dann.neural.som.brain.ExponentialDecaySomBrain;
import java.util.Map.Entry;
import java.awt.Color;

public class ColorMap1dCallable implements Callable<Color[]>
{
	private int iterations;
	private double learningRate;
	private int width;

	private static Random random = new Random();

	private int progress;

	public ColorMap1dCallable(int iterations, double learningRate, int width)
	{
		this.iterations = iterations;
		this.learningRate = learningRate;
		this.width = width;
	}

	public Color[] call()
	{
		//initialize brain
		ExponentialDecaySomBrain brain = new ExponentialDecaySomBrain(3, 1,getIterations(), getLearningRate());

		//create the output latice
		for(double x = 0; x < getWidth(); x++)
			brain.createOutput(new Hyperpoint(new double[]{x}));

		//run through random training data for all iterations
		for(int iteration = 0; iteration < getIterations(); iteration++)
		{
			this.progress++;
			
			brain.setInput(0, random.nextDouble());
			brain.setInput(1, random.nextDouble());
			brain.setInput(2, random.nextDouble());

			brain.getBestMatchingUnit(true);
		}

		//pull the output weight vectors
		Map<Hyperpoint, double[]> outputWeightVectors = brain.getOutputWeightVectors();

		//construct the color array
		Color[] colorPositions = new Color[getWidth()];
		for(Entry<Hyperpoint, double[]> weightVector : outputWeightVectors.entrySet())
		{
			Hyperpoint currentPoint = weightVector.getKey();
			double[] currentVector = weightVector.getValue();

			//convert the current Vector to a color.
			Color currentColor = new Color((float)currentVector[0], (float)currentVector[1], (float)currentVector[2]);

			//add the current color to the colorPositions
			colorPositions[(int)Math.floor(currentPoint.getCoordinate(1))] = currentColor;
		}

		//return the color positions
		return colorPositions;
	}



	/**
	 * @return the iterations
	 */
	public
	int getIterations()
	{
		return iterations;
	}



	/**
	 * @return the learningRate
	 */
	public
	double getLearningRate()
	{
		return learningRate;
	}



	/**
	 * @return the width
	 */
	public
	int getWidth()
	{
		return width;
	}



	/**
	 * @return the progress
	 */
	public
	int getProgress()
	{
		return progress;
	}
}
