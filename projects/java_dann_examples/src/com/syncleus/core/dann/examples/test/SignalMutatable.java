package com.syncleus.core.dann.examples.test;

public interface SignalMutatable<E> extends Mutatable<E>
{
    public E mutate();
    public E mutate(Signal newSignal);
}
