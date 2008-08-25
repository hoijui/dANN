package com.syncleus.core.dann.examples.nci.ui;
import java.awt.image.BufferedImage;



public interface BrainListener
{
    public void brainFinishedBuffering();
    public void brainSampleProcessed(BufferedImage finalImage);
    public void brainTrainingComplete();
}
