package com.syncleus.core.dann.examples.nci.ui;
import com.syncleus.core.dann.examples.nci.NciBrain;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;



public class SampleRun implements Callable<BufferedImage>
{
    private NciBrain brain;
    private BufferedImage sampleImage;
    
    
    public SampleRun(NciBrain brain, BufferedImage sampleImage)
    {
        this.brain = brain;
        this.sampleImage = sampleImage;
    }
    
    public BufferedImage call()
    {
        this.brain.setLearning(false);
        return this.brain.test(sampleImage);
    }
}
