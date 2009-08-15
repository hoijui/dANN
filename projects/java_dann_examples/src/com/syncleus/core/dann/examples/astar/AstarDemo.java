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
package com.syncleus.core.dann.examples.astar;

import com.syncleus.dann.graph.pathfinding.astar.*;
import java.awt.Point;

public class AstarDemo
{
	public static void main(String[] args)
	{
		NodeMap2D map = new NodeMap2D(new Point(0, 0), new Point(9, 0));
		Node[] path = map.getPath();
		for( Node step : path )
		{
			System.out.println("step: " + map.getCoords(step));
		}
		System.out.println("done");
	}
}
