package com.syncleus.core.dann.examples.nci;

import com.syncleus.dann.*;


public class BackPropagateRun implements Runnable
{
    private NetworkNode processor;
    
    public BackPropagateRun(NetworkNode processor)
    {
        this.processor = processor;
    }
    
    public void run()
    {
        this.processor.backPropagate();
    }
}
