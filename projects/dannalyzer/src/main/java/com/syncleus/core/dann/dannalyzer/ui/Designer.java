package com.syncleus.core.dann.dannalyzer.ui;

import com.syncleus.core.dann.dannalyzer.ui.resources.ScalableSVGCanvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;


public class Designer extends JPanel implements ImageObserver
{
    protected ScalableSVGCanvas test = new ScalableSVGCanvas(0.1f, this);



    public Designer()
    {
        initComponents();
        
        this.setBackground(Color.WHITE);
    }
    
    
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
    {
        if( (infoflags & (ImageObserver.WIDTH|ImageObserver.HEIGHT)) == (ImageObserver.WIDTH|ImageObserver.HEIGHT) )
        {
            int newX = (this.getWidth() > width+x ? this.getWidth() : width+x);
            int newY = (this.getHeight() > height+y ? this.getHeight() : height+y);
            this.setSize(newX, newY);
            this.setPreferredSize(new Dimension(newX, newY));
        
            return false;
        }
        
        return true;
    }



    public void addDesignerElement(DesignerElement newElement)
    {
//        this.setSize(4000, 4000);
//        this.setPreferredSize(new Dimension(4000,4000));
        
        this.test.setURI(this.getClass().getResource("resources/neuron.svg").toString());

        this.test.setVisible(true);
        this.test.setLocation(0, 0);

        this.add(this.test);
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
