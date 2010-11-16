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
package com.syncleus.core.dann.examples.fft;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import com.syncleus.dann.dataprocessing.signal.transform.*;

public class FftDemo extends JFrame implements ActionListener
{
	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	private FastFourierTransformer transformer;
	final private JProgressBar[] frequencyBars;
	private Timer sampleTimer = new Timer(100, this);

    public FftDemo()
	{
        initComponents();

		this.setResizable(false);
		
		this.frequencyBars = new JProgressBar[]{frequencyBar1, frequencyBar2,
												frequencyBar3, frequencyBar4,
												frequencyBar5, frequencyBar6,
												frequencyBar7, frequencyBar8,
												frequencyBar9, frequencyBar10,
												frequencyBar11, frequencyBar12,
												frequencyBar13, frequencyBar14,
												frequencyBar15, frequencyBar16,
												frequencyBar17, frequencyBar18,
												frequencyBar19, frequencyBar20,
												frequencyBar21, frequencyBar22,
												frequencyBar23, frequencyBar24};

		//set the colors as a fradient from blue to red
		for(int index = 0; index < this.frequencyBars.length; index++)
		{
			float colorPercent = ((float)index) / ((float)(this.frequencyBars.length-1));
			this.frequencyBars[index].setForeground(new Color(colorPercent, 0f, 1f - colorPercent));
			this.frequencyBars[index].setMaximum(1024);
		}

		this.audioFormat = getAudioFormat();
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		try
		{
			this.targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
		}
		catch(LineUnavailableException caughtException)
		{
			System.out.println("Line unavailible, exiting...");
			System.exit(0);
		}

		this.transformer = new CooleyTukeyFastFourierTransformer(1024, 8000);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frequencyBar1 = new javax.swing.JProgressBar();
        listenButton = new javax.swing.JButton();
        frequencyBar10 = new javax.swing.JProgressBar();
        frequencyBar2 = new javax.swing.JProgressBar();
        frequencyBar4 = new javax.swing.JProgressBar();
        frequencyBar5 = new javax.swing.JProgressBar();
        frequencyBar9 = new javax.swing.JProgressBar();
        frequencyBar3 = new javax.swing.JProgressBar();
        frequencyBar6 = new javax.swing.JProgressBar();
        frequencyBar7 = new javax.swing.JProgressBar();
        frequencyBar8 = new javax.swing.JProgressBar();
        frequencyBar11 = new javax.swing.JProgressBar();
        frequencyBar12 = new javax.swing.JProgressBar();
        frequencyBar13 = new javax.swing.JProgressBar();
        frequencyBar14 = new javax.swing.JProgressBar();
        frequencyBar15 = new javax.swing.JProgressBar();
        frequencyBar16 = new javax.swing.JProgressBar();
        frequencyBar17 = new javax.swing.JProgressBar();
        frequencyBar18 = new javax.swing.JProgressBar();
        frequencyBar19 = new javax.swing.JProgressBar();
        frequencyBar20 = new javax.swing.JProgressBar();
        frequencyBar21 = new javax.swing.JProgressBar();
        frequencyBar22 = new javax.swing.JProgressBar();
        frequencyBar23 = new javax.swing.JProgressBar();
        frequencyBar24 = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenuItem = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenuItem = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Fast Fourier Transform Demo");

        frequencyBar1.setForeground(new java.awt.Color(0, 0, 255));
        frequencyBar1.setOrientation(1);

        listenButton.setText("Listen");
        listenButton.setName("listenButton"); // NOI18N
        listenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listenButtonActionPerformed(evt);
            }
        });

        frequencyBar10.setOrientation(1);

        frequencyBar2.setOrientation(1);

        frequencyBar4.setOrientation(1);

        frequencyBar5.setOrientation(1);

        frequencyBar9.setOrientation(1);

        frequencyBar3.setOrientation(1);

        frequencyBar6.setOrientation(1);

        frequencyBar7.setOrientation(1);

        frequencyBar8.setOrientation(1);

        frequencyBar11.setOrientation(1);

        frequencyBar12.setOrientation(1);

        frequencyBar13.setOrientation(1);

        frequencyBar14.setOrientation(1);

        frequencyBar15.setOrientation(1);

        frequencyBar16.setOrientation(1);

        frequencyBar17.setOrientation(1);

        frequencyBar18.setOrientation(1);

        frequencyBar19.setOrientation(1);

        frequencyBar20.setOrientation(1);

        frequencyBar21.setOrientation(1);

        frequencyBar22.setOrientation(1);

        frequencyBar23.setOrientation(1);

        frequencyBar24.setForeground(new java.awt.Color(255, 0, 0));
        frequencyBar24.setOrientation(1);

        fileMenuItem.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                exitMenuItemMouseReleased(evt);
            }
        });
        fileMenuItem.add(exitMenuItem);

        jMenuBar1.add(fileMenuItem);

        helpMenuItem.setText("Help");

        aboutMenuItem.setText("About");
        helpMenuItem.add(aboutMenuItem);

        jMenuBar1.add(helpMenuItem);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(frequencyBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frequencyBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frequencyBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frequencyBar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frequencyBar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frequencyBar6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frequencyBar7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frequencyBar8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frequencyBar9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frequencyBar10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(frequencyBar11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listenButton)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(frequencyBar12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frequencyBar24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(frequencyBar24, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar23, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar22, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar21, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar20, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar19, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar18, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar17, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar16, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar15, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar14, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar8, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar7, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar6, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar5, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar4, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar9, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar10, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar11, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar12, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addComponent(frequencyBar13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listenButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void exitMenuItemMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_exitMenuItemMouseReleased
	{//GEN-HEADEREND:event_exitMenuItemMouseReleased
		System.exit(0);
	}//GEN-LAST:event_exitMenuItemMouseReleased

	private void listenButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_listenButtonActionPerformed
	{//GEN-HEADEREND:event_listenButtonActionPerformed
		try
		{
			if(!this.targetDataLine.isOpen())
			{
				this.targetDataLine.open(audioFormat);
				this.targetDataLine.start();
				
				this.sampleTimer.start();

				this.listenButton.setText("Stop");
			}
			else
			{
				this.sampleTimer.stop();

				this.targetDataLine.stop();
				this.targetDataLine.close();

				this.listenButton.setText("Listen");
			}
		}
		catch(LineUnavailableException caughtException)
		{
			System.out.println("Line unavailible, exiting...");
			System.exit(0);
		}
	}//GEN-LAST:event_listenButtonActionPerformed

	public void actionPerformed(ActionEvent evt)
	{
		if(this.transformer.getBlockSize()*2 <= this.targetDataLine.available())
		{
			final byte[] signalBytes = new byte[this.transformer.getBlockSize()*2];
			this.targetDataLine.read(signalBytes, 0, signalBytes.length);

			final double[] signal = new double[this.transformer.getBlockSize()];
			for(int signalIndex = 0; signalIndex < signal.length; signalIndex++)
			{
				final int signalBytesIndex = signalIndex * 2;
				signal[signalIndex] = bytesToDouble(signalBytes[signalBytesIndex], signalBytes[signalBytesIndex+1]);
			}


			final DiscreteFourierTransform transform = this.transformer.transform(signal);
			final double maximumFrequency = transform.getMaximumFrequency();
			final double bandSize = maximumFrequency/((double)this.frequencyBars.length);
			for(int frequencyBarIndex = 0; frequencyBarIndex < this.frequencyBars.length; frequencyBarIndex++)
			{
				double bandPower = transform.getBandGeometricMean(((double)frequencyBarIndex)*bandSize, ((double)frequencyBarIndex+1)*bandSize);
				this.frequencyBars[frequencyBarIndex].setValue((int) (bandPower*500.0));
			}
		}
	}

	private double bytesToDouble(byte... data)
	{
		return ((double) (((short)data[1])<<8) + ((short)data[0])) / ((double)Short.MAX_VALUE);

	}

	private AudioFormat getAudioFormat()
	{
		//8000,11025,16000,22050,44100
		float sampleRate = 8000.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

    public static void main(String args[])
	{
        java.awt.EventQueue.invokeLater(
			new Runnable()
			{
				public void run()
				{
					new FftDemo().setVisible(true);
				}
			});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenuItem;
    private javax.swing.JProgressBar frequencyBar1;
    private javax.swing.JProgressBar frequencyBar10;
    private javax.swing.JProgressBar frequencyBar11;
    private javax.swing.JProgressBar frequencyBar12;
    private javax.swing.JProgressBar frequencyBar13;
    private javax.swing.JProgressBar frequencyBar14;
    private javax.swing.JProgressBar frequencyBar15;
    private javax.swing.JProgressBar frequencyBar16;
    private javax.swing.JProgressBar frequencyBar17;
    private javax.swing.JProgressBar frequencyBar18;
    private javax.swing.JProgressBar frequencyBar19;
    private javax.swing.JProgressBar frequencyBar2;
    private javax.swing.JProgressBar frequencyBar20;
    private javax.swing.JProgressBar frequencyBar21;
    private javax.swing.JProgressBar frequencyBar22;
    private javax.swing.JProgressBar frequencyBar23;
    private javax.swing.JProgressBar frequencyBar24;
    private javax.swing.JProgressBar frequencyBar3;
    private javax.swing.JProgressBar frequencyBar4;
    private javax.swing.JProgressBar frequencyBar5;
    private javax.swing.JProgressBar frequencyBar6;
    private javax.swing.JProgressBar frequencyBar7;
    private javax.swing.JProgressBar frequencyBar8;
    private javax.swing.JProgressBar frequencyBar9;
    private javax.swing.JMenu helpMenuItem;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JButton listenButton;
    // End of variables declaration//GEN-END:variables

}
