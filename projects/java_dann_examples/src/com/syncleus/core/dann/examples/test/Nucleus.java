package com.syncleus.core.dann.examples.test;


import java.util.TreeSet;

public class Nucleus implements Cloneable
{
    private TreeSet<Chromosome> chromosomes = new TreeSet<Chromosome>();
    private Cell cell = null;
    
    private Nucleus(Nucleus originalNucleus)
    {
        this.cell = originalNucleus.cell;
        for(Chromosome oldChromosome : originalNucleus.chromosomes)
            this.chromosomes.add(oldChromosome);
    }
    
    public Nucleus clone()
    {
        Nucleus copy = new Nucleus(this);
        return copy;
    }



    public Nucleus clone(Cell cell)
    {
        Nucleus copy = this.clone();
        copy.setCell(cell);
        return copy;
    }
    
    
    void preTick()
    {
        for(Chromosome chromosome : this.chromosomes)
            chromosome.preTick();
    }
    
    void tick()
    {
        for(Chromosome chromosome : this.chromosomes)
            chromosome.preTick();
    }



    private void setCell(Cell cell)
    {
        this.cell = cell;
        for(Chromosome chromosome : this.chromosomes )
            chromosome.setCell(cell);
    }
}
