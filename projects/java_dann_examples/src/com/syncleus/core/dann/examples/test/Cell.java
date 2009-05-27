/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                    *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by syncleus at http://www.syncleus.com.  *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact syncleus at the information below if you cannot find   *
 *  a license:                                                                 *
 *                                                                             *
 *  Syncleus                                                                   *
 *  1116 McClellan St.                                                         *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.core.dann.examples.test;


import com.syncleus.util.UniqueId;
import java.util.Hashtable;

public class Cell
{
    private Organism organism = null;
    private Nucleus nucleus = null;
    private Hashtable<UniqueId, LocalSignal> localSignals = new Hashtable<UniqueId, LocalSignal>();
    
    private LocalSignal mitosisActivator;
    private LocalSignal identitySignal;
    
    LocalSignal getLocalSignal(UniqueId signalId)
    {
        return this.localSignals.get(signalId);
    }
    
    GlobalSignal getGlobalSignal(UniqueId signalId)
    {
        return this.organism.getGlobalSignal(signalId);
    }
    
    Signal updateSignal(Signal oldSignal)
    {
        if( oldSignal instanceof GlobalSignal )
            return oldSignal;
        
        LocalSignal oldLocalSignal = (LocalSignal) oldSignal;
        
        //if the local signal already exists return the current one else create it
        LocalSignal newSignal = this.getLocalSignal(oldLocalSignal.getId());
        if( newSignal != null )
            return newSignal;
        else
            newSignal = oldLocalSignal.clone();
        
        this.localSignals.put(newSignal.getId(), newSignal);
        
        return newSignal;
    }
    
    void preTick()
    {
        this.nucleus.preTick();
    }
    
    void tick()
    {
        this.nucleus.tick();
    }
}
