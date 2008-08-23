/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
