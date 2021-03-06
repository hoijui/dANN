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
package com.syncleus.dann.math.statistics;

import java.util.*;
import java.util.Map.Entry;
import com.syncleus.dann.math.linear.*;

public class SimpleMarkovChain<S> extends AbstractMarkovChain<S>
{
	private final RealMatrix transitionProbabilityMatrix;
	private final ArrayList<List<S>> rowMapping;
	private final ArrayList<S> columnMapping;
	private final Set<S> states;
	private final int order;
	private final ArrayDeque<S> history;
	private static final double MAXIMUM_ROW_ERROR = 0.00001;
	private static final Random RANDOM = new Random();

	public SimpleMarkovChain(final Map<List<S>, Map<S, Double>> transitionProbabilities, final int order, final Set<S> states)
	{
		this.history = new ArrayDeque<S>(order);
		this.order = order;
		this.states = Collections.unmodifiableSet(states);
		this.rowMapping = new ArrayList<List<S>>();

		final int rows = transitionProbabilities.size();
		final int columns = this.states.size();
		final double[][] matrixValues = new double[rows][columns];

		//this.columnMapping = new ArrayList<S>(this.states);  // <-- didnt get the states in the correct order

        //Generate the column mapping from the first element of each transition probability's entry key (which is a list of states)
        //iterates through column mapping, matching rowheading leading elements
        this.columnMapping = new ArrayList<S>();
        for(final Entry<List<S>, Map<S, Double>> transitionProbability : transitionProbabilities.entrySet())
		{
			final List<S> rowHeader = Collections.unmodifiableList(new ArrayList<S>(transitionProbability.getKey()));
            
            S nextColumn;
            if (rowHeader.isEmpty()) {
                nextColumn = null;
            }
            else {
                nextColumn = rowHeader.get(0);
            }

            if (!columnMapping.contains(nextColumn)) {
                columnMapping.add(nextColumn);
            }
        }


		//iterate through all the new rows
		int row = 0;
		for(final Entry<List<S>, Map<S, Double>> transitionProbability : transitionProbabilities.entrySet())
		{
			final List<S> rowHeader = Collections.unmodifiableList(new ArrayList<S>(transitionProbability.getKey()));
			final Map<S, Double> rowTransition = Collections.unmodifiableMap(new LinkedHashMap<S, Double>(transitionProbability.getValue()));

			assert !rowMapping.contains(rowHeader);

			this.rowMapping.add(rowHeader);

			double rowSum = 0.0;
			for(final Entry<S, Double> stateTransition : rowTransition.entrySet())
			{
				final int column = this.columnMapping.indexOf(stateTransition.getKey());
				matrixValues[row][column] = stateTransition.getValue();
				rowSum += matrixValues[row][column];
			}

			if( Math.abs(rowSum - 1.0) > MAXIMUM_ROW_ERROR )
				throw new IllegalArgumentException("One of the rows does not sum to 1");

			row++;
		}

		this.transitionProbabilityMatrix = new SimpleRealMatrix(matrixValues);
	}

	public SimpleMarkovChain(final Map<S, Map<S, Double>> transitionProbabilities, final Set<S> states)
	{
		this(packTransitions(transitionProbabilities), 1, states);
	}

	private static <S> Map<List<S>, Map<S, Double>> packTransitions(Map<S, Map<S, Double>> transitions)
	{
		Map<List<S>, Map<S, Double>> pack = new LinkedHashMap<List<S>, Map<S, Double>>(transitions.size());
		for(Map.Entry<S, Map<S, Double>> transitionEntry : transitions.entrySet())
			if(transitionEntry.getKey() == null)
				pack.put(Collections.<S>emptyList(), transitionEntry.getValue());
			else
				pack.put(Collections.singletonList(transitionEntry.getKey()), transitionEntry.getValue());
		return pack;
	}

	public List<S> getTransitionProbabilityColumns()
	{
		return Collections.unmodifiableList(this.columnMapping);
	}

	public List<List<S>> getTransitionProbabilityRows()
	{
		return Collections.unmodifiableList(this.rowMapping);
	}

	public RealMatrix getTransitionProbabilityMatrix()
	{
		return this.transitionProbabilityMatrix;
	}

	public int getOrder()
	{
		return this.order;
	}

	public Set<S> getStates()
	{
		return this.states;
	}

