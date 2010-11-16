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

import com.syncleus.dann.graph.HyperEdge;
import com.syncleus.dann.graph.HyperGraph;
import edu.uci.ics.jung.algorithms.layout.HypergraphLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.BasicHypergraphRenderer;
import java.awt.Dimension;

/**
 *
 * @author seh
 */
public class HypergraphPanel<N,E extends HyperEdge<N>> extends GraphPanel<N,E> {

    public HypergraphPanel(HyperGraph<N,E> g, int w, int h) {
        super(new JungGraph(g), w, h);
    }

    protected void initLayout(int w, int h) {
		//layout = new HypergraphLayout(graph, FRLayout.class);
		layout = new HypergraphLayout(graph, SpringLayout.class);

		vis = new VisualizationViewer(layout, new Dimension(w, h));

		// replace standard renderer with hypergraph renderer
		vis.setRenderer(new BasicHypergraphRenderer());
    }

}
