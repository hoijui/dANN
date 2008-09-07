package com.syncleus.core.dann.dannalyzer.ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;


public class Designer extends JPanel
{
    protected ScalableSVGCanvas test = new ScalableSVGCanvas(0.1);



    public Designer()
    {
        initComponents();
        
        this.test.setVisible(true);
//        this.setSize(2000,2000);
//        this.setPreferredSize(new Dimension(2000,2000));
//        this.test.setLocation(0, 0);
//        this.test.setSize(400, 400);
        
//        this.setBackground(Color.BLUE);
    }



    public void addDesignerElement(DesignerElement newElement)
    {
        this.setSize(4000, 4000);
        this.setPreferredSize(new Dimension(4000,4000));
        
        this.test.setURI(this.getClass().getResource("resources/neuron.svg").toString());

        this.test.setVisible(true);
        this.test.setLocation(0, 0);
        this.test.setSize(1500, 1500);
        this.test.setPreferredSize(new Dimension(1500,1500));

        this.add(this.test);

        System.out.println("element added");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
