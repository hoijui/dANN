package com.syncleus.core.dann.examples.test;


import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

public class SignalProcessingWavelet implements SignaledWaveMutatable<SignalProcessingWavelet>
{
    //private Hashtable<DistributedFormedWaveMathFunction, Signal> inputs = new Hashtable<DistributedFormedWaveMathFunction, Signal>();
    private Signal output;
    private double currentOutput = 0.0;
    private static Random random = new Random();
    //ArrayList<Hashtable<Signal, WaveMultidimensionalMathFunction>> waveDimensions = new ArrayList<Hashtable<Signal, WaveMultidimensionalMathFunction>>();
    TreeSet<Signal> dimensions = new TreeSet<Signal>();
    ArrayList<WaveMultidimensionalMathFunction> waveDimensions = new ArrayList<WaveMultidimensionalMathFunction>();
    WaveletMathFunction wavelet;



    public SignalProcessingWavelet(Signal initialInput, Signal initialOutput)
    {
        //this.inputs.put(initialWave, initialInput);
        this.output = initialOutput;

        //Hashtable<Signal, WaveMultidimensionalMathFunction> initialDimensions = new Hashtable<Signal, WaveMultidimensionalMathFunction>();
        //initialDimensions.put(initialInput, initialWave);
        this.dimensions.add(initialInput);
        
        WaveMultidimensionalMathFunction initialWave = generateNewWave();
        this.waveDimensions.add(initialWave);
    }



    private SignalProcessingWavelet()
    {
    }



    public WaveletMathFunction getWavelet()
    {
        this.reconstructWavelet();
        return this.wavelet.clone();
    }



    public void updateOutput()
    {
        this.reconstructWavelet();

        for(Signal signal:this.dimensions)
        {
            this.wavelet.setParameter(this.wavelet.getParameterNameIndex(signal.getId().toString()), signal.getValue());
        }

        double newOutput = this.wavelet.calculate();

        this.output.setValue(this.output.getValue() + (-1 * this.currentOutput) + newOutput);
        this.currentOutput = newOutput;
        
        //for(Signal dimension : this.dimensions)
        //    System.out.println("dimension: " + dimension.getId().toString());
    }



    public SignalProcessingWavelet clone()
    {
        SignalProcessingWavelet copy = new SignalProcessingWavelet();
        //copy.inputs = (Hashtable<DistributedFormedWaveMathFunction, Signal>)this.inputs.clone();
        copy.dimensions = (TreeSet<Signal>)this.dimensions.clone();
        copy.waveDimensions = (ArrayList<WaveMultidimensionalMathFunction>)this.waveDimensions.clone();
        /*
        for(Hashtable<Signal, WaveMultidimensionalMathFunction> current:this.waveDimensions)
        {
        Hashtable<Signal, WaveMultidimensionalMathFunction> waveDimensionsCopy = new Hashtable<Signal, WaveMultidimensionalMathFunction>();
        for(Entry<Signal, WaveMultidimensionalMathFunction> currentPair:current.entrySet())
        {
        waveDimensionsCopy.put(currentPair.getKey(), currentPair.getValue().clone());
        }
        copy.waveDimensions.add(waveDimensionsCopy);
        }*/
        copy.output = this.output;
        copy.wavelet = (this.wavelet == null ? null : this.wavelet.clone());
        return copy;
    }



