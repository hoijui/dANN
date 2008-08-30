package com.syncleus.core.dann.examples.nci;

import com.syncleus.dann.*;


public class BackPropagateRun implements Runnable
{
    private ProcessingUnit processor;
    
    public BackPropagateRun(ProcessingUnit processor)
    {
        this.processor = processor;
    }
    
    public void run()
    {
        this.processor.backPropagate();
    }
}
