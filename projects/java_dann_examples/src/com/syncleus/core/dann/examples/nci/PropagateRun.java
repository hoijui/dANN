package com.syncleus.core.dann.examples.nci;

import com.syncleus.dann.*;


public class PropagateRun implements Runnable
{
    private NetworkNode processor;
    
    public PropagateRun(NetworkNode processor)
    {
        this.processor = processor;
    }
    
    public void run()
    {
        this.processor.propagate();
    }
}
