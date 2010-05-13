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
package com.syncleus.dann.graph.jung;

import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class JungGraph<N, E extends DirectedEdge<N>> implements edu.uci.ics.jung.graph.Graph<N,E> {
    
    final Graph<N, E> dannGraph;

    public JungGraph(Graph<N, E> dannGraph) {
        super();
        this.dannGraph = dannGraph;
    }

    public Collection<E> getIncidentEdges(N v) {
        return dannGraph.getAdjacentEdges(v);
    }

    public Collection<N> getIncidentVertices(E e) {
        List<N> ll = new LinkedList();
        ll.add(e.getSourceNode());
        ll.add(e.getDestinationNode());
        return ll;
    }

    public E findEdge(N v, N v1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<E> findEdgeSet(N v, N v1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<E> getInEdges(N v) {
        List<E> inEdges = new LinkedList();
        Set<E> edges = dannGraph.getAdjacentEdges(v);
        for (E e : edges) {
            if (e.getDestinationNode() == v)
                inEdges.add(e);
        }
        return inEdges;
    }

    public Collection<N> getPredecessors(N v) {
        Collection<E> inEdges = getInEdges(v);
        Set<N> p = new HashSet<N>(inEdges.size());
        for (E e : inEdges) {
            p.add(e.getSourceNode());
        }
        return p;
    }

    public Collection<N> getSuccessors(N v) {
        Collection<E> outEdges = getOutEdges(v);
        Set<N> p = new HashSet<N>(outEdges.size());
        for (E e : outEdges) {
            p.add(e.getDestinationNode());
        }
        return p;
    }

    public boolean isPredecessor(N a, N b) {
        return getPredecessors(a).contains(b);
    }

    public boolean isSuccessor(N a, N b) {
        return getSuccessors(a).contains(b);
    }

    public int getPredecessorCount(N v) {
        return getPredecessors(v).size();
    }

    public int getSuccessorCount(N v) {
        return getSuccessors(v).size();
    }

    public Collection<E> getOutEdges(N v) {
        List<E> outEdges = new LinkedList();
        Set<E> edges = dannGraph.getAdjacentEdges(v);
        for (E e : edges) {
            if (e.getSourceNode() == v)
                outEdges.add(e);
        }
        return outEdges;
    }

    public int inDegree(N v) {
        //TODO this may be sped up by not needing to create a list in getInEdges
        return getInEdges(v).size();
    }

    public int outDegree(N v) {
        //TODO this may be sped up by not needing to create a list in getOutEdges
        return getOutEdges(v).size();
    }

    public N getSource(E e) {
        return e.getSourceNode();
    }

    public N getDest(E e) {
        return e.getDestinationNode();
    }

    public boolean isSource(N v, E e) {
        return e.getSourceNode() == v;
    }

    public boolean isDest(N v, E e) {
        return e.getDestinationNode() == v;
    }

    public Pair<N> getEndpoints(E e) {
        return new Pair<N>(e.getSourceNode(), e.getDestinationNode());
    }

    public N getOpposite(N v, E e) {
        if (e.getSourceNode() == v) return e.getDestinationNode();
        return e.getSourceNode();
    }

    public Collection<E> getEdges() {
        return dannGraph.getEdges();
    }

    public Collection<N> getVertices() {
        return dannGraph.getNodes();
    }

    public boolean containsVertex(N v) {
        return getVertices().contains(v);
    }

    public boolean containsEdge(E e) {
        return getEdges().contains(e);
    }

    public int getEdgeCount() {
        return getEdges().size();
    }

    public int getVertexCount() {
        return getVertices().size();
    }

    public Collection<N> getNeighbors(N v) {
        return dannGraph.getAdjacentNodes(v);
    }


    public boolean isNeighbor(N a, N b) {
        return dannGraph.getAdjacentNodes(a).contains(b);
    }

    public boolean isIncident(N v, E e) {
        return dannGraph.getAdjacentEdges(v).contains(e);
    }

    public int degree(N v) {
        return dannGraph.getDegree(v);
    }

    public int getNeighborCount(N v) {
        return getNeighbors(v).size();
    }

    public int getIncidentCount(E e) {
        return getIncidentVertices(e).size();
    }

    public EdgeType getEdgeType(E e) {
        return EdgeType.DIRECTED;
    }

    public Collection<E> getEdges(EdgeType et) {
        if (et == EdgeType.DIRECTED)
            return getEdges();
        return new LinkedList<E>();
    }

    public int getEdgeCount(EdgeType et) {
        return getEdges(et).size();
    }


    public boolean addEdge(E e, N v, N v1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addEdge(E e, N v, N v1, EdgeType et) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addVertex(N v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addEdge(E e, Collection<? extends N> clctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addEdge(E e, Collection<? extends N> clctn, EdgeType et) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeVertex(N v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeEdge(E e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
