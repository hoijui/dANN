package com.syncleus.core.dann.dannalyzer.ui;

import java.awt.Color;
import java.awt.image.ImageObserver;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.*;
import org.w3c.dom.*;
import org.apache.batik.util.*;
import org.apache.batik.dom.svg.*;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScalableSVGCanvas extends JSVGCanvas
{
    private static final Pattern translatePattern = Pattern.compile("translate\\(([\\-\\.1234567890]*)[,\\s]*([\\-\\.1234567890]*)\\)");
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
        SVGSVGElement svg = (SVGSVGElement) doc.getRootElement();
        
        svg.removeAttribute("width");
        svg.removeAttribute("height");
        svg.removeAttribute("viewbox");

        Element vectorsElement = (Element) doc.getElementsByTagName("g").item(0);
        
        Matcher translateMatch = translatePattern.matcher(vectorsElement.getAttribute("transform"));
        vectorsElement.removeAttribute("transform");



        GVTBuilder builder = new GVTBuilder();
        BridgeContext context = new BridgeContext(new UserAgentAdapter());
        GraphicsNode gvtRoot = builder.build(context, doc);

        float oldWidth = (float) gvtRoot.getSensitiveBounds().getWidth();
        float oldHeight = (float) gvtRoot.getSensitiveBounds().getHeight();
        
        if( ! this.usingScalar )
        {
            float widthScale = ((float)this.width)/oldWidth;
            float heightScale = ((float)this.height)/oldHeight;
            this.scale = (widthScale < heightScale ? widthScale : heightScale);
        }

        float newWidth = oldWidth * this.scale;
        float newHeight = oldHeight * this.scale;

        
        double translateX = 0.0;
        double translateY = 0.0;
        
        if(translateMatch.find())
        {
            String translateXText = translateMatch.group(1);
            String translateYText = translateMatch.group(2);
            
            translateX = Double.parseDouble(translateXText) * this.scale;
            translateY = Double.parseDouble(translateYText) * this.scale;
        }
        vectorsElement.setAttribute("transform", "translate(" + translateX + ", " + translateY + ") scale(" + this.scale + ")");
        
        if(this.usingScalar)
            this.setSize((int) newWidth, (int) newHeight);
        else
            this.setSize((int)this.width, (int)this.height);
        this.observer.imageUpdate(this.image, ImageObserver.WIDTH|ImageObserver.HEIGHT, this.getX(), this.getY(), (int) newWidth, (int)newHeight);
        
        svg.setAttribute("width", newWidth + "in");
        svg.setAttribute("height", newHeight + "in");
        svg.setAttribute("viewBox", "0 0 " + newWidth + " " + newHeight);
        
        super.installSVGDocument(doc);
    }
}
