package com.syncleus.core.dann.examples.nci.ui;
import com.syncleus.dann.visualization.*;
import java.awt.Frame;
import javax.swing.JDialog;


public class ViewBrain extends JDialog
{
    private AssociativeMapCanvas brainVisual;

    public ViewBrain(Frame parent, AssociativeMapCanvas brainVisual)
    {
        super(parent, false);
        
        initComponents();
        
        this.brainVisual = brainVisual;
        
        this.add(this.brainVisual);
        this.brainVisual.setLocation(0, 0);
        this.brainVisual.setSize(800, 600);
        this.brainVisual.setVisible(true);
        
        this.setSize(800,600);
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
