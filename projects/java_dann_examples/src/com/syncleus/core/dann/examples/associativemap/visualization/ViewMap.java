package com.syncleus.core.dann.examples.associativemap.visualization;

import com.syncleus.dann.*;
import com.syncleus.dann.associativemap.*;
import com.syncleus.dann.visualization.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.swing.JFrame;
import javax.swing.Timer;


public class ViewMap extends JFrame implements ActionListener
{
    private AssociativeMapCanvas mapVisual;
    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private FutureTask<Void> lastRun;



    public ViewMap()
    {
        LayeredAssociativeMap associativeMap = new LayeredAssociativeMap(8);

        AssociativeMapCanvas mapVisual = new AssociativeMapCanvas(associativeMap);

        initComponents();

        this.mapVisual = mapVisual;

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
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
