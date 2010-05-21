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

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;

/**
 *
 * @author seh
 */
public class Main {

    public static void main(String[] args) {
        try {
            Repository myRepository = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
            myRepository.initialize();

            String rdfSource = "http://dbpedia.org/data/Semantic_Web.rdf";
            RepositoryConnection con = myRepository.getConnection();

            con.add(new URL(rdfSource), "", RDFFormat.RDFXML);
            con.close();
            MemoryRDFGraph graph = new MemoryRDFGraph();
            graph.add(myRepository);

            System.out.println(graph.getNodes());
            System.out.println(graph.getEdges());

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
