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
package com.syncleus.dann.examples.nci;

import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;

public class TrainRun implements Runnable
{
    private NciBrain brain;
    private BufferedImage trainImage;
	private final static Logger LOGGER = Logger.getLogger(TrainRun.class);
    
    public TrainRun(NciBrain brain, BufferedImage trainImage)
    {
        this.brain = brain;
        this.trainImage = trainImage;
    }
    
    public void run()
    {
		try
		{
			this.brain.setLearning(true);
			this.brain.test(trainImage);
		}
		catch(Exception caught)
		{
			LOGGER.error("Exception was caught", caught);
			throw new RuntimeException("Throwable was caught", caught);
		}
		catch(Error caught)
		{
			LOGGER.error("Error was caught", caught);
			throw new Error("Throwable was caught");
		}
    }
}