    private void reconstructWavelet()
    {
        /*
        TreeSet<String> newDimensions = new TreeSet<String>();
        for(Hashtable<Signal, WaveMultidimensionalMathFunction> current:this.waveDimensions)
        {
        for(Signal currentSignal:current.keySet())
        {
        newDimensions.add(currentSignal.getId().toString());
        }
        }
        //*
        for(Entry<DistributedFormedWaveMathFunction, Signal> input:this.inputs.entrySet())
        {
        newDimensions.add(input.getValue().getId().toString());
        }*/
        String[] signalNames = new String[this.dimensions.size()];
        //System.out.println("dimensions size: " + this.dimensions.size());
        int signalNamesIndex = 0;
        for(Signal dimension:this.dimensions)
        {
            signalNames[signalNamesIndex++] = dimension.getId().toString();
        }

        this.wavelet = new WaveletMathFunction(signalNames);

        for(WaveMultidimensionalMathFunction wave:this.waveDimensions)
        {
            this.wavelet.addWave(wave);
        }

    /*
    for(Hashtable<Signal, WaveMultidimensionalMathFunction> current:this.waveDimensions)
    {
    WaveDimension[] dimensions = new WaveDimension[signalNames.length];
    int index = 0;
    TreeSet<String> filledSignals = new TreeSet<String>();
    for(Entry<Signal, WaveMultidimensionalMathFunction> currentEntry:current.entrySet())
    {
    dimensions[index] = new WaveDimension(currentEntry.getKey().getId().toString(), currentEntry.getValue());
    index++;
    filledSignals.add(currentEntry.getKey().getId().toString());
    }
    while(index < dimensions.length)
    {
    String unusedSignal = null;
    //System.out.println("signal count: " + signalNames.length);
    for(String currentSignal:signalNames)
    {
    if(filledSignals.contains(currentSignal) == false)
    {
    unusedSignal = currentSignal;
    }
    }
    WaveMultidimensionalMathFunction wave = new WaveMultidimensionalMathFunction(1.0);
    dimensions[index] = new WaveDimension(unusedSignal, wave);
    index++;
    }
    this.wavelet.addWave(dimensions);
    }*/
    /*
    for(Entry<DistributedFormedWaveMathFunction, Signal> input:this.inputs.entrySet())
    {
    Signal currentSignal = input.getValue();
    DistributedFormedWaveMathFunction currentWave = input.getKey();
    WaveDimension waveDimension = new WaveDimension(currentSignal.getId().toString(), currentWave);
    this.wavelet.addWave(waveDimension);
    }*/
    }



    public SignalProcessingWavelet mutate()
    {
        if(this.waveDimensions.size() > 0)
        {
            //there is a chance a signal will be used to create a new wave
            if(random.nextDouble() < 0.5)
            {
                //Signal newSignal = this.getRandomSignal();
                //return this.mutate(newSignal);
                SignalProcessingWavelet copy = this.clone();
                copy.waveDimensions.add(this.generateRandomWave());
                return copy;
            }
            else
            {
                SignalProcessingWavelet copy = this.clone();
                copy.waveDimensions.add(this.generateNewWave());
                return copy;
            }
        //there is a chance a wave will be removed
            /*
        else
        {
        if(this.inputs.size() >= 2)
        {
        DistributedFormedWaveMathFunction[] waves = getWaves();
        DistributedFormedWaveMathFunction randomWave = waves[random.nextInt(waves.length)];
        SignalProcessingWavelet copy = this.clone();
        copy.inputs.remove(randomWave);
        return copy;
        }
        }*/
        }

        return this;
    }



    public SignalProcessingWavelet mutate(Signal newSignal)
    {
        //if(this.inputs.size() > 0)
        //{
        //there is a chance the wave used will be new
        if(random.nextDouble() < 0.9)
        {
            SignalProcessingWavelet copy = this.clone();
            //WaveMultidimensionalMathFunction newWave = generateNewWave(new String[]{newSignal.getId().toString()});
            //System.out.println("adding dimensions: " + newSignal.getId().toString());
            //System.out.println("dimensions before: " + copy.dimensions.size());
            copy.dimensions.add(newSignal);
            //System.out.println("dimensions after: " + copy.dimensions.size());
            return copy;
        //WaveMultidimensionalMathFunction newWave = generateNewWave();

        //return this.mutate(newWave);
        }
        //there is a chance the new signal will replace an old signal
            /*else
        {
        DistributedFormedWaveMathFunction[] waves = getWaves();
        DistributedFormedWaveMathFunction randomWave = waves[random.nextInt(waves.length)];
        SignalProcessingWavelet copy = this.clone();
        copy.inputs.put(randomWave, newSignal);
        return copy;
        }*/
        //}

        return this;
    }



