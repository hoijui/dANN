package com.syncleus.core.dann.examples.test;


import com.syncleus.util.UniqueId;
import java.util.Hashtable;
import java.util.TreeSet;

public class Organism
{
    TreeSet<Cell> cells = new TreeSet<Cell>();
    Hashtable<UniqueId, GlobalSignal> globalSignals = new Hashtable<UniqueId, GlobalSignal>();
    
    public GlobalSignal getGlobalSignal(UniqueId signalId)
    {
        return this.globalSignals.get(signalId);
    }
    
    void preTick()
    {
        for(Cell cell : this.cells)
            cell.preTick();
    }
    
    void tick()
    {
        for(Cell cell : this.cells)
            cell.tick();
    }
}
