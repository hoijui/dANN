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
package com.syncleus.tests.dann.genetics;

import java.util.*;
import com.syncleus.dann.genetics.*;
import org.junit.*;

public class TestGeneticCube
{
	private class VolumeAreaCubeFitness extends AbstractGeneticAlgorithmFitnessFunction
	{
		private static final double IDEAL_AREA = 2200d;
		private static final double IDEAL_VOLUME = 6000d;
		private boolean errorProcessed = false;
		private double error;

		public VolumeAreaCubeFitness(final GeneticAlgorithmChromosome chromosome)
		{
			super(chromosome);
			if( chromosome.getGenes().size() < 3 )
				throw new IllegalArgumentException("Chromosome must have atleast 3 genes");
		}

		public void process()
		{
			final List<AbstractValueGene> genes = this.getChromosome().getGenes();
			final double side1 = genes.get(0).expressionActivity();
			final double side2 = genes.get(1).expressionActivity();
			final double side3 = genes.get(2).expressionActivity();
			final double volume = side1 * side2 * side3;
			final double area = (side1 * side2 * 2d) + (side1 * side3 * 2d) + (side2 * side3 * 2d);
			final double volumeError = Math.abs(IDEAL_VOLUME - volume);
			final double areaError = Math.abs(IDEAL_AREA - area);
			this.error = volumeError + areaError;
			this.errorProcessed = true;
		}

		public double getError()
		{
			if( !this.errorProcessed )
				this.process();
			return this.error;
		}

		public int compareTo(final AbstractGeneticAlgorithmFitnessFunction baseCompareWith)
		{
			if( !(baseCompareWith instanceof VolumeAreaCubeFitness) )
				throw new IllegalArgumentException("Can only compare with VolumeAreaCubeFitness");
			final VolumeAreaCubeFitness compareWith = (VolumeAreaCubeFitness) baseCompareWith;
			if( this.getError() < compareWith.getError() )
				return 1;
			else if( this.getError() > compareWith.getError() )
				return -1;
			else
				return 0;
		}
	}

	private class VolumeAreaCubePopulation extends AbstractGeneticAlgorithmPopulation
	{
		public VolumeAreaCubePopulation(final Set<GeneticAlgorithmChromosome> initialChromosomes)
		{
			super(0.25d, 0.75d, 0.95d);
			this.addAll(initialChromosomes);
		}

		protected AbstractGeneticAlgorithmFitnessFunction packageChromosome(final GeneticAlgorithmChromosome chromosome)
		{
			return new VolumeAreaCubeFitness(chromosome);
		}
	}

	@Test
	public void testVolumeArea()
	{
		final HashSet<GeneticAlgorithmChromosome> cubeChromosomes = new HashSet<GeneticAlgorithmChromosome>();
		while( cubeChromosomes.size() < 100 )
		{
			cubeChromosomes.add(new GeneticAlgorithmChromosome(3, 10d));
		}

		final VolumeAreaCubePopulation population = new VolumeAreaCubePopulation(cubeChromosomes);
		VolumeAreaCubeFitness fitness = new VolumeAreaCubeFitness(population.getWinner());
		while( (population.getGenerations() < 10000) && (fitness.getError() > 0.5d) )
		{
			population.nextGeneration();
			fitness = new VolumeAreaCubeFitness(population.getWinner());
		}

		Assert.assertTrue("Volume/Area Cube failed (error was too great)" + fitness.getError(), fitness.getError() < 0.5d);
	}
}
