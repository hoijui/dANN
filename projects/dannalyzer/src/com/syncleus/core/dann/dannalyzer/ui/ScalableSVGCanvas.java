package com.syncleus.core.dann.dannalyzer.ui;

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
    private double scale;
    private ImageObserver observer;



    public ScalableSVGCanvas(double scale, ImageObserver observer)
    {
        this.scale = scale;
        this.observer = observer;
    }
    
    public void setScale(double scale)
    {
        this.scale = scale;
        
        this.setURI(this.uri);
    }



    protected void installSVGDocument(SVGDocument doc)
    {
        SVGSVGElement svg = (SVGSVGElement) doc.getRootElement();

        Element vectorsElement = (Element) doc.getElementsByTagName("g").item(0);



        GVTBuilder builder = new GVTBuilder();
        BridgeContext context = new BridgeContext(new UserAgentAdapter());
        GraphicsNode gvtRoot = builder.build(context, doc);

        double oldWidth = gvtRoot.getSensitiveBounds().getWidth();
        double oldHeight = gvtRoot.getSensitiveBounds().getHeight();

        double newWidth = oldWidth * this.scale;
        double newHeight = oldHeight * this.scale;

        
        double translateX = 0.0;
        double translateY = 0.0;
        
        Matcher translateMatch = translatePattern.matcher(vectorsElement.getAttribute("transform"));
        if(translateMatch.find())
        {
            String translateXText = translateMatch.group(1);
            String translateYText = translateMatch.group(2);
            
            translateX = Double.parseDouble(translateXText) * this.scale;
            translateY = Double.parseDouble(translateYText) * this.scale;
        }
        vectorsElement.setAttribute("transform", "translate(" + translateX + ", " + translateY + ") scale(" + this.scale + ")");
        
        this.setSize((int) newWidth, (int) newHeight);
        this.observer.imageUpdate(this.image, ImageObserver.WIDTH|ImageObserver.HEIGHT, this.getX(), this.getY(), (int) newWidth, (int)newHeight);
        
        svg.setAttribute("width", newWidth + "in");
        svg.setAttribute("height", newHeight + "in");
        svg.setAttribute("viewBox", "0 0 " + newWidth + " " + newHeight);
        
        super.installSVGDocument(doc);
    }
}
