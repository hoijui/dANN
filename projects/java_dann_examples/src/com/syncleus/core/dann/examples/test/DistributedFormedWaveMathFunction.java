package com.syncleus.core.dann.examples.test;
import java.security.InvalidParameterException;

public class DistributedFormedWaveMathFunction extends FormedWaveMathFunction
{
    private boolean constantMode = false;
    private double constantValue;
    
    public DistributedFormedWaveMathFunction(double constantValue)
    {
        this();
        
        this.constantMode = true;
        this.constantValue = constantValue;
    }
    
    public DistributedFormedWaveMathFunction()
    {
        super(new String[]{"center", "distribution"});
        this.setDistribution(1.0);
    }



    public DistributedFormedWaveMathFunction(String[] additionalParameters)
    {
        super(combineLabels(new String[]{"center", "distribution"}, additionalParameters));
        this.setDistribution(1.0);
    }



    public void setCenter(double center)
    {
        this.setParameter(this.getParameterNameIndex("center"), center);
    }



    public double getCenter()
    {
        return this.getParameter(this.getParameterNameIndex("center"));
    }



    public void setDistribution(double distribution)
    {
        if(distribution == 0.0)
            throw new InvalidParameterException("distribution can't be 0");
        
        this.setParameter(this.getParameterNameIndex("distribution"), distribution);
    }



    public double getDistribution()
    {
        return this.getParameter(this.getParameterNameIndex("distribution"));
    }



    private double calculateDistribution()
    {
        return (1 / (this.getDistribution() * Math.sqrt(2 * Math.PI))) * Math.exp(-1 * (Math.pow((this.getX() - this.getCenter()), 2)) / (2 * Math.pow(this.getDistribution(), 2)));
    //return Math.pow(1/Math.cosh(this.getX() - this.getCenter()), this.getDistribution());
    }



    public double calculate()
    {
        if( this.constantMode )
            return this.constantValue;
        
        return super.calculate() * this.calculateDistribution();
    }



    public DistributedFormedWaveMathFunction clone()
    {
        DistributedFormedWaveMathFunction copy = new DistributedFormedWaveMathFunction();
        copy.setX(this.getX());
        copy.setFrequency(this.getFrequency());
        copy.setPhase(this.getPhase());
        copy.setAmplitude(this.getAmplitude());
        copy.setForm(this.getForm());
        copy.setCenter(this.getCenter());
        copy.setDistribution(this.getDistribution());
        return copy;
    }



    public String toString()
    {
        return "";
    }
}
