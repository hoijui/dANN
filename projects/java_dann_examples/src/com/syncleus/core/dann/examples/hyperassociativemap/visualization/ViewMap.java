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
package com.syncleus.core.dann.examples.hyperassociativemap.visualization;

import com.syncleus.dann.*;
import com.syncleus.dann.hyperassociativemap.*;
import com.syncleus.dann.hyperassociativemap.visualization.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.swing.JFrame;
import javax.swing.Timer;


public class ViewMap extends JFrame implements ActionListener
{
    private HyperassociativeMapCanvas mapVisual;
    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private FutureTask<Void> lastRun;



    public ViewMap()
    {
        LayeredHyperassociativeMap associativeMap = new LayeredHyperassociativeMap(8);

        this.mapVisual = new HyperassociativeMapCanvas(associativeMap, 0.07F);

        initComponents();

        this.add(this.mapVisual);
        this.mapVisual.setLocation(0, 0);
        this.mapVisual.setSize(800, 600);
        this.mapVisual.setVisible(true);

        this.setSize(800, 600);

        this.mapVisual.refresh();

        this.lastRun = new FutureTask<Void>(new UpdateViewRun(this.mapVisual), null);
        this.executor.execute(this.lastRun);

        new Timer(100, this).start();

    }



    public void actionPerformed(ActionEvent evt)
    {
        if ((this.lastRun != null) && (this.lastRun.isDone() == false))
            return;

        if (this.isVisible() == false)
            return;

        this.lastRun = new FutureTask<Void>(new UpdateViewRun(this.mapVisual), null);
        this.executor.execute(this.lastRun);
    }



    public static void main(String args[]) throws Exception
    {
        java.awt.EventQueue.invokeLater(new Runnable()
                                      {
                                          public void run()
                                          {
                                              new ViewMap().setVisible(true);
                                          }
                                      });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	// </editor-fold>
}
