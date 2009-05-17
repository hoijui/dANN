package com.syncleus.core.dann.examples.test;

public class LocalSignal extends Signal
{
    public LocalSignal()
    {
    }
    
    protected LocalSignal(LocalSignal originalSignal)
    {
        super(originalSignal);
    }
    
    public LocalSignal clone()
    {
        LocalSignal newSignal = new LocalSignal(this);
        return newSignal;
    }
}