    public SignalProcessingWavelet mutate(Signal newSignal, WaveMultidimensionalMathFunction newWave)
    {
        return this.clone();
    }
    /*
    public SignalProcessingWavelet mutate(WaveMultidimensionalMathFunction newWave)
    {
    SignalProcessingWavelet copy = this.clone();
    //copy.dimensions.add(newSignal);
    copy.waveDimensions.add(newWave);
    return copy;
    // *
    //ther eis a chance it will be added 
    SignalProcessingWavelet copy = this.clone();
    while(random.nextDouble() < 0.999)
    {
    Hashtable<Signal, WaveMultidimensionalMathFunction> randomWave = this.generateRandomWave();
    if(randomWave.containsKey(newSignal) == false)
    {
    randomWave.put(newSignal, newWave);
    copy.waveDimensions.add(randomWave);
    return copy;
    }
    }
    //copy.inputs.put(newWave, newSignal);
    Hashtable<Signal, WaveMultidimensionalMathFunction> newDimension = new Hashtable<Signal, WaveMultidimensionalMathFunction>();
    newDimension.put(newSignal, newWave);
    copy.waveDimensions.add(newDimension);
    return copy;* /
    }*/


    /*
    private DistributedFormedWaveMathFunction[] getWaves()
    {
    DistributedFormedWaveMathFunction[] waves = new DistributedFormedWaveMathFunction[this.inputs.size()];
    this.inputs.keySet().toArray(waves);
    return waves;
    }
     */
    /*
    private Signal[] getSignals()
    {*/
    /*
    TreeSet<Signal> newDimensions = new TreeSet<Signal>();
    for(Entry<DistributedFormedWaveMathFunction, Signal> input:this.inputs.entrySet())
    {
    newDimensions.add(input.getValue());
    }
    Signal[] signals = new Signal[newDimensions.size()];
    newDimensions.toArray(signals);
    return signals;*//*
    TreeSet<Signal> newDimensions = new TreeSet<Signal>();
    for(Hashtable<Signal, WaveMultidimensionalMathFunction> current:this.waveDimensions)
    {
    for(Signal currentSignal:current.keySet())
    {
    newDimensions.add(currentSignal);
    }
    }
    Signal[] signals = new Signal[newDimensions.size()];
    newDimensions.toArray(signals);
    return signals;
    }*/


    private Signal getRandomSignal()
    {

        Signal[] signalsArray = new Signal[this.dimensions.size()];
        this.dimensions.toArray(signalsArray);
        Signal randomSignal = signalsArray[random.nextInt(signalsArray.length)];

        return randomSignal;
    }



    private WaveMultidimensionalMathFunction generateNewWave()
    {
        //   if(this.waveDimensions.size() > 0)
        //   {
        //       return generateRandomWave();
        //   }
        //   else
        //   {
        String[] dimensionNames = new String[this.dimensions.size()];
        int index = 0;
        for(Signal dimension : this.dimensions)
            dimensionNames[index++] = dimension.getId().toString();
        WaveMultidimensionalMathFunction newWave = new WaveMultidimensionalMathFunction(dimensionNames);

        newWave.setFrequency(random.nextGaussian() * 10);
        newWave.setPhase(random.nextGaussian() * 10);
        newWave.setAmplitude(random.nextGaussian());
        newWave.setForm(random.nextGaussian() * 10);
        if(newWave.getForm() <= 0.0)
        {
            newWave.setForm(newWave.getForm() + ((1 + random.nextGaussian()) * 10));
        }

        for(String dimensionName:dimensionNames)
        {
            newWave.setCenter(dimensionName, newWave.getCenter(dimensionName) + ((random.nextFloat() * 2 - 1) * 10));
        }
        newWave.setDistribution(random.nextGaussian() * 10);

        return newWave;
    //}
    }



