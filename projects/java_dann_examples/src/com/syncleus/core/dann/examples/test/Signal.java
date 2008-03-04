package com.syncleus.core.dann.examples.test;


import com.syncleus.util.UniqueId;

public class Signal implements Comparable<Signal>
{
    private double value = 0.0;
    private UniqueId id = new UniqueId(32);
    
    public Signal()
    {
    }
    
    public double add(double addValue)
    {
        this.value += addValue;
        return this.value;
    }
    
    public double getValue()
    {
        return this.value;
    }
    
    public void setValue(double newValue)
    {
        this.value = newValue;
    }
    
    public UniqueId getId()
    {
        return this.id;
    }
    
    public int compareTo(Signal compareWith)
    {
        return this.getId().toString().compareTo(compareWith.getId().toString());
    }
}
