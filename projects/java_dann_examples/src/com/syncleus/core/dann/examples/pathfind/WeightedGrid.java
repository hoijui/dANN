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

import com.syncleus.dann.graph.*;
import java.util.*;

/**
 * A 2D mesh with individual weights for nodes and edges.  Each node is linked to its four neighbors by weighted undirected edges.
 * @author seh
 */
public class WeightedGrid extends AbstractBidirectedAdjacencyGraph<GridNode, SimpleWeightedUndirectedEdge<GridNode>>
{

    private final GridNode[][] nodes;
    private final Set<GridNode> nodeSet = new HashSet<GridNode>();
    private final Set<SimpleWeightedUndirectedEdge<GridNode>> edges = new HashSet<SimpleWeightedUndirectedEdge<GridNode>>();
    private final Map<GridNode, Set<SimpleWeightedUndirectedEdge<GridNode>>> neighborEdges = new HashMap<GridNode, Set<SimpleWeightedUndirectedEdge<GridNode>>>();
    private final Map<GridNode, Set<GridNode>> neighborNodes = new HashMap<GridNode, Set<GridNode>>();

    /**
     * Initialize the grid with a 2D array of nodeweights specified as double's.
     * @param nodeWeights the 2D array of nodeweights.  the dimensions of the array specify the dimensions of the constructed grid.
     */
    public WeightedGrid(final double[][] nodeWeights)
    {
        this.nodes = new GridNode[nodeWeights.length][nodeWeights[0].length];

        //construct nodes
        for (int y = 0; y < nodeWeights.length; y++)
        {
            for (int x = 0; x < nodeWeights[0].length; x++)
            {
                nodes[y][x] = new GridNode(x, y, nodeWeights[y][x]);
                this.nodeSet.add(nodes[y][x]);
                this.neighborEdges.put(nodes[y][x], new HashSet<SimpleWeightedUndirectedEdge<GridNode>>());
                this.neighborNodes.put(nodes[y][x], new HashSet<GridNode>());
            }
        }

        //connect nodes
        for (int y = 0; y < nodes.length; y++)
        {
            for (int x = 0; x < nodes[0].length; x++)
            {
                //connect to the right
                if (x < nodes[0].length - 1)
                {
                    SimpleWeightedUndirectedEdge<GridNode> newEdge = new SimpleWeightedUndirectedEdge<GridNode>(nodes[y][x], nodes[y][x + 1], 0);
                    this.edges.add(newEdge);
                    this.neighborEdges.get(nodes[y][x]).add(newEdge);
                    this.neighborEdges.get(nodes[y][x + 1]).add(newEdge);
                    this.neighborNodes.get(nodes[y][x]).add(nodes[y][x + 1]);
                    this.neighborNodes.get(nodes[y][x + 1]).add(nodes[y][x]);
                }
                //connect to the bottom
                if (y < nodes.length - 1)
                {
                    SimpleWeightedUndirectedEdge<GridNode> newEdge = new SimpleWeightedUndirectedEdge<GridNode>(nodes[y][x], nodes[y + 1][x], 0);
                    this.edges.add(newEdge);
                    this.neighborEdges.get(nodes[y][x]).add(newEdge);
                    this.neighborEdges.get(nodes[y + 1][x]).add(newEdge);
                    this.neighborNodes.get(nodes[y][x]).add(nodes[y + 1][x]);
                    this.neighborNodes.get(nodes[y + 1][x]).add(nodes[y][x]);
                }
            }
        }
    }

    /**
     * Gets the width of the grid (number of nodes horizontally).
     * @return the width of the grid (number of nodes horizontal)
     */
    public int getWidth()
    {
        return nodes[0].length;
    }

    /**
     * Gets the height of the grid (number of nodes vertically).
     * @return the height of the grid (number of nodes vertical)
     */
    public int getHeight()
    {
        return nodes.length;
    }

    /**
     * Gets the node coresponding to a specific X,Y coordinate.
     * @param x x coordinate of a specific node
     * @param y y coordinate of a specific node
     * @return the node at (x,y) or null if non-existent
     */
    public GridNode getNode(final int x, final int y)
    {
        if ((x >= nodes[0].length) || (y >= nodes.length))
        {
            throw new IllegalArgumentException("coordinates are out of bounds");
        }
        return this.nodes[y][x];
    }

    /**
     * Gets all nodes in the grid.
     * @return the set of all nodes in this grid
     */
    @Override
    public Set<GridNode> getNodes()
    {
        return Collections.unmodifiableSet(this.nodeSet);
    }

