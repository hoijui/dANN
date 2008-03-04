package com.syncleus.core.dann.examples.test;


public class WaveDimension
{
    private String dimension;
    private WaveMultidimensionalMathFunction wave;



    public WaveDimension(String dimension, WaveMultidimensionalMathFunction wave)
    {
        this.dimension = dimension;
        this.wave = wave;
    }



    public String getDimension()
    {
        return dimension;
    }



    public WaveMultidimensionalMathFunction getWave()
    {
        return wave;
    }
}
