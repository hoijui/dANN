package com.syncleus.core.dann.examples.nci.ui;
import com.syncleus.core.dann.examples.nci.NciBrain;
import java.awt.image.BufferedImage;



public class TrainRun implements Runnable
{
    private NciBrain brain;
    private BufferedImage trainImage;
    
    public TrainRun(NciBrain brain, BufferedImage trainImage)
    {
        this.brain = brain;
        this.trainImage = trainImage;
    }
    
    public void run()
    {
        this.brain.setLearning(true);
        this.brain.test(trainImage);
    }
}
