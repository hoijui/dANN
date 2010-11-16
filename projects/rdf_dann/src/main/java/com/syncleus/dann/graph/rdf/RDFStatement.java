/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph.rdf;

import com.syncleus.dann.graph.AbstractDirectedEdge;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;

public class RDFStatement extends AbstractDirectedEdge<RDFValue> {

    public final URI predicate;
    private final Resource contexts;

    public RDFStatement(RDFValue sub, URI predicate, RDFValue obj, Resource contexts) {
        super(sub, obj);
        this.predicate = predicate;
        this.contexts = contexts;
    }

    @Override
    public int hashCode() {
        return predicate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RDFStatement) {
            RDFStatement rs = (RDFStatement) obj;
            return rs.predicate.equals(predicate);
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        //TODO print contexts
        return predicate.toString();
    }
}
