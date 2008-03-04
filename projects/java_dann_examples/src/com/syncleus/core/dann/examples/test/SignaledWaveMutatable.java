package com.syncleus.core.dann.examples.test;

public interface SignaledWaveMutatable<E> extends SignalMutatable<E>
{
    public E mutate();
    public E mutate(Signal newSignal);
    public E mutate(Signal newSignal, WaveMultidimensionalMathFunction newWave);
}
