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
package com.syncleus.core.dann.examples.nci;

import com.syncleus.dann.graph.drawing.hyperassociativemap.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.concurrent.*;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;

public class BrainRunner implements Runnable
{
    private NciBrain brain;
    private HyperassociativeMap brainMap;
    private double compression;
    private int xSize;
    private int ySize;
    private boolean extraConnectivity;
    private BufferedImage[] trainingImages;
    private File[] trainingFiles;
    private BufferedImage sampleImage;
    private volatile File sampleFile;
    private BrainListener listener;
    private final static Random RANDOM = new Random();
    private volatile boolean shutdown = false;
    private volatile int trainingRemaining = 0;
    private volatile int sampleRemaining;
    private volatile int sampleTotal;
	private final static Logger LOGGER = Logger.getLogger(BrainRunner.class);



    public BrainRunner(BrainListener listener, File[] trainingFiles, double compression, int xSize, int ySize, boolean extraConnectivity)
    {
        this.listener = listener;
        this.trainingFiles = (File[]) trainingFiles.clone();
        this.compression = compression;
        this.xSize = xSize;
        this.ySize = ySize;
        this.extraConnectivity = extraConnectivity;
    }
    
    
    public HyperassociativeMap getBrainMap()
    {
        return this.brainMap;
    }
    
    
    
    public double getAverageAbsoluteWeights()
    {
        return this.brain.getAverageAbsoluteWeight();
    }
    
    public double getAverageWeights()
    {
        return this.brain.getAverageWeight();
    }



    public int getSampleProgress()
    {
        if(sampleTotal == 0)
            return 100;
        return ((sampleTotal - sampleRemaining) * 100) / sampleTotal;
    }



    public void setSampleImage(File sampleFile)
    {
        this.sampleFile = sampleFile;
    }



    private void setTrainingImages(File[] trainingFiles)
    {
        this.trainingFiles = trainingFiles;
        try
        {
            this.trainingImages = new BufferedImage[this.trainingFiles.length];
            for (int trainingFilesIndex = 0; trainingFilesIndex < trainingFiles.length; trainingFilesIndex++)
                this.trainingImages[trainingFilesIndex] = ImageIO.read(trainingFiles[trainingFilesIndex]);
        }
        catch (Exception e)
        {
            System.out.println("Danger will robinson, Danger: " + e);
            e.printStackTrace();
            return;
        }
    }



    public void setTrainingCycles(int cycles)
    {
        this.trainingRemaining = cycles;
    }



    public int getTrainingCycles()
    {
        return this.trainingRemaining;
    }



    public void shutdown()
    {
        this.shutdown = true;
    }
    
    public void stop()
    {
        this.trainingRemaining = 0;
        this.sampleFile = null;
    }



