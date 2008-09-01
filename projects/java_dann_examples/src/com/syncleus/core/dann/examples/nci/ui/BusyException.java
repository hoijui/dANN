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
package com.syncleus.core.dann.examples.nci.ui;

/**
 *
 * @author Administrator
 */
public class BusyException extends Exception {

    /**
     * Creates a new instance of <code>BusyException</code> without detail message.
     */
    public BusyException() {
    }


    /**
     * Constructs an instance of <code>BusyException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BusyException(String msg) {
        super(msg);
    }
}
