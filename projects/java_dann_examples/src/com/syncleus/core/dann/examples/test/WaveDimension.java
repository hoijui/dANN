package com.syncleus.core.dann.examples.test;


public class WaveDimension
{
    private String dimension;
    private DistributedFormedWaveMathFunction wave;



    public WaveDimension(String dimension, DistributedFormedWaveMathFunction wave)
    {
        this.dimension = dimension;
        this.wave = wave;
    }



    public String getDimension()
    {
        return dimension;
    }



    public DistributedFormedWaveMathFunction getWave()
    {
        return wave;
    }
}
