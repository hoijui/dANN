package com.syncleus.core.dann.examples.nci.ui;

import com.syncleus.core.dann.examples.nci.NciBrain;
import java.awt.image.BufferedImage;


public class BrainThread extends Thread
{
    private volatile NciBrain brain = null;
    private volatile boolean busy = true;
    private volatile BufferedImage originalImage = null;
    private volatile BufferedImage finalImage = null;
    private volatile byte[] compressedImage = null;
    private int width = 0;
    private int height = 0;
    private double compression;
    private boolean extraConnectivity;



    public BrainThread(double compression, int xSize, int ySize, boolean extraConnectivity)
    {
        width = xSize;
        height = ySize;
        this.compression = compression;
        this.extraConnectivity = extraConnectivity;
    }



    public void setLearning(boolean active) throws BusyException
    {
        if (this.isBusy())
            throw new BusyException("Still processing last image");

        synchronized (this.brain)
        {
            this.brain.setLearning(active);
        }
    }



    public void processImage(BufferedImage originalImage) throws Exception
    {
        if (this.isBusy())
            throw new BusyException("Still processing last image");

        if (originalImage == null)
            return;

//        if ((originalImage.getWidth() < width) || (originalImage.getHeight() < height))
//            throw new Exception("Image is too small");

        this.busy = true;
        this.originalImage = originalImage;
    }



    public BufferedImage getLastFinalImage()
    {
        return this.finalImage;
    }



    public byte[] getLastCompressedImage()
    {
        return this.compressedImage;
    }



    public void run()
    {
        try
        {
            brain = new NciBrain(compression, width, height, extraConnectivity);
            this.busy = false;

            while (true)
            {
                while (originalImage == null)
                {
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch (Exception caughtException)
                    {
                    }
                }

                this.busy = true;

                synchronized (this.brain)
                {
                    /*
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println("about to compress...");
//                    Thread.sleep(1000);
                    this.compressedImage = this.brain.compress(this.originalImage);
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println("about to decompress...");
//                    Thread.sleep(1000);
                    this.finalImage = this.brain.uncompress(this.compressedImage);
                     */
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println("about to test...");
                    this.finalImage = this.brain.test(this.originalImage);
                    this.originalImage = null;
                }

                this.busy = false;
            }
        }
        catch (Throwable e)
        {
            System.out.println("Exception! : " + e);
            e.printStackTrace();
        }
    }



    public boolean isBusy()
    {
        return this.busy;
    }
}
