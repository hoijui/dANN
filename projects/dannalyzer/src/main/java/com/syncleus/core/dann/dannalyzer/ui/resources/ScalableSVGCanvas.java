package com.syncleus.core.dann.dannalyzer.ui.resources;

import java.awt.Color;
import java.awt.image.ImageObserver;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.*;
import com.syncleus.core.dann.dannalyzer.ui.resources.*;
import java.awt.Dimension;

public class ScalableSVGCanvas extends JSVGCanvas
{
    private float scale;
    private float width;
    private float height;
    private boolean usingScalar;
    private ImageObserver observer;



    public ScalableSVGCanvas(float scale, ImageObserver observer)
    {
        this.scale = scale;
        this.observer = observer;
        this.usingScalar = true;
        
        this.setBackground(new Color(0.5f, 0.5f, 0.5f, 0.0f));
        this.setOpaque(false);
    }
    
    public ScalableSVGCanvas(int width, int height, ImageObserver observer)
    {
        this.width = width;
        this.height = height;
        this.observer = observer;
        this.usingScalar = false;
        
        this.setBackground(new Color(0.5f, 0.5f, 0.5f, 0.0f));
        this.setOpaque(false);
    }
    
    public void setScale(float scale)
    {
        this.scale = scale;
        this.usingScalar = true;
        
        this.setURI(this.uri);
    }
    
    
    public void setScale(float width, float height)
    {
        this.width = width;
        this.height = height;
        this.usingScalar = false;
        
        this.setURI(this.uri);
    }



    protected void installSVGDocument(SVGDocument doc)
    {        
        Dimension newDimensions = null;
        if(this.usingScalar)
            newDimensions = SvgUtility.scale(doc, scale);
            else
            newDimensions = SvgUtility.scale(doc, (int)this.width, (int)this.height);
        
        this.setSize((int) newDimensions.getWidth(), (int) newDimensions.getHeight());

        this.observer.imageUpdate(this.image, ImageObserver.WIDTH|ImageObserver.HEIGHT, this.getX(), this.getY(), (int) newDimensions.getWidth(), (int) newDimensions.getHeight());
        
        super.installSVGDocument(doc);
    }
}