    public void run()
    {
        ExecutorService executor = null;
        try
        {
            executor = Executors.newFixedThreadPool(1);

            this.brain = new NciBrain(this.compression, this.xSize, this.ySize, this.extraConnectivity);
            this.brainMap = new HyperassociativeMap(brain, 3);
            this.setTrainingImages(trainingFiles);
            


            this.listener.brainFinishedBuffering();
            while (this.shutdown == false)
            {
                  
                File sampleFile = this.sampleFile;
                if (this.sampleFile != null)
                {
                    this.brain.setLearning(false);
                    
                    this.sampleImage = ImageIO.read(sampleFile);

                    ArrayBlockingQueue<FutureTask<BufferedImage>> processingSampleSegments = new ArrayBlockingQueue<FutureTask<BufferedImage>>(10000, true);

                    this.sampleTotal = 0;
                    stopProcessing:
						for (int currentY = 0; currentY < this.sampleImage.getHeight(); currentY += ySize)
							for (int currentX = 0; currentX < this.sampleImage.getWidth(); currentX += xSize)
							{
								int blockWidth = this.sampleImage.getWidth() - currentX < xSize ? this.sampleImage.getWidth() - currentX : xSize;
								int blockHeight = this.sampleImage.getHeight() - currentY < ySize ? this.sampleImage.getHeight() - currentY : ySize;
								BufferedImage currentSegment = this.sampleImage.getSubimage(currentX, currentY, blockWidth, blockHeight);

								SampleRun sampleRun = new SampleRun(this.brain, currentSegment);
								FutureTask<BufferedImage> futureSampleRun = new FutureTask<BufferedImage>(sampleRun);

								this.sampleTotal++;

								if( processingSampleSegments.remainingCapacity() <= 0)
								{
									System.out.println("The original image you selected is too large, aborting processing");
									break stopProcessing;
								}

								processingSampleSegments.add(futureSampleRun);
								executor.execute(futureSampleRun);
							}

                    this.sampleRemaining = this.sampleTotal;

                    BufferedImage finalImage = new BufferedImage(this.sampleImage.getWidth(), this.sampleImage.getHeight(), BufferedImage.TYPE_INT_RGB);

                    this.sampleImage = null;
                    this.sampleFile = null;

                    int currentX = 0;
                    int currentY = 0;
                    while (processingSampleSegments.peek() != null)
                    {
                        FutureTask<BufferedImage> nextSegment = processingSampleSegments.take();

                        BufferedImage currentSegment = nextSegment.get();

                        int writeWidth = (currentSegment.getWidth() < (finalImage.getWidth() - currentX) ? currentSegment.getWidth() : finalImage.getWidth() - currentX);
                        int writeHeight = (currentSegment.getHeight() < (finalImage.getHeight() - currentY) ? currentSegment.getHeight() : finalImage.getHeight() - currentY);
                        int[] chunkArray = new int[writeHeight * writeWidth];
                        currentSegment.getRGB(0, 0, writeWidth, writeHeight, chunkArray, 0, writeWidth);
                        finalImage.setRGB(currentX, currentY, writeWidth, writeHeight, chunkArray, 0, writeWidth);

                        this.sampleRemaining--;

                        if (currentX + writeWidth >= finalImage.getWidth())
                        {
                            currentX = 0;
                            if (currentY + writeHeight >= finalImage.getHeight())
                                currentY = 0;
                            else
                                currentY += writeHeight;
                        }
                        else
                            currentX += writeWidth;
                    }

                    this.listener.brainSampleProcessed(finalImage);
                }
                else if (this.trainingRemaining > 0)
                {
                    this.brain.setLearning(true);
                    
                    ArrayBlockingQueue<FutureTask> trainingSegments = new ArrayBlockingQueue<FutureTask>(50, true);

                    while(this.trainingRemaining > 0)
                    {
                        if (trainingSegments.remainingCapacity() <= 0)
                        {
                            FutureTask currentTask = trainingSegments.take();
                            currentTask.get();
                            this.trainingRemaining--;
                            if (this.trainingRemaining < 0)
                                this.trainingRemaining = 0;
                        }
                        TrainRun trainRun = new TrainRun(this.brain, this.getRandomTrainingBlock(xSize, ySize));
                        FutureTask<Void> trainTask = new FutureTask<Void>(trainRun, null);
                        
                        
                        trainingSegments.add(trainTask);
                        executor.execute(trainTask);
                    }

                    while (trainingSegments.isEmpty() == false)
                    {
                        FutureTask currentTask = trainingSegments.take();
                        currentTask.get();
                        this.trainingRemaining--;
                        if (this.trainingRemaining < 0)
                            this.trainingRemaining = 0;
                    }
                    
                    this.listener.brainTrainingComplete();
                }
                else if(this.getTrainingCycles() <= 0)
                    this.brainMap.align(); 
            }


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
        finally
        {
            if (executor != null)
                executor.shutdown();
        }
    }



    private BufferedImage getRandomTrainingBlock(int width, int height) throws Exception
    {
        BufferedImage randomImage = this.getRandomTrainingImage();

        int randomX = this.RANDOM.nextInt(randomImage.getWidth() - width);
        int randomY = this.RANDOM.nextInt(randomImage.getHeight() - height);
        return randomImage.getSubimage(randomX, randomY, width, height);
    }



    private BufferedImage getRandomTrainingImage() throws Exception
    {
        return this.trainingImages[RANDOM.nextInt(this.trainingImages.length)];
    }
}
