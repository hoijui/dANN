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
package com.syncleus.dann.examples.nci.ui;

import com.syncleus.dann.graph.drawing.hyperassociativemap.visualization.*;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.swing.JDialog;
import javax.swing.Timer;


public class ViewBrain extends JDialog implements ActionListener, KeyListener
{
    private HyperassociativeMapCanvas brainVisual;
    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private FutureTask<Void> lastRun;

    public ViewBrain(Frame parent, HyperassociativeMapCanvas brainVisual)
    {
        super(parent, false);
        
        initComponents();
        
        this.brainVisual = brainVisual;
        
        this.add(this.brainVisual);
        this.brainVisual.setLocation(0, 0);
        this.brainVisual.setSize(800, 600);
        this.brainVisual.setVisible(true);
        
        this.setSize(800,600);
        
        this.brainVisual.refresh();
        
        this.lastRun = new FutureTask<Void>(new UpdateViewRun(this.brainVisual),null);
        this.executor.execute(this.lastRun);
        
        new Timer(100, this).start();

		this.addKeyListener(this);
		this.brainVisual.addKeyListener(this);
    }


	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_R)
			this.brainVisual.getHyperassociativeMap().reset();
		if(e.getKeyCode() == KeyEvent.VK_L)
			this.brainVisual.getHyperassociativeMap().resetLearning();
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
	}
    
    
    public void actionPerformed(ActionEvent evt)
    {
        if((this.lastRun != null)&&(this.lastRun.isDone() == false))
            return;
        
        if(this.isVisible() == false)
            return;
        
        this.lastRun = new FutureTask<Void>(new UpdateViewRun(this.brainVisual),null);
        this.executor.execute(this.lastRun);
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
