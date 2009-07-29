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

import java.util.Map;
import java.util.Random;
import com.syncleus.dann.math.Hyperpoint;
import com.syncleus.dann.neural.som.brain.ExponentialDecaySomBrain;
import java.util.Map.Entry;
import java.awt.Color;

public class ColorMapper
{
	private static Random random = new Random();

	public static Color[] mapColor1d(int iterations, double learningRate, int width)
	{
		//initialize brain
		ExponentialDecaySomBrain brain = new ExponentialDecaySomBrain(3, 1, iterations, learningRate);

		//create the output latice
		for(double x = 0; x < width; x++)
			brain.createOutput(new Hyperpoint(new double[]{x}));

		//run through random training data for all iterations
		for(int iteration = 0; iteration < iterations; iteration++)
		{
			brain.setInput(0, random.nextDouble());
			brain.setInput(1, random.nextDouble());
			brain.setInput(2, random.nextDouble());

			brain.getBestMatchingUnit(true);
		}

		//pull the output weight vectors
		Map<Hyperpoint, double[]> outputWeightVectors = brain.getOutputWeightVectors();

		//construct the color array
		Color[] colorPositions = new Color[width];
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

	public static Color[][] mapColor2d(int iterations, double learningRate, int width, int height)
	{
		//initialize brain
		ExponentialDecaySomBrain brain = new ExponentialDecaySomBrain(3, 2, iterations, learningRate);

		//create the output latice
		for(double x = 0; x < width; x++)
			for(double y = 0; y < height; y++)
				brain.createOutput(new Hyperpoint(new double[]{x, y}));

		//run through random training data
		for(int iteration = 0; iteration < iterations; iteration++)
		{
			brain.setInput(0, random.nextDouble());
			brain.setInput(1, random.nextDouble());
			brain.setInput(2, random.nextDouble());

			brain.getBestMatchingUnit(true);
		}

		//pull the output weight vectors
		Map<Hyperpoint, double[]> outputWeightVectors = brain.getOutputWeightVectors();

		//construct the color array
		Color[][] colorPositions = new Color[width][height];
		for(Entry<Hyperpoint, double[]> weightVector : outputWeightVectors.entrySet())
		{
			Hyperpoint currentPoint = weightVector.getKey();
			double[] currentVector = weightVector.getValue();

			//convert the current Vector to a color.
			Color currentColor = new Color((float)currentVector[0], (float)currentVector[1], (float)currentVector[2]);

			//add the current color to the colorPositions
			colorPositions[(int)Math.floor(currentPoint.getCoordinate(1))][(int)Math.floor(currentPoint.getCoordinate(2))] = currentColor;
		}

		//return the color positions
		return colorPositions;
	}
}