    private WaveMultidimensionalMathFunction generateRandomWave()
    {
        if(this.waveDimensions.size() > 0)
        {
            WaveMultidimensionalMathFunction[] wavesArray = new WaveMultidimensionalMathFunction[this.waveDimensions.size()];
            this.waveDimensions.toArray(wavesArray);
            WaveMultidimensionalMathFunction randomWave = wavesArray[random.nextInt(wavesArray.length)];
            WaveMultidimensionalMathFunction newWave = randomWave.clone();

            if(random.nextDouble() <= 1.0)
            {
                newWave.setFrequency(newWave.getFrequency() + ((random.nextFloat() * 2 - 1) * 10));
            }
            if(random.nextDouble() <= 1.0)
            {
                newWave.setPhase(newWave.getPhase() + ((random.nextFloat() * 2 - 1) * 10));
            }
            if(random.nextDouble() <= 1.0)
            {
                newWave.setAmplitude(newWave.getAmplitude() + ((random.nextFloat() * 2 - 1) * 10));
            }
            if(random.nextDouble() <= 1.0)
            {
                newWave.setForm(newWave.getForm() + (random.nextFloat() * 10.0));
            }
            if(random.nextDouble() <= 1.0)
            {
                newWave.setDistribution(newWave.getDistribution() + ((random.nextFloat() * 2 - 1) * 10));
            }
            if(random.nextDouble() <= 1.0)
            {
                String[] dimensionNames = newWave.getDimensionNames();
                for(String dimensionName:dimensionNames)
                {
                    newWave.setCenter(dimensionName, newWave.getCenter(dimensionName) + ((random.nextFloat() * 2 - 1) * 10));
                }
            }

            return newWave;
        }

        return null;


    /*
    DistributedFormedWaveMathFunction newWave = new DistributedFormedWaveMathFunction();
    if(this.waveDimensions.size() <= 0)
    {
    newWave.setX(0.0);
    newWave.setFrequency(random.nextGaussian() * 10);
    newWave.setPhase(random.nextGaussian() * 10);
    newWave.setAmplitude(random.nextGaussian());
    newWave.setForm(random.nextGaussian() * 10);
    if(newWave.getForm() <= 0.0)
    {
    newWave.setForm(newWave.getForm() + ((1 + random.nextGaussian()) * 10));
    }
    newWave.setCenter(random.nextGaussian() * 10);
    newWave.setDistribution(random.nextGaussian() * 10);
    }
    else
    {
    DistributedFormedWaveMathFunction[] waves = this.getWaves();
    DistributedFormedWaveMathFunction randomWave = waves[random.nextInt(waves.length)];
    newWave = randomWave.clone();
    if(random.nextDouble() <= 1.0)
    {
    newWave.setFrequency(newWave.getFrequency() + ((random.nextFloat() * 2 - 1) * 10));
    }
    if(random.nextDouble() <= 1.0)
    {
    newWave.setPhase(newWave.getPhase() + ((random.nextFloat() * 2 - 1) * 10));
    }
    if(random.nextDouble() <= 1.0)
    {
    newWave.setAmplitude(newWave.getAmplitude() + ((random.nextFloat() * 2 - 1) * 10));
    }
    if(random.nextDouble() <= 1.0)
    {
    newWave.setForm(newWave.getForm() + (random.nextFloat() * 10.0));
    }
    if(random.nextDouble() <= 1.0)
    {
    newWave.setCenter(newWave.getCenter() + ((random.nextFloat() * 2 - 1) * 100));
    }
    if(random.nextDouble() <= 1.0)
    {
    newWave.setDistribution(newWave.getDistribution() + ((random.nextFloat() * 2 - 1) * 10));
    }
    }
    return newWave;*/
    }
}
