package com.syncleus.core.dann.examples.test;

public class GlobalSignal extends Signal
{
    public GlobalSignal()
    {
    }
    
    protected GlobalSignal(GlobalSignal originalSignal)
    {
        super(originalSignal);
    }
    
    public GlobalSignal clone()
    {
        GlobalSignal newSignal = new GlobalSignal(this);
        return newSignal;
    }
}
