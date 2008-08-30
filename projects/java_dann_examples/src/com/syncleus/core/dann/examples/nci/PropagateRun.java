package com.syncleus.core.dann.examples.nci;

import com.syncleus.dann.*;


public class PropagateRun implements Runnable
{
    private ProcessingUnit processor;
    
    public PropagateRun(ProcessingUnit processor)
    {
        this.processor = processor;
    }
    
    public void run()
    {
        this.processor.propagate();
    }
}
