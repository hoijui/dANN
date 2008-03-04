package com.syncleus.core.dann.examples.test;


import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
import java.util.Random;
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
        this.drawingPanel.add(canvas, java.awt.BorderLayout.CENTER);
    }



    private Canvas3D createUniverse()
    {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Signal signalX = new Signal();
        Signal signalY = new Signal();
        Signal signalZ = new Signal();
        SignalProcessingWavelet processor = new SignalProcessingWavelet(signalX, signalZ);
        Random random = new Random();
        for(int index = 0;index < 50;index++)
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

        processor.updateOutput();

        MathFunction3dDataBinder dataBinder = new MathFunction3dDataBinder(
            processor.getWavelet(),
            signalX.getId().toString(),
            signalY.getId().toString(),
            -100.0f,
            100.0f,
            -100.0f,
            100.0f,
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
                    for(int index = 0; index < 5; index++)
                        new Test3d().setVisible(true);
                }
            });
    }
}