	public void transition(final S nextState)
	{
		this.history.add(nextState);
		while( this.history.size() > this.order )
			this.history.poll();
	}

	public S generateTransition(final boolean step)
	{
		final List<S> currentState = this.getStateHistory();
		final int row = this.rowMapping.indexOf(currentState);

		final double randomValue = RANDOM.nextDouble();
		double probabilityOffset = 0.0;
		S nextStep = this.columnMapping.get(this.columnMapping.size() - 1);
		for(int columnIndex = 0; columnIndex < this.transitionProbabilityMatrix.getWidth(); columnIndex++)
		{
			final double transitionProbability = this.transitionProbabilityMatrix.getDouble(row, columnIndex);
			if( randomValue < transitionProbability + probabilityOffset )
			{
				nextStep = this.columnMapping.get(columnIndex);
				break;
			}
			probabilityOffset += transitionProbability;
		}

		if( step )
			this.transition(nextStep);
		return nextStep;
	}

	public List<S> getStateHistory()
	{
		return Collections.unmodifiableList(new ArrayList<S>(this.history));
	}

	public void reset()
	{
		this.history.clear();
	}

	public Map<S, Double> getProbability(final int steps)
	{
		final List<S> currentState = this.getStateHistory();
		if( (currentState == null) || (currentState.size() <= 0) )
			throw new IllegalStateException("probability can not be calculated without atleast one transition");
		RealMatrix futureMatrix = this.transitionProbabilityMatrix;
		for(int currentStep = 0; currentStep < steps - 1; currentStep++)
			futureMatrix = futureMatrix.multiply(this.transitionProbabilityMatrix);
		final int row = this.rowMapping.indexOf(currentState);
		final Map<S, Double> probability = new LinkedHashMap<S, Double>();
		for(int columnIndex = 0; columnIndex < this.transitionProbabilityMatrix.getWidth(); columnIndex++)
		{
			final double transitionProbability = futureMatrix.getDouble(row, columnIndex);
			final S transitionState = this.columnMapping.get(columnIndex);
			probability.put(transitionState, transitionProbability);
		}
		return Collections.unmodifiableMap(probability);
	}

	public Map<S, Double> getSteadyStateProbability()
	{
		final RealMatrix steadyStateMatrix = this.transitionProbabilityMatrix.subtract(SimpleRealMatrix.identity(this.transitionProbabilityMatrix.getHeight(), this.transitionProbabilityMatrix.getWidth())).flip();
		final double[][] simultaniousValues = new double[steadyStateMatrix.getHeight()  + 1][steadyStateMatrix.getWidth()];
		for(int rowIndex = 0; rowIndex < simultaniousValues.length; rowIndex++)
			for(int columnIndex = 0; columnIndex < simultaniousValues[0].length; columnIndex++)
			{
				if( rowIndex >= simultaniousValues.length - 1 )
					simultaniousValues[rowIndex][columnIndex] = 1.0;
				else
					simultaniousValues[rowIndex][columnIndex] = steadyStateMatrix.get(rowIndex, columnIndex).doubleValue();
			}
		final RealMatrix simultaniousMatrix = new SimpleRealMatrix(simultaniousValues);

        //System.out.println("steadyState matrix:\n" + steadyStateMatrix.toString());
        //System.out.println("simultaneous matrix:\n" + simultaniousMatrix.toString());

		final double[][] solutionValues = new double[simultaniousValues.length][1];
		solutionValues[simultaniousValues.length - 1][0] = 1.0;
		final RealMatrix solutionMatrix = new SimpleRealMatrix(solutionValues);

        //System.out.println("solution matrix:\n" + solutionMatrix.toString());

		final RealMatrix simultaniousSolved = simultaniousMatrix.solve(solutionMatrix);

        //System.out.println("simultaneous solved:\n" + simultaniousSolved.toString());

		final Map<S, Double> stateProbabilities = new LinkedHashMap<S, Double>();
		for(int stateIndex = 0; stateIndex < simultaniousSolved.getHeight(); stateIndex++)
		{
			final S currentState = this.columnMapping.get(stateIndex);
			final double currentProbability = simultaniousSolved.get(stateIndex, 0).doubleValue();
			stateProbabilities.put(currentState, currentProbability);
		}
        
        //System.out.println("xstate probabilities:\n" + stateProbabilities);
        //System.out.println("---");

		return Collections.unmodifiableMap(stateProbabilities);
	}
}
