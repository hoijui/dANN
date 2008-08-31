package com.syncleus.core.dann.examples.nci.ui;

import com.syncleus.core.dann.examples.nci.NciBrain;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;


public class BrainRunner implements Runnable
{
    private NciBrain brain = null;
    private double compression;
    private int xSize;
    private int ySize;
    private boolean extraConnectivity;
    private BufferedImage[] trainingImages;
    private File[] trainingFiles;
    private BufferedImage sampleImage;
    private volatile File sampleFile;
//    private BufferedImage finalImage;
    private BrainListener listener;
    private static Random random = new Random();
    private volatile boolean shutdown = false;
    private volatile int trainingRemaining = 0;
    private volatile int sampleRemaining;
    private volatile int sampleTotal;



    public BrainRunner(BrainListener listener, File[] trainingFiles, double compression, int xSize, int ySize, boolean extraConnectivity)
    {
        this.listener = listener;
        this.trainingFiles = trainingFiles;
        this.compression = compression;
        this.xSize = xSize;
        this.ySize = ySize;
        this.extraConnectivity = extraConnectivity;
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
                    for (int currentY = 0; currentY < this.sampleImage.getHeight(); currentY += ySize)
                        for (int currentX = 0; currentX < this.sampleImage.getWidth(); currentX += xSize)
                        {
                            int blockWidth = this.sampleImage.getWidth() - currentX < xSize ? this.sampleImage.getWidth() - currentX : xSize;
                            int blockHeight = this.sampleImage.getHeight() - currentY < ySize ? this.sampleImage.getHeight() - currentY : ySize;
                            BufferedImage currentSegment = this.sampleImage.getSubimage(currentX, currentY, blockWidth, blockHeight);

                            SampleRun sampleRun = new SampleRun(this.brain, currentSegment);
                            FutureTask<BufferedImage> futureSampleRun = new FutureTask<BufferedImage>(sampleRun);

                            this.sampleTotal++;

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

                    //int cycles = trainingRemaining;
                    
                    //for (int currentTraining = 0; currentTraining < trainingRemaining; currentTraining++)
                    while(this.trainingRemaining > 0)
                    {
                        if (trainingSegments.remainingCapacity() <= 0)
                        {
                            FutureTask currentTask = trainingSegments.take();
//                            if(currentTask.isDone() == false)
//                                Thread.sleep(100);
                            currentTask.get();
                            this.trainingRemaining--;
                            if (this.trainingRemaining < 0)
                                this.trainingRemaining = 0;
                        }
                        TrainRun trainRun = new TrainRun(this.brain, this.getRandomTrainingBlock(xSize, ySize));
                        FutureTask trainTask = new FutureTask(trainRun, null);
                        
                        
                        trainingSegments.add(trainTask);
                        executor.execute(trainTask);
                    }

                    while (trainingSegments.isEmpty() == false)
                    {
                        FutureTask currentTask = trainingSegments.take();
//                        if(currentTask.isDone() == false)
//                            Thread.sleep(100);
                        currentTask.get();
                        this.trainingRemaining--;
                        if (this.trainingRemaining < 0)
                            this.trainingRemaining = 0;
                    }
                    
                    this.listener.brainTrainingComplete();
                }
                else
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch (Exception e)
                    {
                    }
            }


        }
        catch (Exception e)
        {
            System.out.println("Danger will robinson, Danger: " + e);
            e.printStackTrace();
            return;
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

        int randomX = this.random.nextInt(randomImage.getWidth() - width);
        int randomY = this.random.nextInt(randomImage.getHeight() - height);
        return randomImage.getSubimage(randomX, randomY, width, height);
    }



    private BufferedImage getRandomTrainingImage() throws Exception
    {
        return this.trainingImages[this.random.nextInt(this.trainingImages.length)];
    }
}