    /**
     * Gets all edges in the grid.
     * @return the set of all edges in this grid
     */
    @Override
    public Set<SimpleWeightedUndirectedEdge<GridNode>> getEdges()
    {
        return Collections.unmodifiableSet(this.edges);
    }

    /**
     * Gets all edges surrounding a specific grid node.
     * @param node the node to find the edges surrounding it
     * @return a list of all edges surrounding a specific grid node
     */
    public Set<SimpleWeightedUndirectedEdge<GridNode>> getAdjacentEdges(final GridNode node)
    {
        return Collections.unmodifiableSet(this.neighborEdges.get(node));
    }

    /**
     * Since all edges are traversible, this is the same as getEdges(node).
     * @param node the node to find edges surrounding it
     * @return all traversable edges
     */
    public Set<SimpleWeightedUndirectedEdge<GridNode>> getTraversableEdges(final GridNode node)
    {
        return this.getAdjacentEdges(node);
    }

    /**
     * Since all edges are undirected, all edges are both outgoing and incoming.
     * @param node the node to find edges surrounding it
     * @return all edges surrounding it
     */
    public Set<SimpleWeightedUndirectedEdge<GridNode>> getOutEdges(final GridNode node)
    {
        return this.getAdjacentEdges(node);
    }

    /**
     * Since all edges are undirected, all edges are both outgoing and incoming.
     * @param node the node to find edges surrounding it
     * @return all edges surrounding it
     */
    public Set<SimpleWeightedUndirectedEdge<GridNode>> getInEdges(final GridNode node)
    {
        return this.getAdjacentEdges(node);
    }

    /**
     * Gets the number of edges surrounding a specific node (which are both incoming and outgoing), which should range from 2 to 4.
     * @param node the node to find edges surrounding it
     * @return number of edges surrounding it
     */
    public int getIndegree(final GridNode node)
    {
        return this.getInEdges(node).size();
    }

    /**
     * Gets the number of edges surrounding a specific node (which are both incoming and outgoing), which should range from 2 to 4.
     * @param node the node to find edges surrounding it
     * @return number of edges surrounding it
     */
    public int getOutdegree(final GridNode node)
    {
        return this.getOutEdges(node).size();
    }

    /**
     * Determines whether two nodes are connected by a shared edge.
     * @param leftNode first node
     * @param rightNode second node
     * @return whether they are connected by a shared edge
     */
    public boolean isConnected(final GridNode leftNode, final GridNode rightNode)
    {
        return this.neighborNodes.get(leftNode).contains(rightNode);
    }

    /**
     * Gets all neighbors surrounding a node.
     * @param node to find neighbors surrounding it
     * @return list of nodes that can be traversed into
     */
    public List<GridNode> getAdjacentNodes(final GridNode node)
    {
        return Collections.unmodifiableList(new ArrayList<GridNode>(this.neighborNodes.get(node)));
    }

    /**
     * Gets all neighbors surrounding a node.
     * @param node to find neighbors surrounding it
     * @return list of nodes that can be traversed into
     */
    public List<GridNode> getTraversableNodes(final GridNode node)
    {
        return this.getAdjacentNodes(node);
    }

    /**
     * Sets all nodes and edges to the same specific weight value.
     * @param weight the weight value to set
     */
    public void setAll(final double weight)
    {
        for (GridNode gn : getNodes())
        {
            gn.setWeight(weight);
        }
        for (SimpleWeightedUndirectedEdge wbe : getEdges())
        {
            wbe.setWeight(weight);
        }
    }

    /**
     * Returns the edge between two specified grid locations.
     * @param firstX x coordinate of 1st grid location
     * @param firstY y coordinate of 1st grid location
     * @param secondX x coordinate of 2nd grid location
     * @param secondY y coordinate of 2nd grid location
     * @return the specific edge in the grid between the two points, or null if such edge is non-existent
     */
    public SimpleWeightedUndirectedEdge<GridNode> getEdgeBetween(final int firstX, final int firstY, final int secondX, final int secondY)
    {
        if ((firstX == secondX) && (firstY == secondY))
        {
            //no edge between the same gridnode
            return null;
        }

        final GridNode gn = getNode(firstX, firstY);
        for (SimpleWeightedUndirectedEdge<GridNode> edge : getAdjacentEdges(gn))
        {
            final GridNode left = edge.getLeftNode();
            final GridNode right = edge.getRightNode();
            if ((left.getX() == secondX) && (left.getY() == secondY))
            {
                return edge;
            }

            if ((right.getX() == secondX) && (right.getY() == secondY))
            {
                return edge;
            }
        }
        return null;
    }
}
