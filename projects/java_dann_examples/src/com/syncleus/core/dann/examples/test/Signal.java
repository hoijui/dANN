/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                    *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by syncleus at http://www.syncleus.com.  *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact syncleus at the information below if you cannot find   *
 *  a license:                                                                 *
 *                                                                             *
 *  Syncleus                                                                   *
 *  1116 McClellan St.                                                         *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.core.dann.examples.test;


import com.syncleus.util.UniqueId;

public abstract class Signal implements Comparable<Signal>
{
    private double value = 0.0;
    private UniqueId id = new UniqueId(32);
    
    public Signal()
    {
    }
    
    protected Signal(Signal originalSignal)
    {
        value = originalSignal.value;
        id = originalSignal.id;
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
        return this.getId().compareTo(compareWith.getId());
    }
    
    abstract public Signal clone();
}
