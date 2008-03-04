package com.syncleus.core.dann.examples.test;

public class FormedWaveMathFunction extends WaveMathFunction
{
    public FormedWaveMathFunction()
    {
        super(new String[]{"form"});
    }
    
    public FormedWaveMathFunction(String[] additionalParameters)
    {
        super(combineLabels(new String[]{"form"},additionalParameters));
    }
    
    public void setForm(double form)
    {
        this.setParameter(this.getParameterNameIndex("form"), form);
    }
    
    public double getForm()
    {
        return this.getParameter(this.getParameterNameIndex("form"));
    } 
    
    public double calculate()
    {
        if( super.calculate() == 0.0)
            return 0.0;
        if( this.getAmplitude() == 0.0 )
            return 0.0;
        
        return (super.calculate()/Math.abs(super.calculate())) * Math.abs(Math.pow(Math.abs(super.calculate()/this.getAmplitude()),this.getForm()) * this.getAmplitude() );
    }
    
    public FormedWaveMathFunction clone()
    {
        FormedWaveMathFunction copy = new FormedWaveMathFunction();
        copy.setX(this.getX());
        copy.setFrequency(this.getFrequency());
        copy.setPhase(this.getPhase());
        copy.setAmplitude(this.getAmplitude());
        copy.setForm(this.getForm());
        return copy;
    }
    
    public String toString()
    {
        return "(" + super.toString() + "/|" + super.toString() + "|) * |(|" + super.toString() + ")/amplitude|^form| * amplitude";
    }
}
