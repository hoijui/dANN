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

import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;

/**
 *
 * Contains a copy of an OpenRDF repository
 * @author seh
 */
public class MemoryRDFGraph extends MutableDirectedAdjacencyGraph<RDFValue, RDFStatement> {

    int maxEdges;

    public MemoryRDFGraph() {
        this(-1);
    }

    public MemoryRDFGraph(int maxEdges) {
        super();
        this.maxEdges = maxEdges;
    }

    /** iterates through the entire RDF graph and builds a dANN graph */
    public boolean add(Repository rep) {
        //TODO clear the repository first, then make 'refresh()' a public method

        try {
            RepositoryConnection con = rep.getConnection();
            RepositoryResult<Statement> is = con.getStatements(null, null, null, true, (Resource) null);
            while (is.hasNext() ) {
                Statement s = is.next();
                RDFValue subj = new RDFValue(s.getSubject());
                RDFValue obj = new RDFValue(s.getObject());
                RDFStatement statement = new RDFStatement(subj, s.getPredicate(), obj, s.getContext());
                add(subj);
                add(obj);
                add(statement);

                if (maxEdges>0) {
                    if (getEdges().size() > maxEdges)
                        break;
                }
            }
            is.close();
            con.close();
        } catch (RepositoryException ex) {
            Logger.getLogger(MemoryRDFGraph.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }


        return true;
    }
}
