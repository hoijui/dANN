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
package com.syncleus.core.dann.examples.tsp;

import com.syncleus.dann.genetics.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import com.syncleus.dann.math.Vector;

public class TravellingSalesmanPopulation extends AbstractGeneticAlgorithmPopulation
{
	private final Vector cities[];
	
	public TravellingSalesmanPopulation(final Vector cities[], final double mutationDeviation, final double crossoverPercentage, final double dieOffPercentage)
	{
		super(mutationDeviation, crossoverPercentage, dieOffPercentage);

		if(cities == null)
			throw new IllegalArgumentException("cities can not be null");
		if(cities.length < 4)
			throw new IllegalArgumentException("cities must have atleast 4 elements");
		
		this.cities = cities.clone();
	}

	public TravellingSalesmanPopulation(final Vector cities[], final double mutationDeviation, final double crossoverPercentage, final double dieOffPercentage, final ThreadPoolExecutor threadExecutor)
	{
		super(mutationDeviation, crossoverPercentage, dieOffPercentage, threadExecutor);

		if(cities == null)
			throw new IllegalArgumentException("cities can not be null");
		if(cities.length < 4)
			throw new IllegalArgumentException("cities must have atleast 4 elements");
		
		this.cities = cities.clone();
	}

	public void initializePopulation(final int populationSize)
	{
		if(populationSize < 4)
			throw new IllegalArgumentException("populationSize must have atleast 4 elements");

		this.addAll(initialChromosomes(cities.length, populationSize));
	}

	protected TravellingSalesmanFitnessFunction packageChromosome(final GeneticAlgorithmChromosome chromosome)
	{
		if(!(chromosome instanceof TravellingSalesmanChromosome))
			throw new IllegalArgumentException("Chromosome must be a TravellingSalesmanChromosome");
		
		return new TravellingSalesmanFitnessFunction((TravellingSalesmanChromosome)chromosome, this.cities);
	}

	private static Set<GeneticAlgorithmChromosome> initialChromosomes(final int cityCount, final int populationSize)
	{
		if(populationSize < 4)
			throw new IllegalArgumentException("populationSize must have atleast 4 elements");
		if(cityCount < 4)
			throw new IllegalArgumentException("cityCount must be atleast 4");

		final HashSet<GeneticAlgorithmChromosome> returnValue = new HashSet<GeneticAlgorithmChromosome>();
		while(returnValue.size() < populationSize)
			returnValue.add(new TravellingSalesmanChromosome(cityCount));
		return returnValue;
	}

	public final TravellingSalesmanChromosome getWinner()
	{
		GeneticAlgorithmChromosome winner = super.getWinner();
		assert(winner instanceof TravellingSalesmanChromosome);
		return (TravellingSalesmanChromosome) winner;
	}

	public Vector[] getCities()
	{
		return cities.clone();
	}
}
