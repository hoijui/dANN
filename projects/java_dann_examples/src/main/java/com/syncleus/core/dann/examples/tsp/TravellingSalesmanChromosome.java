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
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class TravellingSalesmanChromosome extends GeneticAlgorithmChromosome implements Cloneable
{
	public TravellingSalesmanChromosome(int cityCount)
	{
		super(cityCount, 10.0);
	}

	public int getCityOrder(int cityIndex)
	{
		if( cityIndex >= this.getGeneCount())
			throw new IllegalArgumentException("cityIndex is out of bounds");

		SortedSet<AbstractValueGene> sortedGenes = this.getSortedGenes();

		AbstractValueGene cityGene = this.getGenes().get(cityIndex);
		int cityOrder = 0;
		for(AbstractValueGene sortedGene : sortedGenes)
		{
			if(sortedGene == cityGene)
				return cityOrder;

			cityOrder++;
		}

		throw new AssertionError("Could not find matching city gene in sorted genes");
	}

	public int[] getCitiesOrder()
	{
		int[] citiesOrder = new int[this.getGeneCount()];
		for(int cityIndex = 0; cityIndex < citiesOrder.length; cityIndex++)
			citiesOrder[cityIndex] = this.getCityOrder(cityIndex);
		return citiesOrder;
	}

	public SortedSet<AbstractValueGene> getSortedGenes()
	{
		TreeSet<AbstractValueGene> sortedGenes = new TreeSet<AbstractValueGene>(new Comparator<AbstractValueGene>()
							{
								public int compare(AbstractValueGene gene1, AbstractValueGene gene2)
								{
									if( gene1.getValue().doubleValue() < gene2.getValue().doubleValue() )
										return -1;
									else if( gene1.getValue().doubleValue() > gene2.getValue().doubleValue() )
										return 1;
									else
										return 0;
								}
							});

		sortedGenes.addAll(this.getGenes());

		return Collections.unmodifiableSortedSet(sortedGenes);
	}

	/**
	 * Creates a new instance that is a copy of this object.
	 *
	 * @return an exact copy of this object.
	 * @since 2.0
	 */
	@Override
	public TravellingSalesmanChromosome clone()
	{
			TravellingSalesmanChromosome copy = (TravellingSalesmanChromosome) super.clone();
//			copy.sortedGenes.clear();
//			copy.sortedGenes.addAll(copy.getGenes());
			return copy;
	}

	/**
	 * This will make a copy of the object and mutate it. The mutation has
	 * a normal distribution multiplied by the deviation. This will be applied
	 * to each gene in the chromosome.
	 *
	 * @param deviation A double indicating how extreme the mutation will be.
	 * The greater the deviation the more drastically the object will mutate.
	 * A deviation of 0 should cause no mutation.
	 * @return A copy of the current object with potential mutations.
	 * @since 2.0
	 */
	public TravellingSalesmanChromosome mutate(double deviation)
	{
		TravellingSalesmanChromosome mutated = (TravellingSalesmanChromosome)super.mutate(deviation);
//		mutated.sortedGenes.clear();
//		mutated.sortedGenes.addAll(mutated.getGenes());
		return mutated;
	}
}
