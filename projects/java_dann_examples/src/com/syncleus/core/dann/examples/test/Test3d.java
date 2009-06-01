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
package com.syncleus.core.dann.examples.test;


import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.*;
import org.freehep.j3d.plot.*;

public class Test3d extends JFrame
{
    private javax.swing.JPanel drawingPanel;
    //private SimpleUniverse universe = null;
    private BranchGroup origin = new BranchGroup();



    public Test3d()
    {
        this.initComponents();

        Canvas3D canvas = this.createUniverse();
        try
        {
           this.drawingPanel.add(canvas, java.awt.BorderLayout.CENTER);
        }
        catch(ArithmeticException caughtException)
        {
            System.out.println("Division by 0!");
        }
    }



    private Canvas3D createUniverse()
    {
        GlobalSignal signalX = new GlobalSignal();
        GlobalSignal signalY = new GlobalSignal();
        GlobalSignal signalZ = new GlobalSignal();
        SignalProcessingWavelet processor = new SignalProcessingWavelet(new Cell(), signalX, signalZ);
        for(int index = 0;index < 500 ;index++)
        {
            //if(random.nextDouble() < 0.5555556)
            //{
            //    if(random.nextDouble() < 0.5)
            //    {
            processor = processor.mutate(signalX);
            //    }
            //    else
            //    {
            processor = processor.mutate(signalY);
            //    }
            //}

            processor = processor.mutate();
        }
        
        System.out.println("The current equation contains " + processor.getWaveCount() + " waves:");
        System.out.println(processor.toString());

        processor.preTick();
        processor.tick();

        MathFunction3dDataBinder dataBinder = new MathFunction3dDataBinder(
            processor.getWavelet(),
            signalX.getId().toString(),
            signalY.getId().toString(),
            -200.0f,
            200.0f,
            -200.0f,
            200.0f,
            200);


        SurfacePlot plotCanvas = new SurfacePlot();
        plotCanvas.setLogZscaling(false);
        plotCanvas.setData(dataBinder);

        return plotCanvas;
    }



    private void initComponents()
    {
        this.drawingPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hello Universe");
        this.drawingPanel.setLayout(new java.awt.BorderLayout());

        this.drawingPanel.setPreferredSize(new java.awt.Dimension(250, 250));
        this.getContentPane().add(this.drawingPanel, java.awt.BorderLayout.CENTER);

        this.pack();
    }



    public static void main(String[] args)
    {
        java.awt.EventQueue.invokeLater(
            new Runnable()
            {
                public void run()
                {
                    for(int index = 0; index < 1; index++)
                        new Test3d().setVisible(true);
                }
            });
    }
}
