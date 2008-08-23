package com.syncleus.core.dann.examples.nci.ui;

import com.syncleus.core.dann.examples.nci.NciBrain;
import java.awt.image.BufferedImage;


public class BrainThread extends Thread
{
    private NciBrain brain = null;
    private boolean busy = true;
    private BufferedImage originalImage = null;
    private BufferedImage finalImage = null;
    private int[] compressedImage = null;
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

        if ((originalImage.getWidth() < width) || (originalImage.getHeight() < height))
            throw new Exception("Image is too small");

        this.busy = true;
        this.originalImage = originalImage;
    }



    public BufferedImage getLastFinalImage()
    {
        return this.finalImage;
    }



    public int[] getLastCompressedImage()
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
                    this.compressedImage = this.brain.compress(this.originalImage);
                    this.finalImage = this.brain.uncompress(this.compressedImage);
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
