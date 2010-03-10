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
package com.syncleus.core.dann.examples.pathfind;

import com.syncleus.dann.graph.SimpleUndirectedEdge;
import com.syncleus.dann.graph.WeightedBidirectedEdge;

/**
 * Extends SimpleUndirectedEdge to implement the WeightedBidirectedEdge interface (a 'double' weight).
 * @author seh
 * @param <X> type of node that the edge connects
 */
public class SimpleWeightedUndirectedEdge<X> extends SimpleUndirectedEdge<X> implements WeightedBidirectedEdge<X>
{

    private double weight;

    /**
     * Constructs a new undirected edge between leftNode and rightNode with a specific initialWeight.
     * @param leftNode one node of the pair
     * @param rightNode another node of the pair
     * @param initialWeight  the initial weight value associated with this edge
     */
    public SimpleWeightedUndirectedEdge(final X leftNode, final X rightNode, final double initialWeight)
    {
        super(leftNode, rightNode);
        this.weight = initialWeight;
    }

    /**
     * Adjusts the weight value associated with this edge.
     * @param newWeight the new weight value
     */
    public void setWeight(final double newWeight)
    {
        this.weight = newWeight;
    }

    /**
     * Returns the edge's current associated weight value.
     * @return the associated weight value
     */
    @Override
    public double getWeight()
    {
        return weight;
    }

    /** Same as SimpleBidirectedEge.equals(o).
         @param compareToObj object to test equality with
         @return whether compareToObj equals this
     */
    @Override
    public boolean equals(final Object compareToObj)
    {
        return super.equals(compareToObj);
    }

    /** Same as SimpleBidirectedEge.hashCode().
         @return the hashcode of this object
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

}
