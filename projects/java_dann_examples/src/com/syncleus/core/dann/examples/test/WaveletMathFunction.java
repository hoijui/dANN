package com.syncleus.core.dann.examples.test;


import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.TreeSet;

public class WaveletMathFunction extends MathFunction
{
    private TreeSet<String> dimensions = new TreeSet<String>();
    //private Hashtable<DistributedFormedWaveMathFunction, String> waves = new Hashtable<DistributedFormedWaveMathFunction, String>();
    private Hashtable<String, DistributedFormedWaveMathFunction[]> waves = new Hashtable<String, DistributedFormedWaveMathFunction[]>();



    public WaveletMathFunction(String[] dimensions)
    {
        super(dimensions);
        for(String dimension:dimensions)
            this.dimensions.add(dimension);
    }
    
    
    public TreeSet<String> getDimensions()
    {
        return (TreeSet<String>) this.dimensions.clone();
    }
    
    
    private boolean checkWaveDimension(WaveDimension[] waves)
    {
        TreeSet<String> waveDimensions = new TreeSet<String>();
        for(WaveDimension wave : waves)
            waveDimensions.add(wave.getDimension());
        
        if( this.dimensions.size() != waveDimensions.size())
            return false;
        
        for(String dimension : this.dimensions)
            if( waveDimensions.contains(dimension) == false )
                return false;
        
        return true;
    }
    
    
    private void addToWaveArray(WaveDimension newWave)
    {
        DistributedFormedWaveMathFunction[] currentWaves = this.waves.get(newWave.getDimension());
        DistributedFormedWaveMathFunction[] newWaves;
        if( currentWaves != null)
        {
            newWaves = new DistributedFormedWaveMathFunction[currentWaves.length + 1];
            System.arraycopy(newWaves, 0, currentWaves, 0, currentWaves.length);
        }
        else
            newWaves = new DistributedFormedWaveMathFunction[1];
        newWaves[newWaves.length - 1] = newWave.getWave();
        
        this.waves.put(newWave.getDimension(), newWaves);
    }

    public void addWave(WaveDimension[] waves)
    {
        if( checkWaveDimension(waves) == false )
            throw new InvalidParameterException("dimensions dont match");
        
        for(WaveDimension wave : waves)
            addToWaveArray(wave);
    }



    public double calculate()
    {
        double result = 0.0;
        for(int index = 0; index < this.waves.get(this.dimensions.first()).length; index++)
        {
            double waveProduct = 1.0;
            double waveTotal = 0.0;
            for(String dimension : this.dimensions)
            {
                DistributedFormedWaveMathFunction wave = this.waves.get(dimension)[index];
                //System.out.println("wave: " + wave);
                if( wave != null )
                {
                    wave.setX(this.getParameter(this.getParameterNameIndex(dimension)));
                    waveProduct *= wave.calculate();
                    waveTotal += wave.calculate();
                }
            }
            
            if(waveTotal != 0.0 )
                result += waveProduct/waveTotal;
        }
        
        return result;
        
        /*
        double waveTotal = 0.0;
        for( DistributedFormedWaveMathFunction wave : this.waves.keySet())
        {
            String currentWaveDimension = this.waves.get(wave);
            int currentWaveDimensionIndex = this.getParameterNameIndex(currentWaveDimension);
            double dimensionValue = this.getParameter(currentWaveDimensionIndex);
            
            wave.setX(dimensionValue);
            waveTotal += wave.calculate();
        }
        
        return waveTotal;
         */
    }
    
    
    public WaveletMathFunction clone()
    {
        String[] dimensionsCopy = new String[this.dimensions.size()];
        this.dimensions.toArray(dimensionsCopy);
        
        WaveletMathFunction copy = new WaveletMathFunction(dimensionsCopy);
        for( Entry<String, DistributedFormedWaveMathFunction[]> wavePair : waves.entrySet())
        {
            //DistributedFormedWaveMathFunction wave = wavePair.getKey();
            //String dimension = wavePair.getValue();
            
            copy.waves.put(wavePair.getKey(), wavePair.getValue().clone());
        }
        
        return copy;
    }



    public String toString()
    {
        return "";
    }
}
