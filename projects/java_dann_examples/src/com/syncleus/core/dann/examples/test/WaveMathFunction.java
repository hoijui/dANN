package com.syncleus.core.dann.examples.test;

public class WaveMathFunction extends MathFunction
{
    public WaveMathFunction()
    {
        super(new String[]{"x", "frequency", "amplitude", "phase"});
    }
    
    protected WaveMathFunction(String[] parameterNames)
    {
        super(
                combineLabels(new String[]{"x", "frequency", "amplitude", "phase"}, parameterNames)
        );
    }
    
    protected void setX(double x)
    {
        this.setParameter(this.getParameterNameIndex("x"), x);
    }
    
    protected double getX()
    {
        return this.getParameter(this.getParameterNameIndex("x"));
    }
    
    public void setFrequency(double frequency)
    {
        this.setParameter(this.getParameterNameIndex("frequency"), frequency);
    }
    
    public double getFrequency()
    {
        return this.getParameter(this.getParameterNameIndex("frequency"));
    }
    
    public void setAmplitude(double amplitude)
    {
        this.setParameter(this.getParameterNameIndex("amplitude"), amplitude);
    }
    
    public double getAmplitude()
    {
        return this.getParameter(this.getParameterNameIndex("amplitude"));
    }
    
    public void setPhase(double phase)
    {
        this.setParameter(this.getParameterNameIndex("phase"), phase);
    }
    
    public double getPhase()
    {
        return this.getParameter(this.getParameterNameIndex("phase"));
    }
    
    public double calculate()
    {
        return Math.sin( (this.getX()+(this.getPhase()/360)) * 2 * Math.PI * this.getFrequency()) * this.getAmplitude();
    }
    
    public WaveMathFunction clone()
    {
        WaveMathFunction copy = new WaveMathFunction();
        copy.setX(this.getX());
        copy.setFrequency(this.getFrequency());
        copy.setPhase(this.getPhase());
        copy.setAmplitude(this.getAmplitude());
        return copy;
    }
    
    public String toString()
    {
        return this.toString("x");
    }
    
    String toString(String xName)
    {
        return "sin( (" + xName + "+(phase/360)) * 2pi * frequency) * amplitude";
    }
}
