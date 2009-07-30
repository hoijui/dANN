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
package com.syncleus.core.dann.examples.colormap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

public class ColorMapDemo extends javax.swing.JFrame implements ActionListener
{
	private SpinnerNumberModel iterationsModel = new SpinnerNumberModel(INITIAL_ITERATIONS, 1, 10000,100);
	private SpinnerNumberModel learningRateModel = new SpinnerNumberModel(INITIAL_LEARNING_RATE, 0.01, 1.0, 0.01);

	private Color[] color1d;
	private Color[][] color2d;

	private Future<Color[]> future1d;
	private Future<Color[][]> future2d;

	private ColorMap1dCallable callable1d;
	private ColorMap2dCallable callable2d;

	private static final int INITIAL_ITERATIONS = 200;
	private static final double INITIAL_LEARNING_RATE = 0.5;

	private ExecutorService executor = Executors.newFixedThreadPool(1);

	private Timer progressTimer = new Timer(100, this);

    public ColorMapDemo() {
        initComponents();

		this.iterationsSpinner.setValue(INITIAL_ITERATIONS);
		this.iterationsSpinner.setModel(this.iterationsModel);
		this.learningRateSpinner.setValue(INITIAL_LEARNING_RATE);
		this.learningRateSpinner.setModel(this.learningRateModel);
		this.setResizable(false);
		this.setSize(550, 150);
		this.color1d = (new ColorMap1dCallable(INITIAL_ITERATIONS, INITIAL_LEARNING_RATE, 500)).call();
    }

	public void actionPerformed(ActionEvent evt)
	{
		if(this.callable1d != null)
		{
			this.progressBar.setMaximum(this.callable1d.getIterations());
			this.progressBar.setMinimum(0);
			this.progressBar.setValue(this.callable1d.getProgress());
		}
		else if(this.callable2d != null)
		{
			this.progressBar.setMaximum(this.callable2d.getIterations());
			this.progressBar.setMinimum(0);
			this.progressBar.setValue(this.callable2d.getProgress());
		}


		if(this.future1d != null)
		{
			if(! this.future1d.isDone())
				return;
			try
			{
				this.color2d = null;
				this.color1d = this.future1d.get();
				this.setSize(550, 150);
				this.repaint();

				this.future1d = null;
				this.future2d = null;
				this.progressTimer.stop();
				this.trainDisplayButton.setEnabled(true);

			}
			catch(Exception caughtException)
			{
				throw new AssertionError("call to get shouldnt fail");
			}
		}
		else if(this.future2d != null)
		{
			if(! this.future2d.isDone())
				return;
			try
			{
				this.color1d = null;
				this.color2d = this.future2d.get();
				this.setSize(550, 450);
				this.repaint();

				this.future1d = null;
				this.future2d = null;
				this.progressTimer.stop();
				this.trainDisplayButton.setEnabled(true);
			}
			catch(Exception caughtException)
			{
				throw new AssertionError("call to get shouldnt fail");
			}
		}
		else
		{
			this.progressTimer.stop();
			this.trainDisplayButton.setEnabled(true);
		}
	}


	@Override
	public void paint(Graphics graphics)
	{
		super.paint(graphics);

		if(this.color1d != null)
		{
			for(int colorIndex = 0; colorIndex < this.color1d.length; colorIndex++)
			{
				Color color = this.color1d[colorIndex];
				graphics.setColor(color);
				graphics.drawLine(25+colorIndex, 125, 25+colorIndex, 150);
			}
		}
		else if(this.color2d != null)
		{
			Graphics2D  graphics2d = (Graphics2D) graphics;

			for(int colorXIndex = 0; colorXIndex < this.color2d.length; colorXIndex++)
			{
				for(int colorYIndex = 0; colorYIndex < this.color2d[colorXIndex].length; colorYIndex++)
				{
					Color color = this.color2d[colorXIndex][colorYIndex];
					graphics2d.setColor(color);
					int xPos = colorXIndex*10;
					int yPos = colorYIndex*10;
					graphics2d.fillRect(5+xPos, 125+yPos, 15+xPos, 135+yPos);
				}
			}
		}
	}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        iterationsSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        learningRateSpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        dimentionalityComboBox = new javax.swing.JComboBox();
        trainDisplayButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Training Iterations:");

        jLabel2.setText("Learning Rate:");

        jLabel3.setText("Dimentionality:");

        dimentionalityComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1D", "2D" }));

        trainDisplayButton.setText("Train & Display");
        trainDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainDisplayButtonActionPerformed(evt);
            }
        });

        progressBar.setStringPainted(true);

        jMenu1.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                exitMenuItemMouseReleased(evt);
            }
        });
        jMenu1.add(exitMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        jMenuItem2.setText("About");
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iterationsSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(learningRateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dimentionalityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(trainDisplayButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(dimentionalityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(iterationsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(learningRateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(trainDisplayButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(219, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void exitMenuItemMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_exitMenuItemMouseReleased
	{//GEN-HEADEREND:event_exitMenuItemMouseReleased
		System.exit(0);
	}//GEN-LAST:event_exitMenuItemMouseReleased

	private void trainDisplayButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_trainDisplayButtonActionPerformed
	{//GEN-HEADEREND:event_trainDisplayButtonActionPerformed
		int iterations = this.iterationsModel.getNumber().intValue();
		double learningRate = this.learningRateModel.getNumber().doubleValue();

		if( this.dimentionalityComboBox.getSelectedIndex() == 0 )
		{
			if(this.future1d != null)
				this.future1d.cancel(true);
			if(this.future2d != null)
				this.future2d.cancel(true);

			this.callable2d = null;
			this.future2d = null;

			this.callable1d = new ColorMap1dCallable(iterations, learningRate, 500);
			this.future1d = executor.submit(this.callable1d);
		}
		else
		{
			if(this.future1d != null)
				this.future1d.cancel(true);
			if(this.future2d != null)
				this.future2d.cancel(true);

			this.callable1d = null;
			this.future1d = null;

			this.callable2d = new ColorMap2dCallable(iterations, learningRate, 50, 50);
			this.future2d = executor.submit(this.callable2d);
		}

		this.progressTimer.start();
		this.trainDisplayButton.setEnabled(false);
	}//GEN-LAST:event_trainDisplayButtonActionPerformed

    public static void main(String args[])
	{
        java.awt.EventQueue.invokeLater(new Runnable()
		{
            public void run()
			{
                new ColorMapDemo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox dimentionalityComboBox;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JSpinner iterationsSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JSpinner learningRateSpinner;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton trainDisplayButton;
    // End of variables declaration//GEN-END:variables

}
